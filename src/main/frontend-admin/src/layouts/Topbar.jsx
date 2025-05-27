import '../styles/layouts/topbar.css';
import Login from '../components/Login';

function Topbar() {
  return (
    <header className="topbar">
      <div className="topbar-left">
        <span className="logo">로고</span>
      </div>
      <div className="topbar-right">
        <Login />
      </div>
    </header>
  );
}
export default Topbar;