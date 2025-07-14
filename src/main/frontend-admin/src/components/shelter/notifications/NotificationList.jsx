import React, { useState, useEffect } from 'react';
import {
  getNotifications,
  markNotificationAsRead,
  deleteNotification
} from '../../../api/shelter/notification';
import api from '../../../api/shelter/api';
import '../../../styles/shelter/notification/notificationList.css';
import { MdNotificationsActive, MdNotificationsNone } from 'react-icons/md';

// 전체 읽음처리 API
const markAllAsRead = async () => {
  await api.patch('/api/notifications/read-all');
};

const NotificationList = ({ setUnreadCount }) => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 알림 목록 불러오기
  const fetchNotifications = async () => {
    try {
      const res = await getNotifications();
      const notiList = Array.isArray(res) ? res : [];

      setNotifications(notiList);
      if (setUnreadCount) {
        setUnreadCount(notiList.filter(n => !n.isRead).length);
      }
    } catch (err) {
      console.error("알림 목록 조회 실패:", err);
      setError("알림을 불러오지 못했습니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchNotifications(); // 최초 1회 호출

    const interval = setInterval(async () => {
      try {
        const res = await getNotifications();
        const notiList = Array.isArray(res) ? res : [];

        setNotifications(prev => {
          if (JSON.stringify(prev) !== JSON.stringify(notiList)) {
            if (setUnreadCount) {
              setUnreadCount(notiList.filter(n => !n.isRead).length);
            }
            return notiList;
          }
          return prev;
        });
      } catch (err) {
        console.error("주기적 알림 조회 실패:", err);
      }
    }, 5000); // 5초마다 갱신

    return () => clearInterval(interval);
  }, []);

  const handleMarkAsRead = async (id) => {
    try {
      await markNotificationAsRead(id);
      setNotifications(prev =>
        prev.map(n =>
          n.id === id ? { ...n, isRead: true } : n
        )
      );
      if (setUnreadCount) setUnreadCount(prev => Math.max(prev - 1, 0));
    } catch {
      setError("알림 읽음 처리에 실패했습니다.");
    }
  };

  const handleDeleteNotification = async (id) => {
    try {
      await deleteNotification(id);
      setNotifications(prev => prev.filter(n => n.id !== id));
    } catch {
      setError("알림 삭제에 실패했습니다.");
    }
  };

  const onClickAllRead = async () => {
    try {
      await markAllAsRead();
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
      if (setUnreadCount) setUnreadCount(0);
    } catch {
      setError("전체 읽음 처리에 실패했습니다.");
    }
  };

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="notification-list-wrapper">
      <div className="notification-list-title-row">
        <h2 className="notification-list-title">
          <MdNotificationsActive style={{ marginRight: 8, color: '#23b266', verticalAlign: 'middle' }} />
          알림 목록
        </h2>
        <button
          className="notification-list-btn notification-list-allread"
          onClick={onClickAllRead}
        >
          전체 읽음 처리
        </button>
      </div>
      <ul className="notification-list-ul">
        {notifications.map((notification) => (
          <li
            key={notification.id}
            className={`notification-list-li ${notification.isRead ? 'notification-read' : 'notification-unread'}`}
          >
            <div className="notification-main">
              <div className="notification-list-heading-row">
                {notification.isRead ? (
                  <MdNotificationsNone className="notification-list-icon" />
                ) : (
                  <MdNotificationsActive className="notification-list-icon notification-list-icon-unread" />
                )}
                <h3 className="notification-list-heading">{notification.title}</h3>
                {notification.createdAt && (
                  <span className="notification-list-date">{new Date(notification.createdAt).toLocaleString()}</span>
                )}
              </div>
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
