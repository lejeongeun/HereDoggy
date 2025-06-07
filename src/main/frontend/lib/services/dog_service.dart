import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/dog.dart';
import '../utils/constants.dart';

class DogService {
  Future<List<Dog>> getAllDogs() async {
    try {
      final response = await http.get(
        Uri.parse('${AppConstants.baseUrl}/dogs'),
      );
      
      if (response.statusCode == 200) {
        final List<dynamic> data = json.decode(response.body);
        return data.map((json) => Dog.fromJson(json)).toList();
      } else {
        throw Exception('Failed to load dogs: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Failed to load dogs: $e');
    }
  }

  Future<Dog> getDogDetails(int dogId) async {
    try {
      final response = await http.get(
        Uri.parse('${AppConstants.baseUrl}/dogs/$dogId'),
      );
      
      if (response.statusCode == 200) {
        return Dog.fromJson(json.decode(response.body));
      } else {
        throw Exception('Failed to load dog details: ${response.statusCode}');
      }
    } catch (e) {
      throw Exception('Failed to load dog details: $e');
    }
  }
} 