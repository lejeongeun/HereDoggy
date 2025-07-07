import React, { useState } from "react";
import { LineChart, Line, ResponsiveContainer } from 'recharts';

function DashCard({ label, value, tooltipContent, diff, icon, trendData }) {
  const [showTooltip, setShowTooltip] = useState(false);

  return (
    <div
      className="dash-card"
      onMouseEnter={() => setShowTooltip(true)}
      onMouseLeave={() => setShowTooltip(false)}
    >
      <div className="dash-card-top">
        <span className="dash-card-label">{label}</span>
        <span className="dash-card-icon">{icon}</span>
      </div>
      <div className="dash-card-value">{value}</div>
      {diff && (
        <div className={`dash-card-diff ${diff.startsWith('+') ? 'up' : 'down'}`}>{diff}</div>
      )}

      {trendData && (
        <div className="dash-card-sparkline-container">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={trendData} margin={{ top: 5, right: 0, left: 0, bottom: 5 }}>
              <Line type="monotone" dataKey="count" stroke="#6495ED" strokeWidth={2} dot={false} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      )}

      {showTooltip && tooltipContent && (
        <div className="dash-card-tooltip">
          {tooltipContent}
        </div>
      )}
    </div>
  );
}

export default DashCard;
