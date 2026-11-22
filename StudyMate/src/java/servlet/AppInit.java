package com.studymate.servlet;
import com.studymate.dao.*;
import javax.servlet.*; import javax.servlet.annotation.WebListener; import java.sql.*;
/** On startup: creates any missing tables (ERD) and a default admin account if none exists. */
@WebListener
public class AppInit implements ServletContextListener {
 public void contextInitialized(ServletContextEvent e){
  try(Connection c=DBUtil.get(); Statement s=c.createStatement()){
   s.execute("CREATE TABLE IF NOT EXISTS `user`(userID INT AUTO_INCREMENT PRIMARY KEY, role ENUM('student','tutor','admin') NOT NULL DEFAULT 'student', password VARCHAR(255) NOT NULL, name VARCHAR(100) NOT NULL, contactNo VARCHAR(20), email VARCHAR(100) NOT NULL UNIQUE, eduLevel VARCHAR(50), avgRate DECIMAL(3,2) NOT NULL DEFAULT 0)");
   s.execute("CREATE TABLE IF NOT EXISTS availability(availabilityID INT AUTO_INCREMENT PRIMARY KEY, availabilityDate DATE NOT NULL, availabilityTime TIME NOT NULL, tutorUserID INT NOT NULL, FOREIGN KEY(tutorUserID) REFERENCES `user`(userID))");
   s.execute("CREATE TABLE IF NOT EXISTS `session`(sessionID INT AUTO_INCREMENT PRIMARY KEY, sessionDate DATE NOT NULL, sessionTime TIME NOT NULL, studentUserID INT NOT NULL, tutorUserID INT NOT NULL, subjectName TEXT, FOREIGN KEY(studentUserID) REFERENCES `user`(userID), FOREIGN KEY(tutorUserID) REFERENCES `user`(userID))");
   s.execute("CREATE TABLE IF NOT EXISTS review(reviewID INT AUTO_INCREMENT PRIMARY KEY, sessionID INT NOT NULL, `comment` TEXT, rating INT NOT NULL, FOREIGN KEY(sessionID) REFERENCES `session`(sessionID))");
   s.execute("CREATE TABLE IF NOT EXISTS payment(paymentID INT AUTO_INCREMENT PRIMARY KEY, sessionID INT NOT NULL, paymentDate DATE, totalPayment DECIMAL(10,2), FOREIGN KEY(sessionID) REFERENCES `session`(sessionID))");
   ResultSet r=s.executeQuery("SELECT COUNT(*) FROM `user` WHERE role='admin'"); r.next();
   if(r.getInt(1)==0) try(PreparedStatement p=c.prepareStatement("INSERT INTO `user`(role,password,name,email,avgRate) VALUES('admin',?,'Administrator','admin@studymate.com',0)")){
    p.setString(1,PasswordUtil.hash("admin12345")); p.executeUpdate(); }
  } catch(Exception ex){ e.getServletContext().log("StudyMate DB init failed: "+ex); } }
 public void contextDestroyed(ServletContextEvent e){} }
