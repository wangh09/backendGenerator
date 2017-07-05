package ${packageName};
import ${utilsPackageName}.Result;
import ${utilsPackageName}.ResultGenerator;
import ${basePackageName}.model.${modelNameUpperCamel};
import ${servicePackageName}.${modelNameUpperCamel}Service;
import ${basePackageName}.model.${roleNameUpperCamel};
import ${servicePackageName}.${roleNameUpperCamel}Service;
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
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
/**
* Created by ${author} on ${date}.
*/
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller {
    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @Resource
    private ${roleNameUpperCamel}Service ${roleNameLowerCamel}Service;

    @PostMapping("/add")
    public Result add(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {

        ${modelNameLowerCamel}.setId(TextUtils.getIdByUUID());
        ${modelNameLowerCamel}.setGlobalStateType(StateUtils.STATE_NORMAL);
        ${modelNameLowerCamel}.setDicDefaultRoleType(StateUtils.ROLE_ADMIN);
        ${modelNameLowerCamel}.setDicIdType(0);
        ${modelNameLowerCamel}.setDicSexType(0);
        if(${modelNameLowerCamel}.getPasswd() != null)
            ${modelNameLowerCamel}.setPasswd(TextUtils.passwdEncodeToDB(${modelNameLowerCamel}.getPasswd()));
        Date nowTime = TextUtils.getNowTime();
        ${modelNameLowerCamel}.setCreateTime(nowTime);

        ${modelNameLowerCamel}Service.save(${modelNameLowerCamel});

        ${roleNameUpperCamel} role = new ${roleNameUpperCamel}();
        role.setId(TextUtils.getIdByUUID());
        role.setAccountId(${modelNameLowerCamel}.getId());
        role.setCreateTime(nowTime);
        role.setGlobalStateType(StateUtils.STATE_NORMAL);
        role.setDicRoleType(StateUtils.ROLE_ADMIN);
        ${roleNameLowerCamel}Service.save(role);


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
    @RequestMapping(value="/login",method= RequestMethod.POST)
        public Result loginAccount(@RequestBody  ${modelNameUpperCamel} object) {
            try {
                if ((object.getAccount() == null && object.getPhone() == null && object.getEmail() == null)
                        || object.getPasswd() == null) {
                    return ResultGenerator.genFailResult("账户或密码为空!");
                }
                Condition condition = new Condition(${modelNameUpperCamel}.class);
                Example.Criteria criteria = condition.createCriteria();
                criteria.andCondition(String.format("(account = '%s' or email = '%s' or phone='%s') and passwd = '%s'",
                        object.getAccount(), object.getEmail(), object.getPhone(), TextUtils.passwdEncodeToDB(object.getPasswd())));
                List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.list(condition);

                if (list != null && list.size() > 0) {
                    ${modelNameUpperCamel} data = list.get(0);
                    int state = data.getGlobalStateType();
                    if (state == StateUtils.STATE_NORMAL || state == StateUtils.STATE_UNCERTIFIED) {
                        return ResultGenerator.genSuccessResult(data);
                    } else if (state == StateUtils.STATE_FREEZE) {
                        return ResultGenerator.genFailResult("账户已冻结！");
                    }
                } else {
                    return ResultGenerator.genFailResult("账户或密码错误!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResultGenerator.genFailResult(e.getCause().getMessage());
            }
            return ResultGenerator.genFailResult("内部错误");
        }
}
