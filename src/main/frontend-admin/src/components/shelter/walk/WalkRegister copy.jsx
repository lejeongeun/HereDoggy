import { useEffect, useRef, useState } from "react";
import "../../../styles/shelter/walk/walkRegister.css";

function WalkRegister() {
  const mapRef = useRef(null);
  const markerRef = useRef(null);
  const polylineRef = useRef(null);

  const [address, setAddress] = useState("");
  const [zipcode, setZipcode] = useState("");
  const [latlng, setLatlng] = useState(null);

  const [path, setPath] = useState([]);
  const [distance, setDistance] = useState(0);

  // 지도 생성
  useEffect(() => {
    if (!window.kakao || !window.kakao.maps) return;

    mapRef.current = new window.kakao.maps.Map(document.getElementById("map"), {
      center: new window.kakao.maps.LatLng(37.5665, 126.9780),
      level: 4,
    });

    window.kakao.maps.event.addListener(mapRef.current, "click", function (e) {
      const latlng = e.latLng;
      setPath((prev) => [...prev, { lat: latlng.getLat(), lng: latlng.getLng() }]);
    });

    // 언마운트시 리소스 해제
    return () => {
      if (polylineRef.current) {
        polylineRef.current.setMap(null);
        polylineRef.current = null;
      }
      if (markerRef.current) {
        markerRef.current.setMap(null);
        markerRef.current = null;
      }
      mapRef.current = null;
    };
  }, []);

  // 경로 바뀔 때 폴리라인 & 거리계산
 useEffect(() => {
    let retryTimer;
    function drawLine() {
      if (
        !window.kakao ||
        !window.kakao.maps ||
        !window.kakao.maps.geometry ||
        !window.kakao.maps.geometry.spherical ||
        !mapRef.current
      ) {
        // geometry 안 뜨면 100ms 후 재시도
        console.log("폴리라인 재시도: 카카오맵 geometry 미로드 상태");
        retryTimer = setTimeout(drawLine, 100);
        return;
      }

      // 기존 폴리라인 제거
      if (polylineRef.current) {
        polylineRef.current.setMap(null);
        polylineRef.current = null;
      }
      if (path.length > 1) {
        const linePath = path.map(
          (p) => new window.kakao.maps.LatLng(p.lat, p.lng)
        );
        polylineRef.current = new window.kakao.maps.Polyline({
          map: mapRef.current,
          path: linePath,
          strokeWeight: 7,
          strokeColor: "#FF0000",
          strokeOpacity: 1,
          strokeStyle: "solid",
        });

        // 거리 계산
        const len = window.kakao.maps.geometry.spherical.computeLength(linePath);
        setDistance(len);
      } else {
        setDistance(0);
      }
    }

    drawLine();
    return () => retryTimer && clearTimeout(retryTimer);
  }, [path]);

  // 주소 검색 (다음 우편번호 서비스 + 카카오맵 주소검색)
  const handleAddressSearch = () => {
    new window.daum.Postcode({
      oncomplete: function (data) {
        const addr = data.address;
        setAddress(addr);
        setZipcode(data.zonecode);

        const geocoder = new window.kakao.maps.services.Geocoder();
        geocoder.addressSearch(addr, function (result, status) {
          if (status === window.kakao.maps.services.Status.OK && result[0]) {
            const lat = parseFloat(result[0].y);
            const lng = parseFloat(result[0].x);
            setLatlng({ lat, lng });
            if (mapRef.current) {
              mapRef.current.setCenter(new window.kakao.maps.LatLng(lat, lng));
            }
          } else {
            alert("지도를 찾을 수 없습니다.");
          }
        });
      },
    }).open();
  };

  // 마커 표시
  useEffect(() => {
    if (!latlng || !window.kakao || !mapRef.current) return;
    if (!markerRef.current) {
      markerRef.current = new window.kakao.maps.Marker({
        position: new window.kakao.maps.LatLng(latlng.lat, latlng.lng),
        map: mapRef.current,
      });
    } else {
      markerRef.current.setPosition(
        new window.kakao.maps.LatLng(latlng.lat, latlng.lng)
      );
    }
    mapRef.current.setCenter(new window.kakao.maps.LatLng(latlng.lat, latlng.lng));
  }, [latlng]);

  // 경로 초기화
  const handleClearPath = () => setPath([]);

  // 경로 저장
  const handleSave = () => {
    const estimatedTime = distance > 0 ? Math.round((distance / 1000) * 20) : 0;
    const walkData = {
      address,
      zipcode,
      routeCoords: path,
      distance,
      estimatedTime,
    };
    alert("산책로가 저장됩니다! (콘솔확인)");
    console.log(walkData);
  };

  const estimatedTime =
    distance > 0 ? Math.round((distance / 1000) * 20) : 0;

  return (
    <div className="walkregister-wrap">
      <div className="walkregister-address-area">
        <label className="walkregister-label">보호소 주소</label>
        <div className="walkregister-row">
          <input
            type="text"
            value={address}
            readOnly
            placeholder="주소를 검색하세요"
            className="walkregister-address-input"
          />
          <button
            type="button"
            onClick={handleAddressSearch}
            className="walkregister-search-btn"
          >
            주소 검색
          </button>
        </div>
        {zipcode && (
          <div className="walkregister-zipcode">우편번호: {zipcode}</div>
        )}
      </div>
      <div id="map" className="walkregister-map" />
      <div className="walkregister-path-info">
        <span>경로에 점을 찍어 산책로를 그려보세요!</span>
        <br />
        <b>총 거리:</b>{" "}
        {distance > 0 ? (distance / 1000).toFixed(2) + " km" : "-"}
        &nbsp; | &nbsp;
        <b>예상 시간:</b> {distance > 0 ? estimatedTime + " 분" : "-"}
      </div>
      <div style={{ marginTop: 8 }}>
        <button
          onClick={handleClearPath}
          type="button"
          className="walkregister-btn-outline"
        >
          경로 초기화
        </button>
        <button
          onClick={handleSave}
          type="button"
          className="walkregister-btn-solid"
          style={{ marginLeft: 8 }}
        >
          경로 저장
        </button>
      </div>
      {address && (
        <div className="walkregister-selected-address">
          선택한 주소: {address}
        </div>
      )}
    </div>
  );
}

export default WalkRegister;
