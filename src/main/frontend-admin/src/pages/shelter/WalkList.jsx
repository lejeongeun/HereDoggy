import React, { useEffect, useState, forwardRef, useImperativeHandle } from "react";
import { fetchWalkRoutes, deleteRoute } from "../../api/shelter/route";
import WalkRouteCard from "../../components/shelter/walk/WalkRouteCard";
import AddRouteCard from "../../components/shelter/walk/AddRouteCard";
import '../../styles/shelter/walk/walkList.css';

const WalkList = forwardRef(({ sheltersId, onRouteSelect, onAddNewRoute }, ref) => {
  const [walkRoutes, setWalkRoutes] = useState([]);
  const maxRoutes = 3;

  // 외부에서 호출할 수 있도록 함수로 분리
  const loadRoutes = async () => {
    try {
      const data = await fetchWalkRoutes(sheltersId);
      setWalkRoutes(Array.isArray(data) ? data : []);
    } catch (err) {
      alert("산책 경로를 불러오는 데 실패했습니다");
    }
  };

  // 부모에서 loadRoutes를 호출할 수 있게 함
  useImperativeHandle(ref, () => ({
    fetchRoutes: loadRoutes,
  }));

  useEffect(() => {
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
    <div className="walk-list-wrap">
      <h2 className="walk-list-title">산책로 관리</h2>
      <div className="walk-list-grid">
        {cards.map((route, idx) =>
          route ? (
            <WalkRouteCard
              key={route.id}
              route={route}
              onDelete={handleDelete}
              onSelect={() => onRouteSelect(route)}
            />
          ) : (
            <AddRouteCard key={`add-${idx}`} onAddNewRoute={onAddNewRoute} />
          )
        )}
      </div>
    </div>
  );
});

export default WalkList;
