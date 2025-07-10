import React, { useRef, useEffect, useState } from 'react';

const FadeIn = ({ children, duration = 900, delay = 0, style = {}, className = '', direction = 'up' }) => {
  const ref = useRef();
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const node = ref.current;
    if (!node) return;
    const observer = new window.IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setVisible(true);
          observer.disconnect();
        }
      },
      { threshold: 0.15 }
    );
    observer.observe(node);
    return () => observer.disconnect();
  }, []);

  let transformHidden = 'translateY(40px)';
  if (direction === 'left') transformHidden = 'translateX(-40px)';

  return (
    <div
      ref={ref}
      className={className}
      style={{
        opacity: visible ? 1 : 0,
        transform: visible ? 'none' : transformHidden,
        transition: `opacity ${duration}ms cubic-bezier(0.4,0,0.2,1) ${delay}ms, transform ${duration}ms cubic-bezier(0.4,0,0.2,1) ${delay}ms`,
        willChange: 'opacity, transform',
        ...style,
      }}
    >
      {children}
    </div>
  );
};

export default FadeIn; 