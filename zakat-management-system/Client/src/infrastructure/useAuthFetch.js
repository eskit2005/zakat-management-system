import { API_BASE_URL } from "../config/apiConfig";
import { useContext } from "react";
import { AuthContext } from "../auth/authContext";

export function useAuthFetch() {
  const { accessToken, setAccessToken, setUser } = useContext(AuthContext);

  async function refreshToken() {
    const res = await fetch(`${API_BASE_URL}/auth/refresh`, {
      credentials: "include",
    });

    if (!res.ok) return false;

    const data = await res.json();
    setAccessToken(data.accessToken);
    return data.accessToken;
  }

  async function authFetch(url, options = {}) {
    const res = await fetch(url, {
      ...options,
      headers: {
        ...(options.headers || {}),
        Authorization: accessToken ? `Bearer ${accessToken}` : "",
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    if (res.status === 401) {
      const newToken = await refreshToken();
      if (!newToken) {
        setAccessToken(null);
        setUser(null);
        throw new Error("Session expired");
      }

      return fetch(url, {
        ...options,
        headers: {
          ...(options.headers || {}),
          Authorization: `Bearer ${newToken}`,
          "Content-Type": "application/json",
        },
        credentials: "include",
      });
    }

    return res;
  }

  return authFetch;
}
