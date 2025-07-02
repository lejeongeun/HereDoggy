import 'package:http/http.dart' as http;
import 'package:firebase_messaging/firebase_messaging.dart';
import 'dart:convert';
import '../utils/constants.dart';
import 'auth_service.dart';

class FcmService {
  final AuthService _authService = AuthService();
  final FirebaseMessaging _messaging = FirebaseMessaging.instance;

  // FCM 토큰을 백엔드로 전송
  Future<bool> sendTokenToServer(String token) async {
    try {
      final accessToken = await _authService.getAccessToken();
      
      if (accessToken == null) {
        print('인증 토큰이 없어 FCM 토큰 전송을 건너뜁니다.');
        return false;
      }

      final response = await http.post(
        Uri.parse('${AppConstants.baseUrl}/fcm/token'),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $accessToken',
        },
        body: jsonEncode({'token': token}),
      );

      if (response.statusCode == 200) {
        print('FCM 토큰 서버 전송 성공');
        return true;
      } else {
        print('FCM 토큰 서버 전송 실패: ${response.statusCode}');
        print('응답 내용: ${response.body}');
        return false;
      }
    } catch (e) {
      print('FCM 토큰 서버 전송 오류: $e');
      return false;
    }
  }

  // FCM 토큰 삭제
  Future<bool> deleteTokenFromServer(String token) async {
    try {
      final accessToken = await _authService.getAccessToken();
      
      if (accessToken == null) {
        print('인증 토큰이 없어 FCM 토큰 삭제를 건너뜁니다.');
        return false;
      }

      final response = await http.delete(
        Uri.parse('${AppConstants.baseUrl}/fcm?token=$token'),
        headers: {
          'Authorization': 'Bearer $accessToken',
        },
      );

      if (response.statusCode == 200) {
        print('FCM 토큰 서버 삭제 성공');
        return true;
      } else {
        print('FCM 토큰 서버 삭제 실패: ${response.statusCode}');
        return false;
      }
    } catch (e) {
      print('FCM 토큰 서버 삭제 오류: $e');
      return false;
    }
  }

  // FCM 권한 요청
  Future<NotificationSettings> requestPermission() async {
    return await _messaging.requestPermission(
      alert: true,
      announcement: false,
      badge: true,
      carPlay: false,
      criticalAlert: false,
      provisional: false,
      sound: true,
    );
  }

  // FCM 토큰 가져오기
  Future<String?> getToken() async {
    return await _messaging.getToken();
  }

  // 토큰 갱신 리스너 설정
  void onTokenRefresh(Function(String) onTokenRefresh) {
    _messaging.onTokenRefresh.listen(onTokenRefresh);
  }

  // 포그라운드 메시지 리스너 설정
  void onMessage(Function(RemoteMessage) onMessage) {
    FirebaseMessaging.onMessage.listen(onMessage);
  }

  // 백그라운드에서 알림 탭 시 리스너 설정
  void onMessageOpenedApp(Function(RemoteMessage) onMessageOpenedApp) {
    FirebaseMessaging.onMessageOpenedApp.listen(onMessageOpenedApp);
  }

  // 초기 메시지 가져오기 (앱이 종료된 상태에서 알림 탭 시)
  Future<RemoteMessage?> getInitialMessage() async {
    return await _messaging.getInitialMessage();
  }
} 