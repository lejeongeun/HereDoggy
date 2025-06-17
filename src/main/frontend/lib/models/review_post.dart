class ReviewPost {
  final int id;
  final String title;
  final String content;
  final String type; // WALK, ADOPTION
  final int rank; // 별점(1~5)
  final int viewCount;
  final String email;
  final String nickname;
  final String createdAt;
  final List<String> imageUrls;

  ReviewPost({
    required this.id,
    required this.title,
    required this.content,
    required this.type,
    required this.rank,
    required this.viewCount,
    required this.email,
    required this.nickname,
    required this.createdAt,
    this.imageUrls = const [],
  });

  factory ReviewPost.fromJson(Map<String, dynamic> json) {
    return ReviewPost(
      id: json['id'] as int,
      title: json['title'] as String,
      content: json['content'] as String,
      type: json['type'] as String,
      rank: json['rank'] as int,
      viewCount: json['viewCount'] is int ? json['viewCount'] : int.tryParse(json['viewCount'].toString()) ?? 0,
      email: json['email'] as String,
      nickname: json['nickname'] as String,
      createdAt: json['createdAt'] as String,
      imageUrls: json['imageUrls'] != null ? List<String>.from(json['imageUrls']) : [],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'content': content,
      'type': type,
      'rank': rank,
      'viewCount': viewCount,
      'email': email,
      'nickname': nickname,
      'createdAt': createdAt,
      'imageUrls': imageUrls,
    };
  }
} 