import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../../utils/constants.dart';
import '../../services/auth_service.dart';

class TossPaymentPage extends StatefulWidget {
  const TossPaymentPage({Key? key}) : super(key: key);

  @override
  State<TossPaymentPage> createState() => _TossPaymentPageState();
}

class _TossPaymentPageState extends State<TossPaymentPage> {
  late final WebViewController _controller;
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setNavigationDelegate(
        NavigationDelegate(
          onNavigationRequest: (request) async {
            final url = request.url;
            if (url.contains("/donations/success")) {
              final uri = Uri.parse(url);
              final paymentKey = uri.queryParameters['paymentKey'];
              final orderId = uri.queryParameters['orderId'];
              final amount = uri.queryParameters['amount'];
              if (paymentKey != null && orderId != null && amount != null) {
                final authService = AuthService();
                final accessToken = await authService.getAccessToken();
                final response = await http.post(
                  Uri.parse("${AppConstants.baseUrl}/donations/success"),
                  headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "Authorization": "Bearer $accessToken"
                  },
                  body: {
                    "paymentKey": paymentKey,
                    "orderId": orderId,
                    "amount": amount
                  },
                );
                if (response.statusCode == 200) {
                  Navigator.pop(context, true);
                } else {
                  Navigator.pop(context, false);
                }
              } else {
                Navigator.pop(context, false);
              }
              return NavigationDecision.prevent;
            } else if (url.contains("/donations/fail")) {
              Navigator.pop(context, false);
              return NavigationDecision.prevent;
            }
            return NavigationDecision.navigate;
          },
          onPageFinished: (_) {
            setState(() {
              _isLoading = false;
            });
          },
        ),
      );
    _startDonation();
  }

  Future<void> _startDonation() async {
    try {
      final authService = AuthService();
      final accessToken = await authService.getAccessToken();
      if (accessToken == null) {
        _showErrorAndPop("로그인이 필요합니다. 로그인 후 다시 시도해주세요.");
        return;
      }
      final url = Uri.parse("${AppConstants.baseUrl}/donations/request");
      print("요청 URL: $url");
      final response = await http.post(
        url,
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer $accessToken",
        },
        body: jsonEncode({
          "amount": 10000,
          "orderName": "HereDoggy 유기견 후원",
        }),
      );
      print("응답 상태 코드: ${response.statusCode}");
      print("응답 바디: ${response.body}");
      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final paymentUrl = data["tossPayment"] ?? data["paymentUrl"];
        if (paymentUrl != null) {
          print("결제 URL: $paymentUrl");
          _controller.loadRequest(Uri.parse(paymentUrl));
        } else {
          _showErrorAndPop("결제 URL을 받아오지 못했습니다. 응답: ${response.body}");
        }
      } else if (response.statusCode == 401) {
        _showErrorAndPop("인증이 만료되었거나 유효하지 않습니다. 다시 로그인 해주세요.");
      } else {
        _showErrorAndPop("후원 결제 요청 실패 (${response.statusCode}): ${response.body}");
      }
    } catch (e) {
      print("API 호출 중 에러 발생: $e");
      _showErrorAndPop("네트워크 오류가 발생했습니다: $e");
    }
  }

  void _showErrorAndPop(String message) {
    print("에러 메시지: $message");
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message)),
    );
    Navigator.pop(context, false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("후원 결제"),
      ),
      body: Stack(
        children: [
          WebViewWidget(controller: _controller),
          if (_isLoading)
            const Center(child: CircularProgressIndicator()),
        ],
      ),
    );
  }
} 