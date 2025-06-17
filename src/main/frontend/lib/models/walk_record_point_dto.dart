import 'package:flutter/foundation.dart';

class WalkRecordPointDTO {
  final double latitude;
  final double longitude;
  final String recordedAt;

  WalkRecordPointDTO({
    required this.latitude,
    required this.longitude,
    required this.recordedAt,
  });

  Map<String, dynamic> toJson() => {
    'latitude': latitude,
    'longitude': longitude,
    'recordedAt': recordedAt,
  };

  factory WalkRecordPointDTO.fromJson(Map<String, dynamic> json) {
    return WalkRecordPointDTO(
      latitude: json['latitude'] as double,
      longitude: json['longitude'] as double,
      recordedAt: json['recordedAt'] as String,
    );
  }
} 