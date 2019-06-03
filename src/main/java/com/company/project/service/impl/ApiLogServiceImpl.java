package com.company.project.service.impl;

import com.company.project.dao.ApiLogMapper;
import com.company.project.model.ApiLog;
import com.company.project.service.ApiLogService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/04/12.
 */
@Service
@Transactional
public class ApiLogServiceImpl extends AbstractService<ApiLog> implements ApiLogService {
    @Resource
    private ApiLogMapper apiLogMapper;

}
