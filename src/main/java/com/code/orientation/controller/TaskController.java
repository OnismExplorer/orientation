package com.code.orientation.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.code.orientation.common.PageInfo;
import com.code.orientation.common.Result;
import com.code.orientation.controller.base.BaseController;
import com.code.orientation.entity.Task;
import com.code.orientation.entity.dto.TaskDTO;
import com.code.orientation.entity.vo.TaskVO;
import com.code.orientation.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "任务模块")
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController<TaskService, Task, TaskDTO, Long> {

    @Autowired
    private TaskService taskService;

    @Override
    @Operation(summary = "id查询")
    @GetMapping("/{id}")
    @SaCheckPermission("student.task.get")
    public Result<Task> get(@PathVariable Long id) {
        return super.get(id);
    }

    @Operation(summary = "任务分页查询",description = "type为排序标准，默认按积分降序(-1)，0和1表示按开始时间升降序，2和3表示按结束时间升降序，4按积分升序")
    @GetMapping("/page")
    public Result<PageInfo<TaskVO>> page(@RequestParam(required = false,defaultValue = "1")Long pageNum,
                                         @RequestParam(required = false,defaultValue = "10")Long pageSize,
                                         @RequestParam(required = false)String key,
                                         @RequestParam(required = false,defaultValue = "-1")Integer type){
        return Result.page(taskService.page(pageNum,pageSize,key,type));
    }

    @Operation(summary = "开始任务")
    @PostMapping("/begin/{uid}/{id}")
    public Result<String> begin(@PathVariable Long uid, @PathVariable Long id){
        return taskService.begin(uid,id);
    }

    @Operation(summary = "完成任务")
    @PostMapping("/finish/{id}/{uid}")
    public Result<String> finish(@PathVariable Long id,@PathVariable Long uid,
                                 @RequestParam(required = false) String material){
        return taskService.finish(id,uid,material);
    }
    @Override
    protected Class<Task> createInstance() {
        return Task.class;
    }
}
