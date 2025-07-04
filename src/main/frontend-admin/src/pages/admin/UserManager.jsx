import React, { useState } from "react";
import '../../styles/admin/userManager/userManager.css';

// 더미 사용자 데이터
const usersDummy = [
  { id: 1, name: "유재석", email: "hong@naver.com", role: "일반", status: "정상", joined: "2024-03-22" },
  { id: 2, name: "박명수", email: "kim@kakao.com", role: "일반", status: "정상", joined: "2024-06-05" },
  { id: 3, name: "하동훈", email: "admin@test.com", role: "SYSTEM_ADMIN", status: "정상", joined: "2024-02-11" },
  { id: 4, name: "노홍철", email: "baduser@test.com", role: "일반", status: "블랙리스트", joined: "2024-04-30" },
];
for (let i = 5; i <= 30; i++) {
  usersDummy.push({
    id: i,
    name: `유저${i}`,
    email: `user${i}@test.com`,
    role: i % 9 === 0 ? "SHELTER_ADMIN" : "일반", // 9번째마다 SHELTER_ADMIN
    status: i % 7 === 0 ? "블랙리스트" : "정상", // 7번째마다 블랙리스트
    joined: `2024-07-${(i % 30 + 1).toString().padStart(2, '0')}`
  });
}

function UserManager() {
  const [search, setSearch] = useState("");
  const [users, setUsers] = useState(usersDummy);
  const [sortKey, setSortKey] = useState("id");
  const [sortOrder, setSortOrder] = useState("asc");

  // 필터 상태
  const [roleFilter, setRoleFilter] = useState("전체");
  const [statusFilter, setStatusFilter] = useState("전체");

  // 필터 + 검색
  const filtered = users.filter(u => {
    const matchSearch = u.name.includes(search) || u.email.includes(search);
    const matchRole = roleFilter === "전체" || u.role === roleFilter;
    const matchStatus = statusFilter === "전체" || u.status === statusFilter;
    return matchSearch && matchRole && matchStatus;
  });

  // 정렬
  const sorted = [...filtered].sort((a, b) => {
    const aVal = a[sortKey];
    const bVal = b[sortKey];
    if (sortOrder === "asc") return aVal > bVal ? 1 : -1;
    else return aVal < bVal ? 1 : -1;
  });

  return (
    <div className="user-manager-container">
      <h2 className="user-manager-header">사용자 관리</h2>

      <div className="user-search-row">
        <input
          className="user-search-input"
          placeholder="이름/이메일 검색"
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
        <button
          className="user-blacklist-btn"
          onClick={() => alert("블랙리스트 처리(예시)")}
        >
          선택 블랙리스트
        </button>
      </div>

      {/* 필터 UI 추가 */}
      <div className="user-filter-row">
        <select value={roleFilter} onChange={e => setRoleFilter(e.target.value)} className="user-filter-select">
          <option value="전체">전체 권한</option>
          <option value="일반">일반</option>
          <option value="SHELTER_ADMIN">보호소 관리자</option>
          <option value="SYSTEM_ADMIN">시스템 관리자</option>
        </select>

        <select value={statusFilter} onChange={e => setStatusFilter(e.target.value)} className="user-filter-select">
          <option value="전체">전체 상태</option>
          <option value="정상">정상</option>
          <option value="블랙리스트">블랙리스트</option>
        </select>

        {/* 정렬 옵션 예시 */}
        <select value={sortKey} onChange={e => setSortKey(e.target.value)} className="user-filter-select">
          <option value="id">ID</option>
          <option value="name">이름</option>
          <option value="joined">가입일</option>
        </select>
        <select value={sortOrder} onChange={e => setSortOrder(e.target.value)} className="user-filter-select">
          <option value="asc">오름차순</option>
          <option value="desc">내림차순</option>
        </select>
      </div>

      <table className="user-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>이름</th>
            <th>이메일</th>
            <th>권한</th>
            <th>상태</th>
            <th>가입일</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {sorted.length === 0 ? (
            <tr>
              <td colSpan={7} style={{ textAlign: "center", color: "#aaa", padding: 28 }}>
                사용자가 없습니다.
              </td>
            </tr>
          ) : (
            sorted.map(user => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.email}</td>
                <td>{user.role}</td>
                <td className={user.status === "블랙리스트" ? "user-status-black" : "user-status-normal"}>
                  {user.status}
                </td>
                <td>{user.joined}</td>
                <td>
                  {user.status !== "블랙리스트" ? (
                    <button
                      className="user-action-btn"
                      onClick={() => alert(`${user.name} 블랙리스트 처리(예시)`)}
                    >
                      블랙리스트
                    </button>
                  ) : (
                    <button
                      className="user-action-btn"
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

export default UserManager;