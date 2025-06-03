class WalkOption {
  final int walkOptionId;
  final DateTime date;
  final String startTime;
  final String endTime;

  WalkOption({
    required this.walkOptionId,
    required this.date,
    required this.startTime,
    required this.endTime,
  });

  factory WalkOption.fromJson(Map<String, dynamic> json) {
    return WalkOption(
      walkOptionId: json['walkOptionId'],
      date: DateTime.parse(json['date']),
      startTime: json['startTime'],
      endTime: json['endTime'],
    );
  }
} 