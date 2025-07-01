import React, { useState } from "react";

// 더미 사용자 데이터
const usersDummy = [
  { id: 1, name: "홍길동", email: "hong@naver.com", role: "일반", status: "정상", joined: "2024-03-22" },
  { id: 2, name: "김개발", email: "kim@kakao.com", role: "일반", status: "정상", joined: "2024-06-05" },
  { id: 3, name: "관리자", email: "admin@test.com", role: "SYSTEM_ADMIN", status: "정상", joined: "2024-02-11" },
  { id: 4, name: "불량이", email: "baduser@test.com", role: "일반", status: "블랙리스트", joined: "2024-04-30" },
];

function UserManage() {
  const [search, setSearch] = useState("");
  const [users, setUsers] = useState(usersDummy);

  // 검색
  const filtered = users.filter(u =>
    u.name.includes(search) || u.email.includes(search)
  );

  return (
    <div>
      <h2 style={{ marginBottom: 18 }}>사용자 관리</h2>
      <div style={{ marginBottom: 20 }}>
        <input
          style={{
            padding: 8,
            border: "1px solid #ccc",
            borderRadius: 7,
            minWidth: 220,
            marginRight: 12,
          }}
          placeholder="이름/이메일 검색"
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
        {/* 예시 버튼, 실서버라면 api로 상태 변경 */}
        <button
          style={{
            background: "#24324a",
            color: "#fff",
            border: "none",
            padding: "8px 20px",
            borderRadius: 7,
            cursor: "pointer",
          }}
          onClick={() => alert("블랙리스트 처리(예시)")}
        >
          선택 블랙리스트
        </button>
      </div>
      <table style={{ width: "100%", borderCollapse: "collapse", background: "#fff" }}>
        <thead>
          <tr style={{ background: "#f0f3f8", textAlign: "left" }}>
            <th style={thStyle}>ID</th>
            <th style={thStyle}>이름</th>
            <th style={thStyle}>이메일</th>
            <th style={thStyle}>권한</th>
            <th style={thStyle}>상태</th>
            <th style={thStyle}>가입일</th>
            <th style={thStyle}>관리</th>
          </tr>
        </thead>
        <tbody>
          {filtered.length === 0 ? (
            <tr>
              <td colSpan={7} style={{ textAlign: "center", color: "#aaa", padding: 28 }}>
                사용자가 없습니다.
              </td>
            </tr>
          ) : (
            filtered.map(user => (
              <tr key={user.id}>
                <td style={tdStyle}>{user.id}</td>
                <td style={tdStyle}>{user.name}</td>
                <td style={tdStyle}>{user.email}</td>
                <td style={tdStyle}>{user.role}</td>
                <td style={{ ...tdStyle, color: user.status === "블랙리스트" ? "red" : "#0a0" }}>{user.status}</td>
                <td style={tdStyle}>{user.joined}</td>
                <td style={tdStyle}>
                  {user.status !== "블랙리스트" ? (
                    <button
                      style={btnStyle}
                      onClick={() => alert(`${user.name} 블랙리스트 처리(예시)`)}
                    >
                      블랙리스트
                    </button>
                  ) : (
                    <button
                      style={{ ...btnStyle, background: "#888", cursor: "not-allowed" }}
                      disabled
                    >
                      해제불가
                    </button>
                  )}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

const thStyle = { padding: "10px 8px", borderBottom: "1px solid #ececec" };
const tdStyle = { padding: "10px 8px", borderBottom: "1px solid #f2f2f2" };
const btnStyle = {
  background: "#24324a",
  color: "#fff",
  border: "none",
  padding: "6px 14px",
  borderRadius: 7,
  fontSize: 13,
  cursor: "pointer",
};

export default UserManage;
