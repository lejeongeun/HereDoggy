import React, { useEffect, useRef, useState } from "react";

function Test() {
  const mapRef = useRef(null);
  const [distance, setDistance] = useState(0); // 거리
  const [duration, setDuration] = useState(0); // 예상 소요 시간(분)

  useEffect(() => {
    console.log("window.kakao:", window.kakao);

    // 기존 스크립트가 이미 있다면 추가하지 않음
    if (window.kakao && window.kakao.maps) {
      loadKakaoMap();
      return;
    }

    const script = document.createElement("script");
    script.src =
      "//dapi.kakao.com/v2/maps/sdk.js?appkey=4b28d66be90b2192c69cac9d15b4edda&autoload=false&libraries=services";
    script.async = true;
    document.head.appendChild(script);

    script.onload = loadKakaoMap;
    console.log("카카오맵 스크립트 로드 O", window.kakao);

    function loadKakaoMap() {
      if (!window.kakao || !window.kakao.maps) {
        // 아직도 kakao.maps(지도 그릴 준비)가 없으면 0.5초 후 재시도
        setTimeout(loadKakaoMap, 500);
        return;
      }
      window.kakao.maps.load(() => {
        const container = mapRef.current;
        const options = {
          center: new window.kakao.maps.LatLng(37.5665, 126.9780), // 서울시청 좌표
          level: 5,
        };
        const map = new window.kakao.maps.Map(container, options);

        let linePath = [];
        let polyline = null;

        // 지도 클릭 이벤트
        window.kakao.maps.event.addListener(map, "click", (mouseEvent) => {
          const latlng = mouseEvent.latLng;
          linePath.push(latlng);
          
          // 폴리라인 사용 부분
          if (polyline) polyline.setMap(null);
          polyline = new window.kakao.maps.Polyline({
            path: linePath,
            strokeWeight: 4,
            strokeColor: "#FF0000",
            strokeOpacity: 0.7,
            strokeStyle: "solid",
          });
          polyline.setMap(map);  // 지도 위에 표시
          // 여기에서 거리 계산
          const length = polyline.getLength(); // 단위: 미터(m)
          setDistance(length);

          // 예상 소요 시간
          const walkSpeed = 67; // m/min
          setDuration(length > 0 ? Math.round(length / walkSpeed) : 0);
        });

        window.saveCoords = () => {
          const coords = linePath.map((latlng) => ({
            lat: latlng.getLat(),
            lng: latlng.getLng(),
          }));
          const blob = new Blob([JSON.stringify(coords, null, 2)], {
            type: "application/json",
          });
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          link.download = "route.json";
          link.click();
        };
      });
    }

    // eslint-disable-next-line
  }, []);

 return (
    <div>
      <div
        ref={mapRef}
        style={{ width: "100%", height: "400px", marginBottom: "10px" }}
      />
      <div style={{ margin: "8px 0" }}>
        <strong>총 거리:</strong> {distance.toFixed(1)} m
        <span style={{ marginLeft: 16 }}>
          <strong>예상 소요 시간:</strong> {duration} 분
        </span>
      </div>
      <button onClick={() => window.saveCoords()}>좌표 JSON 다운로드</button>
    </div>
  );
}

export default Test;