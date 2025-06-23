import '../../../styles/shelter/layouts/topbar.css';
import axios from 'axios';
import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Bell } from 'lucide-react';

// 현재 사용 가능한 API를 호출
const getUnreadNotifications = async () => {
  try {
    const response = await axios.get("/api/notifications/unread", { withCredentials: true });
    return response.data;  // 읽지 않은 알림의 목록을 반환
  } catch (error) {
    console.error("알림을 가져오는 데 실패했습니다.", error);
    return [];
  }
};

function Topbar() {
  const [unreadCount, setUnreadCount] = useState(0);  // 읽지 않은 알림 수 상태
  const navigate = useNavigate();

  // 알림 갯수 가져오기
  const fetchUnreadNotifications = async () => {
    try {
      const data = await getUnreadNotifications();  // 읽지 않은 알림 가져오기
      setUnreadCount(data.length);  // 읽지 않은 알림 수
    } catch (error) {
      console.error("알림 수를 가져오는 데 실패했습니다.", error);
    }
  };

  // 로그인 로그아웃 처리
  const logout = async () => {
    try {
      await axios.post("http://localhost:8080/api/shelters/logout", {}, { withCredentials: true });
      alert("로그아웃 되었습니다!");
      navigate("/");  // 홈 페이지로 이동
    } catch (e) {
      alert("로그아웃 실패");
    }
  };

  // 알림 버튼 클릭 시 알림 페이지로 이동
  const goNotification = () => {
    navigate("/shelter/notifications");  // 알림 페이지로 이동
  };

  // 컴포넌트 마운트 시 읽지 않은 알림 수 가져오기
  useEffect(() => {
    fetchUnreadNotifications();
  }, []);  // 한 번만 호출

  return (
    <header className="topbar">
      <div className="topbar-left">
        <span className="logo">관리자페이지</span>
      </div>
      <div className="topbar-right">
        <button className="notif-btn" onClick={goNotification}>
          <Bell size={22} />
          {unreadCount > 0 && <span className="notif-badge">{unreadCount}</span>} {/* 배지 표시 */}
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
