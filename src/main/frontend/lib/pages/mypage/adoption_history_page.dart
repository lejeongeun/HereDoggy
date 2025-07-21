import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/adoption_history_provider.dart';
import 'adoption_history_detail_page.dart';

class AdoptionHistoryPage extends StatelessWidget {
  const AdoptionHistoryPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final Color cardBg = const Color(0xFFF7F8FA);
    final Color border = const Color(0xFFE5E8EB);
    final Color primary = const Color(0xFFFF9800);
    return Scaffold(
      appBar: AppBar(
        title: const Text('입양신청 내역', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: primary,
        iconTheme: const IconThemeData(color: Colors.white),
        elevation: 0,
      ),
      body: Consumer<AdoptionHistoryProvider>(
        builder: (context, provider, _) {
          final histories = provider.histories;
          if (histories.isEmpty) {
            return const Center(child: Text('입양신청 내역이 없습니다.'));
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
                        builder: (_) => AdoptionHistoryDetailPage(historyId: h.id),
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
                            Icon(Icons.pets, color: primary, size: 22),
                            const SizedBox(width: 8),
                            Text(
                              h.applicantName,
                              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 17, color: Colors.black),
                            ),
                            const SizedBox(width: 6),
                            Text('(${h.dogName})', style: TextStyle(fontSize: 15, color: Colors.grey[700], fontWeight: FontWeight.w500)),
                          ],
                        ),
                        const SizedBox(height: 14),
                        Row(
                          children: [
                            Icon(Icons.calendar_today, size: 18, color: Colors.grey[600]),
                            const SizedBox(width: 6),
                            Text('신청일 ', style: TextStyle(fontSize: 14, color: Colors.grey[700], fontWeight: FontWeight.w500)),
                            Text(_formatDate(h.appliedAt), style: const TextStyle(fontSize: 14, color: Colors.black)),
                          ],
                        ),
                        const SizedBox(height: 8),
                        Row(
                          children: [
                            Icon(Icons.phone, size: 18, color: Colors.grey[600]),
                            const SizedBox(width: 6),
                            Text('연락처 ', style: TextStyle(fontSize: 14, color: Colors.grey[700], fontWeight: FontWeight.w500)),
                            Text(h.contact, style: const TextStyle(fontSize: 14, color: Colors.black)),
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