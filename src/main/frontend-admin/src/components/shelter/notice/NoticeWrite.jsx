import React, { useState } from "react";
import { createNotice } from "../../../api/shelter/notice";
import { useNavigate } from "react-router-dom";
import { LuMegaphone } from "react-icons/lu";
import { FiCheck, FiX, FiAlertCircle, FiUpload } from "react-icons/fi";
import "../../../styles/shelter/notice/noticeWrite.css";

function NoticeWrite() {
  const [form, setForm] = useState({ title: "", content: "" });
  const [images, setImages] = useState([]);
  const [previewUrls, setPreviewUrls] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleImageChange = (e) => {
    const files = Array.from(e.target.files);
    setImages(files);
    setPreviewUrls(files.map(file => URL.createObjectURL(file)));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    if (!form.title.trim() || !form.content.trim()) {
      setError("제목과 내용을 모두 입력해 주세요.");
      return;
    }

    try {
      await createNotice({ ...form, images });
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
              placeholder="공지 내용 입력"
              rows={8}
              maxLength={2000}
            />
          </label>

          <label className="nw-label">
            이미지 첨부 (선택)
            <span className="nw-upload-guide">
              <FiUpload style={{marginRight:4, verticalAlign:'middle'}} />
              이미지는 여러 장 첨부할 수 있습니다.
            </span>
            <input
              type="file"
              accept="image/*"
              multiple
              onChange={handleImageChange}
              className="nw-input"
              style={{ padding: "10px" }}
            />
          </label>

          {previewUrls.length > 0 && (
            <div className="nw-image-preview">
              {previewUrls.map((url, idx) => (
                <img key={idx} src={url} alt={`미리보기 ${idx}`} className="nw-preview-img" />
              ))}
            </div>
          )}

          {error && (
            <div className="nw-error">
              <FiAlertCircle style={{marginRight:4, verticalAlign:'middle'}} />
              {error}
            </div>
          )}

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
