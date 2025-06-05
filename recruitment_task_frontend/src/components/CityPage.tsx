import React, {useEffect, useState} from 'react';
import {Link, useParams} from 'react-router-dom';

type Article = {
    id: number;
    url: string;
    title: string;
    date: string;
    city: string;
    state: string;
}

type Response = {
    local: Article[];
    state: Article[];
    global: Article[];
}
const CityPage: React.FC = () => {
    const { fullName } = useParams();
    const [articles, setArticles] = useState<Response>({
        local: [],
        state: [],
        global: [],
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);


    useEffect(() => {
        fetch(`/api/news?city=${fullName}`)
            .then((res) => {
                if (!res.ok) throw new Error("Failed to fetch articles");
                return res.json();
            })
            .then((data: Response) => {
                setArticles(data);
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setLoading(false);
            });
    }, [fullName]);

    if (loading) return <p>Loading articles...</p>;
    if (error) return <p>Error: {error}</p>;

    const renderArticles = (title: string, list: Article[]) => (
        <>
            <h2>{title}</h2>
            {list.length > 0 ? (
                <ul style={{ listStyle: "none", padding: 0 }}>
                    {list.map(({ id, title, date, city, state }, index) => (
                        <li key={index} style={{ borderBottom: "1px solid #ccc", marginBottom: "1rem", paddingBottom: "1rem" }}>
                            <Link to={`/article/${id}`} style={{
                                fontSize: "1.2rem",
                                fontWeight: "bold",
                                color: "#cc0000",
                                textDecoration: "none"
                            }}>
                                {title}
                            </Link>
                            <div style={{ fontSize: "0.9rem", color: "#555", marginTop: "0.3rem" }}>
                                <span>{new Date(date).toLocaleDateString()}</span> | <span>{city}, {state}</span>
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>Brak artykułów</p>
            )}
        </>
    );

    return (
        <div style={{ padding: '20px' }}>
            <h1 style={{ textAlign: 'center' }}>Miasto: {fullName?.replace(/_/g, " ")}</h1>
            <div style={{ maxWidth: "800px", margin: "auto", padding: "1rem" }}>
                <h1>Artykuły</h1>
                {renderArticles('Lokalne', articles.local)}
                {renderArticles('Stanowe', articles.state)}
                {renderArticles('Krajowe', articles.global)}
            </div>
        </div>
    );
};

export default CityPage;
