import api from "./api";

// 입양 신청 목록
export const getAdoptions = (sheltersId) => api.get(`/api/shelters/${sheltersId}/adoptions`);
// 입양 신청 내역 상세
export const getAdoptionDetail = (sheltersId, adoptionsId) =>
  api.get(`/api/shelters/${sheltersId}/adoptions/${adoptionsId}`);
// 입양 신청 승인
export const approveAdoption = (sheltersId, adoptionsId) =>
  api.put(`/api/shelters/${sheltersId}/adoptions/${adoptionsId}/approve`);
// 입양 신청 거절
export const rejectAdoption = (sheltersId, adoptionsId) =>
  api.put(`/api/shelters/${sheltersId}/adoptions/${adoptionsId}/reject`);
