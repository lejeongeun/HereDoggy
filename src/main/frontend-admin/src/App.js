import React from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import Home from './pages/Home';
import SignUp from './pages/SignUp';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/admin/login" element={<Login />}/>
        <Route path="/admin/home" element={<Home />}/>
        <Route path="/admin/signup" element={<SignUp />}/>
      </Routes>
    </Router>
  );
}

export default App;
