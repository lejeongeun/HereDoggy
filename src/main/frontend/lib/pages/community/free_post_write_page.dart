import 'package:flutter/material.dart';
import '../../api/free_post_api.dart';

class FreePostWritePage extends StatefulWidget {
  const FreePostWritePage({Key? key}) : super(key: key);

  @override
  State<FreePostWritePage> createState() => _FreePostWritePageState();
}

class _FreePostWritePageState extends State<FreePostWritePage> {
  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _contentController = TextEditingController();
  bool _isLoading = false;

  @override
  void dispose() {
    _titleController.dispose();
    _contentController.dispose();
    super.dispose();
  }

  Future<void> _submitPost() async {
    final title = _titleController.text.trim();
    final content = _contentController.text.trim();

    print('글 작성 시작: title=$title, content=$content');

    // 유효성 검사
    if (title.isEmpty) {
      print('제목이 비어있음');
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('제목을 입력해주세요')),
      );
      return;
    }

    if (content.isEmpty) {
      print('내용이 비어있음');
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('내용을 입력해주세요')),
      );
      return;
    }

    setState(() {
      _isLoading = true;
    });

    try {
      print('API 호출 시작');
      await FreePostApi.createFreePost(
        title: title,
        content: content,
      );
      print('API 호출 성공');
      
      if (mounted) {
        Navigator.pop(context, true); // 글 작성 성공 시 true 반환
      }
    } catch (e) {
      print('API 호출 실패: $e');
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('글 작성 실패: ${e.toString()}'),
            backgroundColor: Colors.red,
          ),
        );
      }
    } finally {
      if (mounted) {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // 상단 바
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text(
                    '자유게시판',
                    style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                  ),
                  IconButton(
                    icon: const Icon(Icons.close, size: 28),
                    onPressed: () => Navigator.pop(context),
                  ),
                ],
              ),
            ),
            const Divider(height: 1),
            // 제목 입력란
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              child: TextField(
                controller: _titleController,
                decoration: const InputDecoration(
                  hintText: '제목을 입력하세요',
                  border: InputBorder.none,
                ),
                style: const TextStyle(fontSize: 18),
              ),
            ),
            const Divider(height: 1),
            // 내용 입력란
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                child: TextField(
                  controller: _contentController,
                  decoration: const InputDecoration(
                    hintText: '내용을 입력하세요',
                    border: InputBorder.none,
                  ),
                  style: const TextStyle(fontSize: 16),
                  maxLines: null,
                  keyboardType: TextInputType.multiline,
                ),
              ),
            ),
            const Divider(height: 1),
            // 하단 바
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
              child: Row(
                children: [
                  IconButton(
                    icon: const Icon(Icons.image, color: Colors.grey),
                    onPressed: () {
                      // 사진 첨부 기능(추후 구현)
                    },
                  ),
                  const Text('사진', style: TextStyle(color: Colors.grey)),
                  const Spacer(),
                  _isLoading
                      ? const SizedBox(
                          width: 24,
                          height: 24,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        )
                      : TextButton(
                          onPressed: _submitPost,
                          child: const Text('완료', style: TextStyle(fontSize: 16)),
                        ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
} 