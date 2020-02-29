package com.example.framework.gson;

//Token实体类
public class TokenBean {

    /**
     * code : 200
     * userId : 543b515ce5
     * token : c55qQzsEHbrAUJSu5sGMO9qE+DYlNJFIscI3/3i3XF3aUIwHFDMzT/nQEZBajDgbWJSpeOQ9t/DZ1I9mqYPhyhEhe9uk9AjZ
     */

    private int code;
    private String userId;
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
