import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:geolocator/geolocator.dart';
import 'dart:async';
import 'dart:convert';
import 'dart:typed_data';
import 'dart:math' as math;
import 'package:http/http.dart' as http;
import '../../services/auth_service.dart';
import '../../services/walk_record_service.dart';
import '../../models/walk_record_point_dto.dart';
import '../../models/walk_record_start_request_dto.dart';
import '../../models/walk_record_end_request_dto.dart';
import '../../utils/constants.dart';
import '../../pages/walk_route/walk_result_page.dart';

class WalkRouteMapPage extends StatefulWidget {
  final int shelterId;
  final int walkRouteId;
  final int reservationId;
  const WalkRouteMapPage({
    Key? key, 
    required this.shelterId, 
    required this.walkRouteId,
    required this.reservationId,
  }) : super(key: key);

  @override
  State<WalkRouteMapPage> createState() => _WalkRouteMapPageState();
}

class _WalkRouteMapPageState extends State<WalkRouteMapPage> {
  GoogleMapController? _mapController;
  LatLng? _currentPosition;
  Marker? _myLocationMarker;
  List<LatLng> _walkRoutePath = [];
  List<LatLng> _myPath = [];
  List<WalkRecordPointDTO> _actualPath = [];
  StreamSubscription<Position>? _positionStream;
  bool _isLoading = true;
  String? _error;
  int _seconds = 0;
  Timer? _timer;
  final _authService = AuthService();
  final _walkRecordService = WalkRecordService();
  int? _walkRecordId;
  bool _isWalking = false;

  @override
  void initState() {
    super.initState();
    _fetchWalkRoute();
  }

  @override
  void dispose() {
    _positionStream?.cancel();
    _timer?.cancel();
    super.dispose();
  }

  void _startTimer() {
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      setState(() {
        _seconds++;
      });
    });
  }

  Future<void> _fetchWalkRoute() async {
    setState(() { _isLoading = true; _error = null; });
    try {
      final token = await _authService.getAccessToken();
      final response = await http.get(
        Uri.parse('${AppConstants.baseUrl}/members/reservations/${widget.reservationId}/walk-routes/${widget.walkRouteId}'),
        headers: {'Authorization': 'Bearer $token'},
      );
      
      if (response.statusCode == 200) {
        final data = json.decode(utf8.decode(response.bodyBytes));
        final points = data['points'] as List;
        _walkRoutePath = points.map((p) => LatLng(p['lat'], p['lng'])).toList();
        setState(() { _isLoading = false; });
        _determinePosition();
      } else {
        setState(() {
          _error = '경로 정보를 불러오지 못했습니다. (${response.statusCode})';
          _isLoading = false;
        });
      }
    } catch (e) {
      setState(() {
        _error = '네트워크 오류가 발생했습니다: ${e.toString()}';
        _isLoading = false;
      });
    }
  }

  Future<void> _determinePosition() async {
    bool serviceEnabled;
    LocationPermission permission;
    serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) return;
    permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) return;
    }
    if (permission == LocationPermission.deniedForever) return;
    // LocationAccuracy.high: 약 10-20m 정확도
    Position position = await Geolocator.getCurrentPosition(desiredAccuracy: LocationAccuracy.high);
    _updatePosition(position);
    _positionStream = Geolocator.getPositionStream(
      locationSettings: const LocationSettings(
        accuracy: LocationAccuracy.high,  // 약 10-20m 정확도
        distanceFilter: 10,  // 10m 이상 이동 시에만 위치 업데이트
      )
    ).listen((Position pos) {
      _updatePosition(pos);
    });
  }

  void _updatePosition(Position position) {
    LatLng newLatLng = LatLng(position.latitude, position.longitude);
    setState(() {
      _currentPosition = newLatLng;
      _myLocationMarker = Marker(
        markerId: const MarkerId('my_location'),
        position: newLatLng,
        infoWindow: const InfoWindow(title: '내 위치'),
        icon: BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueAzure),
      );
      // 10미터 이상 이동했을 때만 경로에 추가
      if (_myPath.isEmpty || _calculateDistance(_myPath.last, newLatLng) >= 10.0) {
        _myPath.add(newLatLng);
        _actualPath.add(WalkRecordPointDTO(
          latitude: position.latitude,
          longitude: position.longitude,
          recordedAt: DateTime.now().toIso8601String(),
        ));
      }
    });
  }

  double _calculateDistance(LatLng a, LatLng b) {
    const double earthRadius = 6371000; // meter
    double dLat = _deg2rad(b.latitude - a.latitude);
    double dLng = _deg2rad(b.longitude - a.longitude);
    double sindLat = math.sin(dLat / 2);
    double sindLng = math.sin(dLng / 2);
    double va = sindLat * sindLat + math.cos(_deg2rad(a.latitude)) * math.cos(_deg2rad(b.latitude)) * sindLng * sindLng;
    double c = 2 * math.atan2(math.sqrt(va), math.sqrt(1 - va));
    return earthRadius * c;
  }
  double _deg2rad(double deg) => deg * (math.pi / 180.0);

  double get _myTotalDistance {
    double dist = 0;
    for (int i = 1; i < _myPath.length; i++) {
      dist += _calculateDistance(_myPath[i - 1], _myPath[i]);
    }
    return dist;
  }

  String get _formattedTime {
    final min = (_seconds ~/ 60).toString().padLeft(2, '0');
    final sec = (_seconds % 60).toString().padLeft(2, '0');
    return '$min:$sec';
  }

  Future<void> _startWalk() async {
    try {
      final request = WalkRecordStartRequestDTO(
        reservationId: widget.reservationId,
        walkRouteId: widget.walkRouteId,
      );
      
      final response = await _walkRecordService.startWalk(request);
      setState(() {
        _walkRecordId = response.id;
        _isWalking = true;
      });
      
      _startTimer();
      _determinePosition();
      
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('산책이 시작되었습니다.')),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('산책 시작 실패: ${e.toString()}')),
        );
      }
    }
  }

  Future<void> _endWalk() async {
    if (_walkRecordId == null) return;
    
    try {
      // 타이머 정지
      _timer?.cancel();
      
      // 스크린샷 캡처
      final Uint8List? screenshot = await _mapController?.takeSnapshot();
      if (screenshot == null) throw Exception('스크린샷 캡처 실패');
      
      // MultipartFile 생성
      final imageFile = http.MultipartFile.fromBytes(
        'image',
        screenshot,
        filename: 'walk_route_${_walkRecordId}_${DateTime.now().millisecondsSinceEpoch}.png',
      );
      
      // 산책 종료 요청 데이터 생성
      final endRequest = WalkRecordEndRequestDTO(
        actualDistance: _myTotalDistance,
        actualDuration: _seconds,
        actualPath: _actualPath,
      );
      
      // 산책 종료 요청
      final response = await _walkRecordService.endWalk(
        walkRecordId: _walkRecordId!,
        request: endRequest,
        image: imageFile,
      );
      
      if (mounted) {
        // 산책 결과 페이지로 이동
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(
            builder: (context) => WalkResultPage(
              distance: _myTotalDistance,
              duration: _seconds,
              imageUrl: response.thumbnailUrl ?? '',
            ),
          ),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('산책 종료 실패: ${e.toString()}')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('산책 경로 지도'),
        backgroundColor: Colors.grey[300],
        foregroundColor: Colors.black,
        elevation: 0,
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Center(child: Text(_error!))
              : Stack(
                  children: [
                    GoogleMap(
                      onMapCreated: (controller) => _mapController = controller,
                      initialCameraPosition: CameraPosition(
                        target: _walkRoutePath.isNotEmpty ? _walkRoutePath.first : const LatLng(37.5665, 126.9780),
                        zoom: 16.0,
                      ),
                      myLocationEnabled: true,
                      myLocationButtonEnabled: true,
                      markers: _myLocationMarker != null ? {_myLocationMarker!} : {},
                      polylines: {
                        if (_walkRoutePath.isNotEmpty)
                          Polyline(
                            polylineId: const PolylineId('walk_route'),
                            color: Colors.green,
                            width: 5,
                            points: _walkRoutePath,
                          ),
                        if (_myPath.isNotEmpty)
                          Polyline(
                            polylineId: const PolylineId('my_path'),
                            color: Colors.blue,
                            width: 5,
                            points: _myPath,
                          ),
                      },
                    ),
                    Positioned(
                      left: 0,
                      right: 0,
                      bottom: 0,
                      child: Container(
                        color: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 32),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: [
                            Column(
                              children: [
                                const Text('거리(m)', style: TextStyle(fontWeight: FontWeight.bold)),
                                Text(_myTotalDistance.toStringAsFixed(0)),
                              ],
                            ),
                            if (!_isWalking)
                              ElevatedButton(
                                onPressed: _startWalk,
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.green,
                                  foregroundColor: Colors.white,
                                ),
                                child: const Text('산책 시작'),
                              )
                            else
                              ElevatedButton(
                                onPressed: _endWalk,
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.red,
                                  foregroundColor: Colors.white,
                                ),
                                child: const Text('산책 종료'),
                              ),
                            Column(
                              children: [
                                const Text('시간', style: TextStyle(fontWeight: FontWeight.bold)),
                                Text(_formattedTime),
                              ],
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
    );
  }
} 