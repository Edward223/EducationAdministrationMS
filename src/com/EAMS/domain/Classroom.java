package com.EAMS.domain;

public class Classroom {
    private String id;	        //uuid
    private String area;	    //区域（eg.A1->A1区）
    private String floor;	    //楼层（eg.1->1楼）
    private String roomNo;	    //教室号（eg.01->01号教室）
    private String holdNum;	    //容纳人数

    public Classroom() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getHoldNum() {
        return holdNum;
    }

    public void setHoldNum(String holdNum) {
        this.holdNum = holdNum;
    }

    @Override
    public String toString() {
        return area +"-"+ floor + roomNo ;
    }
}
