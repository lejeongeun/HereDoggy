import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getNotices } from "../../api/shelter/notice";
import "../../styles/shelter/notice/noticeList.css";
import { FiChevronDown, FiChevronUp, FiAlertCircle } from "react-icons/fi";

function NoticeList() {
  const [notices, setNotices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [openId, setOpenId] = useState(null);

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

  const handleToggle = (e, id) => {
    e.stopPropagation();
    setOpenId(openId === id ? null : id);
  };

  if (loading) return <div className="notice-list-wrap">로딩 중...</div>;
  if (error) return <div className="notice-list-wrap error"><FiAlertCircle style={{marginRight:4, verticalAlign:'middle'}} />{error}</div>;

  return (
    <div className="notice-list-wrap">
      <div className="notice-list-header">
        <h2 className="notice-list-title">공지사항</h2>
        <Link to="/shelter/noticewrite" className="notice-list-btn">
          작성하기
        </Link>
      </div>
      <div className="notice-list">
        {notices.length === 0 ? (
          <div className="notice-item empty">
            <FiAlertCircle style={{marginRight:4, verticalAlign:'middle'}} />
            등록된 공지사항이 없습니다.
          </div>
        ) : (
          notices.map((notice) => (
            <div className="notice-item" key={notice.id}>
              <div className="notice-head" onClick={(e) => handleToggle(e, notice.id)}>
                <div className="notice-title-date">
                  <Link to={`/shelter/notice/detail/${notice.id}`} className="notice-title-link">
                    {notice.title}
                  </Link>
                  <span className="notice-date">
                    {(notice.createdAt || "").slice(0, 10)}
                  </span>
                </div>
                <button
                  className="notice-toggle"
                  aria-label="내용 열기/닫기"
                >
                  {openId === notice.id ? <FiChevronUp /> : <FiChevronDown />}
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
    </div>
  );
}

export default NoticeList;
