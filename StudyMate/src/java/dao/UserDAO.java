package com.studymate.dao;
import com.studymate.model.User;
import java.sql.*; import java.util.*;
public class UserDAO {
 public boolean register(String name,String email,String pw,String role,String contact,String edu) throws SQLException {
  if(!"tutor".equals(role)) role="student";
  try(Connection c=DBUtil.get()){
   try(PreparedStatement k=c.prepareStatement("SELECT 1 FROM `user` WHERE email=?")){ k.setString(1,email); if(k.executeQuery().next()) return false; }
   try(PreparedStatement p=c.prepareStatement("INSERT INTO `user`(role,password,name,contactNo,email,eduLevel,avgRate) VALUES(?,?,?,?,?,?,0)")){
    p.setString(1,role); p.setString(2,PasswordUtil.hash(pw)); p.setString(3,name); p.setString(4,contact); p.setString(5,email); p.setString(6,edu);
    p.executeUpdate(); return true; } } }
 public User login(String email,String pw) throws SQLException {
  try(Connection c=DBUtil.get(); PreparedStatement p=c.prepareStatement("SELECT * FROM `user` WHERE email=?")){
   p.setString(1,email==null?"":email.trim().toLowerCase()); ResultSet r=p.executeQuery();
   if(r.next()){ try{ if(PasswordUtil.check(pw,r.getString("password"))) return new User(r.getInt("userID"),r.getString("name"),r.getString("email"),r.getString("role")); }
    catch(IllegalArgumentException e){ /* ignore */ } }
   return null; } }
 public double rate(int uid) throws SQLException {
  try(Connection c=DBUtil.get(); PreparedStatement p=c.prepareStatement("SELECT avgRate FROM `user` WHERE userID=?")){
   p.setInt(1,uid); ResultSet r=p.executeQuery(); return r.next()?r.getDouble(1):0; } }

 public String[] profile(int uid) throws SQLException {
  try(Connection c=DBUtil.get(); PreparedStatement p=c.prepareStatement("SELECT name,contactNo,eduLevel FROM `user` WHERE userID=?")){
   p.setInt(1,uid); ResultSet r=p.executeQuery(); r.next();
   return new String[]{r.getString(1),r.getString(2)==null?"":r.getString(2),r.getString(3)==null?"":r.getString(3)}; } }
 public void updateProfile(int uid,String name,String contact,String edu) throws SQLException {
  try(Connection c=DBUtil.get(); PreparedStatement p=c.prepareStatement("UPDATE `user` SET name=?,contactNo=?,eduLevel=? WHERE userID=?")){
   p.setString(1,name); p.setString(2,contact); p.setString(3,edu); p.setInt(4,uid); p.executeUpdate(); } }
 public boolean changePassword(int uid,String oldPw,String newPw) throws SQLException {
  if(newPw==null||newPw.length()<8) return false;
  try(Connection c=DBUtil.get()){
   try(PreparedStatement p=c.prepareStatement("SELECT password FROM `user` WHERE userID=?")){ p.setInt(1,uid); ResultSet r=p.executeQuery();
    if(!r.next()||!PasswordUtil.check(oldPw,r.getString(1))) return false; }
   try(PreparedStatement p=c.prepareStatement("UPDATE `user` SET password=? WHERE userID=?")){ p.setString(1,PasswordUtil.hash(newPw)); p.setInt(2,uid); p.executeUpdate(); return true; } } }
 /** admin: {users, sessions, reviews, open slots} */
 public int[] stats() throws SQLException {
  try(Connection c=DBUtil.get(); Statement st=c.createStatement()){
   ResultSet r=st.executeQuery("SELECT (SELECT COUNT(*) FROM `user`),(SELECT COUNT(*) FROM `session`),(SELECT COUNT(*) FROM review),(SELECT COUNT(*) FROM availability)");
   r.next(); return new int[]{r.getInt(1),r.getInt(2),r.getInt(3),r.getInt(4)}; } }
 public List<String[]> allUsers() throws SQLException { List<String[]> l=new ArrayList<>();
  try(Connection c=DBUtil.get(); Statement st=c.createStatement()){
   ResultSet r=st.executeQuery("SELECT name,email,role,avgRate FROM `user` ORDER BY role,name");
   while(r.next()) l.add(new String[]{r.getString(1),r.getString(2),r.getString(3),r.getString(4)}); } return l; }
}
