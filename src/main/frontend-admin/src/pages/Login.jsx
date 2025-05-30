import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/login.css";
import { shelterLogin } from "../api/shelter/auth";
import { Link } from "react-router-dom";

function Login() {
  const navigate = useNavigate();
  const [useremail, setUseremail] = useState("");
  const [password, setPassword] = useState("");

const handleSubmit = async (e) => {
  e.preventDefault();

  try {
    const response = await shelterLogin(useremail, password);

    console.log("로그인 응답:", response.data);

    // role 정보가 응답에 있는지 확인
    const role = response.data.role || "USER"; // role이 없으면 USER로 기본값

    alert(response.data.message || "로그인 성공");
    navigate("/shelter/dashboard");
    // 권한(role)에 따라 분기
    // if (role === "SHELTER_ADMIN") {
    //   navigate("/shelter/dashboard");
    // } else {
    //   navigate("/shelter/register");
    // }
  } catch (error) {
    if (error.response) {
      alert("로그인 실패: " + (error.response.data.message || error.response.data || error.message));
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
            onChange={(e) => setUseremail(e.target.value)}
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
