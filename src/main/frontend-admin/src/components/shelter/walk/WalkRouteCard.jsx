

import { Link } from "react-router-dom";

function WalkRouteCard({ route, onDelete }) {
  const handleDelete = (e) => {
    e.preventDefault(); // 카드 클릭 방지
    if (window.confirm("정말 삭제하시겠습니까?")) {
      onDelete(route.id);
    }
  };

  return (
    <Link
      to={`/shelter/walkregister?lat=${route.centerLat}&lng=${route.centerLng}`}
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
        textDecoration: "none",
        color: "#333",
      }}
    >
      <img
        src={route.thumbnailUrl}
        alt="미리보기"
        style={{ width: "100%", height: 120, objectFit: "cover", marginBottom: 12 }}
      />
      <div style={{ fontSize: 18, fontWeight: "bold" }}>{route.routeName}</div>
      <div style={{ marginTop: 12 }}>
        <div><strong>거리:</strong> {route.totalDistance.toFixed(1)} m</div>
        <div><strong>시간:</strong> {route.expectedDuration} 분</div>
      </div>
      <button onClick={handleDelete} style={{
        marginTop: 12,
        background: "#f44336",
        color: "#fff",
        border: "none",
        padding: "6px 12px",
        borderRadius: 4,
        cursor: "pointer"
      }}>
        삭제
      </button>
    </Link>
  );
}

export default WalkRouteCard;
