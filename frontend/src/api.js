// Thin wrapper around fetch: adds the JWT and turns error responses into Error objects.
export async function api(path, { method = 'GET', body, token } = {}) {
  const res = await fetch('/api' + path, {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  });
  if (!res.ok) {
    let message = 'Something went wrong. Try again.';
    try {
      const data = await res.json();
      if (data.message) message = data.message;
    } catch { /* response had no JSON body */ }
    const err = new Error(message);
    err.status = res.status;
    throw err;
  }
  return res.status === 204 ? null : res.json();
}
