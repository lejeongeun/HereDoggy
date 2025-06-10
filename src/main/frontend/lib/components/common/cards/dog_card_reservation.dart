import 'package:flutter/material.dart';
import 'dog_card.dart';

class DogCardReservation extends StatelessWidget {
  final String imageUrl;
  final String name;
  final int age;
  final String gender; // "수컷" 또는 "암컷"
  final double weight;
  final String shelterName;
  final String reservationDate;
  final String reservationTime;
  final VoidCallback? onTap;

  const DogCardReservation({
    Key? key,
    required this.imageUrl,
    required this.name,
    required this.age,
    required this.gender,
    required this.weight,
    required this.shelterName,
    required this.reservationDate,
    required this.reservationTime,
    this.onTap,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      child: InkWell(
        onTap: onTap,
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              ClipRRect(
                borderRadius: BorderRadius.circular(8),
                child: Image.network(
                  imageUrl,
                  width: 110,
                  height: 80,
                  fit: BoxFit.cover,
                  errorBuilder: (context, error, stackTrace) {
                    return Container(
                      width: 110,
                      height: 80,
                      color: Colors.grey[300],
                      child: const Icon(Icons.pets, size: 40),
                    );
                  },
                ),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      '$name | $gender',
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                    const SizedBox(height: 4),
                    Text(
                      '${age}살 / ${weight}kg',
                      style: Theme.of(context).textTheme.bodyMedium,
                    ),
                    const SizedBox(height: 4),
                    Row(
                      children: [
                        const Text('보호소 ', style: TextStyle(fontWeight: FontWeight.bold)),
                        Text(shelterName, style: const TextStyle(color: Colors.black54)),
                      ],
                    ),
                    const SizedBox(height: 4),
                    Row(
                      children: [
                        const Text('예약 날짜 ', style: TextStyle(fontWeight: FontWeight.bold)),
                        Text(reservationDate, style: const TextStyle(color: Colors.black87)),
                      ],
                    ),
                    Row(
                      children: [
                        const Text('예약 시간 ', style: TextStyle(fontWeight: FontWeight.bold)),
                        Text(reservationTime, style: const TextStyle(color: Colors.black87)),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
} 