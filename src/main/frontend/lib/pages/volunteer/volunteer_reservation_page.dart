import 'package:flutter/material.dart';
import 'shelter_intro_page.dart';

enum ReservationType { walk, volunteer }

class _ReservationDropdown extends StatelessWidget {
  final ReservationType selected;
  final ValueChanged<ReservationType> onChanged;
  const _ReservationDropdown({Key? key, required this.selected, required this.onChanged}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return DropdownButtonHideUnderline(
      child: DropdownButton<ReservationType>(
        value: selected,
        icon: const Icon(Icons.arrow_drop_down, color: Colors.white),
        dropdownColor: const Color(0xFF4CAF50),
        style: const TextStyle(color: Colors.white, fontSize: 20, fontWeight: FontWeight.bold),
        items: const [
          DropdownMenuItem(
            value: ReservationType.walk,
            child: Text('산책 & 체험 예약', style: TextStyle(color: Colors.white)),
          ),
          DropdownMenuItem(
            value: ReservationType.volunteer,
            child: Text('봉사 예약', style: TextStyle(color: Colors.white)),
          ),
        ],
        onChanged: (value) {
          if (value != null && value != selected) {
            onChanged(value);
          }
        },
      ),
    );
  }
}

class VolunteerReservationPage extends StatelessWidget {
  VolunteerReservationPage({Key? key}) : super(key: key);

  final List<Map<String, String>> shelters = const [
    {
      'name': '서초동물사랑센터',
      'address': '서울시 서초구 역삼로 33-2길',
      'phone': '02-281-2312',
      'description': '서초구 지역의 유기동물을 보호하고 새로운 가족을 찾아주는 동물사랑센터입니다.'
    },
    {
      'name': '서울동물복지지원센터 마포센터',
      'address': '서울시 마포구 장승배기로27길',
      'phone': '02-342-7958',
      'description': '마포구 지역의 동물복지 향상을 위해 노력하는 전문 보호센터입니다.'
    },
    {
      'name': '강동 리본(Re:born)센터',
      'address': '서울시 강동구 가마산로 245',
      'phone': '02-342-7958',
      'description': '강동구의 유기동물들에게 새로운 삶의 기회를 제공하는 리본센터입니다.'
    },
    {
      'name': '양천동물입양센터',
      'address': '서울시 양천구 목동동로 151',
      'phone': '02-902-1297',
      'description': '양천구 지역의 동물입양을 전문으로 하는 동물보호센터입니다.'
    },
    {
      'name': '나비야사랑해 동물보호소',
      'address': '서울시 용산구 후암로 51',
      'phone': '02-311-4212',
      'description': '용산구에 위치한 나비야사랑해 동물보호소는 유기동물에게 따뜻한 보금자리를 제공합니다.'
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF5F6FA),
      appBar: AppBar(
        title: _ReservationDropdown(
          selected: ReservationType.volunteer,
          onChanged: (type) {
            if (type == ReservationType.walk) {
              Navigator.pushReplacementNamed(context, '/walk-reservation');
            }
          },
        ),
        backgroundColor: const Color(0xFF4CAF50),
        elevation: 0,
        foregroundColor: Colors.white,
      ),
      body: ListView.separated(
        padding: const EdgeInsets.all(16),
        itemCount: shelters.length,
        separatorBuilder: (context, index) => const SizedBox(height: 12),
        itemBuilder: (context, index) {
          final shelter = shelters[index];
          return Card(
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
            elevation: 2,
            child: InkWell(
              borderRadius: BorderRadius.circular(16),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => ShelterIntroPage(shelter: shelter),
                  ),
                );
              },
              child: Padding(
                padding: const EdgeInsets.all(20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      shelter['name'] ?? '',
                      style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      shelter['address'] ?? '',
                      style: const TextStyle(fontSize: 16, color: Colors.black87),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      'Tel) ${shelter['phone'] ?? ''}',
                      style: const TextStyle(fontSize: 15, color: Colors.grey),
                    ),
                  ],
                ),
              ),
            ),
          );
        },
      ),
    );
  }
} 