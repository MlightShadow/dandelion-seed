package com.company.project.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by CodeGenerator on 2019/03/28.
 */
public class AccountDTO {
    @ApiModelProperty(value = "帐号名")
    private String username;

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
}