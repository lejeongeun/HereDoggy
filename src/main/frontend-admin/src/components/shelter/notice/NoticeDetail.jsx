import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getNoticeDetail } from "../../../api/shelter/notice";
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

  if (loading) return <div className="notice-detail-wrap">로딩 중...</div>;
  if (error) return <div className="notice-detail-wrap notice-detail-error">{error}</div>;
  if (!notice) return <div className="notice-detail-wrap">데이터가 없습니다.</div>;

  return (
    <div className="notice-detail-wrap">
      <div className="notice-detail-box">
        <div className="notice-detail-meta">
          <div className="notice-detail-title">{notice.title}</div>
          <div className="notice-detail-date">
            {notice.createdAt
              ? notice.createdAt.slice(0, 10)
              : notice.date
                ? notice.date.slice(0, 10)
                : ""}
          </div>
        </div>
        <div className="notice-detail-content">
          <pre>{notice.content}</pre>
        </div>
        <div className="notice-detail-btns">
          <button className="nd-btn" onClick={() => navigate(-1)}>목록으로</button>
          <button className="nd-btn nd-btn-edit" onClick={() => navigate(`/shelter/notice/update/${id}`)}>수정</button>
        </div>
      </div>
    </div>
  );
}

export default NoticeDetail;
