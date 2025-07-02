import React, { useState } from 'react';
import './Adoption.css';

const Adoption = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    address: '',
    experience: '',
    reason: '',
    agreement: false
  });

  const [isSubmitted, setIsSubmitted] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setIsSubmitted(true);
  };

  if (isSubmitted) {
    return (
      <div className="adoption">
        <div className="adoption-complete">
          <h2>입양 신청 완료</h2>
          <div className="complete-icon">✅</div>
          <p>입양 신청이 성공적으로 제출되었습니다.</p>
          <p>보호소에서 검토 후 연락드리겠습니다.</p>
          <button 
            className="back-to-home"
            onClick={() => setIsSubmitted(false)}
          >
            다시 신청하기
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="adoption">
      <h2>입양 신청</h2>
      
      <div className="adoption-info">
        <h3>입양 신청 안내</h3>
        <ul>
          <li>입양 신청 후 보호소에서 검토하여 연락드립니다.</li>
          <li>입양 조건에 맞지 않는 경우 신청이 거절될 수 있습니다.</li>
          <li>입양 후에도 정기적인 소식을 전해주시기 바랍니다.</li>
        </ul>
      </div>

      <form className="adoption-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="name">이름 *</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="email">이메일 *</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="phone">전화번호 *</label>
          <input
            type="tel"
            id="phone"
            name="phone"
            value={formData.phone}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="address">주소 *</label>
          <input
            type="text"
            id="address"
            name="address"
            value={formData.address}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="experience">반려동물 키워본 경험</label>
          <textarea
            id="experience"
            name="experience"
            value={formData.experience}
            onChange={handleChange}
            rows="4"
            placeholder="반려동물을 키워본 경험이 있다면 적어주세요."
          />
        </div>

        <div className="form-group">
          <label htmlFor="reason">입양을 원하는 이유 *</label>
          <textarea
            id="reason"
            name="reason"
            value={formData.reason}
            onChange={handleChange}
            rows="4"
            placeholder="입양을 원하는 이유를 자세히 적어주세요."
            required
          />
        </div>

        <div className="form-group checkbox">
          <label>
            <input
              type="checkbox"
              name="agreement"
              checked={formData.agreement}
              onChange={handleChange}
              required
            />
            입양 신청에 동의합니다. *
          </label>
        </div>

        <button type="submit" className="submit-button">
          입양 신청하기
        </button>
      </form>
    </div>
  );
};

export default Adoption; 