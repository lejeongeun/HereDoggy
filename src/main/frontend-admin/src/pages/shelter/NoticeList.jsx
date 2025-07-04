import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getNotices } from "../../api/shelter/notice";
import "../../styles/shelter/notice/noticeList.css";
import { ChevronDown, ChevronUp, AlertCircle, PlusCircle } from "lucide-react";

function NoticeList() {
  const [notices, setNotices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [openId, setOpenId] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10); // 페이지당 10개 항목

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

  // 현재 페이지에 해당하는 공지사항 계산
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentNotices = notices.slice(indexOfFirstItem, indexOfLastItem);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(notices.length / itemsPerPage);

  // 페이지 변경 핸들러
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  const handleToggle = (id) => {
    setOpenId(openId === id ? null : id);
  };

  if (loading) return <div className="notice-list-container">로딩 중...</div>;
  if (error) return <div className="error-message"><AlertCircle size={48} />{error}</div>;

  return (
    <div className="notice-list-container">
      <div className="notice-list-header">
        <h2 className="notice-list-title">공지사항 관리</h2>
        <Link to="/shelter/noticewrite" className="write-button">
          <PlusCircle size={20} />
          <span>새 공지 작성</span>
        </Link>
      </div>
      <div className="notice-list">
        {notices.length === 0 ? (
          <div className="empty-message">
            <AlertCircle size={48} />
            <span>등록된 공지사항이 없습니다.</span>
          </div>
        ) : (
          currentNotices.map((notice) => (
            <div className="notice-item" key={notice.id}>
              <div className="notice-head" onClick={() => handleToggle(notice.id)}>
                <div className="notice-title-date">
                  <Link to={`/shelter/notice/detail/${notice.id}`} className="notice-title-link" onClick={(e) => e.stopPropagation()}>
                    {notice.title}
                  </Link>
                  <span className="notice-date">
                    {new Date(notice.createdAt).toLocaleDateString('ko-KR')}
                  </span>
                </div>
                <button
                  className="notice-toggle"
                  aria-label="내용 열기/닫기"
                >
                  {openId === notice.id ? <ChevronUp /> : <ChevronDown />}
                </button>
              </div>
              {openId === notice.id && (
                <div className="notice-content">
                  <pre>{notice.content}</pre>
                </div>
              )}
            </div>
          ))
        )}
      </div>
      {notices.length > itemsPerPage && (
        <div className="pagination">
          <button
            onClick={() => paginate(currentPage - 1)}
            disabled={currentPage === 1}
            className="pagination-button"
          >
            이전
          </button>
          {[...Array(totalPages).keys()].map((number) => (
            <button
              key={number + 1}
              onClick={() => paginate(number + 1)}
              className={`pagination-button ${currentPage === number + 1 ? 'active' : ''}`}
            >
              {number + 1}
            </button>
          ))}
          <button
            onClick={() => paginate(currentPage + 1)}
            disabled={currentPage === totalPages}
            className="pagination-button"
          >
            다음
          </button>
        </div>
      )}
    </div>
  );
}

export default NoticeList;
