import 'package:flutter/material.dart';
import '../../components/common/inputs/text_field.dart';
import 'auth_button.dart';
import '../../services/auth_service.dart';
import 'register_form.dart';
import 'package:provider/provider.dart';
import '../../providers/user_provider.dart';

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
            Navigator.pushReplacementNamed(context, '/mypage');
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
          CustomTextField(
            label: '이메일',
            controller: _emailController,
            keyboardType: TextInputType.emailAddress,
            prefixIcon: const Icon(Icons.email),
            validator: (value) {
              if (value == null || value.isEmpty) {
                return '이메일을 입력해주세요';
              }
              if (!value.contains('@')) {
                return '유효한 이메일 주소를 입력해주세요';
              }
              return null;
            },
          ),
          const SizedBox(height: 16),
          CustomTextField(
            label: '비밀번호',
            controller: _passwordController,
            obscureText: true,
            prefixIcon: const Icon(Icons.lock),
            validator: (value) {
              if (value == null || value.isEmpty) {
                return '비밀번호를 입력해주세요';
              }
              return null;
            },
          ),
          const SizedBox(height: 24),
          AuthButton(
            text: '로그인',
            onPressed: _handleLogin,
            isLoading: _isLoading,
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
                icon: const Icon(Icons.arrow_back, size: 32),
                onPressed: () => Navigator.pop(context),
              ),
              const SizedBox(height: 16),
              const Text(
                '로그인',
                style: TextStyle(fontSize: 32, fontWeight: FontWeight.bold),
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
                    child: const Text('Google', style: TextStyle(color: Colors.black)),
                  ),
                  const SizedBox(width: 32),
                  CircleAvatar(
                    backgroundColor: Color(0xFFFFEB3B),
                    radius: 28,
                    child: const Text('Kakao', style: TextStyle(color: Colors.black)),
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