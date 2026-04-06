document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('id').value;
    const password = document.getElementById('password').value;
    const errorMsgDiv = document.getElementById('errorMessage');

    errorMsgDiv.classList.add('d-none'); // Hide errors initially

    try {
        const res = await fetch(`${API_BASE_URL}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: Number(id), password: password })
        });

        const data = await res.json();
        if (data.code === 200 && data.data) {
            const tokenStr = data.data; // token is just the string payload

            // Save token
            setAuthToken(tokenStr);

            // Parse role from token (Token format: token-role-id)
            const parts = tokenStr.split('-');
            const role = parts.length >= 2 ? parts[1] : 'student';

            // Fetch user info
            const infoRes = await fetchWithAuth('/users/my/info');
            const infoData = await infoRes.json();

            if (infoData.code === 200) {
                localStorage.setItem('role', role);
                localStorage.setItem('user', JSON.stringify(infoData.data));

                // Route to respective pages
                if (role.toUpperCase() === 'ADMIN') {
                    window.location.href = '/admin.html';
                } else if (role.toUpperCase() === 'TEACHER') {
                    window.location.href = '/teacher.html';
                } else {
                    window.location.href = '/student.html';
                }
            } else {
                throw new Error(infoData.message || 'Failed to fetch user info');
            }
        } else {
            throw new Error(data.message || 'Login failed');
        }
    } catch (err) {
        errorMsgDiv.textContent = err.message;
        errorMsgDiv.classList.remove('d-none');
    }
});
