import React from 'react';
import './Notification.css';

const Notification = () => {
  const notifications = [
    {
      id: 1,
      type: 'adoption',
      title: '입양 신청이 승인되었습니다',
      message: '멍멍이 입양 신청이 승인되었습니다. 보호소에 연락해주세요.',
      date: '2024.01.15',
      isRead: false
    },
    {
      id: 2,
      type: 'community',
      title: '새로운 댓글이 달렸습니다',
      message: '작성하신 게시글에 새로운 댓글이 달렸습니다.',
      date: '2024.01.14',
      isRead: true
    },
    {
      id: 3,
      type: 'system',
      title: '시스템 공지사항',
      message: '서비스 점검이 예정되어 있습니다.',
      date: '2024.01.13',
      isRead: true
    }
  ];

  return (
    <div className="notification">
      <h2>알림</h2>
      
      <div className="notification-list">
        {notifications.map((notification) => (
          <div 
            key={notification.id} 
            className={`notification-item ${!notification.isRead ? 'unread' : ''}`}
          >
            <div className="notification-icon">
              {notification.type === 'adoption' && '🏠'}
              {notification.type === 'community' && '💬'}
              {notification.type === 'system' && '🔔'}
            </div>
            <div className="notification-content">
              <h4>{notification.title}</h4>
              <p>{notification.message}</p>
              <span className="date">{notification.date}</span>
            </div>
            {!notification.isRead && <div className="unread-dot"></div>}
          </div>
        ))}
      </div>
    </div>
  );
};

export default Notification; 