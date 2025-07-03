import 'package:flutter/material.dart';
import '../../models/chat_message.dart';
import '../../services/chat_service.dart';
import '../../services/auth_service.dart';
import '../../utils/constants.dart';

class ChatPage extends StatefulWidget {
  const ChatPage({Key? key}) : super(key: key);

  @override
  State<ChatPage> createState() => _ChatPageState();
}

class _ChatPageState extends State<ChatPage> {
  final TextEditingController _messageController = TextEditingController();
  final ScrollController _scrollController = ScrollController();
  final ChatService _chatService = ChatService();
  final AuthService _authService = AuthService();
  
  List<ChatMessage> _messages = [];
  bool _isLoading = false;
  int? _remainingCount;

  @override
  void initState() {
    super.initState();
    _loadRemainingCount();
    _addWelcomeMessage();
  }

  void _addWelcomeMessage() {
    _messages.add(ChatMessage.bot(
      '안녕하세요! 여기보개 챗봇 보리예요 🐾\n'
      '반려동물에 궁금한 점이 있으면 언제든 물어보세요!'
    ));
  }

  Future<void> _loadRemainingCount() async {
    final count = await _chatService.getRemainingCount();
    setState(() {
      _remainingCount = count;
    });
  }

  Future<void> _sendMessage() async {
    final message = _messageController.text.trim();
    if (message.isEmpty || _isLoading) return;

    // 사용자 메시지 추가
    final userMessage = ChatMessage.user(message);
    setState(() {
      _messages.add(userMessage);
      _isLoading = true;
    });
    _messageController.clear();
    _scrollToBottom();

    // 로딩 메시지 추가
    final loadingMessage = ChatMessage.loading();
    setState(() {
      _messages.add(loadingMessage);
    });
    _scrollToBottom();

    try {
      final result = await _chatService.sendMessage(message);
      
      // 로딩 메시지 제거
      setState(() {
        _messages.removeLast();
        _isLoading = false;
      });

      if (result['success']) {
        // 봇 응답 추가
        final botMessage = ChatMessage.bot(result['reply']);
        setState(() {
          _messages.add(botMessage);
        });
      } else {
        // 에러 메시지 추가
        final errorMessage = ChatMessage.bot(result['message']);
        setState(() {
          _messages.add(errorMessage);
        });
      }
      
      _scrollToBottom();
      _loadRemainingCount(); // 남은 횟수 업데이트
    } catch (e) {
      setState(() {
        _messages.removeLast();
        _isLoading = false;
        _messages.add(ChatMessage.bot('오류가 발생했습니다. 다시 시도해주세요.'));
      });
      _scrollToBottom();
    }
  }

  void _scrollToBottom() {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (_scrollController.hasClients) {
        _scrollController.animateTo(
          _scrollController.position.maxScrollExtent,
          duration: const Duration(milliseconds: 300),
          curve: Curves.easeOut,
        );
      }
    });
  }

  @override
  void dispose() {
    _messageController.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF5F5F5),
      appBar: AppBar(
        title: const Text('보리와 대화하기 🐾'),
        backgroundColor: const Color(0xFF4CAF50),
        foregroundColor: Colors.white,
        elevation: 0,
        actions: [
          if (_remainingCount != null)
            Container(
              margin: const EdgeInsets.only(right: 16),
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
              decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.2),
                borderRadius: BorderRadius.circular(12),
              ),
              child: Text(
                '남은 질문: $_remainingCount',
                style: const TextStyle(
                  fontSize: 12,
                  color: Colors.white,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
        ],
      ),
      body: Column(
        children: [
          Expanded(
            child: ListView.builder(
              controller: _scrollController,
              padding: const EdgeInsets.all(16),
              itemCount: _messages.length,
              itemBuilder: (context, index) {
                final message = _messages[index];
                return _ChatMessageWidget(message: message);
              },
            ),
          ),
          _buildInputArea(),
        ],
      ),
    );
  }

  Widget _buildInputArea() {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            offset: const Offset(0, -2),
            blurRadius: 8,
          ),
        ],
      ),
      child: Row(
        children: [
          Expanded(
            child: Container(
              decoration: BoxDecoration(
                color: const Color(0xFFF0F0F0),
                borderRadius: BorderRadius.circular(24),
              ),
              child: TextField(
                controller: _messageController,
                decoration: const InputDecoration(
                  hintText: '메시지를 입력하세요...',
                  border: InputBorder.none,
                  contentPadding: EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                ),
                maxLines: null,
                textInputAction: TextInputAction.send,
                onSubmitted: (_) => _sendMessage(),
              ),
            ),
          ),
          const SizedBox(width: 8),
          GestureDetector(
            onTap: _sendMessage,
            child: Container(
              width: 48,
              height: 48,
              decoration: BoxDecoration(
                color: _isLoading || _messageController.text.trim().isEmpty
                    ? Colors.grey
                    : const Color(0xFF4CAF50),
                borderRadius: BorderRadius.circular(24),
              ),
              child: const Icon(
                Icons.send,
                color: Colors.white,
                size: 20,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _ChatMessageWidget extends StatelessWidget {
  final ChatMessage message;

  const _ChatMessageWidget({required this.message});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      child: Row(
        mainAxisAlignment: message.isUser ? MainAxisAlignment.end : MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          if (!message.isUser) ...[
            CircleAvatar(
              radius: 16,
              backgroundColor: const Color(0xFF4CAF50),
              child: const Text(
                '🐾',
                style: TextStyle(fontSize: 16),
              ),
            ),
            const SizedBox(width: 8),
          ],
          Flexible(
            child: Container(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
              decoration: BoxDecoration(
                color: message.isUser ? const Color(0xFF4CAF50) : Colors.white,
                borderRadius: BorderRadius.circular(18).copyWith(
                  bottomLeft: message.isUser ? const Radius.circular(18) : const Radius.circular(4),
                  bottomRight: message.isUser ? const Radius.circular(4) : const Radius.circular(18),
                ),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(0.1),
                    offset: const Offset(0, 1),
                    blurRadius: 2,
                  ),
                ],
              ),
              child: message.isLoading
                  ? const Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        SizedBox(
                          width: 16,
                          height: 16,
                          child: CircularProgressIndicator(
                            strokeWidth: 2,
                            valueColor: AlwaysStoppedAnimation<Color>(Color(0xFF4CAF50)),
                          ),
                        ),
                        SizedBox(width: 8),
                        Text(
                          '보리가 생각중이에요...',
                          style: TextStyle(
                            color: Colors.grey,
                            fontSize: 14,
                          ),
                        ),
                      ],
                    )
                  : Text(
                      message.content,
                      style: TextStyle(
                        color: message.isUser ? Colors.white : Colors.black87,
                        fontSize: 14,
                      ),
                    ),
            ),
          ),
          if (message.isUser) ...[
            const SizedBox(width: 8),
            CircleAvatar(
              radius: 16,
              backgroundColor: Colors.grey[300],
              child: const Icon(
                Icons.person,
                size: 20,
                color: Colors.grey,
              ),
            ),
          ],
        ],
      ),
    );
  }
} 