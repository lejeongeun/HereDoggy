import 'package:flutter/material.dart';
import '../../components/common/inputs/text_field.dart';
import 'auth_button.dart';

class LoginForm extends StatefulWidget {
  const LoginForm({Key? key}) : super(key: key);

  @override
  State<LoginForm> createState() => _LoginFormState();
}

class _LoginFormState extends State<LoginForm> {
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