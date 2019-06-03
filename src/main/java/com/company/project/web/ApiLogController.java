package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.ApiLog;
import com.company.project.service.ApiLogService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

import java.util.List;

/**
 * Created by CodeGenerator on 2019/04/12.
 */
@ApiIgnore
@Api(description = "接口请求日志")
@RestController
@RequestMapping("/api/log")
public class ApiLogController {
    @Resource
    private ApiLogService apiLogService;

    @ApiOperation(value = "详情")
    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable String id) {
        ApiLog apiLog = apiLogService.findById(id);
        return ResultGenerator.genSuccessResult(apiLog);
    }

    @ApiOperation(value = "列表")
    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ApiLog> list = apiLogService.findAll();
        PageInfo<?> pageInfo = new PageInfo<ApiLog>(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
