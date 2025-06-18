import React, { useEffect, useRef, useState } from "react";
import { createRoute } from "../../../api/shelter/route";
import "../../../styles/shelter/walk/walkRegister.css";

function WalkRegister({ sheltersId, selectedRoute, setIsDrawing, onRouteSaved }) {
  const mapRef = useRef(null);
  const mapInstance = useRef(null);
  const myLocationMarker = useRef(null);
  const myLocationLatLng = useRef(null);

  const [hasLocation, setHasLocation] = useState(false);
  const [distance, setDistance] = useState(0);
  const [duration, setDuration] = useState(0);
  const [linePath, setLinePath] = useState([]);
  const [polyline, setPolyline] = useState(null);
  const [routeName, setRouteName] = useState(selectedRoute ? selectedRoute.routeName : "");
  const [description, setDescription] = useState(selectedRoute ? selectedRoute.description : "");

  // 최초 1회 지도 로딩만 실행
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
          level: 3,
          draggable: true,     // 지도 이동 가능
          zoomable: true,      // 줌 가능
          scrollwheel: true    // 마우스 휠로 줌 가능
        };
        const map = new window.kakao.maps.Map(container, options);
        mapInstance.current = map;

        let _linePath = [];
        let _polyline = null;

        // 지도 클릭 시 폴리라인 그리기
        window.kakao.maps.event.addListener(map, "click", (mouseEvent) => {
          if (selectedRoute) return; // 이미 경로가 선택되어 있으면 그리기 불가

          const latlng = mouseEvent.latLng;
          _linePath.push(latlng);
          setLinePath([..._linePath]); // 새로운 점을 linePath에 추가

          // 기존 폴리라인이 있으면 제거
          if (_polyline) _polyline.setMap(null);

          // 새로운 폴리라인 그리기
          _polyline = new window.kakao.maps.Polyline({
            path: _linePath,
            strokeWeight: 4,
            strokeColor: "#FF0000",
            strokeOpacity: 0.7,
            strokeStyle: "solid",
          });
          _polyline.setMap(map);
          setPolyline(_polyline); // 새로운 폴리라인을 상태에 설정

          // 거리 및 시간 계산
          const length = _polyline.getLength();
          setDistance(length);
          setDuration(length > 0 ? Math.round(length / 67) : 0);
        });

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
            (error) => {
              console.error("위치 오류:", error);  // 콘솔에 상세 정보 출력
              if (error.code === error.PERMISSION_DENIED) {
                alert("위치 권한이 거부되었습니다. 브라우저 설정을 확인해주세요.");
              } else if (error.code === error.POSITION_UNAVAILABLE) {
                alert("위치 정보를 찾을 수 없습니다. GPS나 인터넷 상태를 확인해주세요.");
              } else if (error.code === error.TIMEOUT) {
                alert("위치 정보를 받아오지 못했습니다. 다시 시도해주세요.");
              } else {
                alert("알 수 없는 위치 오류가 발생했습니다.");
              }
            },
            { enableHighAccuracy: true }
          );
        }

        window.addEventListener("beforeunload", () => {
          if (watchId) navigator.geolocation.clearWatch(watchId);
        });

        window.resetRoute = () => {
          _linePath = [];
          setLinePath([]);
          if (_polyline) _polyline.setMap(null);
          setPolyline(null);
          setDistance(0);
          setDuration(0);
          setIsDrawing(true);
          setRouteName("");
          setDescription("");
        };
      });
    }

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
  }, []); // selectedRoute 넣지 마세요! 최초 1회만 실행

  // selectedRoute가 바뀌었을 때만 경로 표시 또는 초기화
  useEffect(() => {
    if (selectedRoute && mapInstance.current && window.kakao && window.kakao.maps) {
      // 1. selectedRoute 있을 때: 경로 표시
      const points = selectedRoute.points.map(
        p => new window.kakao.maps.LatLng(p.lat, p.lng)
      );

      if (points.length > 0) {
        mapInstance.current.setCenter(points[0]);
      }

      if (polyline) polyline.setMap(null);

      if (points.length > 1) {
        const newPolyline = new window.kakao.maps.Polyline({
          path: points,
          strokeWeight: 4,
          strokeColor: "#FF0000",
          strokeOpacity: 0.7,
          strokeStyle: "solid",
        });
        newPolyline.setMap(mapInstance.current);
        setPolyline(newPolyline);

        const length = newPolyline.getLength();
        setDistance(length);
        setDuration(length > 0 ? Math.round(length / 67) : 0);
        setLinePath(points);
      }

      setRouteName(selectedRoute.routeName || "");
      setDescription(selectedRoute.description || "");
      setIsDrawing(false);
    } else {
      // 2. selectedRoute가 null일 때: 초기화
      setRouteName("");
      setDescription("");
      setDistance(0);
      setDuration(0);
      setLinePath([]);
      if (polyline) polyline.setMap(null);
      setPolyline(null);
      setIsDrawing(true);
    }
  }, [selectedRoute]);

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

    const path = routeData.points.map(p => `${p.lng},${p.lat}`).join('|');
    const centerLat = routeData.points[0].lat;
    const centerLng = routeData.points[0].lng;
    const kakaoUrl = `https://dapi.kakao.com/v2/maps/staticmap?center=${centerLat},${centerLng}&level=4&size=300x150&appkey=${process.env.REACT_APP_KAKAO_REST_API_KEY}&path=lw:4|lc:0F8A5F|${path}`;
  console.log("카카오 썸네일 URL:", kakaoUrl); 
    const routeDataWithThumbnail = {
      ...routeData,
      thumbnailUrl: kakaoUrl
    };

    try {
      const res = await createRoute(sheltersId, routeDataWithThumbnail);
      console.log("서버 응답:", res.data);
      setIsDrawing(false);

      if (onRouteSaved) onRouteSaved(); // 저장 후 산책경로 리스트 갱신 요청

    } catch (err) {
      const msg = err.response?.data?.message || err.message;
      alert("저장 오류: " + msg);
    }
  };

  const moveToMyLocation = () => {
    if (mapInstance.current && myLocationLatLng.current) {
      mapInstance.current.setCenter(myLocationLatLng.current);
    } else {
      alert("아직 내 위치 정보가 없습니다!");
    }
  };

  const optimizeMiddlePoints = (points) => {
    if (points.length <= 1) return points;
    const minDistance = 0.0001; // 최소 거리 기준 설정
    const result = [points[0]]; // 첫 번째 점 추가
    for (let i = 1; i < points.length; i++) {
      const last = result[result.length - 1];
      if (getDistance(last, points[i]) >= minDistance) {
        result.push(points[i]);
      }
    }
    return result;
  };

  const getDistance = (p1, p2) => {
    const dx = p1.getLng() - p2.getLng();
    const dy = p1.getLat() - p2.getLat();
    return Math.sqrt(dx * dx + dy * dy);
  };

  return (
    <div className="walk-register-wrap">
      <div className="walk-input-group">
        <input
          type="text"
          placeholder="경로 이름"
          value={routeName}
          onChange={e => setRouteName(e.target.value)}
          className="walk-input"
          disabled={!!selectedRoute}  // 기존 경로는 수정 불가
        />
        <textarea
          placeholder="경로 설명"
          value={description}
          onChange={e => setDescription(e.target.value)}
          className="walk-textarea"
          disabled={!!selectedRoute}  // 기존 경로는 수정 불가
        />
      </div>

      <div ref={mapRef} className="walk-map" />

      <div className="walk-btn-group">
        {/* `selectedRoute`가 없을 때만 버튼 보이기 */}
        {!selectedRoute ? (
          <>
            <button onClick={moveToMyLocation} className="walk-btn walk-btn-blue">내 위치 보기</button>
            <button onClick={() => window.resetRoute()} className="walk-btn walk-btn-green">경로 다시 그리기</button>
            <button onClick={saveRoute} disabled={linePath.length < 2} className="walk-btn walk-btn-red">경로 저장</button>
          </>
        ) : null}
      </div>

      {/* 총 거리와 예상 소요 시간 */}
      <div className="walk-info-row">
        <strong>총 거리:</strong> {distance.toFixed(1)} m
        <span style={{ marginLeft: 16 }}>
          <strong>예상 소요 시간:</strong> {duration} 분
        </span>
      </div>

      {/* 경로 수정 안내 메시지 - 총 거리와 예상 소요 시간 아래에 표시 */}
      {selectedRoute && (
        <div style={{ textAlign: "center", fontSize: "12px", color: "#f44336" }}>
          <span>경로를 수정하시려면 기존 경로를 삭제 후 다시 만들어주세요.</span>
        </div>
      )}
    </div>
  );
}

export default WalkRegister;
