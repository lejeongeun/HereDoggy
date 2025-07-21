import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/user_provider.dart';
import '../../components/auth/login_form.dart';
import '../home/home_page.dart';
import '../../services/auth_service.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../walk_route/walk_route_select_page.dart';
import '../../services/dog_service.dart';
import '../../components/common/cards/dog_card.dart';
import '../../components/common/cards/dog_card_reservation.dart';
import '../../utils/constants.dart';
import '../mypage/my_posts_tab.dart';
import '../walk_route/walk_result_page.dart';
import '../../models/member_reservation_response_dto.dart';
import '../../services/reservation_service.dart';
import '../../models/walk_simple_statistic_dto.dart';
import '../../services/walk_record_service.dart';
import 'setting_page.dart';
import 'adoption_history_page.dart';
import 'inquiry_history_page.dart';
import '../volunteer/volunteer_history_page.dart';
import 'inquiry_report_history_page.dart';


class MyPage extends StatefulWidget {
  const MyPage({Key? key}) : super(key: key);

  @override
  State<MyPage> createState() => _MyPageState();
}

class _MyPageState extends State<MyPage> {
  final WalkRecordService _walkRecordService = WalkRecordService();
  WalkSimpleStatisticDTO? _statistic;
  bool _statisticLoading = true;
  String? _statisticError;

  @override
  void initState() {
    super.initState();
    _fetchStatistics();
  }

  Future<void> _fetchStatistics() async {
    try {
      final stat = await _walkRecordService.getWalkStatistics();
      setState(() {
        _statistic = stat;
        _statisticLoading = false;
      });
    } catch (e) {
      setState(() {
        _statisticError = '산책 통계를 불러오지 못했습니다.';
        _statisticLoading = false;
      });
    }
  }

  String _formatDuration(int seconds) {
    final minutes = seconds ~/ 60;
    final remainSec = seconds % 60;
    return '${minutes}분${remainSec}초';
  }

  String _formatDistance(double meters) {
    final kilometers = meters / 1000;
    return '${kilometers.toStringAsFixed(2)}km';
  }

  @override
  Widget build(BuildContext context) {
    final userProvider = Provider.of<UserProvider>(context);
    final isLoggedIn = userProvider.isLoggedIn;
    final Color green = const Color(0xFF4CAF50);
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        title: Text('마이페이지', style: TextStyle(color: green, fontWeight: FontWeight.bold)),
        actions: [
          IconButton(
            icon: const Icon(Icons.settings, color: Colors.black),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => const SettingPage(),
                ),
              );
            },
          ),
        ],
        iconTheme: const IconThemeData(color: Colors.black),
      ),
      body: isLoggedIn
          ? Column(
              children: [
                InkWell(
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => const ProfileEditPage(),
                      ),
                    );
                  },
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 24),
                    child: Row(
                      children: [
                        const CircleAvatar(
                          radius: 32,
                          backgroundColor: Colors.black,
                          child: Icon(Icons.person, color: Colors.white, size: 40),
                        ),
                        const SizedBox(width: 16),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                '${userProvider.user?['name'] ?? '사용자'}님 안녕하세요',
                                style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.black87),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                userProvider.user?['email'] ?? '',
                                style: TextStyle(fontSize: 14, color: green.withOpacity(0.7)),
                              ),
                            ],
                          ),
                        ),
                        const Icon(Icons.chevron_right, size: 32, color: Colors.black54),
                      ],
                    ),
                  ),
                ),
                const Divider(thickness: 1, height: 1),
                // 산책 통계 표시
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 18),
                  child: _statisticLoading
                      ? const Center(child: SizedBox(height: 48, child: CircularProgressIndicator()))
                      : _statisticError != null
                          ? Center(child: Text(_statisticError!))
                          : Container(
                              decoration: BoxDecoration(
                                color: Colors.white,
                                borderRadius: BorderRadius.circular(16),
                                border: Border.all(color: Colors.blue.shade100, width: 1.5),
                              ),
                              padding: const EdgeInsets.symmetric(vertical: 18, horizontal: 8),
                              child: Row(
                                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                                children: [
                                  _buildStatItem('총 이동거리', _formatDistance(_statistic!.totalDistance)),
                                  Container(width: 1, height: 40, color: Colors.blue.shade100),
                                  _buildStatItem('총 산책시간', _formatDuration(_statistic!.totalDuration)),
                                  Container(width: 1, height: 40, color: Colors.blue.shade100),
                                  _buildStatItem('총 산책횟수', '${_statistic!.totalWalkCount}회'),
                                ],
                              ),
                            ),
                ),
                // 5개 탭 UI 추가
                ListTile(
                  title: Text('산책', style: TextStyle(fontSize: 16)),
                  trailing: Icon(Icons.chevron_right),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => const WalkTabPage(),
                      ),
                    );
                  },
                ),
                ListTile(
                  title: Text('내 게시글', style: TextStyle(fontSize: 16)),
                  trailing: Icon(Icons.chevron_right),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const MyPostsTab()),
                    );
                  },
                ),
                ListTile(
                  title: Text('입양신청 내역', style: TextStyle(fontSize: 16)),
                  trailing: Icon(Icons.chevron_right),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => const AdoptionHistoryPage(),
                      ),
                    );
                  },
                ),
                ListTile(
                  title: Text('봉사 내역', style: TextStyle(fontSize: 16)),
                  trailing: Icon(Icons.chevron_right),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => const VolunteerHistoryPage(),
                      ),
                    );
                  },
                ),
                ListTile(
                  title: Text('후원내역', style: TextStyle(fontSize: 16)),
                  trailing: Icon(Icons.chevron_right),
                  onTap: null,
                ),
                ListTile(
                  title: Text('문의 & 신고내역', style: TextStyle(fontSize: 16)),
                  trailing: Icon(Icons.chevron_right),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => const InquiryReportHistoryPage(),
                      ),
                    );
                  },
                ),
                const Expanded(child: SizedBox()),
              ],
            )
          : Column(
              children: [
                InkWell(
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => const LoginFullScreenPage(),
                      ),
                    );
                  },
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 24),
                    child: Row(
                      children: [
                        const CircleAvatar(
                          radius: 32,
                          backgroundColor: Colors.black,
                          child: Icon(Icons.person, color: Colors.white, size: 40),
                        ),
                        const SizedBox(width: 16),
                        const Expanded(
                          child: Text(
                            '로그인을 해주세요',
                            style: TextStyle(fontSize: 18, color: Colors.black87),
                          ),
                        ),
                        const Icon(Icons.chevron_right, size: 32, color: Colors.black54),
                      ],
                    ),
                  ),
                ),
                const Divider(thickness: 1, height: 1),
                // 나머지 공간 비우기
                const Expanded(child: SizedBox()),
              ],
            ),
      bottomNavigationBar: _HomeBottomNavBar(currentIndex: 3),
    );
  }

  Widget _buildStatItem(String label, String value) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Text(label, style: const TextStyle(fontSize: 13, color: Colors.black54)),
        const SizedBox(height: 6),
        Text(value, style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: Colors.black)),
      ],
    );
  }
}

// _HomeBottomNavBar를 재사용할 수 있도록 수정
class _HomeBottomNavBar extends StatelessWidget {
  final int currentIndex;
  const _HomeBottomNavBar({this.currentIndex = 0, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final Color green = const Color(0xFF4CAF50);
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [
          BoxShadow(
            color: Colors.black12,
            offset: Offset(0, -2),
            blurRadius: 12,
          ),
        ],
      ),
      child: BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        selectedItemColor: green,
        unselectedItemColor: Colors.black,
        showSelectedLabels: true,
        showUnselectedLabels: true,
        backgroundColor: Colors.white,
        currentIndex: currentIndex,
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
              Navigator.pushReplacementNamed(context, '/');
              break;
            case 1:
              Navigator.pushReplacementNamed(context, '/walkReservation');
              break;
            case 2:
              Navigator.pushReplacementNamed(context, '/community');
              break;
            case 3:
              Navigator.pushReplacementNamed(context, '/mypage');
              break;
          }
        },
      ),
    );
  }
}

// 프로필 수정 페이지
class ProfileEditPage extends StatelessWidget {
  const ProfileEditPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final userProvider = Provider.of<UserProvider>(context, listen: false);
    final Color green = const Color(0xFF4CAF50);
    final String nickname = userProvider.user?['nickname'] ?? '';
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.black),
          onPressed: () => Navigator.pop(context),
        ),
        title: Text('프로필 수정', style: TextStyle(color: green, fontWeight: FontWeight.bold)),
        centerTitle: false,
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            const SizedBox(height: 24),
            Center(
              child: Stack(
                children: [
                  const CircleAvatar(
                    radius: 48,
                    backgroundColor: Colors.black,
                    child: Icon(Icons.person, color: Colors.white, size: 60),
                  ),
                  Positioned(
                    right: 0,
                    bottom: 0,
                    child: Container(
                      decoration: BoxDecoration(
                        color: Colors.white,
                        shape: BoxShape.circle,
                        boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 2)],
                      ),
                      child: const Padding(
                        padding: EdgeInsets.all(4.0),
                        child: Icon(Icons.camera_alt, color: Colors.black54, size: 22),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 32),
            Align(
              alignment: Alignment.centerLeft,
              child: Text('닉네임', style: TextStyle(fontSize: 16, color: Colors.black87)),
            ),
            const SizedBox(height: 8),
            TextField(
              enabled: false,
              controller: TextEditingController(text: nickname),
              decoration: InputDecoration(
                filled: true,
                fillColor: Colors.grey[200],
                border: InputBorder.none,
                contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
              ),
              style: const TextStyle(fontSize: 18, color: Colors.black87),
            ),
            const SizedBox(height: 24),
            const Divider(thickness: 1),
            ListTile(
              title: const Text('비밀번호 변경', style: TextStyle(fontSize: 16)),
              trailing: const Icon(Icons.chevron_right),
              onTap: () {},
            ),
            ListTile(
              title: const Text('회원탈퇴', style: TextStyle(fontSize: 16, color: Colors.red)),
              trailing: const Icon(Icons.chevron_right),
              onTap: () {},
            ),
            const Spacer(),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.grey[300],
                  foregroundColor: Colors.black,
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  textStyle: const TextStyle(fontSize: 18),
                  elevation: 0,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(4)),
                ),
                onPressed: () {
                  userProvider.logout();
                  Navigator.pushNamedAndRemoveUntil(context, '/', (route) => false);
                },
                child: const Text('로그아웃'),
              ),
            ),
            const SizedBox(height: 24),
          ],
        ),
      ),
    );
  }
}

// 예약내역
class WalkReservationHistoryPage extends StatefulWidget {
  const WalkReservationHistoryPage({Key? key}) : super(key: key);

  @override
  State<WalkReservationHistoryPage> createState() => _WalkReservationHistoryPageState();
}

class _WalkReservationHistoryPageState extends State<WalkReservationHistoryPage> {
  final _authService = AuthService();
  final _dogService = DogService();
  bool _isLoading = true;
  String? _error;
  List<dynamic> _reservations = [];
  Map<int, dynamic> _dogInfoMap = {}; // dogId -> dog info
  bool _dogsLoading = false;

  @override
  void initState() {
    super.initState();
    _fetchReservations();
  }

  Future<void> _fetchReservations() async {
    try {
      final token = await _authService.getAccessToken();
      final response = await http.get(
        Uri.parse('${AppConstants.baseUrl}/members/reservations'),
        headers: {'Authorization': 'Bearer $token'},
      );
      if (response.statusCode == 200) {
        final reservations = json.decode(utf8.decode(response.bodyBytes));
        setState(() {
          _reservations = reservations;
          _isLoading = false;
        });
        _fetchDogsForReservations(reservations);
      } else {
        setState(() {
          _error = '예약 내역을 불러오지 못했습니다.';
          _isLoading = false;
        });
      }
    } catch (e) {
      setState(() {
        _error = '네트워크 오류가 발생했습니다.';
        _isLoading = false;
      });
    }
  }

  Future<void> _fetchDogsForReservations(List reservations) async {
    setState(() { _dogsLoading = true; });
    final dogIds = reservations.map((r) => r['dogId'] as int).toSet().toList();
    final Map<int, dynamic> dogInfoMap = {};
    for (final dogId in dogIds) {
      try {
        final dog = await _dogService.getDogDetails(dogId);
        dogInfoMap[dogId] = dog;
      } catch (e) {
        dogInfoMap[dogId] = null;
      }
    }
    setState(() {
      _dogInfoMap = dogInfoMap;
      _dogsLoading = false;
    });
  }

  // 상태별로 예약 분류
  Map<String, List<dynamic>> _groupByStatus(List reservations) {
    final Map<String, List<dynamic>> map = {
      'PENDING': [],
      'APPROVED': [],
      'REJECTED': [],
      'CANCELED_REQUEST': [],
      'COMPLETED': [],
    };
    for (final r in reservations) {
      final status = r['walkReservationStatus'] ?? '';
      if (map.containsKey(status)) {
        map[status]!.add(r);
      }
    }
    return map;
  }

  Color get green => const Color(0xFF4CAF50);

  @override
  Widget build(BuildContext context) {
    final statusOrder = [
      'PENDING',
      'APPROVED',
      'REJECTED',
      'CANCELED_REQUEST',
      'COMPLETED',
    ];
    final statusKor = {
      'PENDING': '대기중',
      'APPROVED': '승인됨',
      'REJECTED': '거절됨',
      'CANCELED_REQUEST': '취소 요청',
      'COMPLETED': '완료',
    };
    final grouped = _groupByStatus(_reservations);

    return Scaffold(
      appBar: AppBar(
        title: const Text('예약 내역'),
        backgroundColor: green,
        foregroundColor: Colors.white,
      ),
      body: _isLoading || _dogsLoading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Center(child: Text(_error!))
              : _reservations.isEmpty
                  ? const Center(child: Text('예약 내역이 없습니다.'))
                  : SingleChildScrollView(
                      child: Padding(
                        padding: const EdgeInsets.symmetric(vertical: 8),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            for (final status in statusOrder)
                              if (grouped[status]!.isNotEmpty) ...[
                                Padding(
                                  padding: const EdgeInsets.only(left: 16, top: 16, bottom: 4),
                                  child: Text(
                                    statusKor[status]!,
                                    style: TextStyle(
                                      fontSize: 20,
                                      fontWeight: FontWeight.bold,
                                      color: green,
                                    ),
                                  ),
                                ),
                                ...grouped[status]!.map<Widget>((r) {
                                  final dog = _dogInfoMap[r['dogId']];
                                  if (dog == null) {
                                    return Card(
                                      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                                      child: Padding(
                                        padding: const EdgeInsets.all(24),
                                        child: Row(
                                          children: const [
                                            Icon(Icons.pets, size: 40, color: Colors.grey),
                                            SizedBox(width: 16),
                                            Text('강아지 정보를 불러올 수 없습니다.', style: TextStyle(color: Colors.black54)),
                                          ],
                                        ),
                                      ),
                                    );
                                  }
                                  return DogCardReservation(
                                    imageUrl: dog.imagesUrls.isNotEmpty
                                        ? '${AppConstants.baseUrl.replaceAll('/api', '')}${dog.imagesUrls.first}'
                                        : '',
                                    name: dog.name,
                                    age: dog.age,
                                    gender: dog.gender == 'MALE' ? '수컷' : '암컷',
                                    weight: dog.weight,
                                    shelterName: dog.shelterName,
                                    reservationDate: r['date'] ?? '-',
                                    reservationTime: '${r['startTime'] ?? '-'} ~ ${r['endTime'] ?? '-'}',
                                    onTap: status == 'APPROVED' || status == 'PENDING'
                                        ? () {
                                            Navigator.push(
                                              context,
                                              MaterialPageRoute(
                                                builder: (_) => WalkRouteSelectPage(
                                                  shelterId: r['shelterId'],
                                                  reservationId: r['id'],
                                                ),
                                              ),
                                            );
                                          }
                                        : null,
                                  );
                                }).toList(),
                              ],
                          ],
                        ),
                      ),
                    ),
    );
  }
}

// 산책 관련 탭 모음 페이지
class WalkTabPage extends StatelessWidget {
  const WalkTabPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('산책'),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: Column(
        children: [
          const Divider(thickness: 1, height: 1),
          ListTile(
            title: const Text('예약 내역'),
            trailing: const Icon(Icons.chevron_right),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => const WalkReservationHistoryPage(),
                ),
              );
            },
          ),
          const Divider(thickness: 1, height: 1),
          ListTile(
            title: const Text('산책 내역'),
            trailing: const Icon(Icons.chevron_right),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => const WalkHistoryPage(),
                ),
              );
            },
          ),
        ],
      ),
    );
  }
}

// 산책 내역 페이지
class WalkHistoryPage extends StatefulWidget {
  const WalkHistoryPage({Key? key}) : super(key: key);

  @override
  State<WalkHistoryPage> createState() => _WalkHistoryPageState();
}

class _WalkHistoryPageState extends State<WalkHistoryPage> {
  final _authService = AuthService();
  final _reservationService = ReservationService();
  bool _isLoading = true;
  String? _error;
  List<dynamic> _walkRecords = [];
  Map<int, MemberReservationResponseDTO?> _reservationInfoMap = {}; // reservationId -> 상세정보

  @override
  void initState() {
    super.initState();
    _fetchWalkRecords();
  }

  Future<void> _fetchWalkRecords() async {
    try {
      final token = await _authService.getAccessToken();
      final response = await http.get(
        Uri.parse('${AppConstants.baseUrl}/members/walk-records?status=COMPLETED'),
        headers: {'Authorization': 'Bearer $token'},
      );
      if (response.statusCode == 200) {
        final records = json.decode(utf8.decode(response.bodyBytes));
        // 최신순 정렬
        records.sort((a, b) {
          final aTime = DateTime.parse(a['endTime'] ?? '');
          final bTime = DateTime.parse(b['endTime'] ?? '');
          return bTime.compareTo(aTime);
        });
        setState(() {
          _walkRecords = records;
          _isLoading = false;
        });
        // 각 record의 reservationId로 상세 정보 비동기 호출
        for (final record in records) {
          final reservationId = record['reservationId'];
          if (reservationId != null) {
            _fetchReservationInfo(reservationId);
          }
        }
      } else {
        setState(() {
          _error = '산책 내역을 불러오지 못했습니다.';
          _isLoading = false;
        });
      }
    } catch (e) {
      setState(() {
        _error = '네트워크 오류가 발생했습니다.';
        _isLoading = false;
      });
    }
  }

  Future<void> _fetchReservationInfo(int reservationId) async {
    if (_reservationInfoMap.containsKey(reservationId)) return;
    try {
      final info = await _reservationService.getReservationDetail(reservationId);
      setState(() {
        _reservationInfoMap[reservationId] = info;
      });
    } catch (e) {
      setState(() {
        _reservationInfoMap[reservationId] = null;
      });
    }
  }

  String _formatDateTime(String? dateTimeStr) {
    if (dateTimeStr == null) return '-';
    try {
      final dateTime = DateTime.parse(dateTimeStr);
      return '${dateTime.month.toString().padLeft(2, '0')}.${dateTime.day.toString().padLeft(2, '0')} (${_getWeekdayKor(dateTime.weekday)}) ${dateTime.hour.toString().padLeft(2, '0')}:${dateTime.minute.toString().padLeft(2, '0')}' ;
    } catch (e) {
      return '-';
    }
  }

  String _formatDuration(int? seconds) {
    if (seconds == null) return '-';
    final minutes = seconds ~/ 60;
    final remainSec = seconds % 60;
    return '${minutes}분 ${remainSec}초';
  }

  String _formatDistance(dynamic meters) {
    if (meters == null) return '-';
    if (meters is int || meters is double) {
      return '${meters.round()}m';
    }
    return '-';
  }

  String _getFullImageUrl(String relativeUrl) {
    if (relativeUrl.isEmpty) return '';
    final serverBaseUrl = AppConstants.baseUrl.replaceAll('/api', '');
    final normalizedPath = relativeUrl.startsWith('/') ? relativeUrl : '/$relativeUrl';
    return '$serverBaseUrl$normalizedPath';
  }

  String _getWeekdayKor(int weekday) {
    const days = ['월', '화', '수', '목', '금', '토', '일'];
    return days[(weekday - 1) % 7];
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('산책 내역'),
        backgroundColor: const Color(0xFF4CAF50),
        foregroundColor: Colors.white,
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Center(child: Text(_error!))
              : _walkRecords.isEmpty
                  ? const Center(child: Text('산책 내역이 없습니다.'))
                  : Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                      child: GridView.builder(
                        itemCount: _walkRecords.length,
                        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                          crossAxisCount: 2,
                          crossAxisSpacing: 16,
                          mainAxisSpacing: 16,
                          childAspectRatio: 0.5,
                        ),
                        itemBuilder: (context, index) {
                          final record = _walkRecords[index];
                          final thumbnailUrl = _getFullImageUrl(record['thumbnailUrl'] ?? '');
                          final reservationId = record['reservationId'];
                          final reservationInfo = reservationId != null ? _reservationInfoMap[reservationId] : null;
                          return GestureDetector(
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => WalkResultPage(
                                    distance: (record['actualDistance'] ?? 0).toDouble(),
                                    duration: record['actualDuration'] ?? 0,
                                    imageUrl: record['thumbnailUrl'] ?? '',
                                    startTime: record['startTime'],
                                    endTime: record['endTime'],
                                    fromHistory: true,
                                  ),
                                ),
                              );
                            },
                            child: Container(
                              decoration: BoxDecoration(
                                color: const Color(0xFFF5F9FF),
                                borderRadius: BorderRadius.circular(12),
                              ),
                              child: Column(
                                mainAxisSize: MainAxisSize.min,
                                crossAxisAlignment: CrossAxisAlignment.stretch,
                                children: [
                                  Expanded(
                                    child: ClipRRect(
                                      borderRadius: const BorderRadius.vertical(
                                        top: Radius.circular(12),
                                      ),
                                      child: thumbnailUrl.isNotEmpty
                                          ? Image.network(
                                              thumbnailUrl,
                                              fit: BoxFit.cover,
                                              errorBuilder: (context, error, stackTrace) => Container(
                                                color: Colors.grey[100],
                                                child: const Center(child: Icon(Icons.image_not_supported, size: 32, color: Colors.grey)),
                                              ),
                                            )
                                          : Container(
                                              color: Colors.grey[100],
                                              child: const Center(child: Icon(Icons.image, size: 32, color: Colors.grey)),
                                            ),
                                    ),
                                  ),
                                  Expanded(
                                    child: Padding(
                                      padding: const EdgeInsets.all(12),
                                      child: Column(
                                        crossAxisAlignment: CrossAxisAlignment.start,
                                        children: [
                                          // 보호소명, 강아지명 표시
                                          Row(
                                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                            children: [
                                              Expanded(
                                                child: Text(
                                                  reservationInfo?.shelterName ?? '보호소 정보 불러오는 중...',
                                                  style: const TextStyle(fontSize: 13, color: Colors.blue, fontWeight: FontWeight.bold),
                                                  maxLines: 1,
                                                  overflow: TextOverflow.ellipsis,
                                                ),
                                              ),
                                              const SizedBox(width: 8),
                                              Expanded(
                                                child: Text(
                                                  reservationInfo?.dogName ?? '강아지 정보 불러오는 중...',
                                                  style: const TextStyle(fontSize: 13, color: Colors.black87),
                                                  textAlign: TextAlign.right,
                                                  maxLines: 1,
                                                  overflow: TextOverflow.ellipsis,
                                                ),
                                              ),
                                            ],
                                          ),
                                          const SizedBox(height: 4),
                                          Container(
                                            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                                            decoration: BoxDecoration(
                                              color: const Color(0xFFE3F2FD),
                                              borderRadius: BorderRadius.circular(4),
                                            ),
                                            child: Text(
                                              'COMPLETED',
                                              style: TextStyle(
                                                color: Colors.blue[700],
                                                fontSize: 11,
                                                fontWeight: FontWeight.w500,
                                              ),
                                            ),
                                          ),
                                          const SizedBox(height: 8),
                                          Text(
                                            '${_formatDateTime(record['startTime'])} ~',
                                            style: const TextStyle(
                                              fontSize: 13,
                                              color: Colors.black87,
                                              height: 1.3,
                                            ),
                                            maxLines: 1,
                                            overflow: TextOverflow.ellipsis,
                                          ),
                                          Text(
                                            _formatDateTime(record['endTime']),
                                            style: const TextStyle(
                                              fontSize: 13,
                                              color: Colors.black87,
                                              height: 1.3,
                                            ),
                                            maxLines: 1,
                                            overflow: TextOverflow.ellipsis,
                                          ),
                                          const Spacer(),
                                          Text(
                                            '산책 시간: ${_formatDuration(record['actualDuration'])}',
                                            style: const TextStyle(
                                              fontSize: 12,
                                              color: Colors.black54,
                                              height: 1.3,
                                            ),
                                          ),
                                          Text(
                                            '이동 거리: ${_formatDistance(record['actualDistance'])}',
                                            style: const TextStyle(
                                              fontSize: 12,
                                              color: Colors.black54,
                                              height: 1.3,
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          );
                        },
                      ),
                    ),
    );
  }
} 