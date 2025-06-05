import { useEffect, useRef, useState } from 'react';
import '../../../styles/shelter/walk/walkRegister.css';

function WalkRegister() {
  const mapRef = useRef(null);
  const markerRef = useRef(null);
  const polylineRef = useRef(null);

  const [address, setAddress] = useState('');
  const [zipcode, setZipcode] = useState('');
  const [latlng, setLatlng] = useState(null);

  const [path, setPath] = useState([]);
  const [distance, setDistance] = useState(0);
  const [naverLoaded, setNaverLoaded] = useState(false);

  // 1. 네이버 지도 및 geometry 서브모듈 동적 로드 + 지도 생성
  useEffect(() => {
    let script;
    function createMapAndListener() {
      const { naver } = window;
      if (!naver) return;

      mapRef.current = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.5665, 126.9780),
        zoom: 15,
      });

      naver.maps.Event.addListener(mapRef.current, 'click', function (e) {
        const latlng = e.coord;
        setPath(prev => [...prev, { lat: latlng.lat(), lng: latlng.lng() }]);
      });
      setNaverLoaded(true); // 서브모듈까지 다 로드 후 true
    }

    if (!window.naver) {
      script = document.createElement('script');
      script.src = `https://oapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=${process.env.REACT_APP_NAVER_CLIENT_ID}&submodules=geocoder,geometry`;
      script.async = true;
      script.onload = createMapAndListener;
      document.body.appendChild(script);
    } else {
      createMapAndListener();
    }

    return () => {
      if (script) document.body.removeChild(script);
    };
  }, []);

  // 2. path 변경시 폴리라인 생성 및 거리 계산
useEffect(() => {
  if (
    !naverLoaded ||
    !window.naver ||
    !window.naver.maps ||
    !window.naver.maps.Polyline ||
    !mapRef.current
  ) {
    return;
  }

  // 기존 폴리라인 제거
  if (polylineRef.current) {
    console.log("기존 폴리라인 제거");
    polylineRef.current.setMap(null);
    polylineRef.current = null;
  }

  if (path.length > 1) {
    const polyPath = path.map(p => {
      if (typeof p.lat !== 'number' || typeof p.lng !== 'number') {
        console.log("잘못된 좌표", p);
      }
      return new window.naver.maps.LatLng(p.lat, p.lng);
    });

    console.log('폴리라인 생성! polyPath:', polyPath);

    polylineRef.current = new window.naver.maps.Polyline({
      map: mapRef.current,
      path: polyPath,
      strokeColor: "#ff0000",
      strokeWeight: 4,
      strokeOpacity: 1,
      strokeStyle: 'solid'
    });

    console.log('polylineRef.current:', polylineRef.current);

    if (window.naver.maps.GeometryUtil) {
      const len = window.naver.maps.GeometryUtil.getLength(polylineRef.current.getPath());
      setDistance(len);
    } else {
      console.log("GeometryUtil 서브모듈이 없습니다.");
      setDistance(0);
    }
  } else {
    setDistance(0);
  }
}, [path, naverLoaded]);



  // 3. 주소 검색 및 지도 중심 이동
  const handleAddressSearch = () => {
    new window.daum.Postcode({
      oncomplete: function (data) {
        const addr = data.address;
        setAddress(addr);
        setZipcode(data.zonecode);
        window.naver.maps.Service.geocode({
          query: addr,
        }, function(status, response) {
          if (status === window.naver.maps.Service.Status.OK && response.v2.addresses.length > 0) {
            const result = response.v2.addresses[0];
            setLatlng({ lat: Number(result.y), lng: Number(result.x) });
            if (mapRef.current) {
              mapRef.current.setCenter(new window.naver.maps.LatLng(Number(result.y), Number(result.x)));
            }
          } else {
            alert("지도를 찾을 수 없습니다.");
          }
        });
      }
    }).open();
  };

  // 4. latlng이 바뀔 때마다 마커 표시
  useEffect(() => {
    if (!latlng || !window.naver || !mapRef.current) return;
    const { naver } = window;
    mapRef.current.setCenter(new naver.maps.LatLng(latlng.lat, latlng.lng));
    if (!markerRef.current) {
      markerRef.current = new naver.maps.Marker({
        position: new naver.maps.LatLng(latlng.lat, latlng.lng),
        map: mapRef.current,
      });
    } else {
      markerRef.current.setPosition(new naver.maps.LatLng(latlng.lat, latlng.lng));
    }
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
    alert('산책로가 저장됩니다! (콘솔확인)');
    console.log(walkData);
  };

  const estimatedTime = distance > 0 ? Math.round((distance / 1000) * 20) : 0;

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
          <div className="walkregister-zipcode">
            우편번호: {zipcode}
          </div>
        )}
      </div>
      <div id="map" className="walkregister-map" />
      <div className="walkregister-path-info">
        <span>경로에 점을 찍어 산책로를 그려보세요!</span><br />
        <b>총 거리:</b> {distance > 0 ? (distance / 1000).toFixed(2) + ' km' : '-'}
        &nbsp; | &nbsp;
        <b>예상 시간:</b> {distance > 0 ? estimatedTime + ' 분' : '-'}
      </div>
      <div style={{ marginTop: 8 }}>
        <button onClick={handleClearPath} type="button" className="walkregister-btn-outline">경로 초기화</button>
        <button onClick={handleSave} type="button" className="walkregister-btn-solid" style={{ marginLeft: 8 }}>경로 저장</button>
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

