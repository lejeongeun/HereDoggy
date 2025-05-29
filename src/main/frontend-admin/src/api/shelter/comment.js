import api from "./api";

// 댓글 작성
export const createComment = (postId, data) => api.post(`/api/notice-posts/${postId}/comments`, data);
// 댓글 리스트
export const getComments = (postId) => api.get(`/api/notice-posts/${postId}/comments`);
// 댓글 수정
export const updateComment = (commentId, data) => api.put(`/api/comments/${commentId}`, data);
// 댓글 삭제
export const deleteComment = (commentId) => api.delete(`/api/comments/${commentId}`);
