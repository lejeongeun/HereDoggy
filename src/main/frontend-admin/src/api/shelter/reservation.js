import api from "./api";

// 예약 목록 조회
export const getReservations = () => api.get("/api/reservations");

// 예약 상세 조회
export const getReservationDetail = (reservationId) =>
  api.get(`/api/reservations/${reservationId}`);

// 산책 예약 승인
export const approveReservation = (reservationId) =>
  api.put(`/api/reservations/${reservationId}/approve`);

// 산책 예약 거절
export const rejectReservation = (reservationId) =>
  api.put(`/api/reservations/${reservationId}/reject`);

// 예약 취소 승인/거절
export const approveCancel = (reservationId) =>
  api.put(`/api/reservations/${reservationId}/cancel`);
