package com.company.project.dto.common;

import io.swagger.annotations.ApiModelProperty;

public class IdDTO {

    @ApiModelProperty(value = "id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}