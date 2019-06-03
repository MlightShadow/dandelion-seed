package com.company.project.web;

import java.util.List;

import javax.annotation.Resource;

import com.company.project.aop.NeedApiLog;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dto.AuthorizedInfo;
import com.company.project.dto.LoginDTO;
import com.company.project.model.Account;
import com.company.project.service.AccountService;
import com.company.project.util.JWTUtil;
import com.company.project.util.MD5Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import tk.mybatis.mapper.entity.Condition;

/**
 * 账号认证控制器
 */
@Api(description = "登录管理")
@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    private MD5Util MD5;

    @Autowired
    private JWTUtil jwtUtil;

    @Resource
    private AccountService accountService;

    //@NeedApiLog 没数据库就不log了
    @PostMapping("/take")
    public Result<?> take(@RequestBody LoginDTO loginInfo) {
        Condition condition = new Condition(Account.class);
        try {
            condition.createCriteria()
                .andCondition("username=", loginInfo.getUsername())
                .andCondition("password=", MD5.md5(loginInfo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("登录失败");
        }
        //登录演示
        //List<Account> list = accountService.findByCondition(condition);
        Account account = new Account();
        if (/*list.size() == 1*/ true) {
            //account = list.get(0);

            account.setId("123");
            account.setUsername("root");
            account.setPassword("root");
            AuthorizedInfo authInfo = new AuthorizedInfo(account.getId(), account.getUsername(), account.getPassword());
            String token = jwtUtil.generateAccessToken(authInfo);
            jwtUtil.putToken(account.getId(), token);
            return ResultGenerator.genSuccessResult(token);

        } else {
            return ResultGenerator.genFailResult("登录失败");
        }

    }

    @GetMapping("validate")
    public Result<?> test() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = "";
        if (principal instanceof UserDetails) {

            name = ((UserDetails) principal).getUsername();

        }

        return ResultGenerator.genSuccessResult(name + "你已通过验证");
    }
}
