import 'package:flutter/material.dart';
import 'package:table_calendar/table_calendar.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import '../../models/walk_option.dart';

class WalkTimeSelectPage extends StatefulWidget {
  final int dogId;
  const WalkTimeSelectPage({Key? key, required this.dogId}) : super(key: key);

  @override
  State<WalkTimeSelectPage> createState() => _WalkTimeSelectPageState();
}

class _WalkTimeSelectPageState extends State<WalkTimeSelectPage> {
  DateTime? _selectedDay;
  int? _selectedWalkOptionId;
  String _note = '';
  List<WalkOption> _walkOptions = [];
  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    fetchWalkOptions();
  }

  Future<void> fetchWalkOptions() async {
    setState(() {
      _loading = true;
      _error = null;
    });
    try {
      final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/walk-options/dogs/${widget.dogId}'));
      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(utf8.decode(response.bodyBytes));
        _walkOptions = data.map((e) => WalkOption.fromJson(e)).toList();
        // 기본 선택 날짜: walkOption이 있는 첫 날짜
        if (_walkOptions.isNotEmpty) {
          _selectedDay = _walkOptions.first.date;
        }
      } else {
        _error = '예약 옵션을 불러오지 못했습니다.';
      }
    } catch (e) {
      _error = '네트워크 오류가 발생했습니다.';
    }
    setState(() {
      _loading = false;
    });
  }

  List<DateTime> get _availableDates => _walkOptions.map((e) => e.date).toSet().toList();

  List<WalkOption> get _optionsForSelectedDay =>
      _walkOptions.where((e) => _selectedDay != null && isSameDay(e.date, _selectedDay!)).toList();

  Future<void> _requestReservation() async {
    if (_selectedWalkOptionId == null) return;
    final url = 'http://10.0.2.2:8080/api/reservations/dogs/${widget.dogId}/walk-options/$_selectedWalkOptionId/reservationsRequest';
    try {
      final response = await http.post(
        Uri.parse(url),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'note': _note}),
      );
      if (response.statusCode == 200 || response.statusCode == 201) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('예약이 완료되었습니다.')),
        );
        setState(() {
          _selectedWalkOptionId = null;
          _note = '';
        });
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('예약에 실패했습니다. (${response.statusCode})')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('네트워크 오류가 발생했습니다.')),
      );
    }
  }

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
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Center(child: Text(_error!))
              : SingleChildScrollView(
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
                          firstDay: _walkOptions.isNotEmpty ? _walkOptions.map((e) => e.date).reduce((a, b) => a.isBefore(b) ? a : b) : DateTime.now(),
                          lastDay: _walkOptions.isNotEmpty ? _walkOptions.map((e) => e.date).reduce((a, b) => a.isAfter(b) ? a : b) : DateTime.now().add(const Duration(days: 30)),
                          focusedDay: _selectedDay ?? DateTime.now(),
                          selectedDayPredicate: (day) => _selectedDay != null && isSameDay(_selectedDay!, day),
                          onDaySelected: (selectedDay, focusedDay) {
                            if (_availableDates.any((d) => isSameDay(d, selectedDay))) {
                              setState(() {
                                _selectedDay = selectedDay;
                                _selectedWalkOptionId = null;
                              });
                            }
                          },
                          enabledDayPredicate: (day) => _availableDates.any((d) => isSameDay(d, day)),
                          calendarStyle: const CalendarStyle(
                            selectedDecoration: BoxDecoration(
                              color: Colors.green,
                              shape: BoxShape.circle,
                            ),
                            todayDecoration: BoxDecoration(
                              color: Colors.greenAccent,
                              shape: BoxShape.circle,
                            ),
                            disabledTextStyle: TextStyle(color: Colors.grey),
                          ),
                        ),
                        const SizedBox(height: 24),
                        // 시간대 선택
                        if (_optionsForSelectedDay.isNotEmpty)
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              ..._optionsForSelectedDay.map((option) {
                                final isSelected = _selectedWalkOptionId == option.walkOptionId;
                                return Padding(
                                  padding: const EdgeInsets.only(bottom: 8.0),
                                  child: ChoiceChip(
                                    label: Text('${option.startTime}~${option.endTime}'),
                                    selected: isSelected,
                                    onSelected: (selected) {
                                      setState(() {
                                        _selectedWalkOptionId = selected ? option.walkOptionId : null;
                                      });
                                    },
                                    backgroundColor: Colors.grey[200],
                                    selectedColor: Colors.green[100],
                                  ),
                                );
                              }).toList(),
                            ],
                          )
                        else
                          const Text('이 날짜에는 예약 가능한 시간이 없습니다.'),
                        const SizedBox(height: 24),
                        // 메모 입력란
                        TextField(
                          decoration: const InputDecoration(
                            labelText: '메모',
                            hintText: '산책 메모를 작성해 주세요',
                            border: OutlineInputBorder(),
                          ),
                          maxLines: 1,
                          onChanged: (value) {
                            setState(() {
                              _note = value;
                            });
                          },
                          controller: TextEditingController(text: _note),
                        ),
                        const SizedBox(height: 24),
                        // 예약하기 버튼
                        SizedBox(
                          width: double.infinity,
                          height: 48,
                          child: ElevatedButton(
                            onPressed: _selectedWalkOptionId != null && _note.trim().isNotEmpty
                                ? _requestReservation
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