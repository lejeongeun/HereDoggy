import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'walk_route_map_page.dart';

class WalkRouteSelectPage extends StatefulWidget {
  final int shelterId;
  const WalkRouteSelectPage({Key? key, required this.shelterId}) : super(key: key);

  @override
  State<WalkRouteSelectPage> createState() => _WalkRouteSelectPageState();
}

class _WalkRouteSelectPageState extends State<WalkRouteSelectPage> {
  bool _isLoading = true;
  String? _error;
  List<dynamic> _routes = [];
  int? _selectedRouteId;

  @override
  void initState() {
    super.initState();
    _fetchRoutes();
  }

  Future<void> _fetchRoutes() async {
    setState(() { _isLoading = true; _error = null; });
    try {
      final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/shelters/${widget.shelterId}/walk-routes'));
      if (response.statusCode == 200) {
        setState(() {
          _routes = json.decode(utf8.decode(response.bodyBytes));
          _isLoading = false;
        });
      } else {
        setState(() {
          _error = '산책 경로를 불러오지 못했습니다.';
          _isLoading = false;
        });
      }
    } catch (e) {
      setState(() {
        _error = '네트워크 오류가 발생했습니다.';
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final Color green = const Color(0xFF4CAF50);
    return Scaffold(
      appBar: AppBar(
        title: const Text('산책 경로 선택'),
        backgroundColor: Colors.grey[300],
        foregroundColor: Colors.black,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Center(child: Text(_error!))
              : Column(
                  children: [
                    Expanded(
                      child: ListView.builder(
                        itemCount: _routes.length,
                        itemBuilder: (context, idx) {
                          final route = _routes[idx];
                          final isSelected = _selectedRouteId == route['id'];
                          return GestureDetector(
                            onTap: () {
                              setState(() { _selectedRouteId = route['id']; });
                            },
                            child: Container(
                              margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                              padding: const EdgeInsets.all(12),
                              decoration: BoxDecoration(
                                color: Colors.grey[100],
                                border: Border.all(
                                  color: isSelected ? green : Colors.transparent,
                                  width: 3,
                                ),
                                borderRadius: BorderRadius.circular(16),
                              ),
                              child: Row(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Container(
                                    width: 80,
                                    height: 80,
                                    decoration: BoxDecoration(
                                      color: Colors.grey[300],
                                      borderRadius: BorderRadius.circular(12),
                                    ),
                                    // 썸네일 비워둠
                                  ),
                                  const SizedBox(width: 16),
                                  Expanded(
                                    child: Column(
                                      crossAxisAlignment: CrossAxisAlignment.start,
                                      children: [
                                        Text('${route['totalDistance'] ?? '-'}km', style: const TextStyle(fontSize: 16)),
                                        Text('${route['expectedDuration'] ?? '-'}분', style: const TextStyle(fontSize: 16)),
                                        Text(route['description'] ?? '-', style: const TextStyle(fontSize: 15, color: Colors.black87)),
                                      ],
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          );
                        },
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 16),
                      child: SizedBox(
                        width: double.infinity,
                        height: 48,
                        child: ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            backgroundColor: green,
                            foregroundColor: Colors.white,
                            textStyle: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                          ),
                          onPressed: _selectedRouteId == null ? null : () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (_) => WalkRouteMapPage(
                                  shelterId: widget.shelterId,
                                  walkRouteId: _selectedRouteId!,
                                ),
                              ),
                            );
                          },
                          child: const Text('산책 시작'),
                        ),
                      ),
                    ),
                  ],
                ),
    );
  }
} 