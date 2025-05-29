import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getNoticeDetail } from "../../../api/shelter/notice"; // 경로는 상황에 맞게

function NoticeDetail() {
  const { id } = useParams(); // /notice/detail/:id 라우트의 id 파라미터 가져오기
  const [notice, setNotice] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    // 상세 데이터 불러오기
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

  if (loading) return <div className="container mt-4">로딩 중...</div>;
  if (error) return <div className="container mt-4 text-danger">{error}</div>;
  if (!notice) return <div className="container mt-4">데이터가 없습니다.</div>;

  return (
    <div className="container mt-4">
      <h4>{notice.title}</h4>
      <div className="mb-2">{notice.content}</div>
      <div>작성일:  {notice.createdAt
                  ? notice.createdAt.slice(0, 10)
                  : notice.date
                    ? notice.date.slice(0, 10)
                    : ""}</div>
      <button className="btn btn-secondary mt-3" onClick={() => navigate(-1)}>
        목록으로
      </button>
    </div>
  );
}

export default NoticeDetail;
