import api from "./api";

// 예약 옵션 등록
export const registerWalkOption = (sheltersId, dogId, data) =>
  api.post(`/api/shelters/${sheltersId}/dogs/${dogId}/walk-options`, data);
// 예약 옵션 수정
export const updateWalkOption = (sheltersId, dogId, optionsId, data) =>
  api.put(`/api/shelters/${sheltersId}/dogs/${dogId}/walk-options/${optionsId}`, data);
// 예약 옵션 삭제
export const deleteWalkOption = (sheltersId, dogId, optionsId) =>
  api.delete(`/api/shelters/${sheltersId}/dogs/${dogId}/walk-options/${optionsId}`);
