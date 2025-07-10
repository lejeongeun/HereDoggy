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
  final String nickname;
  final List<String> imagesUrls;
  final int likeCount;
  final bool isLiked;

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
    this.nickname = '',
    this.imagesUrls = const [],
    this.likeCount = 0,
    this.isLiked = false,
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
      viewCount: json['viewCount'] as int? ?? 0,
      createdAt: json['createdAt'] != null ? DateTime.parse(json['createdAt']) : null,
      updatedAt: json['updatedAt'] != null ? DateTime.parse(json['updatedAt']) : null,
      nickname: json['nickname'] as String? ?? '',
      imagesUrls: (json['imageUrls'] as List<dynamic>?)?.map((e) => e as String).toList() ?? [],
      likeCount: json['likeCount'] as int? ?? 0,
      isLiked: json['isLiked'] as bool? ?? false,
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

  // 좋아요 상태를 업데이트하는 메서드
  MissingPost copyWith({
    int? id,
    MissingPostType? type,
    String? title,
    DogGender? gender,
    int? age,
    double? weight,
    String? furColor,
    String? feature,
    DateTime? missingDate,
    String? missingLocation,
    String? description,
    bool? isContactPublic,
    int? viewCount,
    DateTime? createdAt,
    DateTime? updatedAt,
    String? nickname,
    List<String>? imagesUrls,
    int? likeCount,
    bool? isLiked,
  }) {
    return MissingPost(
      id: id ?? this.id,
      type: type ?? this.type,
      title: title ?? this.title,
      gender: gender ?? this.gender,
      age: age ?? this.age,
      weight: weight ?? this.weight,
      furColor: furColor ?? this.furColor,
      feature: feature ?? this.feature,
      missingDate: missingDate ?? this.missingDate,
      missingLocation: missingLocation ?? this.missingLocation,
      description: description ?? this.description,
      isContactPublic: isContactPublic ?? this.isContactPublic,
      viewCount: viewCount ?? this.viewCount,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
      nickname: nickname ?? this.nickname,
      imagesUrls: imagesUrls ?? this.imagesUrls,
      likeCount: likeCount ?? this.likeCount,
      isLiked: isLiked ?? this.isLiked,
    );
  }
} 