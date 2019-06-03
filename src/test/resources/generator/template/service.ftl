package ${basePackage}.service;

import ${basePackage}.model.${modelNameUpperCamel};
import ${basePackage}.dto.${modelNameUpperCamel}DTO;
import ${basePackage}.core.Service;

/**
 * Created by ${author} on ${date}.
 */
public interface ${modelNameUpperCamel}Service extends Service<${modelNameUpperCamel}> {
    ${modelNameUpperCamel} add${modelNameUpperCamel}(${modelNameUpperCamel}DTO dto);

    int update${modelNameUpperCamel}(String id, ${modelNameUpperCamel}DTO dto);
    
    int delete${modelNameUpperCamel}(String id);
}
