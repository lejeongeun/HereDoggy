import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/inquiry_history_provider.dart';

class InquiryPage extends StatefulWidget {
  const InquiryPage({Key? key}) : super(key: key);

  @override
  State<InquiryPage> createState() => _InquiryPageState();
}

class _InquiryPageState extends State<InquiryPage> {
  final List<String> _targets = ['보호소 1', '보호소 2', '여기보개'];
  String _selectedTarget = '보호소 1';
  final TextEditingController _contentController = TextEditingController();

  @override
  void initState() {
    super.initState();
    // 문의내역 로드
    Future.microtask(() {
      Provider.of<InquiryHistoryProvider>(context, listen: false).loadHistories();
    });
    
    // 텍스트 변경 리스너 추가
    _contentController.addListener(() {
      setState(() {});
    });
  }

  @override
  void dispose() {
    _contentController.dispose();
    super.dispose();
  }

  void _showCompleteDialog() async {
    // Provider에 문의 저장
    await Provider.of<InquiryHistoryProvider>(context, listen: false).addInquiry(
      target: _selectedTarget,
      content: _contentController.text,
    );
    
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const InquiryCompletePage()),
    );
  }

  @override
  Widget build(BuildContext context) {
    final Color brand = const Color(0xFF22C55E);
    return Scaffold(
      appBar: AppBar(
        title: const Text('문의하기', style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
        elevation: 0,
        automaticallyImplyLeading: true,
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // 문의 대상 드롭다운
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              decoration: BoxDecoration(
                color: Colors.grey[100],
                borderRadius: BorderRadius.circular(24),
              ),
              child: DropdownButtonHideUnderline(
                child: DropdownButton<String>(
                  value: _selectedTarget,
                  isExpanded: true,
                  icon: const Icon(Icons.arrow_drop_down, color: Colors.black),
                  style: const TextStyle(fontSize: 16, color: Colors.black),
                  dropdownColor: Colors.white,
                  borderRadius: BorderRadius.circular(24),
                  items: _targets.map((target) => DropdownMenuItem(
                    value: target,
                    child: Text(target),
                  )).toList(),
                  onChanged: (val) {
                    if (val != null) setState(() => _selectedTarget = val);
                  },
                ),
              ),
            ),
            const SizedBox(height: 24),
            // 문의 내용 입력
            Expanded(
              child: TextField(
                controller: _contentController,
                maxLines: null,
                expands: true,
                decoration: InputDecoration(
                  hintText: '발생한 문제나 오류 혹은 개선하고 싶은 점을 알려주세요',
                  filled: true,
                  fillColor: Colors.grey[100],
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(8),
                    borderSide: BorderSide.none,
                  ),
                  contentPadding: const EdgeInsets.all(16),
                ),
                style: const TextStyle(fontSize: 15),
              ),
            ),
            const SizedBox(height: 24),
            Row(
              children: [
                Expanded(
                  child: SizedBox(
                    height: 48,
                    child: ElevatedButton(
                      onPressed: (_selectedTarget.isNotEmpty && _contentController.text.trim().isNotEmpty) 
                          ? _showCompleteDialog 
                          : null,
                      style: ElevatedButton.styleFrom(
                        backgroundColor: (_selectedTarget.isNotEmpty && _contentController.text.trim().isNotEmpty) 
                            ? brand 
                            : Colors.grey[300],
                        foregroundColor: (_selectedTarget.isNotEmpty && _contentController.text.trim().isNotEmpty) 
                            ? Colors.white 
                            : Colors.grey[600],
                        shape: const RoundedRectangleBorder(
                          borderRadius: BorderRadius.only(
                            topLeft: Radius.circular(8),
                            bottomLeft: Radius.circular(8),
                          ),
                        ),
                        elevation: 0,
                      ),
                      child: const Text('제출', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                    ),
                  ),
                ),
                Expanded(
                  child: SizedBox(
                    height: 48,
                    child: ElevatedButton(
                      onPressed: () => Navigator.pop(context),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.grey[300],
                        foregroundColor: Colors.black,
                        shape: const RoundedRectangleBorder(
                          borderRadius: BorderRadius.only(
                            topRight: Radius.circular(8),
                            bottomRight: Radius.circular(8),
                          ),
                        ),
                        elevation: 0,
                      ),
                      child: const Text('취소', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

class InquiryCompletePage extends StatelessWidget {
  const InquiryCompletePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final Color brand = const Color(0xFF22C55E);
    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(
                Icons.check_circle_outline,
                size: 100,
                color: Color(0xFF22C55E),
              ),
              const SizedBox(height: 32),
              const Text(
                '문의가 정상적으로 제출되었습니다!',
                style: TextStyle(
                  fontSize: 22,
                  fontWeight: FontWeight.bold,
                ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 16),
              const Text(
                '빠른 시일 내에 답변드리겠습니다.',
                style: TextStyle(
                  fontSize: 16,
                  color: Colors.grey,
                ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 48),
              SizedBox(
                width: double.infinity,
                height: 48,
                child: ElevatedButton(
                  onPressed: () {
                    Navigator.pushNamedAndRemoveUntil(
                      context,
                      '/',
                      (route) => false,
                    );
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: brand,
                    foregroundColor: Colors.white,
                  ),
                  child: const Text(
                    '메인으로 이동',
                    style: TextStyle(fontSize: 18),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
} 