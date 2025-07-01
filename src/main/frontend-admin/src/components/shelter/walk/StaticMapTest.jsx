import React, { useEffect, useRef, useState } from 'react';
import { toPng } from 'html-to-image';

const StaticMapCaptureTest = () => {
  const mapRef = useRef(null);
  const captureRef = useRef(null);
  const [imgUrl, setImgUrl] = useState('');
  const [points, setPoints] = useState([]);
  const polylineRef = useRef(null); // 폴리라인 인스턴스 저장

  useEffect(() => {
    const script = document.createElement('script');
    const CLIENT_ID = process.env.REACT_APP_NAVER_MAP_KEY_ID;
    script.src = `https://oapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=${CLIENT_ID}`;
    script.async = true;

    script.onload = () => {
      const map = new window.naver.maps.Map(mapRef.current, {
        center: new window.naver.maps.LatLng(37.5665, 126.9780),
        zoom: 15,
      });

      // 지도 클릭 이벤트
      window.naver.maps.Event.addListener(map, 'click', (e) => {
        const latlng = e.coord;
        setPoints((prev) => [...prev, latlng]);
      });

      mapRef.current.map = map;
    };

    document.head.appendChild(script);
    return () => document.head.removeChild(script);
  }, []);

  // 폴리라인 갱신
  useEffect(() => {
    if (!mapRef.current || !mapRef.current.map) return;
    const map = mapRef.current.map;

    // 기존 선 삭제
    if (polylineRef.current) {
      polylineRef.current.setMap(null);
    }

    if (points.length > 1) {
      polylineRef.current = new window.naver.maps.Polyline({
        map: map,
        path: points,
        strokeColor: '#FF0000',
        strokeWeight: 4,
        strokeOpacity: 0.8,
        strokeStyle: 'solid',
      });
    }
  }, [points]);

  const handleCapture = async () => {
    if (!captureRef.current) return;
    try {
      const dataUrl = await toPng(captureRef.current);
      setImgUrl(dataUrl);
    } catch (e) {
      console.error('캡처 실패', e);
    }
  };

  return (
    <div>
      <div
        ref={captureRef}
        style={{ width: 600, height: 300, border: '1px solid #ccc', marginBottom: 8 }}
      >
        <div ref={mapRef} style={{ width: '100%', height: '100%' }} />
      </div>
      <button onClick={handleCapture}>지도 캡처</button>
      <p>🖱 지도 클릭으로 경로를 추가하세요! (총 {points.length}개 지점)</p>
      {imgUrl && (
        <div>
          <h4>캡처된 썸네일</h4>
          <img src={imgUrl} alt="캡처 결과" style={{ border: '1px solid #aaa', marginTop: 8 }} />
        </div>
      )}
    </div>
  );
};

export default StaticMapCaptureTest;
