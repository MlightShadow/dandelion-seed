package com.company.project.service.impl;

import com.company.project.dao.AccountMapper;
import com.company.project.dto.AccountDTO;
import com.company.project.model.Account;
import com.company.project.service.AccountService;
import com.company.project.util.MD5Util;
import com.company.project.util.UUIDUtil;
import com.company.project.core.AbstractService;
import com.company.project.core.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * Created by CodeGenerator on 2019/03/28.
 */
@Service
@Transactional
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {
    @Resource
    private AccountMapper accountMapper;

    @Autowired
    private UUIDUtil uuid;

    @Autowired
    private MD5Util MD5;

    @Override
    public Account addAccount(AccountDTO dto) {
        
        // 判断用户名是否存在
        Condition condition = new Condition(Account.class);
        condition.createCriteria().andCondition("username=", dto.getUsername());
        List<Account> list = this.findByCondition(condition);

        if(list.size() > 0){
            throw new ServiceException("用户名已存在");
        }

        Account account = new Account();
        account.setId(uuid.getUUID());
        account.setIsDeleted(false);
        account.setCreateTime(new Date());

        account.setUsername(dto.getUsername());
        try {
            account.setPassword(MD5.md5("123456"));
        } catch (Exception e) {
            throw new ServiceException("系统异常");
        }

        int rows = this.save(account);
        if (rows == 1) {
            return account;
        } else {
            throw new ServiceException("account添加失败");
        }
    }

    @Override
    public int updateAccount(String id, AccountDTO dto) {
        Account account = new Account();
        account.setId(id);
        account.setUsername(dto.getUsername());

        int rows = this.update(account);
        if (rows == 1) {
            return rows;
        } else {
            throw new ServiceException("account更新失败");
        }
    }

    @Override
    public int deleteAccount(String id) {
        Account account = new Account();
        account.setId(id);
        account.setIsDeleted(true);
        int rows = this.update(account);
        if (rows == 1) {
            return rows;
        } else {
            throw new ServiceException("account删除失败");
        }
    }

}
