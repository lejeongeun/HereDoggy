import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:uuid/uuid.dart';

class InquiryHistory {
  final String id;
  final String target;
  final String content;
  final DateTime submittedAt;

  InquiryHistory({
    required this.id,
    required this.target,
    required this.content,
    required this.submittedAt,
  });

  factory InquiryHistory.fromJson(Map<String, dynamic> json) {
    return InquiryHistory(
      id: json['id'],
      target: json['target'],
      content: json['content'],
      submittedAt: DateTime.parse(json['submittedAt']),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'target': target,
      'content': content,
      'submittedAt': submittedAt.toIso8601String(),
    };
  }
}

class InquiryHistoryProvider with ChangeNotifier {
  static const String _storageKey = 'inquiry_histories';
  List<InquiryHistory> _histories = [];
  bool _loaded = false;

  List<InquiryHistory> get histories => List.unmodifiable(_histories);

  Future<void> loadHistories() async {
    if (_loaded) return;
    final prefs = await SharedPreferences.getInstance();
    final jsonString = prefs.getString(_storageKey);
    if (jsonString != null) {
      final List<dynamic> jsonList = json.decode(jsonString);
      _histories = jsonList.map((e) => InquiryHistory.fromJson(e)).toList();
    }
    _loaded = true;
    notifyListeners();
  }

  Future<void> addInquiry({
    required String target,
    required String content,
  }) async {
    final newInquiry = InquiryHistory(
      id: const Uuid().v4(),
      target: target,
      content: content,
      submittedAt: DateTime.now(),
    );
    _histories.insert(0, newInquiry);
    await _saveToPrefs();
    notifyListeners();
  }

  Future<void> removeInquiry(String id) async {
    _histories.removeWhere((h) => h.id == id);
    await _saveToPrefs();
    notifyListeners();
  }

  InquiryHistory? getInquiryById(String id) {
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