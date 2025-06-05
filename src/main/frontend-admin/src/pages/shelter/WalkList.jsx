import { Link } from "react-router-dom";

function WalkList() {
  // 예시 데이터 (실제로는 props, 상태, fetch 등에서 받아옴)
  const walkRoutes = [
    // { id: 1, name: "한강 산책로", ... }
    // { id: 2, name: "공원 산책로", ... }
  ];
  const maxRoutes = 2;

  // 2개 꽉 안 차면 빈 카드
  const cards = [...walkRoutes];
  while (cards.length < maxRoutes) {
    cards.push(null);
  }

  return (
    <div style={{ maxWidth: 800, margin: "0 auto", padding: 32 }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h2>산책로 관리</h2>
      </div>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 24, marginTop: 32 }}>
        {cards.map((route, idx) =>
          route ? (
            <div
              key={route.id}
              style={{
                border: "1px solid #eee",
                borderRadius: 12,
                padding: 24,
                background: "#fafafa",
                minHeight: 200,
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              <div style={{ fontSize: 18, fontWeight: "bold" }}>{route.name}</div>
              {/* 기타 정보, 수정/삭제 버튼 등 */}
            </div>
          ) : (
            // 빈 카드(추가)
            <Link
              to="/shelter/walkregister"
              key={idx}
              style={{
                border: "2px dashed #aac",
                borderRadius: 12,
                padding: 24,
                background: "#f4f7fa",
                minHeight: 200,
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
                alignItems: "center",
                textDecoration: "none",
                color: "#888",
                fontSize: 36,
                fontWeight: "bold",
                cursor: "pointer",
                transition: "border 0.2s",
              }}
            >
              <span style={{ fontSize: 60, marginBottom: 12 }}>+</span>
              <div style={{ fontSize: 18 }}>산책로 추가</div>
            </Link>
          )
        )}
      </div>
    </div>
  );
}

export default WalkList;
