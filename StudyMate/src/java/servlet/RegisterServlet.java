package com.studymate.servlet;
import com.studymate.dao.UserDAO;
import javax.servlet.*; import javax.servlet.annotation.WebServlet; import javax.servlet.http.*; import java.io.IOException;
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
 protected void doGet(HttpServletRequest q,HttpServletResponse r) throws ServletException,IOException { q.getRequestDispatcher("/register.jsp").forward(q,r); }
 protected void doPost(HttpServletRequest q,HttpServletResponse r) throws ServletException,IOException {
  String n=q.getParameter("name"),e=q.getParameter("email"),p=q.getParameter("password");
  if(n==null||e==null||p==null||p.length()<8){ q.setAttribute("error","All fields required; password min 8 characters."); doGet(q,r); return; }
  try { if(!new UserDAO().register(n.trim(),e.trim().toLowerCase(),p,q.getParameter("role"),q.getParameter("contactNo"),q.getParameter("eduLevel"))){ q.setAttribute("error","Email already registered."); doGet(q,r); return; }
   r.sendRedirect("login?registered=1"); } catch(Exception ex){ throw new ServletException(ex); } } }
