const API_BASE = 'http://127.0.0.1:8080/api/auth'; 

// Fungsi Register
async function handleRegister(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });

        const data = await response.json();

        if (response.ok) {
            alert("Registrasi Berhasil! Silakan Login.");
            window.location.href = 'login.html';
        } else {
            alert("Gagal: " + (data.message || "Terjadi kesalahan"));
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Tidak bisa terhubung ke server (Backend mati atau CORS error)");
    }
}

// Pastikan Form Event Listener terpasang
const regForm = document.getElementById('registerForm');
if (regForm) regForm.addEventListener('submit', handleRegister);
