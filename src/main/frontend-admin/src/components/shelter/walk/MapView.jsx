// // src/components/shelter/walk/KakaoMapView.jsx
// import { useEffect, useRef, forwardRef, useImperativeHandle } from "react";

// const MapView = forwardRef(({ route, ...props }, ref) => {
//   const mapContainerRef = useRef();
//   const mapInstanceRef = useRef();

//   useImperativeHandle(ref, () => ({
//     panTo: (center) => {
//       if (mapInstanceRef.current && center) {
//         const kakaoCenter = new window.kakao.maps.LatLng(center.lat, center.lng);
//         mapInstanceRef.current.panTo(kakaoCenter);
//       }
//     }
//   }));

//   useEffect(() => {
//     if (!window.kakao || !window.kakao.maps) return;

//     const center = route?.points?.[0]
//       ? new window.kakao.maps.LatLng(route.points[0].lat, route.points[0].lng)
//       : new window.kakao.maps.LatLng(37.5665, 126.9780); // 서울시청

//     const map = new window.kakao.maps.Map(mapContainerRef.current, {
//       center,
//       level: 4
//     });
//     mapInstanceRef.current = map;

//     // 경로가 있으면 선(Polyline) 표시
//     if (route?.points?.length > 1) {
//       const path = route.points.map(
//         (p) => new window.kakao.maps.LatLng(p.lat, p.lng)
//       );
//       new window.kakao.maps.Polyline({
//         map,
//         path,
//         strokeWeight: 5,
//         strokeColor: "#0F8A5F",
//         strokeOpacity: 0.8
//       });
//     }
//   }, [route]);

//   return (
//     <div
//       ref={mapContainerRef}
//       style={props.style || { width: "100%", height: 300, borderRadius: 16 }}
//     />
//   );
// });

// export default MapView;
