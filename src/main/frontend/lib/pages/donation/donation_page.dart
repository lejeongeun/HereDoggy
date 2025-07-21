import 'package:flutter/material.dart';
import 'toss_payment_page.dart';
import '../../utils/constants.dart';

class DonationPage extends StatelessWidget {
  const DonationPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final green = Color(0xFF22C55E);
    final orange = Color(0xFFF9A825);
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: green,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Navigator.of(context).pop(),
        ),
        centerTitle: true,
        title: const Text(
          '여기보개 후원하기',
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.bold,
            fontSize: 22,
          ),
        ),
      ),
      body: SafeArea(
        top: false,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // 상단 이미지
            SizedBox(
              height: 220,
              child: Image.asset(
                'assets/images/dogImage1.jpeg',
                fit: BoxFit.cover,
                width: double.infinity,
              ),
            ),
            Expanded(
              child: SingleChildScrollView(
                padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const SizedBox(height: 8),
                    Text(
                      '여기보개 후원 안내',
                      style: TextStyle(
                        color: green,
                        fontWeight: FontWeight.bold,
                        fontSize: 28,
                      ),
                    ),
                    const SizedBox(height: 12),
                    Text(
                      '여러분의 소중한 후원은\n유기견들이 더 나은 내일을 꿈꿀 수 있도록\n아래와 같은 곳에 사용됩니다.',
                      style: TextStyle(
                        color: orange,
                        fontSize: 18,
                        fontWeight: FontWeight.w500,
                        height: 1.4,
                      ),
                    ),
                    const SizedBox(height: 22),
                    _donationItem(
                      title: '산책 체험 봉사 프로그램 운영',
                      titleColor: green,
                      desc: '유기견과 산책할 수 있는 체험 기회를 확대하고,\n산책에 필요한 용품과 환경을 지원합니다.',
                    ),
                    const SizedBox(height: 18),
                    _donationItem(
                      title: '입양 활성화 및 지원',
                      titleColor: green,
                      desc: '보호소와의 연계를 통해 더 많은 유기견이\n새로운 가족을 만날 수 있도록 입양 절차와 안내를 제공합니다.',
                    ),
                    const SizedBox(height: 18),
                    _donationItem(
                      title: '실종견 및 유기견 제보 시스템 관리',
                      titleColor: green,
                      desc: '실종견과 유기견의 신속한 제보와 구조가 이루어질 수 있도록\n앱 서비스와 관련 시스템을 운영·관리합니다.',
                    ),
                    const SizedBox(height: 18),
                    _donationItem(
                      title: '보호소 협력 및 서비스 운영',
                      titleColor: green,
                      desc: '보호소와의 협력 활동, 앱 서비스의 안정적인 운영,\n정보 제공 및 커뮤니티 관리에 소중히 쓰입니다.',
                    ),
                  ],
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 0, 20, 24),
              child: Row(
                children: [
                  Expanded(
                    child: OutlinedButton(
                      onPressed: () async {
                        final result = await Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (_) => const TossPaymentPage(),
                          ),
                        );
                        if (result == true) {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text('후원이 완료되었습니다!')),
                          );
                          Future.delayed(const Duration(milliseconds: 500), () {
                            Navigator.of(context).pushNamedAndRemoveUntil(AppConstants.homeRoute, (route) => false);
                          });
                        } else if (result == false) {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text('후원이 실패했습니다.')),
                          );
                        }
                      },
                      style: OutlinedButton.styleFrom(
                        side: BorderSide(color: green, width: 2),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(16),
                        ),
                        padding: const EdgeInsets.symmetric(vertical: 16),
                      ),
                      child: Text(
                        '후원하기',
                        style: TextStyle(
                          color: green,
                          fontWeight: FontWeight.bold,
                          fontSize: 18,
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: OutlinedButton(
                      onPressed: () => Navigator.of(context).pop(),
                      style: OutlinedButton.styleFrom(
                        side: const BorderSide(color: Colors.black, width: 2),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(16),
                        ),
                        padding: const EdgeInsets.symmetric(vertical: 16),
                      ),
                      child: const Text(
                        '나중에',
                        style: TextStyle(
                          color: Colors.black,
                          fontWeight: FontWeight.bold,
                          fontSize: 18,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

Widget _donationItem({
  required String title,
  required Color titleColor,
  required String desc,
}) {
  return Column(
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
      Text(
        title,
        style: TextStyle(
          color: titleColor,
          fontWeight: FontWeight.bold,
          fontSize: 20,
        ),
      ),
      const SizedBox(height: 4),
      Text(
        desc,
        style: const TextStyle(
          color: Colors.black,
          fontSize: 15,
          height: 1.5,
        ),
      ),
    ],
  );
} 