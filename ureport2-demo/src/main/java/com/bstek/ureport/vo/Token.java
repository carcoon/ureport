package com.bstek.ureport.vo;

/**
 * @author ffs
 * @Description:
 * @create 2017/10/25 10:20
 */
public class Token {

    public static final int USER_LOGIN_EXPIRED = 10800;
    protected String token;
    protected int expired;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }
}
