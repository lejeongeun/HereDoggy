import React from 'react';

function StatCard({ icon, label, value, color }) {
  const stringValue = String(value);
  const numericValue = parseFloat(stringValue.replace(/[^0-9.]/g, ''));
  const unit = stringValue.replace(/[0-9.,]/g, '');

  return (
    <div style={{
      display: 'flex', alignItems: 'center', background: '#f9f9f9', borderRadius: 12, padding: '18px 20px', boxShadow: '0 2px 8px rgba(44,62,80,0.06)', marginRight: 16, marginBottom: 12
    }}>
      <span style={{ fontSize: 28, color, marginRight: 14 }}>{icon}</span>
      <div>
        <div style={{ fontSize: 13, color: '#888', fontWeight: 600 }}>{label}</div>
        <div style={{ fontSize: 22, fontWeight: 800, color: '#222' }}>
          {value}
        </div>
      </div>
    </div>
  );
}

export default StatCard;
