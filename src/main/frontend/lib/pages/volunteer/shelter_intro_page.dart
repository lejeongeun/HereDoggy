import 'package:flutter/material.dart';
import 'volunteer_time_select_page.dart';

class ShelterIntroPage extends StatelessWidget {
  final Map<String, String> shelter;
  const ShelterIntroPage({Key? key, required this.shelter}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(
        preferredSize: const Size.fromHeight(50),
        child: _buildCustomAppBar(context, shelter['name'] ?? ''),
      ),
      body: Column(
        children: [
          _buildImageGrid(),
          _buildIntroTabIndicator(),
          Expanded(
            child: _buildIntroductionTab(context),
          ),
        ],
      ),
    );
  }

  Widget _buildImageGrid() {
    return Container(
      height: 200,
      padding: const EdgeInsets.all(16),
      child: Row(
        children: [
          Expanded(
            flex: 1,
            child: Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(8),
                image: const DecorationImage(
                  image: AssetImage('assets/images/dogImage1.jpeg'),
                  fit: BoxFit.cover,
                ),
              ),
            ),
          ),
          const SizedBox(width: 8),
          Expanded(
            flex: 1,
            child: Column(
              children: [
                Expanded(
                  child: Container(
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(8),
                      image: const DecorationImage(
                        image: AssetImage('assets/images/dogImage2.jpg'),
                        fit: BoxFit.cover,
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 8),
                Expanded(
                  child: Container(
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(8),
                      image: const DecorationImage(
                        image: AssetImage('assets/images/dogImage3.jpg'),
                        fit: BoxFit.cover,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildIntroTabIndicator() {
    return Column(
      children: [
        Container(
          color: Colors.white,
          padding: const EdgeInsets.only(top: 8, left: 16, right: 16),
          alignment: Alignment.centerLeft,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                '소개',
                style: TextStyle(
                  color: Colors.green,
                  fontSize: 18,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 6),
              Container(
                height: 3,
                width: 32,
                color: Colors.green,
              ),
            ],
          ),
        ),
        const Divider(
          color: Color(0xFFE0E0E0),
          thickness: 1,
          height: 16,
        ),
      ],
    );
  }

  Widget _buildIntroductionTab(BuildContext context) {
    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            '주소',
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            shelter['address'] ?? '-',
            style: const TextStyle(
              fontSize: 16,
              color: Colors.black54,
            ),
          ),
          const SizedBox(height: 20),
          const Text(
            '연락처',
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            shelter['phone'] ?? '-',
            style: const TextStyle(
              fontSize: 16,
              color: Colors.black54,
            ),
          ),
          const SizedBox(height: 20),
          const Text(
            '설명',
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            shelter['description'] ?? '보호소 소개가 없습니다.',
            style: const TextStyle(
              fontSize: 16,
              color: Colors.black54,
            ),
          ),
          const SizedBox(height: 32),
          SizedBox(
            width: double.infinity,
            height: 48,
            child: ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => VolunteerTimeSelectPage(shelter: shelter),
                  ),
                );
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.green[300],
                foregroundColor: Colors.black,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
                textStyle: const TextStyle(fontSize: 18),
              ),
              child: const Text('봉사 신청'),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildCustomAppBar(BuildContext context, String title) {
    return Container(
      height: 80,
      color: const Color(0xFF4CAF50),
      child: SafeArea(
        child: Stack(
          alignment: Alignment.center,
          children: [
            Align(
              alignment: Alignment.centerLeft,
              child: IconButton(
                icon: const Icon(Icons.arrow_back, color: Colors.white),
                onPressed: () => Navigator.of(context).pop(),
              ),
            ),
            Center(
              child: Text(
                title,
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  letterSpacing: 1.1,
                ),
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ],
        ),
      ),
    );
  }
} 