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
  final int likeCount;
  final bool isLiked;

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
    this.likeCount = 0,
    this.isLiked = false,
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
      createdAt: json['createdAt']?.toString() ?? '',
      imageUrls: json['imageUrls'] != null ? List<String>.from(json['imageUrls']) : [],
      likeCount: json['likeCount'] as int? ?? 0,
      isLiked: json['isLiked'] as bool? ?? false,
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
      'likeCount': likeCount,
      'isLiked': isLiked,
    };
  }

  // 좋아요 상태를 업데이트하는 메서드
  ReviewPost copyWith({
    int? id,
    String? title,
    String? content,
    String? type,
    int? rank,
    int? viewCount,
    String? email,
    String? nickname,
    String? createdAt,
    List<String>? imageUrls,
    int? likeCount,
    bool? isLiked,
  }) {
    return ReviewPost(
      id: id ?? this.id,
      title: title ?? this.title,
      content: content ?? this.content,
      type: type ?? this.type,
      rank: rank ?? this.rank,
      viewCount: viewCount ?? this.viewCount,
      email: email ?? this.email,
      nickname: nickname ?? this.nickname,
      createdAt: createdAt ?? this.createdAt,
      imageUrls: imageUrls ?? this.imageUrls,
      likeCount: likeCount ?? this.likeCount,
      isLiked: isLiked ?? this.isLiked,
    );
  }
} 