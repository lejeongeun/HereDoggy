import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  Future<String> fetchMessage() async {
    final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/hello'));
    if (response.statusCode == 200) {
      return response.body;
    } else {
      throw Exception('Failed to load message');
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('Spring Boot + Flutter')),
        body: Center(
          child: FutureBuilder<String>(
            future: fetchMessage(),
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return CircularProgressIndicator();
              } else if (snapshot.hasError) {
                return Text('❌ 오류: ${snapshot.error}');
              } else {
                return Text('✅ 응답: ${snapshot.data}');
              }
            },
          ),
        ),
      ),
    );
  }
}
