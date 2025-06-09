import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:geolocator/geolocator.dart';
import 'dart:math' as math;

class MapTestPage2 extends StatefulWidget {
  const MapTestPage2({Key? key}) : super(key: key);

  @override
  State<MapTestPage2> createState() => _MapTestPage2State();
}

class _MapTestPage2State extends State<MapTestPage2> {
  static const LatLng _defaultCenter = LatLng(37.5665, 126.9780); // 서울시청
  GoogleMapController? _mapController;
  LatLng? _currentPosition;
  Marker? _myLocationMarker;
  List<LatLng> _path = [];
  Stream<Position>? _positionStream;

  @override
  void initState() {
    super.initState();
    _determinePosition();
  }

  Future<void> _determinePosition() async {
    bool serviceEnabled;
    LocationPermission permission;

    // 위치 서비스 활성화 여부 확인
    serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      _showLocationError('위치 서비스가 비활성화되어 있습니다. 설정에서 위치를 켜주세요.');
      return;
    }

    // 권한 확인 및 요청
    permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        _showLocationError('위치 권한이 거부되었습니다.');
        return;
      }
    }
    if (permission == LocationPermission.deniedForever) {
      _showLocationError('위치 권한이 영구적으로 거부되었습니다. 설정에서 권한을 허용해주세요.');
      return;
    }

    // 현재 위치 가져오기
    Position position = await Geolocator.getCurrentPosition(desiredAccuracy: LocationAccuracy.high);
    _updatePosition(position);

    // 위치 스트림 구독 (1m 이상 이동 시만 경로 추가)
    _positionStream = Geolocator.getPositionStream(
      locationSettings: const LocationSettings(accuracy: LocationAccuracy.high, distanceFilter: 1),
    );
    _positionStream!.listen((Position pos) {
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
      // 1m 이상 이동 시만 경로에 추가
      if (_path.isEmpty || _calculateDistance(_path.last, newLatLng) >= 1.0) {
        _path.add(newLatLng);
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

  void _showLocationError(String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('위치 오류'),
        content: Text(message),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('확인'),
          ),
        ],
      ),
    );
  }

  void _onMapCreated(GoogleMapController controller) {
    _mapController = controller;
    // 위치를 이미 받아온 경우, 지도 카메라 이동
    if (_currentPosition != null) {
      _mapController!.animateCamera(
        CameraUpdate.newLatLng(_currentPosition!),
      );
    }
  }

  void _resetPath() {
    setState(() {
      _path.clear();
    });
  }

  @override
  void dispose() {
    super.dispose();
    // 스트림 구독 해제 (메모리 누수 방지)
    _positionStream = null;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('구글 지도 테스트2'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            tooltip: '경로 초기화',
            onPressed: _resetPath,
          ),
        ],
      ),
      body: GoogleMap(
        onMapCreated: _onMapCreated,
        initialCameraPosition: CameraPosition(
          target: _currentPosition ?? _defaultCenter,
          zoom: 14.0,
        ),
        myLocationEnabled: true,
        myLocationButtonEnabled: true,
        markers: _myLocationMarker != null ? {_myLocationMarker!} : {},
        polylines: {
          Polyline(
            polylineId: const PolylineId('move_path'),
            color: Colors.blue,
            width: 5,
            points: _path,
          ),
        },
      ),
    );
  }
} 