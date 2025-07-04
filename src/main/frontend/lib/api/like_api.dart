import 'package:dio/dio.dart';
import '../utils/constants.dart';
import '../services/auth_service.dart';
import '../models/like_status.dart';

class LikeApi {
  static final Dio _dio = Dio(
    BaseOptions(
      baseUrl: AppConstants.baseUrl,
      connectTimeout: const Duration(seconds: 5),
      receiveTimeout: const Duration(seconds: 5),
      contentType: Headers.jsonContentType,
      responseType: ResponseType.json,
    ),
  );

  /// 좋아요 토글 (등록/취소)
  /// 
  /// [postType] 게시글 타입: 'free', 'missing', 'review'
  /// [postId] 게시글 ID
  /// 
  /// 반환값: 좋아요 상태 (true: 좋아요 등록됨, false: 좋아요 취소됨)
  static Future<bool> toggleLike(String postType, int postId) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();

      final response = await _dio.post(
        '/$postType-posts/$postId/likes',
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> data = response.data;
        return data['liked'] as bool;
      } else {
        throw Exception('좋아요 토글 실패: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('좋아요 토글 중 오류가 발생했습니다: $e');
    }
  }

  /// 좋아요 개수 조회
  /// 
  /// [postType] 게시글 타입: 'free', 'missing', 'review'
  /// [postId] 게시글 ID
  /// 
  /// 반환값: 좋아요 개수
  static Future<int> getLikeCount(String postType, int postId) async {
    try {
      final response = await _dio.get('/$postType-posts/$postId/likes/count');

      if (response.statusCode == 200) {
        return response.data as int;
      } else {
        throw Exception('좋아요 개수 조회 실패: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('좋아요 개수 조회 중 오류가 발생했습니다: $e');
    }
  }

  static Future<LikeStatus> fetchLikeStatus(String postType, int postId) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();

      // 좋아요 개수
      final countRes = await _dio.get('/$postType-posts/$postId/likes/count');
      int likeCount = countRes.statusCode == 200 ? countRes.data as int : 0;

      // 좋아요 상태(내가 눌렀는지)
      final statusRes = await _dio.post(
        '/$postType-posts/$postId/likes',
        options: Options(
          headers: {'Authorization': 'Bearer $token'},
        ),
      );
      bool isLiked = statusRes.statusCode == 200
          ? (statusRes.data['liked'] as bool? ?? false)
          : false;

      return LikeStatus(likeCount: likeCount, isLiked: isLiked);
    } catch (e) {
      return LikeStatus(likeCount: 0, isLiked: false);
    }
  }
} 