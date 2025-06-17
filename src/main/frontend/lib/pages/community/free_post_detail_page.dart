import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/user_provider.dart';
import '../../api/free_post_api.dart';
import '../../api/comment_api.dart';
import '../../models/free_post.dart';
import '../../models/comment.dart';
import '../../utils/constants.dart';

class FreePostDetailPage extends StatefulWidget {
  final int postId;
  const FreePostDetailPage({Key? key, required this.postId}) : super(key: key);

  @override
  State<FreePostDetailPage> createState() => _FreePostDetailPageState();
}

class _FreePostDetailPageState extends State<FreePostDetailPage> {
  FreePost? post;
  List<Comment> comments = [];
  bool isLoading = true;
  bool isLoadingComments = false;
  String? errorMessage;
  final TextEditingController _commentController = TextEditingController();
  int? editingCommentId;
  final TextEditingController _editCommentController = TextEditingController();

  @override
  void initState() {
    super.initState();
    fetchDetail();
    fetchComments();
  }

  @override
  void dispose() {
    _commentController.dispose();
    _editCommentController.dispose();
    super.dispose();
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

  Future<void> fetchComments() async {
    setState(() {
      isLoadingComments = true;
    });
    try {
      final fetchedComments = await CommentApi.fetchComments('free', widget.postId);
      setState(() {
        comments = fetchedComments;
        isLoadingComments = false;
      });
    } catch (e) {
      setState(() {
        isLoadingComments = false;
      });
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('댓글을 불러오지 못했습니다: $e'), backgroundColor: Colors.red),
        );
      }
    }
  }

  Future<void> _createComment() async {
    if (_commentController.text.trim().isEmpty) return;

    final userProvider = Provider.of<UserProvider>(context, listen: false);
    if (!userProvider.isLoggedIn) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('로그인이 필요한 서비스입니다'),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }

    try {
      await CommentApi.createComment('free', widget.postId, _commentController.text.trim());
      _commentController.clear();
      fetchComments();
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('댓글 작성 실패: $e'), backgroundColor: Colors.red),
        );
      }
    }
  }

  Future<void> _deleteComment(int commentId) async {
    try {
      await CommentApi.deleteComment(commentId);
      fetchComments();
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('댓글 삭제 실패: $e'), backgroundColor: Colors.red),
        );
      }
    }
  }

  void _showDeleteCommentDialog(int commentId) async {
    final result = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('댓글 삭제'),
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
      _deleteComment(commentId);
    }
  }

  Future<void> _editComment(int commentId, String content) async {
    try {
      await CommentApi.editComment(commentId, content);
      _editCommentController.clear();
      setState(() {
        editingCommentId = null;
      });
      fetchComments();
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('댓글 수정 실패: $e'), backgroundColor: Colors.red),
        );
      }
    }
  }

  void _startEditing(Comment comment) {
    setState(() {
      editingCommentId = comment.id;
      _editCommentController.text = comment.content;
    });
  }

  void _cancelEditing() {
    setState(() {
      editingCommentId = null;
      _editCommentController.clear();
    });
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
              : Column(
                  children: [
                    Expanded(
                      child: SingleChildScrollView(
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
                            const SizedBox(height: 16),
                            // 이미지 표시
                            if (post!.imagesUrls.isNotEmpty)
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  const SizedBox(height: 16),
                                  ...post!.imagesUrls.map((imageUrl) => Padding(
                                    padding: const EdgeInsets.only(bottom: 16),
                                    child: ClipRRect(
                                      borderRadius: BorderRadius.circular(8),
                                      child: Image.network(
                                        'http://192.168.10.128:8080$imageUrl',
                                        fit: BoxFit.cover,
                                        loadingBuilder: (context, child, loadingProgress) {
                                          if (loadingProgress == null) return child;
                                          return Container(
                                            height: 200,
                                            color: Colors.grey[200],
                                            child: const Center(
                                              child: CircularProgressIndicator(),
                                            ),
                                          );
                                        },
                                        errorBuilder: (context, error, stackTrace) {
                                          return Container(
                                            height: 200,
                                            color: Colors.grey[200],
                                            child: const Center(
                                              child: Icon(Icons.error_outline, color: Colors.red),
                                            ),
                                          );
                                        },
                                      ),
                                    ),
                                  )).toList(),
                                ],
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
                            const SizedBox(height: 32),
                            // 댓글 섹션
                            const Divider(height: 1),
                            const SizedBox(height: 16),
                            const Text(
                              '댓글',
                              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                            ),
                            const SizedBox(height: 16),
                            if (isLoadingComments)
                              const Center(child: CircularProgressIndicator())
                            else if (comments.isEmpty)
                              const Center(
                                child: Padding(
                                  padding: EdgeInsets.symmetric(vertical: 32),
                                  child: Text(
                                    '아직 댓글이 없습니다.',
                                    style: TextStyle(color: Colors.grey),
                                  ),
                                ),
                              )
                            else
                              ListView.builder(
                                shrinkWrap: true,
                                physics: const NeverScrollableScrollPhysics(),
                                itemCount: comments.length,
                                itemBuilder: (context, index) {
                                  final comment = comments[index];
                                  final isCommentMine = userProvider.user?['email'] == comment.email;
                                  return Padding(
                                    padding: const EdgeInsets.only(bottom: 16),
                                    child: Column(
                                      crossAxisAlignment: CrossAxisAlignment.start,
                                      children: [
                                        Row(
                                          children: [
                                            const CircleAvatar(
                                              radius: 16,
                                              backgroundColor: Colors.black12,
                                              child: Icon(Icons.person, color: Colors.black45, size: 20),
                                            ),
                                            const SizedBox(width: 8),
                                            Text(
                                              comment.nickname,
                                              style: const TextStyle(fontWeight: FontWeight.bold),
                                            ),
                                            const SizedBox(width: 8),
                                            Text(
                                              comment.createdAt.split("T")[0].replaceAll("-", "."),
                                              style: const TextStyle(color: Colors.grey, fontSize: 12),
                                            ),
                                            const Spacer(),
                                            if (isCommentMine)
                                              Row(
                                                children: [
                                                  IconButton(
                                                    icon: const Icon(Icons.edit_outlined, size: 20),
                                                    onPressed: () => _startEditing(comment),
                                                    color: Colors.grey,
                                                  ),
                                                  IconButton(
                                                    icon: const Icon(Icons.delete_outline, size: 20),
                                                    onPressed: () => _showDeleteCommentDialog(comment.id),
                                                    color: Colors.grey,
                                                  ),
                                                ],
                                              ),
                                          ],
                                        ),
                                        const SizedBox(height: 8),
                                        Padding(
                                          padding: const EdgeInsets.only(left: 40),
                                          child: editingCommentId == comment.id
                                              ? Column(
                                                  children: [
                                                    TextField(
                                                      controller: _editCommentController,
                                                      decoration: const InputDecoration(
                                                        hintText: '댓글을 수정하세요',
                                                        border: OutlineInputBorder(
                                                          borderRadius: BorderRadius.all(Radius.circular(24)),
                                                          borderSide: BorderSide.none,
                                                        ),
                                                        filled: true,
                                                        fillColor: Color(0xFFF5F5F5),
                                                        contentPadding: EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                                                      ),
                                                      maxLines: null,
                                                    ),
                                                    const SizedBox(height: 8),
                                                    Row(
                                                      mainAxisAlignment: MainAxisAlignment.end,
                                                      children: [
                                                        TextButton(
                                                          onPressed: _cancelEditing,
                                                          child: const Text('취소'),
                                                        ),
                                                        const SizedBox(width: 8),
                                                        ElevatedButton(
                                                          onPressed: () => _editComment(comment.id, _editCommentController.text.trim()),
                                                          child: const Text('수정'),
                                                        ),
                                                      ],
                                                    ),
                                                  ],
                                                )
                                              : Text(comment.content),
                                        ),
                                      ],
                                    ),
                                  );
                                },
                              ),
                          ],
                        ),
                      ),
                    ),
                    // 댓글 입력 섹션
                    Container(
                      padding: const EdgeInsets.all(16),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withOpacity(0.05),
                            blurRadius: 10,
                            offset: const Offset(0, -5),
                          ),
                        ],
                      ),
                      child: Row(
                        children: [
                          Expanded(
                            child: TextField(
                              controller: _commentController,
                              decoration: const InputDecoration(
                                hintText: '댓글을 입력하세요',
                                border: OutlineInputBorder(
                                  borderRadius: BorderRadius.all(Radius.circular(24)),
                                  borderSide: BorderSide.none,
                                ),
                                filled: true,
                                fillColor: Color(0xFFF5F5F5),
                                contentPadding: EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                              ),
                              maxLines: null,
                            ),
                          ),
                          const SizedBox(width: 8),
                          IconButton(
                            onPressed: _createComment,
                            icon: const Icon(Icons.send),
                            color: Theme.of(context).primaryColor,
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
    );
  }
} 