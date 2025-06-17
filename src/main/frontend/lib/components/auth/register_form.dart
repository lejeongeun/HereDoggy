import 'package:flutter/material.dart';
import 'package:daum_postcode_search/daum_postcode_search.dart';
import '../../services/auth_service.dart';

class RegisterForm extends StatefulWidget {
  final String? preFilledEmail;
  final String? preFilledName;
  final String? preFilledBirth;
  final bool isSocialLogin;
  final String? provider;

  const RegisterForm({
    Key? key,
    this.preFilledEmail,
    this.preFilledName,
    this.preFilledBirth,
    this.isSocialLogin = false,
    this.provider,
  }) : super(key: key);

  @override
  State<RegisterForm> createState() => _RegisterFormState();
}

class _RegisterFormState extends State<RegisterForm> {
  final _formKey = GlobalKey<FormState>();
  final _emailController = TextEditingController();
  final _nameController = TextEditingController();
  final _birthController = TextEditingController();
  final _nicknameController = TextEditingController();
  final _passwordController = TextEditingController();
  final _passwordCheckController = TextEditingController();
  final _phoneController = TextEditingController();
  final _zipcodeController = TextEditingController();
  final _addressController = TextEditingController();
  final _addressDetailController = TextEditingController();
  bool _isLoading = false;
  final _authService = AuthService();

  @override
  void initState() {
    super.initState();
    // 카카오에서 받은 정보 미리 채우기
    if (widget.preFilledEmail != null) {
      _emailController.text = widget.preFilledEmail!;
    }
    if (widget.preFilledName != null) {
      _nameController.text = widget.preFilledName!;
    }
    if (widget.preFilledBirth != null) {
      _birthController.text = widget.preFilledBirth!;
    }
  }

  @override
  void dispose() {
    _emailController.dispose();
    _nameController.dispose();
    _birthController.dispose();
    _nicknameController.dispose();
    _passwordController.dispose();
    _passwordCheckController.dispose();
    _phoneController.dispose();
    _zipcodeController.dispose();
    _addressController.dispose();
    _addressDetailController.dispose();
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

  Future<void> _handleRegister() async {
    if (_formKey.currentState!.validate()) {
      setState(() { _isLoading = true; });
      try {
        final result = await _authService.register(
          email: _emailController.text,
          password: widget.isSocialLogin ? null : _passwordController.text,
          passwordCheck: widget.isSocialLogin ? null : _passwordCheckController.text,
          name: _nameController.text,
          nickname: _nicknameController.text,
          birth: _birthController.text,
          phone: _phoneController.text,
          zipcode: _zipcodeController.text,
          address1: _addressController.text,
          address2: _addressDetailController.text,
          provider: widget.isSocialLogin ? (widget.provider ?? 'kakao') : 'local',
        );

        if (!result['success']) {
          _showErrorDialog(result['message']);
        } else if (mounted) {
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(builder: (_) => const RegisterSuccessPage()),
          );
        }
      } finally {
        if (mounted) {
          setState(() { _isLoading = false; });
        }
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 16),
          child: Form(
            key: _formKey,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    IconButton(
                      icon: const Icon(Icons.arrow_back, size: 32, color: Color(0xFF388E3C)),
                      onPressed: () => Navigator.pop(context),
                    ),
                    const SizedBox(width: 8),
                    Text(
                      widget.isSocialLogin ? '추가 정보 입력' : '회원가입',
                      style: const TextStyle(fontSize: 32, fontWeight: FontWeight.bold, color: Color(0xFF388E3C))
                    ),
                  ],
                ),
                const SizedBox(height: 24),
                _buildLabel('이메일'),
                _buildTextField(_emailController, '이메일을 입력해주세요', validator: (v) {
                  if (v == null || v.isEmpty) return '이메일을 입력해주세요';
                  if (!v.contains('@')) return '유효한 이메일을 입력해주세요';
                  return null;
                }),
                const SizedBox(height: 16),
                _buildLabel('이름'),
                _buildTextField(_nameController, '이름을 입력해주세요', validator: (v) => (v == null || v.isEmpty) ? '이름을 입력해주세요' : null),
                const SizedBox(height: 16),
                _buildLabel('생년월일'),
                _buildTextField(_birthController, '생년월일을 입력해주세요 (예: 2000-01-01)', validator: (v) => (v == null || v.isEmpty) ? '생년월일을 입력해주세요' : null),
                const SizedBox(height: 16),
                _buildLabel('닉네임'),
                _buildTextField(_nicknameController, '닉네임을 입력해주세요', validator: (v) => (v == null || v.isEmpty) ? '닉네임을 입력해주세요' : null),
                if (!widget.isSocialLogin) ...[
                  const SizedBox(height: 16),
                  _buildLabel('비밀번호'),
                  _buildTextField(_passwordController, '숫자, 영문을 포함하여 8자리 이상',
                    obscureText: true,
                    validator: (v) {
                      if (v == null || v.isEmpty) return '비밀번호를 입력해주세요';
                      if (v.length < 8) return '8자리 이상 입력해주세요';
                      if (!RegExp(r'^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$').hasMatch(v)) return '숫자, 영문을 포함해야 합니다';
                      return null;
                    }),
                  const SizedBox(height: 16),
                  _buildLabel('비밀번호 재확인'),
                  _buildTextField(_passwordCheckController, '숫자, 영문을 포함하여 8자리 이상',
                    obscureText: true,
                    validator: (v) {
                      if (v == null || v.isEmpty) return '비밀번호를 재입력해주세요';
                      if (v != _passwordController.text) return '비밀번호가 일치하지 않습니다';
                      return null;
                    }),
                ],
                const SizedBox(height: 16),
                _buildLabel('전화번호'),
                _buildTextField(_phoneController, '전화번호를 입력해주세요', validator: (v) => (v == null || v.isEmpty) ? '전화번호를 입력해주세요' : null),
                const SizedBox(height: 16),
                _buildLabel('주소'),
                Row(
                  children: [
                    Expanded(
                      flex: 2,
                      child: _buildTextField(_zipcodeController, '우편번호', readOnly: true, validator: (v) => (v == null || v.isEmpty) ? '우편번호를 입력해주세요' : null),
                    ),
                    const SizedBox(width: 8),
                    SizedBox(
                      height: 48,
                      child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: const Color(0xFF4CAF50),
                          foregroundColor: Colors.white,
                          elevation: 2,
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(12),
                          ),
                        ),
                        onPressed: () async {
                          final result = await Navigator.of(context).push(
                            MaterialPageRoute(
                              builder: (context) => DaumPostcodeSearch(),
                            ),
                          );
                          if (result != null) {
                            _zipcodeController.text = result.zonecode ?? '';
                            _addressController.text = result.address ?? '';
                          }
                        },
                        child: const Text('주소찾기', style: TextStyle(fontWeight: FontWeight.bold)),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 8),
                _buildTextField(_addressController, '주소를 입력해주세요', readOnly: true, validator: (v) => (v == null || v.isEmpty) ? '주소를 입력해주세요' : null),
                const SizedBox(height: 8),
                _buildTextField(_addressDetailController, '상세주소를 입력해주세요', validator: (v) => (v == null || v.isEmpty) ? '상세주소를 입력해주세요' : null),
                const SizedBox(height: 32),
                SizedBox(
                  width: double.infinity,
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
                    onPressed: _isLoading ? null : _handleRegister,
                    child: _isLoading
                        ? const CircularProgressIndicator(color: Colors.white)
                        : Text(
                            widget.isSocialLogin ? '추가 정보 입력 완료' : '아이디 만들기',
                            style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)
                          ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildLabel(String text) => Padding(
    padding: const EdgeInsets.only(left: 2, bottom: 4),
    child: Text(text, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16, color: Color(0xFF388E3C))),
  );

  Widget _buildTextField(
    TextEditingController controller,
    String hint, {
    bool obscureText = false,
    bool readOnly = false,
    String? Function(String?)? validator,
  }) {
    return TextFormField(
      controller: controller,
      obscureText: obscureText,
      readOnly: readOnly || (controller == _emailController && widget.isSocialLogin),
      validator: validator,
      decoration: InputDecoration(
        hintText: hint,
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
    );
  }
}

class RegisterSuccessPage extends StatelessWidget {
  const RegisterSuccessPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFE0E0E0),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text('어서오개의 회원이 되신걸 축하해요! 🐶', style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
            const SizedBox(height: 32),
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.grey[400],
                foregroundColor: Colors.black,
                elevation: 0,
                padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 16),
              ),
              onPressed: () {
                Navigator.of(context).popUntil((route) => route.isFirst);
              },
              child: const Text('메인으로', style: TextStyle(fontSize: 18)),
            ),
          ],
        ),
      ),
    );
  }
} 