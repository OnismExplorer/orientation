package com.code.orientation.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.code.orientation.constants.CodeEnum;
import com.code.orientation.entity.*;
import com.code.orientation.entity.dto.RegisterDTO;
import com.code.orientation.entity.dto.StudentExcelDTO;
import com.code.orientation.entity.vo.StudentVO;
import com.code.orientation.exception.CustomException;
import com.code.orientation.listener.StudentListener;
import com.code.orientation.mapper.StudentMapper;
import com.code.orientation.service.*;
import com.code.orientation.utils.PasswordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description 针对表【student】的数据库操作Service实现
 * @createDate 2023-12-28 13:00:13
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
        implements StudentService {


    private final AcademyService academyService;

    private final UserService userService;

    private final UserRoleService userRoleService;

    private final RoleService roleService;

    @Lazy
    @Autowired
    public StudentServiceImpl(AcademyService academyService, UserService userService, UserRoleService userRoleService, RoleService roleService) {
        this.academyService = academyService;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
    }

    @Override
    public IPage<StudentVO> page(Long pageNum, Long pageSize, String key, Integer type, Boolean isAdmin) {
        // 查询关键词对应的学院信息
        List<Long> ids = academyService.lambdaQuery().select(Academy::getId).like(StringUtils.isNotBlank(key), Academy::getName, key).list()
                .stream()
                .map(Academy::getId)
                .toList();
        LambdaQueryChainWrapper<Student> wrapper = lambdaQuery().like(StringUtils.isNotBlank(key), Student::getName, key)
                .or()
                .in(CollUtil.isNotEmpty(ids),Student::getDepartmentId,ids);
        // 按type排序
        switch (type) {
            case -1 -> wrapper.orderByDesc(Student::getPoints);
            case 0 -> wrapper.orderByAsc(Student::getName);
            case 1 -> wrapper.orderByDesc(Student::getName);
            default ->
                // 处理未知的 type
                    throw new CustomException("Unknown type: " + type);
        }
        // 获取学生所在学院名称
        Page<Student> page = wrapper.page(new Page<>(pageNum, pageSize));
        Map<Long, String> academyMap = academyService.lambdaQuery().select(Academy::getName, Academy::getId)
                .in(Academy::getId,  page.getRecords().stream().map(Student::getDepartmentId).toList())
                .list().stream()
                .collect(Collectors.toMap(Academy::getId, Academy::getName));
        Map<Long, User> userMap = userService.lambdaQuery().select(User::getId, User::getAvatar)
                .in(User::getId, page.getRecords().stream().map(Student::getUid).toList())
                .list().stream()
                .collect(Collectors.toMap(User::getId, user ->user));
        if(Boolean.TRUE.equals(isAdmin)){
            return page.convert(student -> new StudentVO()
                    .setId(student.getId())
                    .setName(student.getName())
                    .setSex(student.getSex() == 1 ? '男' : '女')
                    .setAvatar(userMap.get(student.getUid()).getAvatar())
                    .setState(student.getState() == 1 ? "已报道" : "未报道")
                    .setPoints(student.getPoints())
                    .setContact(student.getContact())
                    .setIdentityCard(student.getIdentityCard())
                    .setAdmissionLetter(student.getAdmissionLetter())
                    .setDepartment(academyMap.get(student.getDepartmentId())));
        }
        return page.convert(student -> new StudentVO()
                .setId(student.getId())
                .setName(student.getName())
                .setSex(student.getSex() == 1 ? '男' : '女')
                .setAvatar(userMap.get(student.getUid()).getAvatar())
                .setState(student.getState() == 1 ? "已报道" : "未报道")
                .setPoints(student.getPoints())
                .setDepartment(academyMap.get(student.getDepartmentId())));
    }

    @Override
    public List<StudentVO> rankList() {
        List<Student> list = lambdaQuery().orderByDesc(Student::getPoints).list();
        // 获取学生uid
        List<Long> uidList = list.stream().map(Student::getUid).toList();
        // 获取学生学院id
        List<Long> departmentIdList = list.stream().map(Student::getDepartmentId).toList();
        // 获取学生头像
        List<User> users = userService.listByIds(uidList);
        Map<Long, String> userMap = users.stream().collect(Collectors.toMap(User::getId, User::getAvatar ));
        // 获取学生学院
        Map<Long, String> academyMap = academyService.listByIds(departmentIdList).stream().collect(Collectors.toMap(Academy::getId, Academy::getName));
        return list.stream().map(student -> new StudentVO()
                .setId(student.getId())
                .setName(student.getName())
                .setSex(student.getSex() == 1 ? '男' : '女')
                .setAvatar(userMap.get(student.getUid()))
                .setState(student.getState() == 1 ? "已报道" : "未报道")
                .setPoints(student.getPoints())
                .setDepartment(academyMap.get(student.getDepartmentId()))
        ).toList();
    }

    @Override
    @Transactional(rollbackFor = CustomException.class)
    public Boolean createStudentUser(StudentExcelDTO studentExcelDTO) {
        Student student = toStudent(studentExcelDTO);
        // 创建对应用户信息
        String password = "xdzn" + student.getIdentityCard().substring(student.getIdentityCard().length() - 6);
        User user = new User()
                .setUserName(student.getName())
                .setNickname(student.getName())
                .setPassword(PasswordUtils.encrypt(password))
                .setType(0)
                .setPhone(student.getContact().get("phone"))
                .setState(0)
                .setEmail(student.getContact().get("email"));
        boolean u = userService.save(user);
        student.setUid(user.getId());
        boolean s = save(student);
        // 添加学生权限
        Role role = roleService.lambdaQuery().eq(Role::getRoleKey, "student").one();
        if(role == null) {
            throw new CustomException(CodeEnum.NOT_FOUND_ROLE);
        }
        UserRole userRole = new UserRole()
                .setRoleId(role.getId())
                .setUserId(user.getId());
        boolean r = userRoleService.save(userRole);
        return u && s && r;
    }

    @Override
    public Boolean upload(MultipartFile file) {
        try {
            EasyExcelFactory.read(file.getInputStream(),new StudentListener(this))
                    .head(StudentExcelDTO.class).sheet().doRead();
        } catch (IOException e) {
            throw new CustomException(CodeEnum.SYSTEM_ERROR);
        }
        return true;
    }

    @Override
    public Boolean register(RegisterDTO registerDTO) {
        // 获取学院
        Academy academy = academyService.lambdaQuery().eq(Academy::getName, registerDTO.getDepartmentName()).one();
        if(academy == null) {
            throw new CustomException(CodeEnum.NOT_FOUND_ACADEMY);
        }
        // 获取学生信息
        Student student = lambdaQuery().eq(Student::getName, registerDTO.getUsername())
                .eq(Student::getAdmissionLetter, registerDTO.getAdmissionLetter())
                .eq(Student::getIdentityCard, registerDTO.getIdentityCard())
                .eq(Student::getDepartmentId, academy.getId()).one();
        if(student == null){
            throw new CustomException(CodeEnum.NOT_FOUND_STUDENT);
        }
        // 获取学生对应用户信息
        User user = userService.getById(student.getUid());
        // 如果已经报道
        if(student.getState().equals(1) || user.getState().equals(1)) {
            throw new CustomException(CodeEnum.ALREADY_REGISTERED);
        }
        return userService.updateById(user.setState(1));
    }

    @Override
    public StudentVO getByUID(Long uid) {
        // 查询
        Student student = lambdaQuery().eq(Student::getUid, uid).one();
        if(student == null) {
            throw new CustomException(CodeEnum.NOT_FOUND_STUDENT);
        }
        User user = userService.getById(uid);
        // 学院信息
        Academy academy = academyService.getById(student.getDepartmentId());
        StudentVO studentVO = new StudentVO();
        studentVO.setAvatar(user.getAvatar())
                .setPoints(student.getPoints())
                .setId(student.getId())
                .setSex(student.getSex() == 0 ? '女':'男')
                .setName(student.getName())
                .setDepartment(academy.getName())
                .setState(student.getState() == 1 ? "已报道" : "未报道")
                .setAdmissionLetter(student.getAdmissionLetter())
                .setContact(student.getContact())
                .setIdentityCard(student.getIdentityCard());
        return studentVO;
    }

    /**
     * 转学生对象
     *
     * @param dto DTO
     * @return {@link Student}
     */
    private Student toStudent(StudentExcelDTO dto) {
        Student student = new Student();
        // 获取学院信息
        Academy academy = academyService.lambdaQuery().eq(Academy::getName, dto.getDepartment()).one();
        if(academy == null) {
            throw new CustomException(CodeEnum.NOT_FOUND_ACADEMY);
        }
        Map<String,String> map = new HashMap<>();
        map.put("phone",dto.getPhone());
        map.put("email",dto.getEmail());
        String sex = dto.getSex();
        if(!sex.equals("男") && !sex.equals("女")){
            throw new CustomException(CodeEnum.UNKNOWN_GENDER);
        }
        return student.setContact(map)
                .setName(dto.getName())
                .setIdentityCard(dto.getIdentityCard())
                .setSex(dto.getSex().equals("男") ? 1 : 0)
                .setDepartmentId(academy.getId());
    }
}




