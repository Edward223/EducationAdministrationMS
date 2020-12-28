package com.EAMS.vo;

public class CourseVo {
    private String id;
    private String name;
    private String instituteName;
    private String period;
    private Integer majorNum;
    private Integer TeacherNum;

    public CourseVo() {
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

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getMajorNum() {
        return majorNum;
    }

    public void setMajorNum(Integer majorNum) {
        this.majorNum = majorNum;
    }

    public Integer getTeacherNum() {
        return TeacherNum;
    }

    public void setTeacherNum(Integer teacherNum) {
        TeacherNum = teacherNum;
    }

}
