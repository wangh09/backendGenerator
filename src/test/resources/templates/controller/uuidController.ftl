package ${packageName};
import ${utilsPackageName}.Result;
import ${utilsPackageName}.ResultGenerator;
import ${basePackageName}.model.${modelNameUpperCamel};
import ${servicePackageName}.${modelNameUpperCamel}Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import ${utilsPackageName}.StateUtils;
import ${utilsPackageName}.TextUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        ${modelNameLowerCamel}.setId(TextUtils.getIdByUUID());
        ${modelNameLowerCamel}.setGlobalStateType(StateUtils.STATE_NORMAL);
        ${modelNameLowerCamel}.setCreateTime(TextUtils.getNowTime());

        ${modelNameLowerCamel}Service.save(${modelNameLowerCamel});
        return ResultGenerator.genSuccessResult(${modelNameLowerCamel});
    }

    @PostMapping("/add-batch")
    public Result addBatch(@RequestBody List<${modelNameUpperCamel}> ${modelNameLowerCamel}) {
        for (${modelNameUpperCamel} api: ${modelNameLowerCamel}) {
            api.setGlobalStateType(StateUtils.STATE_NORMAL);
            api.setCreateTime(TextUtils.getNowTime());
        }
        ${modelNameLowerCamel}Service.save(${modelNameLowerCamel});
        return ResultGenerator.genSuccessResult(${modelNameLowerCamel});
    }
    @GetMapping("/delete")
    public Result delete(@RequestParam(required = true) String id) {
        ${modelNameLowerCamel}Service.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/edit")
    public Result update(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.update(${modelNameLowerCamel});
        return ResultGenerator.genSuccessResult(${modelNameLowerCamel});
    }

    @GetMapping("/get")
    public Result detail(@RequestParam(required = true) String id) {
        ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.get(id);
        return ResultGenerator.genSuccessResult(${modelNameLowerCamel});
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "null") Map<String,Object> params) {
        Integer page = 0,size = 0;
        Condition condition = new Condition(${modelNameUpperCamel}.class);
        Example.Criteria criteria = condition.createCriteria();
        if(params != null) {
            for(String key: params.keySet()) {
                switch (key) {
                    case "page":
                        page = Integer.parseInt((String)params.get("page"));
                        break;
                    case "size":
                        size = Integer.parseInt((String)params.get("size"));
                        break;
                    default:
                    {
                        String name = TextUtils.camelToUnderline(key);
                        criteria.andCondition(name +"='"+params.get(name)+"'");
                    }
                        break;
                }
            }
        }
        if(page != 0 && size !=0) {
            PageHelper.startPage(page, size);
        }
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
