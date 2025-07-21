import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../models/missing_post.dart';
import '../../api/missing_post_api.dart';
import '../../providers/user_provider.dart';
import '../../api/comment_api.dart';
import '../../models/comment.dart';
import '../mypage/report_page.dart';

class MissingPostDetailPage extends StatefulWidget {
  final int postId;
  const MissingPostDetailPage({Key? key, required this.postId}) : super(key: key);

  @override
  State<MissingPostDetailPage> createState() => _MissingPostDetailPageState();
}

class _MissingPostDetailPageState extends State<MissingPostDetailPage> {
  MissingPost? post;
  bool isLoading = true;
  String? errorMessage;
  List<Comment> comments = [];
  bool isLoadingComments = false;
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
      final detail = await MissingPostApi.fetchMissingPostDetail(widget.postId);
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
      final fetchedComments = await CommentApi.fetchComments('missing', widget.postId);
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
      await CommentApi.createComment('missing', widget.postId, _commentController.text.trim());
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
      await MissingPostApi.deleteMissingPost(widget.postId);
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
    // 닉네임으로 본인 글 확인 (정확도는 떨어질 수 있음)
    final isMine = post != null && userProvider.user?['nickname'] == post!.nickname;
    final Color green = const Color(0xFF4CAF50);
    final Color blue = const Color(0xFF2196F3);
    final Color grayBox = const Color(0xFFF0F0F0);
    final Color dividerColor = const Color(0xFFE0E0E0);
    return Scaffold(
      appBar: AppBar(
        title: const Text('실종/발견', style: TextStyle(fontWeight: FontWeight.bold)),
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
                        padding: const EdgeInsets.symmetric(horizontal: 0, vertical: 0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            // 작성자 + 프로필
                            Container(
                              width: double.infinity,
                              padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 24),
                              child: Row(
                                crossAxisAlignment: CrossAxisAlignment.center,
                                children: [
                                  CircleAvatar(
                                    radius: 28,
                                    backgroundColor: blue.withOpacity(0.12),
                                    child: const Icon(Icons.person, color: Colors.black45, size: 32),
                                  ),
                                  const SizedBox(width: 16),
                                  Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        post!.nickname.isNotEmpty ? post!.nickname : '익명',
                                        style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.black87),
                                      ),
                                      const SizedBox(height: 4),
                                      Text(
                                        post!.createdAt.toString().split('T')[0].replaceAll('-', '.') +
                                            ' ' +
                                            (post!.createdAt.toString().length > 16 ? post!.createdAt.toString().substring(11, 16) : ''),
                                        style: const TextStyle(fontSize: 13, color: Colors.grey),
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ),
                            // 주요 정보 표
                            Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 18),
                              child: Card(
                                elevation: 0,
                                margin: EdgeInsets.zero,
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                                child: Column(
                                  children: [
                                    _tableRow('성별', _genderToString(post!.gender)),
                                    Divider(height: 1, color: dividerColor),
                                    _tableRow('나이', post!.age?.toString() ?? '알 수 없음'),
                                    Divider(height: 1, color: dividerColor),
                                    _tableRow('몸무게', post!.weight != null ? '${post!.weight}kg' : '알 수 없음'),
                                    Divider(height: 1, color: dividerColor),
                                    _tableRow('털 색상', post!.furColor?.isNotEmpty == true ? post!.furColor! : '알 수 없음'),
                                    Divider(height: 1, color: dividerColor),
                                    // 날짜/장소는 별도 박스
                                    Padding(
                                      padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 0),
                                      child: Row(
                                        children: [
                                          Container(
                                            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                                            decoration: BoxDecoration(
                                              color: grayBox,
                                              borderRadius: BorderRadius.circular(6),
                                            ),
                                            child: const Text('실종(목격)날짜', style: TextStyle(fontSize: 13, color: Colors.black54)),
                                          ),
                                          const SizedBox(width: 8),
                                          Text(_dateToString(post!.missingDate), style: const TextStyle(fontSize: 15)),
                                        ],
                                      ),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.only(bottom: 8),
                                      child: Row(
                                        children: [
                                          Container(
                                            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                                            decoration: BoxDecoration(
                                              color: grayBox,
                                              borderRadius: BorderRadius.circular(6),
                                            ),
                                            child: const Text('실종(목격)장소', style: TextStyle(fontSize: 13, color: Colors.black54)),
                                          ),
                                          const SizedBox(width: 8),
                                          Expanded(child: Text(post!.missingLocation, style: const TextStyle(fontSize: 15))),
                                        ],
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                            // 설명
                            Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 14),
                              child: Card(
                                color: Colors.white,
                                elevation: 0,
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                                child: Padding(
                                  padding: const EdgeInsets.all(16),
                                  child: Text(post!.description, style: const TextStyle(fontSize: 16, color: Colors.black87)),
                                ),
                              ),
                            ),
                            // 이미지 표시
                            if (post!.imagesUrls.isNotEmpty)
                              Padding(
                                padding: const EdgeInsets.symmetric(horizontal: 18),
                                child: SizedBox(
                                  height: 180,
                                  child: ListView.separated(
                                    scrollDirection: Axis.horizontal,
                                    itemCount: post!.imagesUrls.length,
                                    separatorBuilder: (context, idx) => const SizedBox(width: 12),
                                    itemBuilder: (context, idx) {
                                      final imageUrl = post!.imagesUrls[idx];
                                      return ClipRRect(
                                        borderRadius: BorderRadius.circular(12),
                                        child: Image.network(
                                          imageUrl,
                                          width: 180,
                                          height: 180,
                                          fit: BoxFit.cover,
                                          errorBuilder: (context, error, stackTrace) {
                                            return Container(
                                              width: 180,
                                              height: 180,
                                              color: Colors.grey[200],
                                              child: const Center(
                                                child: Icon(Icons.error_outline, color: Colors.red),
                                              ),
                                            );
                                          },
                                        ),
                                      );
                                    },
                                  ),
                                ),
                              ),
                            const SizedBox(height: 18),
                            // 수정/삭제/신고 버튼
                            Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 18),
                              child: Row(
                                mainAxisAlignment: MainAxisAlignment.end,
                                children: [
                                  // 신고 버튼 (모든 사용자에게 표시)
                                  ElevatedButton(
                                    style: ElevatedButton.styleFrom(
                                      backgroundColor: Colors.red[50],
                                      foregroundColor: Colors.red[600],
                                      elevation: 0,
                                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                                      padding: const EdgeInsets.symmetric(horizontal: 22, vertical: 8),
                                    ),
                                    onPressed: () {
                                      Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                          builder: (_) => ReportPage(
                                            targetType: 'missing',
                                            targetId: post!.id,
                                            targetTitle: post!.title,
                                          ),
                                        ),
                                      );
                                    },
                                    child: const Text('신고', style: TextStyle(fontSize: 15)),
                                  ),
                                  if (isMine) ...[
                                    const SizedBox(width: 12),
                                    ElevatedButton(
                                      style: ElevatedButton.styleFrom(
                                        backgroundColor: Colors.grey[200],
                                        foregroundColor: Colors.black87,
                                        elevation: 0,
                                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
                                        padding: const EdgeInsets.symmetric(horizontal: 22, vertical: 8),
                                      ),
                                      onPressed: () {
                                        Navigator.pushNamed(
                                          context,
                                          '/missing-post-edit/${post!.id}',
                                        ).then((result) {
                                          if (result == true) {
                                            fetchDetail();
                                          }
                                        });
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
                                      onPressed: () {
                                        _showDeleteDialog();
                                      },
                                      child: const Text('삭제', style: TextStyle(fontSize: 15)),
                                    ),
                                  ],
                                ],
                              ),
                            ),
                            const SizedBox(height: 8),
                            // 댓글 구역
                            Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 18),
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
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
                                        final isCommentMine = Provider.of<UserProvider>(context, listen: false).user?['email'] == comment.email;
                                        return Padding(
                                          padding: const EdgeInsets.only(bottom: 16),
                                          child: Column(
                                            crossAxisAlignment: CrossAxisAlignment.start,
                                            children: [
                                              Row(
                                                children: [
                                                  CircleAvatar(
                                                    radius: 16,
                                                    backgroundColor: Colors.grey[300],
                                                    child: const Icon(Icons.person, color: Colors.black45, size: 20),
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
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
      bottomNavigationBar: Container(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
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
                decoration: InputDecoration(
                  hintText: '댓글을 입력하세요',
                  border: InputBorder.none,
                  filled: true,
                  fillColor: const Color(0xFFF5F5F5),
                  contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(24),
                    borderSide: BorderSide.none,
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(24),
                    borderSide: BorderSide.none,
                  ),
                ),
                maxLines: null,
              ),
            ),
            const SizedBox(width: 8),
            Container(
              decoration: BoxDecoration(
                color: green,
                shape: BoxShape.circle,
              ),
              child: IconButton(
                onPressed: _createComment,
                icon: const Icon(Icons.send, color: Colors.white),
              ),
            ),
          ],
        ),
      ),
    );
  }

  // 표 형식의 정보 행
  Widget _tableRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      child: Row(
        children: [
          SizedBox(width: 90, child: Text(label, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15))),
          const SizedBox(width: 8),
          Expanded(child: Text(value, style: const TextStyle(fontSize: 15))),
        ],
      ),
    );
  }

  String _genderToString(DogGender gender) {
    switch (gender) {
      case DogGender.male:
        return '수컷';
      case DogGender.female:
        return '암컷';
      default:
        return '알 수 없음';
    }
  }

  String _dateToString(DateTime date) {
    return '${date.year}.${date.month.toString().padLeft(2, '0')}.${date.day.toString().padLeft(2, '0')}';
  }
} 