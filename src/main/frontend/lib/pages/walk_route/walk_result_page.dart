import 'package:flutter/material.dart';
import '../../models/walk_record_end_request_dto.dart';
import '../../utils/constants.dart';

class WalkResultPage extends StatelessWidget {
  final double distance;
  final int duration;
  final String imageUrl;
  final String? startTime;
  final String? endTime;
  final bool fromHistory;

  const WalkResultPage({
    Key? key,
    required this.distance,
    required this.duration,
    required this.imageUrl,
    this.startTime,
    this.endTime,
    this.fromHistory = false,
  }) : super(key: key);

  String _formatDuration(int seconds) {
    final minutes = seconds ~/ 60;
    final remainingSeconds = seconds % 60;
    return '$minutes분 ${remainingSeconds}초';
  }

  String _formatDistance(double meters) {
    final kilometers = meters / 1000;
    return '${kilometers.toStringAsFixed(2)}km';
  }

  String _getFullImageUrl(String relativeUrl) {
    if (relativeUrl.isEmpty) return '';
    // baseUrl에서 '/api' 부분을 제거하여 서버의 기본 URL만 가져옴
    final serverBaseUrl = AppConstants.baseUrl.replaceAll('/api', '');
    // 상대 경로가 이미 '/'로 시작하면 그대로 사용, 아니면 '/'를 추가
    final normalizedPath = relativeUrl.startsWith('/') ? relativeUrl : '/$relativeUrl';
    return '$serverBaseUrl$normalizedPath';
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

  String _getWeekdayKor(int weekday) {
    const days = ['월', '화', '수', '목', '금', '토', '일'];
    return days[(weekday - 1) % 7];
  }

  @override
  Widget build(BuildContext context) {
    final fullImageUrl = _getFullImageUrl(imageUrl);
    
    return Scaffold(
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [
              Colors.blue.shade50,
              Colors.white,
            ],
          ),
        ),
        child: SafeArea(
          child: Padding(
            padding: const EdgeInsets.all(20.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                const SizedBox(height: 20),
                Text(
                  '산책 완료!',
                  style: TextStyle(
                    fontSize: 28,
                    fontWeight: FontWeight.bold,
                    color: Colors.blue.shade700,
                  ),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 30),
                // 산책 경로 이미지
                Container(
                  height: 300,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(15),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.grey.withOpacity(0.3),
                        spreadRadius: 2,
                        blurRadius: 5,
                        offset: const Offset(0, 3),
                      ),
                    ],
                  ),
                  child: ClipRRect(
                    borderRadius: BorderRadius.circular(15),
                    child: Image.network(
                      fullImageUrl,
                      fit: BoxFit.cover,
                      errorBuilder: (context, error, stackTrace) {
                        print('Image load error: $error'); // 디버깅을 위한 에러 출력
                        return Container(
                          color: Colors.grey.shade200,
                          child: Center(
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                const Text('이미지를 불러올 수 없습니다'),
                                const SizedBox(height: 8),
                                Text('URL: $fullImageUrl', // 디버깅을 위한 URL 출력
                                    style: const TextStyle(fontSize: 10),
                                    textAlign: TextAlign.center),
                              ],
                            ),
                          ),
                        );
                      },
                    ),
                  ),
                ),
                const SizedBox(height: 30),
                // 시작/종료 시간 표시
                if (startTime != null && endTime != null) ...[
                  Container(
                    margin: const EdgeInsets.only(bottom: 18),
                    padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 18),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(12),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.grey.withOpacity(0.12),
                          blurRadius: 4,
                          offset: const Offset(0, 2),
                        ),
                      ],
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text('시작 시간:  ${_formatDateTime(startTime)}', style: const TextStyle(fontSize: 15, color: Colors.black87)),
                        const SizedBox(height: 4),
                        Text('종료 시간:  ${_formatDateTime(endTime)}', style: const TextStyle(fontSize: 15, color: Colors.black87)),
                      ],
                    ),
                  ),
                ],
                // 산책 정보 카드
                Container(
                  padding: const EdgeInsets.all(20),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(15),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.grey.withOpacity(0.2),
                        spreadRadius: 2,
                        blurRadius: 5,
                        offset: const Offset(0, 3),
                      ),
                    ],
                  ),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      _buildInfoItem('소요 시간', _formatDuration(duration)),
                      Container(
                        width: 1,
                        height: 40,
                        color: Colors.grey.shade300,
                      ),
                      _buildInfoItem('이동 거리', _formatDistance(distance)),
                    ],
                  ),
                ),
                const Spacer(),
                // 버튼
                ElevatedButton(
                  onPressed: () {
                    if (fromHistory) {
                      Navigator.of(context).pop();
                    } else {
                      Navigator.of(context).popUntil((route) => route.isFirst);
                    }
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.blue.shade600,
                    padding: const EdgeInsets.symmetric(vertical: 15),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10),
                    ),
                  ),
                  child: Text(
                    fromHistory ? '목록으로 돌아가기' : '메인 화면으로',
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                      color: Colors.white,
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildInfoItem(String label, String value) {
    return Column(
      children: [
        Text(
          label,
          style: TextStyle(
            fontSize: 14,
            color: Colors.grey.shade600,
          ),
        ),
        const SizedBox(height: 5),
        Text(
          value,
          style: const TextStyle(
            fontSize: 20,
            fontWeight: FontWeight.bold,
          ),
        ),
      ],
    );
  }
} 