import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getNoticeDetail, updateNotice } from "../../../api/shelter/notice";
import "../../../styles/shelter/notice/noticeUpdate.css";

function NoticeUpdate() {
  const [form, setForm] = useState({ title: "", content: "" });
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { id } = useParams();
  const [images, setImages] = useState([]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  useEffect(() => {
    async function fetchNotice() {
      try {
        const res = await getNoticeDetail(id);
        setForm({ title: res.data.title, content: res.data.content });
      } catch {
        setError("공지 정보를 불러오지 못했습니다.");
      }
    }
    if (id) fetchNotice();
  }, [id]);

const handleSubmit = async (e) => {
  e.preventDefault();
  if (!form.title.trim() || !form.content.trim()) {
    setError("제목과 내용을 모두 입력해 주세요.");
    return;
  }
  try {
    const formData = new FormData();
    formData.append("info", JSON.stringify({ title: form.title, content: form.content }));
    images.forEach((file) => formData.append("images", file));
    await updateNotice(id, formData);
    alert("공지 수정이 완료되었습니다!");
    navigate("/shelter/noticelist");
  } catch (err) {
    setError(
      err?.response?.data?.message || "공지 수정에 실패했습니다. 다시 시도해 주세요."
    );
  }
};


  return (
    <div className="notice-edit-wrap">
      <div className="notice-edit-box">
        <div className="notice-edit-header">
          <div className="notice-edit-title">공지 수정</div>
        </div>
        <form className="notice-edit-form" onSubmit={handleSubmit}>
          <input
            type="text"
            name="title"
            className="notice-edit-input"
            value={form.title}
            onChange={handleChange}
            required
            placeholder="공지 제목 입력"
            maxLength={100}
          />
          <textarea
            name="content"
            className="notice-edit-textarea"
            value={form.content}
            onChange={handleChange}
            required
            placeholder="공지 내용 입력"
            rows={8}
            maxLength={2000}
          />
          <input
            type="file"
            name="images"
            accept="image/*"
            multiple
            onChange={e => setImages([...e.target.files])}
          />

          {error && <div className="notice-edit-error">{error}</div>}
          <div className="notice-edit-btns">
            <button type="submit" className="ne-btn ne-btn-main">수정</button>
            <button
              type="button"
              className="ne-btn ne-btn-cancel"
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

export default NoticeUpdate;
