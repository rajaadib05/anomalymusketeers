package com.studymate.model;
public class Availability { private int id, tutorId; private String date, time, tutorName; private double rate;
 public int getId(){return id;} public void setId(int v){id=v;}
 public int getTutorId(){return tutorId;} public void setTutorId(int v){tutorId=v;}
 public String getDate(){return date;} public void setDate(String v){date=v;}
 public String getTime(){return time;} public void setTime(String v){time=v;}
 public String getTutorName(){return tutorName;} public void setTutorName(String v){tutorName=v;}
 public double getRate(){return rate;} public void setRate(double v){rate=v;} }
