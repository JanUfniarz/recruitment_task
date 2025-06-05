// ArticlePage.tsx
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const ArticlePage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [article, setArticle] = useState<any>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetch(`/api/news/${id}`)
            .then(res => {
                if (!res.ok) throw new Error("Nie udało się pobrać artykułu");
                return res.json();
            })
            .then(data => {
                setArticle(data);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
                setLoading(false);
            });
    }, [id]);

    if (loading) return <p>Ładowanie...</p>;
    if (error) return <p>Błąd: {error}</p>;
    if (!article) return <p>Brak artykułu</p>;

    return (
        <div style={{ padding: '20px', maxWidth: '800px', margin: 'auto' }}>
            <h1>{article.title}</h1>
            <p style={{ fontSize: '0.9rem', color: '#777' }}>
                {new Date(article.date).toLocaleDateString()} | {article.city}, {article.state}
            </p>
            <div style={{ marginTop: '1rem', lineHeight: 1.6 }}>
                {article.text}
            </div>
        </div>
    );
};

export default ArticlePage;
