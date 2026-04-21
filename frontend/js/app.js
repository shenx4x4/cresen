const API_BASE = 'http://localhost:8080/api';

// Helper: Ambil username dari JWT Token
function getUsernameFromToken() {
    const token = localStorage.getItem('cresen_token');
    if (!token) return null;
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join(''));
    return JSON.parse(jsonPayload).sub;
}

// 1. FUNGSI LOGIN
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const res = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        const data = await res.json();
        if (res.ok) {
            localStorage.setItem('cresen_token', data.token);
            window.location.href = 'dashboard.html';
        } else { alert(data.message); }
    });
}

// 2. FUNGSI DASHBOARD (Ambil Saldo & Produk)
async function loadDashboardData() {
    const username = getUsernameFromToken();
    if (!username) { window.location.href = 'login.html'; return; }

    // Simulasi ambil data saldo & produk (Ganti dengan API asli kamu)
    // Untuk sekarang kita pakai mock-up dulu agar kamu bisa lihat tampilannya
    document.getElementById('user-balance').innerText = "Rp 50.000"; 
    
    const productContainer = document.getElementById('product-list');
    const products = [
        { id: 1, name: 'Knalpot Tyga NSR', price: 25000, stock: 5 },
        { id: 2, name: 'Kunci Lisensi Pro', price: 10000, stock: 10 }
    ];

    productContainer.innerHTML = products.map(p => `
        <div class="product-card">
            <div>
                <div style="font-weight: bold;">${p.name}</div>
                <small class="price">Rp ${p.price.toLocaleString()}</small> • <small>Stok: ${p.stock}</small>
            </div>
            <button class="btn" style="width: auto; padding: 5px 15px;" onclick="handleBuy(${p.id})">Beli</button>
        </div>
    `).join('');
}

// 3. FUNGSI TOP UP
async function handleTopUp() {
    const username = getUsernameFromToken();
    const res = await fetch(`${API_BASE}/shop/topup`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: username, amount: 50000 })
    });
    if (res.ok) {
        alert("Saldo Berhasil Ditambah!");
        location.reload();
    }
}

// 4. FUNGSI BELI
async function handleBuy(productId) {
    const username = getUsernameFromToken();
    const res = await fetch(`${API_BASE}/shop/buy`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: username, productId: productId })
    });
    const data = await res.json();
    if (res.ok) {
        alert(data.message);
        location.reload();
    } else {
        alert(data.message);
    }
}

// 5. LOGOUT
const logoutBtn = document.getElementById('logoutBtn');
if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
        localStorage.removeItem('cresen_token');
        window.location.href = 'login.html';
    });
}
