import { useEffect, useState } from "react";
import { getDogs } from "../../api/shelter/dog";
import '../../styles/shelter/pages/dogList.css';
import { Link } from "react-router-dom";

const BACKEND_URL = "http://localhost:8080";

function DogList() {
  const sheltersId = localStorage.getItem("shelters_id");
  const [dogs, setDogs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

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

        // API가 배열을 바로 반환하면
        const dogsData = Array.isArray(response.data) ? response.data : response.data.dogs || [];

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
          {dogs.map((dog) => (
            <tr key={dog.id}>
              <td>
                {dog.imagesUrls && dog.imagesUrls.length > 0 ? (
                  <img
                    src={BACKEND_URL + dog.imagesUrls[0]}
                    alt={dog.name}
                    className="doglist-img"
                  />
                ) : (
                  <div className="doglist-img doglist-img-placeholder" />
                )}
              </td>
              <td>
                <Link to={`/shelter/dog/${dog.id}`} className="doglist-link">
                  {dog.name}
                </Link>
              </td>
              <td>{dog.gender === "MALE" ? "수컷" : dog.gender === "FEMALE" ? "암컷" : dog.gender}</td>
              <td>{dog.age}살</td>
              <td>{dog.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default DogList;
