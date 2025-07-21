class FreePost {
  final int id;
  final String title;
  final String content;
  final int viewCount;
  final String email;
  final String nickname;
  final String createdAt;
  final List<String> imagesUrls;
  final int likeCount;
  final bool isLiked;

  FreePost({
    required this.id,
    required this.title,
    required this.content,
    required this.viewCount,
    required this.email,
    required this.nickname,
    required this.createdAt,
    this.imagesUrls = const [],
    this.likeCount = 0,
    this.isLiked = false,
  });

  factory FreePost.fromJson(Map<String, dynamic> json) {
    // 백엔드에서 imageUrl(단일) 또는 imagesUrls(배열) 중 하나를 반환할 수 있음
    List<String> imagesUrls = [];
    if (json['imagesUrls'] != null) {
      // 상세 조회 시 (배열)
      imagesUrls = List<String>.from(json['imagesUrls']);
    } else if (json['imageUrl'] != null && json['imageUrl'].toString().isNotEmpty) {
      // 목록 조회 시 (단일)
      imagesUrls = [json['imageUrl'] as String];
    }
    
    return FreePost(
      id: json['id'] as int,
      title: json['title'] as String,
      content: json['content'] as String,
      viewCount: json['viewCount'] as int,
      email: json['email'] as String,
      nickname: json['nickname'] as String,
      createdAt: json['createdAt'] as String,
      imagesUrls: imagesUrls,
      likeCount: json['likeCount'] as int? ?? 0,
      isLiked: json['isLiked'] as bool? ?? false,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'content': content,
      'viewCount': viewCount,
      'email': email,
      'nickname': nickname,
      'createdAt': createdAt,
      'imagesUrls': imagesUrls,
      'likeCount': likeCount,
      'isLiked': isLiked,
    };
  }

  // 좋아요 상태를 업데이트하는 메서드
  FreePost copyWith({
    int? id,
    String? title,
    String? content,
    int? viewCount,
    String? email,
    String? nickname,
    String? createdAt,
    List<String>? imagesUrls,
    int? likeCount,
    bool? isLiked,
  }) {
    return FreePost(
      id: id ?? this.id,
      title: title ?? this.title,
      content: content ?? this.content,
      viewCount: viewCount ?? this.viewCount,
      email: email ?? this.email,
      nickname: nickname ?? this.nickname,
      createdAt: createdAt ?? this.createdAt,
      imagesUrls: imagesUrls ?? this.imagesUrls,
      likeCount: likeCount ?? this.likeCount,
      isLiked: isLiked ?? this.isLiked,
    );
  }
} 