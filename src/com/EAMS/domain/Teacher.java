package com.EAMS.domain;

public class Teacher {
    private String id;	            //uuid
    private String name;	        //老师姓名
    private String sex;	            //性别
    private String email;	        //邮箱
    private String phone;	        //联系电话
    private String instituteId;	    //所属学院Id

    public Teacher() {
    }

    @Override
    public String toString() {
        return name;
    }

    public Teacher(String id, String name, String sex, String email, String phone, String instituteId) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.instituteId = instituteId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }
}

