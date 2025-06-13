import api from "./api";

// 보호소 생성 요청
export const requestShelter = (form) =>
  api.post("/api/shelter-request", form);

// 보호소 프로필 조회
export const getShelterProfile = () =>
  api.get("/api/shelters/profile");

// 보호소 탈퇴
export const removeShelter = () =>
  api.delete("/api/shelters/removal");

// 보호소 마이페이지
export const getShelterMypage = () =>
  api.get("/api/shelters/profile");

// 보호소 목록 조회 (검색)
export const searchShelters = (params) =>
  api.get("/api/shelters/search", { params });

// 보호소 공지 등록
export const postNotice = (data) =>
  api.post("/api/shelters/notice-posts", data);

// 보호소 공지 전체 조회
export const getNotices = () =>
  api.get("/api/shelters/notice-posts");

// 공지 상세
export const getNoticeDetail = (postId) =>
  api.get(`/api/shelters/notice-posts/${postId}`);

// 공지 수정
export const updateNotice = (postId, data) =>
  api.put(`/api/shelters/notice-posts/${postId}`, data);

// 공지 삭제
export const deleteNotice = (postId) =>
  api.delete(`/api/shelters/notice-posts/${postId}`);
