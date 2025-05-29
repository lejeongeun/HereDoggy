import api from "./api";

// 공지 생성
export const createNotice = (data) => api.post("/api/shelters/notice-posts", data);
// 공지 리스트
export const getNotices = () => api.get("/api/shelters/notice-posts");
// 공지 상세
export const getNoticeDetail = (postId) => api.get(`/api/shelters/notice-posts/${postId}`);
// 공지 수정
export const updateNotice = (postId, data) => api.put(`/api/shelters/notice-posts/${postId}`, data);
// 공지 삭제
export const deleteNotice = (postId) => api.delete(`/api/shelters/notice-posts/${postId}`);
