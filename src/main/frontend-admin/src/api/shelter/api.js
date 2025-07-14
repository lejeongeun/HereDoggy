import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true, // 세션 쿠키 전송
});

// 응답 인터셉터 추가: 세션 만료 감지
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;

    if (status === 401 || status === 302) {
      alert("로그인이 만료되었습니다. 다시 로그인 해주세요.");
      window.location.href = "/";
    }

    return Promise.reject(error);
  }
);

export default api;
