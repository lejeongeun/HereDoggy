import Sidebar from "./Sidebar";
import Topbar from "./Topbar";
import { Outlet } from 'react-router-dom';

function Layouts() {
  const sidebarWidth = 220;
  return (
    <div style={{ display: 'flex' }}>
      {/* 고정 sidebar */}
      <div style={{
        width: sidebarWidth,
        position: 'fixed',
        height: '100vh',
        zIndex: 100,
        background: '#fff',
        borderRight: '2px solid #e9eee6'
      }}>
        <Sidebar />
      </div>
      {/* 오른쪽 메인 */}
      <div style={{
        marginLeft: sidebarWidth,
        flex: 1,
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column'
      }}>
        {/* 고정 탑바 */}
        <div style={{
          position: 'fixed',
          top: 0,
          right: 0,
          left: sidebarWidth,
          background: '#fff',
          zIndex: 90,
          borderBottom: '2px solid #e9eee6',
          boxShadow: '0 2px 4px rgba(0,0,0,0.05)'
        }}>
          <Topbar />
        </div>
        {/* 본문 스크롤 */}
        <main style={{
          flex: 1,
          overflowY: 'auto',
          padding: '88px 24px 24px 24px',
          background: '#fafbfa',
          position: 'relative',
          zIndex: 1
        }}>
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default Layouts;
