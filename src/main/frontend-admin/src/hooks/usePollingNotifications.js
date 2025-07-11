import { useEffect, useRef } from "react";
import api from "../api/shelter/api";
import { toast } from "react-toastify";
import { MdNotificationsActive } from "react-icons/md";
import React from "react";

// 커스텀 토스트 컴포넌트
const CustomNotificationToast = ({ count }) => (
  <div style={{ display: "flex", alignItems: "center" }}>
    <MdNotificationsActive size={24} color="#23b266" style={{ marginRight: 10 }} />
    <div>
      <div style={{ fontWeight: "bold", color: "#23b266" }}>새로운 알림 도착</div>
      <div style={{ fontSize: "0.85rem", color: "#555" }}>
        새로운 알림이 {count}개 있습니다.
      </div>
    </div>
  </div>
);

const usePollingNotifications = (setUnreadCount) => {
  const prevCountRef = useRef(Number(localStorage.getItem("prevUnreadCount")) || 0);

  useEffect(() => {
    const interval = setInterval(async () => {
      try {
        const res = await api.get("/api/notifications/unread");
        const count = res.data.length || 0;
        setUnreadCount(count);

        const prevCount = prevCountRef.current;

        // 이전보다 많을 때만 토스트 띄움
        if (count > prevCount) {
          toast(<CustomNotificationToast count={count - prevCount} />, {
            position: "top-right",
            autoClose: 4000,
          });
        }

        // 현재 알림 수 저장
        prevCountRef.current = count;
        localStorage.setItem("prevUnreadCount", count.toString());
      } catch (e) {
        console.error("알림 폴링 실패", e);
      }
    }, 5000);

    return () => clearInterval(interval);
  }, [setUnreadCount]);
};

export default usePollingNotifications;
