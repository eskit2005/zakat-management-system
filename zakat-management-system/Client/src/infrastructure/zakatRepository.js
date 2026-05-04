import { API_BASE_URL } from "../config/apiConfig";

// Report - Open access
export async function fetchReport() {
  const res = await fetch(`${API_BASE_URL}/report`);
  if (!res.ok) throw new Error("Failed to fetch report");
  return await res.json();
}

// Dashboard - Admin only
export async function fetchDashboard(authFetch) {
  const res = await authFetch(`${API_BASE_URL}/dashboard`);
  if (!res.ok) throw new Error("Failed to fetch dashboard");
  return await res.json();
}

// Beneficiaries
export async function fetchBeneficiaryQueue(authFetch) {
  const res = await authFetch(`${API_BASE_URL}/beneficiaries/queue`);
  if (!res.ok) throw new Error("Failed to fetch queue");
  return await res.json();
}

export async function submitBeneficiaryForm(authFetch, payload) {
  const res = await authFetch(`${API_BASE_URL}/beneficiaries/form`, {
    method: 'PATCH',
    body: JSON.stringify(payload)
  });
  if (!res.ok) throw new Error("Form submission failed");
  return await res.json();
}

export async function fetchBeneficiaryById(authFetch, id) {
  const res = await authFetch(`${API_BASE_URL}/beneficiaries/${id}`);
  if (!res.ok) throw new Error("Failed to fetch beneficiary details");
  return await res.json();
}

// Donations
export async function createDonation(authFetch, payload) {
  const res = await authFetch(`${API_BASE_URL}/donors`, {
    method: 'POST',
    body: JSON.stringify(payload)
  });
  if (!res.ok) throw new Error("Donation failed");
  return await res.json();
}

export async function fetchDonorById(authFetch, id) {
  const res = await authFetch(`${API_BASE_URL}/donors/${id}`);
  if (!res.ok) throw new Error("Failed to fetch donor details");
  return await res.json();
}

export async function fetchDonatedBeneficiaries(authFetch, id) {
  const res = await authFetch(`${API_BASE_URL}/donors/${id}/beneficiaries`);
  if (!res.ok) throw new Error("Failed to fetch donated beneficiaries");
  return await res.json();
}

export async function fetchTotalDonations(authFetch, id) {
  const res = await authFetch(`${API_BASE_URL}/donors/${id}/total-donations`);
  if (!res.ok) throw new Error("Failed to fetch total donations");
  return await res.json();
}

export async function donateDirectly(authFetch, donorId, payload) {
  const res = await authFetch(`${API_BASE_URL}/donors/${donorId}/beneficiaries`, {
    method: 'POST',
    body: JSON.stringify(payload)
  });
  if (!res.ok) throw new Error("Direct donation failed");
  // No body returned from backend for this endpoint
  return true;
}

// Item Donation
export async function addInventoryItem(authFetch, payload) {
  const res = await authFetch(`${API_BASE_URL}/inventory`, {
    method: 'POST',
    body: JSON.stringify(payload)
  });
  if (!res.ok) throw new Error("Inventory add failed");
  return await res.json();
}

export async function fetchAvailableInventory(authFetch) {
  const res = await authFetch(`${API_BASE_URL}/inventory/available`);
  if (!res.ok) throw new Error("Failed to fetch inventory");
  return await res.json();
}

// Admin Assignments
export async function assignZakat(authFetch, payload) {
  const res = await authFetch(`${API_BASE_URL}/assignments`, {
    method: 'POST',
    body: JSON.stringify(payload)
  });
  if (!res.ok) {
    const errorData = await res.json();
    throw new Error(errorData.message || "Assignment failed");
  }
  return await res.json();
}

export async function fetchAllAssignments(authFetch) {
  const res = await authFetch(`${API_BASE_URL}/assignments`);
  if (!res.ok) throw new Error("Failed to fetch assignments");
  return await res.json();
}
