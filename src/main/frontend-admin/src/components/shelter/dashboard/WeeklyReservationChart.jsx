import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';
// npm install recharts

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
    <div className="card p-3" style={{ maxWidth: 400 }}>
      <h5>주간 예약 현황</h5>
      <ResponsiveContainer width="100%" height={230}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis allowDecimals={false}/>
          <Tooltip />
          <Bar dataKey="reservations" fill="#26a869" radius={[6, 6, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
export default WeeklyReservationChart;
