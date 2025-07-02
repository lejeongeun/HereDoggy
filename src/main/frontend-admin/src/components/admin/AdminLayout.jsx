import React from "react";
import { Link, Outlet } from "react-router-dom";
import "../../styles/admin/adminLayout/adminLayout.css";

const menuItems = [
  { label: "대시보드", to: "/admin/dashboard" },
  { label: "사용자 관리", to: "/admin/users" },
  { label: "보호소 관리", to: "/admin/shelter" },
  { label: "신고 관리", to: "/admin/report" },
  { label: "문의 관리", to: "/admin/inquiry" },
  { label: "통계", to: "/admin/statistics" },
  { label: "시스템 설정", to: "/admin/settings" }
];

function AdminLayout() {
  return (
    <div className="admin-root">
      {/* 사이드바 */}
      <aside className="admin-sidebar">
        <div className="admin-logo">ADMIN</div>
        <nav>
          <ul>
      {menuItems.map(item => (
        <li key={item.to}>
          <Link className="admin-link" to={item.to}>{item.label}</Link>
        </li>
      ))}
    </ul>
        </nav>
      </aside>
      {/* 메인 컨텐츠 */}
      <div className="admin-content">
        {/* 탑바 */}
        <header className="admin-header">
          <div>여기보개 시스템 관리자</div>
          <button className="admin-logout-btn">
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
