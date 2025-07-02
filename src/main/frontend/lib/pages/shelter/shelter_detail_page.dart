import 'package:flutter/material.dart';
import '../../services/shelter_service.dart';
import '../../components/common/cards/dog_card.dart';
import '../../utils/constants.dart';
import '../../pages/shelter/adoption_dog_detail_page.dart';

class ShelterDetailPage extends StatefulWidget {
  final String shelterId;
  final String? shelterName;

  const ShelterDetailPage({Key? key, required this.shelterId, this.shelterName}) : super(key: key);

  @override
  State<ShelterDetailPage> createState() => _ShelterDetailPageState();
}

class _ShelterDetailPageState extends State<ShelterDetailPage> with SingleTickerProviderStateMixin {
  late TabController _tabController;
  Map<String, dynamic>? shelterData;
  List<Map<String, dynamic>>? dogs;
  bool isLoading = true;
  String? error;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
    _fetchShelterData();
    _fetchDogs();
  }

  Future<void> _fetchShelterData() async {
    try {
      final data = await ShelterService.getShelterDetail(widget.shelterId);
      print('Shelter data received: $data');
      setState(() {
        shelterData = data;
        isLoading = false;
      });
    } catch (e) {
      print('Error fetching shelter data: $e');
      setState(() {
        error = '보호소 정보를 불러오는데 실패했습니다.';
        isLoading = false;
      });
    }
  }

  Future<void> _fetchDogs() async {
    try {
      final dogsList = await ShelterService.getShelterDogs(widget.shelterId);
      print('Dogs data received: $dogsList');
      setState(() {
        dogs = dogsList;
      });
    } catch (e) {
      print('Error fetching dogs: $e');
      if (e.toString().contains('No authentication token found')) {
        // 토큰이 없는 경우 처리
        setState(() {
          dogs = [];
        });
      } else if (e.toString().contains('권한이 없습니다')) {
        // 권한이 없는 경우 처리
        setState(() {
          dogs = [];
        });
      }
    }
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return const Scaffold(
        body: Center(child: CircularProgressIndicator()),
      );
    }

    if (error != null) {
      return Scaffold(
        appBar: PreferredSize(
          preferredSize: const Size.fromHeight(80),
          child: _buildCustomAppBar(context, '오류'),
        ),
        body: Center(child: Text(error!)),
      );
    }

    return Scaffold(
      appBar: PreferredSize(
        preferredSize: const Size.fromHeight(50),
        child: _buildCustomAppBar(
          context,
          (widget.shelterName != null && widget.shelterName!.isNotEmpty)
              ? widget.shelterName!
              : ((shelterData?['name']?.isNotEmpty ?? false) ? shelterData!['name'] : '이름없음'),
        ),
      ),
      body: Column(
        children: [
          _buildImageGrid(),
          TabBar(
            controller: _tabController,
            labelColor: Colors.green,
            unselectedLabelColor: Colors.grey,
            tabs: const [
              Tab(text: '소개'),
              Tab(text: '보호 동물'),
            ],
          ),
          Expanded(
            child: TabBarView(
              controller: _tabController,
              children: [
                _buildIntroductionTab(),
                _buildDogsTab(),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildImageGrid() {
    // 기존 DB 연동 코드 주석 처리
    // final images = shelterData?['images'] as List<dynamic>? ?? [];
    
    return Container(
      height: 200,
      padding: const EdgeInsets.all(16),
      child: Row(
        children: [
          // 왼쪽 큰 이미지
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
          // 오른쪽 세로 스택 (2개의 이미지)
          Expanded(
            flex: 1,
            child: Column(
              children: [
                // 위쪽 이미지
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
                // 아래쪽 이미지
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

  Widget _buildIntroductionTab() {
    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // 주소
          const Text(
            '주소',
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            shelterData?['address'] ?? '-',
            style: const TextStyle(
              fontSize: 16,
              color: Colors.black54,
            ),
          ),
          const SizedBox(height: 20),
          // 연락처
          const Text(
            '연락처',
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            shelterData?['phone'] ?? '-',
            style: const TextStyle(
              fontSize: 16,
              color: Colors.black54,
            ),
          ),
          const SizedBox(height: 20),
          // 설명
          const Text(
            '설명',
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            shelterData?['description'] ?? '보호소 소개가 없습니다.',
            style: const TextStyle(
              fontSize: 16,
              color: Colors.black54,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDogsTab() {
    if (dogs == null) {
      return const Center(child: CircularProgressIndicator());
    }

    if (dogs!.isEmpty) {
      return const Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.pets, size: 48, color: Colors.grey),
            SizedBox(height: 16),
            Text(
              '현재 보호 중인 동물이 없거나\n조회 권한이 없습니다.',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 16,
                color: Colors.grey,
              ),
            ),
          ],
        ),
      );
    }

    return ListView.builder(
      padding: const EdgeInsets.all(0),
      itemCount: dogs!.length,
      itemBuilder: (context, index) {
        final dog = dogs![index];
        String genderKor = '';
        if (dog['gender'] == 'MALE') genderKor = '수컷';
        else if (dog['gender'] == 'FEMALE') genderKor = '암컷';
        final imageUrl = (dog['images'] != null && dog['images'].isNotEmpty)
            ? AppConstants.baseUrl.replaceAll('/api', '') + dog['images'][0]['imageUrl']
            : '';
        return DogCard(
          imageUrl: imageUrl,
          name: dog['name'] ?? '',
          age: dog['age'] ?? 0,
          gender: genderKor,
          weight: dog['weight'] ?? 0.0,
          foundLocation: dog['foundLocation'] ?? '',
          shelterName: widget.shelterName ?? '',
          onTap: () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => AdoptionDogDetailPage(dogId: dog['id'] is int ? dog['id'] : int.parse(dog['id'].toString())),
              ),
            );
          },
        );
      },
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