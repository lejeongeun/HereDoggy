import React, { useState } from "react";
import { createNotice } from "../../../api/shelter/notice";
import { useNavigate } from "react-router-dom";

function NoticeWrite() {
  const [form, setForm] = useState({ title: "", content: "" });
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  // 입력 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  // 폼 제출 핸들러
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    if (!form.title.trim() || !form.content.trim()) {
      setError("제목과 내용을 모두 입력해 주세요.");
      return;
    }
    try {
      await createNotice(form);  // {title, content}
      alert("공지 작성이 완료되었습니다!");
      navigate("/shelter/noticelist"); // 목록으로 이동 (라우팅 경로에 따라 수정)
    } catch (err) {
      setError("공지 작성에 실패했습니다. 다시 시도해 주세요.");
      // 상세 오류 확인 시: console.log(err);
    }
  };

  return (
    <div className="container mt-4">
      <h2>공지 작성</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">제목</label>
          <input
            type="text"
            name="title"
            className="form-control"
            value={form.title}
            onChange={handleChange}
            required
            placeholder="공지 제목 입력"
          />
        </div>
        <div className="mb-3">
          <label className="form-label">내용</label>
          <textarea
            name="content"
            className="form-control"
            value={form.content}
            onChange={handleChange}
            required
            placeholder="공지 내용 입력"
            rows={5}
          />
        </div>
        {error && <div className="text-danger mb-2">{error}</div>}
        <button type="submit" className="btn btn-primary">등록</button>
        <button
          type="button"
          className="btn btn-secondary ms-2"
          onClick={() => navigate("/shelter/noticelist")}
        >
          취소
        </button>
      </form>
    </div>
  );
}

export default NoticeWrite;
