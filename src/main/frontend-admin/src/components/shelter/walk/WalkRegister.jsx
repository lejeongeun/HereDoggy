import React, { useEffect, useRef, useState } from "react";
import { createRoute } from "../../../api/shelter/route";

function WalkRegister({ sheltersId }) {
  const mapRef = useRef(null);
  const mapInstance = useRef(null);          // 지도 인스턴스
  const myLocationMarker = useRef(null);     // 내 위치 마커
  const myLocationLatLng = useRef(null);     // 내 위치 좌표

  const [distance, setDistance] = useState(0);
  const [duration, setDuration] = useState(0);
  const [linePath, setLinePath] = useState([]);
  const [polyline, setPolyline] = useState(null);
  const [isDrawing, setIsDrawing] = useState(true);

  const [routeName, setRouteName] = useState("");
  const [description, setDescription] = useState("");

  // 카카오맵 로드 및 초기화
  useEffect(() => {
    function loadKakaoMap() {
      if (!window.kakao || !window.kakao.maps) {
        setTimeout(loadKakaoMap, 500);
        return;
      }
      window.kakao.maps.load(() => {
        const container = mapRef.current;
        const options = {
          center: new window.kakao.maps.LatLng(37.5665, 126.9780),
          level: 5,
        };
        const map = new window.kakao.maps.Map(container, options);
        mapInstance.current = map;

        // 경로 그리기
        let _linePath = [];
        let _polyline = null;

        window.kakao.maps.event.addListener(map, "click", (mouseEvent) => {
          if (!isDrawing) return;
          const latlng = mouseEvent.latLng;
          _linePath.push(latlng);
          setLinePath([..._linePath]);

          // 폴리라인 그리기
          if (_polyline) _polyline.setMap(null);
          _polyline = new window.kakao.maps.Polyline({
            path: _linePath,
            strokeWeight: 4,
            strokeColor: "#FF0000",
            strokeOpacity: 0.7,
            strokeStyle: "solid",
          });
          _polyline.setMap(map);
          setPolyline(_polyline);

          // 거리/시간 계산
          const length = _polyline.getLength();
          setDistance(length);
          setDuration(length > 0 ? Math.round(length / 67) : 0);
        });

        // 내 위치 마커/좌표 저장
        let watchId = null;
        if (navigator.geolocation) {
          watchId = navigator.geolocation.watchPosition(
            (pos) => {
              const lat = pos.coords.latitude;
              const lng = pos.coords.longitude;
              const loc = new window.kakao.maps.LatLng(lat, lng);
              myLocationLatLng.current = loc;
              if (myLocationMarker.current) {
                myLocationMarker.current.setPosition(loc);
              } else {
                myLocationMarker.current = new window.kakao.maps.Marker({
                  map: map,
                  position: loc,
                  title: "내 위치",
                });
              }
            },
            () => {
              alert("실시간 위치 정보를 사용할 수 없습니다.");
            },
            { enableHighAccuracy: true }
          );
        }
        window.addEventListener("beforeunload", () => {
          if (watchId) navigator.geolocation.clearWatch(watchId);
        });

        // 경로 초기화 함수
        window.resetRoute = () => {
          _linePath = [];
          setLinePath([]);
          if (_polyline) _polyline.setMap(null);
          setPolyline(null);
          setDistance(0);
          setDuration(0);
          setIsDrawing(true);
        };
      });
    }

    // 스크립트 추가(geometry 없이!)
    if (window.kakao && window.kakao.maps) {
      loadKakaoMap();
      return;
    }
    const script = document.createElement("script");
    script.src =
      `//dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.REACT_APP_KAKAO_MAP_API_KEY}&autoload=false&libraries=services`;
    script.async = true;
    script.onload = loadKakaoMap;
    document.head.appendChild(script);

    // eslint-disable-next-line
  }, []);

  // "내 위치 보기" 버튼 클릭시 지도 중심 이동
  const moveToMyLocation = () => {
    if (mapInstance.current && myLocationLatLng.current) {
      mapInstance.current.setCenter(myLocationLatLng.current);
    } else {
      alert("아직 내 위치 정보가 없습니다!");
    }
  };

  // 중간점 최적화 (10m 이상만)
  const optimizeMiddlePoints = (points) => {
    if (points.length <= 1) return points;
    const minDistance = 0.0001; // 위경도 약 10m
    const result = [points[0]];
    for (let i = 1; i < points.length; i++) {
      const last = result[result.length - 1];
      if (getDistance(last, points[i]) >= minDistance) {
        result.push(points[i]);
      }
    }
    return result;
  };
  // 위경도 거리(geometry 없이)
  const getDistance = (p1, p2) => {
    const dx = p1.getLng() - p2.getLng();
    const dy = p1.getLat() - p2.getLat();
    return Math.sqrt(dx * dx + dy * dy);
  };

  // 경로 저장
const saveRoute = async () => {
  if (linePath.length < 2) {
    alert("두 점 이상 경로를 찍어주세요!");
    return;
  }
  if (!routeName.trim()) {
    alert("경로 이름을 입력하세요!");
    return;
  }

  const start = linePath[0];
  const end = linePath[linePath.length - 1];
  const middle = linePath.slice(1, linePath.length - 1);
  const optimizedMiddle = optimizeMiddlePoints(middle);

  const routeData = {
    routeName: routeName.trim(),
    description: description || "",
    totalDistance: distance,
    expectedDuration: duration,
    points: [
      {
        lat: start.getLat(),
        lng: start.getLng(),
        sequence: 0,
        pointType: "START",
      },
      ...optimizedMiddle.map((p, i) => ({
        lat: p.getLat(),
        lng: p.getLng(),
        sequence: i + 1,
        pointType: "MIDDLE",
      })),
      {
        lat: end.getLat(),
        lng: end.getLng(),
        sequence: optimizedMiddle.length + 1,
        pointType: "END",
      },
    ],
  };

  try {
    // 카카오 Static Map URL (중앙 기준)
    const centerLat = start.getLat();
    const centerLng = start.getLng();
    const kakaoUrl = `https://dapi.kakao.com/v2/maps/staticmap?center=${centerLat},${centerLng}&level=5&size=300x150&appkey=${process.env.REACT_APP_KAKAO_REST_API_KEY}`;

    const imgRes = await fetch(kakaoUrl);
    const blob = await imgRes.blob();
    const file = new File([blob], "thumbnail.png", { type: "image/png" });

    const formData = new FormData();
    formData.append(
      "routeData",
      new Blob([JSON.stringify(routeData)], { type: "application/json" })
    );
    formData.append("thumbnail", file);

    await createRoute(sheltersId, formData);

    alert("경로가 저장되었습니다!");
    setIsDrawing(false);
    // window.resetRoute();
  } catch (err) {
    const msg = err.response?.data?.message || err.message;
    alert("저장 오류: " + msg);
  }
};

  return (
    <div>
      {/* 경로 이름/설명 */}
      <div style={{ margin: "8px 0" }}>
        <input
          type="text"
          placeholder="경로 이름"
          value={routeName}
          onChange={(e) => setRouteName(e.target.value)}
          style={{
            width: "100%",
            marginBottom: "8px",
            padding: "8px",
            border: "1px solid #ccc",
            borderRadius: "4px",
          }}
        />
        <textarea
          placeholder="경로 설명"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          style={{
            width: "100%",
            padding: "8px",
            border: "1px solid #ccc",
            borderRadius: "4px",
            minHeight: "60px",
          }}
        />
      </div>
      {/* 지도 */}
      <div
        ref={mapRef}
        style={{ width: "100%", height: "400px", marginBottom: "10px" }}
      />
      {/* "내 위치 보기" 버튼 */}
      <div style={{ margin: "10px 0" }}>
        <button
          onClick={moveToMyLocation}
          style={{
            padding: "8px 16px",
            background: "#2196f3",
            color: "#fff",
            border: "none",
            borderRadius: "4px",
            marginRight: "8px",
          }}
        >
          내 위치 보기
        </button>
        <button
          onClick={() => window.resetRoute()}
          style={{
            padding: "8px 16px",
            background: "#4caf50",
            color: "#fff",
            border: "none",
            borderRadius: "4px",
            marginRight: "8px",
          }}
        >
          경로 다시 그리기
        </button>
        <button
          onClick={saveRoute}
          disabled={!isDrawing || linePath.length < 2}
          style={{
            padding: "8px 16px",
            background: "#f44336",
            color: "#fff",
            border: "none",
            borderRadius: "4px",
            opacity: !isDrawing || linePath.length < 2 ? 0.5 : 1,
          }}
        >
          경로 저장
        </button>
      </div>
      {/* 거리/시간 표시 */}
      <div style={{ margin: "8px 0" }}>
        <strong>총 거리:</strong> {distance.toFixed(1)} m
        <span style={{ marginLeft: 16 }}>
          <strong>예상 소요 시간:</strong> {duration} 분
        </span>
      </div>
    </div>
  );
}

export default WalkRegister;
