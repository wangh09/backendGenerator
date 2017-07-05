package generator.engine.impl.filter;

import freemarker.template.TemplateExceptionHandler;
import generator.engine.Engine;
import generator.parameter.SystemParameters;
import generator.parameter.UserParameters;
import generator.utils.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangh09 on 2017/7/5.
 */
public class FilterBaseEngine implements Engine{
    private static final String TEMPLATE_FILE_PATH = SystemParameters.PROJECT_PATH + "/src/test/resources/templates";
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
            //***************************************************************生成 Application
            freemarker.template.Configuration cfg = getConfiguration();
            Map<String, Object> data = new HashMap<>();
            data.put("author", UserParameters.getAuthor());
            data.put("date",new Date().toString());
            data.put("packageName", UserParameters.getPackageName());
            String[] fileNames = {"PostFilter.java","PreFilter.java"};
            for (String filename:fileNames) {
                File file = new File(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH + TextUtils.packageConvertPath(UserParameters.getPackageName()) + "/"+SystemParameters.FILTER_PACKAGE_NAME +"/"+filename);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                cfg.getTemplate("/"+SystemParameters.FILTER_PACKAGE_NAME+"/"+filename.replace(".java",".ftl")).process(data,
                        new FileWriter(file));
            }

            System.out.println("过滤器生成成功！");


        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
