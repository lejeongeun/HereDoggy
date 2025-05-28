import DashboardCards from "../../components/shelter/dashboard/DashboardCards";
import NoticeBoard from "../../components/shelter/dashboard/NoticeBoard";
import WeeklyReservationChart from "../../components/shelter/dashboard/WeeklyReservationChart";
import AnimalRankingTable from "../../components/shelter/dashboard/AnimalRankingTable";


function DashBoard() {
    return(
        <>
            <DashboardCards/>
            <NoticeBoard/>
            <div style={{ display: "flex", gap: "18px" }}>
                <div style={{ maxWidth: 600, flex: 1 }}>
                    <WeeklyReservationChart />
                </div>
                <div style={{ maxWidth: 600, flex: 1 }}>
                    <AnimalRankingTable />
                </div>
            </div>
        </>
    )

}


export default DashBoard;