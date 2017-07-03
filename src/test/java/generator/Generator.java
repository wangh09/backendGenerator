package generator;

import generator.engine.impl.environment.SpringCloudEngine;
import generator.parameter.UserParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangh09 on 2017/6/30.
 */
public class Generator {
    static Logger logger = LoggerFactory.getLogger(Generator.class);
    public static void execute() throws Exception{
        new SpringCloudEngine().execute();
        printlnResult();
    }
    private static void printlnResult(){
     //   DirectoryUtils.readFile(UserParameters.getDestPath());
        logger.info("\n\n");
        logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        logger.info("@      Genarate Successfully !       @");
        logger.info("@            Thank you!              @");
        logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    public static void main(String[] args) throws Exception {
        String author = "wangh09";
        String projectName = "";
        String dbHost = "127.0.0.1";
        String dbPort = "3306";
        String dbUser = "root";
        String dbPass = "";
        String dbName = "gamesapi";
        String packageName = "individual.wangh09.test";

        UserParameters.initParam(author,dbHost,dbUser,dbPass,
                dbName,dbPort,packageName,projectName);
        Generator.execute();
    }
}
