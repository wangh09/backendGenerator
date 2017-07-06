package generator.engine.impl.ORM;

import generator.engine.Engine;
import generator.parameter.SystemParameters;
import generator.parameter.UserParameters;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangh09 on 2017/6/30.
 */
public class MybatisEngine implements Engine{
    static public void generateWithIDType(String tableName, boolean isGenerateKey) {
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
//        pluginConfiguration.setConfigurationType("com.xxg.mybatis.plugins.MySQLLimitPlugin");

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
        javaClientGeneratorConfiguration.addProperty("enableSubPackages","true");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName(tableName);
        if(isGenerateKey)
            tableConfiguration.setGeneratedKey(new GeneratedKey("id", "mysql", true, null));
        tableConfiguration.setInsertStatementEnabled(true);
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

    }
    @Override
    public void execute() {

    }
}
