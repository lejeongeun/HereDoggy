import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../../utils/constants.dart';
import '../../services/auth_service.dart';

class MyPostsTab extends StatefulWidget {
  const MyPostsTab({Key? key}) : super(key: key);

  @override
  State<MyPostsTab> createState() => _MyPostsTabState();
}

class _MyPostsTabState extends State<MyPostsTab> {
  bool isLoading = true;
  Map<String, dynamic>? posts;
  String? error;

  @override
  void initState() {
    super.initState();
    fetchMyPosts();
  }

  Future<void> fetchMyPosts() async {
    try {
      final authService = AuthService();
      final token = await authService.getAccessToken();
      final response = await http.get(
        Uri.parse('${AppConstants.baseUrl}/members/me/posts'),
        headers: token != null ? {'Authorization': 'Bearer $token'} : {},
      );
      if (response.statusCode == 200) {
        setState(() {
          posts = json.decode(utf8.decode(response.bodyBytes));
          isLoading = false;
        });
      } else {
        setState(() {
          error = '게시글을 불러오지 못했습니다.';
          isLoading = false;
        });
      }
    } catch (e) {
      setState(() {
        error = '네트워크 오류';
        isLoading = false;
      });
    }
  }

  String formatDate(String? dateStr) {
    if (dateStr == null) return '';
    try {
      final date = DateTime.parse(dateStr);
      return DateFormat('yyyy.MM.dd').format(date);
    } catch (_) {
      return '';
    }
  }

  Widget buildPostTile({required String title, required String content, required String date, required VoidCallback onTap, String? badge}) {
    return ListTile(
      contentPadding: const EdgeInsets.symmetric(horizontal: 8, vertical: 0),
      title: Row(
        children: [
          if (badge != null)
            Container(
              margin: const EdgeInsets.only(right: 6),
              padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
              decoration: BoxDecoration(
                color: Colors.grey[200],
                borderRadius: BorderRadius.circular(8),
              ),
              child: Text(badge, style: const TextStyle(fontSize: 11, color: Colors.black54)),
            ),
          Expanded(
            child: Text(
              _ellipsis(title),
              style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w500),
              overflow: TextOverflow.ellipsis,
              maxLines: 1,
            ),
          ),
        ],
      ),
      subtitle: Text(
        _ellipsis(content),
        style: const TextStyle(fontSize: 13, color: Colors.black54),
        overflow: TextOverflow.ellipsis,
        maxLines: 1,
      ),
      trailing: Text(date, style: const TextStyle(fontSize: 12, color: Colors.black38)),
      onTap: onTap,
    );
  }

  String _ellipsis(String text) {
    if (text.length <= 22) return text;
    return text.substring(0, 22) + '...';
  }

  Widget buildSection(String title, List<dynamic>? postList, {String? badge, required String boardType}) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.fromLTRB(12, 18, 0, 6),
          child: Text(title, style: const TextStyle(fontSize: 15, fontWeight: FontWeight.bold)),
        ),
        if (postList == null || postList.isEmpty)
          const Padding(
            padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: Text('게시글이 없습니다.', style: TextStyle(color: Colors.black38)),
          )
        else
          ...postList.map((post) => buildPostTile(
                title: post['title'] ?? '',
                content: post['content'] ?? '',
                date: formatDate(post['createdAt']),
                badge: badge,
                onTap: () {
                  Navigator.pushNamed(context, _getDetailRoute(boardType, post['id']));
                },
              )),
        const Divider(height: 24, thickness: 1),
      ],
    );
  }

  String _getDetailRoute(String boardType, dynamic id) {
    switch (boardType) {
      case 'missing':
        return '/missing-post-detail/$id';
      case 'review':
        return '/review-post-detail/$id';
      case 'free':
      default:
        return '/free-post-detail/$id';
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
        title: const Text('내 게시글', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
        centerTitle: true,
        elevation: 0,
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : error != null
              ? Center(child: Text(error!))
              : SingleChildScrollView(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      buildSection('실종/발견', posts?['missingPosts'], badge: '실종', boardType: 'missing'),
                      buildSection('입양/산책 후기', posts?['reviewPosts'], badge: '입양후기', boardType: 'review'),
                      buildSection('자유게시판', posts?['freePosts'], boardType: 'free'),
                    ],
                  ),
                ),
    );
  }
} 