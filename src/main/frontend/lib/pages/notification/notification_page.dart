import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import '../../models/notification_model.dart';
import '../../providers/notification_provider.dart';

class NotificationPage extends StatefulWidget {
  final bool show;
  final VoidCallback onClose;
  const NotificationPage({Key? key, required this.show, required this.onClose}) : super(key: key);

  @override
  State<NotificationPage> createState() => _NotificationPageState();
}

class _NotificationPageState extends State<NotificationPage> with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<double> _animation;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 250),
    );
    _animation = CurvedAnimation(parent: _controller, curve: Curves.easeInOut);
    if (widget.show) {
      _controller.forward();
      WidgetsBinding.instance.addPostFrameCallback((_) {
        Provider.of<NotificationProvider>(context, listen: false).fetchNotifications();
      });
    }
  }

  @override
  void didUpdateWidget(NotificationPage oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (widget.show && !_controller.isCompleted) {
      _controller.forward();
      Provider.of<NotificationProvider>(context, listen: false).fetchNotifications();
    } else if (!widget.show && _controller.isCompleted) {
      _controller.reverse();
    }
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  String _formatRelativeTime(String createdAt) {
    final time = DateTime.tryParse(createdAt)?.toLocal() ?? DateTime.now();
    final now = DateTime.now();
    final diff = now.difference(time);
    if (diff.inMinutes < 1) return '0h';
    if (diff.inHours < 1) return '${diff.inMinutes}m';
    if (diff.inHours < 24) return '${diff.inHours}h';
    return DateFormat('MM/dd').format(time);
  }

  @override
  Widget build(BuildContext context) {
    if (!widget.show) return const SizedBox.shrink();
    return Consumer<NotificationProvider>(
      builder: (context, provider, _) {
        final notifications = provider.notifications;
        final isLoading = provider.isLoading;
        final error = provider.error;
        return FadeTransition(
          opacity: _animation,
          child: Stack(
            children: [
              // 반투명 배경
              GestureDetector(
                onTap: widget.onClose,
                child: Container(
                  color: Colors.black.withOpacity(0.2),
                  width: double.infinity,
                  height: double.infinity,
                ),
              ),
              // 모달
              Center(
                child: Material(
                  color: Colors.transparent,
                  child: AnimatedContainer(
                    duration: const Duration(milliseconds: 250),
                    curve: Curves.easeInOut,
                    width: 340,
                    constraints: const BoxConstraints(maxHeight: 500),
                    padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 0),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(24),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withOpacity(0.08),
                          blurRadius: 24,
                          offset: const Offset(0, 8),
                        ),
                      ],
                    ),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        // 상단: 모두 읽음 버튼
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.end,
                            children: [
                              TextButton(
                                onPressed: notifications.isEmpty || isLoading ? null : () => provider.markAllAsRead(),
                                child: const Text('모두 읽음'),
                              ),
                            ],
                          ),
                        ),
                        // 알림 목록
                        if (isLoading)
                          const Padding(
                            padding: EdgeInsets.symmetric(vertical: 48),
                            child: CircularProgressIndicator(),
                          )
                        else if (error != null)
                          Padding(
                            padding: const EdgeInsets.symmetric(vertical: 48),
                            child: Text(error, style: const TextStyle(color: Colors.red, fontSize: 16)),
                          )
                        else if (notifications.isEmpty)
                          const Padding(
                            padding: EdgeInsets.symmetric(vertical: 48),
                            child: Text('알림이 없습니다', style: TextStyle(color: Colors.grey, fontSize: 16)),
                          )
                        else
                          Flexible(
                            child: ListView.separated(
                              shrinkWrap: true,
                              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                              itemCount: notifications.length,
                              separatorBuilder: (_, __) => const Divider(height: 1),
                              itemBuilder: (context, idx) {
                                final n = notifications[idx];
                                return Dismissible(
                                  key: ValueKey(n.id),
                                  direction: DismissDirection.endToStart,
                                  onDismissed: (_) => provider.markAsRead(n.id),
                                  background: Container(
                                    alignment: Alignment.centerRight,
                                    padding: const EdgeInsets.only(right: 24),
                                    color: Colors.blue[50],
                                    child: const Icon(Icons.done, color: Colors.blue),
                                  ),
                                  child: ListTile(
                                    contentPadding: const EdgeInsets.symmetric(vertical: 4, horizontal: 0),
                                    title: Text(
                                      n.title,
                                      style: TextStyle(
                                        fontWeight: n.isRead ? FontWeight.normal : FontWeight.bold,
                                        color: n.isRead ? Colors.black87 : Colors.black,
                                      ),
                                    ),
                                    subtitle: Text(n.content, style: const TextStyle(fontSize: 13, color: Colors.grey)),
                                    trailing: Row(
                                      mainAxisSize: MainAxisSize.min,
                                      children: [
                                        if (!n.isRead)
                                          Container(
                                            width: 8,
                                            height: 8,
                                            margin: const EdgeInsets.only(right: 8),
                                            decoration: const BoxDecoration(
                                              color: Colors.red,
                                              shape: BoxShape.circle,
                                            ),
                                          ),
                                        Text(_formatRelativeTime(n.createdAt), style: const TextStyle(fontSize: 13, color: Colors.grey)),
                                      ],
                                    ),
                                  ),
                                );
                              },
                            ),
                          ),
                        const SizedBox(height: 8),
                        // 모두 지우기 버튼
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                          child: SizedBox(
                            width: double.infinity,
                            child: ElevatedButton(
                              onPressed: notifications.isEmpty ? null : provider.hideAllNotifications,
                              style: ElevatedButton.styleFrom(
                                backgroundColor: Colors.grey[200],
                                foregroundColor: Colors.black,
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                                elevation: 0,
                              ),
                              child: const Text('모두 지우기'),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ],
          ),
        );
      },
    );
  }
} 