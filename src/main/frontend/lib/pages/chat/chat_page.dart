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
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 24),
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Expanded(
                    child: Center(
                      child: GradientText(
                        '사용자님, 안녕하세요',
                        style: const TextStyle(
                          fontSize: 24,
                          fontWeight: FontWeight.bold,
                        ),
                        gradient: const LinearGradient(
                          colors: [
                            Color(0xFF4285F4), // 파랑
                            Color(0xFF9B59B6), // 보라
                            Color(0xFFE57373), // 분홍
                          ],
                        ),
                      ),
                    ),
                  ),
                  if (_remainingCount != null)
                    Container(
                      margin: const EdgeInsets.only(left: 8, top: 2),
                      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                      decoration: BoxDecoration(
                        color: const Color(0xFFE0F7FA), // 연한 민트
                        borderRadius: BorderRadius.circular(20),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withOpacity(0.10),
                            blurRadius: 8,
                            offset: const Offset(0, 2),
                          ),
                        ],
                      ),
                      child: Text(
                        '남은 질문: $_remainingCount',
                        style: const TextStyle(
                          fontSize: 14,
                          color: Color(0xFF333333), // 진한 회색
                          fontWeight: FontWeight.bold,
                          letterSpacing: 0.2,
                        ),
                      ),
                    ),
                ],
              ),
            ),
            const Divider(height: 1, thickness: 1, color: Color(0xFFF0F0F0)),
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
      ),
    );
  }

  Widget _buildInputArea() {
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 0, 16, 24),
      child: Container(
        decoration: BoxDecoration(
          color: const Color(0xFFF7F7F7),
          borderRadius: BorderRadius.circular(32),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.07),
              blurRadius: 12,
              offset: const Offset(0, 4),
            ),
          ],
        ),
        child: Row(
          children: [
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: TextField(
                  controller: _messageController,
                  decoration: const InputDecoration(
                    hintText: '보리에게 물어보세요',
                    border: InputBorder.none,
                  ),
                  maxLines: null,
                  textInputAction: TextInputAction.send,
                  onSubmitted: (_) => _sendMessage(),
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(right: 8),
              child: GestureDetector(
                onTap: _sendMessage,
                child: Container(
                  width: 44,
                  height: 44,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: _isLoading || _messageController.text.trim().isEmpty
                        ? Colors.grey[300]
                        : const Color(0xFF4285F4),
                  ),
                  child: const Icon(
                    Icons.send,
                    color: Colors.white,
                    size: 22,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class GradientText extends StatelessWidget {
  final String text;
  final TextStyle style;
  final Gradient gradient;

  const GradientText(this.text, {required this.style, required this.gradient, super.key});

  @override
  Widget build(BuildContext context) {
    return ShaderMask(
      shaderCallback: (bounds) => gradient.createShader(
        Rect.fromLTWH(0, 0, bounds.width, bounds.height),
      ),
      child: Text(
        text,
        style: style.copyWith(color: Colors.white),
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
              backgroundColor: const Color(0xFFE0F7FA),
              child: const Text(
                '🐶',
                style: TextStyle(fontSize: 18),
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