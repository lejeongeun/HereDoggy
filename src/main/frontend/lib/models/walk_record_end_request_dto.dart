import 'package:flutter/foundation.dart';
import 'walk_record_point_dto.dart';

class WalkRecordEndRequestDTO {
  final double actualDistance;
  final int actualDuration;
  final List<WalkRecordPointDTO> actualPath;

  WalkRecordEndRequestDTO({
    required this.actualDistance,
    required this.actualDuration,
    required this.actualPath,
  });

  Map<String, dynamic> toJson() => {
    'actualDistance': actualDistance,
    'actualDuration': actualDuration,
    'actualPath': actualPath.map((x) => x.toJson()).toList(),
  };
} 