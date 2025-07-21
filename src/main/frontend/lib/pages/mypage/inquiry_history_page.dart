import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/inquiry_history_provider.dart';
import 'inquiry_history_detail_page.dart';

class InquiryHistoryPage extends StatefulWidget {
  const InquiryHistoryPage({Key? key}) : super(key: key);

  @override
  State<InquiryHistoryPage> createState() => _InquiryHistoryPageState();
}

class _InquiryHistoryPageState extends State<InquiryHistoryPage> {
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
    final Color cardBg = const Color(0xFFF7F8FA);
    final Color border = const Color(0xFFE5E8EB);
    final Color primary = const Color(0xFF22C55E);
    return Scaffold(
      appBar: AppBar(
        title: const Text('문의내역', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: primary,
        iconTheme: const IconThemeData(color: Colors.white),
        elevation: 0,
      ),
      body: Consumer<InquiryHistoryProvider>(
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
              return Material(
                color: Colors.transparent,
                elevation: 0,
                child: InkWell(
                  borderRadius: BorderRadius.circular(18),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => InquiryHistoryDetailPage(inquiryId: h.id),
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
                              h.target,
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
                            Text(_formatDate(h.submittedAt), style: const TextStyle(fontSize: 14, color: Colors.black)),
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
                                h.content.length > 30 ? '${h.content.substring(0, 30)}...' : h.content,
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
            },
          );
        },
      ),
    );
  }

  String _formatDate(DateTime date) {
    return '${date.year}-${date.month.toString().padLeft(2, '0')}-${date.day.toString().padLeft(2, '0')}';
  }
} 