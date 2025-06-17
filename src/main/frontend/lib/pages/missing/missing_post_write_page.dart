import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:dio/dio.dart';
import '../../api/missing_post_api.dart';
import '../../models/missing_post.dart';
import 'dart:io';

class MissingPostWritePage extends StatefulWidget {
  final MissingPost? post; // null이면 작성, 있으면 수정
  const MissingPostWritePage({Key? key, this.post}) : super(key: key);

  @override
  State<MissingPostWritePage> createState() => _MissingPostWritePageState();
}

class _MissingPostWritePageState extends State<MissingPostWritePage> {
  final _formKey = GlobalKey<FormState>();
  MissingPostType _type = MissingPostType.missing;
  String _title = '';
  DogGender _gender = DogGender.unknown;
  int? _age;
  double? _weight;
  String? _furColor;
  String? _feature;
  DateTime? _missingDate;
  String _missingLocation = '';
  String _description = '';
  bool _isContactPublic = true;
  List<XFile> _images = [];
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    if (widget.post != null) {
      final p = widget.post!;
      _type = p.type;
      _title = p.title;
      _gender = p.gender;
      _age = p.age;
      _weight = p.weight;
      _furColor = p.furColor;
      _feature = p.feature;
      _missingDate = p.missingDate;
      _missingLocation = p.missingLocation;
      _description = p.description;
      _isContactPublic = p.isContactPublic;
      // 기존 이미지는 미리보기만 (수정 시)
    }
  }

  Future<void> _pickImages() async {
    final picker = ImagePicker();
    final picked = await picker.pickMultiImage(imageQuality: 80);
    if (picked != null && picked.isNotEmpty) {
      setState(() {
        _images = (picked.length > 5) ? picked.sublist(0, 5) : picked;
      });
    }
  }

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    if (_missingDate == null) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('실종/발견 날짜를 선택해주세요'), backgroundColor: Colors.red));
      return;
    }
    setState(() { _isLoading = true; });
    _formKey.currentState!.save();
    try {
      final data = {
        'type': _type == MissingPostType.missing ? 'MISSING' : 'FOUND',
        'title': _title,
        'gender': _gender == DogGender.male ? 'MALE' : _gender == DogGender.female ? 'FEMALE' : null,
        'age': _age,
        'weight': _weight,
        'furColor': _furColor,
        'feature': _feature,
        'missingDate': _missingDate!.toIso8601String().split('T')[0],
        'missingLocation': _missingLocation,
        'description': _description,
        'isContactPublic': _isContactPublic,
      };
      final images = _images.map((x) => MultipartFile.fromFileSync(x.path, filename: x.name)).toList();
      if (widget.post == null) {
        await MissingPostApi.createMissingPost(data, images);
      } else {
        await MissingPostApi.editMissingPost(widget.post!.id, data, images);
      }
      if (mounted) Navigator.pop(context, true);
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('저장 실패: $e'), backgroundColor: Colors.red));
    } finally {
      setState(() { _isLoading = false; });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.post == null ? '실종/발견 글쓰기' : '실종/발견 글수정'),
        centerTitle: true,
        leading: IconButton(
          icon: const Icon(Icons.close),
          onPressed: () => Navigator.pop(context),
        ),
        elevation: 0.5,
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // 실종/발견 타입 선택
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  ChoiceChip(
                    label: const Text('실종'),
                    selected: _type == MissingPostType.missing,
                    onSelected: (v) => setState(() => _type = MissingPostType.missing),
                  ),
                  const SizedBox(width: 16),
                  ChoiceChip(
                    label: const Text('발견'),
                    selected: _type == MissingPostType.found,
                    onSelected: (v) => setState(() => _type = MissingPostType.found),
                  ),
                ],
              ),
              const SizedBox(height: 20),
              // 제목
              TextFormField(
                initialValue: _title,
                decoration: const InputDecoration(labelText: '제목을 입력하세요'),
                validator: (v) => (v == null || v.isEmpty) ? '제목을 입력하세요' : null,
                onSaved: (v) => _title = v ?? '',
              ),
              const SizedBox(height: 16),
              // 성별/나이/몸무게/털색
              Row(
                children: [
                  Expanded(
                    child: DropdownButtonFormField<DogGender>(
                      value: _gender,
                      items: const [
                        DropdownMenuItem(value: DogGender.unknown, child: Text('성별 선택')),
                        DropdownMenuItem(value: DogGender.male, child: Text('수컷')),
                        DropdownMenuItem(value: DogGender.female, child: Text('암컷')),
                      ],
                      onChanged: (v) => setState(() => _gender = v ?? DogGender.unknown),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: TextFormField(
                      initialValue: _age?.toString(),
                      decoration: const InputDecoration(labelText: '나이'),
                      keyboardType: TextInputType.number,
                      onSaved: (v) => _age = int.tryParse(v ?? ''),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      initialValue: _weight?.toString(),
                      decoration: const InputDecoration(labelText: '몸무게(kg)'),
                      keyboardType: TextInputType.number,
                      onSaved: (v) => _weight = double.tryParse(v ?? ''),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: TextFormField(
                      initialValue: _furColor,
                      decoration: const InputDecoration(labelText: '털 색상'),
                      onSaved: (v) => _furColor = v,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              // 특징
              TextFormField(
                initialValue: _feature,
                decoration: const InputDecoration(labelText: '특이사항'),
                onSaved: (v) => _feature = v,
              ),
              const SizedBox(height: 16),
              // 실종/발견 날짜
              GestureDetector(
                onTap: () async {
                  final picked = await showDatePicker(
                    context: context,
                    initialDate: _missingDate ?? DateTime.now(),
                    firstDate: DateTime(2000),
                    lastDate: DateTime.now().add(const Duration(days: 365)),
                  );
                  if (picked != null) setState(() => _missingDate = picked);
                },
                child: AbsorbPointer(
                  child: TextFormField(
                    decoration: const InputDecoration(labelText: '실종/발견 날짜'),
                    controller: TextEditingController(
                      text: _missingDate == null ? '' : '${_missingDate!.year}.${_missingDate!.month.toString().padLeft(2, '0')}.${_missingDate!.day.toString().padLeft(2, '0')}',
                    ),
                    validator: (v) => (_missingDate == null) ? '날짜를 선택하세요' : null,
                  ),
                ),
              ),
              const SizedBox(height: 16),
              // 실종/발견 장소
              TextFormField(
                initialValue: _missingLocation,
                decoration: const InputDecoration(labelText: '실종/발견 장소'),
                validator: (v) => (v == null || v.isEmpty) ? '장소를 입력하세요' : null,
                onSaved: (v) => _missingLocation = v ?? '',
              ),
              const SizedBox(height: 16),
              // 설명
              TextFormField(
                initialValue: _description,
                decoration: const InputDecoration(labelText: '설명'),
                maxLines: 4,
                validator: (v) => (v == null || v.isEmpty) ? '설명을 입력하세요' : null,
                onSaved: (v) => _description = v ?? '',
              ),
              const SizedBox(height: 16),
              // 연락처 공개 여부
              Row(
                children: [
                  const Text('연락처 공개 여부:'),
                  const SizedBox(width: 16),
                  ChoiceChip(
                    label: const Text('공개'),
                    selected: _isContactPublic,
                    onSelected: (v) => setState(() => _isContactPublic = true),
                  ),
                  const SizedBox(width: 8),
                  ChoiceChip(
                    label: const Text('비공개'),
                    selected: !_isContactPublic,
                    onSelected: (v) => setState(() => _isContactPublic = false),
                  ),
                ],
              ),
              const SizedBox(height: 20),
              // 사진 업로드
              Row(
                children: [
                  ElevatedButton.icon(
                    onPressed: _images.length >= 5 ? null : _pickImages,
                    icon: const Icon(Icons.photo_camera),
                    label: const Text('사진'),
                  ),
                  const SizedBox(width: 12),
                  Text('(${_images.length}/5)'),
                ],
              ),
              const SizedBox(height: 8),
              if (_images.isNotEmpty)
                SizedBox(
                  height: 80,
                  child: ListView.separated(
                    scrollDirection: Axis.horizontal,
                    itemCount: _images.length,
                    separatorBuilder: (context, idx) => const SizedBox(width: 8),
                    itemBuilder: (context, idx) {
                      return Stack(
                        children: [
                          ClipRRect(
                            borderRadius: BorderRadius.circular(8),
                            child: Image.file(
                              File(_images[idx].path),
                              width: 80,
                              height: 80,
                              fit: BoxFit.cover,
                            ),
                          ),
                          Positioned(
                            top: 0,
                            right: 0,
                            child: GestureDetector(
                              onTap: () {
                                setState(() {
                                  _images.removeAt(idx);
                                });
                              },
                              child: Container(
                                decoration: BoxDecoration(
                                  color: Colors.black54,
                                  borderRadius: BorderRadius.circular(8),
                                ),
                                child: const Icon(Icons.close, color: Colors.white, size: 18),
                              ),
                            ),
                          ),
                        ],
                      );
                    },
                  ),
                ),
              const SizedBox(height: 32),
              // 완료 버튼
              SizedBox(
                width: double.infinity,
                height: 48,
                child: ElevatedButton(
                  onPressed: _isLoading ? null : _submit,
                  child: _isLoading
                      ? const CircularProgressIndicator(color: Colors.white)
                      : const Text('완료', style: TextStyle(fontSize: 18)),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
} 