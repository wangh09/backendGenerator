package generator.parameter;

import java.util.ArrayList;

/**
 * Created by wangh09 on 2017/6/30.
 */
public class UserParameters {
    //1: auto id 2: non-auto id 3: uuid
    public static String[][] tableNames = {
            {"account_user",       "account",      "user",       "1"},
            {"account_dealer",       "account",    "dealer",      "2"},
            {"account_expert",       "account",    "expert",      "2"},
            {"account_follow_user",       "account", "follow",         "2"},
            {"account_message",       "account",    "message",      "3"},
            {"account_sms",       "account",        "sms",  "3"},
       //     {"account_admin",       "account",          "1"},

            {"product_award_record",       "product",  "award",        "1"},
            {"product_bottle",       "product",        "bottle",  "1"},
            {"product_catalog",       "product",       "catalog",   "1"},
            {"product_lottery",       "product",       "lottery",   "1"},
            {"product_lottery_record",       "product", "lottery/record",         "1"},
            {"product_lottery_rule",       "product",   "lottery/rule",       "2"},
            {"product_product",       "product",        "product",  "1"},

            {"resource_crop",       "resource",         "crop", "1"},
            {"resource_crop_catalog",       "resource", "crop/catalog",         "1"},
            {"resource_crop_state",       "resource",   "crop/state",       "1"},
            {"resource_fertilizer",       "resource",   "fertilizer",       "1"},
            {"resource_pest",       "resource",         "pest", "1"},
            {"resource_pesticide",       "resource",    "pesticide",      "1"},
            {"resource_prevent",       "resource",      "prevent",    "1"},

            {"social_article",       "social",          "article",    "1"},
            {"social_article_comment",       "social",   "article/comment",       "1"},
            {"social_article_conversation",       "social", "article/conversation",         "1"},
            {"social_article_tag",       "social",   "article/tag",       "1"},
            {"social_attitude",       "social",      "attitude",    "1"},
            {"social_follow",       "social",     "follow",     "1"},
            {"social_question",       "social",   "question",       "1"},
            {"social_question_comment",       "social",   "question/comment",       "1"},
            {"social_question_conversation",       "social",  "question/conversation",        "1"},
            {"social_question_tag",       "social",     "question/tag",     "1"},
    };



    private static String author;
    private static String dbHost;
    private static String dbUser;
    private static String dbPass;
    private static String dbName;
    private static String dbPort;
    private static String packageName;
    public static ArrayList<String> microServices = new ArrayList<>();
    public static void addMicroService(String serviceName) {
        microServices.add(serviceName);
    }
    public static String getMysqlHost() {
        return String.format("jdbc:mysql://%s:%s/%s",dbHost,dbPort,dbName);

    }
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

    public static void initParam(String author, String dbHost, String dbUser, String dbPass, String dbName,
                                 String dbPort, String packageName) {
        UserParameters.author = author;
        UserParameters.dbHost = dbHost;
        UserParameters.dbUser = dbUser;
        UserParameters.dbPass = dbPass;
        UserParameters.dbName = dbName;
        UserParameters.dbPort = dbPort;
        UserParameters.packageName = packageName;
    }

}
