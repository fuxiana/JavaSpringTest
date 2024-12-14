package com.xiaofutest.unit;

/**
 * @author :fuxianan
 * @date : 2024/11/29
 */
public enum ErrorCode {
    SUCCESS(0,"ok",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求参数为空",""),
    NO_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"暂无权限访问",""),
    SYSTEM_ERROR(50000,"系统内部异常",""),
    NO_TOKEN(400,"无token，请重新登录", ""),
    TOKEN_EX(401,"token验证失败，请重新登录,3秒钟后自动跳转到登入页面",""),

    USER_EX(402,"用户不存在，请重新登录",""),
    VERIFICATION_FAILED(406, "权限校验失败", "")
            ;
    //返回码
    private final int code;
    //操作响应信息
    private final String message;
    //响应信息的详细描述
    private final String description;

    //构造函数
    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
    //get方法
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

}
