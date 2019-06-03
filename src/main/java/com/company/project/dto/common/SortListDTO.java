package com.company.project.dto.common;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class SortListDTO {

    @ApiModelProperty(value = "id列表")
    private List<SortDTO> list;

    public List<SortDTO> getList() {
        return list;
    }

    public void setList(List<SortDTO> list) {
        this.list = list;
    }
}