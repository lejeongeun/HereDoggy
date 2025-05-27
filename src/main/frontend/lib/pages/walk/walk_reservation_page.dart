import 'package:flutter/material.dart';

class WalkReservationPage extends StatelessWidget {
  const WalkReservationPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('산책 예약'),
        backgroundColor: Theme.of(context).primaryColor,
      ),
      body: const Center(
        child: Text(
          '산책 예약 페이지입니다',
          style: TextStyle(fontSize: 20),
        ),
      ),
    );
  }
} 