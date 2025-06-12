import '../../../styles/shelter/walk/walkRouteCard.css';

function WalkRouteCard({ route, onDelete, onSelect }) {
  // 썸네일: route.thumbnailUrl 또는 카카오 Static Map url로 fallback
  const appkey = process.env.REACT_APP_KAKAO_REST_API_KEY;
  const thumbnail =
    route.thumbnailUrl ||
    getStaticMapUrl(route.points, appkey);

   return (
    <div className="walk-route-card" onClick={onSelect}>
      <img
        src={thumbnail}
        alt={"지도 썸네일"}
        className="walk-route-thumb"
      />
      <div className="walk-route-info">
        <div className="walk-route-title-row">
          <div className="walk-route-title">{route.routeName}</div>
          <button
            className="walk-route-del-btn"
            onClick={e => {
              e.stopPropagation();
              onDelete(route.id);
            }}
          >삭제</button>
        </div>
        <div className="walk-route-desc">{route.description}</div>
        <div className="walk-route-meta">
          <strong>총 거리:</strong> {(route.totalDistance || 0).toFixed(1)} m
          <span>
            <strong>예상 시간:</strong> {route.expectedDuration || 0}분
          </span>
        </div>
      </div>
    </div>
  );

}

function getStaticMapUrl(points, appkey) {
  if (!points?.length) return `https://dapi.kakao.com/v2/maps/staticmap?center=37.5665,126.9780&level=5&size=300x150&appkey=${appkey}`;
  const center = `${points[0].lng},${points[0].lat}`;
  const path = points
    .map(p => `${p.lng},${p.lat}`)
    .join('|');
  return `https://dapi.kakao.com/v2/maps/staticmap?center=${center}&level=5&size=300x150&appkey=${appkey}&path=lw:4|lc:0F8A5F|${path}`;
}

export default WalkRouteCard;
