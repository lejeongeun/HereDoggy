import 'package:flutter/material.dart';
import '../../services/auth_service.dart';
import '../../components/auth/login_form.dart';

class WalkReservationPage extends StatefulWidget {
  const WalkReservationPage({Key? key}) : super(key: key);

  @override
  State<WalkReservationPage> createState() => _WalkReservationPageState();
}

class _WalkReservationPageState extends State<WalkReservationPage> {
  final _authService = AuthService();
  bool _isLoading = true;
  bool _isLoggedIn = false;
  bool _dialogShown = false;

  @override
  void initState() {
    super.initState();
    _checkLoginStatus();
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (!_isLoading && !_isLoggedIn && !_dialogShown) {
      _dialogShown = true;
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _showLoginDialog();
      });
    }
  }

  Future<void> _checkLoginStatus() async {
    final token = await _authService.getAccessToken();
    setState(() {
      _isLoggedIn = token != null;
      _isLoading = false;
    });
  }

  void _showLoginDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => Dialog(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Text(
                '로그인이 필요합니다',
                style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 16),
              LoginForm(
                onLoginSuccess: () {
                  Navigator.of(context).pop(); // 다이얼로그 닫기
                  _checkLoginStatus(); // 로그인 상태 새로고침
                },
              ),
            ],
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    if (_isLoading) {
      return const Scaffold(
        body: Center(
          child: CircularProgressIndicator(),
        ),
      );
    }

    if (!_isLoggedIn) {
      return const Scaffold(
        body: Center(
          child: Text('로그인이 필요합니다'),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text('산책 예약'),
        backgroundColor: Theme.of(context).primaryColor,
      ),
      body: const Center(
        child: Text(
          '산책 예약 페이지입니다',
          style: TextStyle(fontSize: 20),
        ),
      ),
    );
  }
} 