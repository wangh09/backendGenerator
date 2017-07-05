package ${packageName};

import com.alibaba.fastjson.JSON;

/**
 * Created by ${author} on ${date}.
 */
public class Result {
    private int status;
    private String message;
    private Object data;

    public Result setCode(ResultCode resultCode) {
        this.status = resultCode.status;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Result setStatus(int code) {
        this.status = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
