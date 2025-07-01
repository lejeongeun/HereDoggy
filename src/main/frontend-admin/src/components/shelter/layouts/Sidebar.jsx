import { Link, useLocation } from 'react-router-dom';
import { 
  LuLayoutDashboard,
  LuMegaphone,
  LuDog,
  LuRoute,
  LuCalendarCheck,
  LuHouse, 
  LuHeartHandshake,
  LuCircleHelp,      
  LuSettings       
} from "react-icons/lu"; 
import '../../../styles/shelter/layouts/sidebar.css';
import logoImg from '../../../assets/Logo.png';

const menuList = [
  { name: '대시보드', path: '/shelter/dashboard', icon: <LuLayoutDashboard /> },
  { name: '공지사항', path: '/shelter/noticelist', icon: <LuMegaphone /> },
  { name: '동물 관리 및 등록', path: '/shelter/doglist', icon: <LuDog /> },
  { name: '산책로 관리', path: '/shelter/walkmanager', icon: <LuRoute /> },
  { name: '산책예약 관리', path: '/shelter/walkreservationlist', icon: <LuCalendarCheck /> },
  { name: '입양 관리', path: '/shelter/adoptionlist', icon: <LuHouse /> },
  { name: '후원 관리', path: '/shelter/donationlist', icon: <LuHeartHandshake /> },
];

function Sidebar() {
  const location = useLocation();

  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <img src={logoImg} alt="여기보개 로고" style={{ height: 110, width: 120 }} />
      </div>
      <ul className="sidebar-menu">
        {menuList.map((menu, i) => (
          <li
            key={i}
            className={
              location.pathname.startsWith(menu.path)
                ? 'sidebar-menu-item active'
                : 'sidebar-menu-item'
            }
          >
            <Link to={menu.path}>
              <span className="sidebar-menu-icon">{menu.icon}</span>
              {menu.name}
            </Link>
          </li>
        ))}
      </ul>
      {/* 하단 영역 추가 */}
      <div className="sidebar-bottom">
        <div className="sidebar-brand">
          <img src={logoImg} alt="여기보개 미니로고" style={{ height: 50, width: 45, marginRight: 6 }} />
          <div>
            <div className="sidebar-brand-version">v1.0.0</div>
          </div>
        </div>     
        <div className="sidebar-bottom-icons">
          <button className="sidebar-icon-btn" title="문의하기"><LuCircleHelp size={20} /></button>
          <button className="sidebar-icon-btn" title="설정"><LuSettings size={20} /></button>
        </div>
    
      </div>
    </aside>
  );
}

export default Sidebar;
