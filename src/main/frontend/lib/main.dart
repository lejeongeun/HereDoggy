import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'dart:convert';
import 'pages/home/home_page.dart';
import 'pages/shelter/shelter_list_page.dart';
import 'pages/walk/walk_reservation_page.dart';
import 'pages/community/community_page.dart';
import 'pages/mypage/mypage.dart';
import 'pages/adoption/adoption_page.dart';
import 'pages/notification/notification_page.dart';
import 'pages/community/free_post_write_page.dart';
import 'pages/community/free_post_detail_page.dart';
import 'pages/community/free_post_edit_page.dart';
import 'pages/walk/dog_detail_page.dart';
import 'pages/missing/missing_post_write_page.dart';
import 'pages/missing/missing_post_detail_page.dart';
import 'pages/community/review_post_write_page.dart';
import 'pages/community/review_post_detail_page.dart';
import 'pages/community/review_post_edit_page.dart';
import 'pages/debug/fcm_token_debug_page.dart';
import 'pages/debug/sse_debug_page.dart';
import 'utils/theme.dart';
import 'utils/constants.dart';
import 'providers/user_provider.dart';
import 'providers/dog_provider.dart';
import 'providers/notification_provider.dart';
import 'services/fcm_service.dart';
import 'package:kakao_flutter_sdk_user/kakao_flutter_sdk_user.dart';
import 'package:google_sign_in/google_sign_in.dart';

// 백그라운드 메시지 핸들러
@pragma('vm:entry-point')
Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  await Firebase.initializeApp();
  print('백그라운드 메시지 처리: ${message.messageId}');
}

// 로컬 알림 플러그인 초기화
final FlutterLocalNotificationsPlugin flutterLocalNotificationsPlugin =
    FlutterLocalNotificationsPlugin();

// FCM 서비스 인스턴스
final FcmService fcmService = FcmService();

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // Firebase 초기화
  await Firebase.initializeApp();
  
  // 백그라운드 메시지 핸들러 등록
  FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
  
  // 로컬 알림 초기화
  await _initializeLocalNotifications();
  
  // 카카오 SDK 초기화
  KakaoSdk.init(nativeAppKey: '3f758e2c33c5d676feaae7f717f42d2c');
  
  // 구글 로그인 초기화
  final GoogleSignIn googleSignIn = GoogleSignIn(
    scopes: ['profile', 'email'],
    serverClientId: '728754467180-df7u27sojli1h5g5ofdlrpb9lqco96lq.apps.googleusercontent.com',
  );
  
  // 이전 로그인 세션 정리
  await googleSignIn.signOut();
  
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => UserProvider()),
        ChangeNotifierProvider(create: (_) => DogProvider()),
        ChangeNotifierProvider(create: (_) => NotificationProvider()),
      ],
      child: const MyApp(),
    ),
  );
}

// 로컬 알림 초기화 함수
Future<void> _initializeLocalNotifications() async {
  const AndroidInitializationSettings initializationSettingsAndroid =
      AndroidInitializationSettings('@mipmap/ic_launcher');
  
  const DarwinInitializationSettings initializationSettingsIOS =
      DarwinInitializationSettings(
    requestAlertPermission: true,
    requestBadgePermission: true,
    requestSoundPermission: true,
  );
  
  const InitializationSettings initializationSettings =
      InitializationSettings(
    android: initializationSettingsAndroid,
    iOS: initializationSettingsIOS,
  );
  
  await flutterLocalNotificationsPlugin.initialize(
    initializationSettings,
    onDidReceiveNotificationResponse: (NotificationResponse response) {
      // 알림 탭 시 처리
      _handleNotificationTap(response);
    },
  );
}

// 알림 탭 처리 함수
void _handleNotificationTap(NotificationResponse response) {
  if (response.payload != null) {
    final data = jsonDecode(response.payload!);
    final referenceType = data['referenceType'];
    final referenceId = data['referenceId'];
    
    // 알림 타입에 따른 화면 이동 처리
    // TODO: 네비게이션 처리 로직 추가
    print('알림 탭됨: $referenceType, $referenceId');
  }
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    _initializeFirebaseMessaging();
    _initializeSseConnection();
  }

  // Firebase Messaging 초기화
  Future<void> _initializeFirebaseMessaging() async {
    // 권한 요청
    NotificationSettings settings = await fcmService.requestPermission();
    print('사용자 권한 상태: ${settings.authorizationStatus}');
    
    // FCM 토큰 가져오기
    String? token = await fcmService.getToken();
    if (token != null) {
      print('FCM 토큰: $token');
      await fcmService.sendTokenToServer(token);
    }
    
    // 토큰 갱신 리스너
    fcmService.onTokenRefresh((String newToken) {
      print('FCM 토큰 갱신: $newToken');
      fcmService.sendTokenToServer(newToken);
    });
    
    // 포그라운드 메시지 리스너
    fcmService.onMessage((RemoteMessage message) {
      print('포그라운드 메시지 수신: ${message.messageId}');
      _showLocalNotification(message);
    });
    
    // 백그라운드에서 알림 탭 시 처리
    fcmService.onMessageOpenedApp((RemoteMessage message) {
      print('백그라운드 알림 탭: ${message.messageId}');
      _handleNotificationData(message.data);
    });
    
    // 앱이 종료된 상태에서 알림 탭 시 처리
    RemoteMessage? initialMessage = await fcmService.getInitialMessage();
    if (initialMessage != null) {
      print('초기 메시지: ${initialMessage.messageId}');
      _handleNotificationData(initialMessage.data);
    }
  }
  
  // 로컬 알림 표시
  Future<void> _showLocalNotification(RemoteMessage message) async {
    const AndroidNotificationDetails androidPlatformChannelSpecifics =
        AndroidNotificationDetails(
      'here_doggy_channel',
      'HereDoggy 알림',
      channelDescription: 'HereDoggy 앱의 푸시 알림',
      importance: Importance.max,
      priority: Priority.high,
      showWhen: true,
      icon: 'notification_icon',
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
    
    // 알림 데이터 준비
    final data = {
      'referenceType': message.data['referenceType'],
      'referenceId': message.data['referenceId'],
    };
    
    await flutterLocalNotificationsPlugin.show(
      message.hashCode,
      message.notification?.title ?? '알림',
      message.notification?.body ?? '',
      platformChannelSpecifics,
      payload: jsonEncode(data),
    );
  }
  
  // 알림 데이터 처리
  void _handleNotificationData(Map<String, dynamic> data) {
    final referenceType = data['referenceType'];
    final referenceId = data['referenceId'];
    
    // 알림 타입에 따른 화면 이동 처리
    // TODO: 네비게이션 처리 로직 추가
    print('알림 데이터 처리: $referenceType, $referenceId');
  }

  // SSE 연결 초기화
  Future<void> _initializeSseConnection() async {
    // Provider가 초기화된 후 SSE 연결 시작
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final notificationProvider = Provider.of<NotificationProvider>(context, listen: false);
      notificationProvider.connectSse();
    });
  }

  // API 테스트용 메서드
  Future<String> fetchMessage() async {
    final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/hello'));
    if (response.statusCode == 200) {
      return response.body;
    } else {
      throw Exception('Failed to load message');
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'HereDoggy',
      theme: AppTheme.lightTheme,
      initialRoute: AppConstants.homeRoute,
      routes: {
        AppConstants.homeRoute: (context) => const HomePage(),
        AppConstants.shelterListRoute: (context) => const ShelterListPage(),
        AppConstants.walkReservationRoute: (context) => const WalkReservationPage(),
        AppConstants.communityRoute: (context) => const CommunityPage(),
        AppConstants.myPageRoute: (context) => const MyPage(),
        AppConstants.adoptionRoute: (context) => const AdoptionPage(),
        '/free-post-write': (context) => const FreePostWritePage(),
        '/missing-post-write': (context) => const MissingPostWritePage(),
        '/review-post-write': (context) => const ReviewPostWritePage(),
      },
      onGenerateRoute: (settings) {
        if (settings.name == '/dog-detail') {
          final args = settings.arguments as Map<String, dynamic>?;
          final dogIdRaw = args?['dogId'];
          final dogId = dogIdRaw is int ? dogIdRaw : int.parse(dogIdRaw.toString());
          if (dogId != null) {
            return MaterialPageRoute(
              builder: (context) => DogDetailPage(dogId: dogId),
            );
          }
        }
        if (settings.name != null && settings.name!.startsWith('/free-post-detail/')) {
          final idStr = settings.name!.split('/').last;
          final postId = int.tryParse(idStr);
          if (postId != null) {
            return MaterialPageRoute(
              builder: (context) => FreePostDetailPage(postId: postId),
            );
          }
        }
        if (settings.name != null && settings.name!.startsWith('/free-post-edit/')) {
          final idStr = settings.name!.split('/').last;
          final postId = int.tryParse(idStr);
          if (postId != null) {
            final args = settings.arguments as Map<String, dynamic>?;
            final title = args?['title'] as String? ?? '';
            final content = args?['content'] as String? ?? '';
            return MaterialPageRoute(
              builder: (context) => FreePostEditPage(
                postId: postId,
                initialTitle: title,
                initialContent: content,
              ),
            );
          }
        }
        if (settings.name != null && settings.name!.startsWith('/missing-post-detail/')) {
          final idStr = settings.name!.split('/').last;
          final postId = int.tryParse(idStr);
          if (postId != null) {
            return MaterialPageRoute(
              builder: (context) => MissingPostDetailPage(postId: postId),
            );
          }
        }
        if (settings.name != null && settings.name!.startsWith('/review-post-detail/')) {
          final idStr = settings.name!.split('/').last;
          final postId = int.tryParse(idStr);
          if (postId != null) {
            return MaterialPageRoute(
              builder: (context) => ReviewPostDetailPage(postId: postId),
            );
          }
        }
        if (settings.name != null && settings.name!.startsWith('/review-post-edit/')) {
          final idStr = settings.name!.split('/').last;
          final postId = int.tryParse(idStr);
          if (postId != null) {
            final args = settings.arguments as Map<String, dynamic>?;
            final title = args?['title'] as String? ?? '';
            final content = args?['content'] as String? ?? '';
            final type = args?['type'] as String? ?? 'WALK';
            final rank = args?['rank'] as int? ?? 5;
            return MaterialPageRoute(
              builder: (context) => ReviewPostEditPage(
                postId: postId,
                initialTitle: title,
                initialContent: content,
                initialType: type,
                initialRank: rank,
              ),
            );
          }
        }
        if (settings.name == '/fcm-token-debug') {
          return MaterialPageRoute(
            builder: (context) => const FcmTokenDebugPage(),
          );
        }
        if (settings.name == '/sse-debug') {
          return MaterialPageRoute(
            builder: (context) => const SseDebugPage(),
          );
        }
        return null;
      },
      // API 테스트용 홈 화면
      /*
      home: Scaffold(
        appBar: AppBar(title: Text('Spring Boot + Flutter')),
        body: Center(
          child: FutureBuilder<String>(
            future: fetchMessage(),
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return CircularProgressIndicator();
              } else if (snapshot.hasError) {
                return Text('❌ 오류: ${snapshot.error}');
              } else {
                return Text('✅ 응답: ${snapshot.data}');
              }
            },
          ),
        ),
      ),
      */
    );
  }
}
