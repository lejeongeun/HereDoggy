class AppConstants {
  // API 관련 상수
  static const String baseUrl = 'http://10.0.2.2:8080/api'; // 에뮬레이터용
  // static const String baseUrl = 'http://192.168.10.128:8080/api'; // 실제 기기용

  
  // 라우트 이름
  static const String homeRoute = '/';
  static const String mypageRoute = '/mypage';
  static const String walkRoute = '/walk';
  static const String shelterRoute = '/shelter';
  static const String communityRoute = '/community';
  static const String notificationRoute = '/notification';
  static const String donationRoute = '/donation';
  static const String adoptionRoute = '/adoption';
  static const String shelterListRoute = '/shelter';
  static const String walkReservationRoute = '/walk';
  static const String myPageRoute = '/mypage';
  static const String chatRoute = '/chat';
  static const String storeRoute = '/store';

  // 로컬 스토리지 키
  static const String tokenKey = 'auth_token';
  static const String userKey = 'user_data';

  // 페이지 타이틀
  static const String appName = 'HereDoggy';
  static const String homeTitle = '홈';
  static const String mypageTitle = '마이페이지';
  static const String walkTitle = '산책';
  static const String shelterTitle = '보호소';
  static const String communityTitle = '커뮤니티';
  static const String notificationTitle = '알림';
  static const String donationTitle = '후원';
  static const String adoptionTitle = '입양';
  static const String chatTitle = '챗봇';
  static const String storeTitle = '스토어';
} 