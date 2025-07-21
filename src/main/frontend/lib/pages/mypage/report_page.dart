import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/report_history_provider.dart';

class ReportPage extends StatefulWidget {
  final String targetType; // 'free', 'missing', 'review'
  final int targetId;
  final String targetTitle;
  
  const ReportPage({
    Key? key, 
    required this.targetType, 
    required this.targetId, 
    required this.targetTitle,
  }) : super(key: key);

  @override
  State<ReportPage> createState() => _ReportPageState();
}

class _ReportPageState extends State<ReportPage> {
  final List<String> _reportReasons = [
    '스팸/홍보성 게시글',
    '음란성/선정적 내용',
    '폭력적/혐오적 내용',
    '개인정보 노출',
    '저작권 침해',
    '기타',
  ];
  String _selectedReason = '스팸/홍보성 게시글';
  final TextEditingController _contentController = TextEditingController();

  @override
  void initState() {
    super.initState();
    // 신고내역 로드
    Future.microtask(() {
      Provider.of<ReportHistoryProvider>(context, listen: false).loadHistories();
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
    // Provider에 신고 저장
    await Provider.of<ReportHistoryProvider>(context, listen: false).addReport(
      targetType: widget.targetType,
      targetId: widget.targetId,
      targetTitle: widget.targetTitle,
      reportReason: _selectedReason,
      reportContent: _contentController.text,
    );
    
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const ReportCompletePage()),
    );
  }

  @override
  Widget build(BuildContext context) {
    final Color brand = const Color(0xFF22C55E);
    return Scaffold(
      appBar: AppBar(
        title: const Text('신고하기', style: TextStyle(fontWeight: FontWeight.bold)),
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
            // 신고 대상 정보
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.grey[100],
                borderRadius: BorderRadius.circular(12),
                border: Border.all(color: Colors.grey[300]!),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    '신고 대상',
                    style: TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.bold,
                      color: Colors.grey,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    widget.targetTitle,
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 24),
            
            // 신고 사유 선택
            const Text(
              '신고 사유',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 12),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              decoration: BoxDecoration(
                color: Colors.grey[100],
                borderRadius: BorderRadius.circular(24),
              ),
              child: DropdownButtonHideUnderline(
                child: DropdownButton<String>(
                  value: _selectedReason,
                  isExpanded: true,
                  icon: const Icon(Icons.arrow_drop_down, color: Colors.black),
                  style: const TextStyle(fontSize: 16, color: Colors.black),
                  dropdownColor: Colors.white,
                  borderRadius: BorderRadius.circular(24),
                  items: _reportReasons.map((reason) => DropdownMenuItem(
                    value: reason,
                    child: Text(reason),
                  )).toList(),
                  onChanged: (val) {
                    if (val != null) setState(() => _selectedReason = val);
                  },
                ),
              ),
            ),
            const SizedBox(height: 24),
            
            // 신고 내용 입력
            const Text(
              '신고 내용 (선택사항)',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 12),
            Expanded(
              child: TextField(
                controller: _contentController,
                maxLines: null,
                expands: true,
                decoration: InputDecoration(
                  hintText: '신고 사유에 대한 추가 설명을 입력해주세요',
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
            
            // 버튼
            Row(
              children: [
                Expanded(
                  child: SizedBox(
                    height: 48,
                    child: ElevatedButton(
                      onPressed: _showCompleteDialog,
                      style: ElevatedButton.styleFrom(
                        backgroundColor: brand,
                        foregroundColor: Colors.white,
                        shape: const RoundedRectangleBorder(
                          borderRadius: BorderRadius.only(
                            topLeft: Radius.circular(8),
                            bottomLeft: Radius.circular(8),
                          ),
                        ),
                        elevation: 0,
                      ),
                      child: const Text('신고하기', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
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

class ReportCompletePage extends StatelessWidget {
  const ReportCompletePage({Key? key}) : super(key: key);

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
                Icons.report_problem_outlined,
                size: 100,
                color: Color(0xFF22C55E),
              ),
              const SizedBox(height: 32),
              const Text(
                '신고가 정상적으로 접수되었습니다!',
                style: TextStyle(
                  fontSize: 22,
                  fontWeight: FontWeight.bold,
                ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 16),
              const Text(
                '검토 후 적절한 조치를 취하겠습니다.',
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
                    Navigator.pop(context);
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: brand,
                    foregroundColor: Colors.white,
                  ),
                  child: const Text(
                    '돌아가기',
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