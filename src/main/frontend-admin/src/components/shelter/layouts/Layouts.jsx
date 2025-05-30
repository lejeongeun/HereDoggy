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
          position: 'sticky',
          top: 0,
          background: '#fff',
          zIndex: 1
        }}>
          <Topbar />
        </div>
        {/* 본문 스크롤 */}
        <main style={{
          flex: 1,
          overflowY: 'auto',
          padding: 24,
          background: '#fafbfa'
        }}>
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default Layouts;
