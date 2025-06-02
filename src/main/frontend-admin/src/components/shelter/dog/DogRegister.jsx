import { useState } from "react";
import '../../../styles/shelter/pages/dogRegister.css';
import api from "../../../api/shelter/api";
import getDogs from "../../../api/shelter/dog";
import { useNavigate } from "react-router-dom";

function DogRegister() {
  // localStorage에서 sheltersId 동적으로 가져오기
  const sheltersId = localStorage.getItem("shelters_id");

  const [images, setImages] = useState([]);
  const [previewUrls, setPreviewUrls] = useState(Array(5).fill(null));
  const navigate = useNavigate();
  const [form, setForm] = useState({
    name: "",
    age: "",
    gender: "",
    weight: "",
    personality: "",
    isNeutered: "",
    foundLocation: "",
    status: "",
  });

  // 이미지 최대 5장, 미리보기, 5칸 고정
  const handleImageChange = (e) => {
    let files = Array.from(e.target.files);

    let newFiles = [...images, ...files];
    if (newFiles.length > 5) {
      alert("이미지는 최대 5장까지 등록할 수 있습니다.");
      newFiles = newFiles.slice(0, 5);
    }
    setImages(newFiles);

    const preview = Array(5).fill(null);
    newFiles.forEach((file, idx) => {
      preview[idx] = URL.createObjectURL(file);
    });
    setPreviewUrls(preview);

    e.target.value = "";
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!sheltersId) {
      alert("쉘터 정보가 없습니다. 다시 로그인 해주세요.");
      return;
    }
    const dog = {
      ...form,
      isNeutered: form.isNeutered === "true",
      age: Number(form.age),
      weight: Number(form.weight),
    };

    if (images.length === 0) {
      alert("이미지는 1장 이상 첨부해주세요.");
      return;
    }

    const formData = new FormData();
    formData.append("dog", JSON.stringify(dog));
    images.forEach(file => formData.append("images", file));

    try {
      const res = await api.post(
        `/api/shelters/${sheltersId}/dogs`,
        formData,
        {
          headers: { "Content-Type": "multipart/form-data" },
          withCredentials: true,
        }
      );
      alert("등록이 완료되었습니다!");
      navigate("/shelter/doglist");
    } catch (err) {
      alert("등록 중 오류가 발생했습니다.");
      console.error(err);
    }
  };

  return (
    <form className="dog-detail-card" onSubmit={handleSubmit}>
      <div className="dog-img-row">
        {previewUrls.map((url, i) =>
          url ? (
            <div className="dog-img-thumb" key={i}>
              <img src={url} alt={`preview${i}`} />
            </div>
          ) : (
            <div className="dog-img-thumb" key={i} />
          )
        )}
      </div>
      <div style={{ marginBottom: "16px" }}>
        <input
          type="file"
          name="images"
          accept="image/*"
          multiple
          onChange={handleImageChange}
          style={{ marginTop: 4, marginBottom: 12 }}
        />
        <span style={{ fontSize: "0.96rem", color: "#6a757a" }}>최대 5장</span>
      </div>
      <div className="dog-info-grid">
        <div className="dog-info-col">
          <div className="info-row">
            <label className="label">이름</label>
            <input
              className="value-input"
              name="name"
              value={form.name}
              onChange={handleChange}
              required
            />
          </div>
          <div className="info-row">
            <label className="label">나이</label>
            <input
              className="value-input"
              name="age"
              type="number"
              value={form.age}
              onChange={handleChange}
              min="0"
              required
            />
          </div>
          <div className="info-row">
            <label className="label">성별</label>
            <select
              className="value-input"
              name="gender"
              value={form.gender}
              onChange={handleChange}
              required
            >
              <option value="">선택</option>
              <option value="MALE">수컷</option>
              <option value="FEMALE">암컷</option>
            </select>
          </div>
          <div className="info-row">
            <label className="label">몸무게</label>
            <input
              className="value-input"
              name="weight"
              value={form.weight}
              onChange={handleChange}
              type="number"
              step="0.1"
              min="0"
              required
            />
          </div>
          <div className="info-row">
            <label className="label">중성화여부</label>
            <select
              className="value-input"
              name="isNeutered"
              value={form.isNeutered}
              onChange={handleChange}
              required
            >
              <option value="">선택</option>
              <option value="true">예</option>
              <option value="false">아니오</option>
            </select>
          </div>
        </div>
        <div className="dog-info-col">
          <div className="info-row">
            <label className="label">상태</label>
            <select
              className="value-input"
              name="status"
              value={form.status}
              onChange={handleChange}
              required
            >
              <option value="">선택</option>
              <option value="AVAILABLE">분양대기</option>
              <option value="RESERVED">예약중</option>
              <option value="ADOPTED">입양완료</option>
            </select>
          </div>
          <div className="info-row">
            <label className="label">특이사항</label>
            <input
              className="value-input"
              name="personality"
              value={form.personality}
              onChange={handleChange}
            />
          </div>
          <div className="info-row">
            <label className="label">발견장소</label>
            <input
              className="value-input"
              name="foundLocation"
              value={form.foundLocation}
              onChange={handleChange}
            />
          </div>
        </div>
      </div>
      <div className="dog-btn-row">
        <button className="update-btn" type="submit">
          등록
        </button>
      </div>
    </form>
  );
}

export default DogRegister;
