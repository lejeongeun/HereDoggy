import 'package:flutter/material.dart';
import 'package:table_calendar/table_calendar.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'walk_reservation_complete_page.dart';
import '../../services/auth_service.dart';

class WalkTimeSelectPage extends StatefulWidget {
  final int dogId;
  const WalkTimeSelectPage({Key? key, required this.dogId}) : super(key: key);

  @override
  State<WalkTimeSelectPage> createState() => _WalkTimeSelectPageState();
}

class _WalkTimeSelectPageState extends State<WalkTimeSelectPage> {
  DateTime _selectedDay = DateTime.now();
  String? _selectedTimeSlot;
  String _memo = '';
  List<DateTime> _unavailableDates = [];
  bool _isLoading = true;
  String? _error;
  final _authService = AuthService();

  final Map<String, List<String>> _timeSlots = {
    '오전': ['10:00-11:00', '11:00-12:00'],
    '오후': ['14:00-15:00', '15:00-16:00', '16:00-17:00'],
  };

  @override
  void initState() {
    super.initState();
    _fetchUnavailableDates();
  }

  Future<void> _fetchUnavailableDates() async {
    try {
      final token = await _authService.getAccessToken();
      if (token == null) {
        setState(() {
          _error = '로그인이 필요합니다.';
          _isLoading = false;
        });
        return;
      }

      final response = await http.get(
        Uri.parse('http://10.0.2.2:8080/api/reservations/dogs/${widget.dogId}/unavailable-dates'),
        headers: {
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        final List<dynamic> dates = json.decode(utf8.decode(response.bodyBytes));
        setState(() {
          _unavailableDates = dates.map((date) => DateTime.parse(date)).toList();
          _isLoading = false;
        });
      } else {
        setState(() {
          _error = '예약 불가능 날짜를 불러오는데 실패했습니다.';
          _isLoading = false;
        });
      }
    } catch (e) {
      setState(() {
        _error = '네트워크 오류가 발생했습니다.';
        _isLoading = false;
      });
    }
  }

  bool _isDateUnavailable(DateTime date) {
    return _unavailableDates.any((unavailableDate) =>
        unavailableDate.year == date.year &&
        unavailableDate.month == date.month &&
        unavailableDate.day == date.day);
  }

  Future<void> _submitReservation() async {
    if (_selectedTimeSlot == null) return;

    final token = await _authService.getAccessToken();
    if (token == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('로그인이 필요합니다.'),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }

    final timeParts = _selectedTimeSlot!.split('-');
    final startTime = timeParts[0].trim();
    final endTime = timeParts[1].trim();

    final requestBody = {
      'date': '${_selectedDay.year}-${_selectedDay.month.toString().padLeft(2, '0')}-${_selectedDay.day.toString().padLeft(2, '0')}',
      'startTime': startTime,
      'endTime': endTime,
      'note': _memo,
    };

    try {
      final response = await http.post(
        Uri.parse('http://10.0.2.2:8080/api/reservations/dogs/${widget.dogId}/reservationsRequest'),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
        body: json.encode(requestBody),
      );

      if (response.statusCode == 200) {
        if (!mounted) return;
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => const WalkReservationCompletePage()),
        );
      } else {
        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('예약 신청에 실패했습니다: ${response.body}'),
            backgroundColor: Colors.red,
          ),
        );
      }
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('네트워크 오류가 발생했습니다.'),
          backgroundColor: Colors.red,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_isLoading) {
      return const Scaffold(
        body: Center(child: CircularProgressIndicator()),
      );
    }

    if (_error != null) {
      return Scaffold(
        appBar: AppBar(
          leading: IconButton(
            icon: const Icon(Icons.arrow_back),
            onPressed: () => Navigator.pop(context),
          ),
          title: const Text('산책 예약'),
          centerTitle: true,
        ),
        body: Center(child: Text(_error!)),
      );
    }

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
                  if (!_isDateUnavailable(selectedDay)) {
                    setState(() {
                      _selectedDay = selectedDay;
                    });
                  }
                },
                calendarStyle: CalendarStyle(
                  selectedDecoration: const BoxDecoration(
                    color: Colors.green,
                    shape: BoxShape.circle,
                  ),
                  todayDecoration: const BoxDecoration(
                    color: Colors.greenAccent,
                    shape: BoxShape.circle,
                  ),
                  disabledDecoration: BoxDecoration(
                    color: Colors.grey[300],
                    shape: BoxShape.circle,
                  ),
                ),
                calendarBuilders: CalendarBuilders(
                  disabledBuilder: (context, date, _) {
                    return Center(
                      child: Text(
                        '${date.day}',
                        style: TextStyle(color: Colors.grey[400]),
                      ),
                    );
                  },
                ),
                enabledDayPredicate: (day) => !_isDateUnavailable(day),
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

              // 메모 입력 필드 추가
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

              // 예약하기 버튼
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