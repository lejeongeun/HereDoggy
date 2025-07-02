import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../../services/fcm_token_debug_service.dart';
import 'dart:convert';

class FcmTokenDebugPage extends StatefulWidget {
  const FcmTokenDebugPage({Key? key}) : super(key: key);

  @override
  State<FcmTokenDebugPage> createState() => _FcmTokenDebugPageState();
}

class _FcmTokenDebugPageState extends State<FcmTokenDebugPage> {
  final FcmTokenDebugService _debugService = FcmTokenDebugService();
  Map<String, dynamic>? _tokenInfo;
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _loadTokenInfo();
    _debugService.setupTokenRefreshListener();
  }

  Future<void> _loadTokenInfo() async {
    setState(() {
      _isLoading = true;
    });

    try {
      final tokenInfo = await _debugService.getTokenInfo();
      setState(() {
        _tokenInfo = tokenInfo;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _tokenInfo = {'error': e.toString()};
        _isLoading = false;
      });
    }
  }

  void _copyTokenToClipboard() {
    if (_tokenInfo != null && _tokenInfo!['token'] != null) {
      Clipboard.setData(ClipboardData(text: _tokenInfo!['token']));
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('FCM 토큰이 클립보드에 복사되었습니다')),
      );
    }
  }

  void _copyTokenInfoToClipboard() {
    if (_tokenInfo != null) {
      final jsonString = JsonEncoder.withIndent('  ').convert(_tokenInfo);
      Clipboard.setData(ClipboardData(text: jsonString));
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('토큰 정보가 클립보드에 복사되었습니다')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('FCM 토큰 디버그'),
        backgroundColor: Colors.blue,
        foregroundColor: Colors.white,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _loadTokenInfo,
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: _isLoading
            ? const Center(child: CircularProgressIndicator())
            : _tokenInfo == null
                ? const Center(child: Text('토큰 정보를 불러올 수 없습니다'))
                : SingleChildScrollView(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        // 토큰 정보 카드
                        Card(
                          child: Padding(
                            padding: const EdgeInsets.all(16.0),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  children: [
                                    const Text(
                                      'FCM 토큰 정보',
                                      style: TextStyle(
                                        fontSize: 18,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                    Row(
                                      children: [
                                        IconButton(
                                          icon: const Icon(Icons.copy),
                                          onPressed: _copyTokenToClipboard,
                                          tooltip: '토큰만 복사',
                                        ),
                                        IconButton(
                                          icon: const Icon(Icons.content_copy),
                                          onPressed: _copyTokenInfoToClipboard,
                                          tooltip: '전체 정보 복사',
                                        ),
                                      ],
                                    ),
                                  ],
                                ),
                                const SizedBox(height: 16),
                                
                                // FCM 토큰
                                if (_tokenInfo!['token'] != null) ...[
                                  const Text(
                                    'FCM 토큰:',
                                    style: TextStyle(
                                      fontWeight: FontWeight.bold,
                                      color: Colors.blue,
                                    ),
                                  ),
                                  const SizedBox(height: 8),
                                  Container(
                                    width: double.infinity,
                                    padding: const EdgeInsets.all(12),
                                    decoration: BoxDecoration(
                                      color: Colors.grey[100],
                                      borderRadius: BorderRadius.circular(8),
                                      border: Border.all(color: Colors.grey[300]!),
                                    ),
                                    child: SelectableText(
                                      _tokenInfo!['token'],
                                      style: const TextStyle(
                                        fontFamily: 'monospace',
                                        fontSize: 12,
                                      ),
                                    ),
                                  ),
                                  const SizedBox(height: 16),
                                ],

                                // 권한 상태
                                if (_tokenInfo!['authorizationStatus'] != null) ...[
                                  const Text(
                                    '알림 권한 상태:',
                                    style: TextStyle(fontWeight: FontWeight.bold),
                                  ),
                                  const SizedBox(height: 8),
                                  _buildStatusChip(_tokenInfo!['authorizationStatus']),
                                  const SizedBox(height: 16),
                                ],

                                // 상세 정보
                                const Text(
                                  '상세 정보:',
                                  style: TextStyle(fontWeight: FontWeight.bold),
                                ),
                                const SizedBox(height: 8),
                                _buildDetailInfo(),
                                const SizedBox(height: 16),

                                // 타임스탬프
                                if (_tokenInfo!['timestamp'] != null) ...[
                                  const Text(
                                    '생성 시간:',
                                    style: TextStyle(fontWeight: FontWeight.bold),
                                  ),
                                  const SizedBox(height: 8),
                                  Text(_tokenInfo!['timestamp']),
                                ],
                              ],
                            ),
                          ),
                        ),

                        const SizedBox(height: 16),

                        // 사용법 안내
                        Card(
                          child: Padding(
                            padding: const EdgeInsets.all(16.0),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                const Text(
                                  '사용법:',
                                  style: TextStyle(
                                    fontSize: 16,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                                const SizedBox(height: 8),
                                const Text(
                                  '1. 위의 FCM 토큰을 복사합니다\n'
                                  '2. Firebase Console에서 Cloud Messaging으로 이동\n'
                                  '3. "테스트 메시지 전송" 선택\n'
                                  '4. "FCM 등록 토큰 추가" 필드에 토큰을 붙여넣기\n'
                                  '5. 메시지를 작성하고 "테스트" 버튼 클릭',
                                  style: TextStyle(fontSize: 14),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
      ),
    );
  }

  Widget _buildStatusChip(String status) {
    Color color;
    String displayText;

    switch (status) {
      case 'authorized':
        color = Colors.green;
        displayText = '허용됨';
        break;
      case 'denied':
        color = Colors.red;
        displayText = '거부됨';
        break;
      case 'notDetermined':
        color = Colors.orange;
        displayText = '결정되지 않음';
        break;
      case 'provisional':
        color = Colors.blue;
        displayText = '임시 허용';
        break;
      default:
        color = Colors.grey;
        displayText = status;
    }

    return Chip(
      label: Text(
        displayText,
        style: const TextStyle(color: Colors.white),
      ),
      backgroundColor: color,
    );
  }

  Widget _buildDetailInfo() {
    return Column(
      children: [
        _buildInfoRow('Alert 권한', _tokenInfo!['alert']?.toString() ?? 'N/A'),
        _buildInfoRow('Badge 권한', _tokenInfo!['badge']?.toString() ?? 'N/A'),
        _buildInfoRow('Sound 권한', _tokenInfo!['sound']?.toString() ?? 'N/A'),
      ],
    );
  }

  Widget _buildInfoRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(label, style: const TextStyle(fontSize: 14)),
          Text(
            value,
            style: TextStyle(
              fontSize: 14,
              fontWeight: FontWeight.bold,
              color: value == 'true' ? Colors.green : Colors.red,
            ),
          ),
        ],
      ),
    );
  }
} 