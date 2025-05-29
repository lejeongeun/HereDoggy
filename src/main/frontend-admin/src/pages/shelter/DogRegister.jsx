import { useState } from "react";
import '../../styles/shelter/pages/dogRegister.css';
import api from "../../api/shelter/api";

function DogRegister() {
  // localStorage에서 shelters_id 동적으로 가져오기
  // const shelters_id = localStorage.getItem('shelters_id'); 실제 로그인 후 저장된 shelter_id 사용 시

  const shelters_id = '1'; // 하드코딩으로 동물등록 테스트 (현재 db에 id : 1 보호소 더미로 넣어둠)

  const [images, setImages] = useState([]);
  const [previewUrls, setPreviewUrls] = useState(Array(5).fill(null)); // 5칸 고정

  const [form, setForm] = useState({
    name: "",
    age: "",
    gender: "",
    weight: "",
    personality: "",
    isNeutered: "",
    foundLocation: "",
    status: "AVAILABLE",
  });

  // 이미지 최대 5장, 미리보기, 5칸 고정
  const handleImageChange = (e) => {
    let files = Array.from(e.target.files);

    // 기존 업로드 + 새 파일
    let newFiles = [...images, ...files];
    if (newFiles.length > 5) {
      alert("이미지는 최대 5장까지 등록할 수 있습니다.");
      newFiles = newFiles.slice(0, 5);
    }
    setImages(newFiles);

    // 5칸 고정 미리보기
    const preview = Array(5).fill(null);
    newFiles.forEach((file, idx) => {
      preview[idx] = URL.createObjectURL(file);
    });
    setPreviewUrls(preview);

    // input 리셋 (같은 파일 다시 첨부 가능하게)
    e.target.value = "";
  };

  // 입력값 변경
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  // ...DogRegister 함수 컴포넌트 내부...

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!shelters_id) {
      alert("쉘터 정보가 없습니다. 다시 로그인 해주세요.");
      return;
    }
    // JSON.stringify로 dog 정보 직렬화
    const dog = {
      ...form,
      isNeutered: form.isNeutered === "true",  // Boolean 처리
      age: Number(form.age),
      weight: Number(form.weight),
    };
    console.log("폼 데이터:", dog);
    console.log("이미지:", images);
    console.log("🐶 sheltersId:", shelters_id);
    if (images.length === 0) {
      alert("이미지는 1장 이상 첨부해주세요.");
      return;
    }

    const formData = new FormData();
    formData.append("dog", JSON.stringify(dog));  // 반드시 JSON string
    images.forEach(file => formData.append("images", file)); // 여러 파일 가능

    try {
      // shelters_id는 실제 값으로 치환 필요
      const res = await api.post(
        `/api/shelters/${shelters_id}/dogs`,
        formData,
        {
          headers: { "Content-Type": "multipart/form-data" },
          withCredentials: true,
        }
      );
      alert("등록이 완료되었습니다!");
      // 등록 후 페이지 이동 등 처리
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
