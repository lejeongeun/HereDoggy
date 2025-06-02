import DashboardCards from "../../components/shelter/dashboard/DashboardCards";
import NoticeBoard from "../../components/shelter/dashboard/NoticeBoard";
import WeeklyReservationChart from "../../components/shelter/dashboard/WeeklyReservationChart";
import AnimalRankingTable from "../../components/shelter/dashboard/AnimalRankingTable";
import '../../styles/shelter/main/dashBoard.css';

function DashBoard() {
    return (
        <div className="dashboard-main-bg">
            <div className="dashboard-cards-row">
                <DashboardCards />
            </div>
            <div className="dashboard-widget">
                <NoticeBoard />
            </div>
            <div className="dashboard-widgets-row">
                <div>
                    <WeeklyReservationChart />
                </div>
                <div>
                    <AnimalRankingTable />
                </div>
            </div>
        </div>
    );
}

export default DashBoard;
