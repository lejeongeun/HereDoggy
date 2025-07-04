import api from './api';

class AuthService {
  // 로그인
  async login(email, password) {
    try {
      const response = await api.post('/auth/login', {
        email,
        password
      }, {
        params: { web: true } // 웹 로그인 플래그
      });
      return { success: true, message: '로그인 성공' };
    } catch (error) {
      // 401 Unauthorized인 경우 한글 메시지로 반환
      if (error.response?.status === 401) {
        return {
          success: false,
          message: '아이디와 비밀번호를 확인해주세요.'
        };
      }
      return {
        success: false,
        message: error.response?.data?.message || '로그인에 실패했습니다.'
      };
    }
  }

  // 로그아웃
  async logout() {
    try {
      await api.post('/auth/logout', null, {
        params: { web: true } // 웹 로그아웃 플래그
      });
      return { success: true };
    } catch (error) {
      return { success: true };
    }
  }

  // 사용자 프로필 가져오기
  async getProfile() {
    try {
      const response = await api.get('/members/profile');
      return { success: true, data: response.data };
    } catch (error) {
      return { success: false, message: '프로필 조회에 실패했습니다.' };
    }
  }
}

export default new AuthService(); 