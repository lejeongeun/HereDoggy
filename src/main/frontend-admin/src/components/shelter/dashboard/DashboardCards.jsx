import '../../../styles/shelter/main/dashboardcards.css'
import DogCount from './DogCount';
import AdoptionCount from './AdoptedCount';
import { FaClipboardList, FaDog, FaUserCheck, FaWalking } from 'react-icons/fa';

function DashboardCards({ stats ={
   todayReservation: 0,
   currentDogsCount: 0,
   thisMonthAdoption: 0,
   thisWeekWalk: 0} }) {
  // stats: { 예약, 보호중, 입양, 산책 } 등 객체로 받음
  // 증감률 0%로 고정
  const changes = {
    todayReservation: 0,
    currentDogsCount: 0,
    thisMonthAdoption: 0,
    thisWeekWalk: 0
  };
  return (
    <div className="dashboard-cards">
      <div className="dashboard-card card-reservation">
        <FaClipboardList size={32} color="#184c24" style={{marginBottom: 8}} />
        <div className="card-value">{stats.todayReservation}</div>
        <div className="card-label">오늘 예약</div>
        <div className={`card-change up`}>0%</div>
      </div>
      <div className="dashboard-card card-dogs">
        <FaDog size={32} color="#2d6cdf" style={{marginBottom: 8}} />
        <DogCount count={stats.currentDogsCount} />
        <div className={`card-change up`}>0%</div>
      </div>
      <div className="dashboard-card card-adoption">
        <FaUserCheck size={32} color="#e6a23c" style={{marginBottom: 8}} />
        <AdoptionCount count={stats.currentDogsCount} />
        <div className={`card-change up`}>0%</div>
      </div>
      <div className="dashboard-card card-walk">
        <FaWalking size={32} color="#1abc9c" style={{marginBottom: 8}} />
        <div className="card-value">{stats.thisWeekWalk}</div>
        <div className="card-label">이번 주 산책</div>
        <div className={`card-change up`}>0%</div>
      </div>
    </div>
  );
}

export default DashboardCards;
