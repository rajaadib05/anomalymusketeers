package com.studymate.servlet;
import com.studymate.dao.UserDAO; import com.studymate.model.User;
import javax.servlet.*; import javax.servlet.annotation.WebServlet; import javax.servlet.http.*; import java.io.IOException;
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
 protected void doGet(HttpServletRequest q,HttpServletResponse r) throws ServletException,IOException { q.getRequestDispatcher("/login.jsp").forward(q,r); }
 protected void doPost(HttpServletRequest q,HttpServletResponse r) throws ServletException,IOException {
  try { User u=new UserDAO().login(q.getParameter("email"),q.getParameter("password"));
   if(u==null){ q.setAttribute("error","Invalid email or password."); doGet(q,r); return; }
   q.getSession().invalidate(); q.getSession(true).setAttribute("user",u); r.sendRedirect("dashboard");
  } catch(Exception e){ throw new ServletException(e); } } }
