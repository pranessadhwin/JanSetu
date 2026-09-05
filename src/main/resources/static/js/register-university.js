renderNav("register");

if (Session.isAuthenticated()) {
  window.location.href = "index.html";
}

const form = document.getElementById("register-form");
const alertBox = document.getElementById("alert-box");
const submitBtn = document.getElementById("register-submit");
const universitySelect = document.getElementById("university");

async function loadUniversities() {
  try {
    const universities = await apiFetch("/universities");
    universitySelect.innerHTML =
      `<option value="">Select your university</option>` +
      universities.map((u) => `<option value="${u.id}">${escapeHtml(u.name)} — ${escapeHtml(u.location || "")}</option>`).join("");
  } catch (err) {
    universitySelect.innerHTML = `<option value="">Could not load universities</option>`;
    showAlert(alertBox, err.message || "Could not load the list of universities.");
  }
}
loadUniversities();

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  clearAlert(alertBox);
  submitBtn.disabled = true;
  submitBtn.textContent = "Submitting...";

  const payload = {
    name: document.getElementById("name").value.trim(),
    email: document.getElementById("email").value.trim(),
    phone: document.getElementById("phone").value.trim(),
    password: document.getElementById("password").value,
    universityId: Number(universitySelect.value),
  };

  try {
    await apiFetch("/university/register", { method: "POST", body: payload });
    form.reset();
    showAlert(
      alertBox,
      "Registration submitted! A Super Admin must approve your account before you can log in.",
      "success"
    );
  } catch (err) {
    showAlert(alertBox, err.message || "Registration failed.");
  } finally {
    submitBtn.disabled = false;
    submitBtn.textContent = "Submit for approval";
  }
});
