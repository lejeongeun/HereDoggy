import React, { useState } from 'react';
import axios from 'axios';
import '../styles/signup.css';
import { useNavigate } from 'react-router-dom';

function SignUp() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    email: '',
    password: '',
    passwordCheck: '',
    name: '',
    nickname: '',
    birth: '',
    phone: '',
    zipcode: '',
    address1: '',
    address2: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // 주소검색 함수 추가
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
        setForm(prev => ({
          ...prev,
          zipcode: data.zonecode,
          address1: fullAddr,
        }));
      }
    }).open();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.password !== form.passwordCheck) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }
    try {
      const response = await axios.post(
        "http://localhost:8080/api/auth/signup",
        form  
      );
      alert(response.data.message || "회원가입 성공!");
      navigate("/");  
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message || "회원가입 실패");
      } else {
        alert("서버 연결 실패");
      }
    }
  };

  return (
    <div className="signup-wrapper">
      <form className="signup-form" onSubmit={handleSubmit}>
        <h2 className="signup-title">회원가입</h2>
        <div className="form-group">
          <label htmlFor="email">이메일</label>
          <input id="email" name="email" value={form.email} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="password">비밀번호</label>
          <input id="password" name="password" type="password" value={form.password} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="passwordCheck">비밀번호 확인</label>
          <input id="passwordCheck" name="passwordCheck" type="password" value={form.passwordCheck} onChange={handleChange} required />
          {form.passwordCheck && form.password !== form.passwordCheck && (
            <span style={{color: "red"}}>비밀번호가 일치하지 않습니다.</span>
          )}
        </div>
        <div className="form-group">
          <label htmlFor="name">이름</label>
          <input id="name" name="name" value={form.name} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="nickname">닉네임</label>
          <input id="nickname" name="nickname" value={form.nickname} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="birth">생일</label>
          <input id="birth" name="birth" type="date" value={form.birth} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="phone">전화번호</label>
          <input id="phone" name="phone" value={form.phone} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="zipcode">우편번호</label>
          <div className="zip-row">
            <input
              id="zipcode"
              name="zipcode"
              value={form.zipcode}
              onChange={handleChange}
              required
              placeholder="우편번호 입력"
              readOnly
            />
            <button
              type="button"
              className="address-search-btn"
              onClick={handleAddressSearch}
            >
              주소 검색
            </button>
          </div>
        </div>
        <div className="form-group">
          <label htmlFor="address1">주소</label>
          <input
            id="address1"
            name="address1"
            value={form.address1}
            onChange={handleChange}
            required
            placeholder="주소 입력"
            readOnly
          />
        </div>
        <div className="form-group">
          <label htmlFor="address2">상세주소</label>
          <input
            id="address2"
            name="address2"
            value={form.address2}
            onChange={handleChange}
            placeholder="상세주소 입력"
          />
        </div>
        <button className="signupbtn" type="submit">회원가입</button>
      </form>
    </div>
  );
}

export default SignUp;
