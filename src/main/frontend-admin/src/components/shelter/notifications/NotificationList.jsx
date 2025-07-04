import React, { useState, useEffect } from 'react';
import api from '../../../api/shelter/api';
import {
  getNotifications,
  markNotificationAsRead,
  deleteNotification
} from '../../../api/shelter/notification';
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

  // 알림 목록 불러오기 (진입 시 자동 전체 읽음 X)
  const fetchNotifications = async () => {
    // 더미 데이터 (테스트용)
    const dummy = [
      { id: 1, title: '입양 문의가 도착했습니다', content: '새 입양 문의가 등록되었습니다.', isRead: false, createdAt: new Date().toISOString() },
      { id: 2, title: '후원금 입금 안내', content: '후원금 10,000원이 입금되었습니다.', isRead: true, createdAt: new Date(Date.now() - 3600*1000).toISOString() },
      { id: 3, title: '예약 취소 알림', content: '산책 예약이 취소되었습니다.', isRead: false, createdAt: new Date(Date.now() - 2*3600*1000).toISOString() },
      { id: 4, title: '공지사항', content: '시스템 점검 안내: 5월 10일 02:00~04:00', isRead: true, createdAt: new Date(Date.now() - 3*3600*1000).toISOString() },
      { id: 5, title: '입양 완료', content: '강아지 뽀삐가 입양되었습니다.', isRead: false, createdAt: new Date(Date.now() - 4*3600*1000).toISOString() },
      { id: 6, title: '후원자 메시지', content: '후원자님이 메시지를 남겼습니다.', isRead: true, createdAt: new Date(Date.now() - 5*3600*1000).toISOString() },
      { id: 7, title: '예약 확정', content: '산책 예약이 확정되었습니다.', isRead: false, createdAt: new Date(Date.now() - 6*3600*1000).toISOString() },
      { id: 8, title: '입양 문의 답변', content: '입양 문의에 답변이 등록되었습니다.', isRead: true, createdAt: new Date(Date.now() - 7*3600*1000).toISOString() },
      { id: 9, title: '시스템 알림', content: '비밀번호가 변경되었습니다.', isRead: false, createdAt: new Date(Date.now() - 8*3600*1000).toISOString() },
      { id: 10, title: '후원금 입금 안내', content: '후원금 5,000원이 입금되었습니다.', isRead: true, createdAt: new Date(Date.now() - 9*3600*1000).toISOString() },
      { id: 11, title: '예약 취소 알림', content: '산책 예약이 취소되었습니다.', isRead: false, createdAt: new Date(Date.now() - 10*3600*1000).toISOString() },
      { id: 12, title: '공지사항', content: '새로운 공지사항이 등록되었습니다.', isRead: true, createdAt: new Date(Date.now() - 11*3600*1000).toISOString() },
    ];
    setNotifications(dummy);
    if (setUnreadCount) {
      setUnreadCount(dummy.filter(n => !n.isRead).length);
    }
    setLoading(false);
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
      <div className="notification-list-title-row">
        <h2 className="notification-list-title">
          <MdNotificationsActive style={{marginRight:8, color:'#23b266', verticalAlign:'middle'}} />
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
 