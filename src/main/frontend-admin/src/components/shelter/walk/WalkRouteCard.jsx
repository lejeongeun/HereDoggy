import "../../../styles/shelter/walk/walkRouteCard.css";
import default_map_img from "../../../assets/default_map_img.png";

function WalkRouteCard({ route, onDelete, onSelect }) {
  const thumbnail =
    route.thumbnailBase64 ? route.thumbnailBase64 :
    route.thumbnailUrl ? route.thumbnailUrl :
    getNaverStaticMapUrl(route.points); // fallback

  return (
    <div className="walk-route-card" onClick={onSelect}>
      <img
        src={thumbnail}
        alt="지도 썸네일"
        className="walk-route-thumb"
        onError={(e) => { e.target.src = default_map_img; }}
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
const getNaverStaticMapUrl = (points) => {
  const baseUrl = 'https://naveropenapi.apigw.ntruss.com/map-static/v2/raster';
  const width = 300;
  const height = 150;
  const level = 16;

  if (!points || points.length === 0) return default_map_img;

  const center = `${points[0].lng},${points[0].lat}`;
  const pathCoords = points.map(p => `${p.lng},${p.lat}`).join('|');
  const pathParam = `path=2|0x0F8A5F|${pathCoords}`;

  return `${baseUrl}?center=${center}&level=${level}&w=${width}&h=${height}&${pathParam}`;
};
export default WalkRouteCard;
