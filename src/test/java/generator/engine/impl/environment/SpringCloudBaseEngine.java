package generator.engine.impl.environment;
import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import freemarker.template.TemplateExceptionHandler;
import generator.engine.Engine;
import generator.engine.impl.ORM.MybatisEngine;
import generator.engine.impl.controller.ControllerBaseEngine;
import generator.engine.impl.filter.FilterBaseEngine;
import generator.parameter.SystemParameters;
import generator.parameter.UserParameters;
import generator.utils.TextUtils;
import org.apache.catalina.User;
import org.apache.catalina.filters.FilterBase;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

/**
 * Created by wangh09 on 2017/7/3.
 */
public class SpringCloudBaseEngine implements Engine {
    private static final String TEMPLATE_FILE_PATH = SystemParameters.PROJECT_PATH + "/src/test/resources/templates/environments/springcloud";

    private static freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }
    private void generateConfigurations() {
        try {
            //***************************************************************生成 Application
            freemarker.template.Configuration cfg = getConfiguration();
            Map<String, Object> data = new HashMap<>();
            data.put("configurationPackageName", UserParameters.getPackageName()+"."+SystemParameters.CONFIG_PACKAGE_NAME);
            data.put("modelPackageName", UserParameters.getPackageName()+"."+SystemParameters.MODEL_PACKAGE_NAME);
            data.put("mapperInterfaceDir", SystemParameters.MAPPER_INTERFACE_REFERENCE);
            data.put("mapperPackageName", UserParameters.getPackageName()+"."+SystemParameters.MAPPER_PACKAGE_NAME);
            data.put("controllerPackageName", UserParameters.getPackageName()+"."+SystemParameters.CONTROLLER_PACKAGE_NAME);

            data.put("author", UserParameters.getAuthor());
            data.put("date",new Date().toString());
            File file;
            String[] fileNames = {"ApplicationConfigurator.java","MybatisConfigurator.java",
                    "SwaggerConfigurator.java"};
            for (String filename:fileNames) {
                file = new File(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH + TextUtils.packageConvertPath(UserParameters.getPackageName()) + "/" + SystemParameters.CONFIG_PACKAGE_NAME + "/"
                        + filename);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                cfg.getTemplate("/" + SystemParameters.CONFIG_PACKAGE_NAME + "/" + filename.replace(".java",".ftl")).process(data,
                        new FileWriter(file));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void generateAuth() {
        try {
            //***************************************************************API表相关
            Connection conn = DriverManager.getConnection(UserParameters.getMysqlHost(), UserParameters.getDbUser(), UserParameters.getDbPass());
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            runner.runScript(Resources.getResourceAsReader("templates/environments/springcloud/api.sql"));
            String tableName = "resource_d_api";
            MybatisEngine.generateWithIDType(tableName,true);
            ControllerBaseEngine.generateForModel(SystemParameters.INNER_PATH,"resource","api",TextUtils.tableNameConvertUpperCamel(tableName),false);
            //***************************************************************Account表相关
            runner.runScript(Resources.getResourceAsReader("templates/environments/springcloud/account.sql"));
            tableName = "account_d_account";
            MybatisEngine.generateWithIDType(tableName,false);
            ControllerBaseEngine.generateForAccount(SystemParameters.INNER_PATH,"account","account",TextUtils.tableNameConvertUpperCamel(tableName),
                    TextUtils.tableNameConvertUpperCamel("account_r_role"));
            //***************************************************************Dictionary表相关
            tableName = "account_d_dictionary";
            runner.runScript(Resources.getResourceAsReader("templates/environments/springcloud/dictionary.sql"));
            MybatisEngine.generateWithIDType(tableName,false);
            ControllerBaseEngine.generateForModel(SystemParameters.INNER_PATH,"account","dictionary",TextUtils.tableNameConvertUpperCamel(tableName),false);

            //***************************************************************role表相关
            tableName = "account_r_role";
            runner.runScript(Resources.getResourceAsReader("templates/environments/springcloud/role.sql"));
            MybatisEngine.generateWithIDType(tableName,false);
            ControllerBaseEngine.generateForModel(SystemParameters.INNER_PATH,"account","role",TextUtils.tableNameConvertUpperCamel(tableName),true);

            System.out.println("生成成功");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void generateBaseFiles() {
        try {
            //***************************************************************生成 Application
            freemarker.template.Configuration cfg = getConfiguration();
            Map<String, Object> data = new HashMap<>();
            data.put("package", UserParameters.getPackageName());
            data.put("author", UserParameters.getAuthor());
            data.put("date",new Date().toString());
            data.put("extension","@Bean\n" +
                    "    public PreFilter accessFilter() {\n" +
                    "        return new PreFilter();\n" +
                    "    }\n" +
                    "    @Bean\n" +
                    "    public PostFilter postFilter() {\n" +
                    "        return new PostFilter();\n" +
                    "    }");
            data.put("package",UserParameters.getPackageName());
            File file = new File(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH + TextUtils.packageConvertPath(UserParameters.getPackageName()) + "Application.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("Application.ftl").process(data,
                    new FileWriter(file));
            data.clear();
            data.put("author", UserParameters.getAuthor());
            data.put("date",new Date().toString());
            data.put("packageName", UserParameters.getPackageName()+"."+SystemParameters.UTILS_PACKAGE_NAME);
            String[] fileNames = {"Mapper.java","Service.java","AbstractService.java","ServiceException.java","Result.java",
                                "ResultCode.java","ResultGenerator.java","TextUtils.java","StateUtils.java",
                                "JWTUtils.java","ServerUtils.java"};
            for (String filename:fileNames) {
                file = new File(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH + TextUtils.packageConvertPath(UserParameters.getPackageName()) + "/"+SystemParameters.UTILS_PACKAGE_NAME +"/"+filename);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                cfg.getTemplate("/"+SystemParameters.UTILS_PACKAGE_NAME+"/"+filename.replace(".java",".ftl")).process(data,
                        new FileWriter(file));
            }
            ControllerBaseEngine.generateInfoController();

            data.clear();
            file = new File(SystemParameters.PROJECT_PATH + SystemParameters.RESOURCE_PATH + "/application.properties");
            data.put("dbHost", UserParameters.getDbHost());
            data.put("dbUser", UserParameters.getDbUser());
            data.put("dbPort", UserParameters.getDbPort());
            data.put("dbName", UserParameters.getDbName());
            data.put("dbPass", UserParameters.getDbPass());

            String urlMappings = "";
            for(String serviceName: UserParameters.microServices) {
                urlMappings += "zuul.routes."+serviceName+".path=/"+serviceName+"/**\n"+
                        "zuul.routes."+serviceName+".url=http://localhost:2223/"+SystemParameters.INNER_PATH+"/"+serviceName+"\n";
            }

            data.put("urlmappings",urlMappings);
            cfg.getTemplate("applicationproperties.ftl").process(data,
                    new FileWriter(file));
            System.out.println("基础结构生成成功！");


        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void execute() {
        generateBaseFiles();
        generateAuth();
        generateConfigurations();
        new FilterBaseEngine().execute();
    }
}