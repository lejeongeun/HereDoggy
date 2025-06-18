import React, { useState, useEffect } from 'react';
// import { getNotifications, markNotificationAsRead, deleteNotification } from '../../../api/shelter/notification';
import '../../../styles/shelter/notification/notificationList.css';

// 더미 데이터 (관리자 시나리오)
const dummy = [
  {
    id: 1,
    title: '산책 예약 요청',
    content: '홍길동님이 6월 25일 14:00에 산책을 예약했습니다.',
    isRead: false,
  },
  {
    id: 2,
    title: '입양 신청 접수',
    content: '박보호님이 “해피” 입양을 신청했습니다.',
    isRead: false,
  },
  {
    id: 3,
    title: '후원 완료',
    content: '김서포터님이 50,000원을 후원했습니다.',
    isRead: true,
  },
  {
    id: 4,
    title: '1:1 문의 도착',
    content: '이진구님이 새로운 문의를 보냈습니다.',
    isRead: false,
  },
  {
    id: 5,
    title: '산책 예약 취소',
    content: '정도우님이 6월 23일 산책 예약을 취소했습니다.',
    isRead: true,
  },
];

const NotificationList = () => {
  // 실제 사용 시 빈 배열로 시작, 더미 테스트 시 바로 데이터 사용
  const [notifications, setNotifications] = useState(dummy);
  const [loading, setLoading] = useState(false); // 더미 테스트 시 false
  const [error, setError] = useState(null);

  // 실제 서버 연동 시 아래 주석 해제
  /*
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
  }, []);
  */

  // 더미데이터용 읽음 처리
  const handleMarkAsRead = (id) => {
    setNotifications(prevNotifications =>
      prevNotifications.map(notification =>
        notification.id === id ? { ...notification, isRead: true } : notification
      )
    );
  };

  // 더미데이터용 삭제
  const handleDeleteNotification = (id) => {
    setNotifications(prevNotifications =>
      prevNotifications.filter(notification => notification.id !== id)
    );
  };

  // 실제 서버 연동 시 아래 함수 사용
  /*
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
  */

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>{error}</div>;

  return (
<div className="notification-list-wrapper">
  <h2 className="notification-list-title">알림 목록</h2>
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
            <button className="notification-list-btn" onClick={() => handleMarkAsRead(notification.id)}>
              읽음 처리
            </button>
          )}
          <button className="notification-list-btn notification-list-delete" onClick={() => handleDeleteNotification(notification.id)}>
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
