import '../../../styles/shelter/layouts/topbar.css';
import { Link } from 'react-router-dom';

function Topbar() {
  return (
    <header className="topbar">
      <div className="topbar-left">
        <span className="logo">로고</span>
      </div>
      <div className="topbar-right">
         <Link to="/shelter/mypage">
            <button className="mypage-btn">마이페이지</button>
         </Link>
      </div>
    </header>
  );
}
export default Topbar;