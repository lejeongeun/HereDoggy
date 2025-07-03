import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Alert,
  CircularProgress,
  Grid
} from '@mui/material';
import './Signup.css';

const Signup = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    passwordCheck: '',
    name: '',
    nickname: '',
    birth: '',
    phone: '',
    zipcode: '',
    address1: '',
    address2: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    // 기본 유효성 검사
    if (formData.password !== formData.passwordCheck) {
      setError('비밀번호가 일치하지 않습니다.');
      setLoading(false);
      return;
    }

    try {
      const response = await fetch(`${process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api'}/auth/signup`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        alert('회원가입이 완료되었습니다. 로그인해주세요.');
        navigate('/login');
      } else {
        const data = await response.json();
        setError(data.message || '회원가입에 실패했습니다.');
      }
    } catch (err) {
      setError('회원가입 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md" className="signup-container">
      <Paper elevation={3} className="signup-paper">
        <Box className="signup-header">
          <img src="/images/logo.png" alt="여기보개 로고" className="signup-logo" />
          <Typography variant="h4" component="h1" className="signup-title">
            회원가입
          </Typography>
          <Typography variant="body2" color="textSecondary" className="signup-subtitle">
            여기보개에 가입하고 반려동물과 함께하세요
          </Typography>
        </Box>

        {error && (
          <Alert severity="error" className="signup-alert">
            {error}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit} className="signup-form">
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="이메일"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                required
                variant="outlined"
                className="signup-input"
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="닉네임"
                name="nickname"
                value={formData.nickname}
                onChange={handleChange}
                required
                variant="outlined"
                className="signup-input"
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="비밀번호"
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                required
                variant="outlined"
                className="signup-input"
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="비밀번호 확인"
                name="passwordCheck"
                type="password"
                value={formData.passwordCheck}
                onChange={handleChange}
                required
                variant="outlined"
                className="signup-input"
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="이름"
                name="name"
                value={formData.name}
                onChange={handleChange}
                required
                variant="outlined"
                className="signup-input"
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="생년월일"
                name="birth"
                type="date"
                value={formData.birth}
                onChange={handleChange}
                required
                variant="outlined"
                className="signup-input"
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="전화번호"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
                required
                variant="outlined"
                className="signup-input"
              />
            </Grid>
            <Grid item xs={12} sm={4}>
              <TextField
                fullWidth
                label="우편번호"
                name="zipcode"
                value={formData.zipcode}
                onChange={handleChange}
                required
                variant="outlined"
                className="signup-input"
              />
            </Grid>
            <Grid item xs={12} sm={8}>
              <TextField
                fullWidth
                label="기본주소"
                name="address1"
                value={formData.address1}
                onChange={handleChange}
                required
                variant="outlined"
                className="signup-input"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="상세주소"
                name="address2"
                value={formData.address2}
                onChange={handleChange}
                required
                variant="outlined"
                className="signup-input"
              />
            </Grid>
          </Grid>

          <Button
            type="submit"
            fullWidth
            variant="contained"
            disabled={loading}
            className="signup-button"
            sx={{ mt: 3, mb: 2 }}
          >
            {loading ? <CircularProgress size={24} /> : '회원가입'}
          </Button>

          <Box className="signup-links">
            <Button
              variant="text"
              onClick={() => navigate('/login')}
              className="signup-link"
            >
              이미 계정이 있으신가요? 로그인
            </Button>
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default Signup; 