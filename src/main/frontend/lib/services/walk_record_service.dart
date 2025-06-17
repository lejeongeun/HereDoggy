import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/walk_record_response_dto.dart';
import '../models/walk_record_start_request_dto.dart';
import '../models/walk_record_end_request_dto.dart';
import '../utils/constants.dart';
import 'auth_service.dart';

class WalkRecordService {
  final AuthService _authService = AuthService();

  Future<WalkRecordResponseDTO> startWalk(WalkRecordStartRequestDTO request) async {
    final token = await _authService.getAccessToken();
    final response = await http.post(
      Uri.parse('${AppConstants.baseUrl}/walk-records/start'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: jsonEncode(request.toJson()),
    );

    if (response.statusCode == 200) {
      return WalkRecordResponseDTO.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
    } else {
      throw Exception('Failed to start walk: ${response.statusCode}');
    }
  }

  Future<WalkRecordResponseDTO> endWalk(int walkRecordId, WalkRecordEndRequestDTO request) async {
    final token = await _authService.getAccessToken();
    final response = await http.post(
      Uri.parse('${AppConstants.baseUrl}/walk-records/$walkRecordId/end'),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
      },
      body: jsonEncode(request.toJson()),
    );

    if (response.statusCode == 200) {
      return WalkRecordResponseDTO.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
    } else {
      throw Exception('Failed to end walk: ${response.statusCode}');
    }
  }
} 