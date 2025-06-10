import { Link, useLocation } from 'react-router-dom';
import '../../../styles/shelter/layouts/sidebar.css';

const menuList = [
 { name: '대시보드', path: '/shelter/dashboard' },
 { name: '공지사항', path: '/shelter/noticelist' },
 { name: '동물 관리 및 등록', path: '/shelter/doglist' },
 { name: '산책예약 관리', path: '/shelter/walkreservationlist' },
 { name: '산책로 관리', path: '/shelter/walklist' },
 { name: '입양 관리', path: '/shelter/adoptionmanager' },
 { name: '후원 관리', path: '/shelter/donationmanager' },
];

function Sidebar() {
  const location = useLocation();

  return (
    <aside className="sidebar">
      <div className="sidebar-logo">여기보개</div>
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
            <Link to={menu.path}>{menu.name}</Link>
          </li>
        ))}
      </ul>
    </aside>
  );
}

export default Sidebar;
