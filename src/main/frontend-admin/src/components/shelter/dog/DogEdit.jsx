import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../../../api/shelter/api";  // <--- axios 인스턴스 import
import "../../../styles/shelter/pages/dogEdit.css";

const BACKEND_URL = "http://localhost:8080";

function DogEdit() {
  const { id } = useParams();
  const shelters_id = '1'; // 실제로는 localStorage 등에서 동적으로!
  const navigate = useNavigate();

  const [images, setImages] = useState([]); // 새로 업로드한 파일들
  const [previewUrls, setPreviewUrls] = useState(Array(5).fill(null)); // 새 이미지 미리보기
  const [originUrls, setOriginUrls] = useState([]); // 기존 이미지 URL (string[])
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
  const [loading, setLoading] = useState(true);

  // 기존 데이터 불러오기
  useEffect(() => {
    const fetchDog = async () => {
      setLoading(true);
      try {
        // axios 직접 호출
        const { data } = await api.get(`/api/shelters/${shelters_id}/dogs/${id}`);
        setForm({
          name: data.name || "",
          age: data.age || "",
          gender: data.gender || "",
          weight: data.weight || "",
          personality: data.personality || "",
          isNeutered: data.isNeutered === true ? "true" : "false",
          foundLocation: data.foundLocation || "",
          status: data.status || "AVAILABLE",
        });
        setOriginUrls(data.imagesUrls || []);
      } catch (err) {
        alert("유기견 정보를 불러올 수 없습니다.");
        navigate(-1);
      } finally {
        setLoading(false);
      }
    };
    fetchDog();
    // eslint-disable-next-line
  }, [id, shelters_id]);

  // 새 이미지 추가
  const handleImageChange = (e) => {
    let files = Array.from(e.target.files);
    let newFiles = [...images, ...files];
    if (originUrls.length + newFiles.length > 5) {
      alert("이미지는 최대 5장까지 등록할 수 있습니다.");
      newFiles = newFiles.slice(0, 5 - originUrls.length);
    }
    setImages(newFiles);

    // 새로 올릴 이미지 미리보기
    const preview = Array(5 - originUrls.length).fill(null);
    newFiles.forEach((file, idx) => {
      preview[idx] = URL.createObjectURL(file);
    });
    setPreviewUrls(preview);

    // input 리셋
    e.target.value = "";
  };

  // 기존 이미지 삭제
  const handleOriginDelete = (idx) => {
    setOriginUrls(originUrls.filter((_, i) => i !== idx));
  };

  // 입력값 변경
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  // 제출
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!shelters_id) {
      alert("쉘터 정보가 없습니다. 다시 로그인 해주세요.");
      return;
    }
    if (originUrls.length + images.length === 0) {
      alert("이미지는 1장 이상 첨부해주세요.");
      return;
    }

    const dog = {
      ...form,
      isNeutered: form.isNeutered === "true",
      age: Number(form.age),
      weight: Number(form.weight),
    };
    const formData = new FormData();
    formData.append("dog", JSON.stringify(dog));
    (originUrls || []).forEach(url => formData.append("imagesUrls", url));
    (images || []).forEach(file => formData.append("images", file));

    try {
      await api.put(
        `/api/shelters/${shelters_id}/dogs/${id}`,
        formData,
        {
          headers: { "Content-Type": "multipart/form-data" },
          withCredentials: true,
        }
      );
      alert("수정이 완료되었습니다!");
      navigate(`/shelter/dog/${id}`);
    } catch (err) {
      alert("수정 중 오류가 발생했습니다.");
      console.error(err);
    }
  };

  if (loading) return <div style={{ textAlign: "center", margin: "44px 0" }}>로딩 중...</div>;

  return (
    <form className="dog-detail-card" onSubmit={handleSubmit}>
      <div className="dog-img-row">
        {/* 기존 이미지 먼저 (삭제 가능) */}
        {originUrls.map((url, i) => (
          <div className="dog-img-thumb" key={`origin-${i}`}>
            <img src={BACKEND_URL + url} alt={`origin${i}`} />
            <button
              type="button"
              className="dog-img-del-btn"
              onClick={() => handleOriginDelete(i)}
              tabIndex={-1}
              title="삭제"
            >x</button>
          </div>
        ))}
        {/* 새로 추가될 이미지 미리보기 */}
        {previewUrls.map(
          (url, i) =>
            url && (
              <div className="dog-img-thumb" key={`new-${i}`}>
                <img src={url} alt={`preview${i}`} />
              </div>
            )
        )}
        {/* 5칸 고정 (빈칸 표시) */}
        {Array(5 - (originUrls.length + images.length)).fill(0).map((_, i) => (
          <div className="dog-img-thumb" key={`empty-${i}`} />
        ))}
      </div>
      <div style={{ marginBottom: "16px" }}>
        <input
          type="file"
          name="images"
          accept="image/*"
          multiple
          onChange={handleImageChange}
          style={{ marginTop: 4, marginBottom: 12 }}
          disabled={originUrls.length + images.length >= 5}
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
              <option value="AVAILABLE">예약가능</option>
              <option value="RESERVED">예약완료</option>
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
          수정
        </button>
        <button className="cancel-btn" type="button" onClick={() => navigate(-1)}>
          취소
        </button>
      </div>
    </form>
  );
}

export default DogEdit;
