const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function request(path, signal) {
  let response;
  try {
    response = await fetch(`${BASE_URL}${path}`, { signal });
  } catch (err) {
    if (err.name === 'AbortError') throw err;
    // fetch only rejects on network failure or CORS, not on HTTP errors
    throw new Error('Cannot reach the server. Is the backend running?');
  }

  if (!response.ok) {
    let message = `Request failed (${response.status})`;
    try {
      const body = await response.json();
      if (body.message) message = body.message;
    } catch {
      // response had no JSON body; keep the generic message
    }
    throw new Error(message);
  }

  return response.json();
}

export function fetchMovies(genre, signal) {
  const query = genre ? `?genre=${encodeURIComponent(genre)}` : '';
  return request(`/api/movies${query}`, signal);
}