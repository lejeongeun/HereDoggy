import 'package:dio/dio.dart';
import '../models/free_post.dart';
import '../utils/constants.dart';
import '../services/auth_service.dart';
import 'dart:convert';
import 'package:image_picker/image_picker.dart';
import 'like_api.dart';

class FreePostApi {
  static final Dio _dio = Dio(
    BaseOptions(
      baseUrl: AppConstants.baseUrl,
      connectTimeout: const Duration(seconds: 5),
      receiveTimeout: const Duration(seconds: 5),
      contentType: Headers.jsonContentType,
      responseType: ResponseType.json,
    ),
  );

  static Future<List<FreePost>> fetchFreePosts() async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();

      final response = await _dio.get(
        '/members/free-posts',
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );
      print('게시글 목록 API 응답: ${response.data}');
      if (response.statusCode == 200) {
        final List<dynamic> data = response.data;
        print('게시글 개수: ${data.length}');
        for (int i = 0; i < data.length; i++) {
          print('게시글 $i: ${data[i]}');
          if (data[i]['imageUrl'] != null) {
            print('게시글 $i imageUrl: ${data[i]['imageUrl']}');
          }
          if (data[i]['imagesUrls'] != null) {
            print('게시글 $i imagesUrls: ${data[i]['imagesUrls']}');
          }
        }
        return data.map((json) => FreePost.fromJson(json)).toList();
      } else {
        throw Exception('서버 오류: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('게시글 목록을 불러오지 못했습니다: $e');
    }
  }

  static Future<void> createFreePost({
    required String title,
    required String content,
    List<XFile>? images,
  }) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();

      print('글 작성 요청 시작: title=$title, content=$content');
      
      final Map<String, dynamic> formDataMap = {
        'info': jsonEncode({
          'title': title,
          'content': content,
        }),
      };

      if (images != null && images.isNotEmpty) {
        formDataMap['images'] = await Future.wait(
          images.map((img) => MultipartFile.fromFile(img.path, filename: img.name)),
        );
      }

      final formData = FormData.fromMap(formDataMap);

      final response = await _dio.post(
        '/members/free-posts',
        data: formData,
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );
      print('글 작성 응답: ${response.statusCode}');
      
      if (response.statusCode != 200 && response.statusCode != 201) {
        throw Exception('글 작성 실패: ${response.statusCode}');
      }
    } catch (e) {
      print('글 작성 에러: $e');
      throw Exception('글 작성 중 오류가 발생했습니다: $e');
    }
  }

  static Future<FreePost> fetchFreePostDetail(int postId) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final response = await _dio.get(
        '/members/free-posts/$postId',
        options: Options(
          headers: {
            'Authorization': 'Bearer $token',
          },
        ),
      );
      print('게시글 상세 API 응답: ${response.data}');
      if (response.statusCode == 200) {
        return FreePost.fromJson(response.data);
      } else {
        throw Exception('서버 오류: $response.statusCode');
      }
    } catch (e) {
      throw Exception('게시글 상세 정보를 불러오지 못했습니다: $e');
    }
  }

  static Future<void> deleteFreePost(int postId) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final response = await _dio.delete(
        '/members/free-posts/$postId',
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

  static Future<void> editFreePost({
    required int postId,
    required String title,
    required String content,
  }) async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      
      // FormData 생성
      final formData = FormData.fromMap({
        'info': jsonEncode({
          'title': title,
          'content': content,
        }),
      });

      final response = await _dio.put(
        '/members/free-posts/$postId',
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
    return await LikeApi.toggleLike('free', postId);
  }

  // 좋아요 개수 조회
  static Future<int> getLikeCount(int postId) async {
    return await LikeApi.getLikeCount('free', postId);
  }

  // 게시글 상세 조회 시 좋아요 정보 포함
  static Future<FreePost> fetchFreePostDetailWithLike(int postId) async {
    try {
      final post = await fetchFreePostDetail(postId);
      final likeCount = await getLikeCount(postId);
      
      // 좋아요 개수만 업데이트 (isLiked는 토글 시에만 업데이트)
      return post.copyWith(likeCount: likeCount);
    } catch (e) {
      throw Exception('게시글 상세 정보를 불러오지 못했습니다: $e');
    }
  }
} 