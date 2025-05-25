import 'package:flutter/material.dart';
import '../../components/common/buttons/primary_button.dart';
import '../../components/common/buttons/secondary_button.dart';
import '../../components/common/inputs/text_field.dart';
import '../../utils/constants.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({Key? key}) : super(key: key);

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final _formKey = GlobalKey<FormState>();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  bool _isLoading = false;

  @override
  void dispose() {
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  void _handleLogin() {
    if (_formKey.currentState!.validate()) {
      setState(() {
        _isLoading = true;
      });
      // TODO: 로그인 로직 구현
      Future.delayed(const Duration(seconds: 2), () {
        setState(() {
          _isLoading = false;
        });
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('로그인'),
        backgroundColor: Theme.of(context).primaryColor,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              const SizedBox(height: 32),
              // 로고 또는 이미지
              Center(
                child: Icon(
                  Icons.pets,
                  size: 80,
                  color: Theme.of(context).primaryColor,
                ),
              ),
              const SizedBox(height: 32),
              // 이메일 입력
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
              // 비밀번호 입력
              CustomTextField(
                label: '비밀번호',
                controller: _passwordController,
                obscureText: true,
                prefixIcon: const Icon(Icons.lock),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '비밀번호를 입력해주세요';
                  }
                  if (value.length < 6) {
                    return '비밀번호는 6자 이상이어야 합니다';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 24),
              // 로그인 버튼
              PrimaryButton(
                text: '로그인',
                onPressed: _handleLogin,
                isLoading: _isLoading,
              ),
              const SizedBox(height: 16),
              // 회원가입 버튼
              SecondaryButton(
                text: '계정이 없으신가요? 회원가입',
                onPressed: () {
                  Navigator.pushNamed(context, AppConstants.registerRoute);
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
} 