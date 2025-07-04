import '../../../styles/shelter/layouts/topbar.css';
import api from '../../../api/shelter/api'; // api 인스턴스 반드시 import!
import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Bell, LogOut } from 'lucide-react';
import useSseNotifications from './useSseNotifications';
import { getShelterProfile } from '../../../api/shelter/shelter';
import { FaHome } from 'react-icons/fa';

// 알림 개수 API (api 인스턴스 사용!)
const getUnreadNotifications = async () => {
  try {
    const response = await api.get("/api/notifications/unread");
    return response.data;
  } catch (error) {
    console.error("알림을 가져오는 데 실패했습니다.", error);
    return [];
  }
};

function Topbar() {
  const [unreadCount, setUnreadCount] = useState(0);
  const [shelterName, setShelterName] = useState("");
  const navigate = useNavigate();

  // 보호소 이름 불러오기
  const fetchShelterName = async () => {
    try {
      const { data } = await getShelterProfile();
      setShelterName(data.name || "보호소");
    } catch {
      setShelterName("보호소");
    }
  };

  // 알림 개수 불러오기
  const fetchUnreadNotifications = async () => {
    try {
      const data = await getUnreadNotifications();
      setUnreadCount(data.length);
    } catch (error) {
      console.error("알림 수를 가져오는 데 실패했습니다.", error);
    }
  };

  const logout = async () => {
    alert("로그아웃 되었습니다!");
    navigate("/");
    try {
      await api.post("/api/shelters/logout", {}, { withCredentials: true });
      alert("로그아웃 되었습니다!");
      navigate("/");
    } catch (e) {
      alert("로그아웃 실패");
    }
  };

  const goNotification = () => {
    setUnreadCount(0);
    navigate("/shelter/notifications");
  };

  useEffect(() => {
    fetchUnreadNotifications();
    fetchShelterName();
  }, []);

  // SSE로 실시간 알림 뱃지 증가
  useSseNotifications(() => setUnreadCount((c) => c + 1));

  return (
    <header className="topbar">
      <div className="topbar-right">
        <Link to="/shelter/profile" className="sheltername">
          <FaHome className="icon" />
          <span className="sheltername-text">{shelterName}</span>
        </Link>
        <button className="notif-btn" onClick={goNotification}>
          <Bell size={22} />
          {unreadCount > 0 && <span className="notif-badge">{unreadCount}</span>}
        </button>
        <button className="logout-btn" onClick={logout}>
          <LogOut size={18} />
          <span>로그아웃</span>
        </button>
      </div>
    </header>
  );
}

export default Topbar;
