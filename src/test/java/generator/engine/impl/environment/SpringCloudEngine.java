package generator.engine.impl.environment;

import freemarker.template.TemplateExceptionHandler;
import generator.engine.Engine;
import generator.parameter.SystemParameters;
import generator.parameter.UserParameters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangh09 on 2017/7/3.
 */
public class SpringCloudEngine implements Engine {
    private static final String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    private static final String TEMPLATE_FILE_PATH = PROJECT_PATH + "/src/test/resources/templates/environments/springcloud";

    private static freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }
    @Override
    public void execute() {
        try {
            freemarker.template.Configuration cfg = getConfiguration();
            Map<String, Object> data = new HashMap<>();
            data.put("package", UserParameters.getPackageName()+";");
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
            File file = new File(PROJECT_PATH + SystemParameters.JAVA_PATH + packageConvertPath(UserParameters.getPackageName()) + "Application.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("Application.ftl").process(data,
                    new FileWriter(file));
            System.out.println("Application 生成成功");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }
}