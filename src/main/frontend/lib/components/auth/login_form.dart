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

  Future<void> _handleKakaoLogin() async {
    try {
      print('카카오 인가 코드 방식 로그인 시작');
      final response = await _authService.loginWithKakao();
      print('백엔드 응답: $response');
      if (response['success']) {
        final profile = await _authService.getProfile();
        if (profile != null && mounted) {
          Provider.of<UserProvider>(context, listen: false).login(profile);
          if (widget.onLoginSuccess != null) {
            widget.onLoginSuccess!();
          } else {
            Navigator.of(context, rootNavigator: true).pop();
          }
        }
      } else if (response['isNewUser'] == true) {
        if (mounted) {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (_) => RegisterForm(
                isSocialLogin: true,
                provider: 'kakao',
              ),
            ),
          );
        }
      } else {
        _showErrorDialog(response['message'] ?? '카카오 로그인에 실패했습니다.');
      }
    } catch (e) {
      print('카카오 로그인 에러: $e');
      _showErrorDialog('카카오 로그인에 실패했습니다.');
    }
  }

  Future<void> _handleGoogleLogin() async {
    try {
      print('구글 로그인 시작 (인가 코드 방식)');
      
      // 새로운 인가 코드 방식 사용
      final response = await _authService.loginWithGoogle();
      print('백엔드 응답: $response');
      
      if (response['success']) {
        print('로그인 성공, 프로필 정보 요청');
        final profile = await _authService.getProfile();
        if (profile != null && mounted) {
          Provider.of<UserProvider>(context, listen: false).login(profile);
          if (widget.onLoginSuccess != null) {
            widget.onLoginSuccess!();
          } else {
            Navigator.of(context, rootNavigator: true).pop();
          }
        }
      } else if (response['isNewUser'] == true) {
        print('회원가입 필요, 회원가입 폼으로 이동');
        if (mounted) {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (_) => RegisterForm(
                isSocialLogin: true,
                provider: 'google',
              ),
            ),
          );
        }
      } else {
        print('로그인 실패: ${response['message']}');
        _showErrorDialog(response['message'] ?? '구글 로그인에 실패했습니다.');
      }
    } catch (e, stackTrace) {
      print('구글 로그인 에러: $e');
      print('스택 트레이스: $stackTrace');
      _showErrorDialog('구글 로그인에 실패했습니다. (${e.toString()})');
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
              hintText: '가입한 이메일 주소 입력',
              hintStyle: const TextStyle(color: Colors.grey, fontSize: 16),
              filled: true,
              fillColor: Colors.white,
              contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
              enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: const BorderSide(color: Colors.grey, width: 1),
              ),
              focusedBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: const BorderSide(color: Colors.black, width: 1),
              ),
              errorBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: const BorderSide(color: Colors.red, width: 1),
              ),
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: const BorderSide(color: Colors.grey, width: 1),
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
              hintText: '비밀번호 입력',
              hintStyle: const TextStyle(color: Colors.grey, fontSize: 16),
              filled: true,
              fillColor: Colors.white,
              contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
              enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: const BorderSide(color: Colors.grey, width: 1),
              ),
              focusedBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: const BorderSide(color: Colors.black, width: 1),
              ),
              errorBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: const BorderSide(color: Colors.red, width: 1),
              ),
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: const BorderSide(color: Colors.grey, width: 1),
              ),
            ),
          ),
          const SizedBox(height: 24),
          SizedBox(
            height: 56,
            child: ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: const Color(0xFFF5F5F5), // 연한 회색 배경
                foregroundColor: Colors.black, // 검정색 텍스트
                elevation: 0,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
              ),
              onPressed: _isLoading ? null : _handleLogin,
              child: _isLoading
                  ? const CircularProgressIndicator(color: Colors.black)
                  : const Text('로그인하기', style: TextStyle(fontSize: 16, fontWeight: FontWeight.normal)),
            ),
          ),
        ],
      ),
    );
  }
}

class LoginFullScreenPage extends StatefulWidget {
  const LoginFullScreenPage({Key? key}) : super(key: key);

  @override
  State<LoginFullScreenPage> createState() => _LoginFullScreenPageState();
}

class _LoginFullScreenPageState extends State<LoginFullScreenPage> {
  final _loginFormKey = GlobalKey<_LoginFormState>();

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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      resizeToAvoidBottomInset: false,
      body: SafeArea(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 16),
            IconButton(
              icon: const Icon(Icons.arrow_back, size: 24, color: Colors.black),
              onPressed: () => Navigator.pop(context),
            ),
            const SizedBox(height: 16),
            const Padding(
              padding: EdgeInsets.symmetric(horizontal: 24.0),
              child: Text(
                '로그인',
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold, color: Colors.black),
              ),
            ),
            const SizedBox(height: 32),
            // 로그인 폼 및 소셜 로그인
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 24.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    LoginForm(key: _loginFormKey),
                    const SizedBox(height: 32),
                    // 소셜 로그인
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Container(
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black.withOpacity(0.1),
                                spreadRadius: 1,
                                blurRadius: 4,
                                offset: const Offset(0, 2),
                              ),
                            ],
                          ),
                          child: CircleAvatar(
                            backgroundColor: Colors.white,
                            radius: 28,
                            child: IconButton(
                              icon: SvgPicture.asset(
                                'assets/icons/google.svg',
                                width: 32,
                                height: 32,
                              ),
                              onPressed: () => _loginFormKey.currentState?._handleGoogleLogin(),
                            ),
                          ),
                        ),
                        const SizedBox(width: 32),
                        Container(
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black.withOpacity(0.1),
                                spreadRadius: 1,
                                blurRadius: 4,
                                offset: const Offset(0, 2),
                              ),
                            ],
                          ),
                          child: CircleAvatar(
                            backgroundColor: const Color(0xFFFFEB3B),
                            radius: 28,
                            child: IconButton(
                              icon: SvgPicture.asset(
                                'assets/icons/kakao.svg',
                                width: 32,
                                height: 32,
                              ),
                              onPressed: () => _loginFormKey.currentState?._handleKakaoLogin(),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
            // 하단 고정: 회원가입/아이디찾기/비밀번호재설정
            Container(
              padding: const EdgeInsets.symmetric(vertical: 16.0),
              child: Center(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
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
    );
  }
} 