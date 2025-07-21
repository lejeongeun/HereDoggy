import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/report_history_provider.dart';

class ReportHistoryDetailPage extends StatefulWidget {
  final String reportId;
  
  const ReportHistoryDetailPage({Key? key, required this.reportId}) : super(key: key);

  @override
  State<ReportHistoryDetailPage> createState() => _ReportHistoryDetailPageState();
}

class _ReportHistoryDetailPageState extends State<ReportHistoryDetailPage> {
  @override
  void initState() {
    super.initState();
    // 신고내역 로드
    Future.microtask(() {
      Provider.of<ReportHistoryProvider>(context, listen: false).loadHistories();
    });
  }

  @override
  Widget build(BuildContext context) {
    final Color primary = const Color(0xFF22C55E);
    return Scaffold(
      appBar: AppBar(
        title: const Text('신고 상세', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: primary,
        iconTheme: const IconThemeData(color: Colors.white),
        elevation: 0,
      ),
      body: Consumer<ReportHistoryProvider>(
        builder: (context, provider, _) {
          final report = provider.getReportById(widget.reportId);
          if (report == null) {
            return const Center(child: Text('신고를 찾을 수 없습니다.'));
          }
          
          return SingleChildScrollView(
            padding: const EdgeInsets.all(20),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _buildInfoCard(
                  title: '신고 대상',
                  content: report.targetTitle,
                  icon: Icons.article,
                ),
                const SizedBox(height: 16),
                _buildInfoCard(
                  title: '게시판',
                  content: _getBoardTypeText(report.targetType),
                  icon: Icons.category,
                ),
                const SizedBox(height: 16),
                _buildInfoCard(
                  title: '신고 사유',
                  content: report.reportReason,
                  icon: Icons.report_problem,
                ),
                const SizedBox(height: 16),
                _buildInfoCard(
                  title: '신고일',
                  content: _formatDate(report.reportedAt),
                  icon: Icons.calendar_today,
                ),
                const SizedBox(height: 16),
                if (report.reportContent.isNotEmpty)
                  _buildInfoCard(
                    title: '신고 내용',
                    content: report.reportContent,
                    icon: Icons.message,
                    isMultiline: true,
                  ),
                if (report.reportContent.isNotEmpty)
                  const SizedBox(height: 16),
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

  String _getBoardTypeText(String boardType) {
    switch (boardType) {
      case 'free':
        return '자유게시판';
      case 'missing':
        return '실종/발견';
      case 'review':
        return '입양/산책 후기';
      default:
        return '기타';
    }
  }

  String _formatDate(DateTime date) {
    return '${date.year}-${date.month.toString().padLeft(2, '0')}-${date.day.toString().padLeft(2, '0')}';
  }
} 