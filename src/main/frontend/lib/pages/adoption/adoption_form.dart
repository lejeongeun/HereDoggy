import 'package:flutter/material.dart';
import 'adoption_complete.dart';

class AdoptionFormPage extends StatefulWidget {
  const AdoptionFormPage({Key? key}) : super(key: key);

  @override
  State<AdoptionFormPage> createState() => _AdoptionFormPageState();
}

class _AdoptionFormPageState extends State<AdoptionFormPage> {
  final _formKey = GlobalKey<FormState>();
  String? _gender;
  String? _maritalStatus;
  bool _hasPetsBefore = false;
  bool _hasCurrentPets = false;
  bool _canProvideProperCare = false;
  bool _canKeepUntilEnd = false;
  bool _agreeToRegularChecks = false;
  bool _agreeToAdoptionTerms = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('입양 신청', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
        backgroundColor: const Color(0xFFFF9800),
        iconTheme: const IconThemeData(color: Colors.white),
        elevation: 0,
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Form(
            key: _formKey,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _buildTextField('성명', '성명을 입력하세요'),
                _buildTextField('생년월일', '생년월일을 입력하세요 (예: 980121)'),
                _buildGenderSelection(),
                _buildTextField('주소', '주소를 입력하세요'),
                _buildTextField('연락처', '연락처를 입력하세요'),
                _buildTextField('직업', '직업을 입력하세요'),
                _buildMaritalStatusSelection(),
                const SizedBox(height: 20),
                _buildYesNoQuestion('반려동물을 키워본 적이 있습니까?', _hasPetsBefore, (value) {
                  setState(() => _hasPetsBefore = value!);
                }),
                if (_hasPetsBefore)
                  _buildTextField('어떤 반려동물을 키웠나요?', '종류와 기간을 입력해주세요'),
                _buildYesNoQuestion('현재 집에 다른 동물을 키우고 있습니까?', _hasCurrentPets, (value) {
                  setState(() => _hasCurrentPets = value!);
                }),
                if (_hasCurrentPets)
                  _buildTextField('현재 키우는 동물', '종류와 수를 입력해주세요'),
                _buildTextField('가족 구성원은 어떻게 되십니까?', '가족 구성원을 입력해주세요'),
                _buildAdoptionTypeSelection(),
                _buildTextField('입양을 원하는 이유가 무엇입니까?', '입양 사유를 입력해주세요'),
                _buildYesNoQuestion('입양 후 입양동물의 사진 및 소식을 전해주실 수 있으십니까?', _canProvideProperCare, (value) {
                  setState(() => _canProvideProperCare = value!);
                }),
                _buildYesNoQuestion('반려동물이 자연사할 때까지 끝을 지켜주실 수 있습니까?', _canKeepUntilEnd, (value) {
                  setState(() => _canKeepUntilEnd = value!);
                }),
                _buildYesNoQuestion('반려동물의 불임 수술 시행에 동의하십니까?', _agreeToRegularChecks, (value) {
                  setState(() => _agreeToRegularChecks = value!);
                }),
                _buildYesNoQuestion('입양 시 반려동물에 대한 책임과 남부에 동의하십니까?', _agreeToAdoptionTerms, (value) {
                  setState(() => _agreeToAdoptionTerms = value!);
                }),
                const SizedBox(height: 20),
                Center(
                  child: ElevatedButton(
                    onPressed: () {
                      if (_formKey.currentState!.validate()) {
                        // 완료 페이지로 이동
                        Navigator.of(context).push(
                          MaterialPageRoute(
                            builder: (context) => const AdoptionCompletePage(),
                          ),
                        );
                      }
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFFFF9800),
                      padding: const EdgeInsets.symmetric(horizontal: 50, vertical: 15),
                    ),
                    child: const Text('제출하기', style: TextStyle(color: Colors.white)),
                  ),
                ),
                const SizedBox(height: 30),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildTextField(String label, String hint) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 16.0),
      child: TextFormField(
        decoration: InputDecoration(
          labelText: label,
          hintText: hint,
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8),
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8),
            borderSide: const BorderSide(color: Color(0xFFFF9800)),
          ),
          labelStyle: const TextStyle(color: Colors.black87),
        ),
        validator: (value) {
          if (value == null || value.isEmpty) {
            return '$label을(를) 입력해주세요';
          }
          return null;
        },
      ),
    );
  }

  Widget _buildGenderSelection() {
    return Padding(
      padding: const EdgeInsets.only(bottom: 16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text('성별', style: TextStyle(fontSize: 16)),
          Row(
            children: [
              Radio<String>(
                value: '남성',
                groupValue: _gender,
                onChanged: (value) => setState(() => _gender = value),
                activeColor: const Color(0xFFFF9800),
              ),
              const Text('남성'),
              const SizedBox(width: 20),
              Radio<String>(
                value: '여성',
                groupValue: _gender,
                onChanged: (value) => setState(() => _gender = value),
                activeColor: const Color(0xFFFF9800),
              ),
              const Text('여성'),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildMaritalStatusSelection() {
    return Padding(
      padding: const EdgeInsets.only(bottom: 16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text('결혼여부', style: TextStyle(fontSize: 16)),
          Row(
            children: [
              Radio<String>(
                value: '기혼',
                groupValue: _maritalStatus,
                onChanged: (value) => setState(() => _maritalStatus = value),
                activeColor: const Color(0xFFFF9800),
              ),
              const Text('기혼'),
              const SizedBox(width: 20),
              Radio<String>(
                value: '미혼',
                groupValue: _maritalStatus,
                onChanged: (value) => setState(() => _maritalStatus = value),
                activeColor: const Color(0xFFFF9800),
              ),
              const Text('미혼'),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildAdoptionTypeSelection() {
    return Padding(
      padding: const EdgeInsets.only(bottom: 16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text('가족들은 유기동물 입양을 찬성하십니까?', style: TextStyle(fontSize: 16)),
          Row(
            children: [
              Radio<String>(
                value: '모두찬성',
                groupValue: _maritalStatus,
                onChanged: (value) => setState(() => _maritalStatus = value),
                activeColor: const Color(0xFFFF9800),
              ),
              const Text('모두찬성'),
              const SizedBox(width: 20),
              Radio<String>(
                value: '부분찬성',
                groupValue: _maritalStatus,
                onChanged: (value) => setState(() => _maritalStatus = value),
                activeColor: const Color(0xFFFF9800),
              ),
              const Text('부분찬성'),
              const SizedBox(width: 20),
              Radio<String>(
                value: '모두반대',
                groupValue: _maritalStatus,
                onChanged: (value) => setState(() => _maritalStatus = value),
                activeColor: const Color(0xFFFF9800),
              ),
              const Text('모두반대'),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildYesNoQuestion(String question, bool value, Function(bool?) onChanged) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(question, style: const TextStyle(fontSize: 16)),
          Row(
            children: [
              Radio<bool>(
                value: true,
                groupValue: value,
                onChanged: onChanged,
                activeColor: const Color(0xFFFF9800),
              ),
              const Text('네'),
              const SizedBox(width: 20),
              Radio<bool>(
                value: false,
                groupValue: value,
                onChanged: onChanged,
                activeColor: const Color(0xFFFF9800),
              ),
              const Text('아니오'),
            ],
          ),
        ],
      ),
    );
  }
} 