import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import '../styles/login.css';
import axios from 'axios';
import { Link } from 'react-router-dom';

function Login() {
const navigate = useNavigate();
const [useremail, setuseremail] = useState('');
const [password, setPassword] = useState('');

const handleSubmit = async (e) => {
    e.preventDefault();
    try {
        const response = await axios.post(
            "http://localhost:8080/api/auth/login",
            {
                email: useremail,
                password: password
            },
        );
        // axios는 자동으로 json 파싱됨

        // 현재는 토큰을 로컬스토리지에 저장 : 외부 공격에 취약함 -> 쿠키에 저장해야함
        localStorage.setItem('accessToken', response.data.accessToken); //
        localStorage.setItem('userRole', response.data.role);
        alert(response.data.message || "로그인 성공");// 서버에서 "로그인 성공" 등 메시지 반환
        navigate('/shelter/register');
        // 권한에 따라 라우팅
        // if (response.data.role === 'ADMIN') {
        // navigate('/admin/home');
        // } else if (response.data.role === 'SHELTER') {
        // navigate('/shelter');
        // } else {
        // navigate('/');
        // }

    } catch (error) {
        // 에러 발생시 서버 응답 메시지
        if (error.response) {
             alert("로그인 실패"); // 서버에서 반환한 에러 메시지
        } else {
            alert("서버 연결 실패");
        }
    }
};

return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2 className="login-title">어서오개</h2>
        <div className="form-group">
          <label>아이디</label>
          <input
            type="text"
            value={useremail}
            onChange={(e) => setuseremail(e.target.value)}
            required
            placeholder="이메일 입력"
          />
        </div>
        <div className="form-group">
          <label>비밀번호</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            placeholder="비밀번호 입력"
          />
        </div>
        <button type="submit" className="login-btn">로그인</button>
        <Link to="/signup">
          <button type="button" className="signup-btn">회원가입</button>
        </Link>
      </form>
    </div>
    );
}

export default Login;