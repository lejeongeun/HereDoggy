import React, { useState, useEffect } from 'react';
import api from '../../../api/shelter/api';
import {
  getNotifications,
  markNotificationAsRead,
  deleteNotification
} from '../../../api/shelter/notification';
import '../../../styles/shelter/notification/notificationList.css';

// 전체 읽음처리 API
const markAllAsRead = async () => {
  await api.patch('/api/notifications/read-all');
};

const NotificationList = ({ setUnreadCount }) => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 알림 목록 불러오기 (진입 시 자동 전체 읽음 X)
  const fetchNotifications = async () => {
    try {
      const data = await getNotifications();
      setNotifications(data);
      if (setUnreadCount) {
        setUnreadCount(data.filter(n => !n.isRead).length);
      }
    } catch (err) {
      setError('알림을 가져오는 데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  // 마운트 시 단순히 목록만 조회
  useEffect(() => {
    fetchNotifications();
  }, []);

  // 개별 읽음처리
  const handleMarkAsRead = async (id) => {
    try {
      await markNotificationAsRead(id);
      setNotifications(prev =>
        prev.map(notification =>
          notification.id === id ? { ...notification, isRead: true } : notification
        )
      );
      if (setUnreadCount) setUnreadCount(prev => Math.max(prev - 1, 0));
    } catch {
      setError('알림 읽음 처리에 실패했습니다.');
    }
  };

  // 삭제
  const handleDeleteNotification = async (id) => {
    try {
      await deleteNotification(id);
      setNotifications(prev =>
        prev.filter(notification => notification.id !== id)
      );
    } catch {
      setError('알림 삭제에 실패했습니다.');
    }
  };

  // "전체 읽음" 버튼 클릭 시 실행
  const onClickAllRead = async () => {
    try {
      await markAllAsRead();
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
      if (setUnreadCount) setUnreadCount(0);
    } catch {
      setError('전체 읽음 처리에 실패했습니다.');
    }
  };

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="notification-list-wrapper">
      <h2 className="notification-list-title">
        알림 목록
        <button
          className="notification-list-btn"
          style={{ float: 'right', fontSize: '0.9em' }}
          onClick={onClickAllRead}
        >
          전체 읽음 처리
        </button>
      </h2>
      <ul className="notification-list-ul">
        {notifications.map((notification) => (
          <li
            key={notification.id}
            className={`notification-list-li ${notification.isRead ? 'notification-read' : 'notification-unread'}`}
          >
            <div className="notification-main">
              <h3 className="notification-list-heading">{notification.title}</h3>
              <p className="notification-list-content">{notification.content}</p>
            </div>
            <div className="notification-list-btnbox">
              {!notification.isRead && (
                <button
                  className="notification-list-btn"
                  onClick={() => handleMarkAsRead(notification.id)}
                >
                  읽음 처리
                </button>
              )}
              <button
                className="notification-list-btn notification-list-delete"
                onClick={() => handleDeleteNotification(notification.id)}
              >
                삭제
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default NotificationList;
