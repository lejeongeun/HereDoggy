import '../../../styles/shelter/layouts/topbar.css';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';
import { Bell } from 'lucide-react';

function Topbar() {
  const navigate = useNavigate();

  const logout = async () => {
    try {
      await axios.post("http://localhost:8080/api/shelters/logout", {}, { withCredentials: true });
      alert("로그아웃 되었습니다!");
      navigate("/");
    } catch (e) {
      alert("로그아웃 실패");
    }
  };

  // 알림 버튼 클릭 시 알림 페이지로 이동(또는 드롭다운 열기 등)
  const goNotification = () => {
    navigate("/shelter/notifications");
  };

  return (
    <header className="topbar">
      <div className="topbar-left">
        <span className="logo">관리자페이지</span>
      </div>
      <div className="topbar-right">
        <button className="notif-btn" onClick={goNotification}>
          <Bell size={22} />
          {/* 
          <span className="notif-badge">3</span> // 알림 갯수 표시용(선택)
          */}
        </button>
        <Link to="/shelter/mypage">
          <button className="mypage-btn">마이페이지</button>
        </Link>
        <button className="logout-btn" onClick={logout}>로그아웃</button>
      </div>
    </header>
  );
}
export default Topbar;
