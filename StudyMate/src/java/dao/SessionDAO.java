package com.studymate.dao;
import com.studymate.model.StudySession;
import java.sql.*; import java.util.*;
public class SessionDAO {
 /** Books a tutor's slot: creates the session and consumes the slot in one transaction. */
 public boolean book(int availId,int student,String subject) throws SQLException {
  try(Connection c=DBUtil.get()){ c.setAutoCommit(false);
   try{ int tutor; String d,t;
    try(PreparedStatement p=c.prepareStatement("SELECT * FROM availability WHERE availabilityID=? FOR UPDATE")){
     p.setInt(1,availId); ResultSet r=p.executeQuery(); if(!r.next()){ c.rollback(); return false; }
     tutor=r.getInt("tutorUserID"); d=r.getString("availabilityDate"); t=r.getString("availabilityTime"); }
    if(tutor==student){ c.rollback(); return false; }
    try(PreparedStatement p=c.prepareStatement("INSERT INTO `session`(sessionDate,sessionTime,studentUserID,tutorUserID,subjectName) VALUES(?,?,?,?,?)")){
     p.setString(1,d); p.setString(2,t); p.setInt(3,student); p.setInt(4,tutor); p.setString(5,subject); p.executeUpdate(); }
    try(PreparedStatement p=c.prepareStatement("DELETE FROM availability WHERE availabilityID=?")){ p.setInt(1,availId); p.executeUpdate(); }
    c.commit(); return true;
   }catch(SQLException e){ c.rollback(); throw e; } } }
 public List<StudySession> forUser(int uid) throws SQLException { List<StudySession> l=new ArrayList<>();
  String q="SELECT s.*,st.name sn,tu.name tn,EXISTS(SELECT 1 FROM review r WHERE r.sessionID=s.sessionID) rv FROM `session` s "+
   "JOIN `user` st ON st.userID=s.studentUserID JOIN `user` tu ON tu.userID=s.tutorUserID WHERE s.studentUserID=? OR s.tutorUserID=? ORDER BY s.sessionDate DESC,s.sessionTime DESC";
  try(Connection c=DBUtil.get(); PreparedStatement p=c.prepareStatement(q)){ p.setInt(1,uid); p.setInt(2,uid); ResultSet r=p.executeQuery();
   while(r.next()){ StudySession s=new StudySession(); s.setId(r.getInt("sessionID")); s.setStudentId(r.getInt("studentUserID")); s.setTutorId(r.getInt("tutorUserID"));
    s.setDate(r.getString("sessionDate")); s.setTime(r.getString("sessionTime")); s.setSubject(r.getString("subjectName"));
    s.setStudentName(r.getString("sn")); s.setTutorName(r.getString("tn")); s.setReviewed(r.getBoolean("rv")); l.add(s); } } return l; }
 /** Student reviews the tutor of their own session (once), then the tutor's avgRate is recomputed. */
 public void review(int sid,int student,int rating,String comment) throws SQLException {
  try(Connection c=DBUtil.get()){ c.setAutoCommit(false);
   try{ int tutor;
    try(PreparedStatement p=c.prepareStatement("SELECT tutorUserID FROM `session` s WHERE sessionID=? AND studentUserID=? AND NOT EXISTS(SELECT 1 FROM review r WHERE r.sessionID=s.sessionID)")){
     p.setInt(1,sid); p.setInt(2,student); ResultSet r=p.executeQuery(); if(!r.next()){ c.rollback(); return; } tutor=r.getInt(1); }
    try(PreparedStatement p=c.prepareStatement("INSERT INTO review(sessionID,`comment`,rating) VALUES(?,?,?)")){
     p.setInt(1,sid); p.setString(2,comment); p.setInt(3,Math.max(1,Math.min(5,rating))); p.executeUpdate(); }
    try(PreparedStatement p=c.prepareStatement("UPDATE `user` SET avgRate=(SELECT AVG(r.rating) FROM review r JOIN `session` s ON s.sessionID=r.sessionID WHERE s.tutorUserID=?) WHERE userID=?")){
     p.setInt(1,tutor); p.setInt(2,tutor); p.executeUpdate(); }
    c.commit();
   }catch(SQLException e){ c.rollback(); throw e; } } }

 /** Cancels an un-reviewed session (student or tutor) and gives the slot back to the tutor. */
 public boolean cancel(int sid,int uid) throws SQLException {
  try(Connection c=DBUtil.get()){ c.setAutoCommit(false);
   try{ String d,t; int tutor;
    try(PreparedStatement p=c.prepareStatement("SELECT * FROM `session` s WHERE sessionID=? AND (studentUserID=? OR tutorUserID=?) AND NOT EXISTS(SELECT 1 FROM review r WHERE r.sessionID=s.sessionID) FOR UPDATE")){
     p.setInt(1,sid); p.setInt(2,uid); p.setInt(3,uid); ResultSet r=p.executeQuery(); if(!r.next()){ c.rollback(); return false; }
     d=r.getString("sessionDate"); t=r.getString("sessionTime"); tutor=r.getInt("tutorUserID"); }
    try(PreparedStatement p=c.prepareStatement("DELETE FROM payment WHERE sessionID=?")){ p.setInt(1,sid); p.executeUpdate(); }
    try(PreparedStatement p=c.prepareStatement("DELETE FROM `session` WHERE sessionID=?")){ p.setInt(1,sid); p.executeUpdate(); }
    try(PreparedStatement p=c.prepareStatement("INSERT INTO availability(availabilityDate,availabilityTime,tutorUserID) VALUES(?,?,?)")){ p.setString(1,d); p.setString(2,t); p.setInt(3,tutor); p.executeUpdate(); }
    c.commit(); return true;
   }catch(SQLException e){ c.rollback(); throw e; } } }
 /** Reviews a tutor received: {rating, comment, studentName, subject} */
 public List<String[]> reviewsFor(int tutor) throws SQLException { List<String[]> l=new ArrayList<>();
  try(Connection c=DBUtil.get(); PreparedStatement p=c.prepareStatement("SELECT r.rating,r.`comment`,u.name,s.subjectName FROM review r JOIN `session` s ON s.sessionID=r.sessionID JOIN `user` u ON u.userID=s.studentUserID WHERE s.tutorUserID=? ORDER BY r.reviewID DESC")){
   p.setInt(1,tutor); ResultSet r=p.executeQuery(); while(r.next()) l.add(new String[]{r.getString(1),r.getString(2),r.getString(3),r.getString(4)}); } return l; }
}
