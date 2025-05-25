import 'package:flutter/material.dart';

class AdoptionPage extends StatelessWidget {
  const AdoptionPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('입양')),
      body: const Center(child: Text('입양페이지입니다')),
    );
  }
} 