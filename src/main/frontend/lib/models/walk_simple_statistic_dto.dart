class WalkSimpleStatisticDTO {
  final double totalDistance;
  final int totalDuration;
  final int totalWalkCount;

  WalkSimpleStatisticDTO({
    required this.totalDistance,
    required this.totalDuration,
    required this.totalWalkCount,
  });

  factory WalkSimpleStatisticDTO.fromJson(Map<String, dynamic> json) {
    return WalkSimpleStatisticDTO(
      totalDistance: (json['totalDistance'] as num).toDouble(),
      totalDuration: json['totalDuration'] as int,
      totalWalkCount: (json['totalWalkCount'] as num).toInt(),
    );
  }
} 