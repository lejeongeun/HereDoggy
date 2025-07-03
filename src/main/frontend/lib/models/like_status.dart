class LikeStatus {
  final int likeCount;
  final bool isLiked;

  LikeStatus({required this.likeCount, required this.isLiked});

  factory LikeStatus.fromJson(Map<String, dynamic> json) {
    return LikeStatus(
      likeCount: json['likeCount'] as int? ?? 0,
      isLiked: json['isLiked'] as bool? ?? false,
    );
  }
} 