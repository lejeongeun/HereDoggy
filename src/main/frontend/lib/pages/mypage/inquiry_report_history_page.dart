import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/inquiry_history_provider.dart';
import '../../providers/report_history_provider.dart';
import 'inquiry_history_detail_page.dart';
import 'report_history_detail_page.dart';

class InquiryReportHistoryPage extends StatefulWidget {
  const InquiryReportHistoryPage({Key? key}) : super(key: key);

  @override
  State<InquiryReportHistoryPage> createState() => _InquiryReportHistoryPageState();
}

class _InquiryReportHistoryPageState extends State<InquiryReportHistoryPage>
    with SingleTickerProviderStateMixin {
  late TabController _tabController;
  final Color primary = const Color(0xFF22C55E);

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
    
    // 문의내역과 신고내역 로드
    Future.microtask(() {
      Provider.of<InquiryHistoryProvider>(context, listen: false).loadHistories();
      Provider.of<ReportHistoryProvider>(context, listen: false).loadHistories();
    });
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('문의 & 신고내역', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: primary,
        iconTheme: const IconThemeData(color: Colors.white),
        elevation: 0,
        bottom: TabBar(
          controller: _tabController,
          indicatorColor: Colors.white,
          labelColor: Colors.white,
          unselectedLabelColor: Colors.white70,
          tabs: const [
            Tab(text: '문의내역'),
            Tab(text: '신고내역'),
          ],
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: [
          _buildInquiryHistoryTab(),
          _buildReportHistoryTab(),
        ],
      ),
    );
  }

  Widget _buildInquiryHistoryTab() {
    return Consumer<InquiryHistoryProvider>(
      builder: (context, provider, _) {
        final histories = provider.histories;
        if (histories.isEmpty) {
          return const Center(child: Text('문의내역이 없습니다.'));
        }
        return ListView.separated(
          padding: const EdgeInsets.symmetric(vertical: 20, horizontal: 16),
          itemCount: histories.length,
          separatorBuilder: (_, __) => const SizedBox(height: 18),
          itemBuilder: (context, idx) {
            final h = histories[idx];
            return _buildInquiryCard(h);
          },
        );
      },
    );
  }

  Widget _buildReportHistoryTab() {
    return Consumer<ReportHistoryProvider>(
      builder: (context, provider, _) {
        final histories = provider.histories;
        if (histories.isEmpty) {
          return const Center(child: Text('신고내역이 없습니다.'));
        }
        return ListView.separated(
          padding: const EdgeInsets.symmetric(vertical: 20, horizontal: 16),
          itemCount: histories.length,
          separatorBuilder: (_, __) => const SizedBox(height: 18),
          itemBuilder: (context, idx) {
            final h = histories[idx];
            return _buildReportCard(h);
          },
        );
      },
    );
  }

  Widget _buildInquiryCard(InquiryHistory inquiry) {
    final Color cardBg = const Color(0xFFF7F8FA);
    final Color border = const Color(0xFFE5E8EB);
    
    return Material(
      color: Colors.transparent,
      elevation: 0,
      child: InkWell(
        borderRadius: BorderRadius.circular(18),
        onTap: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (_) => InquiryHistoryDetailPage(inquiryId: inquiry.id),
            ),
          );
        },
        child: Container(
          decoration: BoxDecoration(
            color: cardBg,
            borderRadius: BorderRadius.circular(18),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withOpacity(0.06),
                blurRadius: 12,
                offset: const Offset(0, 4),
              ),
            ],
            border: Border.all(color: border, width: 1),
          ),
          padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Icon(Icons.question_answer, color: primary, size: 22),
                  const SizedBox(width: 8),
                  Text(
                    inquiry.target,
                    style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 17, color: Colors.black),
                  ),
                ],
              ),
              const SizedBox(height: 14),
              Row(
                children: [
                  Icon(Icons.calendar_today, size: 18, color: Colors.grey[600]),
                  const SizedBox(width: 6),
                  Text('문의일 ', style: TextStyle(fontSize: 14, color: Colors.grey[700], fontWeight: FontWeight.w500)),
                  Text(_formatDate(inquiry.submittedAt), style: const TextStyle(fontSize: 14, color: Colors.black)),
                ],
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  Icon(Icons.message, size: 18, color: Colors.grey[600]),
                  const SizedBox(width: 6),
                  Text('내용 ', style: TextStyle(fontSize: 14, color: Colors.grey[700], fontWeight: FontWeight.w500)),
                  Expanded(
                    child: Text(
                      inquiry.content.length > 30 ? '${inquiry.content.substring(0, 30)}...' : inquiry.content,
                      style: const TextStyle(fontSize: 14, color: Colors.black),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildReportCard(ReportHistory report) {
    final Color cardBg = const Color(0xFFF7F8FA);
    final Color border = const Color(0xFFE5E8EB);
    
    return Material(
      color: Colors.transparent,
      elevation: 0,
      child: InkWell(
        borderRadius: BorderRadius.circular(18),
        onTap: () {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (_) => ReportHistoryDetailPage(reportId: report.id),
            ),
          );
        },
        child: Container(
          decoration: BoxDecoration(
            color: cardBg,
            borderRadius: BorderRadius.circular(18),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withOpacity(0.06),
                blurRadius: 12,
                offset: const Offset(0, 4),
              ),
            ],
            border: Border.all(color: border, width: 1),
          ),
          padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Icon(Icons.report_problem, color: Colors.red[600], size: 22),
                  const SizedBox(width: 8),
                  Expanded(
                    child: Text(
                      report.targetTitle,
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 17, color: Colors.black),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 14),
              Row(
                children: [
                  Icon(Icons.category, size: 18, color: Colors.grey[600]),
                  const SizedBox(width: 6),
                  Text('게시판 ', style: TextStyle(fontSize: 14, color: Colors.grey[700], fontWeight: FontWeight.w500)),
                  Text(_getBoardTypeText(report.targetType), style: const TextStyle(fontSize: 14, color: Colors.black)),
                ],
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  Icon(Icons.report, size: 18, color: Colors.grey[600]),
                  const SizedBox(width: 6),
                  Text('신고사유 ', style: TextStyle(fontSize: 14, color: Colors.grey[700], fontWeight: FontWeight.w500)),
                  Expanded(
                    child: Text(
                      report.reportReason,
                      style: const TextStyle(fontSize: 14, color: Colors.black),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  Icon(Icons.calendar_today, size: 18, color: Colors.grey[600]),
                  const SizedBox(width: 6),
                  Text('신고일 ', style: TextStyle(fontSize: 14, color: Colors.grey[700], fontWeight: FontWeight.w500)),
                  Text(_formatDate(report.reportedAt), style: const TextStyle(fontSize: 14, color: Colors.black)),
                ],
              ),
            ],
          ),
        ),
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