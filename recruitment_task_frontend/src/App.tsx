import React from 'react';
import './App.css';
// @ts-ignore
import CitySearchBar from "./components/city_search_bar.tsx";
import {Route, Routes} from "react-router-dom";
import CityPage from "./components/CityPage";
import ArticlePage from "./components/article_page";

const App: React.FC = () => <Routes>
    <Route path="/" element={<CitySearchBar />} />
    <Route path="/city/:fullName" element={<CityPage/>} />
    <Route path="/article/:id" element={<ArticlePage/>} />
</Routes>;

export default App;
