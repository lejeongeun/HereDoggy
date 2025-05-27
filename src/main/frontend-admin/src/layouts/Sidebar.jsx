import { Link } from 'react-router-dom';
import '../styles/layouts/sidebar.css';

const menuList = [
 { name: '대시보드', path: '/admin/home' },
 { name: '공지사항', path: '/' },
 { name: '산책예약 관리', path: '/' },
 { name: '동물 등록 및 관리', path: '/' },
 { name: '후원 관리', path: '/' },
 { name: '산책로 관리', path: '/' },
 { name: '입양 관리', path: '/' },
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