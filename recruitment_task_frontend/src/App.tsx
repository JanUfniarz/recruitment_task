import './App.css';

import React, { useState } from 'react';

const CitySearchBar = () => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);

  const handleInputChange = async (e) => {
    const value = e.target.value;
    setQuery(value);

    if (value.length >= 2) {
      try {
        const res = await fetch(`/api/cities/search?query=${encodeURIComponent(value)}`);
        if (!res.ok) throw new Error('Błąd pobierania danych');
        const data = await res.json();
        setResults(data);
      } catch (err) {
        console.error('Błąd:', err);
        setResults([]);
      }
    } else {
      setResults([]);
    }
  };

  return (
      <div className="p-4 max-w-md mx-auto">
        <input
            type="text"
            value={query}
            onChange={handleInputChange}
            placeholder="Wyszukaj miasto..."
            className="w-full p-2 border rounded-md"
        />

        {results.length > 0 && (
            <ul className="mt-2 border rounded-md shadow bg-white">
              {results.map((city, index) => (
                  <li key={index} className="p-2 hover:bg-gray-100">
                    {city.name}, {city.state}
                  </li>
              ))}
            </ul>
        )}
      </div>
  );
};

function App() {
  return (
    <div className="App">
      <CitySearchBar></CitySearchBar>
    </div>
  );
}

export default App;
