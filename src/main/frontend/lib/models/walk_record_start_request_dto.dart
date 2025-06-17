import 'package:flutter/foundation.dart';

class WalkRecordStartRequestDTO {
  final int reservationId;
  final int walkRouteId;

  WalkRecordStartRequestDTO({
    required this.reservationId,
    required this.walkRouteId,
  });

  Map<String, dynamic> toJson() => {
    'reservationId': reservationId,
    'walkRouteId': walkRouteId,
  };
} 