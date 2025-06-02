class Dog {
  final int id;
  final String name;
  final int age;
  final String gender;
  final String personality;
  final double weight;
  final bool isNeutered;
  final String foundLocation;
  final List<String> imagesUrls;
  final String shelterName;

  Dog({
    required this.id,
    required this.name,
    required this.age,
    required this.gender,
    required this.personality,
    required this.weight,
    required this.isNeutered,
    required this.foundLocation,
    required this.imagesUrls,
    required this.shelterName,
  });

  factory Dog.fromJson(Map<String, dynamic> json) {
    return Dog(
      id: json['id'] is int ? json['id'] : int.parse(json['id'].toString()),
      name: json['name'] as String,
      age: json['age'] as int,
      gender: json['gender'] as String,
      personality: json['personality'] as String? ?? '',
      weight: (json['weight'] as num).toDouble(),
      isNeutered: json['isNeutered'] as bool,
      foundLocation: json['foundLocation'] as String,
      imagesUrls: List<String>.from(json['imagesUrls'] as List),
      shelterName: json['shelterName'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'age': age,
      'gender': gender,
      'personality': personality,
      'weight': weight,
      'isNeutered': isNeutered,
      'foundLocation': foundLocation,
      'imagesUrls': imagesUrls,
      'shelterName': shelterName,
    };
  }
} 