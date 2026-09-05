requireAuth(["CITIZEN"]);
renderNav("my-challenges");

const alertBox = document.getElementById("alert-box");
const tbody = document.getElementById("challenges-body");
const emptyState = document.getElementById("empty-state");

async function loadMyChallenges() {
  try {
    const challenges = await apiFetch("/challenges/my");
    if (!challenges.length) {
      emptyState.classList.remove("hidden");
      return;
    }
    tbody.innerHTML = challenges
      .map(
        (c) => `
        <tr>
          <td>${escapeHtml(c.title)}</td>
          <td>${escapeHtml(c.address)}</td>
          <td>${c.domain ? escapeHtml(c.domain) : '<span class="muted">Pending</span>'}</td>
          <td><span class="pill status-${c.status}">${c.status}</span></td>
          <td>${formatDate(c.createdAt)}</td>
          <td><a class="btn small secondary" href="challenge.html?id=${c.id}">View</a></td>
        </tr>`
      )
      .join("");
  } catch (err) {
    showAlert(alertBox, err.message || "Could not load your challenges.");
  }
}

loadMyChallenges();
