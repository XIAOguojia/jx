package entity;

import java.io.Serializable;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/1/9
 * Time:10:50
 */
public class Result implements Serializable {
    //是否保存成功
    private boolean success;
    //保存之后的提示信息
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result(boolean success, String message) {

        this.success = success;
        this.message = message;
    }
}
