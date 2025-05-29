import api from "./api";

export const searchShelters = (params) => api.get("/api/shelters/search", { params });
export const searchSheltersPost = (data) => api.post("/api/shelters/search", data);
