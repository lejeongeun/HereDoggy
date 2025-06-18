class MemberReservationResponseDTO {
  final int id;
  final String shelterName;
  final String dogName;

  MemberReservationResponseDTO({
    required this.id,
    required this.shelterName,
    required this.dogName,
  });

  factory MemberReservationResponseDTO.fromJson(Map<String, dynamic> json) {
    return MemberReservationResponseDTO(
      id: json['id'] as int,
      shelterName: json['shelterName'] as String,
      dogName: json['dogName'] as String,
    );
  }
} 