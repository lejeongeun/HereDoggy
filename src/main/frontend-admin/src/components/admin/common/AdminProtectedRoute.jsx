// src/components/common/AdminProtectedRoute.jsx
import React, { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { checkAdminAuth } from "../../api/admin"; // 인증 확인 API 호출

const AdminProtectedRoute = () => {
  const [isAuth, setIsAuth] = useState(null);

  useEffect(() => {
    checkAdminAuth().then(setIsAuth);
  }, []);

  if (isAuth === null) return <div>로딩 중...</div>;
  return isAuth ? <Outlet /> : <Navigate to="/admin/login" replace />;
};

export default AdminProtectedRoute;
