import 'package:flutter/material.dart';
import 'package:daum_postcode_search/daum_postcode_search.dart';

class LocationSettingPage extends StatefulWidget {
  const LocationSettingPage({Key? key}) : super(key: key);

  @override
  State<LocationSettingPage> createState() => _LocationSettingPageState();
}

class _LocationSettingPageState extends State<LocationSettingPage> {
  String _currentAddress = '서울시 강남구 역삼1동';
  final TextEditingController _addressController = TextEditingController();

  @override
  void dispose() {
    _addressController.dispose();
    super.dispose();
  }

  Future<void> _findAddress() async {
    final result = await Navigator.of(context).push(
      MaterialPageRoute(builder: (context) => DaumPostcodeSearch()),
    );
    if (result != null && result.address != null) {
      setState(() {
        _currentAddress = result.address;
        _addressController.text = result.address;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('위치설정', style: TextStyle(fontWeight: FontWeight.bold)),
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
        elevation: 0,
        automaticallyImplyLeading: true,
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text('현재 위치', style: TextStyle(fontSize: 15, color: Colors.black)),
            const SizedBox(height: 8),
            Text(_currentAddress, style: const TextStyle(fontSize: 15, color: Colors.grey)),
            const SizedBox(height: 28),
            const Text('위치 찾기', style: TextStyle(fontSize: 15, color: Colors.black)),
            const SizedBox(height: 8),
            Row(
              children: [
                Expanded(
                  child: TextField(
                    controller: _addressController,
                    readOnly: true,
                    decoration: const InputDecoration(
                      hintText: '주소를 입력해주세요',
                      isDense: true,
                      contentPadding: EdgeInsets.symmetric(horizontal: 12, vertical: 10),
                      border: OutlineInputBorder(),
                    ),
                  ),
                ),
                const SizedBox(width: 8),
                SizedBox(
                  height: 40,
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.grey[300],
                      foregroundColor: Colors.black,
                      elevation: 0,
                      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 0),
                    ),
                    onPressed: _findAddress,
                    child: const Text('주소찾기', style: TextStyle(fontWeight: FontWeight.bold)),
                  ),
                ),
                const SizedBox(width: 4),
                Container(
                  width: 36,
                  height: 40,
                  decoration: BoxDecoration(
                    color: Colors.grey[200],
                    border: Border.all(color: Colors.grey[400]!),
                    borderRadius: BorderRadius.circular(4),
                  ),
                  child: const Icon(Icons.my_location, color: Colors.black54),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
} 