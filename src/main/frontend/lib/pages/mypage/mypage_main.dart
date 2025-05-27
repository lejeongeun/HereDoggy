import 'package:flutter/material.dart';

class MypageMain extends StatelessWidget {
  const MypageMain({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('마이페이지'),
        backgroundColor: Theme.of(context).primaryColor,
      ),
      body: ListView(
        children: [
          // 프로필 섹션
          Container(
            padding: const EdgeInsets.all(16),
            child: Row(
              children: [
                CircleAvatar(
                  radius: 40,
                  backgroundColor: Colors.grey[300],
                  child: const Icon(Icons.person, size: 40),
                ),
                const SizedBox(width: 16),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      '사용자 이름',
                      style: Theme.of(context).textTheme.titleLarge,
                    ),
                    const SizedBox(height: 4),
                    Text(
                      'user@email.com',
                      style: Theme.of(context).textTheme.bodyMedium,
                    ),
                  ],
                ),
              ],
            ),
          ),
          const Divider(),
          // 활동 내역 섹션
          ListTile(
            leading: const Icon(Icons.directions_walk),
            title: const Text('산책 내역'),
            trailing: const Icon(Icons.chevron_right),
            onTap: () {
              // 산책 내역 페이지로 이동
            },
          ),
          ListTile(
            leading: const Icon(Icons.favorite),
            title: const Text('후원 내역'),
            trailing: const Icon(Icons.chevron_right),
            onTap: () {
              // 후원 내역 페이지로 이동
            },
          ),
          ListTile(
            leading: const Icon(Icons.pets),
            title: const Text('입양 내역'),
            trailing: const Icon(Icons.chevron_right),
            onTap: () {
              // 입양 내역 페이지로 이동
            },
          ),
          const Divider(),
          // 설정 섹션
          ListTile(
            leading: const Icon(Icons.notifications),
            title: const Text('알림 설정'),
            trailing: const Icon(Icons.chevron_right),
            onTap: () {
              // 알림 설정 페이지로 이동
            },
          ),
          ListTile(
            leading: const Icon(Icons.settings),
            title: const Text('계정 설정'),
            trailing: const Icon(Icons.chevron_right),
            onTap: () {
              // 계정 설정 페이지로 이동
            },
          ),
        ],
      ),
    );
  }
} 