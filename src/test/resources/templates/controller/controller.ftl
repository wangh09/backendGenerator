package ${packageName};
import ${utilsPackageName}.Result;
import ${utilsPackageName}.ResultGenerator;
import ${basePackageName}.model.${modelNameUpperCamel};
import ${servicePackageName}.${modelNameUpperCamel}Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* Created by ${author} on ${date}.
*/
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller {
    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @PostMapping("/add")
    public Result add(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.save(${modelNameLowerCamel});
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) Integer id) {
        ${modelNameLowerCamel}Service.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.update(${modelNameLowerCamel});
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) Integer id) {
        ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.get(id);
        return ResultGenerator.genSuccessResult(${modelNameLowerCamel});
    }

    @PostMapping("/list")
    public Result list(@RequestBody ${modelNameUpperCamel} object,
        @RequestParam(defaultValue="0") Integer page, @RequestParam(defaultValue="0") Integer size) {
        if(page != 0 && size !=0) {
            PageHelper.startPage(page, size);
        }
        Condition condition = new Condition(${modelNameUpperCamel}.class);
        Example.Criteria criteria = condition.createCriteria();
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.list(condition);
        if(page != 0 && size !=0) {
            PageInfo pageInfo = new PageInfo(list);
            Map<String,Object> result = new HashMap<String,Object>();
            result.put("list",list);
            result.put("total",pageInfo.getTotal());
            return ResultGenerator.genSuccessResult(result);
        } else {
            return ResultGenerator.genSuccessResult(list);
        }
    }
}
