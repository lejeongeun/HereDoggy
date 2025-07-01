import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/login.css";
import { shelterLogin } from "../api/shelter/auth";
import { Link } from "react-router-dom";
import logoImg from '../assets/Logo.png';

function Login() {
  const navigate = useNavigate();
  const [useremail, setUseremail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await shelterLogin(useremail, password);
      const { nextAction, message, role, shelterId } = response.data;

      alert(message || "로그인 성공");

      // 로그인 성공 후 shelterId 저장
      if (role === "SHELTER_ADMIN") {
        localStorage.setItem("shelters_id", shelterId);
      }

  setTimeout(() => {
  if (role === "SYSTEM_ADMIN") {
    navigate("/admin/dashboard");
  } else if (role === "SHELTER_ADMIN") {
    navigate("/shelter/dashboard");
  } else if (nextAction) {
    navigate(nextAction);
  } else {
    navigate("/");
  }
}, 100);


    } catch (error) {
      if (error.response) {
        const { nextAction, message } = error.response.data || {};
        alert(message || "로그인 실패");
        setTimeout(() => {
          if (nextAction) {
            navigate(nextAction);
          } else {
            navigate("/");
          }
        }, 100);
      } else {
        alert("서버 연결 실패");
      }
    }
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <div style={{ textAlign: "center" }}>
        <img src={logoImg} alt="여기보개 로고" style={{ height: 130, width: 130 }} />
      </div>
        <div className="login-form-group">
          <label>아이디</label>
          <input
            type="text"
            value={useremail}
            onChange={(e) => setUseremail(e.target.value)}
            required
            placeholder="이메일 입력"
          />
        </div>
        <div className="login-form-group">
          <label>비밀번호</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            placeholder="비밀번호 입력"
          />
        </div>
        <button type="submit" className="login-btn">
          로그인
        </button>
        <Link to="/signup">
          <button type="button" className="signup-btn">
            회원가입
          </button>
        </Link>
      </form>
    </div>
  );
}

export default Login;
