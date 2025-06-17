import { useEffect, useState } from "react";
import api from "../../../api/shelter/api";
import DatePicker from "react-datepicker";
import { ko } from "date-fns/locale";
import "react-datepicker/dist/react-datepicker.css";
import "../../../styles/shelter/walkoption/walkOption.css";

function WalkOption({ dogId, sheltersId }) {
  const [unavailableDate, setUnavailableDate] = useState(null);
  const [unavailableDates, setUnavailableDates] = useState([]);

  useEffect(() => {
    fetchUnavailableDates();
  }, [dogId, sheltersId]);

  async function fetchUnavailableDates() {
    try {
      const { data } = await api.get(
        `/api/shelters/${sheltersId}/dogs/${dogId}/unavailable-dates`
      );
      setUnavailableDates(data);
    } catch (err) {
      setUnavailableDates([]);
    }
  }

  async function handleAddUnavailable() {
    if (!unavailableDate) {
      alert("날짜를 선택하세요.");
      return;
    }
    try {
      await api.post(
        `/api/shelters/${sheltersId}/dogs/${dogId}/unavailable-dates`,
        {
          dates: [unavailableDate.toISOString().slice(0, 10)],
        }
      );
      alert("예약 불가일 등록 완료!");
      setUnavailableDate(null);
      fetchUnavailableDates();
    } catch (err) {
      alert("등록 실패");
    }
  }

  async function handleDeleteUnavailable(unavailableId) {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    try {
      await api.delete(
        `/api/shelters/${sheltersId}/dogs/${dogId}/unavailable-dates/${unavailableId}`
      );
      alert("삭제되었습니다.");
      fetchUnavailableDates();
    } catch (err) {
      alert("삭제 실패");
    }
  }

  return (
    <div style={{ marginTop: 12 }}>
      <div className="walk-option-add-row">
        <DatePicker
          selected={unavailableDate}
          onChange={(d) => setUnavailableDate(d)}
          dateFormat="yyyy/MM/dd"
          locale={ko}
          placeholderText="예약 불가일"
          className="date-picker-input"
        />
        <button
          type="button"
          className="walk-option-add-btn"
          onClick={handleAddUnavailable}
        >
          예약 불가일 추가
        </button>
      </div>

      {unavailableDates.length > 0 ? (
        <ul className="walk-option-list">
        {unavailableDates.map((dateStr, idx) => (
          <li key={idx}>
            <span className="date-badge">{dateStr}</span>
            <button
              type="button"
              className="walk-option-delete-btn"
              onClick={() => alert("삭제하려면 백엔드에서 id도 반환해야 해요")}
            >
              삭제
            </button>
          </li>
        ))}
      </ul>
      ) : (
        <p style={{ color: "#aaa" }}>등록된 예약 불가일이 없습니다.</p>
      )}
    </div>
  );
}

export default WalkOption;
