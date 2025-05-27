import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/user_provider.dart';
import '../../components/auth/login_form.dart';
import '../home/home_page.dart';

class MyPage extends StatelessWidget {
  const MyPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final userProvider = Provider.of<UserProvider>(context);
    final isLoggedIn = userProvider.isLoggedIn;
    final Color green = const Color(0xFF4CAF50);
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        title: Text('마이페이지', style: TextStyle(color: green, fontWeight: FontWeight.bold)),
        actions: [
          IconButton(
            icon: const Icon(Icons.settings, color: Colors.black),
            onPressed: () {}, // 추후 구현
          ),
        ],
        iconTheme: const IconThemeData(color: Colors.black),
      ),
      body: isLoggedIn
          ? Column(
              children: [
                InkWell(
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => const ProfileEditPage(),
                      ),
                    );
                  },
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 24),
                    child: Row(
                      children: [
                        const CircleAvatar(
                          radius: 32,
                          backgroundColor: Colors.black,
                          child: Icon(Icons.person, color: Colors.white, size: 40),
                        ),
                        const SizedBox(width: 16),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                '${userProvider.user?['name'] ?? '사용자'}님 안녕하세요',
                                style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.black87),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                userProvider.user?['email'] ?? '',
                                style: TextStyle(fontSize: 14, color: green.withOpacity(0.7)),
                              ),
                            ],
                          ),
                        ),
                        const Icon(Icons.chevron_right, size: 32, color: Colors.black54),
                      ],
                    ),
                  ),
                ),
                const Divider(thickness: 1, height: 1),
                const Expanded(child: SizedBox()),
              ],
            )
          : Column(
              children: [
                InkWell(
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => const LoginFullScreenPage(),
                      ),
                    );
                  },
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 24),
                    child: Row(
                      children: [
                        const CircleAvatar(
                          radius: 32,
                          backgroundColor: Colors.black,
                          child: Icon(Icons.person, color: Colors.white, size: 40),
                        ),
                        const SizedBox(width: 16),
                        const Expanded(
                          child: Text(
                            '로그인을 해주세요',
                            style: TextStyle(fontSize: 18, color: Colors.black87),
                          ),
                        ),
                        const Icon(Icons.chevron_right, size: 32, color: Colors.black54),
                      ],
                    ),
                  ),
                ),
                const Divider(thickness: 1, height: 1),
                // 나머지 공간 비우기
                const Expanded(child: SizedBox()),
              ],
            ),
      bottomNavigationBar: _HomeBottomNavBar(currentIndex: 3),
    );
  }
}

// _HomeBottomNavBar를 재사용할 수 있도록 수정
class _HomeBottomNavBar extends StatelessWidget {
  final int currentIndex;
  const _HomeBottomNavBar({this.currentIndex = 0, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final Color green = const Color(0xFF4CAF50);
    return BottomNavigationBar(
      type: BottomNavigationBarType.fixed,
      selectedItemColor: green,
      unselectedItemColor: Colors.black,
      showSelectedLabels: true,
      showUnselectedLabels: true,
      currentIndex: currentIndex,
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
    );
  }
}

// 프로필 수정 페이지
class ProfileEditPage extends StatelessWidget {
  const ProfileEditPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final userProvider = Provider.of<UserProvider>(context, listen: false);
    final Color green = const Color(0xFF4CAF50);
    final String nickname = userProvider.user?['nickname'] ?? '';
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.black),
          onPressed: () => Navigator.pop(context),
        ),
        title: Text('프로필 수정', style: TextStyle(color: green, fontWeight: FontWeight.bold)),
        centerTitle: false,
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            const SizedBox(height: 24),
            Center(
              child: Stack(
                children: [
                  const CircleAvatar(
                    radius: 48,
                    backgroundColor: Colors.black,
                    child: Icon(Icons.person, color: Colors.white, size: 60),
                  ),
                  Positioned(
                    right: 0,
                    bottom: 0,
                    child: Container(
                      decoration: BoxDecoration(
                        color: Colors.white,
                        shape: BoxShape.circle,
                        boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 2)],
                      ),
                      child: const Padding(
                        padding: EdgeInsets.all(4.0),
                        child: Icon(Icons.camera_alt, color: Colors.black54, size: 22),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 32),
            Align(
              alignment: Alignment.centerLeft,
              child: Text('닉네임', style: TextStyle(fontSize: 16, color: Colors.black87)),
            ),
            const SizedBox(height: 8),
            TextField(
              enabled: false,
              controller: TextEditingController(text: nickname),
              decoration: InputDecoration(
                filled: true,
                fillColor: Colors.grey[200],
                border: InputBorder.none,
                contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
              ),
              style: const TextStyle(fontSize: 18, color: Colors.black87),
            ),
            const SizedBox(height: 24),
            const Divider(thickness: 1),
            ListTile(
              title: const Text('비밀번호 변경', style: TextStyle(fontSize: 16)),
              trailing: const Icon(Icons.chevron_right),
              onTap: () {},
            ),
            ListTile(
              title: const Text('회원탈퇴', style: TextStyle(fontSize: 16, color: Colors.red)),
              trailing: const Icon(Icons.chevron_right),
              onTap: () {},
            ),
            const Spacer(),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.grey[300],
                  foregroundColor: Colors.black,
                  padding: const EdgeInsets.symmetric(vertical: 16),
                  textStyle: const TextStyle(fontSize: 18),
                  elevation: 0,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(4)),
                ),
                onPressed: () {
                  userProvider.logout();
                  Navigator.pushNamedAndRemoveUntil(context, '/', (route) => false);
                },
                child: const Text('로그아웃'),
              ),
            ),
            const SizedBox(height: 24),
          ],
        ),
      ),
    );
  }
} 