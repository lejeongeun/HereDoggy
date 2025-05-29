import api from "./api";

// 기본 경로 생성
export const createRoute = (sheltersId, data) =>
  api.post(`/api/shelters/${sheltersId}/routes`, data);
// 경로 컴포넌트 수정
export const updateRoute = (sheltersId, routesId, data) =>
  api.put(`/api/shelters/${sheltersId}/routes/${routesId}`, data);
