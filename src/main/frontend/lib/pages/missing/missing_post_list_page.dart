import 'package:flutter/material.dart';
import '../../models/missing_post.dart';
import '../../api/missing_post_api.dart';

class MissingPostListPage extends StatefulWidget {
  const MissingPostListPage({Key? key}) : super(key: key);

  @override
  State<MissingPostListPage> createState() => _MissingPostListPageState();
}

class _MissingPostListPageState extends State<MissingPostListPage> {
  int selectedTypeTab = 0; // 0: 전체, 1: 발견, 2: 실종
  final List<String> typeTabs = ['전체', '발견', '실종'];
  List<MissingPost> posts = [];
  bool isLoading = false;
  String? errorMessage;

  @override
  void initState() {
    super.initState();
    fetchPosts();
  }

  Future<void> fetchPosts() async {
    setState(() {
      isLoading = true;
      errorMessage = null;
    });
    try {
      String? type;
      if (selectedTypeTab == 1) type = 'FOUND';
      if (selectedTypeTab == 2) type = 'MISSING';
      final result = await MissingPostApi.fetchMissingPosts(type: type);
      setState(() {
        posts = result;
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        errorMessage = e.toString();
        isLoading = false;
      });
    }
  }

  void onTypeTabSelected(int idx) {
    setState(() {
      selectedTypeTab = idx;
    });
    fetchPosts();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('실종/발견'),
        centerTitle: true,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
        elevation: 0.5,
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
      ),
      body: Column(
        children: [
          // 상단 탭
          Container(
            color: Colors.grey[200],
            padding: const EdgeInsets.symmetric(vertical: 8),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: List.generate(typeTabs.length, (idx) {
                final bool isSelected = selectedTypeTab == idx;
                return Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 8.0),
                  child: GestureDetector(
                    onTap: () => onTypeTabSelected(idx),
                    child: Container(
                      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
                      decoration: BoxDecoration(
                        color: isSelected ? Colors.green : Colors.white,
                        borderRadius: BorderRadius.circular(22),
                      ),
                      child: Text(
                        typeTabs[idx],
                        style: TextStyle(
                          color: isSelected ? Colors.white : Colors.black,
                          fontWeight: FontWeight.w500,
                          fontSize: 16,
                        ),
                      ),
                    ),
                  ),
                );
              }),
            ),
          ),
          Expanded(
            child: isLoading
                ? const Center(child: CircularProgressIndicator())
                : errorMessage != null
                    ? Center(child: Text(errorMessage!, style: const TextStyle(color: Colors.red)))
                    : posts.isEmpty
                        ? const Center(child: Text('작성된 게시글이 없습니다.', style: TextStyle(fontSize: 18)))
                        : ListView.separated(
                            itemCount: posts.length,
                            separatorBuilder: (context, index) => const Divider(height: 1, color: Colors.grey),
                            itemBuilder: (context, index) {
                              final post = posts[index];
                              return InkWell(
                                onTap: () async {
                                  // 상세페이지로 이동 (추후 구현)
                                },
                                child: Padding(
                                  padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 12),
                                  child: Row(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      // 이미지 썸네일
                                      Container(
                                        width: 64,
                                        height: 64,
                                        decoration: BoxDecoration(
                                          color: Colors.grey[300],
                                          borderRadius: BorderRadius.circular(8),
                                        ),
                                        child: post.imagesUrls.isNotEmpty
                                            ? ClipRRect(
                                                borderRadius: BorderRadius.circular(8),
                                                child: Image.network(
                                                  post.imagesUrls[0],
                                                  width: 64,
                                                  height: 64,
                                                  fit: BoxFit.cover,
                                                  errorBuilder: (context, error, stackTrace) {
                                                    return const Center(
                                                      child: Icon(Icons.image_not_supported, color: Colors.grey),
                                                    );
                                                  },
                                                ),
                                              )
                                            : const Center(
                                                child: Icon(Icons.image_not_supported, color: Colors.grey),
                                              ),
                                      ),
                                      const SizedBox(width: 16),
                                      // 오른쪽 내용
                                      Expanded(
                                        child: Column(
                                          crossAxisAlignment: CrossAxisAlignment.start,
                                          children: [
                                            // 타입/제목
                                            Row(
                                              children: [
                                                Container(
                                                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                                                  decoration: BoxDecoration(
                                                    color: post.type == MissingPostType.found ? Colors.blue[100] : Colors.red[100],
                                                    borderRadius: BorderRadius.circular(8),
                                                  ),
                                                  child: Text(
                                                    post.type == MissingPostType.found ? '발견' : '실종',
                                                    style: TextStyle(
                                                      color: post.type == MissingPostType.found ? Colors.blue : Colors.red,
                                                      fontWeight: FontWeight.bold,
                                                      fontSize: 13,
                                                    ),
                                                  ),
                                                ),
                                                const SizedBox(width: 8),
                                                Expanded(
                                                  child: Text(
                                                    post.title,
                                                    style: const TextStyle(
                                                      fontSize: 16,
                                                      fontWeight: FontWeight.bold,
                                                      color: Colors.black87,
                                                    ),
                                                    maxLines: 1,
                                                    overflow: TextOverflow.ellipsis,
                                                  ),
                                                ),
                                              ],
                                            ),
                                            const SizedBox(height: 8),
                                            // 내용
                                            Text(
                                              post.description,
                                              style: const TextStyle(fontSize: 13, color: Colors.grey),
                                              maxLines: 1,
                                              overflow: TextOverflow.ellipsis,
                                            ),
                                            const SizedBox(height: 12),
                                            // 날짜
                                            Row(
                                              children: [
                                                Text(
                                                  '${post.missingDate.year}.${post.missingDate.month.toString().padLeft(2, '0')}.${post.missingDate.day.toString().padLeft(2, '0')}',
                                                  style: const TextStyle(fontSize: 12, color: Colors.grey),
                                                ),
                                              ],
                                            ),
                                            const SizedBox(height: 8),
                                            // 닉네임
                                            Text(
                                              post.nickname.isNotEmpty ? post.nickname : '익명',
                                              style: const TextStyle(fontSize: 12, color: Colors.grey),
                                            ),
                                          ],
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              );
                            },
                          ),
          ),
        ],
      ),
    );
  }
} 