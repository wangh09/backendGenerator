package generator.parameter;

/**
 * Created by wangh09 on 2017/7/3.
 */
public class SystemParameters {
    public static final String JAVA_PATH = "/src/main/java";
    public static final String RESOURCE_PATH = "/src/main/resources";
    public static final String CONTROLLER_PACKAGE_NAME = "controller";
    public static final String MODEL_PACKAGE_NAME = "model";
    public static final String MAPPER_PACKAGE_NAME = "mapper";
    public static final String SERVICE_PACKAGE_NAME = "service";
    public static final String FILTER_PACKAGE_NAME = "filter";
    public static final String UTILS_PACKAGE_NAME = "utils";
    public static final String CONFIG_PACKAGE_NAME = "config";
    public static final String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    public static final String JDBC_DIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

    public static final String INNER_PATH = "remove-me";

    public static final String MAPPER_INTERFACE_REFERENCE = UserParameters.getPackageName() + "."+UTILS_PACKAGE_NAME+".Mapper";
}
