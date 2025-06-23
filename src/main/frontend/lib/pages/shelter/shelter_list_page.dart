import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../../utils/constants.dart';
import 'shelter_detail_page.dart';

class ShelterListPage extends StatefulWidget {
  const ShelterListPage({Key? key}) : super(key: key);

  @override
  State<ShelterListPage> createState() => _ShelterListPageState();
}

class _ShelterListPageState extends State<ShelterListPage> {
  // 시/도, 구/시/군 데이터 하드코딩
  final Map<String, List<String>> regionMap = const {
    '서울': [
      '전체',
      '강남구', '강동구', '강북구', '강서구', '관악구', '광진구', '구로구', '금천구',
      '노원구', '도봉구', '동대문구', '동작구', '마포구', '서대문구', '서초구', '성동구',
      '성북구', '송파구', '양천구', '영등포구', '용산구', '은평구', '종로구', '중구', '중랑구',
    ],
    '경기도': [
      '전체',
      '수원시', '성남시', '고양시', '용인시', '부천시', '안산시', '안양시', '남양주시',
      '화성시', '평택시', '의정부시', '시흥시', '파주시', '김포시', '광명시', '광주시',
      '군포시', '오산시', '이천시', '안성시', '의왕시', '하남시', '여주시', '양평군',
      '동두천시', '과천시', '구리시', '양주시', '포천시', '연천군', '가평군',
    ],
    '강원도': [
      '전체',
      '춘천시', '원주시', '강릉시', '동해시', '태백시', '속초시', '삼척시',
      '홍천군', '횡성군', '영월군', '평창군', '정선군', '철원군', '화천군', '양구군', '인제군', '고성군', '양양군',
    ],
    '충청북도': [
      '전체',
      '청주시', '충주시', '제천시', '보은군', '옥천군', '영동군', '증평군', '진천군', '괴산군', '음성군', '단양군',
    ],
    '충청남도': [
      '전체',
      '천안시', '공주시', '보령시', '아산시', '서산시', '논산시', '계룡시', '당진시',
      '금산군', '부여군', '서천군', '청양군', '홍성군', '예산군', '태안군',
    ],
    '경상북도': [
      '전체',
      '포항시', '경주시', '김천시', '안동시', '구미시', '영주시', '영천시', '상주시', '문경시', '경산시',
      '군위군', '의성군', '청송군', '영양군', '영덕군', '청도군', '고령군', '성주군', '칠곡군', '예천군', '봉화군', '울진군', '울릉군',
    ],
    '경상남도': [
      '전체',
      '창원시', '진주시', '통영시', '사천시', '김해시', '밀양시', '거제시', '양산시',
      '의령군', '함안군', '창녕군', '고성군', '남해군', '하동군', '산청군', '함양군', '거창군', '합천군',
    ],
    '전라북도': [
      '전체',
      '전주시', '군산시', '익산시', '정읍시', '남원시', '김제시', '완주군', '진안군', '무주군', '장수군', '임실군', '순창군', '고창군', '부안군',
    ],
    '전라남도': [
      '전체',
      '목포시', '여수시', '순천시', '나주시', '광양시',
      '담양군', '곡성군', '구례군', '고흥군', '보성군', '화순군', '장흥군', '강진군', '해남군', '영암군', '무안군', '함평군', '영광군', '장성군', '완도군', '진도군', '신안군',
    ],
    '제주도': [
      '전체', '제주시', '서귀포시',
    ],
  };

  String selectedRegion = '서울';
  String selectedSubRegion = '전체';
  List<dynamic> shelters = [];
  bool isLoading = false;
  String? error;

  final Color mainGreen = const Color(0xFF4CAF50);
  final Color mainBlue = const Color(0xFF2196F3);
  final Color lightGreen = const Color(0xFFE8F5E9);
  final Color dividerColor = const Color(0xFFE0E0E0);

  @override
  void initState() {
    super.initState();
    fetchShelters();
  }

  Future<void> fetchShelters() async {
    setState(() {
      isLoading = true;
      error = null;
    });
    try {
      String regionParam = selectedRegion;
      if (selectedSubRegion != '전체') {
        regionParam += ' $selectedSubRegion';
      }
      final uri = Uri.parse('${AppConstants.baseUrl}/shelters?region=$regionParam');
      final response = await http.get(uri);
      if (response.statusCode == 200) {
        shelters = json.decode(utf8.decode(response.bodyBytes));
      } else {
        error = '서버 오류: ${response.statusCode}';
      }
    } catch (e) {
      error = '네트워크 오류';
    }
    setState(() {
      isLoading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: mainGreen,
        elevation: 0,
        centerTitle: true,
        title: const Text('보호소 찾기', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        leading: IconButton(
          icon: const Icon(Icons.close, color: Colors.white),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: Column(
        children: [
          Container(
            margin: const EdgeInsets.symmetric(horizontal: 0, vertical: 0),
            padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 10),
            width: double.infinity,
            decoration: BoxDecoration(
              color: lightGreen,
              borderRadius: const BorderRadius.only(
                bottomLeft: Radius.circular(18),
                bottomRight: Radius.circular(18),
              ),
            ),
            child: Row(
              children: [
                // 시/도 드롭다운
                Expanded(
                  child: Container(
                    height: 44,
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(10),
                      border: Border.all(color: mainGreen, width: 1.2),
                    ),
                    alignment: Alignment.center,
                    child: DropdownButtonHideUnderline(
                      child: DropdownButton<String>(
                        value: selectedRegion,
                        isExpanded: true,
                        alignment: Alignment.center,
                        icon: Icon(Icons.arrow_drop_down, color: mainGreen),
                        style: TextStyle(fontWeight: FontWeight.bold, color: mainGreen, fontSize: 16),
                        dropdownColor: Colors.white,
                        selectedItemBuilder: (context) {
                          return regionMap.keys.map((region) {
                            return Center(child: Text(region, style: TextStyle(fontWeight: FontWeight.bold, color: mainGreen, fontSize: 16)));
                          }).toList();
                        },
                        items: regionMap.keys.map((region) {
                          return DropdownMenuItem<String>(
                            value: region,
                            child: Center(child: Text(region, style: TextStyle(fontWeight: FontWeight.bold, color: mainGreen, fontSize: 16))),
                          );
                        }).toList(),
                        onChanged: (value) {
                          if (value != null && value != selectedRegion) {
                            setState(() {
                              selectedRegion = value;
                              selectedSubRegion = '전체';
                            });
                            fetchShelters();
                          }
                        },
                      ),
                    ),
                  ),
                ),
                const SizedBox(width: 10),
                // 구/시/군 드롭다운
                Expanded(
                  child: Container(
                    height: 44,
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(10),
                      border: Border.all(color: mainGreen, width: 1.2),
                    ),
                    alignment: Alignment.center,
                    child: DropdownButtonHideUnderline(
                      child: DropdownButton<String>(
                        value: selectedSubRegion,
                        isExpanded: true,
                        alignment: Alignment.center,
                        icon: Icon(Icons.arrow_drop_down, color: mainGreen),
                        style: TextStyle(fontWeight: FontWeight.bold, color: mainGreen, fontSize: 16),
                        dropdownColor: Colors.white,
                        selectedItemBuilder: (context) {
                          return regionMap[selectedRegion]!.map((sub) {
                            return Center(child: Text(sub, style: TextStyle(fontWeight: FontWeight.bold, color: mainGreen, fontSize: 16)));
                          }).toList();
                        },
                        items: regionMap[selectedRegion]!.map((sub) {
                          return DropdownMenuItem<String>(
                            value: sub,
                            child: Center(child: Text(sub, style: TextStyle(fontWeight: FontWeight.bold, color: mainGreen, fontSize: 16))),
                          );
                        }).toList(),
                        onChanged: (value) {
                          if (value != null && value != selectedSubRegion) {
                            setState(() {
                              selectedSubRegion = value;
                            });
                            fetchShelters();
                          }
                        },
                      ),
                    ),
                  ),
                ),
                const SizedBox(width: 10),
                // 현위치 아이콘 (기능 없음)
                Container(
                  height: 44,
                  width: 44,
                  decoration: BoxDecoration(
                    color: mainGreen.withOpacity(0.12),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: IconButton(
                    icon: Icon(Icons.my_location, color: mainGreen),
                    onPressed: () {},
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 8),
          Expanded(
            child: isLoading
                ? const Center(child: CircularProgressIndicator())
                : error != null
                    ? Center(child: Text(error!, style: const TextStyle(color: Colors.red)))
                    : shelters.isEmpty
                        ? const Center(child: Text('해당 지역에 보호소가 없습니다.', style: TextStyle(color: Colors.black54)))
                        : ListView.separated(
                            padding: const EdgeInsets.symmetric(horizontal: 0, vertical: 0),
                            itemCount: shelters.length,
                            separatorBuilder: (context, idx) => Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 16.0),
                              child: Divider(color: dividerColor, thickness: 1, height: 32),
                            ),
                            itemBuilder: (context, idx) {
                              final shelter = shelters[idx];
                              return Material(
                                color: Colors.transparent,
                                child: InkWell(
                                  onTap: () {
                                    Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                        builder: (context) => ShelterDetailPage(
                                          shelterId: shelter['id'].toString(),
                                          shelterName: shelter['shelterName'] ?? '',
                                        ),
                                      ),
                                    );
                                  },
                                  borderRadius: BorderRadius.zero,
                                  child: Padding(
                                    padding: const EdgeInsets.symmetric(vertical: 18, horizontal: 0),
                                    child: Padding(
                                      padding: const EdgeInsets.only(left: 16.0),
                                      child: Column(
                                        crossAxisAlignment: CrossAxisAlignment.start,
                                        children: [
                                          Text(
                                            shelter['shelterName'] ?? '',
                                            style: TextStyle(
                                              fontWeight: FontWeight.bold,
                                              fontSize: 20,
                                              color: Colors.black,
                                            ),
                                            textAlign: TextAlign.left,
                                          ),
                                          const SizedBox(height: 6),
                                          Text(
                                            shelter['address'] ?? '',
                                            style: const TextStyle(fontSize: 14, color: Colors.black87),
                                            textAlign: TextAlign.left,
                                          ),
                                          const SizedBox(height: 4),
                                          Text(
                                            'Tel) ${shelter['phone'] ?? '-'}',
                                            style: TextStyle(fontSize: 14, color: Colors.black45),
                                            textAlign: TextAlign.left,
                                          ),
                                        ],
                                      ),
                                    ),
                                  ),
                                ),
                              );
                            },
                          ),
          ),
        ],
      ),
    );
  }
} 