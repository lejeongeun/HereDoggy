import 'package:flutter/foundation.dart';
import '../models/dog.dart';
import '../services/dog_service.dart';

class DogProvider with ChangeNotifier {
  final DogService _dogService = DogService();
  List<Dog> _dogs = [];
  bool _isLoading = false;
  String? _error;

  List<Dog> get dogs => _dogs;
  bool get isLoading => _isLoading;
  String? get error => _error;

  Future<void> fetchDogs() async {
    _isLoading = true;
    _error = null;
    notifyListeners();

    try {
      _dogs = await _dogService.getAllDogs();
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
} 