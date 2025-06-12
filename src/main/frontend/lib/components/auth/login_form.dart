import 'package:flutter/material.dart';
import 'auth_button.dart';
import '../../services/auth_service.dart';
import 'register_form.dart';
import 'package:provider/provider.dart';
import '../../providers/user_provider.dart';
import 'package:flutter_svg/flutter_svg.dart';

class LoginForm extends StatefulWidget {
  final VoidCallback? onLoginSuccess;
  
  const LoginForm({
    Key? key,
    this.onLoginSuccess,
  }) : super(key: key);

  @override
  State<LoginForm> createState() => _LoginFormState();
}

class _LoginFormState extends State<LoginForm> {
  final _formKey = GlobalKey<FormState>();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final _authService = AuthService();
  bool _isLoading = false;

  @override
  void dispose() {
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  void _showErrorDialog(String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('오류'),
        content: Text(message),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('확인'),
          ),
        ],
      ),
    );
  }

  Future<void> _handleLogin() async {
    if (_formKey.currentState!.validate()) {
      setState(() {
        _isLoading = true;
      });

      try {
        final result = await _authService.login(
          _emailController.text,
          _passwordController.text,
        );

        if (!result['success']) {
          _showErrorDialog(result['message']);
        } else {
          // 로그인 성공 시 토큰 저장 후 프로필 정보 받아오기
          final profile = await _authService.getProfile();
          if (profile != null && mounted) {
            Provider.of<UserProvider>(context, listen: false).login(profile);
            if (widget.onLoginSuccess != null) {
              widget.onLoginSuccess!();
            } else {
              Navigator.of(context, rootNavigator: true).pop();
            }
          } else {
            _showErrorDialog('회원 정보를 불러오지 못했습니다.');
          }
        }
      } finally {
        if (mounted) {
          setState(() {
            _isLoading = false;
          });
        }
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          TextFormField(
            controller: _emailController,
            keyboardType: TextInputType.emailAddress,
            validator: (value) {
              if (value == null || value.isEmpty) {
                return '이메일을 입력해주세요';
              }
              if (!value.contains('@')) {
                return '유효한 이메일 주소를 입력해주세요';
              }
              return null;
            },
            decoration: InputDecoration(
              labelText: '이메일',
              labelStyle: const TextStyle(color: Color(0xFF388E3C), fontWeight: FontWeight.bold),
              prefixIcon: const Icon(Icons.email, color: Color(0xFF388E3C)),
              filled: true,
              fillColor: Colors.white,
              contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
              enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: const BorderSide(color: Color(0xFF81C784), width: 1.5),
              ),
              focusedBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: const BorderSide(color: Color(0xFF388E3C), width: 2),
              ),
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: BorderSide.none,
              ),
            ),
          ),
          const SizedBox(height: 16),
          TextFormField(
            controller: _passwordController,
            obscureText: true,
            validator: (value) {
              if (value == null || value.isEmpty) {
                return '비밀번호를 입력해주세요';
              }
              return null;
            },
            decoration: InputDecoration(
              labelText: '비밀번호',
              labelStyle: const TextStyle(color: Color(0xFF388E3C), fontWeight: FontWeight.bold),
              prefixIcon: const Icon(Icons.lock, color: Color(0xFF388E3C)),
              filled: true,
              fillColor: Colors.white,
              contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
              enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: const BorderSide(color: Color(0xFF81C784), width: 1.5),
              ),
              focusedBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: const BorderSide(color: Color(0xFF388E3C), width: 2),
              ),
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
                borderSide: BorderSide.none,
              ),
            ),
          ),
          const SizedBox(height: 24),
          SizedBox(
            height: 56,
            child: ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFF4CAF50),
                foregroundColor: Colors.white,
                elevation: 3,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(16),
                ),
              ),
              onPressed: _isLoading ? null : _handleLogin,
              child: _isLoading
                  ? const CircularProgressIndicator(color: Colors.white)
                  : const Text('로그인', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
            ),
          ),
        ],
      ),
    );
  }
}

class LoginFullScreenPage extends StatelessWidget {
  const LoginFullScreenPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 24.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const SizedBox(height: 16),
              IconButton(
                icon: const Icon(Icons.arrow_back, size: 32, color: Color(0xFF388E3C)),
                onPressed: () => Navigator.pop(context),
              ),
              const SizedBox(height: 16),
              const Text(
                '로그인',
                style: TextStyle(fontSize: 32, fontWeight: FontWeight.bold, color: Color(0xFF388E3C)),
              ),
              const SizedBox(height: 32),
              // 로그인 폼
              LoginForm(),
              const SizedBox(height: 32),
              // 소셜 로그인
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircleAvatar(
                    backgroundColor: Colors.white,
                    radius: 28,
                    child: SvgPicture.asset(
                      'assets/icons/google.svg',
                      width: 32,
                      height: 32,
                    ),
                  ),
                  const SizedBox(width: 32),
                  CircleAvatar(
                    backgroundColor: const Color(0xFFFFEB3B),
                    radius: 28,
                    child: SvgPicture.asset(
                      'assets/icons/kakao.svg',
                      width: 32,
                      height: 32,
                    ),
                  ),
                ],
              ),
              const Spacer(),
              Center(
                child: Padding(
                  padding: const EdgeInsets.only(bottom: 16.0),
                  child: Wrap(
                    alignment: WrapAlignment.center,
                    spacing: 8,
                    children: [
                      TextButton(
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (_) => const RegisterForm(),
                            ),
                          );
                        },
                        child: const Text('회원가입', style: TextStyle(color: Colors.black54)),
                      ),
                      const Text('|', style: TextStyle(color: Colors.black54)),
                      TextButton(
                        onPressed: () {},
                        child: const Text('아이디 찾기', style: TextStyle(color: Colors.black54)),
                      ),
                      const Text('|', style: TextStyle(color: Colors.black54)),
                      TextButton(
                        onPressed: () {},
                        child: const Text('비밀번호 재설정', style: TextStyle(color: Colors.black54)),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
} 