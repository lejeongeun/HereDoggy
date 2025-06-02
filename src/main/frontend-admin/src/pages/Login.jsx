import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/login.css";
import { shelterLogin } from "../api/shelter/auth";
import { getShelterProfile } from "../api/shelter/shelter";
import { Link } from "react-router-dom";

function Login() {
  const navigate = useNavigate();
  const [useremail, setUseremail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await shelterLogin(useremail, password);
      const { nextAction, message, role } = response.data;

      alert(message || "로그인 성공");

      // 로그인 성공 후 별도 프로필 조회해서 shelters_id 저장
      if (role === "SHELTER_ADMIN") {
        try {
          const shelterRes = await getShelterProfile();
          const shelters_id = shelterRes.data?.id;
          console.log("shelters_id from profile:", shelterRes.data?.id);

          if (shelters_id) {
            localStorage.setItem("shelters_id", shelters_id);
          }
        } catch (err) {
          console.error("보호소 프로필 조회 실패:", err);
          alert("보호소 정보 조회에 실패했습니다.");
          // 필요시 로그아웃 처리나 다른 조치 가능
        }
      }

      setTimeout(() => {
        if (nextAction) {
          navigate(nextAction);
        } else if (role === "SHELTER_ADMIN") {
          navigate("/shelter/dashboard");
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
