import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';
import '../../../styles/shelter/main/dashBoard.css';

const data = [
  { name: '월', reservations: 12 },
  { name: '화', reservations: 16 },
  { name: '수', reservations: 9 },
  { name: '목', reservations: 13 },
  { name: '금', reservations: 17 },
  { name: '토', reservations: 21 },
  { name: '일', reservations: 7 }
];

function WeeklyReservationChart() {
  return (
    <div className="dashboard-widget">
      <div className="chart-title">주간 예약 현황</div>
      <ResponsiveContainer width="100%" height={343}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e4efe6" />
          <XAxis dataKey="name" />
          <YAxis allowDecimals={false}/>
          <Tooltip contentStyle={{ borderRadius: 10, border: 'none', background: "#effaf3" }}/>
          <Bar dataKey="reservations" fill="#184c24" radius={[6, 6, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
export default WeeklyReservationChart;
