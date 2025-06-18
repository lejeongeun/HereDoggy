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

        // 응답 타입이 JSON이 아닐 경우 예외 처리
        if (typeof res.data === 'string' && res.data.startsWith('<!DOCTYPE')) {
          throw new Error('서버에서 올바른 JSON이 반환되지 않았습니다.');
        }

        setNotices(Array.isArray(res.data) ? res.data : []);
      } catch (err) {
        console.error(err);
        setError("공지사항 목록을 불러오는 중 오류가 발생했습니다.");
      } finally {
        setLoading(false);
      }
    };
    fetchNotices();
  }, []);

  if (loading) return <div className="notice-table-wrap">로딩 중...</div>;
  if (error) return <div className="notice-table-wrap error">{error}</div>;

  return (
    <div className="notice-table-wrap">
      <div className="notice-table-header">
        <Link to="/shelter/noticelist" className="notice-table-title">
          공지사항
        </Link>
      </div>
      <table className="notice-table">
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
              <td colSpan="4" className="notice-empty">등록된 공지사항이 없습니다.</td>
            </tr>
          ) : (
            notices.slice(0, 4).map((notice) => (
              <tr key={notice.id}>
                <td>{notice.id}</td>
                <td>
                  <Link to={`/shelter/notice/detail/${notice.id}`} className="notice-table-link">
                    {notice.title}
                  </Link>
                </td>
                <td className="notice-table-content">
                  {notice.content?.length > 25
                    ? `${notice.content.slice(0, 25)}...`
                    : notice.content}
                </td>
                <td>{new Date(notice.createdAt || notice.date).toLocaleDateString('ko-KR')}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default NoticeBoard;
