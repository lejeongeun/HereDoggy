import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/notification_model.dart';
import '../utils/constants.dart';
import 'auth_service.dart';

class NotificationService {
  final AuthService _authService = AuthService();
  final String _baseUrl = AppConstants.baseUrl;

  // 알림 목록 조회
  Future<List<NotificationModel>> fetchNotifications() async {
    final accessToken = await _authService.getAccessToken();
    final response = await http.get(
      Uri.parse('$_baseUrl/notifications'),
      headers: {
        'Authorization': 'Bearer $accessToken',
      },
    );
    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((e) => NotificationModel.fromJson(e)).toList();
    } else {
      throw Exception('알림 목록을 불러오지 못했습니다.');
    }
  }

  // 알림 읽음 처리
  Future<void> markAsRead(int id) async {
    final accessToken = await _authService.getAccessToken();
    final response = await http.patch(
      Uri.parse('$_baseUrl/notifications/$id/read'),
      headers: {
        'Authorization': 'Bearer $accessToken',
      },
    );
    if (response.statusCode != 200) {
      throw Exception('알림 읽음 처리에 실패했습니다.');
    }
  }

  // 알림 모두 읽음 처리
  Future<void> markAllAsRead() async {
    final accessToken = await _authService.getAccessToken();
    final response = await http.patch(
      Uri.parse('$_baseUrl/notifications/read-all'),
      headers: {
        'Authorization': 'Bearer $accessToken',
      },
    );
    if (response.statusCode != 200) {
      throw Exception('알림 전체 읽음 처리에 실패했습니다.');
    }
  }

  // 알림 삭제
  Future<void> deleteNotification(int id) async {
    final accessToken = await _authService.getAccessToken();
    final response = await http.delete(
      Uri.parse('$_baseUrl/notifications/$id'),
      headers: {
        'Authorization': 'Bearer $accessToken',
      },
    );
    if (response.statusCode != 200) {
      throw Exception('알림 삭제에 실패했습니다.');
    }
  }
} 