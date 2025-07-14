import { useEffect, useState } from "react";
import { getDogs } from "../../api/shelter/dog";
import '../../styles/shelter/pages/dogList.css';
import { Link } from "react-router-dom";
import { ImageOff } from 'lucide-react';
import Pagination from "../../components/shelter/common/Pagination";


const BACKEND_URL = "http://localhost:8080";

function DogList() {
  const sheltersId = localStorage.getItem("shelters_id");
  const [dogs, setDogs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10); // 페이지당 10개 항목

  useEffect(() => {
    if (!sheltersId) {
      setError("보호소 정보가 없습니다.");
      return;
    }
    const fetchDogs = async () => {
      setLoading(true);
      setError(null); // 초기화
      try {
        const response = await getDogs(sheltersId);
        console.log("API 응답:", response.data);
        console.log("첫 번째 강아지 데이터 전체 구조:", JSON.stringify(response.data[0], null, 2)); // 전체 구조 확인

        // API가 배열을 바로 반환하면
        const dogsData = Array.isArray(response.data) ? response.data : response.data.dogs || [];
        console.log("처리된 강아지 데이터:", dogsData);
        console.log("총 개수:", dogsData.length);

        setDogs(dogsData);
      } catch (err) {
        console.error("유기견 목록 조회 실패:", err);
        setError("유기견을 불러오는 중 오류가 발생했습니다.");
      } finally {
        setLoading(false);
      }
    };
    fetchDogs();
  }, [sheltersId]);

  // 현재 페이지에 해당하는 유기견 계산
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentDogs = dogs.slice(indexOfFirstItem, indexOfLastItem);

  // 총 페이지 수 계산
  const totalPages = Math.ceil(dogs.length / itemsPerPage);

  // 페이지 변경 핸들러
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  if (loading) return <p>로딩 중...</p>;
  if (error) return (
    <div className="doglist-empty">
      <p>{error}</p>
      <Link to="/shelter/dogregister" className="doglist-register-btn big">
        유기견 등록하기
      </Link>
    </div>
  );
  if (dogs.length === 0) return (
    <div className="doglist-empty">
      <p>등록된 유기견이 없습니다.</p>
      <Link to="/shelter/dogregister" className="doglist-register-btn big">
        유기견 등록하기
      </Link>
    </div>
  );

  return (
    <div className="doglist-container">
      <div className="doglist-header">
        <Link to="/shelter/dogregister" className="doglist-register-btn">
          등록하기
        </Link>
      </div>
      <table className="doglist-table">
        <thead>
          <tr>
            <th>사진</th>
            <th>이름</th>
            <th>성별</th>
            <th>나이</th>
            <th>상태</th>
          </tr>
        </thead>
        <tbody>
          {currentDogs.map((dog, index) => (
            <tr key={`${dog.id}-${index}`}>
              <td>
                {dog.images && dog.images.length > 0 ? (
                  <img
                    src={BACKEND_URL + dog.images[0].imageUrl}
                    alt={dog.name}
                    className="doglist-img"
                    data-image-id={dog.images[0].id}
                  />
                ) : (
                  <div className="doglist-img doglist-img-placeholder">
                    <ImageOff size={24} color="#999" />
                  </div>
                )}
              </td>
              <td>
                <Link to={`/shelter/dog/${dog.id}`} className="doglist-link">
                  {dog.name}
                </Link>
              </td>
              <td>{dog.gender === "MALE" ? "수컷" : dog.gender === "FEMALE" ? "암컷" : dog.gender}</td>
              <td>{dog.age}살</td>
              <td>{dog.status === "AVAILABLE" ? "예약가능" : dog.status === "RESERVED" ? "예약완료" : dog.status === "ADOPTED" ? "입양완료" : dog.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
     <Pagination
        totalItems={dogs.length}
        itemPerPage={10}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
      />
    </div>
  );
}

export default DogList;
