package generator;

import generator.engine.impl.controller.ControllerBaseEngine;
import generator.engine.impl.environment.SpringCloudBaseEngine;
import generator.engine.impl.mq.KafkaEngine;
import generator.parameter.UserParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangh09 on 2017/6/30.
 */
public class Generator {
    static Logger logger = LoggerFactory.getLogger(Generator.class);
    public static void execute() throws Exception{
        new SpringCloudBaseEngine().execute();
        new KafkaEngine().execute();
        printlnResult();
    }
    private static void printlnResult(){
     //   DirectoryUtils.readFile(UserParameters.getDestPath());
        logger.info("\n\n");
        logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        logger.info("@      Genarated Successfully !      @");
        logger.info("@            Thank you!              @");
        logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    public static void main(String[] args) throws Exception {
        String author = "wangh09";
        String dbHost = "120.92.36.30";
        String dbPort = "3306";
        String dbUser = "admin";
        String dbPass = "Cocare7456321!";
        String dbName = "wh_test";
        String packageName = "individual.wangh09.test";

        UserParameters.initParam(author,dbHost,dbUser,dbPass,
                dbName,dbPort,packageName);
        UserParameters.addMicroService("account-service");
        UserParameters.addMicroService("resource-service");
        Generator.execute();
    }
}
