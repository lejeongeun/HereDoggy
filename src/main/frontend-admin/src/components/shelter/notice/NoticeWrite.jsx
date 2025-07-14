import React, { useState } from "react";
import { createNotice } from "../../../api/shelter/notice";
import { useNavigate, Link } from "react-router-dom";
import "../../../styles/shelter/notice/noticeForms.css";
import { PlusCircle, XCircle, AlertCircle, Image as ImageIcon } from "lucide-react";

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
    <div className="notice-container">
      <div className="notice-header">
        <h1 className="notice-title">새 공지 작성</h1>
      </div>
      <form className="notice-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="title" className="form-label">제목</label>
          <input
            id="title"
            type="text"
            name="title"
            className="form-input"
            value={form.title}
            onChange={handleChange}
            placeholder="공지 제목을 입력하세요"
            maxLength={100}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="content" className="form-label">내용</label>
          <textarea
            id="content"
            name="content"
            className="form-textarea"
            value={form.content}
            onChange={handleChange}
            placeholder="공지 내용을 입력하세요"
            maxLength={2000}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="images" className="form-label">이미지 첨부 (선택)</label>
          <div>
            <label htmlFor="file-upload-write" className="form-file-label">
              <ImageIcon size={18} />
              <span>파일 선택</span>
            </label>
            <input
              id="file-upload-write"
              type="file"
              name="images"
              accept="image/*"
              multiple
              onChange={handleImageChange}
              className="form-file-input"
            />
            {images.length > 0 && (
              <span style={{ marginLeft: '16px', color: '#555' }}>
                {images.length}개의 파일 선택됨
              </span>
            )}
          </div>
        </div>

        {previewUrls.length > 0 && (
          <div className="image-gallery">
            {previewUrls.map((url, idx) => (
              <img key={idx} src={url} alt={`미리보기 ${idx}`} className="notice-image" />
            ))}
          </div>
        )}

        {error && (
          <div className="error-message">
            <AlertCircle size={20} style={{ marginRight: '8px' }} />
            {error}
          </div>
        )}

        <div className="button-group">
          <Link to="/shelter/noticelist" className="btn btn-secondary">
            <XCircle size={18} style={{ marginRight: '8px' }} />
            취소
          </Link>
          <button type="submit" className="btn btn-primary">
            <PlusCircle size={18} style={{ marginRight: '8px' }} />
            등록
          </button>
        </div>
      </form>
    </div>
  );
}

export default NoticeWrite;
