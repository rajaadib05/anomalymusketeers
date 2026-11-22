package com.studymate.model;
public class StudySession { private int id, studentId, tutorId; private String date, time, subject, studentName, tutorName; private boolean reviewed;
 public int getId(){return id;} public void setId(int v){id=v;}
 public int getStudentId(){return studentId;} public void setStudentId(int v){studentId=v;}
 public int getTutorId(){return tutorId;} public void setTutorId(int v){tutorId=v;}
 public String getDate(){return date;} public void setDate(String v){date=v;}
 public String getTime(){return time;} public void setTime(String v){time=v;}
 public String getSubject(){return subject;} public void setSubject(String v){subject=v;}
 public String getStudentName(){return studentName;} public void setStudentName(String v){studentName=v;}
 public String getTutorName(){return tutorName;} public void setTutorName(String v){tutorName=v;}
 public boolean isReviewed(){return reviewed;} public void setReviewed(boolean v){reviewed=v;} }
