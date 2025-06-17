import 'package:flutter/foundation.dart';
import 'walk_record_point_dto.dart';

class WalkRecordResponseDTO {
  final int id;
  final int reservationId;
  final int walkRouteId;
  final double? actualDistance;
  final int? actualDuration;
  final String? startTime;
  final String? endTime;
  final List<WalkRecordPointDTO>? actualPath;
  final String status;
  final String? thumbnailUrl;

  WalkRecordResponseDTO({
    required this.id,
    required this.reservationId,
    required this.walkRouteId,
    this.actualDistance,
    this.actualDuration,
    this.startTime,
    this.endTime,
    this.actualPath,
    required this.status,
    this.thumbnailUrl,
  });

  factory WalkRecordResponseDTO.fromJson(Map<String, dynamic> json) {
    return WalkRecordResponseDTO(
      id: json['id'] as int,
      reservationId: json['reservationId'] as int,
      walkRouteId: json['walkRouteId'] as int,
      actualDistance: json['actualDistance'] as double?,
      actualDuration: json['actualDuration'] as int?,
      startTime: json['startTime'] as String?,
      endTime: json['endTime'] as String?,
      actualPath: json['actualPath'] != null
          ? List<WalkRecordPointDTO>.from(
              (json['actualPath'] as List).map(
                (x) => WalkRecordPointDTO.fromJson(x as Map<String, dynamic>),
              ),
            )
          : null,
      status: json['status'] as String,
      thumbnailUrl: json['thumbnailUrl'] as String?,
    );
  }
} 