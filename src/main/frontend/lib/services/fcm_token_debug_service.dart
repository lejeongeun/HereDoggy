import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'dart:convert';

class FcmTokenDebugService {
  final FirebaseMessaging _messaging = FirebaseMessaging.instance;

  // 현재 FCM 토큰 가져오기
  Future<String?> getCurrentToken() async {
    try {
      String? token = await _messaging.getToken();
      print('현재 FCM 토큰: $token');
      return token;
    } catch (e) {
      print('FCM 토큰 가져오기 오류: $e');
      return null;
    }
  }

  // 토큰 갱신 리스너 설정
  void setupTokenRefreshListener() {
    _messaging.onTokenRefresh.listen((String newToken) {
      print('FCM 토큰 갱신됨: $newToken');
      // 여기서 서버로 새 토큰을 전송할 수 있습니다
    });
  }

  // 알림 권한 상태 확인
  Future<NotificationSettings> getNotificationSettings() async {
    try {
      NotificationSettings settings = await _messaging.getNotificationSettings();
      print('알림 권한 상태: ${settings.authorizationStatus}');
      return settings;
    } catch (e) {
      print('알림 권한 확인 오류: $e');
      rethrow;
    }
  }

  // FCM 토큰 정보를 JSON으로 반환
  Future<Map<String, dynamic>> getTokenInfo() async {
    try {
      String? token = await getCurrentToken();
      NotificationSettings settings = await getNotificationSettings();
      
      return {
        'token': token,
        'authorizationStatus': settings.authorizationStatus.name,
        'alert': settings.alert,
        'badge': settings.badge,
        'sound': settings.sound,
        'timestamp': DateTime.now().toIso8601String(),
      };
    } catch (e) {
      return {
        'error': e.toString(),
        'timestamp': DateTime.now().toIso8601String(),
      };
    }
  }
} 