import 'package:flutter/material.dart';
import '../../utils/constants.dart';
import '../../components/auth/login_form.dart';
import 'package:provider/provider.dart';
import '../../providers/dog_provider.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final ScrollController _scrollController = ScrollController();

  @override
  void initState() {
    super.initState();
    Future.microtask(() => Provider.of<DogProvider>(context, listen: false).fetchDogs());
    _scrollController.addListener(_onScroll);
  }

  void _onScroll() {
    final provider = Provider.of<DogProvider>(context, listen: false);
    if (!provider.isLoading &&
        provider.displayCount < provider.dogs.length &&
        _scrollController.position.pixels >= _scrollController.position.maxScrollExtent - 100) {
      provider.loadMoreDogs();
    }
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final Color green = const Color(0xFF4CAF50);
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: ListView(
          controller: _scrollController,
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
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
            InkWell(
              borderRadius: BorderRadius.circular(8),
              onTap: () => Navigator.pushNamed(context, AppConstants.shelterListRoute),
              child: Container(
                width: double.infinity,
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
            // 5. 강아지 카드 (무한스크롤)
            Consumer<DogProvider>(
              builder: (context, dogProvider, _) {
                if (dogProvider.isLoading) {
                  return const Center(child: CircularProgressIndicator());
                }
                if (dogProvider.error != null) {
                  return Center(child: Text('에러: \\${dogProvider.error}'));
                }
                if (dogProvider.dogs.isEmpty) {
                  return const Center(child: Text('유기견 정보가 없습니다.'));
                }
                final dogsToShow = dogProvider.dogs.take(dogProvider.displayCount).toList();
                return GridView.builder(
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 2,
                    mainAxisSpacing: 12,
                    crossAxisSpacing: 12,
                    childAspectRatio: 0.8,
                  ),
                  itemCount: dogsToShow.length + ((dogProvider.displayCount < dogProvider.dogs.length) ? 1 : 0),
                  itemBuilder: (context, index) {
                    if (index < dogsToShow.length) {
                      final dog = dogsToShow[index];
                      return _DogInfoCard(
                        imageUrl: dog.imagesUrls.isNotEmpty ? 'http://192.168.10.128:8080${dog.imagesUrls.first}' : '',
                        name: dog.name,
                        breed: '', // 품종 정보가 없으므로 빈 문자열
                        age: '${dog.age}살',
                        weight: '${dog.weight}kg',
                        found: dog.foundLocation,
                        shelter: dog.shelterName,
                      );
                    } else if (dogProvider.displayCount < dogProvider.dogs.length) {
                      return const Center(child: CircularProgressIndicator());
                    } else {
                      return const SizedBox.shrink();
                    }
                  },
                );
              },
            ),
            const SizedBox(height: 24),
          ],
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