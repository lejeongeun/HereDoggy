// src/components/common/ShelterProtectedRoute.jsx
import React, { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { checkShelterAuth } from "../../api/shelter/auth"; // 보호소 인증 확인 API

const ShelterProtectedRoute = () => {
  const [isAuth, setIsAuth] = useState(null);

  useEffect(() => {
    checkShelterAuth().then(setIsAuth);
  }, []);

  if (isAuth === null) return <div>로딩 중...</div>;
  return isAuth ? <Outlet /> : <Navigate to="/shelter/login" replace />;
};

export default ShelterProtectedRoute;
