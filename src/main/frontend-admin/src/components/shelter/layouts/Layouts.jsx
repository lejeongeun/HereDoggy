import Sidebar from "./Sidebar";
import Topbar from "./Topbar";
import { Outlet } from 'react-router-dom';

function Layouts() {
    return(
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      <Sidebar />  {/* 좌측 고정 */}
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <Topbar />  {/* 상단 고정 */}
        <main style={{ flex: 1, background: '#fafafa', padding: '24px' }}>
          <Outlet />  {/* 메인 컨텐츠 */}
        </main>
      </div>
    </div>
    );
}

export default Layouts;