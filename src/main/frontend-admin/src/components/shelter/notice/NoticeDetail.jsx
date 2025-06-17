import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getNoticeDetail, deleteNotice } from "../../../api/shelter/notice";
import "../../../styles/shelter/notice/noticeDetail.css";

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

  if (loading) return <div className="notice-detail-wrap">로딩 중...</div>;
  if (error) return <div className="notice-detail-wrap notice-detail-error">{error}</div>;
  if (!notice) return <div className="notice-detail-wrap">데이터가 없습니다.</div>;

  return (
    <div className="notice-detail-wrap">
      <div className="notice-detail-box">
        <div className="notice-detail-header">
          <h2 className="notice-detail-title">{notice.title}</h2>
          <div className="notice-detail-info">
            <span className="notice-detail-author">{notice.nickname} ({notice.email})</span>
            <span className="notice-detail-date">
              {notice.createdAt?.slice(0, 10)}
            </span>
            <span className="notice-detail-view">조회수 {notice.viewCount}</span>
          </div>
        </div>

        <div className="notice-detail-content">
          <pre>{notice.content}</pre>
        </div>

        {notice.imageUrls && notice.imageUrls.length > 0 && (
          <div className="notice-detail-images">
            {notice.imageUrls.map((url, idx) => (
              <img
                key={idx}
                src={
                  url.startsWith("http")
                    ? url
                    : `http://localhost:8080${url}`
                }
                alt={`첨부 이미지 ${idx + 1}`}
                className="notice-detail-image"
              />
            ))}
          </div>
        )}

        <div className="notice-detail-btns">
          <button className="nd-btn" onClick={() => navigate("/shelter/noticelist")}>
            목록으로
            </button>
          <button className="nd-btn nd-btn-edit" onClick={() => navigate(`/shelter/notice/update/${id}`)}>
            수정
            </button>
           <button className="nd-btn nd-btn-delete" onClick={handleDelete} style={{ color: "#fff", background: "#d72f3f" }}>
            삭제
          </button>
        </div>
      </div>
    </div>
  );
}

export default NoticeDetail;