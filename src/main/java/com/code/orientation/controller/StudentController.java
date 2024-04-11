package com.code.orientation.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.code.orientation.common.PageInfo;
import com.code.orientation.common.Result;
import com.code.orientation.constants.CodeEnum;
import com.code.orientation.controller.base.BaseController;
import com.code.orientation.entity.Student;
import com.code.orientation.entity.User;
import com.code.orientation.entity.dto.RegisterDTO;
import com.code.orientation.entity.dto.StudentDTO;
import com.code.orientation.entity.dto.StudentExcelDTO;
import com.code.orientation.entity.vo.StudentVO;
import com.code.orientation.service.StudentService;
import com.code.orientation.service.UserService;
import com.code.orientation.utils.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生管理
 * @author HeXin
 * @date 2024/04/10
 */
@Tag(name = "学生模块")
@RestController
@RequestMapping("/student")
public class StudentController extends BaseController<StudentService, Student, StudentDTO, Long> {

    private final StudentService studentService;

    private final UserService userService;

    @Autowired
    public StudentController(StudentService studentService, UserService userService) {
        this.studentService = studentService;
        this.userService = userService;
    }

    @Override
    @Operation(summary = "保存学生信息")
    @PostMapping()
    @SaCheckPermission(value = {"admin.student.add","teacher.student.add"},mode = SaMode.OR)
    public Result<Void> save(@RequestBody StudentDTO instance) {
        // 判断该用户是否存在
        User user = userService.getById(instance.getUid());
        if(user == null || user.getState().equals(0)) {
            return Result.fail(CodeEnum.USER_NOT_EXIST);
        }
        return Result.isSuccess(studentService.save(instance.toPo(Student.class)));
    }

    @Operation(summary = "分页查询学生信息(非管理员)",description = "key为搜索关键词(姓名、学院)，type为排序规则，默认-1按学生积分数排序，0和1按学生姓名升降序排序，为null的字段是不对外展示的(权限不够)")
    @GetMapping("/page")
    public Result<PageInfo<StudentVO>> page(@RequestParam(required = false,defaultValue = "1")Long pageNum,
                                            @RequestParam(required = false,defaultValue = "10")Long pageSize,
                                            @RequestParam(required = false)String key,
                                            @RequestParam(required = false,defaultValue = "-1")Integer type){
        return Result.page(studentService.page(pageNum,pageSize,key,type,false));
    }

    @Operation(summary = "分页查询学生信息(管理员)",description = "key为搜索关键词(姓名、学院)，type为排序规则，默认-1按学生积分数排序，0和1按学生姓名升降序排序")
    @SaCheckPermission("admin.student.get")
    @GetMapping("/admin/page")
    public Result<PageInfo<StudentVO>> pageforAdmin(@RequestParam(required = false,defaultValue = "1")Long pageNum,
                                          @RequestParam(required = false,defaultValue = "10")Long pageSize,
                                          @RequestParam(required = false)String key,
                                          @RequestParam(required = false,defaultValue = "-1")Integer type){
        return Result.page(studentService.page(pageNum,pageSize,key,type,true));
    }

    @Operation(summary = "完成报道")
    @PutMapping("/report/{id}")
    public Result<String> report(@PathVariable("id")Long id){
        studentService.updateById(studentService.getById(id).setState(1));
        return Result.success();
    }

    @Operation(summary = "Excel导入学生信息")
    @SaCheckPermission(value = {"admin.student.upload","teacher.student.upload"},mode = SaMode.OR)
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam MultipartFile file) {
        return Result.isSuccess(studentService.upload(file));
    }

    @Operation(summary = "获取导入模版")
    @GetMapping("/download/template")
    public void downloadTemplate(HttpServletResponse response){
        ExcelUtil.write(response,"学生信息导入模版", StudentExcelDTO.class,new ArrayList<StudentExcelDTO>());
    }
    @Override
    protected Class<Student> createInstance() {
        return  Student.class;
    }

    @Operation(summary = "学生列表(已按积分排序)",description = "默认按照学生积分数降序排序，隐私字段为null已过滤，不对外展示")
    @GetMapping("/rank/list")
    public Result<List<StudentVO>> rankList() {
        return Result.success(studentService.rankList());
    }

    @Operation(summary = "学生注册")
    @PatchMapping("/register")
    public Result<String> register(@RequestBody RegisterDTO registerDTO){
        return Result.isSuccess(studentService.register(registerDTO));
    }

    @Operation(summary = "根据uid查询学生信息")
    @GetMapping("/student/{uid}")
    public Result<StudentVO> getByUID(@PathVariable Long uid) {
        return Result.success(studentService.getByUID(uid));
    }
}
