import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:uuid/uuid.dart';

class AdoptionHistory {
  final String id;
  final String applicantName;
  final String contact;
  final String dogId;
  final String dogName;
  final DateTime appliedAt;
  // 상세 정보 필드
  final Map<String, dynamic> details;

  AdoptionHistory({
    required this.id,
    required this.applicantName,
    required this.contact,
    required this.dogId,
    required this.dogName,
    required this.appliedAt,
    required this.details,
  });

  factory AdoptionHistory.fromJson(Map<String, dynamic> json) {
    return AdoptionHistory(
      id: json['id'],
      applicantName: json['applicantName'],
      contact: json['contact'],
      dogId: json['dogId'],
      dogName: json['dogName'],
      appliedAt: DateTime.parse(json['appliedAt']),
      details: Map<String, dynamic>.from(json['details'] ?? {}),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'applicantName': applicantName,
      'contact': contact,
      'dogId': dogId,
      'dogName': dogName,
      'appliedAt': appliedAt.toIso8601String(),
      'details': details,
    };
  }
}

class AdoptionHistoryProvider with ChangeNotifier {
  static const String _storageKey = 'adoption_histories';
  List<AdoptionHistory> _histories = [];
  bool _loaded = false;

  List<AdoptionHistory> get histories => List.unmodifiable(_histories);

  Future<void> loadHistories() async {
    if (_loaded) return;
    final prefs = await SharedPreferences.getInstance();
    final jsonString = prefs.getString(_storageKey);
    if (jsonString != null) {
      final List<dynamic> jsonList = json.decode(jsonString);
      _histories = jsonList.map((e) => AdoptionHistory.fromJson(e)).toList();
    }
    _loaded = true;
    notifyListeners();
  }

  Future<void> addHistory({
    required String applicantName,
    required String contact,
    required String dogId,
    required String dogName,
    required Map<String, dynamic> details,
  }) async {
    final newHistory = AdoptionHistory(
      id: const Uuid().v4(),
      applicantName: applicantName,
      contact: contact,
      dogId: dogId,
      dogName: dogName,
      appliedAt: DateTime.now(),
      details: details,
    );
    _histories.insert(0, newHistory);
    await _saveToPrefs();
    notifyListeners();
  }

  Future<void> removeHistory(String id) async {
    _histories.removeWhere((h) => h.id == id);
    await _saveToPrefs();
    notifyListeners();
  }

  AdoptionHistory? getHistoryById(String id) {
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