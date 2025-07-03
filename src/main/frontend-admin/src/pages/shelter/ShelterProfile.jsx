import React, { useEffect, useState } from "react";
import { getShelterProfile, editShelterProfile } from "../../api/shelter/shelter";
import styles from '../../styles/shelter/profile/shelterProfile.module.css';
import ShelterProfileEdit from "./ShelterProfileEdit";

const BACKEND_BASE_URL = "http://localhost:8080";

function ShelterProfilePage() {
  const [profile, setProfile] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [form, setForm] = useState({
    name: "",
    email: "",
    phone: "",
    address: "",
    region: "",
    description: "",
  });
  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(true);

  // 1. 보호소 프로필 GET
  useEffect(() => {
    getShelterProfile()
      .then(res => {
        setProfile(res.data);
        setForm({
          name: res.data.name,
          email: res.data.email,
          phone: res.data.phone,
          address: res.data.address,
          region: res.data.region,
          description: res.data.description,
        });
        setLoading(false);
        console.log("profile.imageUrls after fetch:", res.data.imageUrls);
      })
      .catch(() => {
        setLoading(false);
        setProfile(null);
      });
  }, []);
console.log("profile.email:", profile?.email);
  // 2. 수정 폼 입력 핸들러
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // 3. 이미지 추가
  const handleImageChange = (e) => {
    setImages(Array.from(e.target.files));
  };

  // 4. 수정 저장(PUT)
  const handleSave = async () => {
    const formData = new FormData();
    const infoJson = JSON.stringify(form);
    formData.append("info", infoJson);
    images.forEach(file => formData.append("files", file));
    try {
      await editShelterProfile(formData);
      alert("수정 성공!");
      setEditMode(false);
      window.location.reload();
    } catch (e) {
      alert("수정 실패: " + e.response?.data?.message);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (!profile) return <div>프로필 정보가 없습니다.</div>;

 return (
  <div className={styles.container}>
   {profile?.imageUrls?.length > 0 && (
  <div className={styles.profileImages}>
    {profile.imageUrls.map((url, idx) => (
      <img
        key={idx}
        src={BACKEND_BASE_URL + url} // ★ 반드시 서버 주소를 앞에 붙이기!
        alt="shelter"
        className={styles.profileImg}
      />
    ))}
  </div>
)}

    {!editMode ? (
      <>
        <div className={styles.profileItem}>
          <span className={styles.profileLabel}>이름:</span> {profile.name}
        </div>
        <div className={styles.profileItem}>
          <span className={styles.profileLabel}>이메일:</span> {profile.email}
        </div>
        <div className={styles.profileItem}>
          <span className={styles.profileLabel}>전화번호:</span> {profile.phone}
        </div>
        <div className={styles.profileItem}>
          <span className={styles.profileLabel}>주소:</span> {profile.address}
        </div>
        <div className={styles.profileItem}>
          <span className={styles.profileLabel}>지역:</span> {profile.region}
        </div>
        <div className={styles.profileItem}>
          <span className={styles.profileLabel}>설명:</span> {profile.description}
        </div>
        <div className={styles.profileItem}>
          <span className={styles.profileLabel}>가입일:</span> {new Date(profile.createdAt).toLocaleDateString()}
        </div>
        <div className={styles.buttonGroup}>
          <button className={styles.button} onClick={() => setEditMode(true)}>수정하기</button>
        </div>
      </>
    ) : (
      <ShelterProfileEdit
        form={form}
        images={images}
        handleChange={handleChange}
        handleImageChange={handleImageChange}
        onSave={handleSave}
        onCancel={() => setEditMode(false)}
      />
    )}
  </div>
);
}

export default ShelterProfilePage;
