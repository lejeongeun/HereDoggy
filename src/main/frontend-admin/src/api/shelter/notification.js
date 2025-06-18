import axios from 'axios';

// 전체 알림 목록 조회
export const getNotifications = async () => {
  const response = await axios.get("/api/notifications");
  return response.data;
};

// 개별 알림 읽음 처리
export const markNotificationAsRead = async (id) => {
  const response = await axios.patch(`/api/notifications/${id}/read`);
  return response.data;
};

// 알림 삭제
export const deleteNotification = async (id) => {
  const response = await axios.delete(`/api/notifications/${id}`);
  return response.data;
};
