import React, { useState } from "react";
import { Link, Outlet, useNavigate } from "react-router-dom";
import "../../../styles/admin/adminLayout/adminLayout.css";
import { adminLogout } from "../../../api/system/admin"; // 또는 경로에 따라 조정


const menuItems = [
  { label: "대시보드", to: "/admin/dashboard", icon: "fas fa-tachometer-alt" },
  { label: "사용자 관리", to: "/admin/users", icon: "fas fa-users" },
  { label: "보호소 관리", to: "/admin/shelter", icon: "fas fa-home" },
  { label: "신고 관리", to: "/admin/report", icon: "fas fa-flag" },
  { label: "문의 관리", to: "/admin/inquiry", icon: "fas fa-question-circle" },
  { label: "통계", to: "/admin/statistics", icon: "fas fa-chart-line" },
  { label: "시스템 설정", to: "/admin/settings", icon: "fas fa-cog" }
];



function AdminLayout() {
  const navigate = useNavigate();
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  const handleLogout = async () => {
  try {
    await adminLogout(); // 서버에 로그아웃 요청
    navigate("/"); // 로그인 페이지로 이동
  } catch (error) {
    alert("로그아웃 실패");
  }
};

  return (
    <div className={`admin-root ${isSidebarOpen ? 'sidebar-open' : ''}`}>
      {/* 사이드바 */}
      <aside className={`admin-sidebar ${isSidebarOpen ? 'open' : ''}`}>
        <div className="admin-logo">ADMIN</div>
        <nav>
          <ul>
      {menuItems.map(item => (
        <li key={item.to}>
          <Link className="admin-link" to={item.to}>
            <i className={`${item.icon} menuIcon`}></i> {item.label}
          </Link>
        </li>
      ))}
    </ul>
        </nav>
        <div className="admin-sidebar-footer">
          <p className="version-info">v1.0.0</p>
          <p className="copyright-info">&copy; 2024 HereDoggy</p>
        </div>
      </aside>
      {/* 메인 컨텐츠 */}
      <div className="admin-content">
        {/* 탑바 */}
        <header className="admin-header">
          <button className="hamburger-menu" onClick={toggleSidebar}>
            <i className="fas fa-bars"></i>
          </button>
          <div>여기보개 시스템 관리자</div>
          <button className="admin-logout-btn" onClick={handleLogout}>
            로그아웃
          </button>
        </header>
        {/* 중앙 컨텐츠 */}
        <div className="admin-main">
          <Outlet />
        </div>
      </div>
    </div>
  );
}

export default AdminLayout;
