import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'pages/home/home_page.dart';
import 'pages/auth/login_page.dart';
import 'pages/auth/register_page.dart';
import 'pages/shelter/shelter_list_page.dart';
import 'pages/walk/walk_reservation_page.dart';
import 'pages/community/community_page.dart';
import 'pages/mypage/mypage.dart';
import 'pages/adoption/adoption_page.dart';
import 'pages/notification/notification_page.dart';
import 'utils/theme.dart';
import 'utils/constants.dart';

void main() {
  runApp(const MyApp());
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
      title: 'HereDoggy',
      theme: AppTheme.lightTheme,
      initialRoute: AppConstants.homeRoute,
      routes: {
        AppConstants.homeRoute: (context) => const HomePage(),
        AppConstants.loginRoute: (context) => const LoginPage(),
        AppConstants.registerRoute: (context) => const RegisterPage(),
        AppConstants.shelterListRoute: (context) => const ShelterListPage(),
        AppConstants.walkReservationRoute: (context) => const WalkReservationPage(),
        AppConstants.communityRoute: (context) => const CommunityPage(),
        AppConstants.myPageRoute: (context) => const MyPage(),
        AppConstants.adoptionRoute: (context) => const AdoptionPage(),
        AppConstants.notificationRoute: (context) => const NotificationPage(),
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
