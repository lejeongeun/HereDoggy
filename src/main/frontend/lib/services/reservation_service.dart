import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/member_reservation_response_dto.dart';
import '../utils/constants.dart';
import 'auth_service.dart';

class ReservationService {
  final AuthService _authService = AuthService();

  Future<MemberReservationResponseDTO> getReservationDetail(int reservationId) async {
    final token = await _authService.getAccessToken();
    final response = await http.get(
      Uri.parse('${AppConstants.baseUrl}/members/reservations/$reservationId'),
      headers: {'Authorization': 'Bearer $token'},
    );
    if (response.statusCode == 200) {
      return MemberReservationResponseDTO.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
    } else {
      throw Exception('Failed to fetch reservation detail: \\${response.statusCode}');
    }
  }
} 