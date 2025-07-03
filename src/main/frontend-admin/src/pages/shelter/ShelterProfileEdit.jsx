import React, { useState } from "react";
import styles from '../../styles/shelter/profile/shelterProfileEdit.module.css';

function ShelterProfileEdit({
  form,
  images,
  handleChange,
  handleImageChange,
  onSave,
  onCancel
}) {
  const handleAddressSearch = () => {
    new window.daum.Postcode({
      oncomplete: function (data) {
        let fullAddr = data.address;
        let extraAddr = '';
        if (data.addressType === 'R') {
          if (data.bname !== '') extraAddr += data.bname;
          if (data.buildingName !== '') extraAddr += (extraAddr ? ', ' + data.buildingName : data.buildingName);
          if (extraAddr !== '') fullAddr += ' (' + extraAddr + ')';
        }
        handleChange({ target: { name: "address", value: fullAddr } });
      }
    }).open();
  };

   return (
    <form className={styles.container} onSubmit={e => { e.preventDefault(); onSave(); }}>
      <h2 className={styles.heading}>보호소 프로필 수정</h2>

      {/* 이름 */}
      <div className={styles.profileItem}>
        <div className={styles.profileLabel}>이름</div>
        <input className={styles.input} name="name" value={form.name} onChange={handleChange} />
      </div>

      {/* 이메일 */}
      <div className={styles.profileItem}>
        <div className={styles.profileLabel}>이메일</div>
        <input className={styles.input} name="email" value={form.email} onChange={handleChange} />
      </div>

      {/* 전화번호 */}
      <div className={styles.profileItem}>
        <div className={styles.profileLabel}>전화번호</div>
        <input className={styles.input} name="phone" value={form.phone} onChange={handleChange} />
      </div>

      {/* 주소 + 검색 버튼 */}
      <div className={styles.profileItem}>
        <div className={styles.profileLabel}>주소</div>
        <div style={{ flex: 1, display: 'flex', gap: '8px' }}>
          <input
            className={styles.input}
            name="address"
            value={form.address}
            onChange={handleChange}
            readOnly
            placeholder="주소를 검색해주세요"
          />
          <button type="button" className={styles.button} onClick={handleAddressSearch}>
            주소 검색
          </button>
        </div>
      </div>

      {/* 지역 */}
      <div className={styles.profileItem}>
        <div className={styles.profileLabel}>지역</div>
        <input className={styles.input} name="region" value={form.region} onChange={handleChange} />
      </div>

      {/* 설명 */}
      <div className={styles.profileItem}>
        <div className={styles.profileLabel}>설명</div>
        <textarea
          className={styles.textarea}
          name="description"
          value={form.description}
          onChange={handleChange}
        />
      </div>

      {/* 이미지 첨부 */}
      <div className={styles.profileItem}>
        <div className={styles.profileLabel}>이미지</div>
        <div style={{ flex: 1 }}>
          <input
            type="file"
            accept="image/*"
            multiple
            onChange={handleImageChange}
          />
          {images.length > 0 && (
            <div style={{ marginTop: '6px', color: '#289d54', fontWeight: 600 }}>
              {images.length}장 업로드 예정
            </div>
          )}
        </div>
      </div>

   
      <div className={styles.buttonGroup}>
        <button className={styles.button} type="submit">저장</button>
        <button className={styles.button} type="button" onClick={onCancel}>취소</button>
      </div>
    </form>
  );
}

export default ShelterProfileEdit;
