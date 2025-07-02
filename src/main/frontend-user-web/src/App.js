import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header/Header';
import Footer from './components/Footer/Footer';
import Home from './pages/Home/Home';
import Community from './pages/Community/Community';
import MyPage from './pages/MyPage/MyPage';
import Notification from './pages/Notification/Notification';
import Shelter from './pages/Shelter/Shelter';
import Adoption from './pages/Adoption/Adoption';
import Missing from './pages/Missing/Missing';
import Store from './pages/Store/Store';
import About from './pages/About/About';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/store" element={<Store />} />
            <Route path="/community" element={<Community />} />
            <Route path="/mypage" element={<MyPage />} />
            <Route path="/notification" element={<Notification />} />
            <Route path="/shelter" element={<Shelter />} />
            <Route path="/adoption" element={<Adoption />} />
            <Route path="/missing" element={<Missing />} />
            <Route path="/about" element={<About />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
