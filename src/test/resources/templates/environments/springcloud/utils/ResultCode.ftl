package ${packageName};

/**
 * Created by ${author} on ${date}.
 */
public enum ResultCode {
    SUCCESS(200),//成功
    FAIL(400),//失败
    UNAUTHORIZED(401),//未认证（签名错误）
    NOT_FOUND(404),//接口不存在
    INTERNAL_SERVER_ERROR(500);//服务器内部错误

    public int status;

     ResultCode(int code) {
         this.status = code;
     }
}
