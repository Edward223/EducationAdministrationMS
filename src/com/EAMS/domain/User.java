package com.EAMS.domain;

public class User {

    private String id;	            //uuid
    private String account;	        //用户名
    private String password;	    //密码
    private String name;	        //姓名
    private String sex;	            //性别
    private String email;	        //邮箱
    private String phone;	        //联系电话
    private String institute;	    //学院
    private String authority;	    //权限：1为普通用户，2为管理员

    public User() {
    }

    public User(String id, String account, String password, String name, String sex, String email, String phone, String institute, String authority) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.institute = institute;
        this.authority = authority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
