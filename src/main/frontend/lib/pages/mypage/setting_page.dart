import 'package:flutter/material.dart';
import 'location_setting_page.dart';
import 'inquiry_page.dart';

class SettingPage extends StatefulWidget {
  const SettingPage({Key? key}) : super(key: key);

  @override
  State<SettingPage> createState() => _SettingPageState();
}

class _SettingPageState extends State<SettingPage> {
  bool _notificationOn = true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('설정', style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
        elevation: 0,
        automaticallyImplyLeading: true,
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // 알림설정
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text('알림설정', style: TextStyle(fontSize: 16, fontWeight: FontWeight.w500, color: Colors.black)),
                Switch(
                  value: _notificationOn,
                  onChanged: (val) {
                    setState(() {
                      _notificationOn = val;
                    });
                  },
                  activeColor: Colors.white, // 썸(동그라미) 색상
                  activeTrackColor: Colors.black, // ON 트랙(배경)
                  inactiveThumbColor: Colors.white, // OFF 썸(동그라미)
                  inactiveTrackColor: Colors.grey, // OFF 트랙(배경)
                  materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
                ),
              ],
            ),
            const SizedBox(height: 2),
            const Padding(
              padding: EdgeInsets.only(left: 2),
              child: Text(
                '어서오개의 모든 알림을 설정해요',
                style: TextStyle(fontSize: 13, color: Colors.grey),
              ),
            ),
            const SizedBox(height: 28),
            // 위치설정
            ListTile(
              contentPadding: EdgeInsets.zero,
              title: const Text('위치설정', style: TextStyle(fontSize: 16)),
              trailing: const Icon(Icons.chevron_right, color: Colors.black, size: 26),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => const LocationSettingPage(),
                  ),
                );
              },
            ),
            const SizedBox(height: 8),
            // 문의하기
            ListTile(
              contentPadding: EdgeInsets.zero,
              title: const Text('문의하기', style: TextStyle(fontSize: 16)),
              trailing: const Icon(Icons.chevron_right, color: Colors.black, size: 26),
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => const InquiryPage(),
                  ),
                );
              },
            ),
          ],
        ),
      ),
    );
  }
} 