import 'package:flutter/foundation.dart';

enum MissingPostType { missing, found }
enum DogGender { male, female, unknown }

class MissingPost {
  final int id;
  final MissingPostType type;
  final String title;
  final DogGender gender;
  final int? age;
  final double? weight;
  final String? furColor;
  final String? feature;
  final DateTime missingDate;
  final String missingLocation;
  final String description;
  final bool isContactPublic;
  final int viewCount;
  final DateTime createdAt;
  final DateTime updatedAt;
  final String writerNickname;
  final String writerEmail;
  final List<String> imagesUrls;

  MissingPost({
    required this.id,
    required this.type,
    required this.title,
    required this.gender,
    this.age,
    this.weight,
    this.furColor,
    this.feature,
    required this.missingDate,
    required this.missingLocation,
    required this.description,
    required this.isContactPublic,
    this.viewCount = 0,
    DateTime? createdAt,
    DateTime? updatedAt,
    this.writerNickname = '',
    this.writerEmail = '',
    this.imagesUrls = const [],
  })  : createdAt = createdAt ?? DateTime.now(),
        updatedAt = updatedAt ?? DateTime.now();

  factory MissingPost.fromJson(Map<String, dynamic> json) {
    return MissingPost(
      id: json['id'],
      type: json['type'] == 'MISSING' ? MissingPostType.missing : MissingPostType.found,
      title: json['title'],
      gender: _parseGender(json['gender']),
      age: json['age'],
      weight: (json['weight'] != null) ? (json['weight'] as num).toDouble() : null,
      furColor: json['furColor'],
      feature: json['feature'],
      missingDate: DateTime.parse(json['missingDate']),
      missingLocation: json['missingLocation'],
      description: json['description'],
      isContactPublic: json['isContactPublic'] ?? false,
      imagesUrls: (json['imageUrls'] as List<dynamic>?)?.map((e) => e as String).toList() ?? [],
    );
  }

  static DogGender _parseGender(String? value) {
    switch (value) {
      case 'MALE':
        return DogGender.male;
      case 'FEMALE':
        return DogGender.female;
      default:
        return DogGender.unknown;
    }
  }
} 