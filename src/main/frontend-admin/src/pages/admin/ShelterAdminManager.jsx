import React, { useState } from "react";
import '../../styles/admin/shelterAdminManager/shelterAdminManager.css';

// 더미 보호소 관리자 데이터
const initialAdmins = [
  {
    id: 1,
    name: "홍길동",
    email: "hong@shelter.com",
    phone: "010-1111-1111",
    status: "PENDING",
    createdAt: "2024-06-20T12:34:56Z",
    active: true,
  },
  {
    id: 2,
    name: "이순신",
    email: "lee@shelter.com",
    phone: "010-2222-2222",
    status: "APPROVED",
    createdAt: "2024-06-19T09:15:00Z",
    approvedAt: "2024-06-19T11:00:00Z",
    active: true,
  },
  {
    id: 3,
    name: "강감찬",
    email: "kang@shelter.com",
    phone: "010-3333-3333",
    status: "APPROVED",
    createdAt: "2024-06-15T10:20:00Z",
    approvedAt: "2024-06-16T08:10:00Z",
    active: false,
  }
];

function ShelterAdminManager() {
  const [admins, setAdmins] = useState(initialAdmins);
  const [hoverRow, setHoverRow] = useState(null);

  // 관리 함수
  const approveAdmin = id => {
    setAdmins(prev =>
      prev.map(a =>
        a.id === id
          ? { ...a, status: "APPROVED", approvedAt: new Date().toISOString(), active: true }
          : a
      )
    );
  };

  const rejectAdmin = id => {
    if (!window.confirm("이 계정을 거절(삭제)하시겠습니까?")) return;
    setAdmins(prev => prev.filter(a => a.id !== id));
  };

  const deactivateAdmin = id => {
    setAdmins(prev =>
      prev.map(a =>
        a.id === id ? { ...a, active: false } : a
      )
    );
  };

  const activateAdmin = id => {
    setAdmins(prev =>
      prev.map(a =>
        a.id === id ? { ...a, active: true } : a
      )
    );
  };

  const deleteAdmin = id => {
    if (!window.confirm("이 계정을 영구 삭제하시겠습니까?")) return;
    setAdmins(prev => prev.filter(a => a.id !== id));
  };

  // 목록 분리
  const pendingAdmins = admins.filter(a => a.status === "PENDING");
  const approvedAdmins = admins.filter(a => a.status === "APPROVED");

 return (
  <div className="shelter-admin-wrap">
    <h2>보호소 관리자 계정 관리 (더미)</h2>

    {/* 1. 미승인 보호소 관리자 */}
    <section>
      <h3>미승인 보호소 관리자 요청</h3>
      <table className="shelter-admin-table">
        <thead>
          <tr>
            <th>이름</th>
            <th>이메일</th>
            <th>전화번호</th>
            <th>신청일</th>
            <th>처리</th>
          </tr>
        </thead>
        <tbody>
          {pendingAdmins.length === 0 && (
            <tr>
              <td colSpan={5}>대기 중인 요청이 없습니다.</td>
            </tr>
          )}
          {pendingAdmins.map(a => (
            <tr key={a.id}>
              <td>{a.name}</td>
              <td>{a.email}</td>
              <td>{a.phone}</td>
              <td>{a.createdAt.slice(0, 10)}</td>
              <td>
                <button onClick={() => approveAdmin(a.id)}>
                  승인
                </button>
                <button className="admin-btn-danger" onClick={() => rejectAdmin(a.id)}>
                  거절
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>

    {/* 2. 승인된 보호소 관리자 */}
    <section>
      <h3>승인된 보호소 관리자</h3>
      <table className="shelter-admin-table">
        <thead>
          <tr>
            <th>이름</th>
            <th>이메일</th>
            <th>전화번호</th>
            <th>상태</th>
            <th>가입일</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          {approvedAdmins.length === 0 && (
            <tr>
              <td colSpan={6}>승인된 관리자가 없습니다.</td>
            </tr>
          )}
          {approvedAdmins.map(a => (
            <tr key={a.id}>
              <td>{a.name}</td>
              <td>{a.email}</td>
              <td>{a.phone}</td>
              <td>{a.active ? "활성" : "비활성"}</td>
              <td>{a.approvedAt?.slice(0, 10) ?? "-"}</td>
              <td>
                {a.active ? (
                  <button className="admin-btn-gray" onClick={() => deactivateAdmin(a.id)}>
                    비활성화
                  </button>
                ) : (
                  <button onClick={() => activateAdmin(a.id)}>
                    활성화
                  </button>
                )}
                <button
                  className="admin-btn-danger"
                  style={{ marginLeft: 8 }}
                  onClick={() => deleteAdmin(a.id)}
                >
                  삭제
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  </div>
  );
}

export default ShelterAdminManager;
