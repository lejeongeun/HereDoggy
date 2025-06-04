import 'package:flutter/material.dart';
import '../../utils/constants.dart';
import '../../components/auth/login_form.dart';

class HomePage extends StatelessWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final Color green = const Color(0xFF4CAF50);
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // 1. 강아지들 간식주기 카드 + 하트
                Row(
                  children: [
                    Expanded(
                      child: Container(
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          border: Border.all(color: Colors.black12),
                          borderRadius: BorderRadius.circular(8),
                          color: Colors.white,
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: const [
                            Text('강아지들 간식주기', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                            SizedBox(height: 4),
                            Text('보호소 유기견들의 간식을 챙겨주세요', style: TextStyle(fontSize: 12, color: Colors.black54)),
                          ],
                        ),
                      ),
                    ),
                    IconButton(
                      icon: const Icon(Icons.favorite_border, size: 32),
                      onPressed: () {
                        Navigator.pushNamed(context, AppConstants.notificationRoute);
                      },
                    ),
                  ],
                ),
                const SizedBox(height: 16),
                // 2. 2x2 카드 (산책 예약, 유기견 입양, 스토어, 빈칸)
                Row(
                  children: [
                    Expanded(
                      child: GestureDetector(
                        onTap: () => Navigator.pushNamed(context, AppConstants.walkReservationRoute),
                        child: _HomeSquareCard(title: '산책 예약'),
                      ),
                    ),
                    const SizedBox(width: 12),
                    Expanded(
                      child: GestureDetector(
                        onTap: () => Navigator.pushNamed(context, AppConstants.adoptionRoute),
                        child: _HomeSquareCard(title: '유기견 입양', subtitle: '입양 신청을 해보세요'),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 12),
                Row(
                  children: [
                    Expanded(
                      child: _HomeSquareCard(title: '스토어', subtitle: '다양한 굿즈를 만나보세요', disabled: true),
                    ),
                    const SizedBox(width: 12),
                    Expanded(
                      child: _HomeSquareCard(disabled: true),
                    ),
                  ],
                ),
                const SizedBox(height: 16),
                // 3. 유기견 보호소 찾기 카드
                Row(
                  children: [
                    Expanded(
                      child: Container(
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          border: Border.all(color: Colors.black12),
                          borderRadius: BorderRadius.circular(8),
                          color: Colors.white,
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: const [
                            Text('유기견 보호소 찾기', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                            SizedBox(height: 4),
                            Text('내 주변 보호소를 찾아봐요', style: TextStyle(fontSize: 12, color: Colors.black54)),
                          ],
                        ),
                      ),
                    ),
                    const SizedBox(width: 8),
                    SizedBox(
                      width: 40,
                      height: 40,
                      child: OutlinedButton(
                        style: OutlinedButton.styleFrom(
                          padding: EdgeInsets.zero,
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                          side: BorderSide(color: green),
                        ),
                        onPressed: () => Navigator.pushNamed(context, AppConstants.shelterListRoute),
                        child: Icon(Icons.arrow_forward, color: green),
                      ),
                    ),
                  ],
                ),
                // 지도 테스트 버튼 추가
                const SizedBox(height: 8),
                Row(
                  children: [
                    OutlinedButton.icon(
                      style: OutlinedButton.styleFrom(
                        foregroundColor: green,
                        side: BorderSide(color: green),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                      ),
                      icon: const Icon(Icons.map_outlined, size: 18),
                      label: const Text('지도 테스트', style: TextStyle(fontWeight: FontWeight.w500)),
                      onPressed: () => Navigator.pushNamed(context, AppConstants.mapTestRoute),
                    ),
                    const SizedBox(width: 8),
                    OutlinedButton.icon(
                      style: OutlinedButton.styleFrom(
                        foregroundColor: green,
                        side: BorderSide(color: green),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                      ),
                      icon: const Icon(Icons.map_outlined, size: 18),
                      label: const Text('지도 테스트2', style: TextStyle(fontWeight: FontWeight.w500)),
                      onPressed: () => Navigator.pushNamed(context, AppConstants.mapTest2Route),
                    ),
                  ],
                ),
                const SizedBox(height: 24),
                // 4. 귀여운 보호소 아이들 제목
                Row(
                  children: const [
                    Text('귀여운 보호소 아이들', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
                    SizedBox(width: 4),
                    Text('🐶', style: TextStyle(fontSize: 16)),
                  ],
                ),
                const SizedBox(height: 12),
                // 5. 강아지 카드 4개 (2x2)
                GridView.count(
                  crossAxisCount: 2,
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  mainAxisSpacing: 12,
                  crossAxisSpacing: 12,
                  childAspectRatio: 0.8,
                  children: const [
                    _DogInfoCard(
                      imageUrl: 'https://images.unsplash.com/photo-1518717758536-85ae29035b6d',
                      name: '꼬미',
                      breed: '치와와',
                      age: '2살',
                      weight: '2kg',
                      found: '강남역 8번출구 앞',
                      shelter: '역삼동물보호소',
                    ),
                    _DogInfoCard(
                      imageUrl: 'https://images.unsplash.com/photo-1558788353-f76d92427f16',
                      name: '봉석이',
                      breed: '믹스견',
                      age: '1살',
                      weight: '3kg',
                      found: '코엑스 근처',
                      shelter: '삼성동물병원',
                    ),
                    _DogInfoCard(
                      imageUrl: 'https://images.unsplash.com/photo-1518715308788-3005759c61d3',
                      name: '가나다',
                      breed: '믹스견',
                      age: '5살',
                      weight: '9kg',
                      found: '역삼동주민센터 근처',
                      shelter: '역삼동물보호소',
                    ),
                    _DogInfoCard(
                      imageUrl: 'https://images.unsplash.com/photo-1507146426996-ef05306b995a',
                      name: '찬이',
                      breed: '믹스견',
                      age: '4살',
                      weight: '7kg',
                      found: '석촌호수 산책로',
                      shelter: '잠실동물보호센터',
                    ),
                  ],
                ),
                const SizedBox(height: 24),
              ],
            ),
          ),
        ),
      ),
      bottomNavigationBar: _HomeBottomNavBar(),
    );
  }
}

// 2x2 카드 위젯
class _HomeSquareCard extends StatelessWidget {
  final String? title;
  final String? subtitle;
  final bool disabled;
  const _HomeSquareCard({this.title, this.subtitle, this.disabled = false});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 80,
      decoration: BoxDecoration(
        color: disabled ? Colors.grey[200] : Colors.white,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: Colors.black12),
      ),
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (title != null)
              Text(title!, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
            if (subtitle != null)
              Padding(
                padding: const EdgeInsets.only(top: 4),
                child: Text(subtitle!, style: const TextStyle(fontSize: 12, color: Colors.black54)),
              ),
          ],
        ),
      ),
    );
  }
}

// 강아지 정보 카드 위젯
class _DogInfoCard extends StatelessWidget {
  final String imageUrl, name, breed, age, weight, found, shelter;
  const _DogInfoCard({
    required this.imageUrl,
    required this.name,
    required this.breed,
    required this.age,
    required this.weight,
    required this.found,
    required this.shelter,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: Colors.black12),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          ClipRRect(
            borderRadius: const BorderRadius.vertical(top: Radius.circular(16)),
            child: Image.network(
              imageUrl,
              height: 90,
              width: double.infinity,
              fit: BoxFit.cover,
              errorBuilder: (context, error, stackTrace) => Container(
                height: 90,
                color: Colors.grey[300],
                child: const Center(child: Icon(Icons.pets)),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(8),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('$name | 개($breed)', style: const TextStyle(fontWeight: FontWeight.bold)),
                Text('$age / ${weight}', style: const TextStyle(fontSize: 12)),
                const SizedBox(height: 2),
                Text('발견장소 $found', style: const TextStyle(fontSize: 11, color: Colors.black54)),
                Text('보호소 $shelter', style: const TextStyle(fontSize: 11, color: Colors.black54)),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

// 하단 네비게이션 바
class _HomeBottomNavBar extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final Color green = const Color(0xFF4CAF50);
    return BottomNavigationBar(
      type: BottomNavigationBarType.fixed,
      selectedItemColor: green,
      unselectedItemColor: Colors.black,
      showSelectedLabels: true,
      showUnselectedLabels: true,
      items: const [
        BottomNavigationBarItem(
          icon: Icon(Icons.home),
          label: '홈',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.pets),
          label: '산책예약',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.forum),
          label: '커뮤니티',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.account_circle),
          label: 'MY',
        ),
      ],
      onTap: (index) {
        switch (index) {
          case 0:
            Navigator.pushReplacementNamed(context, AppConstants.homeRoute);
            break;
          case 1:
            Navigator.pushNamed(context, AppConstants.walkReservationRoute);
            break;
          case 2:
            Navigator.pushReplacementNamed(context, AppConstants.communityRoute);
            break;
          case 3:
            Navigator.pushReplacementNamed(context, AppConstants.myPageRoute);
            break;
        }
      },
      currentIndex: 0,
    );
  }
} 