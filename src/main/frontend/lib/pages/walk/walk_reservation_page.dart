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
            builder: (_) => const LoginFullScreenPage(),
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
                imageUrl: dog.imagesUrls.isNotEmpty
                  ? 'http://10.0.2.2:8080${dog.imagesUrls.first}'
                  : '',
                name: dog.name,
                age: dog.age,
                gender: dog.gender == 'MALE' ? '수컷' : '암컷',
                weight: dog.weight,
                foundLocation: dog.foundLocation,
                shelterName: dog.shelterName,
                onTap: () {
                  // 상세 페이지로 이동 (dogId를 넘김)
                  Navigator.pushNamed(context, '/dog-detail', arguments: {'dogId': dog.id});
                },
              );
            },
          );
        },
      ),
    );
  }
} 