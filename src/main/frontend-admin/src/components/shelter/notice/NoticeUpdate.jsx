import { useState, useEffect } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { getNoticeDetail, updateNotice } from "../../../api/shelter/notice";
import "../../../styles/shelter/notice/noticeForms.css";
import { Save, XCircle, AlertCircle, ImageIcon } from "lucide-react";

function NoticeUpdate() {
  const [form, setForm] = useState({ title: "", content: "" });
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { id } = useParams();
  const [images, setImages] = useState([]);
  const [previewUrls, setPreviewUrls] = useState([]); // 미리보기 URL 상태 추가

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  // 이미지 변경 핸들러 (미리보기 기능 포함)
  const handleImageChange = (e) => {
    const files = Array.from(e.target.files);
    setImages(files);
    // 기존 미리보기 URL 해제
    previewUrls.forEach(url => URL.revokeObjectURL(url));
    setPreviewUrls(files.map(file => URL.createObjectURL(file)));
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

    // 컴포넌트 언마운트 시 미리보기 URL 해제 (메모리 누수 방지)
    return () => {
      previewUrls.forEach(url => URL.revokeObjectURL(url));
    };
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
      // 새로 선택된 이미지가 있을 때만 "images" 필드를 추가
      if (images.length > 0) {
        images.forEach((file) => formData.append("images", file));
      }
      await updateNotice(id, formData);
      alert("공지 수정이 완료되었습니다!");
      navigate(`/shelter/notice/detail/${id}`);
    } catch (err) {
      setError(
        err?.response?.data?.message || "공지 수정에 실패했습니다. 다시 시도해 주세요."
      );
    }
  };

  return (
    <div className="notice-container">
      <div className="notice-header">
        <h1 className="notice-title">공지 수정</h1>
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
            required
            maxLength={100}
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
            required
            maxLength={2000}
          />
        </div>
        <div className="form-group">
          <label htmlFor="images" className="form-label">이미지 재업로드</label>
          <p style={{ fontSize: '0.9rem', color: '#666', margin: '-4px 0 4px 0' }}>
            이미지를 새로 첨부하면 기존 이미지는 모두 삭제됩니다.
          </p>
          <div>
            <label htmlFor="file-upload" className="form-file-label">
              <ImageIcon size={18} />
              <span>파일 선택</span>
            </label>
            <input
              id="file-upload"
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

        {/* 이미지 미리보기 섹션 추가 */}
        {previewUrls.length > 0 && (
          <div className="image-gallery">
            {previewUrls.map((url, idx) => (
              <img key={idx} src={url} alt={`미리보기 ${idx + 1}`} className="notice-image" />
            ))}
          </div>
        )}

        {error && <div className="error-message"><AlertCircle size={20} style={{ marginRight: '8px' }} />{error}</div>}
        
        <div className="button-group">
          <Link to={`/shelter/notice/detail/${id}`} className="btn btn-secondary">
            <XCircle size={18} style={{ marginRight: '8px' }} />
            취소
          </Link>
          <button type="submit" className="btn btn-primary">
            <Save size={18} style={{ marginRight: '8px' }} />
            저장
          </button>
        </div>
      </form>
    </div>
  );
}

export default NoticeUpdate;
