import 'package:flutter/foundation.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:flutter/material.dart';
import '../models/notification_model.dart';
import '../services/notification_service.dart';
import '../services/sse_service.dart';

// main.dart에서 사용되는 플러그인 인스턴스 import 필요
import '../main.dart';

class NotificationProvider with ChangeNotifier {
  final NotificationService _notificationService = NotificationService();
  final SseService _sseService = SseService();
  List<NotificationModel> _notifications = [];
  bool _isLoading = false;
  String? _error;
  Set<int> _hiddenIds = {}; // 모두 지우기(로컬 숨김)
  bool _isSseConnected = false;

  List<NotificationModel> get notifications =>
      _notifications.where((n) => !_hiddenIds.contains(n.id)).toList();
  bool get isLoading => _isLoading;
  String? get error => _error;
  int get unreadCount => notifications.where((n) => !n.isRead).length;
  bool get isSseConnected => _isSseConnected;

  // SSE 연결 시작
  Future<void> connectSse() async {
    if (_isSseConnected) {
      print('SSE 이미 연결됨');
      return;
    }

    await _sseService.connect(_handleSseNotification);
    _isSseConnected = _sseService.isConnected;
    notifyListeners();
  }

  // SSE 연결 해제
  void disconnectSse() {
    _sseService.disconnect();
    _isSseConnected = false;
    notifyListeners();
  }

  // SSE 재연결
  Future<void> reconnectSse() async {
    await _sseService.reconnect(_handleSseNotification);
    _isSseConnected = _sseService.isConnected;
    notifyListeners();
  }

  // SSE 알림 수신 처리
  void _handleSseNotification(Map<String, dynamic> notificationData) {
    try {
      // 새로운 알림을 목록 맨 앞에 추가
      final newNotification = NotificationModel(
        id: DateTime.now().millisecondsSinceEpoch, // 임시 ID (실제로는 서버에서 받아야 함)
        title: notificationData['title'] ?? '',
        content: notificationData['content'] ?? '',
        type: notificationData['type'] ?? '',
        referenceType: notificationData['referenceType'] ?? '',
        referenceId: notificationData['referenceId'] ?? 0,
        isRead: false,
        createdAt: DateTime.now().toIso8601String(),
      );

      _notifications.insert(0, newNotification);
      notifyListeners();

      // 실시간 알림 수신 시 로컬 알림(팝업)도 표시
      _showLocalNotification(newNotification);

      // 실시간 알림 수신 시 알림 목록 새로고침 (최신 데이터 동기화)
      Future.delayed(const Duration(seconds: 1), () {
        fetchNotifications();
      });

      print('SSE 알림 처리 완료: ${newNotification.title}');
    } catch (e) {
      print('SSE 알림 처리 오류: $e');
    }
  }

  // 로컬 알림(팝업) 표시 함수
  Future<void> _showLocalNotification(NotificationModel notification) async {
    const AndroidNotificationDetails androidPlatformChannelSpecifics =
        AndroidNotificationDetails(
      'here_doggy_channel',
      'HereDoggy 알림',
      channelDescription: 'HereDoggy 앱의 푸시 알림',
      importance: Importance.max,
      priority: Priority.high,
      showWhen: true,
      icon: 'notification_icon', // 커스텀 알림 아이콘 설정
    );
    const DarwinNotificationDetails iOSPlatformChannelSpecifics =
        DarwinNotificationDetails(
      presentAlert: true,
      presentBadge: true,
      presentSound: true,
    );
    const NotificationDetails platformChannelSpecifics = NotificationDetails(
      android: androidPlatformChannelSpecifics,
      iOS: iOSPlatformChannelSpecifics,
    );

    final data = {
      'referenceType': notification.referenceType,
      'referenceId': notification.referenceId,
    };

    await flutterLocalNotificationsPlugin.show(
      notification.id,
      notification.title,
      notification.content,
      platformChannelSpecifics,
      payload: data.toString(),
    );
  }

  Future<void> fetchNotifications() async {
    _isLoading = true;
    _error = null;
    notifyListeners();
    try {
      _notifications = await _notificationService.fetchNotifications();
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> markAsRead(int id) async {
    try {
      await _notificationService.markAsRead(id);
      _notifications = _notifications.map((n) =>
        n.id == id ? NotificationModel(
          id: n.id,
          title: n.title,
          content: n.content,
          type: n.type,
          referenceType: n.referenceType,
          referenceId: n.referenceId,
          isRead: true,
          createdAt: n.createdAt,
        ) : n
      ).toList();
      notifyListeners();
    } catch (e) {
      _error = e.toString();
      notifyListeners();
    }
  }

  Future<void> markAllAsRead() async {
    try {
      await _notificationService.markAllAsRead();
      _notifications = _notifications.map((n) =>
        NotificationModel(
          id: n.id,
          title: n.title,
          content: n.content,
          type: n.type,
          referenceType: n.referenceType,
          referenceId: n.referenceId,
          isRead: true,
          createdAt: n.createdAt,
        )
      ).toList();
      notifyListeners();
    } catch (e) {
      _error = e.toString();
      notifyListeners();
    }
  }

  Future<void> deleteNotification(int id) async {
    try {
      await _notificationService.deleteNotification(id);
      _notifications.removeWhere((n) => n.id == id);
      notifyListeners();
    } catch (e) {
      _error = e.toString();
      notifyListeners();
    }
  }

  // 모두 지우기(로컬 숨김)
  void hideAllNotifications() {
    _hiddenIds = _notifications.map((n) => n.id).toSet();
    notifyListeners();
  }

  // 숨김 해제(테스트용)
  void resetHidden() {
    _hiddenIds.clear();
    notifyListeners();
  }

  @override
  void dispose() {
    disconnectSse();
    super.dispose();
  }
} 