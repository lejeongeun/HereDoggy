import React from 'react';
import { FaClipboardList, FaUserFriends, FaComment } from "react-icons/fa";
import StatCard from './common/StatCard';

function ReviewStatistics() {
  // 후기/커뮤니티 관련 mock 데이터
  const reviewStats = {
    total: 210,
    avgLikes: 4.2,
    avgComments: 2.1,
    topPosts: [
      { title: '입양 후기 - 행복이', likes: 23, comments: 12 },
      { title: '산책 자원봉사 후기', likes: 19, comments: 8 },
      { title: '보호소 방문기', likes: 17, comments: 7 },
      { title: '입양 꿀팁 공유', likes: 15, comments: 6 },
      { title: '실종견 찾았어요', likes: 14, comments: 5 },
    ],
  };
  return (
    <section className="review-section walk-section">
      <div className="summary-cards">
        <StatCard icon={<FaClipboardList />} label="전체 후기" value={reviewStats.total + '건'} color="#28A745" />
        <StatCard icon={<FaUserFriends />} label="평균 좋아요" value={reviewStats.avgLikes} color="#FFC107" />
        <StatCard icon={<FaComment />} label="평균 댓글" value={reviewStats.avgComments} color="#6495ED" />
      </div>
      <div style={{ fontWeight: 600, margin: '18px 0 8px 0' }}>Top 5 인기 게시글</div>
      <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap' }}>
        {reviewStats.topPosts.map((p, i) => (
          <div key={p.title} style={{ background: '#f9f9f9', borderRadius: 10, boxShadow: '0 1px 4px rgba(44,62,80,0.04)', padding: '12px 18px', minWidth: 160, flex: 1, display: 'flex', alignItems: 'center', gap: 10 }}>
            <span style={{ fontSize: 18, fontWeight: 700, color: '#6495ED', marginRight: 8 }}>#{i+1}</span>
            <div style={{ flex: 1 }}>
              <div style={{ fontWeight: 600, fontSize: 15, color: '#222', marginBottom: 2 }}>{p.title}</div>
              <div style={{ fontSize: 13, color: '#888' }}>👍 {p.likes} · 💬 {p.comments}</div>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}

export default ReviewStatistics;
