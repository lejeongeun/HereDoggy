import api from './api';

class MemberService {
  // 내 게시글 목록 가져오기
  async getMyPosts() {
    try {
      const response = await api.get('/members/me/posts');
      return { success: true, data: response.data };
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data?.message || '게시글 목록을 불러오지 못했습니다.' 
      };
    }
  }

  // 내 입양 내역 가져오기
  async getMyAdoptions() {
    try {
      const response = await api.get('/members/adoptions');
      return { success: true, data: response.data };
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data?.message || '입양 내역을 불러오지 못했습니다.' 
      };
    }
  }

  // 프로필 수정
  async updateProfile(profileData) {
    try {
      const response = await api.put('/members/edit', profileData);
      return { success: true, data: response.data };
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data?.message || '프로필 수정에 실패했습니다.' 
      };
    }
  }

  // 회원 탈퇴
  async deleteAccount(token) {
    try {
      const response = await api.delete('/members/removal', {
        data: token
      });
      return { success: true, data: response.data };
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data?.message || '회원 탈퇴에 실패했습니다.' 
      };
    }
  }

  // 알림 설정 변경
  async updateNotificationSetting(enabled) {
    try {
      const response = await api.patch(`/members/me/notification-enabled?enabled=${enabled}`);
      return { success: true, data: response.data };
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data?.message || '알림 설정 변경에 실패했습니다.' 
      };
    }
  }

  // 산책 내역 가져오기
  async getMyWalkRecords() {
    try {
      const response = await api.get('/members/walk-records');
      return { success: true, data: response.data };
    } catch (error) {
      return { 
        success: false, 
        message: error.response?.data?.message || '산책 내역을 불러오지 못했습니다.' 
      };
    }
  }
}

export default new MemberService(); 