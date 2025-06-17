import 'package:dio/dio.dart';
import '../models/comment.dart';
import '../utils/constants.dart';
import '../services/auth_service.dart';

class CommentApi {
  static final Dio _dio = Dio(
    BaseOptions(
      baseUrl: AppConstants.baseUrl,
      connectTimeout: const Duration(seconds: 5),
      receiveTimeout: const Duration(seconds: 5),
      contentType: Headers.jsonContentType,
      responseType: ResponseType.json,
    ),
  );

  // 댓글 목록 조회
  static Future<List<Comment>> fetchComments(String postType, int postId) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();

      final response = await _dio.get(
        '/$postType-posts/$postId/comments',
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );

      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        return data.map((json) => Comment.fromJson(json)).toList();
      } else {
        throw Exception('서버 오류: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('댓글 목록을 불러오지 못했습니다: $e');
    }
  }

  // 댓글 작성
  static Future<void> createComment(String postType, int postId, String content) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();

      final response = await _dio.post(
        '/$postType-posts/$postId/comments',
        data: {'content': content},
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );

      if (response.statusCode != 200 && response.statusCode != 201) {
        throw Exception('댓글 작성 실패: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('댓글 작성 중 오류가 발생했습니다: $e');
    }
  }

  // 댓글 수정
  static Future<void> editComment(int commentId, String content) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();

      final response = await _dio.put(
        '/comments/$commentId',
        data: {'content': content},
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );

      if (response.statusCode != 200) {
        throw Exception('댓글 수정 실패: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('댓글 수정 중 오류가 발생했습니다: $e');
    }
  }

  // 댓글 삭제
  static Future<void> deleteComment(int commentId) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();

      final response = await _dio.delete(
        '/comments/$commentId',
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );

      if (response.statusCode != 200) {
        throw Exception('댓글 삭제 실패: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('댓글 삭제 중 오류가 발생했습니다: $e');
    }
  }
} 