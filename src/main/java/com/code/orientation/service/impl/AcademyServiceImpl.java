package com.code.orientation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.code.orientation.entity.Academy;
import com.code.orientation.entity.Student;
import com.code.orientation.entity.vo.StudentCount;
import com.code.orientation.mapper.AcademyMapper;
import com.code.orientation.service.AcademyService;
import com.code.orientation.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author HeXin
* @description 针对表【academy】的数据库操作Service实现
* @createDate 2024-04-06 22:43:51
*/
@Service
public class AcademyServiceImpl extends ServiceImpl<AcademyMapper, Academy>
    implements AcademyService{

    private final StudentService studentService;

    @Lazy
    @Autowired
    public AcademyServiceImpl(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public StudentCount count(Long id) {
        StudentCount count = new StudentCount();
        List<Student> studentList = studentService.lambdaQuery().eq(Student::getDepartmentId, id)
                .select(Student::getState).list();
        // 设置实际总人数
        count.setTotal(studentList.size());
        Map<Integer, List<Student>> studentMap = studentList.stream().collect(Collectors.groupingBy(Student::getState));
        // 设置已报道人数
        count.setReported(studentMap.get(1).size());
        // 设置未报道人数
        count.setUnreport(studentMap.get(0).size());
        return count;
    }
}




