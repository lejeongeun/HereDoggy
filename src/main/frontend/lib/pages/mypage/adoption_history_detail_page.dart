import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/adoption_history_provider.dart';

class AdoptionHistoryDetailPage extends StatelessWidget {
  final String historyId;
  const AdoptionHistoryDetailPage({Key? key, required this.historyId}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final provider = Provider.of<AdoptionHistoryProvider>(context);
    final history = provider.getHistoryById(historyId);
    if (history == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('입양신청 상세')),
        body: const Center(child: Text('내역을 찾을 수 없습니다.')),
      );
    }
    return Scaffold(
      appBar: AppBar(
        title: const Text('입양신청 상세', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: const Color(0xFFFF9800),
        iconTheme: const IconThemeData(color: Colors.white),
        elevation: 0,
      ),
      body: ListView(
        padding: const EdgeInsets.all(20),
        children: [
          _infoRow('신청자', history.applicantName),
          _infoRow('연락처', history.contact),
          _infoRow('반려견 이름', history.dogName),
          _infoRow('신청일', _formatDateTime(history.appliedAt)),
          const SizedBox(height: 20),
          const Text('상세 정보', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18)),
          const Divider(),
          ...history.details.entries.map((e) => _infoRow(_fieldKor(e.key), e.value?.toString() ?? '')),
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
    return '${date.year}-${date.month.toString().padLeft(2, '0')}-${date.day.toString().padLeft(2, '0')} ${date.hour.toString().padLeft(2, '0')}:${date.minute.toString().padLeft(2, '0')}';
  }

  String _fieldKor(String key) {
    switch (key) {
      case 'birth': return '생년월일';
      case 'gender': return '성별';
      case 'address': return '주소';
      case 'job': return '직업';
      case 'maritalStatus': return '결혼여부';
      case 'hasPetsBefore': return '반려동물 키운 경험';
      case 'prevPet': return '이전 반려동물';
      case 'hasCurrentPets': return '현재 반려동물';
      case 'currentPet': return '현재 키우는 동물';
      case 'family': return '가족 구성원';
      case 'familyAgree': return '가족 찬성여부';
      case 'reason': return '입양 사유';
      case 'canProvideProperCare': return '소식 제공 가능';
      case 'canKeepUntilEnd': return '끝까지 책임';
      case 'agreeToRegularChecks': return '불임수술 동의';
      case 'agreeToAdoptionTerms': return '입양 책임 동의';
      default: return key;
    }
  }
} 