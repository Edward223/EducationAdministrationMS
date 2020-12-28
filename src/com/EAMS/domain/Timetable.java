package com.EAMS.domain;

public class Timetable {
    private String id;
    private String classId;
    private String courseId;
    private String classroomId;
    private String teacherId;
    private String weeks;
    private String timeslotId;

    public Timetable() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(String timeslotId) {
        this.timeslotId = timeslotId;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                "id='" + id + '\'' +
                ", classId='" + classId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", classroomId='" + classroomId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", weeks='" + weeks + '\'' +
                ", timeslotId='" + timeslotId + '\'' +
                '}';
    }
}
