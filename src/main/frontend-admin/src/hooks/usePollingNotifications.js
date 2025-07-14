import { useEffect, useRef } from "react";
import api from "../api/shelter/api";
import { toast } from "react-toastify";
import { MdNotificationsActive } from "react-icons/md";
import React from "react";

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
    const shelterId = localStorage.getItem("shelters_id");
    if (!shelterId) return;

    const interval = setInterval(async () => {
      // 혹시 로그인/로그아웃 상태 변화에 대응
      const shelterId = localStorage.getItem("shelters_id");
      if (!shelterId) return;

      try {
        const res = await api.get("/api/notifications/unread", { withCredentials: true });
        const count = res.data.length || 0;
        setUnreadCount(count);

        const prevCount = prevCountRef.current;
        if (count > prevCount) {
          toast(<CustomNotificationToast count={count - prevCount} />, {
            position: "top-right",
            autoClose: 4000,
          });
        }
        prevCountRef.current = count;
        localStorage.setItem("prevUnreadCount", count.toString());
      } catch (e) {
        if (e?.response?.status === 401) {
          localStorage.removeItem("shelters_id");
          // 콘솔에 아무것도 출력하지 않음
          return;
        }
        console.error("알림 폴링 실패", e); // 401이 아닌 경우만 콘솔에 출력
      }
    }, 5000);
    return () => clearInterval(interval);
  }, [setUnreadCount]);
};


export default usePollingNotifications;
