package generator.parameter;

/**
 * Created by wangh09 on 2017/6/30.
 */
public class UserParameters {
    private static String author;
    private static String dbHost;
    private static String dbUser;
    private static String dbPass;
    private static String dbName;
    private static String dbPort;
    private static String packageName;
    private static String projectName;

    public static String getAuthor() {
        return author;
    }

    public static void setAuthor(String author) {
        UserParameters.author = author;
    }

    public static String getDbHost() {
        return dbHost;
    }

    public static void setDbHost(String dbHost) {
        UserParameters.dbHost = dbHost;
    }

    public static String getDbUser() {
        return dbUser;
    }

    public static void setDbUser(String dbUser) {
        UserParameters.dbUser = dbUser;
    }

    public static String getDbPass() {
        return dbPass;
    }

    public static void setDbPass(String dbPass) {
        UserParameters.dbPass = dbPass;
    }

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        UserParameters.dbName = dbName;
    }

    public static String getDbPort() {
        return dbPort;
    }

    public static void setDbPort(String dbPort) {
        UserParameters.dbPort = dbPort;
    }

    public static String getPackageName() {
        return packageName;
    }

    public static void setPackageName(String packageName) {
        UserParameters.packageName = packageName;
    }

    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String projectName) {
        UserParameters.projectName = projectName;
    }

    public static void initParam(String author, String dbHost, String dbUser, String dbPass, String dbName,
                                 String dbPort, String packageName,String projectName) {
        UserParameters.author = author;
        UserParameters.dbHost = dbHost;
        UserParameters.dbUser = dbUser;
        UserParameters.dbPass = dbPass;
        UserParameters.dbName = dbName;
        UserParameters.dbPort = dbPort;
        UserParameters.packageName = packageName;
        UserParameters.projectName = projectName;
    }

}