import React from 'react';
import { useParams } from 'react-router-dom';

const CityPage: React.FC = () => {
    const { fullName } = useParams();

    return (
        <div style={{ padding: '20px' }}>
            <h1>Miasto: {fullName}</h1>
        </div>
    );
};

export default CityPage;
