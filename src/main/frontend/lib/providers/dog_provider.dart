import 'package:flutter/foundation.dart';
import '../models/dog.dart';
import '../services/dog_service.dart';

class DogProvider with ChangeNotifier {
  final DogService _dogService = DogService();
  List<Dog> _dogs = [];
  bool _isLoading = false;
  String? _error;
  int _displayCount = 4;

  List<Dog> get dogs => _dogs;
  bool get isLoading => _isLoading;
  String? get error => _error;
  int get displayCount => _displayCount;

  Future<void> fetchDogs() async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _dogs = await _dogService.getAllDogs();
      _displayCount = _dogs.length < 4 ? _dogs.length : 4;
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<Dog?> fetchDogDetails(int dogId) async {
    try {
      return await _dogService.getDogDetails(dogId);
    } catch (e) {
      _error = e.toString();
      notifyListeners();
      return null;
    }
  }

  void loadMoreDogs({int count = 4}) {
    if (_displayCount < _dogs.length) {
      final nextCount = (_displayCount + count).clamp(0, _dogs.length);
      if (nextCount != _displayCount) {
        _displayCount = nextCount;
        notifyListeners();
      }
    }
  }
} 