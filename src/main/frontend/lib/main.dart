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
import 'utils/theme.dart';
import 'utils/constants.dart';
import 'providers/user_provider.dart';
import 'providers/dog_provider.dart';

void main() {
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
      },
      onGenerateRoute: (settings) {
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
