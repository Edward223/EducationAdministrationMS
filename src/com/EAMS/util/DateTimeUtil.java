package com.EAMS.util;

import com.EAMS.domain.Timeslot;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

	public static String[] getCurrentGrade(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		int currentYear = calendar.get(Calendar.YEAR);
		int currentMonth = calendar.get(Calendar.MONTH);

		String currentGrade = "0";
		String currentSemester = "0";

		if(currentMonth > 8){
			currentGrade = String.valueOf(currentYear-year+1);
			currentSemester = "1";
		}else {
			currentGrade = String.valueOf(currentYear-year);
			currentSemester = "2";
		}
		return new String[]{currentGrade,currentSemester};
	}

	public static String[] getInSchoolGrade(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		int currentYear = calendar.get(Calendar.YEAR);
		int currentMonth = calendar.get(Calendar.MONTH);

		String[] grades = new String[5];
		grades[0] = "请选择年级";
		if(currentMonth > 8){
			for (int i = 1; i < grades.length; i++) {
				grades[i] = String.valueOf(currentYear-i+1);
			}
		}else {
			for (int i = 1; i < grades.length; i++) {
				grades[i] = String.valueOf(currentYear-i);
			}
		}
		return grades;
	}

	public static String concatTimeslot(Timeslot begin, Timeslot end){
		if(end == null){
			return begin.getDay()+"-"+begin.getTime();
		}
		if(begin.getDay().equals(end.getDay())){
			return begin.getDay()+"-第"+begin.getTime().charAt(1)+"~"+end.getTime().charAt(1)+"节课";
		}
		return "找不到数据";
	}

	public static String getSemester(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int currentMonth = calendar.get(Calendar.MONTH);
		String currentSemester = "0";

		if(currentMonth > 8){
			currentSemester = "1";
		}else {
			currentSemester = "2";
		}
		return currentSemester;
	}
}
