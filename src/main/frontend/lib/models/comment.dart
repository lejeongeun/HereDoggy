class Comment {
  final int id;
  final String content;
  final String email;
  final String nickname;
  final String createdAt;
  final String postType;
  final int postId;

  Comment({
    required this.id,
    required this.content,
    required this.email,
    required this.nickname,
    required this.createdAt,
    required this.postType,
    required this.postId,
  });

  factory Comment.fromJson(Map<String, dynamic> json) {
    return Comment(
      id: json['id'] as int,
      content: json['content'] as String,
      email: json['email'] as String,
      nickname: json['nickname'] as String,
      createdAt: json['createdAt'] as String,
      postType: json['postType'] as String,
      postId: json['postId'] as int,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'content': content,
      'email': email,
      'nickname': nickname,
      'createdAt': createdAt,
      'postType': postType,
      'postId': postId,
    };
  }
} 