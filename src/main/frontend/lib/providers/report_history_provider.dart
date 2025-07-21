import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:uuid/uuid.dart';

class ReportHistory {
  final String id;
  final String targetType; // 'free', 'missing', 'review'
  final int targetId;
  final String targetTitle;
  final String reportReason;
  final String reportContent;
  final DateTime reportedAt;

  ReportHistory({
    required this.id,
    required this.targetType,
    required this.targetId,
    required this.targetTitle,
    required this.reportReason,
    required this.reportContent,
    required this.reportedAt,
  });

  factory ReportHistory.fromJson(Map<String, dynamic> json) {
    return ReportHistory(
      id: json['id'],
      targetType: json['targetType'],
      targetId: json['targetId'],
      targetTitle: json['targetTitle'],
      reportReason: json['reportReason'],
      reportContent: json['reportContent'],
      reportedAt: DateTime.parse(json['reportedAt']),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'targetType': targetType,
      'targetId': targetId,
      'targetTitle': targetTitle,
      'reportReason': reportReason,
      'reportContent': reportContent,
      'reportedAt': reportedAt.toIso8601String(),
    };
  }
}

class ReportHistoryProvider with ChangeNotifier {
  static const String _storageKey = 'report_histories';
  List<ReportHistory> _histories = [];
  bool _loaded = false;

  List<ReportHistory> get histories => List.unmodifiable(_histories);

  Future<void> loadHistories() async {
    if (_loaded) return;
    final prefs = await SharedPreferences.getInstance();
    final jsonString = prefs.getString(_storageKey);
    if (jsonString != null) {
      final List<dynamic> jsonList = json.decode(jsonString);
      _histories = jsonList.map((e) => ReportHistory.fromJson(e)).toList();
    }
    _loaded = true;
    notifyListeners();
  }

  Future<void> addReport({
    required String targetType,
    required int targetId,
    required String targetTitle,
    required String reportReason,
    required String reportContent,
  }) async {
    final newReport = ReportHistory(
      id: const Uuid().v4(),
      targetType: targetType,
      targetId: targetId,
      targetTitle: targetTitle,
      reportReason: reportReason,
      reportContent: reportContent,
      reportedAt: DateTime.now(),
    );
    _histories.insert(0, newReport);
    await _saveToPrefs();
    notifyListeners();
  }

  Future<void> removeReport(String id) async {
    _histories.removeWhere((h) => h.id == id);
    await _saveToPrefs();
    notifyListeners();
  }

  ReportHistory? getReportById(String id) {
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