package com.company.project.dto.common;

import io.swagger.annotations.ApiModelProperty;

public class SortDTO extends IdDTO {

    @ApiModelProperty(value = "sort")
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}