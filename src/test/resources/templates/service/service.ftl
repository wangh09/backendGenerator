package ${packageName};
import ${basePackageName}.model.${modelNameUpperCamel};
import ${utilsPackageName}.AbstractService;
import ${mapperPackageName}.${modelNameUpperCamel}Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
/**
 * Created by ${author} on ${date}.
 */
@Service
@Transactional
public class ${modelNameUpperCamel}Service extends AbstractService<${modelNameUpperCamel}> {
    @Resource
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

}