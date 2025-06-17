import api from "./api";

export const createRoute = (sheltersId, data) =>
  api.post(`/api/shelters/${sheltersId}/walk-routes`, data);

export const updateRoute = (sheltersId, routesId, data) =>
  api.put(`/api/shelters/${sheltersId}/walk-routes/${routesId}`, data);

export const fetchWalkRoutes = async (sheltersId) => {
  const res = await api.get(`/api/shelters/${sheltersId}/walk-routes`);
  return res.data;
};

// 삭제 요청 API 함수
export const deleteRoute = (sheltersId, routesId) =>
  api.delete(`/api/shelters/${sheltersId}/walk-routes/${routesId}`);

