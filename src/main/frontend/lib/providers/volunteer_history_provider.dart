import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:uuid/uuid.dart';

class VolunteerHistory {
  final String id;
  final String shelterName;
  final DateTime date;
  final String timeSlot;
  final String memo;

  VolunteerHistory({
    required this.id,
    required this.shelterName,
    required this.date,
    required this.timeSlot,
    required this.memo,
  });

  factory VolunteerHistory.fromJson(Map<String, dynamic> json) {
    return VolunteerHistory(
      id: json['id'],
      shelterName: json['shelterName'],
      date: DateTime.parse(json['date']),
      timeSlot: json['timeSlot'],
      memo: json['memo'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'shelterName': shelterName,
      'date': date.toIso8601String(),
      'timeSlot': timeSlot,
      'memo': memo,
    };
  }
}

class VolunteerHistoryProvider with ChangeNotifier {
  static const String _storageKey = 'volunteer_histories';
  List<VolunteerHistory> _histories = [];
  bool _loaded = false;

  List<VolunteerHistory> get histories => List.unmodifiable(_histories);

  Future<void> loadHistories() async {
    if (_loaded) return;
    final prefs = await SharedPreferences.getInstance();
    final jsonString = prefs.getString(_storageKey);
    if (jsonString != null) {
      final List<dynamic> jsonList = json.decode(jsonString);
      _histories = jsonList.map((e) => VolunteerHistory.fromJson(e)).toList();
    }
    _loaded = true;
    notifyListeners();
  }

  Future<void> addHistory({
    required String shelterName,
    required DateTime date,
    required String timeSlot,
    required String memo,
  }) async {
    final newHistory = VolunteerHistory(
      id: const Uuid().v4(),
      shelterName: shelterName,
      date: date,
      timeSlot: timeSlot,
      memo: memo,
    );
    _histories.insert(0, newHistory);
    await _saveToPrefs();
    notifyListeners();
  }

  VolunteerHistory? getHistoryById(String id) {
    try {
      return _histories.firstWhere((h) => h.id == id);
    } catch (_) {
      return null;
    }
  }

  Future<void> _saveToPrefs() async {
    final prefs = await SharedPreferences.getInstance();
    final jsonString = json.encode(_histories.map((e) => e.toJson()).toList());
    await prefs.setString(_storageKey, jsonString);
  }
} 