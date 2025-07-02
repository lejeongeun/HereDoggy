import React, { useState, useEffect } from 'react';
import { sendChatMessage, getChatRemaining } from '../api/chatApi';
import './ChatPage.css';
import ReactMarkdown from 'react-markdown';

const ChatPage = () => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [remaining, setRemaining] = useState(20); // 기본값

  useEffect(() => {
    fetchRemaining();
  }, []);

  const fetchRemaining = async () => {
    try {
      const count = await getChatRemaining();
      setRemaining(count);
    } catch (err) {
      console.error('질문 제한 정보 가져오기 실패:', err);
    }
  };

  const handleSend = async () => {
    if (!input.trim()) return;

    const userMsg = { role: 'user', content: input };
    setMessages((prev) => [...prev, userMsg]);

    const reply = await sendChatMessage(input);
    const modelMsg = { role: 'model', content: reply };

    setMessages((prev) => [...prev, modelMsg]);
    setInput('');
    fetchRemaining(); // 질문 후 남은 수 갱신
  };

  return (
    <div className='chat-container'>
      <div className='chat-header'>
        <img src='/avatar.png' alt='avatar' className='chat-avatar' />
        <div className='chat-info'>
          <div className='chat-name'>챗봇</div>
          <div className='chat-hearts'>❤️❤️❤️</div>
        </div>
        <div className='chat-persona'>따뜻한 말투</div>
      </div>

      <div className='chat-topic'>✨ 여기보개 챗봇입니다 ✨</div>

      <div className='chat-remaining'>남은 질문: {remaining} / 20</div>

      <div className='chat-messages'>
        {messages.map((msg, idx) => (
          <div
            key={idx}
            className={`chat-bubble ${
              msg.role === 'user' ? 'chat-user' : 'chat-model'
            }`}
          >
            <ReactMarkdown>{msg.content}</ReactMarkdown>
          </div>
        ))}
      </div>

      <div className='chat-input-area'>
        <input
          className='chat-input'
          placeholder='마음을 적어주세요...'
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && handleSend()}
        />
        <button className='chat-send-btn' onClick={handleSend}>
          ➤
        </button>
      </div>
    </div>
  );
};

export default ChatPage;
