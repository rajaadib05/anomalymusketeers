package com.studymate.servlet;
import com.studymate.dao.*; import com.studymate.model.*;
import javax.servlet.*; import javax.servlet.annotation.WebServlet; import javax.servlet.http.*; import java.io.IOException; import java.util.*;
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
 protected void doGet(HttpServletRequest q,HttpServletResponse r) throws ServletException,IOException {
  HttpSession ss=q.getSession(); User u=(User)ss.getAttribute("user"); if(u==null){ r.sendRedirect("login"); return; }
  try { String role=u.getRole(); UserDAO d=new UserDAO(); SessionDAO sd=new SessionDAO();
   q.setAttribute("msg",ss.getAttribute("msg")); ss.removeAttribute("msg");
   q.setAttribute("rating",d.rate(u.getId())); q.setAttribute("profile",d.profile(u.getId())); q.setAttribute("mine",sd.forUser(u.getId()));
   List<Availability> slots=new AvailabilityDAO().upcoming("tutor".equals(role)?u.getId():0);
   String f=q.getParameter("q");
   if("student".equals(role) && f!=null && !f.trim().isEmpty()){ List<Availability> k=new ArrayList<>();
    for(Availability a:slots) if(a.getTutorName().toLowerCase().contains(f.trim().toLowerCase())) k.add(a); slots=k; }
   q.setAttribute("slots",slots);
   if("tutor".equals(role)) q.setAttribute("reviews",sd.reviewsFor(u.getId()));
   if("admin".equals(role)){ q.setAttribute("stats",d.stats()); q.setAttribute("users",d.allUsers()); }
   q.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(q,r);
  } catch(Exception e){ throw new ServletException(e); } }
 /** action = addSlot | removeSlot | book | review | cancel | profile | password */
 protected void doPost(HttpServletRequest q,HttpServletResponse r) throws ServletException,IOException {
  HttpSession ss=q.getSession(); User u=(User)ss.getAttribute("user"); if(u==null){ r.sendRedirect("login"); return; }
  try { String a=q.getParameter("action"), role=u.getRole(), msg=null; int id=u.getId();
   boolean tutor="tutor".equals(role), student="student".equals(role); SessionDAO sd=new SessionDAO(); UserDAO d=new UserDAO();
   if(tutor && "addSlot".equals(a)){ new AvailabilityDAO().add(id,q.getParameter("date"),q.getParameter("time")); msg="Slot added."; }
   else if(tutor && "removeSlot".equals(a)) new AvailabilityDAO().remove(Integer.parseInt(q.getParameter("id")),id);
   else if(student && "book".equals(a)) msg=sd.book(Integer.parseInt(q.getParameter("id")),id,q.getParameter("subject"))?"Session booked!":"Sorry, that slot is no longer available.";
   else if(student && "review".equals(a)){ sd.review(Integer.parseInt(q.getParameter("sid")),id,Integer.parseInt(q.getParameter("rating")),q.getParameter("comment")); msg="Thanks for your review."; }
   else if(!"admin".equals(role) && "cancel".equals(a)) msg=sd.cancel(Integer.parseInt(q.getParameter("sid")),id)?"Session cancelled; the slot is open again.":"This session can't be cancelled.";
   else if("profile".equals(a)){ String n=q.getParameter("name"); if(n==null||n.trim().isEmpty()) n=u.getName();
    d.updateProfile(id,n.trim(),q.getParameter("contactNo"),q.getParameter("eduLevel")); ss.setAttribute("user",new User(id,n.trim(),u.getEmail(),role)); msg="Profile updated."; }
   else if("password".equals(a)) msg=d.changePassword(id,q.getParameter("oldPw"),q.getParameter("newPw"))?"Password changed.":"Password not changed (check current password; new one needs 8+ characters).";
   if(msg!=null) ss.setAttribute("msg",msg);
   r.sendRedirect("dashboard");
  } catch(Exception e){ throw new ServletException(e); } } }
