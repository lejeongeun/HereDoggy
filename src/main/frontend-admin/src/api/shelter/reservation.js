import api from "./api";

// 예약 목록 조회
export const getReservations = (sheltersId) =>
  api.get(`/api/shelters/${sheltersId}/reservations`);

// 예약 상세 조회
export const getReservationDetail = (sheltersId, reservationId) =>
  api.get(`/api/shelters/${sheltersId}/reservations/${reservationId}`);

// 산책 예약 승인
export const approveReservation = (sheltersId, reservationId) =>
  api.put(`/api/shelters/${sheltersId}/reservations/${reservationId}/approve`);

// 산책 예약 거절
export const rejectReservation = (sheltersId, reservationId) =>
  api.put(`/api/shelters/${sheltersId}/reservations/${reservationId}/reject`);

// 예약 취소
export const approveCancel = (sheltersId, reservationId) =>
  api.put(`/api/shelters/${sheltersId}/reservations/${reservationId}/cancel`);
