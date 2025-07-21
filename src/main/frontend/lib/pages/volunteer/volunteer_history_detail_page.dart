import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/volunteer_history_provider.dart';

class VolunteerHistoryDetailPage extends StatelessWidget {
  final String historyId;
  const VolunteerHistoryDetailPage({Key? key, required this.historyId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final provider = Provider.of<VolunteerHistoryProvider>(context);
    final history = provider.getHistoryById(historyId);
    if (history == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('봉사 내역 상세')),
        body: const Center(child: Text('내역을 찾을 수 없습니다.')),
      );
    }
    return Scaffold(
      appBar: AppBar(
        title: const Text('봉사 내역 상세', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: Colors.green,
        iconTheme: const IconThemeData(color: Colors.white),
        elevation: 0,
      ),
      body: ListView(
        padding: const EdgeInsets.all(20),
        children: [
          _infoRow('보호소명', history.shelterName),
          _infoRow('봉사일', _formatDateTime(history.date)),
          _infoRow('시간', history.timeSlot),
          _infoRow('메모', history.memo),
        ],
      ),
    );
  }

  Widget _infoRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(width: 110, child: Text(label, style: const TextStyle(fontWeight: FontWeight.bold))),
          Expanded(child: Text(value)),
        ],
      ),
    );
  }

  String _formatDateTime(DateTime date) {
    return '${date.year}-${date.month.toString().padLeft(2, '0')}-${date.day.toString().padLeft(2, '0')}';
  }
} 