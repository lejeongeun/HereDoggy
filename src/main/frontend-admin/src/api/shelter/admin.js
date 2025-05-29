import api from "./api";

// 관리자 로그인/로그아웃 (auth.js에도 같이 쓸 수 있음)
export const adminLogin = (email, password) => api.post("/api/admin/login", { email, password });
export const adminLogout = () => api.post("/api/admin/logout");

// 보호소 생성 요청 목록 조회
export const getShelterRequests = () => api.get("/api/admin/shelter-requests");
// 보호소 생성 응답 목록 조회
export const getShelterRequestResponses = () => api.get("/api/admin/shelter-requests/response");
// 보호소 생성 요청 승인/거절
export const approveShelterRequest = (requestId) => api.post(`/api/admin/shelter-requests/${requestId}/approve`);
export const rejectShelterRequest = (requestId) => api.post(`/api/admin/shelter-requests/${requestId}/reject`);

// 통계
export const getAdminStats = () => api.get("/api/admin/stats");

// 회원 목록 조회
export const getAdminMembers = () => api.get("/api/admin/members");
// 회원 정지
export const banMember = (id) => api.put(`/api/admin/members/${id}/ban`);

// 보호소 목록 조회
export const getAdminShelters = () => api.get("/api/admin/shelters");
// 보호소 정지
export const banShelter = (id) => api.put(`/api/admin/shelters/${id}/ban`);

// 입양 목록 조회
export const getAdminAdoptions = () => api.get("/api/admin/adoptions");

// 게시글 조회/삭제
export const getAdminPosts = () => api.get("/api/admin/post");
export const deleteAdminPost = (postId) => api.delete(`/api/admin/post/${postId}`);
