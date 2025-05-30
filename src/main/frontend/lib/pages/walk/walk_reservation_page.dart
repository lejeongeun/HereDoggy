import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/user_provider.dart';
import '../../components/auth/login_form.dart';
import '../../utils/constants.dart';
import '../../providers/dog_provider.dart';
import '../../components/common/cards/dog_card.dart';

class WalkReservationPage extends StatefulWidget {
  const WalkReservationPage({Key? key}) : super(key: key);

  @override
  State<WalkReservationPage> createState() => _WalkReservationPageState();
}

class _WalkReservationPageState extends State<WalkReservationPage> {
  bool _loginPagePushed = false;

  @override
  void initState() {
    super.initState();
    // 강아지 목록 불러오기
    Future.microtask(() {
      Provider.of<DogProvider>(context, listen: false).fetchDogs();
    });
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final isLoggedIn = Provider.of<UserProvider>(context, listen: false).isLoggedIn;
    if (!isLoggedIn && !_loginPagePushed) {
      _loginPagePushed = true;
      WidgetsBinding.instance.addPostFrameCallback((_) {
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(
            builder: (_) => LoginFullScreenPage(
              onLoginSuccess: () {
                Navigator.pushReplacementNamed(context, AppConstants.walkReservationRoute);
              },
            ),
          ),
        );
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final isLoggedIn = Provider.of<UserProvider>(context).isLoggedIn;
    if (!isLoggedIn) {
      // 로그인 페이지로 이동 중이므로 빈 컨테이너 반환
      return const SizedBox.shrink();
    }
    return Scaffold(
      appBar: AppBar(title: const Text('산책 예약')),
      body: Consumer<DogProvider>(
        builder: (context, dogProvider, _) {
          if (dogProvider.isLoading) {
            return const Center(child: CircularProgressIndicator());
          }
          if (dogProvider.error != null) {
            return Center(child: Text('에러: ${dogProvider.error}'));
          }
          if (dogProvider.dogs.isEmpty) {
            return const Center(child: Text('유기견 정보가 없습니다.'));
          }
          return ListView.builder(
            padding: const EdgeInsets.symmetric(vertical: 8),
            itemCount: dogProvider.dogs.length,
            itemBuilder: (context, index) {
              final dog = dogProvider.dogs[index];
              return DogCard(
                imageUrl: dog.imagesUrls.isNotEmpty ? dog.imagesUrls.first : '',
                name: dog.name,
                age: dog.age,
                gender: dog.gender == 'MALE' ? '수컷' : '암컷',
                weight: dog.weight,
                foundLocation: dog.foundLocation,
                shelterName: dog.shelterName,
                onTap: () {
                  // 상세 페이지로 이동 (id만 전달)
                  Navigator.pushNamed(context, '/dog-detail', arguments: {'dogId': dog.name}); // TODO: dogId로 변경 필요
                },
              );
            },
          );
        },
      ),
    );
  }
}

class LoginFullScreenPage extends StatelessWidget {
  final VoidCallback? onLoginSuccess;
  const LoginFullScreenPage({Key? key, this.onLoginSuccess}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 24.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const SizedBox(height: 16),
              IconButton(
                icon: const Icon(Icons.arrow_back, size: 32),
                onPressed: () => Navigator.pop(context),
              ),
              const SizedBox(height: 16),
              const Text(
                '로그인',
                style: TextStyle(fontSize: 32, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 32),
              LoginForm(
                onLoginSuccess: () {
                  Navigator.of(context, rootNavigator: true).pop();
                  Navigator.pushNamed(context, AppConstants.walkReservationRoute);
                },
              ),
              const SizedBox(height: 32),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircleAvatar(
                    backgroundColor: Colors.white,
                    radius: 28,
                    child: const Text('Google', style: TextStyle(color: Colors.black)),
                  ),
                  const SizedBox(width: 32),
                  CircleAvatar(
                    backgroundColor: Color(0xFFFFEB3B),
                    radius: 28,
                    child: const Text('Kakao', style: TextStyle(color: Colors.black)),
                  ),
                ],
              ),
              const Spacer(),
              Center(
                child: Padding(
                  padding: const EdgeInsets.only(bottom: 16.0),
                  child: Wrap(
                    alignment: WrapAlignment.center,
                    spacing: 8,
                    children: [
                      TextButton(
                        onPressed: () {},
                        child: const Text('회원가입', style: TextStyle(color: Colors.black54)),
                      ),
                      const Text('|', style: TextStyle(color: Colors.black54)),
                      TextButton(
                        onPressed: () {},
                        child: const Text('아이디 찾기', style: TextStyle(color: Colors.black54)),
                      ),
                      const Text('|', style: TextStyle(color: Colors.black54)),
                      TextButton(
                        onPressed: () {},
                        child: const Text('비밀번호 재설정', style: TextStyle(color: Colors.black54)),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
} 