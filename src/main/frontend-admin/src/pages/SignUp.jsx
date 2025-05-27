import React, { useState } from 'react';
import '../styles/pages/signup.css';

function SignUp() {
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

  const handleSubmit = (e) => {
    e.preventDefault();
    // 비밀번호 확인
    if (form.password !== form.passwordCheck) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }
    // 실제 회원가입 API 요청 부분 작성 필요
    alert('회원가입 요청: ' + JSON.stringify(form, null, 2));
  };

  return (
    <div className="signup-wrapper">
  <h2>회원가입</h2>
  <form onSubmit={handleSubmit}>
    <div className="form-group">
      <label>이메일</label>
      <input name="email" value={form.email} onChange={handleChange} required />
    </div>
    <div className="form-group">
      <label>비밀번호</label>
      <input name="password" type="password" value={form.password} onChange={handleChange} required />
    </div>
    <div className="form-group">
      <label>비밀번호 확인</label>
      <input name="passwordCheck" type="password" value={form.passwordCheck} onChange={handleChange} required />
    </div>
    <div className="form-group">
      <label>이름</label>
      <input name="name" value={form.name} onChange={handleChange} required />
    </div>
    <div className="form-group">
      <label>닉네임</label>
      <input name="nickname" value={form.nickname} onChange={handleChange} required />
    </div>
    <div className="form-group">
      <label>생일</label>
      <input name="birth" type="date" value={form.birth} onChange={handleChange} required />
    </div>
    <div className="form-group">
      <label>전화번호</label>
      <input name="phone" value={form.phone} onChange={handleChange} required />
    </div>
    <div className="form-group">
      <label>우편번호</label>
      <input name="zipcode" value={form.zipcode} onChange={handleChange} required />
    </div>
    <div className="form-group">
      <label>주소</label>
      <input name="address1" value={form.address1} onChange={handleChange} required />
    </div>
    <div className="form-group">
      <label>상세 주소</label>
      <input name="address2" value={form.address2} onChange={handleChange} required />
    </div>
    <button className='signupbtn' type="submit">회원가입</button>
  </form>
</div>

  );
}

export default SignUp;
