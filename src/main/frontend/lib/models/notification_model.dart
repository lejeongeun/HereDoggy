class NotificationModel {
  final int id;
  final String title;
  final String content;
  final String type; // NotificationType (enum 문자열)
  final String referenceType; // ReferenceType (enum 문자열)
  final int referenceId;
  final bool isRead;
  final String createdAt;

  NotificationModel({
    required this.id,
    required this.title,
    required this.content,
    required this.type,
    required this.referenceType,
    required this.referenceId,
    required this.isRead,
    required this.createdAt,
  });

  factory NotificationModel.fromJson(Map<String, dynamic> json) {
    return NotificationModel(
      id: json['id'] as int,
      title: json['title'] as String,
      content: json['content'] as String,
      type: json['type'] as String,
      referenceType: json['referenceType'] as String,
      referenceId: json['referenceId'] as int,
      isRead: json['isRead'] as bool,
      createdAt: json['createdAt'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'content': content,
      'type': type,
      'referenceType': referenceType,
      'referenceId': referenceId,
      'isRead': isRead,
      'createdAt': createdAt,
    };
  }
} 