import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/inquiry_history_provider.dart';

class InquiryHistoryDetailPage extends StatefulWidget {
  final String inquiryId;
  
  const InquiryHistoryDetailPage({Key? key, required this.inquiryId}) : super(key: key);

  @override
  State<InquiryHistoryDetailPage> createState() => _InquiryHistoryDetailPageState();
}

class _InquiryHistoryDetailPageState extends State<InquiryHistoryDetailPage> {
  @override
  void initState() {
    super.initState();
    // 문의내역 로드
    Future.microtask(() {
      Provider.of<InquiryHistoryProvider>(context, listen: false).loadHistories();
    });
  }

  @override
  Widget build(BuildContext context) {
    final Color primary = const Color(0xFF22C55E);
    return Scaffold(
      appBar: AppBar(
        title: const Text('문의 상세', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: primary,
        iconTheme: const IconThemeData(color: Colors.white),
        elevation: 0,
      ),
      body: Consumer<InquiryHistoryProvider>(
        builder: (context, provider, _) {
          final inquiry = provider.getInquiryById(widget.inquiryId);
          if (inquiry == null) {
            return const Center(child: Text('문의를 찾을 수 없습니다.'));
          }
          
          return SingleChildScrollView(
            padding: const EdgeInsets.all(20),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _buildInfoCard(
                  title: '문의 대상',
                  content: inquiry.target,
                  icon: Icons.question_answer,
                ),
                const SizedBox(height: 16),
                _buildInfoCard(
                  title: '문의일',
                  content: _formatDate(inquiry.submittedAt),
                  icon: Icons.calendar_today,
                ),
                const SizedBox(height: 16),
                _buildInfoCard(
                  title: '문의 내용',
                  content: inquiry.content,
                  icon: Icons.message,
                  isMultiline: true,
                ),
                const SizedBox(height: 32),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: () => Navigator.pop(context),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: primary,
                      foregroundColor: Colors.white,
                      padding: const EdgeInsets.symmetric(vertical: 16),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                    ),
                    child: const Text('돌아가기', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }

  Widget _buildInfoCard({
    required String title,
    required String content,
    required IconData icon,
    bool isMultiline = false,
  }) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: Colors.grey.shade200),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.05),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(icon, color: const Color(0xFF22C55E), size: 20),
              const SizedBox(width: 8),
              Text(
                title,
                style: const TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                  color: Colors.black87,
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),
          Text(
            content,
            style: TextStyle(
              fontSize: 15,
              color: Colors.black87,
              height: isMultiline ? 1.5 : 1.2,
            ),
          ),
        ],
      ),
    );
  }

  String _formatDate(DateTime date) {
    return '${date.year}-${date.month.toString().padLeft(2, '0')}-${date.day.toString().padLeft(2, '0')}';
  }
} 