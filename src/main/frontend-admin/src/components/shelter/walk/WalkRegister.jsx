/* global naver */
import React, { useEffect, useRef, useState } from "react";
import { toPng } from "html-to-image";
import { createRoute } from "../../../api/shelter/route";
import "../../../styles/shelter/walk/walkRegister.css";

function WalkRegister({ sheltersId, selectedRoute, setIsDrawing, onRouteSaved, isDrawing }) {
  const mapRef = useRef(null);
  const captureRef = useRef(null);
  const mapInstance = useRef(null);
  const myLocationMarker = useRef(null);
  const [distance, setDistance] = useState(0);
  const [duration, setDuration] = useState(0);
  const [linePath, setLinePath] = useState([]);
  const [polyline, setPolyline] = useState(null);
  const [routeName, setRouteName] = useState(selectedRoute?.routeName || "");
  const [description, setDescription] = useState(selectedRoute?.description || "");

  useEffect(() => {
    const loadScript = () => {
      return new Promise((resolve, reject) => {
        const existing = document.querySelector('script[src*="maps.js"]');
        if (existing) return resolve();

        const script = document.createElement("script");
        script.src = `https://oapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=${process.env.REACT_APP_NAVER_MAP_KEY_ID}`;
        script.async = true;
        script.onload = resolve;
        script.onerror = reject;
        document.head.appendChild(script);
      });
    };

    const waitForNaver = () => {
      if (window.naver?.maps) initMap();
      else setTimeout(waitForNaver, 100);
    };

    loadScript().then(waitForNaver);
  }, []);

useEffect(() => {
  if (!mapInstance.current) return;

  const map = mapInstance.current;

  // 기존 경로 삭제하고 새로 그릴 준비
  if (isDrawing) {
    if (polyline) polyline.setMap(null);
    setPolyline(null);
    setLinePath([]);
    setDistance(0);
    setDuration(0);
    setRouteName("");
    setDescription("");
    setIsDrawing(true);
    return;
  }

  // 기존 경로 선택된 경우 경로 그리기
  if (selectedRoute) {
    const points = selectedRoute.points.map(p => new naver.maps.LatLng(p.lat, p.lng));
    if (points.length > 0) map.setCenter(points[0]);

    if (polyline) polyline.setMap(null);
    if (points.length > 1) {
      const newPolyline = new naver.maps.Polyline({
        path: points,
        strokeColor: "#FF0000",
        strokeWeight: 4,
        map: map,
      });
      setPolyline(newPolyline);

      const len = calculatePathDistance(points);
      setDistance(len);
      setDuration(len > 0 ? Math.round(len / 67) : 0);
      setLinePath(points);
    }

    setRouteName(selectedRoute.routeName || "");
    setDescription(selectedRoute.description || "");
    setIsDrawing(false);
  }
}, [selectedRoute, isDrawing]);

  const haversineDistance = (lat1, lon1, lat2, lon2) => {
    const R = 6371000;
    const dLat = degToRad(lat2 - lat1);
    const dLon = degToRad(lon2 - lon1);
    const a = Math.sin(dLat / 2) ** 2 +
              Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) *
              Math.sin(dLon / 2) ** 2;
    return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  };

  const degToRad = (deg) => deg * (Math.PI / 180);

  const calculatePathDistance = (path) => {
    let total = 0;
    for (let i = 0; i < path.length - 1; i++) {
      total += haversineDistance(path[i].lat(), path[i].lng(), path[i + 1].lat(), path[i + 1].lng());
    }
    return total;
  };

  const initMap = () => {
    const map = new naver.maps.Map(mapRef.current, {
      center: new naver.maps.LatLng(37.5665, 126.9780),
      zoom: 16,
    });
    mapInstance.current = map;

    let path = [];
    let poly = null;

    naver.maps.Event.addListener(map, "click", (e) => {
      if (selectedRoute) return;

      const latlng = e.coord;
      path.push(latlng);
      setLinePath([...path]);

      if (poly) poly.setMap(null);
      poly = new naver.maps.Polyline({
        path: path,
        strokeColor: "#FF0000",
        strokeWeight: 4,
        map: map,
      });
      setPolyline(poly);

      const len = calculatePathDistance(path);
      setDistance(len);
      setDuration(len > 0 ? Math.round(len / 67) : 0);
    });

    if (navigator.geolocation) {
      navigator.geolocation.watchPosition(
        (pos) => {
          const latlng = new naver.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
          if (myLocationMarker.current) {
            myLocationMarker.current.setPosition(latlng);
          } else {
            myLocationMarker.current = new naver.maps.Marker({
              position: latlng,
              map: map,
              title: "내 위치",
            });
          }
        },
        (err) => console.error("위치 오류", err),
        { enableHighAccuracy: true }
      );
    }

    window.resetRoute = () => {
      path = [];
      setLinePath([]);
      if (poly) poly.setMap(null);
      setPolyline(null);
      setDistance(0);
      setDuration(0);
      setIsDrawing(true);
      setRouteName("");
      setDescription("");
    };
  };

  const saveRoute = async () => {
    if (linePath.length < 2) return alert("두 점 이상 경로를 찍어주세요!");
    if (!routeName.trim()) return alert("경로 이름을 입력하세요!");

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
        { lat: start.lat(), lng: start.lng(), sequence: 0, pointType: "START" },
        ...optimizedMiddle.map((p, i) => ({
          lat: p.lat(), lng: p.lng(), sequence: i + 1, pointType: "MIDDLE",
        })),
        { lat: end.lat(), lng: end.lng(), sequence: optimizedMiddle.length + 1, pointType: "END" },
      ],
    };

    let thumbnailBase64 = "";
    try {
      if (captureRef.current) {
        thumbnailBase64 = await toPng(captureRef.current);
      }
    } catch (err) {
      console.error("썸네일 캡처 실패", err);
    }

    const dataWithThumbnail = {
      ...routeData,
      thumbnailBase64,
    };

    try {
      await createRoute(sheltersId, dataWithThumbnail);
      setIsDrawing(false);
      onRouteSaved?.();
    } catch (err) {
      alert("저장 오류: " + (err.response?.data?.message || err.message));
    }
  };

  const optimizeMiddlePoints = (points) => {
    if (points.length <= 1) return points;
    const minDistance = 0.0001;
    const result = [points[0]];
    for (let i = 1; i < points.length; i++) {
      const last = result[result.length - 1];
      if (getDistance(last, points[i]) >= minDistance) {
        result.push(points[i]);
      }
    }
    return result;
  };

  const getDistance = (p1, p2) => {
    const dx = p1.lng() - p2.lng();
    const dy = p1.lat() - p2.lat();
    return Math.sqrt(dx * dx + dy * dy);
  };

  const moveToMyLocation = () => {
    if (mapInstance.current && myLocationMarker.current) {
      mapInstance.current.setCenter(myLocationMarker.current.getPosition());
    } else {
      alert("아직 내 위치 정보가 없습니다!");
    }
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
          disabled={!!selectedRoute}
        />
        <textarea
          placeholder="경로 설명"
          value={description}
          onChange={e => setDescription(e.target.value)}
          className="walk-textarea"
          disabled={!!selectedRoute}
        />
      </div>

      <div ref={captureRef} className="walk-map">
        <div ref={mapRef} style={{ width: "100%", height: "100%" }} />
      </div>

      <div className="walk-btn-group">
        {!selectedRoute && (
          <>
            <button onClick={moveToMyLocation} className="walk-btn walk-btn-blue">내 위치 보기</button>
            <button onClick={() => window.resetRoute()} className="walk-btn walk-btn-green">경로 다시 그리기</button>
            <button onClick={saveRoute} disabled={linePath.length < 2} className="walk-btn walk-btn-red">경로 저장</button>
          </>
        )}
      </div>

      <div className="walk-info-row">
        <strong>총 거리:</strong> {distance.toFixed(1)} m
        <span style={{ marginLeft: 16 }}>
          <strong>예상 소요 시간:</strong> {duration} 분
        </span>
      </div>

      {selectedRoute && (
        <div style={{ textAlign: "center", fontSize: "12px", color: "#f44336" }}>
          <span>경로를 수정하시려면 기존 경로를 삭제 후 다시 만들어주세요.</span>
        </div>
      )}
    </div>
  );
}

export default WalkRegister;
