import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Alert,
  CircularProgress
} from '@mui/material';
import './Login.css';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (!email || !password) {
      setError('이메일과 비밀번호를 모두 입력해주세요.');
      setLoading(false);
      return;
    }

    try {
      const result = await login(email, password);
      if (result.success) {
        navigate('/');
      } else {
        setError(result.message);
      }
    } catch (err) {
      setError('로그인 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="sm" className="login-container">
      <Paper elevation={3} className="login-paper">
        <Box className="login-header">
          <img src="/images/logo.png" alt="여기보개 로고" className="login-logo" />
          <Typography variant="h4" component="h1" className="login-title">
            로그인
          </Typography>
          <Typography variant="body2" color="textSecondary" className="login-subtitle">
            여기보개에 오신 것을 환영합니다
          </Typography>
        </Box>

        {error && (
          <Alert severity="error" className="login-alert">
            {error}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit} className="login-form">
          <TextField
            fullWidth
            label="이메일"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            margin="normal"
            required
            variant="outlined"
            className="login-input"
          />
          
          <TextField
            fullWidth
            label="비밀번호"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            margin="normal"
            required
            variant="outlined"
            className="login-input"
          />

          <Button
            type="submit"
            fullWidth
            variant="contained"
            disabled={loading}
            className="login-button"
            sx={{ mt: 3, mb: 2 }}
          >
            {loading ? <CircularProgress size={24} /> : '로그인'}
          </Button>

          <Box className="login-links">
            <Button
              variant="text"
              onClick={() => navigate('/signup')}
              className="login-link"
            >
              회원가입
            </Button>
            <Button
              variant="text"
              onClick={() => navigate('/find-password')}
              className="login-link"
            >
              비밀번호 찾기
            </Button>
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default Login; 