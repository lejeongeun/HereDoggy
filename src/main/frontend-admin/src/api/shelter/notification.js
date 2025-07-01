import api from "./api"; // shelter.js랑 똑같이!

export const getNotifications = async () => {
  const response = await api.get("/api/notifications");
  return response.data;
};

export const markNotificationAsRead = async (id) => {
  const response = await api.patch(`/api/notifications/${id}/read`);
  return response.data;
};

export const deleteNotification = async (id) => {
  const response = await api.delete(`/api/notifications/${id}`);
  return response.data;
};
