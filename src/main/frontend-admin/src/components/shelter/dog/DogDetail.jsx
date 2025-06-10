import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getDogDetail, deleteDog } from "../../../api/shelter/dog";
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css';
import 'swiper/css/navigation';
import { Navigation } from 'swiper/modules';
import "../../../styles/shelter/pages/dogDetail.css";

const BACKEND_URL = "http://localhost:8080";

function DogDetail({ initialImageId }) {
  const { id } = useParams();
  const sheltersId = localStorage.getItem("shelters_id");
  const [dog, setDog] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [activeImageIndex, setActiveImageIndex] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchDog = async () => {
      setLoading(true);
      try {
        const response = await getDogDetail(sheltersId, id);
        console.log("Dog data:", response.data);
        setDog(response.data);
        
        if (initialImageId && response.data.images) {
          const index = response.data.images.findIndex(img => img.id === initialImageId);
          if (index !== -1) {
            setActiveImageIndex(index);
          }
        }
      } catch (err) {
        console.error("Error fetching dog:", err);
        setError("유기견 정보를 불러올 수 없습니다.");
      } finally {
        setLoading(false);
      }
    };
    fetchDog();
  }, [id, sheltersId, initialImageId]);

  // 수정 버튼 클릭 핸들러
  const handleEdit = () => {
    navigate(`/shelter/dogedit/${id}`);
  };

  // 삭제 버튼 클릭 핸들러
  const handleDelete = async () => {
    if (window.confirm("정말로 삭제하시겠습니까?")) {
      try {
        await deleteDog(sheltersId, id);
        alert("삭제가 완료되었습니다.");
        navigate("/shelter/doglist");
      } catch (err) {
        alert("삭제 중 오류가 발생했습니다.");
      }
    }
  };

  if (loading) return <div className="dogdetail-loading">로딩 중...</div>;
  if (error) return <div className="dogdetail-error">{error}</div>;
  if (!dog) return null;

  return (
    <div className="dogdetail-container" style={{ 
      position: 'relative', 
      zIndex: 1,
      maxWidth: '1200px',
      margin: '42px auto 60px auto',
      background: '#fff',
      borderRadius: '16px',
      boxShadow: '0 6px 24px #e5ebf4cc',
      padding: '48px 50px 36px 50px',
      border: '2px solid #ecf1f5',
      fontFamily: "'Pretendard', 'Noto Sans KR', sans-serif"
    }}>
      <button className="dogdetail-back-btn" onClick={() => navigate("/shelter/doglist")}>
        목록으로
      </button>
      <div className="dogdetail-images-slider" style={{ 
        position: 'relative', 
        zIndex: 1,
        marginBottom: '32px',
        borderRadius: '18px',
        overflow: 'hidden'
      }}>
  <Swiper
    modules={[Navigation]}
    navigation
    spaceBetween={16}
          slidesPerView={1}
          style={{ 
            maxWidth: 400, 
            borderRadius: 18, 
            position: 'relative', 
            zIndex: 1 
          }}
          initialSlide={activeImageIndex}
        >
          {dog.images && dog.images.length > 0 ? (
            dog.images.map((image) => (
              <SwiperSlide key={image.id}>
                <img
                  src={BACKEND_URL + image.imageUrl}
                  alt={dog.name}
                  className="dogdetail-img"
                  style={{ 
                    width: '100%', 
                    height: 300, 
                    objectFit: 'cover', 
                    borderRadius: 18 
                  }}
                />
              </SwiperSlide>
            ))
          ) : dog.imagesUrls && dog.imagesUrls.length > 0 ? (
      dog.imagesUrls.map((url, idx) => (
        <SwiperSlide key={idx}>
          <img
            src={BACKEND_URL + url}
            alt={dog.name}
            className="dogdetail-img"
                  style={{ 
                    width: '100%', 
                    height: 300, 
                    objectFit: 'cover', 
                    borderRadius: 18 
                  }}
          />
        </SwiperSlide>
      ))
    ) : (
      <SwiperSlide>
              <div className="dogdetail-img dogdetail-img-placeholder" style={{ 
                width: '100%', 
                height: 240, 
                borderRadius: 18 
              }}/>
      </SwiperSlide>
    )}
  </Swiper>
</div>
      <div className="dogdetail-info-table" style={{
        background: '#fff',
        borderRadius: '12px',
        border: '2px solid #e9eee6',
        padding: '24px',
        marginBottom: '24px'
      }}>
        <div className="dogdetail-info-row dogdetail-name-row">
          <span>이름</span>
          <span>{dog.name}</span>
        </div>
        <div className="dogdetail-info-cols-wrap">
          <div className="dogdetail-info-col">
            <div className="dogdetail-info-row">
              <span>성별</span>
              <span>{dog.gender === "MALE" ? "수컷" : dog.gender === "FEMALE" ? "암컷" : dog.gender}</span>
            </div>
            <div className="dogdetail-info-row">
              <span>나이</span>
              <span>{dog.age}살</span>
            </div>
            <div className="dogdetail-info-row">
              <span>몸무게</span>
              <span>{dog.weight ? `${dog.weight}kg` : "-"}</span>
            </div>
            <div className="dogdetail-info-row">
              <span>중성화</span>
              <span>{dog.isNeutered ? "O" : "X"}</span>
            </div>
          </div>
          <div className="dogdetail-info-col">
            <div className="dogdetail-info-row">
              <span>상태</span>
              <span>{dog.status === "AVAILABLE" ? "예약가능" : dog.status === "RESERVED" ? "예약완료" : dog.status === "ADOPTED" ? "입양완료" : dog.status}</span>
            </div>
            <div className="dogdetail-info-row">
              <span>발견장소</span>
              <span>{dog.foundLocation || "-"}</span>
            </div>
            <div className="dogdetail-info-row">
              <span>성격/특징</span>
              <span>{dog.personality || "-"}</span>
            </div>
          </div>
        </div>
      </div>
      <div className="dogdetail-btn-row">
        <button className="dogdetail-btn edit" onClick={handleEdit}>
          수정
        </button>
        <button className="dogdetail-btn delete" onClick={handleDelete}>
          삭제
        </button>
      </div>
    </div>
  );
}

export default DogDetail;
