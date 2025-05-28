import 'bootstrap/dist/css/bootstrap.min.css';


function NoticeBoardList() {
  const notices = [
    { id: 1, title: "공지 제목 1", content: "내용 1", date: "2024-05-27" },
    { id: 2, title: "공지 제목 2", content: "내용 2", date: "2024-05-26" },
    { id: 3, title: "공지 제목 3", content: "내용 3", date: "2024-05-25" },
    { id: 4, title: "공지 제목 4", content: "내용 4", date: "2024-05-27" },
    { id: 5, title: "공지 제목 5", content: "내용 5", date: "2024-05-26" },
    { id: 6, title: "공지 제목 6", content: "내용 6", date: "2024-05-25" },
    { id: 7, title: "공지 제목 7", content: "내용 7", date: "2024-05-27" },
    { id: 8, title: "공지 제목 8", content: "내용 8", date: "2024-05-26" },
    { id: 9, title: "공지 제목 9", content: "내용 9", date: "2024-05-25" },
  ];

  return (
    <div className="container mt-4">
      <h2>공지사항</h2>
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
              <td>{notice.title}</td>
              <td>{notice.content}</td>
              <td>{notice.date}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
export default NoticeBoardList;
