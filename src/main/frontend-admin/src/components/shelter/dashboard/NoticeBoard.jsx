// import 'bootstrap/dist/css/bootstrap.min.css';   // 삭제!
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getNotices } from '../../../api/shelter/notice';
import '../../../styles/shelter/notice/noticeBoard.css';

function NoticeBoard() {
  const [notices, setNotices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchNotices = async () => {
      try {
        const res = await getNotices();
        setNotices(Array.isArray(res.data) ? res.data : []);
      } catch (err) {
        setError("공지사항 목록을 불러오는 중 오류가 발생했습니다.");
      } finally {
        setLoading(false);
      }
    };
    fetchNotices();
  }, []);

  if (loading) return <div className="noticeboard-container">로딩 중...</div>;
  if (error) return <div className="noticeboard-container error">{error}</div>;

  return (
    <div className="noticeboard-container">
      <h2>
        <Link to={"/shelter/noticelist"} className="noticeboard-title-link">
          공지사항
        </Link>
      </h2>
      <table className="noticeboard-table">
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>내용</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          {notices.slice(0, 4).map((notice, i) => (
            <tr key={notice.id || i}>
              <td>{notice.id}</td>
              <td>
                <Link to={`/shelter/notice/detail/${notice.id}`} className="noticeboard-link">
                  {notice.title}
                </Link>
              </td>
              <td>{notice.content}</td>
              <td>
                {notice.createdAt
                  ? notice.createdAt.slice(0, 10)
                  : notice.date
                    ? notice.date.slice(0, 10)
                    : ""}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default NoticeBoard;
