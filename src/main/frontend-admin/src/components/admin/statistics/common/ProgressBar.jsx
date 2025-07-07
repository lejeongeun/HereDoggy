import React from 'react';

function ProgressBar({ percent, color }) {
  return (
    <div style={{ background: '#eee', borderRadius: 8, height: 12, width: '100%', margin: '6px 0' }}>
      <div style={{ width: `${percent}%`, background: color, height: '100%', borderRadius: 8, transition: 'width 0.4s' }} />
    </div>
  );
}

export default ProgressBar;
