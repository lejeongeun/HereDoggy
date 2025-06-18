import api from "./api";

// 산책 경로 생성 (썸네일 URL 포함)
export const createRoute = (sheltersId, data) =>
  api.post(`/api/shelters/${sheltersId}/walk-routes`, data); // Content-Type 자동 설정

// 산책 경로 수정 (썸네일 URL 포함)
export const updateRoute = (sheltersId, routesId, data) =>
  api.put(`/api/shelters/${sheltersId}/walk-routes/${routesId}`, data); // Content-Type 자동 설정

// 산책 경로 목록 조회
export const fetchWalkRoutes = async (sheltersId) => {
  const res = await api.get(`/api/shelters/${sheltersId}/walk-routes`);
  return res.data;
};

// 산책 경로 삭제
export const deleteRoute = (sheltersId, routesId) =>
  api.delete(`/api/shelters/${sheltersId}/walk-routes/${routesId}`);
