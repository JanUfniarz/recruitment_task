import React, { useState, ChangeEvent } from 'react';
import { useNavigate } from 'react-router-dom';


type City = {
    name: string;
    state: string;
    fullName: string;
};

const CitySearchBar: React.FC = () => {
    const [query, setQuery] = useState<string>('');
    const [results, setResults] = useState<City[]>([]);
    const navigate = useNavigate();
    const handleInputChange = async (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setQuery(value);

        if (value.length >= 2) {
            try {
                const res = await fetch(`/api/cities?query=${encodeURIComponent(value)}`);
                if (!res.ok) throw new Error('Błąd pobierania danych');
                const data: City[] = await res.json();
                setResults(data);
            } catch (err) {
                console.error('Błąd:', err);
                setResults([]);
            }
        } else {
            setResults([]);
        }
    };

    const handleClick = (city: City) => {
        navigate(`/city/${encodeURIComponent(city.fullName)}`);
    };

    return (
        <div style={{ padding: '16px', maxWidth: '500px', margin: '0 auto' }}>
            <input
                type="text"
                value={query}
                onChange={handleInputChange}
                placeholder="Wyszukaj miasto..."
                style={{
                    width: '100%',
                    padding: '8px',
                    fontSize: '16px',
                    border: '1px solid #ccc',
                    borderRadius: '4px',
                }}
            />

            {results.length > 0 && (
                <ul
                    style={{
                        listStyle: 'none',
                        padding: 0,
                        marginTop: '8px',
                        border: '1px solid #ddd',
                        borderRadius: '4px',
                        backgroundColor: '#fff',
                    }}
                >
                    {results.map((city, index) => (
                        <li
                            key={index}
                            onClick={() => handleClick(city)}
                            style={{
                                padding: '8px',
                                borderBottom: '1px solid #eee',
                                cursor: 'pointer',
                            }}
                        >
                            {city.name}, {city.state}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default CitySearchBar;
