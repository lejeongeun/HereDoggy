import React, { useState, useEffect } from 'react';
import { getNotifications, markNotificationAsRead, deleteNotification } from '../../../api/shelter/notification';

const NotificationList = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 알림 목록을 가져오는 함수
  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const data = await getNotifications();
        setNotifications(data);
        setLoading(false);
      } catch (err) {
        setError('알림을 가져오는 데 실패했습니다.');
        setLoading(false);
      }
    };

    fetchNotifications();
  }, []);  // 컴포넌트가 마운트될 때 한 번만 호출

  // 개별 알림 읽음 처리
  const handleMarkAsRead = async (id) => {
    try {
      await markNotificationAsRead(id);
      setNotifications(prevNotifications =>
        prevNotifications.map(notification =>
          notification.id === id ? { ...notification, isRead: true } : notification
        )
      );
    } catch (error) {
      setError('알림 읽음 처리에 실패했습니다.');
    }
  };

  // 개별 알림 삭제 처리
  const handleDeleteNotification = async (id) => {
    try {
      await deleteNotification(id);
      setNotifications(prevNotifications => 
        prevNotifications.filter(notification => notification.id !== id)
      );
    } catch (error) {
      setError('알림 삭제에 실패했습니다.');
    }
  };

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div>
      <h2>알림 목록</h2>
      <ul>
        {notifications.map((notification) => (
          <li key={notification.id} className={notification.isRead ? 'read' : 'unread'}>
            <h3>{notification.title}</h3>
            <p>{notification.content}</p>
            <div>
              {!notification.isRead && (
                <button onClick={() => handleMarkAsRead(notification.id)}>읽음 처리</button>
              )}
              <button onClick={() => handleDeleteNotification(notification.id)}>삭제</button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default NotificationList;
