package com.company.project.service.impl;

import javax.annotation.Resource;

import com.company.project.core.AbstractService;
import com.company.project.dao.AccountMapper;
import com.company.project.dto.AuthorizedInfo;
import com.company.project.model.Account;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component(value="CustomUserDetailsService")
public class CustomUserDetailsServiceImpl extends AbstractService<Account>  implements UserDetailsService {
    @Resource
    private AccountMapper accountMapper;

    @Override
    public AuthorizedInfo loadUserByUsername(String name) throws UsernameNotFoundException {
        AuthorizedInfo a = new AuthorizedInfo("12301","123","123");
        return a;
    }
}
