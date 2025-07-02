import React, { useState } from 'react';
import './Shelter.css';

const Shelter = () => {
  const [selectedShelter, setSelectedShelter] = useState(null);

  const shelters = [
    {
      id: 1,
      name: '행복한 보호소',
      address: '서울시 강남구 테헤란로 123',
      phone: '02-1234-5678',
      description: '사랑과 관심으로 반려동물을 보호하는 보호소입니다.',
      image: '/shelter1.jpg',
      dogs: [
        {
          id: 1,
          name: '멍멍이',
          breed: '골든리트리버',
          age: 3,
          gender: '수컷',
          description: '활발하고 친근한 성격의 강아지입니다.',
          image: '/dog1.jpg'
        },
        {
          id: 2,
          name: '댕댕이',
          breed: '말티즈',
          age: 2,
          gender: '암컷',
          description: '작고 귀여운 말티즈입니다.',
          image: '/dog2.jpg'
        }
      ]
    },
    {
      id: 2,
      name: '사랑의 보호소',
      address: '서울시 서초구 서초대로 456',
      phone: '02-2345-6789',
      description: '반려동물의 새로운 가족을 찾아드립니다.',
      image: '/shelter2.jpg',
      dogs: [
        {
          id: 3,
          name: '바둑이',
          breed: '믹스견',
          age: 1,
          gender: '수컷',
          description: '검은색 털의 귀여운 믹스견입니다.',
          image: '/dog3.jpg'
        }
      ]
    }
  ];

  return (
    <div className="shelter">
      <h2>보호소</h2>
      
      {!selectedShelter ? (
        <div className="shelter-list">
          {shelters.map((shelter) => (
            <div 
              key={shelter.id} 
              className="shelter-item"
              onClick={() => setSelectedShelter(shelter)}
            >
              <div className="shelter-image">
                <img src={shelter.image} alt={shelter.name} />
              </div>
              <div className="shelter-info">
                <h3>{shelter.name}</h3>
                <p className="address">{shelter.address}</p>
                <p className="phone">{shelter.phone}</p>
                <p className="description">{shelter.description}</p>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="shelter-detail">
          <button 
            className="back-button"
            onClick={() => setSelectedShelter(null)}
          >
            ← 목록으로 돌아가기
          </button>
          
          <div className="shelter-header">
            <div className="shelter-image">
              <img src={selectedShelter.image} alt={selectedShelter.name} />
            </div>
            <div className="shelter-info">
              <h3>{selectedShelter.name}</h3>
              <p className="address">{selectedShelter.address}</p>
              <p className="phone">{selectedShelter.phone}</p>
              <p className="description">{selectedShelter.description}</p>
            </div>
          </div>

          <div className="dogs-section">
            <h3>입양 가능한 강아지들</h3>
            <div className="dogs-grid">
              {selectedShelter.dogs.map((dog) => (
                <div key={dog.id} className="dog-card">
                  <div className="dog-image">
                    <img src={dog.image} alt={dog.name} />
                  </div>
                  <div className="dog-info">
                    <h4>{dog.name}</h4>
                    <p className="breed">{dog.breed}</p>
                    <p className="details">{dog.age}살, {dog.gender}</p>
                    <p className="description">{dog.description}</p>
                    <button className="adopt-button">입양 신청</button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Shelter; 