package com.studymate.dao;
import com.studymate.model.Availability;
import java.sql.*; import java.util.*;
public class AvailabilityDAO {
 static String t(String s){ return s!=null&&s.length()==5? s+":00" : s; }
 public void add(int tutor,String date,String time) throws SQLException {
  try(Connection c=DBUtil.get(); PreparedStatement p=c.prepareStatement("INSERT INTO availability(availabilityDate,availabilityTime,tutorUserID) VALUES(?,?,?)")){
   p.setDate(1,java.sql.Date.valueOf(date)); p.setTime(2,Time.valueOf(t(time))); p.setInt(3,tutor); p.executeUpdate(); } }
 /** tutorId=0 -> all upcoming slots (student view); otherwise only that tutor's slots */
 public List<Availability> upcoming(int tutorId) throws SQLException { List<Availability> l=new ArrayList<>();
  String q="SELECT a.*,u.name,u.avgRate FROM availability a JOIN `user` u ON u.userID=a.tutorUserID WHERE a.availabilityDate>=CURDATE()"+(tutorId>0?" AND a.tutorUserID=?":"")+" ORDER BY u.avgRate DESC,a.availabilityDate,a.availabilityTime";
  try(Connection c=DBUtil.get(); PreparedStatement p=c.prepareStatement(q)){
   if(tutorId>0) p.setInt(1,tutorId); ResultSet r=p.executeQuery();
   while(r.next()){ Availability a=new Availability(); a.setId(r.getInt("availabilityID")); a.setTutorId(r.getInt("tutorUserID")); a.setDate(r.getString("availabilityDate"));
    a.setTime(r.getString("availabilityTime")); a.setTutorName(r.getString("name")); a.setRate(r.getDouble("avgRate")); l.add(a); } } return l; }
 public void remove(int id,int tutor) throws SQLException {
  try(Connection c=DBUtil.get(); PreparedStatement p=c.prepareStatement("DELETE FROM availability WHERE availabilityID=? AND tutorUserID=?")){ p.setInt(1,id); p.setInt(2,tutor); p.executeUpdate(); } }
}
