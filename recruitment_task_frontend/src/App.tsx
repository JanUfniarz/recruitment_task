import React from 'react';
import './App.css';
// @ts-ignore
import CitySearchBar from "./components/city_search_bar.tsx";
import {Route, Routes} from "react-router-dom";
import CityPage from "./components/CityPage";

const App: React.FC = () => {
    return (
        <Routes>
            <Route path="/" element={<CitySearchBar />} />
            <Route path="/city/:fullName" element={<CityPage/>} />
        </Routes>
    );
};

export default App;
