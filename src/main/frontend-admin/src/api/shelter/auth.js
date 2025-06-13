import api from "./api";

// 보호소 관리자 로그인/로그아웃 (웹 세션 방식)
export const shelterLogin = (email, password) =>
  api.post("/api/shelters/login", { email, password }, { withCredentials: true });

export const shelterLogout = () =>
  api.post("/api/shelters/logout", {}, { withCredentials: true });


