import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
import '../utils/constants.dart';
import 'auth_service.dart';

class SseService {
  final AuthService _authService = AuthService();
  StreamSubscription<String>? _subscription;
  Timer? _reconnectTimer;
  bool _isConnected = false;
  int _reconnectAttempts = 0;
  static const int _maxReconnectAttempts = 5;
  static const Duration _reconnectDelay = Duration(seconds: 3);

  bool get isConnected => _isConnected;

  // SSE 연결 시작
  Future<void> connect(Function(Map<String, dynamic>) onNotification) async {
    if (_isConnected) {
      print('SSE 이미 연결됨');
      return;
    }

    try {
      final accessToken = await _authService.getAccessToken();
      if (accessToken == null) {
        print('인증 토큰이 없어 SSE 연결을 건너뜁니다.');
        return;
      }

      final url = '${AppConstants.baseUrl}/notifications/subscribe';
      print('SSE 연결 시도: $url');
      print('SSE 토큰: ${accessToken.substring(0, 20)}...');

      final request = http.Request(
        'GET',
        Uri.parse(url),
      );
      request.headers['Authorization'] = 'Bearer $accessToken';
      request.headers['Accept'] = 'text/event-stream';
      request.headers['Cache-Control'] = 'no-cache';
      request.headers['Connection'] = 'keep-alive';

      print('SSE 요청 헤더: ${request.headers}');

      final streamedResponse = await request.send();
      
      print('SSE 응답 상태: ${streamedResponse.statusCode}');
      print('SSE 응답 헤더: ${streamedResponse.headers}');
      
      if (streamedResponse.statusCode == 200) {
        _isConnected = true;
        _reconnectAttempts = 0;
        print('SSE 연결 성공');

        _subscription = streamedResponse.stream
            .transform(utf8.decoder)
            .transform(const LineSplitter())
            .listen(
          (String line) {
            _handleSseMessage(line, onNotification);
          },
          onError: (error) {
            print('SSE 스트림 에러: $error');
            _handleDisconnection(onNotification);
          },
          onDone: () {
            print('SSE 스트림 종료');
            _handleDisconnection(onNotification);
          },
        );
      } else {
        print('SSE 연결 실패: ${streamedResponse.statusCode}');
        print('SSE 응답 내용: ${await streamedResponse.stream.bytesToString()}');
        _scheduleReconnect(onNotification);
      }
    } catch (e) {
      print('SSE 연결 오류: $e');
      _scheduleReconnect(onNotification);
    }
  }

  // SSE 메시지 처리
  void _handleSseMessage(String line, Function(Map<String, dynamic>) onNotification) {
    if (line.startsWith('data: ')) {
      final data = line.substring(6);
      if (data.trim().isNotEmpty) {
        try {
          final jsonData = jsonDecode(data);
          if (jsonData is Map<String, dynamic>) {
            // 알림 메시지인 경우
            if (jsonData.containsKey('title') && jsonData.containsKey('content')) {
              print('SSE 알림 수신: ${jsonData['title']}');
              onNotification(jsonData);
            }
            // 연결 확인 메시지
            else if (jsonData.toString().contains('SSE 연결 완료')) {
              print('SSE 연결 확인됨');
            }
            // 하트비트 메시지
            else if (jsonData.toString().contains('ping')) {
              print('SSE 하트비트 수신');
            }
          }
        } catch (e) {
          print('SSE 메시지 파싱 오류: $e');
        }
      }
    }
  }

  // 연결 해제 처리
  void _handleDisconnection(Function(Map<String, dynamic>) onNotification) {
    _isConnected = false;
    _subscription?.cancel();
    _subscription = null;
    
    if (_reconnectAttempts < _maxReconnectAttempts) {
      _scheduleReconnect(onNotification);
    } else {
      print('SSE 최대 재연결 시도 횟수 초과');
    }
  }

  // 재연결 스케줄링
  void _scheduleReconnect(Function(Map<String, dynamic>) onNotification) {
    _reconnectAttempts++;
    print('SSE 재연결 시도 $_reconnectAttempts/$_maxReconnectAttempts');
    
    _reconnectTimer?.cancel();
    _reconnectTimer = Timer(_reconnectDelay, () {
      connect(onNotification);
    });
  }

  // 연결 해제
  void disconnect() {
    _isConnected = false;
    _subscription?.cancel();
    _subscription = null;
    _reconnectTimer?.cancel();
    _reconnectTimer = null;
    _reconnectAttempts = 0;
    print('SSE 연결 해제');
  }

  // 수동 재연결
  Future<void> reconnect(Function(Map<String, dynamic>) onNotification) async {
    disconnect();
    await Future.delayed(const Duration(seconds: 1));
    await connect(onNotification);
  }
} 