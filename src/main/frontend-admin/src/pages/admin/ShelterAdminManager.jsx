import React, { useState } from "react";
import styles from '../../styles/admin/shelterAdminManager/shelterAdminManager.module.css';

function ShelterAdminManager() {
  const [admins, setAdmins] = useState(() => {
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

    for (let i = 4; i <= 30; i++) {
      const isPending = i % 5 === 0;
      const isActive = i % 2 === 0;
      const baseDate = new Date(2024, 5, (i % 30) + 1);
      const createdAt = baseDate.toISOString();
      const approvedAt = isPending ? undefined : new Date(baseDate.getTime() + 3600000).toISOString();

      initialAdmins.push({
        id: i,
        name: `관리자${i}`,
        email: `admin${i}@shelter.com`,
        phone: `010-${1000 + i}-${2000 + i}`,
        status: isPending ? "PENDING" : "APPROVED",
        createdAt,
        approvedAt,
        active: isActive,
      });
    }
    return initialAdmins;
  });
  const [search, setSearch] = useState("");
  const [sortKey, setSortKey] = useState("id");
  const [sortOrder, setSortOrder] = useState("asc");

  // 페이징 상태
  const [currentPagePending, setCurrentPagePending] = useState(1);
  const [currentPageApproved, setCurrentPageApproved] = useState(1);
  const [itemsPerPage] = useState(10); // 한 페이지당 보여줄 항목 수

  // 목록 분리
  const pendingAdmins = admins.filter(a => a.status === "PENDING");
  const approvedAdmins = admins.filter(a => a.status === "APPROVED");

  // 검색 필터
  const filteredPending = pendingAdmins.filter(a =>
    a.name.includes(search) || a.email.includes(search)
  );
  const filteredApproved = approvedAdmins.filter(a =>
    a.name.includes(search) || a.email.includes(search)
  );

  // 정렬
  const sortAdmins = list =>
    [...list].sort((a, b) => {
      const aVal = a[sortKey] || "";
      const bVal = b[sortKey] || "";
      if (sortOrder === "asc") return aVal > bVal ? 1 : -1;
      else return aVal < bVal ? 1 : -1;
    });

  const sortedPending = sortAdmins(filteredPending);
  const sortedApproved = sortAdmins(filteredApproved);

  // 현재 페이지의 항목 계산
  const indexOfLastItemPending = currentPagePending * itemsPerPage;
  const indexOfFirstItemPending = indexOfLastItemPending - itemsPerPage;
  const currentPendingItems = sortedPending.slice(indexOfFirstItemPending, indexOfLastItemPending);

  const indexOfLastItemApproved = currentPageApproved * itemsPerPage;
  const indexOfFirstItemApproved = indexOfLastItemApproved - itemsPerPage;
  const currentApprovedItems = sortedApproved.slice(indexOfFirstItemApproved, indexOfLastItemApproved);

  // 총 페이지 수 계산
  const totalPagesPending = Math.ceil(sortedPending.length / itemsPerPage);
  const totalPagesApproved = Math.ceil(sortedApproved.length / itemsPerPage);

  // 페이지 변경 핸들러
  const paginatePending = (pageNumber) => setCurrentPagePending(pageNumber);
  const paginateApproved = (pageNumber) => setCurrentPageApproved(pageNumber);

  // 관리 함수
  const approveAdmin = id => {
    setAdmins(prev =>
      prev.map(a =>
        a.id === id
          ? { ...a, status: "APPROVED", approvedAt: new Date().toISOString(), active: true }
          : a
      )
    );
    setCurrentPagePending(1); // 승인 시 미승인 목록 첫 페이지로 이동
    setCurrentPageApproved(1); // 승인 시 승인 목록 첫 페이지로 이동
  };

  const rejectAdmin = id => {
    if (!window.confirm("이 계정을 거절(삭제)하시겠습니까?")) return;
    setAdmins(prev => prev.filter(a => a.id !== id));
    setCurrentPagePending(1); // 거절 시 미승인 목록 첫 페이지로 이동
  };

  const deactivateAdmin = id => {
    setAdmins(prev =>
      prev.map(a => (a.id === id ? { ...a, active: false } : a))
    );
    setCurrentPageApproved(1); // 비활성화 시 승인 목록 첫 페이지로 이동
  };

  const activateAdmin = id => {
    setAdmins(prev =>
      prev.map(a => (a.id === id ? { ...a, active: true } : a))
    );
    setCurrentPageApproved(1); // 활성화 시 승인 목록 첫 페이지로 이동
  };

  const deleteAdmin = id => {
    if (!window.confirm("이 계정을 영구 삭제하시겠습니까?")) return;
    setAdmins(prev => prev.filter(a => a.id !== id));
    setCurrentPageApproved(1); // 삭제 시 승인 목록 첫 페이지로 이동
  };

  return (
    <div className={styles.managerContainer}>
      <h2 className={styles.managerHeader}>보호소 관리자 계정 관리</h2>

      <div className={styles.controlsRow}>
        <input
          className={styles.searchInput}
          placeholder="이름/이메일 검색"
          value={search}
          onChange={e => {
            setSearch(e.target.value);
            setCurrentPagePending(1); // 검색 시 첫 페이지로 이동
            setCurrentPageApproved(1); // 검색 시 첫 페이지로 이동
          }}
        />
        <select value={sortKey} onChange={e => {
          setSortKey(e.target.value);
          setCurrentPagePending(1); // 정렬 시 첫 페이지로 이동
          setCurrentPageApproved(1); // 정렬 시 첫 페이지로 이동
        }} className={styles.filterSelect}>
          <option value="id">ID</option>
          <option value="name">이름</option>
          <option value="createdAt">신청일</option>
          <option value="approvedAt">승인일</option>
        </select>
        <select value={sortOrder} onChange={e => {
          setSortOrder(e.target.value);
          setCurrentPagePending(1); // 정렬 시 첫 페이지로 이동
          setCurrentPageApproved(1); // 정렬 시 첫 페이지로 이동
        }} className={styles.filterSelect}>
          <option value="asc">오름차순</option>
          <option value="desc">내림차순</option>
        </select>
      </div>

      {/* 1. 미승인 보호소 관리자 */}
      <section className={styles.managerSection}>
        <h3 className={styles.sectionHeader}>미승인 보호소 관리자 요청</h3>
        <table className={styles.managerTable}>
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
            {currentPendingItems.length === 0 ? (
              <tr>
                <td colSpan={5}>대기 중인 요청이 없습니다.</td>
              </tr>
            ) : (
              currentPendingItems.map(a => (
                <tr key={a.id}>
                  <td>{a.name}</td>
                  <td>{a.email}</td>
                  <td>{a.phone}</td>
                  <td>{a.createdAt.slice(0, 10)}</td>
                  <td className={styles.actionsCell}>
                    <button className={`${styles.actionBtn} ${styles.approveBtn}`} onClick={() => approveAdmin(a.id)}>승인</button>
                    <button className={`${styles.actionBtn} ${styles.rejectBtn}`} onClick={() => rejectAdmin(a.id)}>거절</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
        {/* Pagination Controls for Pending Admins */}
        <div className={styles.pagination}>
          <button
            onClick={() => paginatePending(currentPagePending - 1)}
            disabled={currentPagePending === 1}
            className={styles.paginationButton}
          >
            이전
          </button>
          {[...Array(totalPagesPending).keys()].map((number) => (
            <button
              key={number + 1}
              onClick={() => paginatePending(number + 1)}
              className={`${styles.paginationButton} ${currentPagePending === number + 1 ? styles.activePage : ''}`}
            >
              {number + 1}
            </button>
          ))}
          <button
            onClick={() => paginatePending(currentPagePending + 1)}
            disabled={currentPagePending === totalPagesPending}
            className={styles.paginationButton}
          >
            다음
          </button>
        </div>
      </section>

      {/* 2. 승인된 보호소 관리자 */}
      <section className={styles.managerSection}>
        <h3 className={styles.sectionHeader}>승인된 보호소 관리자</h3>
        <table className={styles.managerTable}>
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
            {currentApprovedItems.length === 0 ? (
              <tr>
                <td colSpan={6}>승인된 관리자가 없습니다.</td>
              </tr>
            ) : (
              currentApprovedItems.map(a => (
                <tr key={a.id}>
                  <td>{a.name}</td>
                  <td>{a.email}</td>
                  <td>{a.phone}</td>
                  <td>
                    <span className={a.active ? styles.statusActive : styles.statusInactive}>
                      {a.active ? "활성" : "비활성"}
                    </span>
                  </td>
                  <td>{a.approvedAt?.slice(0, 10) ?? "-"}</td>
                  <td className={styles.actionsCell}>
                    {a.active ? (
                      <button className={`${styles.actionBtn} ${styles.deactivateBtn}`} onClick={() => deactivateAdmin(a.id)}>비활성화</button>
                    ) : (
                      <button className={`${styles.actionBtn} ${styles.activateBtn}`} onClick={() => activateAdmin(a.id)}>활성화</button>
                    )}
                    <button className={`${styles.actionBtn} ${styles.deleteBtn}`} onClick={() => deleteAdmin(a.id)}>삭제</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
        {/* Pagination Controls for Approved Admins */}
        <div className={styles.pagination}>
          <button
            onClick={() => paginateApproved(currentPageApproved - 1)}
            disabled={currentPageApproved === 1}
            className={styles.paginationButton}
          >
            이전
          </button>
          {[...Array(totalPagesApproved).keys()].map((number) => (
            <button
              key={number + 1}
              onClick={() => paginateApproved(number + 1)}
              className={`${styles.paginationButton} ${currentPageApproved === number + 1 ? styles.activePage : ''}`}
            >
              {number + 1}
            </button>
          ))}
          <button
            onClick={() => paginateApproved(currentPageApproved + 1)}
            disabled={currentPageApproved === totalPagesApproved}
            className={styles.paginationButton}
          >
            다음
          </button>
        </div>
      </section>
    </div>
  );
}

export default ShelterAdminManager;
