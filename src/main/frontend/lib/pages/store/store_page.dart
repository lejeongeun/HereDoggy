import 'package:flutter/material.dart';
import '../../utils/constants.dart';
import '../home/home_page.dart';
import 'dart:async';

class StorePage extends StatefulWidget {
  StorePage({Key? key}) : super(key: key);

  @override
  State<StorePage> createState() => _StorePageState();
}

class _StorePageState extends State<StorePage> {
  final PageController _bannerController = PageController(viewportFraction: 0.94);
  int _currentBanner = 0;
  Timer? _bannerTimer;
  int _selectedCategory = 0;

  final List<Map<String, String>> banners = [
    {
      'image': 'assets/images/store_banner1.png',
      'text': '',
    },
    {
      'image': 'assets/images/store_banner2.png',
      'text': '',
    },
    {
      'image': 'assets/images/store_banner3.png',
      'text': '',
    },
  ];

  final List<String> bannerTexts = [
    '광고 배너 1: 여기보개 스토어 오픈!',
    '광고 배너 2: 신규 회원 할인 이벤트',
    '광고 배너 3: 인기 상품 특가 세일',
  ];

  final List<Map<String, dynamic>> categories = [
    {'icon': Icons.restaurant_outlined, 'label': '사료'},
    {'icon': Icons.fastfood_outlined, 'label': '간식'},
    {'icon': Icons.sports_tennis_outlined, 'label': '장난감'},
    {'icon': Icons.bed_outlined, 'label': '하우스/방석'},
    {'icon': Icons.directions_walk_outlined, 'label': '산책용품'},
    {'icon': Icons.clean_hands_outlined, 'label': '위생/배변'},
    {'icon': Icons.spa_outlined, 'label': '미용/목욕'},
    {'icon': Icons.checkroom_outlined, 'label': '의류/액세서리'},
  ];

  final List<Map<String, dynamic>> products = [
    {
      'name': '프리미엄 강아지 사료',
      'desc': '고단백 프리미엄 사료 2kg',
      'price': '25,000',
      'discount': 18,
      'image': 'assets/images/store_1.jpg',
      'category': '사료',
    },
    {
      'name': '닭가슴살 트릿',
      'desc': '저지방 닭가슴살 트릿 200g',
      'price': '8,900',
      'discount': 25,
      'image': 'assets/images/store_2.png',
      'category': '간식',
    },
    {
      'name': '노즈워크 장난감',
      'desc': '분리불안 해소 노즈워크 퍼즐',
      'price': '13,500',
      'discount': 15,
      'image': 'assets/images/store_3.jpg',
      'category': '장난감',
    },
    {
      'name': '포근한 방석',
      'desc': '극세사 포근한 강아지 방석',
      'price': '19,000',
      'discount': 22,
      'image': 'assets/images/store_4.png',
      'category': '하우스/방석',
    },
    {
      'name': '산책용 하네스',
      'desc': '튼튼한 산책용 하네스',
      'price': '15,000',
      'discount': 30,
      'image': 'assets/images/store_5.jpg',
      'category': '산책용품',
    },
    {
      'name': '배변패드 50매',
      'desc': '흡수력 좋은 대형 배변패드',
      'price': '12,000',
      'discount': 20,
      'image': 'assets/images/store_6.jpg',
      'category': '위생/배변',
    },
    {
      'name': '펫 샴푸',
      'desc': '저자극 펫 샴푸 500ml',
      'price': '9,500',
      'discount': 12,
      'image': 'assets/images/store_7.png',
      'category': '미용/목욕',
    },
    {
      'name': '귀여운 강아지 옷',
      'desc': '여름용 귀여운 강아지 티셔츠',
      'price': '17,000',
      'discount': 28,
      'image': 'assets/images/store_8.jpg',
      'category': '의류/액세서리',
    },
  ];

  @override
  void initState() {
    super.initState();
    _startBannerTimer();
  }

  void _startBannerTimer() {
    _bannerTimer?.cancel();
    _bannerTimer = Timer.periodic(const Duration(seconds: 5), (timer) {
      if (_bannerController.hasClients) {
        int nextPage = (_currentBanner + 1) % banners.length;
        _bannerController.animateToPage(
          nextPage,
          duration: const Duration(milliseconds: 500),
          curve: Curves.ease,
        );
      }
    });
  }

  @override
  void dispose() {
    _bannerTimer?.cancel();
    _bannerController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('스토어', style: TextStyle(color: Colors.black)),
        backgroundColor: Colors.white,
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.shopping_cart, color: Color(0xFF4CAF50)),
            onPressed: () {
              // TODO: 장바구니 페이지로 이동 (추후 구현)
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('장바구니는 추후 지원됩니다.')),
              );
            },
          ),
        ],
        iconTheme: const IconThemeData(color: Colors.black),
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // 광고 배너
            SizedBox(
              height: 300,
              child: Stack(
                alignment: Alignment.bottomCenter,
                children: [
                  PageView.builder(
                    controller: _bannerController,
                    itemCount: banners.length,
                    onPageChanged: (idx) {
                      setState(() {
                        _currentBanner = idx;
                      });
                    },
                    itemBuilder: (context, index) {
                      final banner = banners[index];
                      return Container(
                        margin: EdgeInsets.only(
                          left: 8,
                          right: index == banners.length - 1 ? 8 : 18,
                          bottom: 12,
                        ),
                        child: ClipRRect(
                          borderRadius: BorderRadius.circular(24),
                          child: Stack(
                            fit: StackFit.expand,
                            children: [
                              Image.asset(
                                banner['image']!,
                                fit: BoxFit.cover,
                              ),
                              Center(
                                child: Padding(
                                  padding: const EdgeInsets.symmetric(horizontal: 18.0),
                                  child: Text(
                                    banner['text']!,
                                    textAlign: TextAlign.center,
                                    style: const TextStyle(
                                      color: Colors.white,
                                      fontWeight: FontWeight.bold,
                                      fontSize: 20,
                                      shadows: [Shadow(blurRadius: 4, color: Colors.black26)],
                                    ),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  ),
                  // 페이지네이션 점
                  Positioned(
                    bottom: 10,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: List.generate(banners.length, (idx) {
                        return Container(
                          width: 10,
                          height: 10,
                          margin: const EdgeInsets.symmetric(horizontal: 4),
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            color: _currentBanner == idx
                                ? const Color(0xFF2196F3)
                                : Colors.white.withOpacity(0.7),
                            border: Border.all(color: const Color(0xFF2196F3), width: 1.2),
                          ),
                        );
                      }),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 8),
            // 카테고리 탭 (미니멀)
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 0, vertical: 8),
              child: Column(
                children: [
                  GridView.builder(
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    itemCount: categories.length,
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 4,
                      mainAxisSpacing: 0,
                      crossAxisSpacing: 0,
                      childAspectRatio: 1.1,
                    ),
                    itemBuilder: (context, index) {
                      final cat = categories[index];
                      final bool selected = _selectedCategory == index;
                      return GestureDetector(
                        onTap: () {
                          setState(() {
                            _selectedCategory = index;
                          });
                        },
                        child: Container(
                          margin: const EdgeInsets.symmetric(vertical: 2, horizontal: 2),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Icon(
                                cat['icon'],
                                size: 26,
                                color: selected ? Colors.black87 : const Color(0xFFB0BEC5),
                                weight: 1.5,
                              ),
                              const SizedBox(height: 2),
                              Text(
                                cat['label'],
                                style: TextStyle(
                                  fontSize: 11,
                                  color: selected ? Colors.black87 : const Color(0xFFB0BEC5),
                                  fontWeight: FontWeight.w400,
                                ),
                              ),
                              const SizedBox(height: 4),
                              // 인디케이터
                              AnimatedContainer(
                                duration: const Duration(milliseconds: 200),
                                height: 2,
                                width: selected ? 16 : 0,
                                decoration: BoxDecoration(
                                  color: selected ? Colors.black87 : Colors.transparent,
                                  borderRadius: BorderRadius.circular(2),
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),
            // 상품 리스트
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 6),
              child: GridView.builder(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                itemCount: products.length,
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 2,
                  mainAxisSpacing: 6,
                  crossAxisSpacing: 6,
                  childAspectRatio: 0.72,
                ),
                itemBuilder: (context, index) {
                  final product = products[index];
                  return InkWell(
                    borderRadius: BorderRadius.circular(8),
                    onTap: () {},
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        Expanded(
                          child: ClipRRect(
                            borderRadius: BorderRadius.circular(8),
                            child: product['image'].toString().startsWith('assets/')
                                ? Image.asset(
                                    product['image'],
                                    width: double.infinity,
                                    fit: BoxFit.cover,
                                  )
                                : Image.network(
                                    product['image'],
                                    width: double.infinity,
                                    fit: BoxFit.cover,
                                  ),
                          ),
                        ),
                        const SizedBox(height: 6),
                        Text(
                          '${product['price']}원',
                          style: const TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 14,
                            color: Colors.black,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                        const SizedBox(height: 2),
                        Text(
                          product['desc'],
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                          style: const TextStyle(
                            fontWeight: FontWeight.w400,
                            fontSize: 11,
                            color: Colors.grey,
                          ),
                        ),
                        const SizedBox(height: 4),
                      ],
                    ),
                  );
                },
              ),
            ),
            const SizedBox(height: 24),
          ],
        ),
      ),
      bottomNavigationBar: HomeBottomNavBar(currentIndex: 0),
    );
  }
}