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
    <div className="dashboard-widget" style={{background:'#fff', borderRadius:16, boxShadow:'0 3px 16px rgba(44,69,67,0.07)'}}>
      <div className="chart-title" style={{color:'#222', fontWeight:700}}>
        주간 예약 현황
      </div>
      <ResponsiveContainer width="100%" height={276}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e4efe6" />
          <XAxis dataKey="name" tick={{fill:'#222', fontWeight:600}} />
          <YAxis allowDecimals={false} tick={{fill:'#222', fontWeight:600}}/>
          <Tooltip contentStyle={{ borderRadius: 10, border: 'none', background: "#fff", color:'#222', fontWeight:600 }}/>
          <Bar dataKey="reservations" fill="#184c24" radius={[6, 6, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
export default WeeklyReservationChart;
