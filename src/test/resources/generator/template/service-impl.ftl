package ${basePackage}.service.impl;

import ${basePackage}.dao.${modelNameUpperCamel}Mapper;
import ${basePackage}.dto.${modelNameUpperCamel}DTO;
import ${basePackage}.model.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import ${basePackage}.util.SecurityContextUtil;
import ${basePackage}.util.UUIDUtil;
import ${basePackage}.core.AbstractService;
import ${basePackage}.core.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * Created by ${author} on ${date}.
 */
@Service
@Transactional
public class ${modelNameUpperCamel}ServiceImpl extends AbstractService<${modelNameUpperCamel}> implements ${modelNameUpperCamel}Service {
    @Resource
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

    @Autowired
    private SecurityContextUtil securityContextUtil;

    @Autowired
    private UUIDUtil uuid;

    @Override
    public ${modelNameUpperCamel} add${modelNameUpperCamel}(${modelNameUpperCamel}DTO dto){
        ${modelNameUpperCamel} ${modelNameLowerCamel} = new ${modelNameUpperCamel}();
        ${modelNameLowerCamel}.setId(uuid.getUUID());
        ${modelNameLowerCamel}.setIsDeleted(false);
        ${modelNameLowerCamel}.setCreateTime(new Date());
        ${modelNameLowerCamel}.setCreator(securityContextUtil.getAuthInfo().getId());

        int rows = this.save(${modelNameLowerCamel});
        if (rows == 1) {
            return ${modelNameLowerCamel};
        } else {
            throw new ServiceException("${modelNameLowerCamel}添加失败");
        }
    }

    @Override
    public int update${modelNameUpperCamel}(String id, ${modelNameUpperCamel}DTO dto){
        ${modelNameUpperCamel} ${modelNameLowerCamel} = new ${modelNameUpperCamel}();
        ${modelNameLowerCamel}.setId(id);

        int rows = this.update(${modelNameLowerCamel});
        if (rows == 1) {
            return rows;
        } else {
            throw new ServiceException("${modelNameLowerCamel}更新失败");
        }
    }

    @Override
    public int delete${modelNameUpperCamel}(String id){
        ${modelNameUpperCamel} ${modelNameLowerCamel} = new ${modelNameUpperCamel}();
        ${modelNameLowerCamel}.setId(id);
        ${modelNameLowerCamel}.setIsDeleted(true);
        int rows = this.update(${modelNameLowerCamel});
        if (rows == 1) {
            return rows;
        } else {
            throw new ServiceException("${modelNameLowerCamel}删除失败");
        }
    }
}
