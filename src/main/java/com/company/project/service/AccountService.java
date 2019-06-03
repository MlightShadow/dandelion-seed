package com.company.project.service;

import com.company.project.model.Account;


import com.company.project.core.Service;
import com.company.project.dto.AccountDTO;

/**
 * Created by CodeGenerator on 2019/03/28.
 */

public interface AccountService extends Service<Account> {
    Account addAccount(AccountDTO dto);

    int updateAccount(String id, AccountDTO dto);

    int deleteAccount(String id);
}