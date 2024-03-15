package com.code.orientation.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.code.orientation.constants.CodeEnum;
import com.code.orientation.entity.dto.StudentExcelDTO;
import com.code.orientation.exception.CustomException;
import com.code.orientation.service.StudentService;

public class StudentListener implements ReadListener<StudentExcelDTO> {

    /**
     * 身份证正则
     */
    private static final String IDENTITY_CARD_REGULAR = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";

    /**
     * 手机号正则
     */
    private static final String PHONE_REGULAR = "^1[3-9]\\d{9}$";

    /**
     * 电子邮件正则
     */
    private static final String EMAIL_REGULAR = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private final StudentService studentService;

    public StudentListener(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void invoke(StudentExcelDTO studentExcelDTO, AnalysisContext analysisContext) {
        String name = studentExcelDTO.getName();
        String identityCard = studentExcelDTO.getIdentityCard();
        String department = studentExcelDTO.getDepartment();
        String phone = studentExcelDTO.getPhone();
        String email = studentExcelDTO.getEmail();
        // 校验字段
        if(StringUtils.isBlank(name) || StringUtils.isBlank(identityCard) || StringUtils.isBlank(department)
        || !StringUtils.matches(IDENTITY_CARD_REGULAR,identityCard)
        || !StringUtils.matches(PHONE_REGULAR,phone)
        || !StringUtils.matches(EMAIL_REGULAR,email)) {
            throw new CustomException(CodeEnum.IMPORT_ERROR);
        }
        studentService.createStudentUser(studentExcelDTO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // do nothing
    }
}
