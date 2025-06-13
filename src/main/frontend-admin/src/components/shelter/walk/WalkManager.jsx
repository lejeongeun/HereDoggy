import { useState, useRef } from "react";
import WalkList from "../../../pages/shelter/WalkList";
import WalkRegister from "./WalkRegister";
import "../../../styles/shelter/walk/walkManager.css";

function WalkManager({ sheltersId }) {
  const [selectedRoute, setSelectedRoute] = useState(null);
  const [isDrawing, setIsDrawing] = useState(false); // 새 경로 추가 여부

  const walkListRef = useRef();

  // onAddNewRoute 함수 정의 (새로운 경로 추가)
  const handleAddNewRoute = () => {
    setSelectedRoute(null); // 기존 경로 해제
    setIsDrawing(true); // 새 경로 그리기 모드로 설정
  };
  const refreshRouteList = () => {
    walkListRef.current?.fetchRoutes(); // 자식 컴포넌트의 fetch 호출
  };
  return (
    <div className="walk-manager-wrap">
      <div className="walk-list-panel">
        <WalkList
          ref={walkListRef} // ref연결
          sheltersId={sheltersId}
          onRouteSelect={(route) => {
            setSelectedRoute(route);
            setIsDrawing(false);
          }}
          onAddNewRoute={handleAddNewRoute}
        />
      </div>

      {/* 오른쪽 지도 패널은 고정 */}
      <div className="walk-register-fixed">
        <WalkRegister
          sheltersId={sheltersId}
          selectedRoute={selectedRoute}
          isDrawing={isDrawing}
          setIsDrawing={setIsDrawing}
           onRouteSaved={refreshRouteList} // 콜백 전달
        />
      </div>
    </div>
  );
}

export default WalkManager;
