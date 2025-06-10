import { useEffect, useState } from "react";
import { getReservations } from "../../api/shelter/reservation";
import { Link } from "react-router-dom";

function WalkReservationList() {
  const sheltersId = localStorage.getItem("shelters_id");
  const [reservations, setReservations] = useState([]);

  useEffect(() => {
    if (!sheltersId) return;
    getReservations(sheltersId).then(res => setReservations(res.data));
  }, []);

  return (
    <div>
      <h2>산책 예약 목록</h2>
      <ul>
        {reservations.map(r => (
          <li key={r.id}>
            <Link to={`/shelter/walk-reservations/${r.id}`}>
              {r.memberName} - {r.dogName} ({r.date} {r.startTime}~{r.endTime}) - {r.walkReservationStatus}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default WalkReservationList;
