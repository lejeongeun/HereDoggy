import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/report_history_provider.dart';
import 'report_history_detail_page.dart';

class ReportHistoryPage extends StatefulWidget {
  const ReportHistoryPage({Key? key}) : super(key: key);

  @override
  State<ReportHistoryPage> createState() => _ReportHistoryPageState();
}

class _ReportHistoryPageState extends State<ReportHistoryPage> {
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
    final Color cardBg = const Color(0xFFF7F8FA);
    final Color border = const Color(0xFFE5E8EB);
    final Color primary = const Color(0xFF22C55E);
    return Scaffold(
      appBar: AppBar(
        title: const Text('신고내역', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: primary,
        iconTheme: const IconThemeData(color: Colors.white),
        elevation: 0,
      ),
      body: Consumer<ReportHistoryProvider>(
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
              return Material(
                color: Colors.transparent,
                elevation: 0,
                child: InkWell(
                  borderRadius: BorderRadius.circular(18),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => ReportHistoryDetailPage(reportId: h.id),
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
                                h.targetTitle,
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
                            Text(_getBoardTypeText(h.targetType), style: const TextStyle(fontSize: 14, color: Colors.black)),
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
                                h.reportReason,
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
                            Text(_formatDate(h.reportedAt), style: const TextStyle(fontSize: 14, color: Colors.black)),
                          ],
                        ),
                      ],
                    ),
                  ),
                ),
              );
            },
          );
        },
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