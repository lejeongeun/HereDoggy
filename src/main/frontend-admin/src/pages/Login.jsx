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

      // 백엔드가 세션 기반이므로 별도의 토큰 저장 X
      alert(response.data || "로그인 성공");

      // 역할에 따라 라우팅 예시 (response.data에 role 정보가 없으면 직접 API 호출 필요)
      // 여기서는 일단 쉘터 관리자 페이지로 이동
      navigate("/shelter/dashboard");
    } catch (error) {
      if (error.response) {
        alert("로그인 실패: " + (error.response.data || error.message));
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
