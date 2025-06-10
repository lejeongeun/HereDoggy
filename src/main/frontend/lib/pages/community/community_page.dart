import 'package:flutter/material.dart';
import '../../models/free_post.dart';
import '../../api/free_post_api.dart';
import 'package:provider/provider.dart';
import '../../providers/user_provider.dart';

enum BoardType { all, free, missing, adoption }

class BoardPost {
  final int id;
  final String title;
  final String content;
  final String boardType; // 'free', 'missing', 'adoption'
  final int viewCount;
  final String email;
  final String nickname;
  final String createdAt;

  BoardPost({
    required this.id,
    required this.title,
    required this.content,
    required this.boardType,
    required this.viewCount,
    required this.email,
    required this.nickname,
    required this.createdAt,
  });

  factory BoardPost.fromFreePost(FreePost post) {
    return BoardPost(
      id: post.id,
      title: post.title,
      content: post.content,
      boardType: 'free',
      viewCount: post.viewCount,
      email: post.email,
      nickname: post.nickname,
      createdAt: post.createdAt,
    );
  }
}

class CommunityPage extends StatefulWidget {
  const CommunityPage({Key? key}) : super(key: key);

  @override
  State<CommunityPage> createState() => _CommunityPageState();
}

class _CommunityPageState extends State<CommunityPage> {
  int selectedTab = 0; // 0: 전체, 1: 실종/발견, 2: 입양/산책 후기, 3: 자유게시판
  final List<String> tabs = ['전체', '실종/발견', '입양/산책 후기', '자유게시판'];
  final Color green = const Color(0xFF4CAF50);

  List<BoardPost> boardPosts = [];
  bool isLoading = false;
  String? errorMessage;

  @override
  void initState() {
    super.initState();
    // 전체 탭이 기본 선택이므로, 진입 시 전체 글 목록을 불러온다
    fetchAllPosts();
  }

  Future<void> fetchAllPosts() async {
    setState(() {
      isLoading = true;
      errorMessage = null;
    });
    try {
      // 지금은 자유게시판만, 추후 missing, adoption 추가
      final freePosts = await FreePostApi.fetchFreePosts();
      final allPosts = freePosts.map((e) => BoardPost.fromFreePost(e)).toList();
      // 최신순 정렬(추후 여러 게시판 합칠 때 필요)
      allPosts.sort((a, b) => b.createdAt.compareTo(a.createdAt));
      setState(() {
        boardPosts = allPosts;
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        errorMessage = e.toString();
        isLoading = false;
      });
    }
  }

  Future<void> fetchFreeBoardPosts() async {
    setState(() {
      isLoading = true;
      errorMessage = null;
    });
    try {
      final freePosts = await FreePostApi.fetchFreePosts();
      setState(() {
        boardPosts = freePosts.map((e) => BoardPost.fromFreePost(e)).toList();
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        errorMessage = e.toString();
        isLoading = false;
      });
    }
  }

  void onTabSelected(int idx) {
    setState(() {
      selectedTab = idx;
      errorMessage = null;
    });
    if (idx == 0) {
      fetchAllPosts();
    } else if (idx == 3) {
      fetchFreeBoardPosts();
    }
    // 추후 실종/입양 탭 추가 시 else if (idx == 1) ...
  }

  void _handleWriteButtonPress() async {
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
    final result = await Navigator.pushNamed(context, '/free-post-write');
    if (result == true) {
      // 글 작성이 완료되면 현재 선택된 탭에 따라 새로고침
      if (selectedTab == 0) {
        fetchAllPosts();
      } else if (selectedTab == 3) {
        fetchFreeBoardPosts();
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Column(
          children: [
            Container(
              color: Colors.grey[300],
              padding: const EdgeInsets.only(top: 16, bottom: 8),
              child: Column(
                children: [
                  Text(
                    '커뮤니티',
                    style: TextStyle(
                      color: green,
                      fontWeight: FontWeight.bold,
                      fontSize: 28,
                    ),
                  ),
                  const SizedBox(height: 8),
                  SingleChildScrollView(
                    scrollDirection: Axis.horizontal,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: List.generate(tabs.length, (idx) {
                        final bool isSelected = selectedTab == idx;
                        return Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 6.0),
                          child: GestureDetector(
                            onTap: () => onTabSelected(idx),
                            child: Container(
                              padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 10),
                              decoration: BoxDecoration(
                                color: isSelected ? green : Colors.white,
                                borderRadius: BorderRadius.circular(22),
                              ),
                              child: Text(
                                tabs[idx],
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
                ],
              ),
            ),
            Expanded(
              child: _buildBody(),
            ),
          ],
        ),
      ),
      bottomNavigationBar: _CommunityBottomNavBar(),
      floatingActionButton: (selectedTab == 0)
          ? null
          : FloatingActionButton(
              backgroundColor: green,
              onPressed: _handleWriteButtonPress,
              child: const Icon(Icons.edit, color: Colors.white),
            ),
    );
  }

  Widget _buildBody() {
    if (selectedTab != 0 && selectedTab != 3) {
      return const Center(
        child: Text(
          '작성된 게시글이 없습니다.',
          style: TextStyle(fontSize: 20),
        ),
      );
    }
    if (isLoading) {
      return const Center(child: CircularProgressIndicator());
    }
    if (errorMessage != null) {
      return Center(child: Text(errorMessage!, style: const TextStyle(color: Colors.red)));
    }
    if (boardPosts.isEmpty) {
      return const Center(
        child: Text(
          '작성된 게시글이 없습니다.',
          style: TextStyle(fontSize: 20),
        ),
      );
    }
    return ListView.separated(
      padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 0),
      itemCount: boardPosts.length,
      separatorBuilder: (context, index) => const Divider(height: 1, color: Colors.grey),
      itemBuilder: (context, index) {
        final post = boardPosts[index];
        return InkWell(
          onTap: () async {
            final result = await Navigator.pushNamed(context, '/free-post-detail/${post.id}');
            if (result == true) {
              if (selectedTab == 0) {
                fetchAllPosts();
              } else if (selectedTab == 3) {
                fetchFreeBoardPosts();
              }
            }
          },
          child: Padding(
            padding: const EdgeInsets.symmetric(vertical: 18, horizontal: 12),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // 이미지 자리(회색 네모)
                Container(
                  width: 64,
                  height: 64,
                  decoration: BoxDecoration(
                    color: Colors.grey[300],
                    borderRadius: BorderRadius.circular(8),
                  ),
                ),
                const SizedBox(width: 16),
                // 오른쪽 내용
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // 제목
                      Text(
                        post.title,
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                          color: Colors.black87,
                        ),
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                      ),
                      const SizedBox(height: 16/3), // 제목과 내용 사이 간격 (제목 폰트 크기 16의 1/3)
                      // 내용
                      Text(
                        post.content,
                        style: const TextStyle(
                          fontSize: 13,
                          color: Colors.grey,
                        ),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                      const SizedBox(height: 18),
                      Row(
                        crossAxisAlignment: CrossAxisAlignment.end,
                        children: [
                          // 조회수
                          Row(
                            children: [
                              const Icon(Icons.remove_red_eye, size: 16, color: Colors.grey),
                              const SizedBox(width: 4),
                              Text(
                                post.viewCount.toString(),
                                style: const TextStyle(fontSize: 13, color: Colors.grey),
                              ),
                            ],
                          ),
                          const Spacer(),
                          // 작성자 닉네임, 날짜
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.end,
                            children: [
                              Text(
                                post.nickname,
                                style: const TextStyle(fontSize: 13, color: Colors.black54),
                              ),
                              const SizedBox(height: 2),
                              Text(
                                post.createdAt.split("T")[0].replaceAll("-", "."),
                                style: const TextStyle(fontSize: 12, color: Colors.grey),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}

class _CommunityBottomNavBar extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final Color green = const Color(0xFF4CAF50);
    return BottomNavigationBar(
      type: BottomNavigationBarType.fixed,
      selectedItemColor: green,
      unselectedItemColor: Colors.black,
      showSelectedLabels: true,
      showUnselectedLabels: true,
      items: const [
        BottomNavigationBarItem(
          icon: Icon(Icons.home),
          label: '홈',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.pets),
          label: '산책예약',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.forum),
          label: '커뮤니티',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.account_circle),
          label: 'MY',
        ),
      ],
      onTap: (index) {
        switch (index) {
          case 0:
            Navigator.pushReplacementNamed(context, '/');
            break;
          case 1:
            Navigator.pushReplacementNamed(context, '/walkReservation');
            break;
          case 2:
            Navigator.pushReplacementNamed(context, '/community');
            break;
          case 3:
            Navigator.pushReplacementNamed(context, '/mypage');
            break;
        }
      },
      currentIndex: 2, // 커뮤니티 선택
    );
  }
} 