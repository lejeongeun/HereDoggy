import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../../utils/constants.dart';
import '../../providers/user_provider.dart';
import '../adoption/adoption_form.dart';
import 'package:provider/provider.dart';
import '../shelter/shelter_detail_page.dart';

class AdoptionDogDetailPage extends StatefulWidget {
  final int dogId;
  const AdoptionDogDetailPage({Key? key, required this.dogId}) : super(key: key);

  @override
  State<AdoptionDogDetailPage> createState() => _AdoptionDogDetailPageState();
}

class _AdoptionDogDetailPageState extends State<AdoptionDogDetailPage> {
  Map<String, dynamic>? dog;
  bool isLoading = true;
  String? error;
  int _currentPage = 0;

  @override
  void initState() {
    super.initState();
    fetchDogDetail();
  }

  Future<void> fetchDogDetail() async {
    setState(() {
      isLoading = true;
      error = null;
    });
    try {
      final response = await http.get(Uri.parse('${AppConstants.baseUrl}/dogs/${widget.dogId}'));
      if (response.statusCode == 200) {
        setState(() {
          dog = json.decode(utf8.decode(response.bodyBytes));
          isLoading = false;
        });
      } else {
        setState(() {
          error = '유기견 정보를 불러오지 못했습니다.';
          isLoading = false;
        });
      }
    } catch (e) {
      setState(() {
        error = '네트워크 오류가 발생했습니다.';
        isLoading = false;
      });
    }
  }

  String genderToKor(String gender) {
    switch (gender) {
      case 'MALE':
        return '수컷';
      case 'FEMALE':
        return '암컷';
      default:
        return '-';
    }
  }

  String neuteredToKor(bool? isNeutered) {
    if (isNeutered == null) return '-';
    return isNeutered ? 'O' : 'X';
  }

  String statusToKor(String status) {
    switch (status) {
      case 'AVAILABLE':
        return '예약가능';
      case 'RESERVED':
        return '예약 불가능';
      case 'ADOPTED':
        return '입양됨';
      default:
        return '-';
    }
  }

  // 보호소 목록 조회 및 id 찾기
  Future<void> navigateToShelterDetail(String shelterName) async {
    try {
      final response = await http.get(Uri.parse('${AppConstants.baseUrl}/shelters'));
      
      if (response.statusCode == 200) {
        final List<dynamic> shelters = json.decode(utf8.decode(response.bodyBytes));
        final shelter = shelters.firstWhere(
          (shelter) => shelter['shelterName'] == shelterName,
          orElse: () => null,
        );
        
        if (shelter != null) {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => ShelterDetailPage(
                shelterId: shelter['id'].toString(),
                shelterName: shelter['shelterName'],
              ),
            ),
          );
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('보호소를 찾을 수 없습니다.')),
          );
        }
      } else {
        throw Exception('Failed to load shelters');
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('보호소 정보를 불러오는데 실패했습니다.')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('유기견 상세 정보'),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : error != null
              ? Center(child: Text(error!))
              : dog == null
                  ? const Center(child: Text('정보 없음'))
                  : SingleChildScrollView(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          // 보호소 이름
                          Container(
                            color: Colors.grey[200],
                            padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 16),
                            child: InkWell(
                              onTap: () {
                                if (dog != null && dog!["shelterName"] != null) {
                                  navigateToShelterDetail(dog!["shelterName"]);
                                }
                              },
                              child: Row(
                                children: [
                                  const Icon(Icons.verified, color: Colors.green),
                                  const SizedBox(width: 8),
                                  Text(
                                    dog!["shelterName"] ?? '-',
                                    style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
                                  ),
                                ],
                              ),
                            ),
                          ),
                          // 이미지 슬라이더
                          if ((dog!["imagesUrls"] as List).isNotEmpty)
                            Column(
                              children: [
                                SizedBox(
                                  height: 220,
                                  child: PageView.builder(
                                    itemCount: (dog!["imagesUrls"] as List).length,
                                    onPageChanged: (index) {
                                      setState(() {
                                        _currentPage = index;
                                      });
                                    },
                                    itemBuilder: (context, idx) {
                                      final url = dog!["imagesUrls"][idx];
                                      return Image.network(
                                        AppConstants.baseUrl.replaceAll('/api', '') + url,
                                        fit: BoxFit.cover,
                                        width: double.infinity,
                                        errorBuilder: (c, e, s) => const Center(child: Icon(Icons.broken_image)),
                                      );
                                    },
                                  ),
                                ),
                                const SizedBox(height: 8),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: List.generate((dog!["imagesUrls"] as List).length, (idx) {
                                    return Container(
                                      margin: const EdgeInsets.symmetric(horizontal: 4),
                                      width: 8,
                                      height: 8,
                                      decoration: BoxDecoration(
                                        shape: BoxShape.circle,
                                        color: _currentPage == idx ? Colors.black : Colors.white,
                                        border: Border.all(color: Colors.black12),
                                      ),
                                    );
                                  }),
                                ),
                              ],
                            )
                          else
                            Container(
                              height: 220,
                              color: Colors.grey[300],
                              child: const Center(child: Icon(Icons.pets, size: 80, color: Colors.white)),
                            ),
                          // 기본 정보
                          Container(
                            color: Colors.white,
                            padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 16),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  '${dog!["name"] ?? '-'} | ${genderToKor(dog!["gender"] ?? '')}',
                                  style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                                ),
                                const SizedBox(height: 4),
                                Text('${dog!["age"] ?? '-'}살 / 중성화 ${neuteredToKor(dog!["isNeutered"])} / ${dog!["weight"] ?? '-'}kg'),
                                const SizedBox(height: 12),
                                const Text('성격/특징', style: TextStyle(fontWeight: FontWeight.bold)),
                                Text(dog!["personality"] ?? '-', style: const TextStyle(color: Colors.black87)),
                                const SizedBox(height: 8),
                                const Text('발견장소', style: TextStyle(fontWeight: FontWeight.bold)),
                                Text(dog!["foundLocation"] ?? '-', style: const TextStyle(color: Colors.black87)),
                                const SizedBox(height: 8),
                                const Text('소속 보호소', style: TextStyle(fontWeight: FontWeight.bold)),
                                Text(dog!["shelterName"] ?? '-', style: const TextStyle(color: Colors.black87)),
                                const SizedBox(height: 8),
                                const Text('현재 상태', style: TextStyle(fontWeight: FontWeight.bold)),
                                Container(
                                  margin: const EdgeInsets.only(top: 4),
                                  padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
                                  decoration: BoxDecoration(
                                    color: Colors.grey[100],
                                    borderRadius: BorderRadius.circular(16),
                                  ),
                                  child: Text(statusToKor(dog!["status"] ?? ''), style: const TextStyle(fontWeight: FontWeight.bold)),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 32),
                          // 입양신청 버튼
                          Padding(
                            padding: const EdgeInsets.symmetric(horizontal: 16.0),
                            child: ElevatedButton(
                              onPressed: () {
                                final isLoggedIn = Provider.of<UserProvider>(context, listen: false).isLoggedIn;
                                if (!isLoggedIn) {
                                  ScaffoldMessenger.of(context).showSnackBar(
                                    const SnackBar(content: Text('로그인이 필요한 서비스입니다')),
                                  );
                                  return;
                                }
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (context) => AdoptionFormPage(
                                      dogId: widget.dogId.toString(),
                                      dogName: dog != null && dog!['name'] != null ? dog!['name'] : '',
                                    ),
                                  ),
                                );
                              },
                              style: ElevatedButton.styleFrom(
                                minimumSize: const Size.fromHeight(48),
                                backgroundColor: Color(0xFFFF9800),
                                foregroundColor: Colors.white,
                              ),
                              child: const Text(
                                '입양 신청',
                                style: TextStyle(fontSize: 20),
                              ),
                            ),
                          ),
                          const SizedBox(height: 32),
                        ],
                      ),
                    ),
    );
  }
} 