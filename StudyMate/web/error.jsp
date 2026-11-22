<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%! static String h(String s){ return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"); } %>
<% Throwable t=exception; while(t!=null && t.getCause()!=null && t.getCause()!=t) t=t.getCause();
 String m=t==null?"":String.valueOf(t), hint="Check the GlassFish log for details.";
 if(m.contains("Communications link failure")||m.contains("Connection refused")) hint="MySQL is not running. Start MySQL in the XAMPP Control Panel.";
 else if(m.contains("Access denied")) hint="Wrong MySQL username/password. Edit DBUtil.java (default: root, empty password).";
 else if(m.contains("Unknown database")) hint="Database 'tutor_match' does not exist. Create it in phpMyAdmin.";
 else if(m.contains("No suitable driver")||m.contains("ClassNotFound")) hint="MySQL driver missing. Project Properties > Libraries > Add Library > MySQL JDBC Driver (tick Package), then Clean and Build.";
 else if(m.contains("doesn't exist")||m.contains("Unknown column")) hint="A table/column differs from the ERD. Compare your phpMyAdmin tables with sql/schema.sql."; %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Something went wrong</title>
<style>body{font:16px system-ui,sans-serif;max-width:640px;margin:3rem auto;padding:0 1rem}code{display:block;padding:.75rem;background:#8881;border-radius:8px;overflow:auto}</style></head><body>
<h1>Something went wrong</h1><p><strong><%= h(hint) %></strong></p><code><%= h(m) %></code><p><a href="login">Back to login</a></p></body></html>
