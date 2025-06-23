import 'dart:convert';
import 'package:http/http.dart' as http;
import '../utils/constants.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class ShelterService {
  static const _storage = FlutterSecureStorage();

  // 토큰 가져오기
  static Future<String?> _getToken() async {
    return await _storage.read(key: 'access_token');
  }

  // 보호소 상세 정보 조회
  static Future<Map<String, dynamic>> getShelterDetail(String shelterId) async {
    try {
      final response = await http.get(
        Uri.parse('${AppConstants.baseUrl}/shelters/$shelterId'),
        headers: {'Content-Type': 'application/json'},
      );
      
      if (response.statusCode == 200) {
        print('Shelter detail response: ${response.body}'); // 디버깅용
        return json.decode(utf8.decode(response.bodyBytes));
      } else {
        print('Shelter detail error: ${response.statusCode}, ${response.body}'); // 디버깅용
        throw Exception('Failed to load shelter details');
      }
    } catch (e) {
      print('Shelter detail exception: $e'); // 디버깅용
      throw Exception('Failed to load shelter details: $e');
    }
  }

  // 보호소의 보호 동물 목록 조회 (사용자용 API)
  static Future<List<Map<String, dynamic>>> getShelterDogs(String shelterId) async {
    final response = await http.get(
      Uri.parse('${AppConstants.baseUrl}/dogs/shelters/$shelterId/another-shelters'),
    );
    
    print('Dogs API response status: ${response.statusCode}'); // 디버깅용
    print('Dogs API response body: ${response.body}'); // 디버깅용
    
    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(utf8.decode(response.bodyBytes));
      return data.map((item) => item as Map<String, dynamic>).toList();
    } else {
      throw Exception('Failed to load shelter dogs');
    }
  }
} 