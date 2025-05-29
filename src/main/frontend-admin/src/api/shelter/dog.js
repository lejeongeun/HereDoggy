import api from "./api";

// 유기견 등록
export const registerDog = (sheltersId, dogData, images) => {
  const formData = new FormData();
  formData.append("dog", JSON.stringify(dogData));
  images.forEach(img => formData.append("images", img));
  return api.post(`/api/shelters/${sheltersId}/dogs`, formData, {
    headers: { "Content-Type": "multipart/form-data" }
  });
};

// 전체 유기견 조회
export const getDogs = (sheltersId) =>
  api.get(`/api/shelters/${sheltersId}/dogs`);

// 유기견 상세 조회
export const getDogDetail = (sheltersId, dogId) =>
  api.get(`/api/shelters/${sheltersId}/dogs/${dogId}`);

// 유기견 정보 수정
export const updateDog = (sheltersId, dogId, dogData, images) => {
  const formData = new FormData();
  formData.append("dog", JSON.stringify(dogData));
  images.forEach(img => formData.append("images", img));
  return api.put(`/api/shelters/${sheltersId}/dogs/${dogId}`, formData, {
    headers: { "Content-Type": "multipart/form-data" }
  });
};

// 유기견 삭제
export const deleteDog = (sheltersId, dogId) =>
  api.delete(`/api/shelters/${sheltersId}/dogs/${dogId}`);
