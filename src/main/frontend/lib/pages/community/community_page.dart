import 'package:flutter/material.dart';
import '../../models/free_post.dart';
import '../../api/free_post_api.dart';
import 'package:provider/provider.dart';
import '../../providers/user_provider.dart';
import '../../models/missing_post.dart';
import '../../api/missing_post_api.dart';
import '../../utils/constants.dart';
import '../../models/review_post.dart';
import '../../api/review_post_api.dart';
import 'package:google_fonts/google_fonts.dart';

enum BoardType { all, free, missing, adoption }

class BoardPost {
  final int id;
  final String title;
  final String content;
  final String boardType; // 'free', 'missing', 'review', 'adoption'
  final int viewCount;
  final String email;
  final String nickname;
  final String createdAt;
  final List<String> imagesUrls;  // 이미지 URL 목록 추가
  final String? extraType; // 실종/발견 구분용

  BoardPost({
    required this.id,
    required this.title,
    required this.content,
    required this.boardType,
    required this.viewCount,
    required this.email,
    required this.nickname,
    required this.createdAt,
    this.imagesUrls = const [],  // 기본값으로 빈 리스트
    this.extraType,
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
      imagesUrls: post.imagesUrls,  // 이미지 URL 목록 추가
    );
  }

  factory BoardPost.fromMissingPost(MissingPost post) {
    return BoardPost(
      id: post.id,
      title: post.title,
      content: post.description,
      boardType: 'missing',
      viewCount: post.viewCount,
      email: post.writerEmail,
      nickname: post.writerNickname,
      createdAt: post.createdAt.toIso8601String(),
      imagesUrls: post.imagesUrls,
      extraType: post.type == MissingPostType.found ? 'found' : 'missing',
    );
  }

  factory BoardPost.fromReviewPost(ReviewPost post) {
    return BoardPost(
      id: post.id,
      title: post.title,
      content: post.content,
      boardType: 'review',
      viewCount: post.viewCount,
      email: post.email,
      nickname: post.nickname,
      createdAt: post.createdAt,
      imagesUrls: post.imageUrls,
      extraType: post.type, // 'ADOPTION' 또는 'WALK'
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
      final freePosts = await FreePostApi.fetchFreePosts();
      final missingPosts = await MissingPostApi.fetchMissingPosts();
      final allPosts = <BoardPost>[];
      allPosts.addAll(freePosts.map((e) => BoardPost.fromFreePost(e)));
      allPosts.addAll(missingPosts.map((e) => BoardPost.fromMissingPost(e)));
      // 최신순 정렬 (createdAt이 ISO8601 문자열이거나 날짜 객체)
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

  Future<void> fetchMissingBoardPosts() async {
    setState(() {
      isLoading = true;
      errorMessage = null;
    });
    try {
      final missingPosts = await MissingPostApi.fetchMissingPosts();
      setState(() {
        boardPosts = missingPosts.map((e) => BoardPost(
          id: e.id,
          title: e.title,
          content: e.description,
          boardType: 'missing',
          viewCount: e.viewCount,
          email: e.writerEmail,
          nickname: e.writerNickname,
          createdAt: e.createdAt.toIso8601String(),
          imagesUrls: e.imagesUrls,
          extraType: e.type == MissingPostType.found ? 'found' : 'missing',
        )).toList();
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        errorMessage = e.toString();
        isLoading = false;
      });
    }
  }

  Future<void> fetchReviewBoardPosts() async {
    setState(() {
      isLoading = true;
      errorMessage = null;
    });
    try {
      final reviewPosts = await ReviewPostApi.fetchReviewPosts();
      final boardList = reviewPosts.map((e) => BoardPost.fromReviewPost(e)).toList();
      // createdAt 기준 내림차순 정렬 (최신순)
      boardList.sort((a, b) => b.createdAt.compareTo(a.createdAt));
      setState(() {
        boardPosts = boardList;
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
    } else if (idx == 1) {
      fetchMissingBoardPosts();
    } else if (idx == 2) {
      fetchReviewBoardPosts();
    } else if (idx == 3) {
      fetchFreeBoardPosts();
    }
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
    String? route;
    if (selectedTab == 3) {
      route = '/free-post-write';
    } else if (selectedTab == 1) {
      route = '/missing-post-write';
    } else if (selectedTab == 2) {
      route = '/review-post-write';
    }
    if (route != null) {
      final result = await Navigator.pushNamed(context, route);
      if (result == true) {
        if (selectedTab == 3) {
          fetchFreeBoardPosts();
        } else if (selectedTab == 1) {
          fetchMissingBoardPosts();
        } else if (selectedTab == 2) {
          fetchReviewBoardPosts();
        }
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
      separatorBuilder: (context, index) => const Divider(height: 1, color: Color(0xFFE0E0E0)),
      itemBuilder: (context, index) {
        final post = boardPosts[index];
        // 말머리(카테고리) 텍스트
        String categoryLabel = '';
        if (post.boardType == 'free') {
          categoryLabel = '일반';
        } else if (post.boardType == 'missing') {
          if (post.extraType == 'found') {
            categoryLabel = '발견';
          } else {
            categoryLabel = '실종';
          }
        } else if (post.boardType == 'review') {
          if (post.extraType == 'ADOPTION') {
            categoryLabel = '입양 후기';
          } else if (post.extraType == 'WALK') {
            categoryLabel = '산책 후기';
          } else {
            categoryLabel = '후기';
          }
        }
        return InkWell(
          onTap: () async {
            String? detailRoute;
            if (post.boardType == 'free') {
              detailRoute = '/free-post-detail/${post.id}';
            } else if (post.boardType == 'missing') {
              detailRoute = '/missing-post-detail/${post.id}';
            } else if (post.boardType == 'review') {
              detailRoute = '/review-post-detail/${post.id}';
            }
            if (detailRoute != null) {
              final result = await Navigator.pushNamed(context, detailRoute);
              if (result == true) {
                if (selectedTab == 0) {
                  fetchAllPosts();
                } else if (selectedTab == 3) {
                  fetchFreeBoardPosts();
                } else if (selectedTab == 1) {
                  fetchMissingBoardPosts();
                } else if (selectedTab == 2) {
                  fetchReviewBoardPosts();
                }
              }
            }
          },
          child: Padding(
            padding: const EdgeInsets.symmetric(vertical: 18, horizontal: 18),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // 왼쪽: 텍스트 정보
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // 말머리(카테고리)
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                        decoration: BoxDecoration(
                          color: const Color(0xFFE8F5E9),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Text(
                          categoryLabel,
                          style: GoogleFonts.notoSansKr(
                            fontSize: 13,
                            fontWeight: FontWeight.w600,
                            color: const Color(0xFF388E3C),
                          ),
                        ),
                      ),
                      const SizedBox(height: 10),
                      // 제목
                      Text(
                        post.title,
                        style: GoogleFonts.notoSansKr(
                          fontSize: 17,
                          fontWeight: FontWeight.bold,
                          color: Colors.black87,
                        ),
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                      ),
                      const SizedBox(height: 8),
                      // 내용
                      Text(
                        post.content,
                        style: GoogleFonts.notoSansKr(
                          fontSize: 14,
                          color: Colors.grey[700],
                        ),
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                      ),
                      const SizedBox(height: 18),
                      // 작성자/날짜/조회수
                      Row(
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Icon(Icons.person_outline, size: 18, color: Colors.grey[500]),
                          const SizedBox(width: 4),
                          Text(
                            post.nickname,
                            style: GoogleFonts.notoSansKr(fontSize: 13, color: Colors.black54),
                          ),
                          const SizedBox(width: 10),
                          Icon(Icons.calendar_today_outlined, size: 16, color: Colors.grey[500]),
                          const SizedBox(width: 3),
                          Text(
                            post.createdAt.split("T")[0].replaceAll("-", "."),
                            style: GoogleFonts.notoSansKr(fontSize: 12, color: Colors.grey),
                          ),
                          const SizedBox(width: 10),
                          Icon(Icons.remove_red_eye_outlined, size: 16, color: Colors.grey[500]),
                          const SizedBox(width: 3),
                          Text(
                            post.viewCount.toString(),
                            style: GoogleFonts.notoSansKr(fontSize: 12, color: Colors.grey),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
                // 썸네일이 있을 때만 이미지 표시
                if (post.imagesUrls.isNotEmpty)
                  ...[
                    const SizedBox(width: 16),
                    ClipRRect(
                      borderRadius: BorderRadius.circular(12),
                      child: Image.network(
                        'http://192.168.10.128:8080${post.imagesUrls[0]}',
                        width: 80,
                        height: 80,
                        fit: BoxFit.cover,
                        errorBuilder: (context, error, stackTrace) {
                          return Container(
                            width: 80,
                            height: 80,
                            color: Colors.grey[200],
                            child: const SizedBox.shrink(),
                          );
                        },
                      ),
                    ),
                  ],
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
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [
          BoxShadow(
            color: Colors.black12,
            offset: Offset(0, -2),
            blurRadius: 12,
          ),
        ],
      ),
      child: BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        selectedItemColor: green,
        unselectedItemColor: Colors.black,
        showSelectedLabels: true,
        showUnselectedLabels: true,
        backgroundColor: Colors.white,
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
              Navigator.pushReplacementNamed(context, AppConstants.homeRoute);
              break;
            case 1:
              Navigator.pushNamed(context, AppConstants.walkReservationRoute);
              break;
            case 2:
              Navigator.pushReplacementNamed(context, AppConstants.communityRoute);
              break;
            case 3:
              Navigator.pushReplacementNamed(context, AppConstants.myPageRoute);
              break;
          }
        },
        currentIndex: 2, // 커뮤니티 선택
      ),
    );
  }
} 