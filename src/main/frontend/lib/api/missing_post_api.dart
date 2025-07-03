import 'package:dio/dio.dart';
import '../models/missing_post.dart';
import '../utils/constants.dart';
import '../services/auth_service.dart';
import 'dart:convert';
import 'like_api.dart';

class MissingPostApi {
  static final Dio _dio = Dio(
    BaseOptions(
      baseUrl: AppConstants.baseUrl,
      connectTimeout: const Duration(seconds: 5),
      receiveTimeout: const Duration(seconds: 5),
      contentType: Headers.jsonContentType,
      responseType: ResponseType.json,
    ),
  );

  // 실종/발견 게시글 목록 조회 (type: null이면 전체)
  static Future<List<MissingPost>> fetchMissingPosts({String? type}) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final response = await _dio.get(
        '/members/missing-posts',
        queryParameters: type != null ? {'type': type} : null,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );
      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        return data.map((json) => MissingPost.fromJson(json)).toList();
      } else {
        throw Exception('서버 오류: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('실종/발견 게시글 목록을 불러오지 못했습니다: $e');
    }
  }

  // 실종/발견 게시글 상세 조회
  static Future<MissingPost> fetchMissingPostDetail(int postId) async {
    try {
      final response = await _dio.get('/members/missing-posts/$postId');
      if (response.statusCode == 200) {
        return MissingPost.fromJson(response.data);
      } else {
        throw Exception('서버 오류: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('실종/발견 게시글을 불러오지 못했습니다: $e');
    }
  }

  // 실종/발견 게시글 작성
  static Future<void> createMissingPost(Map<String, dynamic> data, List<MultipartFile> images) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final formData = FormData.fromMap({
        'info': jsonEncode(data),
        if (images.isNotEmpty) 'images': images,
      });
      final response = await _dio.post(
        '/members/missing-posts',
        data: formData,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
            'Content-Type': 'multipart/form-data',
          },
        ),
      );
      if (response.statusCode != 200 && response.statusCode != 201) {
        throw Exception('게시글 작성 실패: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('게시글 작성 중 오류가 발생했습니다: $e');
    }
  }

  // 실종/발견 게시글 수정
  static Future<void> editMissingPost(int postId, Map<String, dynamic> data, List<MultipartFile> images) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final formData = FormData.fromMap({
        ...data,
        if (images.isNotEmpty) 'images': images,
      });
      final response = await _dio.put(
        '/missing-posts/$postId',
        data: formData,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
            'Content-Type': 'multipart/form-data',
          },
        ),
      );
      if (response.statusCode != 200) {
        throw Exception('게시글 수정 실패: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('게시글 수정 중 오류가 발생했습니다: $e');
    }
  }

  // 실종/발견 게시글 삭제
  static Future<void> deleteMissingPost(int postId) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final response = await _dio.delete(
        '/missing-posts/$postId',
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );
      if (response.statusCode != 200) {
        throw Exception('게시글 삭제 실패: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('게시글 삭제 중 오류가 발생했습니다: $e');
    }
  }

  // 좋아요 토글
  static Future<bool> toggleLike(int postId) async {
    return await LikeApi.toggleLike('missing', postId);
  }

  // 좋아요 개수 조회
  static Future<int> getLikeCount(int postId) async {
    return await LikeApi.getLikeCount('missing', postId);
  }

  // 게시글 상세 조회 시 좋아요 정보 포함
  static Future<MissingPost> fetchMissingPostDetailWithLike(int postId) async {
    try {
      final post = await fetchMissingPostDetail(postId);
      final likeCount = await getLikeCount(postId);
      
      // 좋아요 개수만 업데이트 (isLiked는 토글 시에만 업데이트)
      return post.copyWith(likeCount: likeCount);
    } catch (e) {
      throw Exception('실종/발견 게시글 상세 정보를 불러오지 못했습니다: $e');
    }
  }
} 