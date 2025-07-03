import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
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
import Login from './pages/Login/Login';
import Signup from './pages/Signup/Signup';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Header />
          <main className="main-content">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/store" element={<Store />} />
              <Route path="/community" element={<Community />} />
              <Route path="/shelter" element={<Shelter />} />
              <Route path="/about" element={<About />} />
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<Signup />} />
              
              {/* 인증이 필요한 페이지들 */}
              <Route path="/mypage" element={
                <ProtectedRoute>
                  <MyPage />
                </ProtectedRoute>
              } />
              <Route path="/notification" element={
                <ProtectedRoute>
                  <Notification />
                </ProtectedRoute>
              } />
              <Route path="/adoption" element={
                <ProtectedRoute>
                  <Adoption />
                </ProtectedRoute>
              } />
              <Route path="/missing" element={
                <ProtectedRoute>
                  <Missing />
                </ProtectedRoute>
              } />
            </Routes>
          </main>
          <Footer />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
