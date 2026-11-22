package com.studymate.servlet;
import javax.servlet.annotation.WebServlet; import javax.servlet.http.*; import java.io.IOException;
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
 protected void doGet(HttpServletRequest q,HttpServletResponse r) throws IOException { q.getSession().invalidate(); r.sendRedirect("login"); } }
