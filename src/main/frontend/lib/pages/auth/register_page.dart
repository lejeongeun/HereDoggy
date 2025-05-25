import 'package:flutter/material.dart';
import '../../components/common/buttons/primary_button.dart';
import '../../components/common/buttons/secondary_button.dart';
import '../../components/common/inputs/text_field.dart';
import '../../utils/constants.dart';

class RegisterPage extends StatefulWidget {
  const RegisterPage({Key? key}) : super(key: key);

  @override
  State<RegisterPage> createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final _confirmPasswordController = TextEditingController();
  bool _isLoading = false;

  @override
  void dispose() {
    _nameController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    _confirmPasswordController.dispose();
    super.dispose();
  }

  void _handleRegister() {
    if (_formKey.currentState!.validate()) {
      setState(() {
        _isLoading = true;
      });
      // TODO: 회원가입 로직 구현
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
        title: const Text('회원가입'),
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
              // 이름 입력
              CustomTextField(
                label: '이름',
                controller: _nameController,
                prefixIcon: const Icon(Icons.person),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '이름을 입력해주세요';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 16),
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
              const SizedBox(height: 16),
              // 비밀번호 확인 입력
              CustomTextField(
                label: '비밀번호 확인',
                controller: _confirmPasswordController,
                obscureText: true,
                prefixIcon: const Icon(Icons.lock_outline),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return '비밀번호를 다시 입력해주세요';
                  }
                  if (value != _passwordController.text) {
                    return '비밀번호가 일치하지 않습니다';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 24),
              // 회원가입 버튼
              PrimaryButton(
                text: '회원가입',
                onPressed: _handleRegister,
                isLoading: _isLoading,
              ),
              const SizedBox(height: 16),
              // 로그인 버튼
              SecondaryButton(
                text: '이미 계정이 있으신가요? 로그인',
                onPressed: () {
                  Navigator.pop(context);
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
} 