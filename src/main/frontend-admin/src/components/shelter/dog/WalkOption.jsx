import { useEffect, useState } from "react";
import api from "../../../api/shelter/api";
import DatePicker from "react-datepicker";
import { ko } from "date-fns/locale";
import "react-datepicker/dist/react-datepicker.css";
import "../../../styles/shelter/walkoption/walkOption.css"; // CSS 파일 import

function WalkOption({ dogId, sheltersId }) {
  const [options, setOptions] = useState([]);
  const [date, setDate] = useState(null);
  const [startTime, setStartTime] = useState(null);
  const [endTime, setEndTime] = useState(null);

  // 기존 옵션 불러오기
  useEffect(() => {
    async function fetchOptions() {
      try {
        const { data } = await api.get(
          `/api/shelters/${sheltersId}/dogs/${dogId}/walk-options`
        );
        setOptions(data);
      } catch (err) {
        setOptions([]);
      }
    }
    fetchOptions();
  }, [dogId, sheltersId]);

  // 옵션 등록
  async function handleAdd() {
    if (!date || !startTime || !endTime) {
      alert("날짜/시간을 모두 선택하세요.");
      return;
    }
    try {
      await api.post(
        `/api/shelters/${sheltersId}/dogs/${dogId}/walk-options`,
        {
          date: date.toISOString().slice(0, 10),
          startTime: startTime.toTimeString().slice(0, 5),
          endTime: endTime.toTimeString().slice(0, 5),
        }
      );
      alert("옵션 등록 완료!");
      setDate(null);
      setStartTime(null);
      setEndTime(null);
      // 다시 조회
      const { data } = await api.get(
        `/api/shelters/${sheltersId}/dogs/${dogId}/walk-options`
      );
      setOptions(data);
    } catch (err) {
      alert("등록 실패");
    }
  }

  // 옵션 삭제
  async function handleDelete(optionId) {
    if (!window.confirm("정말 삭제할까요?")) return;
    try {
      await api.delete(
        `/api/shelters/${sheltersId}/dogs/${dogId}/walk-options/${optionId}`
      );
      setOptions(opts => opts.filter(o => o.id !== optionId));
    } catch (err) {
      alert("삭제 실패");
    }
  }

  return (
    <div style={{ marginTop: 12 }}>
      <div className="walk-option-add-row">
        <DatePicker
          selected={date}
          onChange={d => setDate(d)}
          dateFormat="yyyy/MM/dd"
          locale={ko}
          placeholderText="날짜"
        />
        <DatePicker
          selected={startTime}
          onChange={t => setStartTime(t)}
          showTimeSelect
          showTimeSelectOnly
          timeIntervals={30}
          timeCaption="시작"
          dateFormat="HH:mm"
          locale={ko}
          placeholderText="시작 시간"
          minTime={new Date().setHours(10, 0, 0, 0)}
          maxTime={new Date().setHours(16, 0, 0, 0)}
        />
        <span style={{ margin: "0 10px" }}>~</span>
        <DatePicker
          selected={endTime}
          onChange={t => setEndTime(t)}
          showTimeSelect
          showTimeSelectOnly
          timeIntervals={30}
          timeCaption="종료"
          dateFormat="HH:mm"
          locale={ko}
          placeholderText="종료 시간"
          minTime={startTime || new Date().setHours(10, 0, 0, 0)}
          maxTime={new Date().setHours(16, 0, 0, 0)}
          disabled={!startTime}
        />
        <button
          type="button"
          className="walk-option-add-btn"
          onClick={handleAdd}
        >
          옵션 추가
        </button>
      </div>

      <div>
        {options.length === 0 ? (
          <p style={{ color: "#aaa" }}>등록된 산책 예약 옵션이 없습니다.</p>
        ) : (
          <ul className="walk-option-list">
            {options.map(opt => (
              <li key={opt.id}>
                {opt.date} {opt.startTime} ~ {opt.endTime}
                <button
                  type="button"
                  className="walk-option-delete-btn"
                  onClick={() => handleDelete(opt.id)}
                >
                  삭제
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>
      
        <div>

            
        </div>

    </div>
  );
}

export default WalkOption;
