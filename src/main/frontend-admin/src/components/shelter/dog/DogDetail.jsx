import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getDogDetail, deleteDog } from "../../../api/shelter/dog";
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css';
import 'swiper/css/navigation';
import { Navigation } from 'swiper/modules';
import "../../../styles/shelter/pages/dogDetail.css";
import { ImageOff, PawPrint, Calendar, Weight, Heart, MapPin, Smile } from 'lucide-react';

import { BACKEND_URL } from "../../../api/config";

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
    <div className="dogdetail-container">
      <button className="dogdetail-back-btn" onClick={() => navigate("/shelter/doglist")}>
        목록으로
      </button>

      <div className="dogdetail-content-row">
        <div className="dogdetail-images-slider">
          <Swiper
            modules={[Navigation]}
            navigation
            spaceBetween={16}
            slidesPerView={1}
            initialSlide={activeImageIndex}
            className="dogdetail-swiper"
          >
            {dog.images && dog.images.length > 0 ? (
              dog.images.map((image) => (
                <SwiperSlide key={image.id}>
                  <img
                    src={BACKEND_URL + image.imageUrl}
                    alt={dog.name}
                    className="dogdetail-img dogdetail-slide-img"
                  />
                </SwiperSlide>
              ))
            ) : dog.imagesUrls && dog.imagesUrls.length > 0 ? (
              dog.imagesUrls.map((url, idx) => (
                <SwiperSlide key={idx}>
                  <img
                    src={BACKEND_URL + url}
                    alt={dog.name}
                    className="dogdetail-img dogdetail-slide-img"
                  />
                </SwiperSlide>
              ))
            ) : (
              <SwiperSlide>
                <div className="dogdetail-img dogdetail-img-placeholder">
                  <ImageOff size={48} color="#999" />
                </div>
              </SwiperSlide>
            )}
          </Swiper>
        </div>

        <div className="dogdetail-info-table">
          <div className="dogdetail-name-section">
            <span className="dogdetail-label">이름</span>
            <span className="dogdetail-value dogdetail-name">{dog.name}</span>
          </div>
          <div className="dogdetail-info-grid">
            <div className="dogdetail-info-item">
              <span className="dogdetail-label">성별</span>
              <span className="dogdetail-value">{dog.gender === "MALE" ? "수컷" : dog.gender === "FEMALE" ? "암컷" : dog.gender}</span>
            </div>
            <div className="dogdetail-info-item">
              <span className="dogdetail-label">나이</span>
              <span className="dogdetail-value">{dog.age}살</span>
            </div>
            <div className="dogdetail-info-item">
              <span className="dogdetail-label">몸무게</span>
              <span className="dogdetail-value">{dog.weight ? `${dog.weight}kg` : "-"}</span>
            </div>
            <div className="dogdetail-info-item">
              <span className="dogdetail-label">중성화</span>
              <span className="dogdetail-value">{dog.isNeutered ? "O" : "X"}</span>
            </div>
            <div className="dogdetail-info-item">
              <span className="dogdetail-label">상태</span>
              <span className="dogdetail-value">{dog.status === "AVAILABLE" ? "예약가능" : dog.status === "RESERVED" ? "예약완료" : dog.status === "ADOPTED" ? "입양완료" : dog.status}</span>
            </div>
            <div className="dogdetail-info-item">
              <span className="dogdetail-label">발견장소</span>
              <span className="dogdetail-value">{dog.foundLocation || "-"}</span>
            </div>
            <div className="dogdetail-info-item dogdetail-full-width">
              <span className="dogdetail-label">성격/특징</span>
              <span className="dogdetail-value">{dog.personality || "-"}</span>
            </div>
          </div>

          <div className="dogdetail-btn-row">
            <button className="dogdetail-btn edit" onClick={handleEdit}>수정</button>
            <button className="dogdetail-btn delete" onClick={handleDelete}>삭제</button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DogDetail;