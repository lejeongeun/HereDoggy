import React, { useEffect, useState } from "react";
import { getShelterProfile, editShelterProfile } from "../../api/shelter/shelter";
import styles from '../../styles/shelter/profile/shelterProfile.module.css';
import ShelterProfileEdit from "./ShelterProfileEdit";
import { MdLocationOn, MdEmail, MdPhone, MdCalendarToday, MdHome, MdInfo } from 'react-icons/md';

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
    {/* 상단 요약 카드 */}
    <div className={styles.profileSummaryCard}>
      <div className={styles.profileSummaryImgWrap}>
        {profile?.imageUrls?.length > 0 ? (
          <img
            src={BACKEND_BASE_URL + profile.imageUrls[0]}
            alt="shelter"
            className={styles.profileSummaryImg}
          />
        ) : (
          <div className={styles.profileSummaryImgPlaceholder}><MdHome size={48} color="#b5e4c6"/></div>
        )}
      </div>
      <div className={styles.profileSummaryInfo}>
        <div className={styles.profileSummaryName}>{profile.name}</div>
        <div className={styles.profileSummaryRow}><MdLocationOn className={styles.icon}/>{profile.region || <span className={styles.placeholder}>지역 정보 없음</span>}</div>
        <div className={styles.profileSummaryRow}><MdEmail className={styles.icon}/>{profile.email || <span className={styles.placeholder}>이메일 없음</span>}</div>
        <div className={styles.profileSummaryRow}><MdPhone className={styles.icon}/>{profile.phone || <span className={styles.placeholder}>전화번호 없음</span>}</div>
      </div>
    </div>

    {/* 상세 정보 카드 */}
    <div className={styles.profileDetailCard}>
      <div className={styles.profileDetailRow}><MdHome className={styles.icon}/><span className={styles.profileLabel}>주소</span>{profile.address || <span className={styles.placeholder}>주소 없음</span>}</div>
      <div className={styles.profileDetailRow}><MdInfo className={styles.icon}/><span className={styles.profileLabel}>설명</span>{profile.description || <span className={styles.placeholder}>설명 없음</span>}</div>
      <div className={styles.profileDetailRow}><MdCalendarToday className={styles.icon}/><span className={styles.profileLabel}>가입일</span>{new Date(profile.createdAt).toLocaleDateString()}</div>
    </div>

    {/* 이미지 여러장 있을 때 썸네일 하단에 추가 */}
    {profile?.imageUrls?.length > 1 && (
      <div className={styles.profileImagesSub}>
        {profile.imageUrls.slice(1).map((url, idx) => (
          <img
            key={idx}
            src={BACKEND_BASE_URL + url}
            alt="shelter"
            className={styles.profileImgSub}
          />
        ))}
      </div>
    )}

    {/* 수정 버튼 */}
    {!editMode && (
      <div className={styles.buttonGroup}>
        <button className={styles.button} onClick={() => setEditMode(true)}>수정하기</button>
      </div>
    )}

    {/* 수정 모드 */}
    {editMode && (
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
