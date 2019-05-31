package cn.edu.cuc.logindemo.domain;

/**
 * 用户实体类
 */

public class User {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;


}
