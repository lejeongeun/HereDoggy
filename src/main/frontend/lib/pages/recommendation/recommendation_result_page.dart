import 'package:flutter/material.dart';

class RecommendationResultPage extends StatelessWidget {
  final List<int?> answers;
  
  const RecommendationResultPage({
    Key? key,
    required this.answers,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color(0xFFF8F9FA),
      appBar: AppBar(
        title: const Text(
          '추천 결과',
          style: TextStyle(
            fontWeight: FontWeight.bold,
            color: Colors.black,
          ),
        ),
        backgroundColor: Colors.white,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.black),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // 상단 제목
              Center(
                child: RichText(
                  textAlign: TextAlign.center,
                  text: TextSpan(
                    style: const TextStyle(
                      fontSize: 20,
                      fontWeight: FontWeight.w600,
                      color: Colors.black87,
                    ),
                    children: [
                      const TextSpan(text: ''),
                      WidgetSpan(
                        child: ShaderMask(
                          shaderCallback: (bounds) => LinearGradient(
                            colors: [
                              Color(0xFF667eea),
                              Color(0xFF764ba2),
                            ],
                            begin: Alignment.centerLeft,
                            end: Alignment.centerRight,
                          ).createShader(bounds),
                          child: const Text(
                            '유예성',
                            style: TextStyle(
                              fontSize: 23,
                              fontWeight: FontWeight.bold,
                              color: Colors.white,
                            ),
                          ),
                        ),
                      ),
                      const TextSpan(text: '님에게 가장 어울리는 강아지는...'),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 32),
              
              // 1등 추천 (큰 카드)
              Container(
                width: double.infinity,
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(20),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.08),
                      offset: const Offset(0, 4),
                      blurRadius: 20,
                    ),
                  ],
                ),
                child: Column(
                  children: [
                    // 강아지 이미지
                    Container(
                      width: double.infinity,
                      height: 280,
                      decoration: BoxDecoration(
                        borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
                      ),
                      child: ClipRRect(
                        borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
                        child: Image.asset(
                          'assets/images/recommend1.jpg',
                          width: double.infinity,
                          height: 280,
                          fit: BoxFit.cover,
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(20),
                      child: Column(
                        children: [
                          // 1등 + 왕관 이모지
                          Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              const Text(
                                '1등',
                                style: TextStyle(
                                  fontSize: 30,
                                  fontWeight: FontWeight.bold,
                                  color: Colors.black87,
                                ),
                              ),
                              const SizedBox(width: 8),
                              const Text('👑', style: TextStyle(fontSize: 30)),
                            ],
                          ),
                          const SizedBox(height: 8),
                          // 품종 + 퍼센트
                          RichText(
                            text: TextSpan(
                              style: const TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.w500,
                                color: Colors.black87,
                              ),
                              children: [
                                const TextSpan(text: '치와와 ('),
                                TextSpan(
                                  text: '91%',
                                  style: TextStyle(
                                    color: Color(0xFFE74C3C),
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                const TextSpan(text: ')'),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 32),
              
              // 2-4등 추천 (가로 나열)
              Row(
                children: [
                  // 2등
                  Expanded(
                    child: _buildRankCard(
                      rank: '2등',
                      breed: '진돗개',
                      percentage: '83%',
                    ),
                  ),
                  const SizedBox(width: 12),
                  // 3등
                  Expanded(
                    child: _buildRankCard(
                      rank: '3등',
                      breed: '웰시코기',
                      percentage: '75%',
                    ),
                  ),
                  const SizedBox(width: 12),
                  // 4등
                  Expanded(
                    child: _buildRankCard(
                      rank: '4등',
                      breed: '말티즈',
                      percentage: '62%',
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 32),
            ],
          ),
        ),
      ),
      bottomNavigationBar: _buildBottomNavigationBar(),
    );
  }

  Widget _buildRankCard({
    required String rank,
    required String breed,
    required String percentage,
  }) {
    // 각 순위에 맞는 이미지 파일명 결정
    String imageAsset;
    switch (rank) {
      case '2등':
        imageAsset = 'assets/images/recommend2.jpg';
        break;
      case '3등':
        imageAsset = 'assets/images/recommend3.jpg';
        break;
      case '4등':
        imageAsset = 'assets/images/recommend4.jpg';
        break;
      default:
        imageAsset = 'assets/images/recommend2.jpg';
    }

    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.06),
            offset: const Offset(0, 2),
            blurRadius: 12,
          ),
        ],
      ),
      child: Column(
        children: [
          // 강아지 이미지
          Container(
            width: double.infinity,
            height: 120,
            decoration: BoxDecoration(
              borderRadius: const BorderRadius.vertical(top: Radius.circular(16)),
            ),
            child: ClipRRect(
              borderRadius: const BorderRadius.vertical(top: Radius.circular(16)),
              child: Image.asset(
                imageAsset,
                width: double.infinity,
                height: 120,
                fit: BoxFit.cover,
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              children: [
                Text(
                  rank,
                  style: const TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.bold,
                    color: Colors.black87,
                  ),
                ),
                const SizedBox(height: 4),
                RichText(
                  text: TextSpan(
                    style: const TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w500,
                      color: Colors.black87,
                    ),
                    children: [
                      TextSpan(text: '$breed ('),
                      TextSpan(
                        text: percentage,
                        style: const TextStyle(
                          color: Color(0xFFE74C3C),
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const TextSpan(text: ')'),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildBottomNavigationBar() {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            offset: const Offset(0, -2),
            blurRadius: 12,
          ),
        ],
      ),
      child: SafeArea(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              _buildNavItem(Icons.home, '홈', true),
              _buildNavItem(Icons.pets, '산책예약', false),
              _buildNavItem(Icons.forum, '커뮤니티', false),
              _buildNavItem(Icons.account_circle, 'MY', false),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildNavItem(IconData icon, String label, bool isSelected) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Icon(
          icon,
          color: isSelected ? Color(0xFF4CAF50) : Colors.grey[600],
          size: 24,
        ),
        const SizedBox(height: 4),
        Text(
          label,
          style: TextStyle(
            fontSize: 12,
            color: isSelected ? Color(0xFF4CAF50) : Colors.grey[600],
            fontWeight: isSelected ? FontWeight.w600 : FontWeight.normal,
          ),
        ),
      ],
    );
  }
} 