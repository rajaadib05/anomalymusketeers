<%@ page contentType="text/html;charset=UTF-8" import="java.util.*,com.studymate.model.*" %>
<%! static String h(String s){ return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;"); } %>
<% User me=(User)session.getAttribute("user"); boolean tutor="tutor".equals(me.getRole());
 List<Availability> slots=(List<Availability>)request.getAttribute("slots"); List<StudySession> mine=(List<StudySession>)request.getAttribute("mine");
 double rating=(Double)request.getAttribute("rating"); %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Dashboard</title><style>
:root{--bg:#f4f6fb;--card:#fff;--ink:#1e2433;--mut:#6b7280;--pri:#4f46e5;--line:#e5e7eb}
@media(prefers-color-scheme:dark){:root{--bg:#12141c;--card:#1b1e29;--ink:#e8eaf2;--mut:#9aa1b2;--line:#2b3040}}
*{box-sizing:border-box}body{margin:0;font:16px system-ui,sans-serif;background:var(--bg);color:var(--ink)}
body.center{display:grid;place-items:center;min-height:100vh;padding:1rem}
.card,.panel{background:var(--card);border:1px solid var(--line);border-radius:12px;padding:1.25rem}
.card{width:100%;max-width:380px;display:grid;gap:.75rem}
label{display:grid;gap:.25rem;font-size:.9rem}
input,select{padding:.6rem;border:1px solid var(--line);border-radius:8px;background:var(--bg);color:var(--ink);font:inherit}
button{padding:.6rem 1rem;border:0;border-radius:8px;background:var(--pri);color:#fff;font:inherit;cursor:pointer}
.muted{color:var(--mut)}.err{color:#dc2626}.ok{color:#16a34a}a{color:var(--pri)}
header{display:flex;justify-content:space-between;align-items:center;padding:1rem 1.5rem;background:var(--card);border-bottom:1px solid var(--line);flex-wrap:wrap;gap:.5rem}
main{max-width:1100px;margin:1.5rem auto;padding:0 1rem;display:grid;gap:1rem;grid-template-columns:repeat(auto-fit,minmax(320px,1fr))}
.panel h2{margin-top:0;font-size:1.1rem}.row{display:flex;gap:.5rem;flex-wrap:wrap;align-items:end}
ul{list-style:none;padding:0;margin:0;display:grid;gap:.5rem}li{padding:.6rem;border:1px solid var(--line);border-radius:8px}
.tag{display:inline-block;padding:.1rem .5rem;border-radius:99px;background:var(--pri);color:#fff;font-size:.75rem}
.tag.offer{background:#059669}form.inline{display:inline-flex;gap:.4rem;margin-top:.4rem}form.inline input,form.inline select{padding:.3rem}
</style></head><body>
<header><strong>📚 StudyMate</strong><span><%= h(me.getName()) %> <span class="tag"><%= me.getRole() %></span><% if(tutor){ %> · ⭐ <%= String.format("%.2f",rating) %><% } %> · <a href="logout">Log out</a></span></header>
<main>
<% if(tutor){ %>
<section class="panel"><h2>Add availability</h2>
 <form method="post" action="dashboard" class="row"><input type="hidden" name="action" value="addSlot">
  <label>Date<input type="date" name="date" required></label><label>Time<input type="time" name="time" required></label><button>Add</button></form></section>
<section class="panel"><h2>My open slots</h2><ul>
 <% if(slots.isEmpty()){ %><li class="muted">No upcoming slots.</li><% } %>
 <% for(Availability a:slots){ %><li><%= a.getDate() %> · <%= a.getTime() %>
  <form class="inline" method="post" action="dashboard"><input type="hidden" name="action" value="removeSlot"><input type="hidden" name="id" value="<%= a.getId() %>"><button>Remove</button></form></li><% } %></ul></section>
<% } else { %>
<section class="panel" style="grid-column:1/-1"><h2>Find a tutor (best rated first)</h2><ul>
 <% if(slots.isEmpty()){ %><li class="muted">No tutor slots available yet.</li><% } %>
 <% for(Availability a:slots){ %><li><strong><%= h(a.getTutorName()) %></strong> ⭐ <%= String.format("%.2f",a.getRate()) %> · <%= a.getDate() %> · <%= a.getTime() %>
  <form class="inline" method="post" action="dashboard"><input type="hidden" name="action" value="book"><input type="hidden" name="id" value="<%= a.getId() %>">
   <input name="subject" placeholder="Subject / course" required><button>Book</button></form></li><% } %></ul></section>
<% } %>
<section class="panel" style="grid-column:1/-1"><h2>My sessions</h2><ul>
 <% if(mine.isEmpty()){ %><li class="muted">No sessions yet.</li><% } %>
 <% for(StudySession s:mine){ %><li><strong><%= h(s.getSubject()) %></strong> · <%= s.getDate() %> <%= s.getTime() %><br>
  <span class="muted">Student: <%= h(s.getStudentName()) %> · Tutor: <%= h(s.getTutorName()) %><%= s.isReviewed()?" · reviewed":"" %></span>
  <% if(!tutor && !s.isReviewed()){ %><form class="inline" method="post" action="dashboard"><input type="hidden" name="action" value="review"><input type="hidden" name="sid" value="<%= s.getId() %>">
   <select name="rating"><% for(int i=5;i>=1;i--){ %><option><%= i %></option><% } %></select><input name="comment" placeholder="Comment"><button>Rate tutor</button></form><% } %></li><% } %></ul></section>
</main></body></html>
