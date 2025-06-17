import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../utils/constants.dart';

class AuthService {
  final _storage = const FlutterSecureStorage();
  final _baseUrl = AppConstants.baseUrl;

  // 토큰 저장
  Future<void> saveTokens(String accessToken, String refreshToken) async {
    await _storage.write(key: 'access_token', value: accessToken);
  }

  // 토큰 가져오기
  Future<String?> getAccessToken() async {
    return await _storage.read(key: 'access_token');
  }

  // 토큰 삭제 (로그아웃)
  Future<void> deleteTokens() async {
    await _storage.delete(key: 'access_token');
  }

  // 로그인
  Future<Map<String, dynamic>> login(String email, String password) async {
    try {
      final response = await http.post(
        Uri.parse('$_baseUrl/auth/login'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'email': email,
          'password': password,
        }),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await saveTokens(data['accessToken'], '');
        return {'success': true};
      } else {
        return {
          'success': false,
          'message': '로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.'
        };
      }
    } catch (e) {
      return {
        'success': false,
        'message': '네트워크 오류가 발생했습니다. 다시 시도해주세요.'
      };
    }
  }

  // 로그아웃
  Future<void> logout() async {
    try {
      final accessToken = await getAccessToken();
      if (accessToken != null) {
        await http.post(
          Uri.parse('$_baseUrl/auth/logout'),
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer $accessToken',
          },
        );
      }
    } finally {
      await deleteTokens();
    }
  }

  // 회원가입
  Future<Map<String, dynamic>> register({
    required String email,
    String? password,
    String? passwordCheck,
    required String name,
    required String nickname,
    required String birth,
    required String phone,
    required String zipcode,
    required String address1,
    required String address2,
    String provider = 'local',
  }) async {
    try {
      final response = await http.post(
        Uri.parse('$_baseUrl/auth/signup'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'email': email,
          'password': password,
          'passwordCheck': passwordCheck,
          'name': name,
          'nickname': nickname,
          'birth': birth,
          'phone': phone,
          'zipcode': zipcode,
          'address1': address1,
          'address2': address2,
          'provider': provider,
        }),
      );

      if (response.statusCode == 200) {
        return {'success': true, 'message': '회원가입이 완료되었습니다.'};
      } else {
        final data = jsonDecode(response.body);
        return {
          'success': false,
          'message': data['message'] ?? '회원가입에 실패했습니다. 다시 시도해주세요.'
        };
      }
    } catch (e) {
      return {
        'success': false,
        'message': '네트워크 오류가 발생했습니다. 다시 시도해주세요.'
      };
    }
  }

  // 소셜 로그인 회원가입
  Future<Map<String, dynamic>> registerSocial({
    required String email,
    required String name,
    required String nickname,
    required String birth,
    required String phone,
    required String zipcode,
    required String address1,
    required String address2,
    required String provider,
  }) async {
    return register(
      email: email,
      name: name,
      nickname: nickname,
      birth: birth,
      phone: phone,
      zipcode: zipcode,
      address1: address1,
      address2: address2,
      provider: provider,
    );
  }

  // 회원 프로필 정보 가져오기
  Future<Map<String, dynamic>?> getProfile() async {
    final accessToken = await getAccessToken();
    if (accessToken == null) return null;
    final response = await http.get(
      Uri.parse('$_baseUrl/members/profile'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $accessToken',
      },
    );
    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      return null;
    }
  }

  Future<Map<String, dynamic>> loginWithKakao(String kakaoToken) async {
    try {
      print('카카오 로그인 시도: $_baseUrl/auth/oauth/kakao'); // 디버그 로그
      
      final response = await http.post(
        Uri.parse('$_baseUrl/auth/oauth/kakao'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'token': kakaoToken,
        }),
      );

      print('카카오 로그인 응답 상태: ${response.statusCode}'); // 디버그 로그
      print('카카오 로그인 응답 내용: ${response.body}'); // 디버그 로그

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await _storage.write(key: AppConstants.tokenKey, value: data['accessToken']);
        await _storage.write(key: 'refreshToken', value: data['refreshToken']);
        return {'success': true};
      }

      return {
        'success': false,
        'message': '카카오 로그인에 실패했습니다.',
      };
    } catch (e) {
      print('카카오 로그인 에러: $e'); // 디버그 로그
      return {
        'success': false,
        'message': '서버 연결에 실패했습니다.',
      };
    }
  }

  Future<Map<String, dynamic>> loginWithGoogle(String idToken) async {
    try {
      print('구글 로그인 시도: $_baseUrl/auth/oauth/google'); // 디버그 로그
      final response = await http.post(
        Uri.parse('$_baseUrl/auth/oauth/google'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'token': idToken}),
      );
      
      print('구글 로그인 응답 상태: ${response.statusCode}'); // 디버그 로그
      print('구글 로그인 응답 내용: ${response.body}'); // 디버그 로그

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await _storage.write(key: AppConstants.tokenKey, value: data['accessToken']);
        await _storage.write(key: 'refreshToken', value: data['refreshToken']);
        return {'success': true};
      } else {
        final data = jsonDecode(response.body);
        return {
          'success': false,
          'message': data['message'] ?? '구글 로그인에 실패했습니다.',
        };
      }
    } catch (e) {
      print('구글 로그인 에러: $e');
      return {
        'success': false,
        'message': '서버 연결에 실패했습니다.',
      };
    }
  }
} 