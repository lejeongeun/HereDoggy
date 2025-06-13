import '../../../styles/shelter/main/dashboardcards.css'
import DogCount from './DogCount';
import AdoptionCount from './AdoptedCount';

function DashboardCards({ stats ={
   todayReservation: 0,
   currentDogsCount: 0,
   thisMonthAdoption: 0,
   thisWeekWalk: 0} }) {
  // stats: { 예약, 보호중, 입양, 산책 } 등 객체로 받음
  return (
    <div className="dashboard-cards">
      <div className="dashboard-card">
        <div className="card-value">{stats.todayReservation}</div>
        <div className="card-label">오늘 예약</div>
      </div>
      <div className="dashboard-card">
        <DogCount count={stats.currentDogsCount} />
      </div>

      <div className="dashboard-card">
        <AdoptionCount count={stats.currentDogsCount} />
      </div>
      <div className="dashboard-card">
        <div className="card-value">{stats.thisWeekWalk}</div>
        <div className="card-label">이번 주 산책</div>
      </div>
    </div>
  );
}

export default DashboardCards;
