class FreePost {
  final int id;
  final String title;
  final String content;
  final int viewCount;
  final String email;
  final String nickname;
  final String createdAt;

  FreePost({
    required this.id,
    required this.title,
    required this.content,
    required this.viewCount,
    required this.email,
    required this.nickname,
    required this.createdAt,
  });

  factory FreePost.fromJson(Map<String, dynamic> json) {
    return FreePost(
      id: json['id'] as int,
      title: json['title'] as String,
      content: json['content'] as String,
      viewCount: json['viewCount'] as int,
      email: json['email'] as String,
      nickname: json['nickname'] as String,
      createdAt: json['createdAt'] as String,
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
    };
  }
} 