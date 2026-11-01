import { useState } from 'react';
import MovieList from './components/MovieList';
import './App.css';

const GENRES = ['THRILLER', 'ACTION', 'COMEDY', 'DRAMA', 'HORROR', 'ROMANCE', 'SCI_FI'];

export default function App() {
  const [genre, setGenre] = useState('');

  return (
    <div className="app">
      <header className="app-header">
        <h1>CineAI</h1>
        <p>Discover movies and book tickets</p>
      </header>

      <div className="filters">
        <label htmlFor="genre">Genre</label>
        <select id="genre" value={genre} onChange={(e) => setGenre(e.target.value)}>
          <option value="">All genres</option>
          {GENRES.map((g) => (
            <option key={g} value={g}>
              {g.replace('_', ' ')}
            </option>
          ))}
        </select>
      </div>

      <MovieList genre={genre} />
    </div>
  );
}