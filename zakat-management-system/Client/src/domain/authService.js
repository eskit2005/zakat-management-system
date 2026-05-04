import { API_BASE_URL } from "../config/apiConfig";

export async function login(email, password) {
    const res = await fetch(`${API_BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ email, password }),
    });

    if (!res.ok) throw new Error("Invalid credentials");
    return res.json();
}

export async function fetchMe(accessToken) {
    const res = await fetch(`${API_BASE_URL}/auth/me`, {
        headers: {
            Authorization: `Bearer ${accessToken}`,
        },
    });

    if (!res.ok) throw new Error("Unauthorized");
    return res.json();
}

export async function registerUser(payload) {
    // FIXED: Removed extra "/api" since it is already in API_BASE_URL
    const res = await fetch(`${API_BASE_URL}/users/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });

    if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw {
            status: res.status,
            message: errorData.message || "Registration failed",
            errors: errorData.errors || []
        };
    }
    return res.json();
}

export async function logout(authfetch) {
  const res = await authfetch(`${API_BASE_URL}/auth/logout`, {
    method: "DELETE",
  });
  if (!res.ok) {
    throw new Error("Logout failed");
  }
}

export async function persistReload(setAccessToken, setUser) {
  const res = await fetch(`${API_BASE_URL}/auth/refresh`, {
    credentials: "include",
  });

  if (!res.ok) throw new Error("Cookie expired or user not signed in");

  const { accessToken } = await res.json();
  setAccessToken(accessToken);

  const userRes = await fetch(`${API_BASE_URL}/auth/me`, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });

  if (!userRes.ok) throw new Error("Couldn't verify user");

  const user = await userRes.json();
  setUser(user);
}
