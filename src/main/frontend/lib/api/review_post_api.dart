import 'package:dio/dio.dart';
import '../models/review_post.dart';
import '../utils/constants.dart';
import '../services/auth_service.dart';
import 'dart:convert';
import 'package:image_picker/image_picker.dart';
import 'like_api.dart';

class ReviewPostApi {
  static final Dio _dio = Dio(
    BaseOptions(
      baseUrl: AppConstants.baseUrl,
      connectTimeout: const Duration(seconds: 5),
      receiveTimeout: const Duration(seconds: 5),
      contentType: Headers.jsonContentType,
      responseType: ResponseType.json,
    ),
  );

  static Future<List<ReviewPost>> fetchReviewPosts() async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final response = await _dio.get(
        '/members/review-posts',
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );
      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        return data.map((json) => ReviewPost.fromJson(json)).toList();
      } else {
        throw Exception('서버 오류: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('게시글 목록을 불러오지 못했습니다: $e');
    }
  }

  static Future<void> createReviewPost({
    required String title,
    required String content,
    required String type,
    required int rank,
    List<XFile>? images,
  }) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final Map<String, dynamic> formDataMap = {
        'info': jsonEncode({
          'title': title,
          'content': content,
          'type': type,
          'rank': rank,
        }),
      };
      if (images != null && images.isNotEmpty) {
        formDataMap['images'] = await Future.wait(
          images.map((img) => MultipartFile.fromFile(img.path, filename: img.name)),
        );
      }
      final formData = FormData.fromMap(formDataMap);
      final response = await _dio.post(
        '/members/review-posts',
        data: formData,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );
      if (response.statusCode != 200 && response.statusCode != 201) {
        throw Exception('글 작성 실패: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('글 작성 중 오류가 발생했습니다: $e');
    }
  }

  static Future<ReviewPost> fetchReviewPostDetail(int postId) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final response = await _dio.get(
        '/members/review-posts/$postId',
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );
      if (response.statusCode == 200) {
        return ReviewPost.fromJson(response.data);
      } else {
        throw Exception('서버 오류: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('게시글 상세 정보를 불러오지 못했습니다: $e');
    }
  }

  static Future<void> deleteReviewPost(int postId) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final response = await _dio.delete(
        '/members/review-posts/$postId',
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );
      if (response.statusCode != 200) {
        throw Exception('글 삭제 실패: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('글 삭제 중 오류가 발생했습니다: $e');
    }
  }

  static Future<void> editReviewPost({
    required int postId,
    required String title,
    required String content,
    required String type,
    required int rank,
    List<XFile>? images,
    List<int>? deleteImageIds,
  }) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final Map<String, dynamic> formDataMap = {
        'info': jsonEncode({
          'title': title,
          'content': content,
          'type': type,
          'rank': rank,
          'deleteImageIds': deleteImageIds ?? [],
        }),
      };
      if (images != null && images.isNotEmpty) {
        formDataMap['images'] = await Future.wait(
          images.map((img) => MultipartFile.fromFile(img.path, filename: img.name)),
        );
      }
      final formData = FormData.fromMap(formDataMap);
      final response = await _dio.put(
        '/members/review-posts/$postId',
        data: formData,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );
      if (response.statusCode != 200) {
        throw Exception('글 수정 실패: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('글 수정 중 오류가 발생했습니다: $e');
    }
  }

  // 좋아요 토글
  static Future<bool> toggleLike(int postId) async {
    return await LikeApi.toggleLike('review', postId);
  }

  // 좋아요 개수 조회
  static Future<int> getLikeCount(int postId) async {
    return await LikeApi.getLikeCount('review', postId);
  }

  // 게시글 상세 조회 시 좋아요 정보 포함
  static Future<ReviewPost> fetchReviewPostDetailWithLike(int postId) async {
    try {
      final post = await fetchReviewPostDetail(postId);
      final likeCount = await getLikeCount(postId);
      
      // 좋아요 개수만 업데이트 (isLiked는 토글 시에만 업데이트)
      return post.copyWith(likeCount: likeCount);
    } catch (e) {
      throw Exception('리뷰 게시글 상세 정보를 불러오지 못했습니다: $e');
    }
  }
} 