import React, { useEffect, useState } from 'react';
import '../styles/layouts/login.css';
import axios from 'axios';

function Login() {
const [useremail, setuseremail] = useState('');
const [password, setPassword] = useState('');

const handleSubmit = async (e) => {
    e.preventDefault();
    try {
        const response = await axios.post(
            "http://localhost:8080/api/shelters/login",
            {
                email: useremail,
                password: password
            },
            {
                // 세션/쿠키 인증 방식이면 꼭 필요
                withCredentials: true
            }
        );
        // axios는 자동으로 json 파싱됨
        alert(response.data); // 서버에서 "로그인 성공" 등 메시지 반환

    } catch (error) {
        // 에러 발생시 서버 응답 메시지
        if (error.response) {
            alert(error.response.data); // 서버에서 반환한 에러 메시지
        } else {
            alert("서버 연결 실패");
        }
    }
};

return (
    <div>

        <form className='login-form' onSubmit={handleSubmit}>
            <div>
                <label>아이디</label>
                <input
                    type="text"
                    value={useremail}
                    onChange={(e) => setuseremail(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>비밀번호</label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
            </div>
            <button type="submit">로그인</button>
        </form>
    </div>
    );
}

export default Login;