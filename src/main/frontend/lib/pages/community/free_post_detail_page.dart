import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/user_provider.dart';
import '../../api/free_post_api.dart';
import '../../models/free_post.dart';

class FreePostDetailPage extends StatefulWidget {
  final int postId;
  const FreePostDetailPage({Key? key, required this.postId}) : super(key: key);

  @override
  State<FreePostDetailPage> createState() => _FreePostDetailPageState();
}

class _FreePostDetailPageState extends State<FreePostDetailPage> {
  FreePost? post;
  bool isLoading = true;
  String? errorMessage;

  @override
  void initState() {
    super.initState();
    fetchDetail();
  }

  Future<void> fetchDetail() async {
    setState(() {
      isLoading = true;
      errorMessage = null;
    });
    try {
      final detail = await FreePostApi.fetchFreePostDetail(widget.postId);
      setState(() {
        post = detail;
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        errorMessage = e.toString();
        isLoading = false;
      });
    }
  }

  void _showDeleteDialog() async {
    final result = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('게시글 삭제'),
        content: const Text('정말 삭제하시겠습니까?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('취소'),
          ),
          TextButton(
            onPressed: () => Navigator.pop(context, true),
            child: const Text('확인'),
          ),
        ],
      ),
    );
    if (result == true) {
      _deletePost();
    }
  }

  Future<void> _deletePost() async {
    try {
      await FreePostApi.deleteFreePost(widget.postId);
      if (mounted) {
        Navigator.pop(context, true); // true 반환(삭제됨)
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('삭제 실패: $e'), backgroundColor: Colors.red),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final userProvider = Provider.of<UserProvider>(context, listen: false);
    final isMine = post != null && userProvider.user?['email'] == post!.email;
    return Scaffold(
      appBar: AppBar(
        title: const Text('자유게시판'),
        centerTitle: true,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context, true),
        ),
        elevation: 0.5,
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : errorMessage != null
              ? Center(child: Text(errorMessage!, style: const TextStyle(color: Colors.red)))
              : SingleChildScrollView(
                  padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // 작성자 + 프로필
                      Row(
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          const CircleAvatar(
                            radius: 24,
                            backgroundColor: Colors.black12,
                            child: Icon(Icons.person, color: Colors.black45, size: 32),
                          ),
                          const SizedBox(width: 12),
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                post!.nickname,
                                style: const TextStyle(fontSize: 17, fontWeight: FontWeight.bold, color: Colors.black87),
                              ),
                              const SizedBox(height: 2),
                              Row(
                                children: [
                                  Text(
                                    post!.createdAt.split("T")[0].replaceAll("-", ".") +
                                        ' ' +
                                        (post!.createdAt.length > 16 ? post!.createdAt.substring(11, 16) : ''),
                                    style: const TextStyle(fontSize: 13, color: Colors.grey),
                                  ),
                                  const SizedBox(width: 8),
                                  const Text('|', style: TextStyle(color: Colors.grey)),
                                  const SizedBox(width: 8),
                                  Row(
                                    children: [
                                      const Icon(Icons.remove_red_eye, size: 16, color: Colors.grey),
                                      const SizedBox(width: 2),
                                      Text(
                                        post!.viewCount.toString(),
                                        style: const TextStyle(fontSize: 13, color: Colors.grey),
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ],
                          ),
                        ],
                      ),
                      const SizedBox(height: 24),
                      // 제목
                      Text(
                        post!.title,
                        style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: Colors.black87),
                      ),
                      const SizedBox(height: 18),
                      // 내용
                      Text(
                        post!.content,
                        style: const TextStyle(fontSize: 16, color: Colors.black87),
                      ),
                      const SizedBox(height: 32),
                      // 수정/삭제 버튼 (본인 글일 때만)
                      if (isMine)
                        Row(
                          mainAxisAlignment: MainAxisAlignment.end,
                          children: [
                            ElevatedButton(
                              style: ElevatedButton.styleFrom(
                                backgroundColor: Colors.grey[200],
                                foregroundColor: Colors.black87,
                                elevation: 0,
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                                padding: const EdgeInsets.symmetric(horizontal: 22, vertical: 8),
                              ),
                              onPressed: () async {
                                final result = await Navigator.pushNamed(
                                  context,
                                  '/free-post-edit/${post!.id}',
                                  arguments: {
                                    'title': post!.title,
                                    'content': post!.content,
                                  },
                                );
                                if (result == true) {
                                  fetchDetail(); // 수정 후 상세페이지 새로고침
                                }
                              },
                              child: const Text('수정', style: TextStyle(fontSize: 15)),
                            ),
                            const SizedBox(width: 12),
                            ElevatedButton(
                              style: ElevatedButton.styleFrom(
                                backgroundColor: Colors.grey[200],
                                foregroundColor: Colors.black87,
                                elevation: 0,
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                                padding: const EdgeInsets.symmetric(horizontal: 22, vertical: 8),
                              ),
                              onPressed: _showDeleteDialog,
                              child: const Text('삭제', style: TextStyle(fontSize: 15)),
                            ),
                          ],
                        ),
                    ],
                  ),
                ),
    );
  }
} 