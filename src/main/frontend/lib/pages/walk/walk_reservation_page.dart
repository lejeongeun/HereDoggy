import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/user_provider.dart';
import '../../components/auth/login_form.dart';
import '../../utils/constants.dart';
import '../../providers/dog_provider.dart';
import '../../components/common/cards/dog_card.dart';

// 예시: MaterialApp(
//   routes: {
//     '/walk-reservation': (_) => WalkReservationPage(),
//     '/volunteer-reservation': (_) => VolunteerReservationPage(),
//   },
// )
// 위와 같이 main.dart 등에서 라우트 등록 필요

enum ReservationType { walk, volunteer }

class _ReservationDropdown extends StatelessWidget {
  final ReservationType selected;
  final ValueChanged<ReservationType> onChanged;
  const _ReservationDropdown({Key? key, required this.selected, required this.onChanged}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return DropdownButtonHideUnderline(
      child: DropdownButton<ReservationType>(
        value: selected,
        icon: const Icon(Icons.arrow_drop_down, color: Colors.white),
        dropdownColor: AppConstants.brandGreen,
        style: const TextStyle(color: Colors.white, fontSize: 20, fontWeight: FontWeight.bold),
        items: const [
          DropdownMenuItem(
            value: ReservationType.walk,
            child: Text('산책 & 체험 예약', style: TextStyle(color: Colors.white)),
          ),
          DropdownMenuItem(
            value: ReservationType.volunteer,
            child: Text('봉사 예약', style: TextStyle(color: Colors.white)),
          ),
        ],
        onChanged: (value) {
          if (value != null && value != selected) {
            onChanged(value);
          }
        },
      ),
    );
  }
}

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
      backgroundColor: const Color(0xFFF5F6FA),
      appBar: AppBar(
        title: _ReservationDropdown(
          selected: ReservationType.walk,
          onChanged: (type) {
            if (type == ReservationType.volunteer) {
              Navigator.pushReplacementNamed(context, '/volunteer-reservation');
            }
          },
        ),
        backgroundColor: AppConstants.brandGreen,
        elevation: 0,
        foregroundColor: Colors.white,
      ),
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
                  ? '${AppConstants.baseUrl.replaceAll('/api', '')}${dog.imagesUrls.first}'
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