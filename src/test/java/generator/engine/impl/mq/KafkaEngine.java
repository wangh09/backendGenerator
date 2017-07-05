package generator.engine.impl.mq;

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
public class KafkaEngine implements Engine {
    private static final String[] KAFKA_SERVICE_NO_LISTENER_LIST ={"resource"};
    private static final String[] KAFKA_SERVICE_WITH_LISTENER_LIST ={"account"};
    private static final String KAFKA_HOST = "52.80.70.251";
    private static final String KAFKA_PORT = "9092";
    private static final String KAFKA_MESSAGE_TUNNEL = "generalTopic";

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
            data.put("packageName", UserParameters.getPackageName()+"."+SystemParameters.CONFIG_PACKAGE_NAME);
            data.put("kafkaPort",KAFKA_PORT);
            data.put("kafkaHost",KAFKA_HOST);
            File file = new File(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH + TextUtils.packageConvertPath(UserParameters.getPackageName()) +
                    "/"+SystemParameters.CONFIG_PACKAGE_NAME +"/"+"KafkaProducerConfig.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("/"+SystemParameters.MESSAGE_QUEUE_PACKAGE_NAME+"/"+"KafkaProducerConfig.ftl").process(data,
                    new FileWriter(file));

            data.put("messageTunnel", KAFKA_MESSAGE_TUNNEL);

            for(String service: KAFKA_SERVICE_NO_LISTENER_LIST) {
                String upperName = TextUtils.lowerToUpperBegin(service);
                data.put("serviceUpperName", upperName);
                data.put("serviceLowerName", service);
                data.put("packageName", UserParameters.getPackageName()+"."+SystemParameters.MESSAGE_QUEUE_PACKAGE_NAME+"."+service);
                file = new File(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH + TextUtils.packageConvertPath(UserParameters.getPackageName()) +
                        "/"+SystemParameters.MESSAGE_QUEUE_PACKAGE_NAME+"/"+ service+"/"+upperName+"KafkaConsumerConfig.java");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                cfg.getTemplate("/"+SystemParameters.MESSAGE_QUEUE_PACKAGE_NAME+"/"+"KafkaConsumerConfig.ftl").process(data,
                        new FileWriter(file));

                file = new File(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH + TextUtils.packageConvertPath(UserParameters.getPackageName()) +
                        "/"+SystemParameters.MESSAGE_QUEUE_PACKAGE_NAME+"/"+ service+"/"+upperName+"KafkaMessageHandler.java");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                cfg.getTemplate("/"+SystemParameters.MESSAGE_QUEUE_PACKAGE_NAME+"/"+"KafkaMessageHandler.ftl").process(data,
                        new FileWriter(file));

            }

            for(String service: KAFKA_SERVICE_WITH_LISTENER_LIST) {
                String upperName = TextUtils.lowerToUpperBegin(service);
                data.put("serviceUpperName", upperName);
                data.put("serviceLowerName", service);
                data.put("packageName", UserParameters.getPackageName()+"."+SystemParameters.MESSAGE_QUEUE_PACKAGE_NAME+"."+service);
                file = new File(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH + TextUtils.packageConvertPath(UserParameters.getPackageName()) +
                        "/"+SystemParameters.MESSAGE_QUEUE_PACKAGE_NAME+"/"+ service+"/"+upperName+"KafkaConsumerConfig.java");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                cfg.getTemplate("/"+SystemParameters.MESSAGE_QUEUE_PACKAGE_NAME+"/"+"KafkaConsumerConfig.ftl").process(data,
                        new FileWriter(file));

                file = new File(SystemParameters.PROJECT_PATH + SystemParameters.JAVA_PATH + TextUtils.packageConvertPath(UserParameters.getPackageName()) +
                        "/"+SystemParameters.MESSAGE_QUEUE_PACKAGE_NAME+"/"+ service+"/"+"/"+upperName+"KafkaMessageHandler.java");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                cfg.getTemplate("/"+SystemParameters.MESSAGE_QUEUE_PACKAGE_NAME+"/"+"KafkaMessageHandlerWithListener.ftl").process(data,
                        new FileWriter(file));

            }



            System.out.println("过滤器生成成功！");


        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
