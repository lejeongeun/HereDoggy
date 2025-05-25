import 'package:flutter/material.dart';

class ShelterListPage extends StatelessWidget {
  const ShelterListPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('보호소 찾기'),
        backgroundColor: Theme.of(context).primaryColor,
      ),
      body: const Center(
        child: Text(
          '보호소 찾기 페이지입니다',
          style: TextStyle(fontSize: 20),
        ),
      ),
    );
  }
} 