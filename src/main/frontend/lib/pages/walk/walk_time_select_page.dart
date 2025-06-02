import 'package:flutter/material.dart';
import 'package:table_calendar/table_calendar.dart';

class WalkTimeSelectPage extends StatefulWidget {
  final int dogId;
  const WalkTimeSelectPage({Key? key, required this.dogId}) : super(key: key);

  @override
  State<WalkTimeSelectPage> createState() => _WalkTimeSelectPageState();
}

class _WalkTimeSelectPageState extends State<WalkTimeSelectPage> {
  DateTime _selectedDay = DateTime.now();
  String? _selectedTimeSlot;
  final Map<String, List<String>> _timeSlots = {
    '오전': ['10:00-11:00', '11:00-12:00'],
    '오후': ['14:00-15:00', '15:00-16:00', '16:00-17:00'],
  };

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
        title: const Text('산책 예약'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // 달력 아이콘과 안내 텍스트
              Row(
                children: [
                  const Icon(Icons.calendar_today, size: 24),
                  const SizedBox(width: 8),
                  const Text(
                    '날짜와 시간을 선택해 주세요',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 24),

              // 캘린더
              TableCalendar(
                firstDay: DateTime.now(),
                lastDay: DateTime.now().add(const Duration(days: 30)),
                focusedDay: _selectedDay,
                selectedDayPredicate: (day) {
                  return isSameDay(_selectedDay, day);
                },
                onDaySelected: (selectedDay, focusedDay) {
                  setState(() {
                    _selectedDay = selectedDay;
                  });
                },
                calendarStyle: const CalendarStyle(
                  selectedDecoration: BoxDecoration(
                    color: Colors.green,
                    shape: BoxShape.circle,
                  ),
                  todayDecoration: BoxDecoration(
                    color: Colors.greenAccent,
                    shape: BoxShape.circle,
                  ),
                ),
              ),
              const SizedBox(height: 24),

              // 시간대 선택
              ..._timeSlots.entries.map((entry) {
                return Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      entry.key,
                      style: const TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Wrap(
                      spacing: 8,
                      children: entry.value.map((time) {
                        final isSelected = _selectedTimeSlot == time;
                        return ChoiceChip(
                          label: Text(time),
                          selected: isSelected,
                          onSelected: (selected) {
                            setState(() {
                              _selectedTimeSlot = selected ? time : null;
                            });
                          },
                          backgroundColor: Colors.grey[200],
                          selectedColor: Colors.green[100],
                        );
                      }).toList(),
                    ),
                    const SizedBox(height: 16),
                  ],
                );
              }).toList(),

              const SizedBox(height: 24),

              // 예약하기 버튼
              SizedBox(
                width: double.infinity,
                height: 48,
                child: ElevatedButton(
                  onPressed: _selectedTimeSlot != null
                      ? () {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(
                              content: Text('예약이 완료되었습니다.'),
                            ),
                          );
                        }
                      : null,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.green[300],
                    foregroundColor: Colors.black,
                    disabledBackgroundColor: Colors.grey[300],
                    disabledForegroundColor: Colors.grey[600],
                  ),
                  child: const Text(
                    '산책 예약하기',
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