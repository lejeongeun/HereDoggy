import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../utils/constants.dart';
import 'package:flutter_web_auth/flutter_web_auth.dart';

class AuthService {
  final _storage = const FlutterSecureStorage();
  final _baseUrl = AppConstants.baseUrl;

  // 토큰 저장 - 키 통일
  Future<void> saveTokens(String accessToken, String refreshToken) async {
    await _storage.write(key: AppConstants.tokenKey, value: accessToken);
    await _storage.write(key: 'refreshToken', value: refreshToken);
  }

  // 토큰 가져오기 - 키 통일
  Future<String?> getAccessToken() async {
    return await _storage.read(key: AppConstants.tokenKey);
  }

  // 토큰 삭제 (로그아웃) - 키 통일
  Future<void> deleteTokens() async {
    await _storage.delete(key: AppConstants.tokenKey);
    await _storage.delete(key: 'refreshToken');
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

  // 카카오 로그인 - 인가 코드 방식으로 변경
  Future<Map<String, dynamic>> loginWithKakao() async {
    try {
      print('카카오 로그인 시작 (인가 코드 방식)');
      
      // 카카오 인가 URL 생성
      final authUrl = 'https://kauth.kakao.com/oauth/authorize'
          '?client_id=f672fc97c767b2220faed59a97064398' // 실제 카카오 REST API 키로 교체 필요
          '&redirect_uri=heredoggy://callback'
          '&response_type=code';

      // 브라우저로 인증 진행
      final result = await FlutterWebAuth.authenticate(
        url: authUrl,
        callbackUrlScheme: 'heredoggy',
      );

      // 인가 코드 추출
      final code = Uri.parse(result).queryParameters['code'];
      
      if (code == null) {
        return {
          'success': false,
          'message': '인가 코드를 받아오지 못했습니다.',
        };
      }

      print('카카오 인가 코드 받음: $code');

      // 백엔드에 인가 코드 전달
      final response = await http.post(
        Uri.parse('$_baseUrl/auth/oauth/kakao'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'code': code}),
      );

      print('카카오 로그인 응답 상태: ${response.statusCode}');
      print('카카오 로그인 응답 내용: ${response.body}');

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await saveTokens(data['accessToken'], data['refreshToken']);
        return {'success': true};
      } else {
        // 에러 응답 처리
        String errorMessage;
        try {
          final data = jsonDecode(response.body);
          errorMessage = data['message'] ?? '카카오 로그인에 실패했습니다.';
        } catch (e) {
          errorMessage = response.body;
        }
        
        // 신규 사용자 체크
        if (response.statusCode == 404 || errorMessage.contains('회원가입이 필요합니다')) {
          return {
            'success': false,
            'message': '회원가입이 필요합니다',
            'isNewUser': true
          };
        }
        
        return {
          'success': false,
          'message': errorMessage,
        };
      }
    } catch (e) {
      print('카카오 로그인 에러: $e');
      return {
        'success': false,
        'message': '카카오 로그인 중 오류가 발생했습니다.',
      };
    }
  }

  // 구글 로그인 - 인가 코드 방식으로 변경
  Future<Map<String, dynamic>> loginWithGoogle() async {
    try {
      print('구글 로그인 시작 (인가 코드 방식)');
      
      // 구글 인가 URL 생성
      final authUrl = 'https://accounts.google.com/o/oauth2/v2/auth'
          '?client_id=728754467180-df7u27sojli1h5g5ofdlrpb9lqco96lq.apps.googleusercontent.com'
          '&redirect_uri=heredoggy://callback'
          '&response_type=code'
          '&scope=openid%20email%20profile';

      // 브라우저로 인증 진행
      final result = await FlutterWebAuth.authenticate(
        url: authUrl,
        callbackUrlScheme: 'heredoggy',
      );

      // 인가 코드 추출
      final code = Uri.parse(result).queryParameters['code'];
      
      if (code == null) {
        return {
          'success': false,
          'message': '인가 코드를 받아오지 못했습니다.',
        };
      }

      print('구글 인가 코드 받음: $code');

      // 백엔드에 인가 코드 전달
      final response = await http.post(
        Uri.parse('$_baseUrl/auth/oauth/google'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'code': code}),
      );
      
      print('구글 로그인 응답 상태: ${response.statusCode}');
      print('구글 로그인 응답 내용: ${response.body}');

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await saveTokens(data['accessToken'], data['refreshToken']);
        return {'success': true};
      } else {
        // 에러 응답 처리
        String errorMessage;
        try {
          final data = jsonDecode(response.body);
          errorMessage = data['message'] ?? '구글 로그인에 실패했습니다.';
        } catch (e) {
          errorMessage = response.body;
        }
        
        // 신규 사용자 체크
        if (response.statusCode == 404 || errorMessage.contains('회원가입이 필요합니다')) {
          return {
            'success': false,
            'message': '회원가입이 필요합니다',
            'isNewUser': true
          };
        }
        
        return {
          'success': false,
          'message': errorMessage,
        };
      }
    } catch (e) {
      print('구글 로그인 에러: $e');
      return {
        'success': false,
        'message': '구글 로그인 중 오류가 발생했습니다.',
      };
    }
  }

  // 기존 방식 (호환성을 위해 유지, 나중에 삭제 예정)
  @Deprecated('Use loginWithKakao() instead')
  Future<Map<String, dynamic>> loginWithKakaoToken(String kakaoToken) async {
    try {
      print('카카오 로그인 시도 (기존 방식): $_baseUrl/auth/oauth/kakao');
      
      final response = await http.post(
        Uri.parse('$_baseUrl/auth/oauth/kakao'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'token': kakaoToken,
        }),
      );

      print('카카오 로그인 응답 상태: ${response.statusCode}');
      print('카카오 로그인 응답 내용: ${response.body}');

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await saveTokens(data['accessToken'], data['refreshToken']);
        return {'success': true};
      }

      return {
        'success': false,
        'message': '카카오 로그인에 실패했습니다.',
      };
    } catch (e) {
      print('카카오 로그인 에러: $e');
      return {
        'success': false,
        'message': '서버 연결에 실패했습니다.',
      };
    }
  }

  @Deprecated('Use loginWithGoogle() instead')
  Future<Map<String, dynamic>> loginWithGoogleToken(String idToken) async {
    try {
      print('구글 로그인 시도 (기존 방식): $_baseUrl/auth/oauth/google');
      print('구글 idToken: $idToken');
      
      final response = await http.post(
        Uri.parse('$_baseUrl/auth/oauth/google'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'token': idToken}),
      );
      
      print('구글 로그인 응답 상태: ${response.statusCode}');
      print('구글 로그인 응답 내용: ${response.body}');

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await saveTokens(data['accessToken'], data['refreshToken']);
        return {'success': true};
      } else {
        String errorMessage;
        try {
          final data = jsonDecode(response.body);
          errorMessage = data['message'] ?? '구글 로그인에 실패했습니다.';
        } catch (e) {
          errorMessage = response.body;
        }
        
        if (response.statusCode == 404 || errorMessage.contains('회원가입이 필요합니다')) {
          return {
            'success': false,
            'message': '회원가입이 필요합니다',
            'isNewUser': true
          };
        }
        
        return {
          'success': false,
          'message': errorMessage,
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