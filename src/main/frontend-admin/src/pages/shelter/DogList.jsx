import { useEffect, useState } from "react";
import { getDogs } from "../../api/shelter/dog";
import '../../styles/shelter/pages/dogList.css';
import { Link } from "react-router-dom";

const sheltersId = 1; // 보호소가 등록한 유기견리스트 직접 지정 -> api필요함
const BACKEND_URL = "http://localhost:8080"; // 현재는 이미지 경로에 추가해야 이미지 뜸
function DogList() { // {sheltersId}
  
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
      try {
        const response = await getDogs(sheltersId);
        setDogs(response.data);
      } catch (err) {
        setError("유기견 목록을 불러오는 중 오류가 발생했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchDogs();
  }, []); 

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>{error}</p>;
  if (dogs.length === 0) return <p>등록된 유기견이 없습니다.</p>;

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

              <td>{dog.name}</td>
              <td>{dog.gender}</td>
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
