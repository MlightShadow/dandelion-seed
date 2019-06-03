package com.company.project.web;

import com.company.project.aop.NeedApiLog;
import com.company.project.aop.TargetDataSource;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dto.AccountDTO;
import com.company.project.model.Account;
import com.company.project.service.AccountService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;

import java.util.List;

/**
 * Created by CodeGenerator on 2019/03/28.
 */
@Api(description = "后台账号")
@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @NeedApiLog
    @ApiOperation(value = "新增 默认password: 123456")
    @PostMapping
    public Result<?> add(@RequestBody AccountDTO dto) {
        return ResultGenerator.genSuccessResult(accountService.addAccount(dto));
    }

    @NeedApiLog
    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id) {
        return ResultGenerator.genSuccessResult(accountService.deleteAccount(id));
    }

    @NeedApiLog
    @ApiOperation(value = "更新")
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable String id, @RequestBody AccountDTO dto) {
        return ResultGenerator.genSuccessResult(accountService.updateAccount(id, dto));
    }

    @ApiIgnore
    @ApiOperation(value = "数据源测试")
    @GetMapping("test/{type}")
    @TargetDataSource("auth")
    @NeedApiLog
    public Result<?> test(@PathVariable String type) {
        return ResultGenerator.genSuccessResult("okay");
    }

    @ApiOperation(value = "详情")
    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable String id) {
        Account account = accountService.findById(id);
        return ResultGenerator.genSuccessResult(account);
    }

    @ApiOperation(value = "列表")
    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        Condition condition = new Condition(Account.class);
        condition.createCriteria().andCondition("is_deleted=", false);
        List<Account> list = accountService.findByCondition(condition);
        PageInfo<?> pageInfo = new PageInfo<Account>(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
