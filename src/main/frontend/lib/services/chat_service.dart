import 'package:http/http.dart' as http;
import 'dart:convert';
import '../utils/constants.dart';
import 'auth_service.dart';

class ChatService {
  final AuthService _authService = AuthService();
  final String _baseUrl = AppConstants.baseUrl;

  // 챗봇 메시지 전송
  Future<Map<String, dynamic>> sendMessage(String message) async {
    try {
      final accessToken = await _authService.getAccessToken();
      if (accessToken == null) {
        return {
          'success': false,
          'message': '로그인이 필요합니다.',
        };
      }

      final response = await http.post(
        Uri.parse('$_baseUrl/ai/chat'),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $accessToken',
        },
        body: jsonEncode({
          'message': message,
        }),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return {
          'success': true,
          'reply': data['reply'],
        };
      } else if (response.statusCode == 429) {
        return {
          'success': false,
          'message': '일일 사용 한도 초과',
        };
      } else {
        final errorData = jsonDecode(response.body);
        return {
          'success': false,
          'message': errorData['message'] ?? '메시지 전송에 실패했습니다.',
        };
      }
    } catch (e) {
      return {
        'success': false,
        'message': '네트워크 오류가 발생했습니다.',
      };
    }
  }

  // 남은 질문 횟수 조회
  Future<int?> getRemainingCount() async {
    try {
      final accessToken = await _authService.getAccessToken();
      if (accessToken == null) return null;

      final response = await http.get(
        Uri.parse('$_baseUrl/ai/chat/limit'),
        headers: {
          'Authorization': 'Bearer $accessToken',
        },
      );

      if (response.statusCode == 200) {
        return int.parse(response.body);
      }
      return null;
    } catch (e) {
      return null;
    }
  }
} 