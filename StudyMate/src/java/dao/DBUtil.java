package com.studymate.dao;
import java.sql.*;
public class DBUtil {
 public static Connection get() throws SQLException {
  try { Class.forName("com.mysql.jdbc.Driver"); }
  catch (ClassNotFoundException e) { try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException e2) { /* JDBC 4 auto-load */ } }
  String url = System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/tutor_match?useSSL=false");
  return DriverManager.getConnection(url, System.getenv().getOrDefault("DB_USER","root"), System.getenv().getOrDefault("DB_PASS",""));
 } }
