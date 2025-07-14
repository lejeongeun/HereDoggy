import Sidebar from "./Sidebar";
import Topbar from "./Topbar";
import { Outlet } from 'react-router-dom';

function Layouts() {
  return (
    <div className="layout-container">
      {/* 고정 sidebar */}
      <div className="sidebar-wrapper">
        <Sidebar />
      </div>
      {/* 오른쪽 메인 */}
      <div className="content-wrapper">
        {/* 고정 탑바 */}
        <div className="topbar-wrapper">
          <Topbar />
        </div>
        {/* 본문 스크롤 */}
        <main className="main-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default Layouts;
