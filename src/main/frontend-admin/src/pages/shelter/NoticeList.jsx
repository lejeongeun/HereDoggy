import 'bootstrap/dist/css/bootstrap.min.css';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getNotices } from '../../api/shelter/notice';

function NoticeList() {

  const [notices, setNotices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  useEffect(() => {
    // 공지사항 목록 불러오기
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

  if (loading) return <div className="container mt-4">로딩 중...</div>;
  if (error) return <div className="container mt-4 text-danger">{error}</div>;

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-2">
        <h2 className="mb-0">공지사항</h2>
        <Link to="/shelter/noticewrite" className="btn btn-primary">
          작성하기
        </Link>
      </div>
      <table className="table table-hover">
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>내용</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          {notices.length === 0 ? (
            <tr>
              <td colSpan={4} className="text-center">등록된 공지사항이 없습니다.</td>
            </tr>
          ) : (
            notices.map((notice, i) => (
              <tr key={notice.id || i}>
                <td>{notice.id}</td>
                <td>
                  <Link to={`/shelter/notice/detail/${notice.id}`} className="text-decoration-none text-dark">
                    {notice.title}
                  </Link>
                </td>
                <td>{notice.content}</td>
                <td>{notice.createdAt
                  ? notice.createdAt.slice(0, 10)
                  : notice.date
                    ? notice.date.slice(0, 10)
                    : ""}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default NoticeList;