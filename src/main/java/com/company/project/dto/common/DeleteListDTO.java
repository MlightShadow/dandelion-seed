package com.company.project.dto.common;

import java.util.List;

import com.company.project.dto.common.IdDTO;

import io.swagger.annotations.ApiModelProperty;

public class DeleteListDTO {

    @ApiModelProperty(value = "id列表")
    private List<IdDTO> list;

    public List<IdDTO> getList() {
        return list;
    }

    public void setList(List<IdDTO> list) {
        this.list = list;
    }
}