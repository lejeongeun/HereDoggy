import '../../../styles/shelter/layouts/topbar.css';
import axios from 'axios';
import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Bell } from 'lucide-react';
import useSseNotifications from './useSseNotifications';

const getUnreadNotifications = async () => {
  try {
    const response = await axios.get("/api/notifications/unread", { withCredentials: true });
    return response.data;
  } catch (error) {
    console.error("알림을 가져오는 데 실패했습니다.", error);
    return [];
  }
};

function Topbar() {
  const [unreadCount, setUnreadCount] = useState(0);
  const navigate = useNavigate();

  const fetchUnreadNotifications = async () => {
    try {
      const data = await getUnreadNotifications();
      setUnreadCount(data.length);
    } catch (error) {
      console.error("알림 수를 가져오는 데 실패했습니다.", error);
    }
  };

  const logout = async () => {
    try {
      await axios.post("http://localhost:8080/api/shelters/logout", {}, { withCredentials: true });
      alert("로그아웃 되었습니다!");
      navigate("/");
    } catch (e) {
      alert("로그아웃 실패");
    }
  };

  const goNotification = () => {
    setUnreadCount(0); // 페이지 이동 시 뱃지 0으로 초기화 (원하면 유지해도 됨)
    navigate("/shelter/notifications");
  };

  useEffect(() => {
    fetchUnreadNotifications();
  }, []);

  // SSE로 실시간 알림 뱃지 증가
  useSseNotifications(() => setUnreadCount((c) => c + 1));

  return (
    <header className="topbar">
      <div className="topbar-left">
        <span className="logo">관리자페이지</span>
      </div>
      <div className="topbar-right">
        <button className="notif-btn" onClick={goNotification}>
          <Bell size={22} />
          {unreadCount > 0 && <span className="notif-badge">{unreadCount}</span>}
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
