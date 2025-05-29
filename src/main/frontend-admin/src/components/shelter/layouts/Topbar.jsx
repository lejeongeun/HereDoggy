import '../../../styles/shelter/layouts/topbar.css';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

function Topbar() {
  const navigate = useNavigate();

  // 로그아웃 함수
  const logout = async () => {
    try {
      await axios.post("http://localhost:8080/api/shelters/logout", {}, { withCredentials: true });
      alert("로그아웃 되었습니다!");
      navigate("/"); // 홈으로 이동
    } catch (e) {
      alert("로그아웃 실패");
    }
  };

  return (
    <header className="topbar">
      <div className="topbar-left">
        <span className="logo">로고</span>
      </div>
      <div className="topbar-right">
        <Link to="/shelter/mypage">
          <button className="mypage-btn">마이페이지</button>
        </Link>
        <button className="logout-btn" onClick={logout}>로그아웃</button>
      </div>
    </header>
  );
}

export default Topbar;
