import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';
// npm install bootstrap

function NoticeBoard() {
  const notices = [
    { id: 1, title: "공지 제목 1", content: "내용 1", date: "2024-05-27" },
    { id: 2, title: "공지 제목 2", content: "내용 2", date: "2024-05-26" },
    { id: 3, title: "공지 제목 3", content: "내용 3", date: "2024-05-25" },
    
  ];

  return (
    <div className="container mt-4">
      <h2>
        <Link to={"/shelter/noticeboardlist"} className="text-decoration-none fw-bold text-dark">
        공지사항
        </Link>
        </h2>
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
          {notices.map((notice, i) => (
          <tr key={notice.id}>
            <td>{notice.id}</td>
            <td>
              <Link to={`/notice/${notice.id}`} className="text-decoration-none text-dark">
                {notice.title}
              </Link>
            </td>
            <td>{notice.content}</td>
            <td>{notice.date}</td>
          </tr>
        ))}
        </tbody>
      </table>
    </div>
  );
}
export default NoticeBoard;
