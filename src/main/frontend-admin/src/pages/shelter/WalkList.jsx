// WalkList.jsx
import { useEffect, useState } from "react";
import { fetchWalkRoutes, deleteRoute } from "../../api/shelter/route";
import WalkRouteCard from "../../components/shelter/walk/WalkRouteCard";
import AddRouteCard from "../../components/shelter/walk/AddRouteCard";

function WalkList({ sheltersId }) {
  const [walkRoutes, setWalkRoutes] = useState([]);
  const maxRoutes = 3;

  useEffect(() => {
    const loadRoutes = async () => {
      try {
        const data = await fetchWalkRoutes(sheltersId);
        setWalkRoutes(Array.isArray(data) ? data : []);
      } catch (err) {
        alert("산책 경로를 불러오는 데 실패했습니다");
      }
    };
    loadRoutes();
  }, [sheltersId]);

  const handleDelete = async (routeId) => {
    if (!window.confirm("정말 이 산책로를 삭제하시겠습니까?")) return;
    try {
      await deleteRoute(sheltersId, routeId);
      alert("삭제되었습니다.");
      setWalkRoutes((prev) => prev.filter((r) => r.id !== routeId));
    } catch (err) {
      alert("삭제 실패: " + (err.response?.data?.message || err.message));
    }
  };

  const cards = [...walkRoutes];
  while (cards.length < maxRoutes) cards.push(null);

  return (
    <div style={{ maxWidth: 800, margin: "0 auto", padding: 32 }}>
      <h2>산책로 관리</h2>
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "1fr 1fr",
          gap: 24,
          marginTop: 32,
        }}
      >
        {cards.map((route, idx) =>
          route ? (
            <WalkRouteCard key={route.id} route={route} onDelete={handleDelete} />
          ) : (
            <AddRouteCard key={`add-${idx}`} />
          )
        )}
      </div>
    </div>
  );
}

export default WalkList;
