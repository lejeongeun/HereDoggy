import axios from 'axios';

export const sendChatMessage = async (message) => {
  const response = await axios.post('/api/ai/chat', { message });
  return response.data.reply;
};

export const getChatRemaining = async () => {
  const response = await axios.get('/api/ai/chat/limit');
  return response.data; // 숫자만 반환됨
};
