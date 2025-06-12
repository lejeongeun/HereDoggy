import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';
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
import 'utils/theme.dart';
import 'utils/constants.dart';
import 'providers/user_provider.dart';
import 'providers/dog_provider.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => UserProvider()),
        ChangeNotifierProvider(create: (_) => DogProvider()),
      ],
      child: const MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

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
        AppConstants.notificationRoute: (context) => const NotificationPage(),
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
