import React, { useState } from "react";
import { createNotice } from "../../../api/shelter/notice";
import { useNavigate } from "react-router-dom";
import "../../../styles/shelter/notice/noticeWrite.css"; // 경로 맞게!

function NoticeWrite() {
  const [form, setForm] = useState({ title: "", content: "" });
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    if (!form.title.trim() || !form.content.trim()) {
      setError("제목과 내용을 모두 입력해 주세요.");
      return;
    }
    try {
      await createNotice(form);
      alert("공지 작성이 완료되었습니다!");
      navigate("/shelter/noticelist");
    } catch (err) {
      setError("공지 작성에 실패했습니다. 다시 시도해 주세요.");
    }
  };

  return (
    <div className="nw-wrap">
      <div className="nw-box">
        <h2 className="nw-title">공지 작성</h2>
        <form className="nw-form" onSubmit={handleSubmit}>
          <label className="nw-label">
            제목
            <input
              type="text"
              name="title"
              className="nw-input"
              value={form.title}
              onChange={handleChange}
              required
              placeholder="공지 제목 입력"
              maxLength={100}
            />
          </label>
          <label className="nw-label">
            내용
            <textarea
              name="content"
              className="nw-textarea"
              value={form.content}
              onChange={handleChange}
              required
              placeholder="공지 내용 입력"
              rows={8}
              maxLength={2000}
            />
          </label>
          {error && <div className="nw-error">{error}</div>}
          <div className="nw-btns">
            <button type="submit" className="nw-btn nw-btn-main">등록</button>
            <button
              type="button"
              className="nw-btn nw-btn-cancel"
              onClick={() => navigate("/shelter/noticelist")}
            >
              취소
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default NoticeWrite;
