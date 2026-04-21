const API_URL = 'http://localhost:8080/api/auth';

// Helper untuk menampilkan notifikasi
function showStatus(id, text, color) {
    const el = document.getElementById(id);
    if (el) {
        el.innerText = text;
        el.style.color = color;
    }
}

// LOGIKA LOGIN
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const res = await fetch(`${API_URL}/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });
            const data = await res.json();

            if (res.ok) {
                localStorage.setItem('cresen_token', data.token);
                window.location.href = 'dashboard.html';
            } else {
                alert(data.message);
            }
        } catch (err) {
            alert('Gagal terhubung ke Backend Java!');
        }
    });
}

// LOGIKA REGISTER
const regForm = document.getElementById('registerForm');
if (regForm) {
    regForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('reg-username').value;
        const email = document.getElementById('reg-email').value;
        const password = document.getElementById('reg-password').value;

        try {
            const res = await fetch(`${API_URL}/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, email, password })
            });
            const data = await res.json();

            if (res.ok) {
                showStatus('msg', 'Registrasi Berhasil! Mengalihkan...', '#4ade80');
                setTimeout(() => window.location.href = 'login.html', 1500);
            } else {
                showStatus('msg', data.message, '#f87171');
            }
        } catch (err) {
            showStatus('msg', 'Server tidak merespon.', '#f87171');
        }
    });
}
