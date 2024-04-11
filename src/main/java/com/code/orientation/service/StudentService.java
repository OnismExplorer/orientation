package com.code.orientation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.code.orientation.entity.Student;
import com.code.orientation.entity.dto.RegisterDTO;
import com.code.orientation.entity.dto.StudentExcelDTO;
import com.code.orientation.entity.vo.StudentVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author HeXin
 * @description 针对表【student】的数据库操作Service
 * @createDate 2023-12-28 13:00:13
 */
public interface StudentService extends IService<Student> {

    IPage<StudentVO> page(Long pageNum, Long pageSize, String key, Integer type, Boolean isAdmin);

    List<StudentVO> rankList();

    /**
     * 创建学生用户
     *
     * @param studentExcelDTO 学生 Excel DTO
     */
    Boolean createStudentUser(StudentExcelDTO studentExcelDTO);

    /**
     * Excel上传学生
     *
     * @param file 文件
     * @return {@link Boolean}
     */
    Boolean upload(MultipartFile file);

    /**
     * 注册
     *
     * @param registerDTO 寄存器DTO
     * @return {@link Boolean}
     */
    Boolean register(RegisterDTO registerDTO);

    /**
     * 通过UID获取
     *
     * @param uid UID 接口
     * @return {@link StudentVO}
     */
    StudentVO getByUID(Long uid);
}
