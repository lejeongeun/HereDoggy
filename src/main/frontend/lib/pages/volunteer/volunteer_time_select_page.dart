import 'package:flutter/material.dart';
import 'volunteer_reservation_complete_page.dart';
import '../../providers/volunteer_history_provider.dart';
import 'package:provider/provider.dart';

class VolunteerTimeSelectPage extends StatefulWidget {
  final Map<String, String> shelter;
  const VolunteerTimeSelectPage({Key? key, required this.shelter}) : super(key: key);

  @override
  State<VolunteerTimeSelectPage> createState() => _VolunteerTimeSelectPageState();
}

class _VolunteerTimeSelectPageState extends State<VolunteerTimeSelectPage> {
  DateTime _selectedDay = DateTime.now();
  String? _selectedTimeSlot;
  String _memo = '';

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
        title: const Text('봉사 예약'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
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
              _buildCalendar(),
              const SizedBox(height: 24),
              _buildTimeSlotSelection(),
              const SizedBox(height: 16),
              const Text(
                '메모',
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 8),
              TextField(
                decoration: const InputDecoration(
                  hintText: '메모를 입력해주세요',
                  border: OutlineInputBorder(),
                  contentPadding: EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                ),
                maxLines: 1,
                onChanged: (value) {
                  setState(() {
                    _memo = value;
                  });
                },
              ),
              const SizedBox(height: 24),
              SizedBox(
                width: double.infinity,
                height: 48,
                child: ElevatedButton(
                  onPressed: _selectedTimeSlot != null ? _submitReservation : null,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.green[300],
                    foregroundColor: Colors.black,
                    disabledBackgroundColor: Colors.grey[300],
                    disabledForegroundColor: Colors.grey[600],
                  ),
                  child: const Text(
                    '봉사 예약하기',
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

  Widget _buildCalendar() {
    return CalendarDatePicker(
      initialDate: _selectedDay,
      firstDate: DateTime.now(),
      lastDate: DateTime.now().add(const Duration(days: 30)),
      onDateChanged: (date) {
        setState(() {
          _selectedDay = date;
          _selectedTimeSlot = null;
        });
      },
      currentDate: DateTime.now(),
      selectableDayPredicate: (_) => true, // 모든 날짜 선택 가능
    );
  }

  Widget _buildTimeSlotSelection() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: _timeSlots.entries.map((entry) {
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
              children: entry.value.map((timeSlot) {
                return ChoiceChip(
                  label: Text(timeSlot),
                  selected: _selectedTimeSlot == timeSlot,
                  onSelected: (selected) {
                    if (selected) {
                      setState(() {
                        _selectedTimeSlot = timeSlot;
                      });
                    }
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
    );
  }

  void _submitReservation() {
    final shelterName = widget.shelter['name'] ?? '';
    final date = _selectedDay;
    final timeSlot = _selectedTimeSlot ?? '';
    final memo = _memo;
    Provider.of<VolunteerHistoryProvider>(context, listen: false).addHistory(
      shelterName: shelterName,
      date: date,
      timeSlot: timeSlot,
      memo: memo,
    );
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(
        builder: (context) => const VolunteerReservationCompletePage(),
      ),
    );
  }
} 