<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>StudyMate – Login</title><style>
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
</style></head><body class="center">
<form class="card" method="post" action="login"><h1>📚 StudyMate</h1><p class="muted">Find study partners for your courses.</p>
<% if(request.getAttribute("error")!=null){ %><p class="err"><%= request.getAttribute("error") %></p><% } %>
<% if(request.getParameter("registered")!=null){ %><p class="ok">Account created – please log in.</p><% } %>
<label>Email<input type="email" name="email" required></label><label>Password<input type="password" name="password" required></label>
<button>Log in</button><p class="muted">No account? <a href="register">Register</a></p></form></body></html>
