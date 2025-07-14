import { useParams, useNavigate, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { getNoticeDetail, deleteNotice } from "../../../api/shelter/notice";
import "../../../styles/shelter/notice/noticeForms.css";
import { Calendar, User, Eye, Edit, Trash2, List } from "lucide-react";

function NoticeDetail() {
  const { id } = useParams();
  const [notice, setNotice] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchDetail = async () => {
      try {
        const res = await getNoticeDetail(id);
        setNotice(res.data);
      } catch (err) {
        setError("공지사항을 불러오는 데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };
    fetchDetail();
  }, [id]);

  const handleDelete = async () => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    try {
      await deleteNotice(id);
      alert("삭제가 완료되었습니다.");
      navigate("/shelter/noticelist");
    } catch (e) {
      alert("삭제에 실패했습니다. 다시 시도해 주세요.");
    }
  };

  if (loading) return <div className="notice-container">로딩 중...</div>;
  if (error) return <div className="notice-container error-message">{error}</div>;
  if (!notice) return <div className="notice-container">데이터가 없습니다.</div>;

  return (
    <div className="notice-container">
      <div className="notice-header">
        <h1 className="notice-title">{notice.title}</h1>
        <div className="notice-meta">
          <span><User size={16} /> {notice.nickname}</span>
          <span><Calendar size={16} /> {new Date(notice.createdAt).toLocaleDateString('ko-KR')}</span>
          <span><Eye size={16} /> 조회수 {notice.viewCount}</span>
        </div>
      </div>

      <div className="notice-content">
        <pre>{notice.content}</pre>
      </div>

      {notice.imageUrls && notice.imageUrls.length > 0 && (
        <div className="notice-images">
          <h3 className="notice-images-title">첨부 이미지</h3>
          <div className="image-gallery">
            {notice.imageUrls.map((url, idx) => (
              <img
                key={idx}
                src={`http://localhost:8080${url}`}
                alt={`첨부 이미지 ${idx + 1}`}
                className="notice-image"
              />
            ))}
          </div>
        </div>
      )}

      <div className="button-group">
        <Link to="/shelter/noticelist" className="btn btn-secondary">
          <List size={18} style={{ marginRight: '8px' }} />
          목록
        </Link>
        <Link to={`/shelter/notice/update/${id}`} className="btn btn-primary">
          <Edit size={18} style={{ marginRight: '8px' }} />
          수정
        </Link>
        <button className="btn btn-danger" onClick={handleDelete}>
          <Trash2 size={18} style={{ marginRight: '8px' }} />
          삭제
        </button>
      </div>
    </div>
  );
}

export default NoticeDetail;