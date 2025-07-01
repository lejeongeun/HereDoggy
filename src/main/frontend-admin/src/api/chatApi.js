import axios from 'axios';

export const sendChatMessage = async (message) => {
  const response = await axios.post(
    '/api/ai/chat',
    { message },
    { withCredentials: true }
  );
  return response.data.reply;
};
