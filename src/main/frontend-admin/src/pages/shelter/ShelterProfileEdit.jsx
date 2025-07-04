import React, { useState } from "react";
import styles from '../../styles/shelter/profile/shelterProfileEdit.module.css';
import { MdPerson, MdEmail, MdPhone, MdLocationOn, MdHome, MdInfo, MdImage, MdSave, MdCancel } from 'react-icons/md';

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
    <form className={styles.formContainer} onSubmit={e => { e.preventDefault(); onSave(); }}>
      <div className={styles.formHeader}><MdPerson size={22} style={{marginRight:7, color:'#23b266', verticalAlign:'middle'}}/>보호소 프로필 수정</div>
      <section className={styles.formSection}>
        <div className={styles.formItem}><label className={styles.formLabel}><MdPerson className={styles.icon}/> 이름</label><input className={styles.input} name="name" value={form.name} onChange={handleChange} /></div>
        <div className={styles.formItem}><label className={styles.formLabel}><MdEmail className={styles.icon}/> 이메일</label><input className={styles.input} name="email" value={form.email} onChange={handleChange} /></div>
        <div className={styles.formItem}><label className={styles.formLabel}><MdPhone className={styles.icon}/> 전화번호</label><input className={styles.input} name="phone" value={form.phone} onChange={handleChange} /></div>
        <div className={styles.formItem}><label className={styles.formLabel}><MdLocationOn className={styles.icon}/> 주소</label>
          <div style={{ flex: 1, display: 'flex', gap: '8px' }}>
            <input className={styles.input} name="address" value={form.address} onChange={handleChange} readOnly placeholder="주소를 검색해주세요" />
            <button type="button" className={styles.addressButton} onClick={handleAddressSearch}>주소 검색</button>
          </div>
        </div>
        <div className={styles.formItem}><label className={styles.formLabel}><MdHome className={styles.icon}/> 지역</label><input className={styles.input} name="region" value={form.region} onChange={handleChange} /></div>
        <div className={styles.formItem}><label className={styles.formLabel}><MdInfo className={styles.icon}/> 설명</label><textarea className={styles.textarea} name="description" value={form.description} onChange={handleChange} /></div>
      </section>
      <section className={styles.formSection}>
        <div className={styles.formItem}><label className={styles.formLabel}><MdImage className={styles.icon}/> 이미지</label>
          <input type="file" accept="image/*" multiple onChange={handleImageChange} className={styles.fileInput} />
          {images.length > 0 && (
            <div className={styles.fileInfo}>{images.length}장 업로드 예정</div>
          )}
        </div>
      </section>
      <div className={styles.buttonGroup}>
        <button className={styles.button} type="submit">저장</button>
        <button className={styles.button + ' ' + styles.cancelButton} type="button" onClick={onCancel}>취소</button>
      </div>
    </form>
  );
}

export default ShelterProfileEdit;
