import { Link } from 'react-router-dom';
import '../../../styles/shelter/layouts/sidebar.css';

const menuList = [
 { name: '대시보드', path: '/shelter/dashboard' },
 { name: '공지사항', path: '/shelter/noticeboardlist' },
 { name: '동물 관리 및 등록', path: '/shelter/doglist' },
 { name: '산책예약 관리', path: '/shelter/walkreservationmanager' },
 { name: '산책로 관리', path: '/shelter/routemanager' },
 { name: '입양 관리', path: '/shelter/adoptionmanager' },
 { name: '후원 관리', path: '/shelter/donationmanager' },
];


function Sidebar() {
    return (
        <aside className="sidebar">
            <h1>여기보개</h1>
            <ul>
                {menuList.map((menu, i) => (
                    <li key={i}>
                        <Link to={menu.path}>{menu.name}</Link>
                    </li>
                ))}
            </ul>
        </aside>
    );
}

export default Sidebar;