import 'package:http/http.dart' as http;
import 'dart:convert';
import '../utils/constants.dart';
import 'auth_service.dart';

class NotificationTestService {
  final AuthService _authService = AuthService();

  // 테스트 알림 전송 (백엔드 API 호출)
  Future<bool> sendTestNotification() async {
    try {
      final accessToken = await _authService.getAccessToken();
      
      if (accessToken == null) {
        print('인증 토큰이 없어 테스트 알림을 전송할 수 없습니다.');
        return false;
      }

      // 백엔드의 테스트 알림 API 호출
      final response = await http.post(
        Uri.parse('${AppConstants.baseUrl}/api/notifications/test'),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $accessToken',
        },
        body: jsonEncode({
          'title': '테스트 알림',
          'content': 'FCM 푸시 알림이 정상적으로 작동합니다!',
          'type': 'SYSTEM_NOTICE',
          'referenceType': 'SYSTEM_NOTICE',
          'referenceId': 1,
        }),
      );

      if (response.statusCode == 200) {
        print('테스트 알림 전송 성공');
        return true;
      } else {
        print('테스트 알림 전송 실패: ${response.statusCode}');
        print('응답 내용: ${response.body}');
        return false;
      }
    } catch (e) {
      print('테스트 알림 전송 오류: $e');
      return false;
    }
  }

  // 로컬 테스트 알림 표시
  Future<void> showLocalTestNotification() async {
    // 이 함수는 main.dart의 _showLocalNotification 함수와 유사한 로직을 사용
    // 실제로는 main.dart에서 직접 호출하는 것이 좋습니다.
    print('로컬 테스트 알림 표시');
  }
} 