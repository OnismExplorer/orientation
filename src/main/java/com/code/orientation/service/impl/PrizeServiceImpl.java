package com.code.orientation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.code.orientation.common.Result;
import com.code.orientation.constants.CodeEnum;
import com.code.orientation.entity.ExchangeLog;
import com.code.orientation.entity.Prize;
import com.code.orientation.entity.Student;
import com.code.orientation.exception.CustomException;
import com.code.orientation.mapper.PrizeMapper;
import com.code.orientation.service.ExchangeLogService;
import com.code.orientation.service.PrizeService;
import com.code.orientation.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description 针对表【prize】的数据库操作Service实现
 * @createDate 2024-01-14 16:18:12
 */
@Service
public class PrizeServiceImpl extends ServiceImpl<PrizeMapper, Prize>
        implements PrizeService{

    private final ExchangeLogService exchangeLogService;
    private final StudentService studentService;

    @Autowired
    public PrizeServiceImpl(ExchangeLogService exchangeLogService, StudentService studentService) {
        this.exchangeLogService = exchangeLogService;
        this.studentService = studentService;
    }

    @Override
    public Result<String> exchange(Long uid,Long id) {
        Prize prize = getById(id);
        if(prize == null) {
            throw new CustomException(CodeEnum.NOT_FOUND_PRIZE);
        }
        // 判断是否还有库存
        if(prize.getInventory() <= 0 || prize.getState() == 0){
            return Result.fail(260,"奖品已兑完");
        }
        // 修改库存数量
        prize.setInventory(prize.getInventory() - 1);
        if(prize.getInventory() == 0){
            prize.setState(0);
        }
        Student student = studentService.lambdaQuery().eq(Student::getUid,uid).one();
        // 判断学生积分余额是否足够
        if(student.getPoints() < prize.getPrice()){
            return Result.fail(261,"积分余额不足");
        }
        // 扣除对应积分数
        studentService.updateById(student.setPoints(student.getPoints() - prize.getPrice()));
        // 添加兑换记录
        exchangeLogService.save(new ExchangeLog().setUid(uid).setPrizeId(prize.getId()));
        updateById(prize);
        return Result.success();
    }
}




