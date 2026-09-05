/**
 * Renders the shared site navigation into <div id="site-nav"></div>.
 * Call renderNav("current-page-id") near the bottom of each page.
 */
function renderNav(activeId) {
  const mount = document.getElementById("site-nav");
  if (!mount) return;

  const session = Session.get();
  const links = [
    { id: "home",         href: "index.html",       label: "Home" },
    { id: "universities", href: "universities.html", label: "Universities" },
  ];

  if (session?.token) {
    if (session.role === "CITIZEN") {
      links.push({ id: "submit",        href: "submit-challenge.html", label: "Report Issue" });
      links.push({ id: "my-challenges", href: "my-challenges.html",    label: "My Reports" });
    }
    if (session.role === "UNIVERSITY_ADMIN") {
      links.push({ id: "university-assignments", href: "university-assignments.html", label: "Assignments" });
    }
    if (session.role === "INDUSTRY") {
      links.push({ id: "industry-dashboard", href: "industry-dashboard.html", label: "Opportunities" });
    }
    if (session.role === "SUPER_ADMIN") {
      links.push({ id: "admin", href: "admin.html", label: "Admin" });
    }
  }

  const linkHtml = links
    .map(
      (l) =>
        `<a href="${l.href}" class="${l.id === activeId ? "active" : ""}">${l.label}</a>`
    )
    .join("");

  const authHtml = session?.token
    ? `<span class="nav-user">
         <span>${escapeHtml(session.name)}</span>
         <span class="badge-role">${session.role.replace(/_/g, " ")}</span>
       </span>
       <button id="nav-logout-btn">Logout</button>`
    : `<a href="login.html" class="${activeId === "login" ? "active" : ""}">Login</a>
       <a href="register.html" class="btn small ${activeId === "register" ? "active" : ""}" style="margin-left:0.25rem;">Get Started</a>`;

  mount.innerHTML = `
    <header class="site-header">
      <nav class="navbar">
        <a class="brand" href="index.html">
          <span class="brand-icon">🏛️</span>
          JanSetu
        </a>
        <div class="nav-links">
          ${linkHtml}
          ${authHtml}
        </div>
      </nav>
    </header>
  `;

  const logoutBtn = document.getElementById("nav-logout-btn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      Session.clear();
      window.location.href = "index.html";
    });
  }
}
