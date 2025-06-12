import api from "./api";

// 공지 생성
export const createNotice = async ({ title, content, images }) => {
  const formData = new FormData();

  // 1. info 부분: JSON 문자열로 감싸기
  const info = JSON.stringify({ title, content });
  formData.append("info", info);

  // 2. 이미지가 있다면 추가
  if (images && images.length > 0) {
    images.forEach((img) => {
      formData.append("images", img);
    });
  }

  return api.post("/api/shelters/notice-posts", formData, {
  headers: { "Content-Type": "multipart/form-data" }
});

};
// 공지 리스트
export const getNotices = () => api.get("/api/shelters/notice-posts");
// 공지 상세
export const getNoticeDetail = (postId) => api.get(`/api/shelters/notice-posts/${postId}`);
// 공지 수정
export const updateNotice = async (postId, { title, content, images }) => {
  const formData = new FormData();
  const info = JSON.stringify({ title, content });
  formData.append("info", info);

  if (images && images.length > 0) {
    images.forEach((img) => formData.append("images", img));
  }

  return api.put(`/api/shelters/notice-posts/${postId}`, formData); // 헤더 뺌
};

// 공지 삭제
export const deleteNotice = (postId) => api.delete(`/api/shelters/notice-posts/${postId}`);
