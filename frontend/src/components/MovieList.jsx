import { useEffect, useState } from 'react';
import { fetchMovies } from '../api';

export default function MovieList({ genre }) {
  // `key` records which genre this result belongs to. If it differs from the
  // currently selected genre, we are still loading.
  const [result, setResult] = useState({ key: null, movies: [], error: null });
  const loading = result.key !== genre;

  useEffect(() => {
    const controller = new AbortController();

    fetchMovies(genre, controller.signal)
      .then((movies) => setResult({ key: genre, movies, error: null }))
      .catch((err) => {
        if (err.name === 'AbortError') return;
        setResult({ key: genre, movies: [], error: err.message });
      });

    // If the user changes the filter quickly, cancel the older request so a
    // slow response can't overwrite a newer one.
    return () => controller.abort();
  }, [genre]);

  if (loading) return <p className="status">Loading movies...</p>;
  if (result.error) return <p className="status error">{result.error}</p>;
  if (result.movies.length === 0) return <p className="status">No movies found.</p>;

  return (
    <ul className="movie-grid">
      {result.movies.map((movie) => (
        <li key={movie.id} className="movie-card">
          <img src={movie.posterUrl} alt={`${movie.title} poster`} />
          <div className="movie-info">
            <h3>{movie.title}</h3>
            <p className="meta">
              {movie.genre.replace('_', ' ')} · {movie.language} · {movie.durationMinutes} min
            </p>
            <p className="description">{movie.description}</p>
          </div>
        </li>
      ))}
    </ul>
  );
}