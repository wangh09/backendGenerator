package generator.engine.impl.environment;
import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import freemarker.template.TemplateExceptionHandler;
import generator.engine.Engine;
import generator.engine.impl.controller.ControllerBaseEngine;
import generator.parameter.SystemParameters;
import generator.parameter.UserParameters;
import generator.utils.TextUtils;
import org.apache.catalina.User;
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
            String tableName = "resource_d_api";
            //***************************************************************建立数据库
            Connection conn = DriverManager.getConnection(UserParameters.getMysqlHost(), UserParameters.getDbUser(), UserParameters.getDbPass());
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            runner.runScript(Resources.getResourceAsReader("templates/environments/springcloud/api.sql"));
            //***************************************************************生成mapper代码
            Context context = new Context(ModelType.FLAT);
            context.setId("wangh09-api");
            context.setTargetRuntime("MyBatis3Simple");
            context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
            context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

            JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
            jdbcConnectionConfiguration.setConnectionURL(UserParameters.getMysqlHost());
            jdbcConnectionConfiguration.setUserId(UserParameters.getDbUser());
            jdbcConnectionConfiguration.setPassword(UserParameters.getDbPass());
            jdbcConnectionConfiguration.setDriverClass(SystemParameters.JDBC_DIVER_CLASS_NAME);
            context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

            PluginConfiguration pluginConfiguration = new PluginConfiguration();
            pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
            pluginConfiguration.addProperty("mappers", SystemParameters.MAPPER_INTERFACE_REFERENCE);
            context.addPluginConfiguration(pluginConfiguration);

            JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
            javaModelGeneratorConfiguration.setTargetProject(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH);
            javaModelGeneratorConfiguration.setTargetPackage(UserParameters.getPackageName() +"." +SystemParameters.MODEL_PACKAGE_NAME);
            context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

            SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
            sqlMapGeneratorConfiguration.setTargetProject(SystemParameters.PROJECT_PATH + SystemParameters.RESOURCE_PATH);
            sqlMapGeneratorConfiguration.setTargetPackage("mapper");
            context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

            JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
            javaClientGeneratorConfiguration.setTargetProject(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH);
            javaClientGeneratorConfiguration.setTargetPackage(UserParameters.getPackageName() +"." +SystemParameters.MAPPER_PACKAGE_NAME);
            javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
            context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

            TableConfiguration tableConfiguration = new TableConfiguration(context);
            tableConfiguration.setTableName(tableName);
            tableConfiguration.setGeneratedKey(new GeneratedKey("id", "Mysql", true, null));
            context.addTableConfiguration(tableConfiguration);

            List<String> warnings;
            MyBatisGenerator generator;
            try {
                Configuration config = new Configuration();
                config.addContext(context);
                config.validate();

                boolean overwrite = true;
                DefaultShellCallback callback = new DefaultShellCallback(overwrite);
                warnings = new ArrayList<String>();
                generator = new MyBatisGenerator(config, callback, warnings);
                generator.generate(null);
            } catch (Exception e) {
                throw new RuntimeException("生成Model和Mapper失败", e);
            }

            if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
                throw new RuntimeException("生成Model和Mapper失败：" + warnings);
            }

            ControllerBaseEngine.generateForModel("resource","api",TextUtils.tableNameConvertUpperCamel(tableName));
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
            data.put("packageName", UserParameters.getPackageName()+"."+SystemParameters.UTILS_PACKAGE_NAME);
            String[] fileNames = {"Mapper.java","Service.java","AbstractService.java","ServiceException.java","Result.java",
                                "ResultCode.java","ResultGenerator.java"};
            for (String filename:fileNames) {
                file = new File(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH + TextUtils.packageConvertPath(UserParameters.getPackageName()) + "/"+SystemParameters.UTILS_PACKAGE_NAME +"/"+filename);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                cfg.getTemplate("/"+SystemParameters.UTILS_PACKAGE_NAME+"/"+filename.replace(".java",".ftl")).process(data,
                        new FileWriter(file));
            }
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
    }
}