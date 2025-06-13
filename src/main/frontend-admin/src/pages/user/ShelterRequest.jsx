import { useState } from "react";
import { requestShelter } from "../../api/shelter/shelter";
import { useNavigate } from "react-router-dom";
import '../../styles/user/shelterRequest.css';

function ShelterRequest() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    shelterName: "",
    phone: "",
    zipcode: "",
    address1: "",
    address2: "",
    description: "",
  });

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // 주소 검색 함수
  const handleAddressSearch = () => {
    new window.daum.Postcode({
      oncomplete: function (data) {
        let fullAddr = data.address;
        let extraAddr = '';
        if (data.addressType === 'R') {
          if (data.bname !== '') extraAddr += data.bname;
          if (data.buildingName !== '') extraAddr += (extraAddr ? ', ' + data.buildingName : data.buildingName);
          if (extraAddr !== '') fullAddr += ' (' + extraAddr + ')';
        }
        setForm(prev => ({
          ...prev,
          zipcode: data.zonecode,
          address1: fullAddr,
        }));
      }
    }).open();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!form.shelterName || !form.phone || !form.zipcode || !form.address1) {
      alert("필수 항목(이름, 전화번호, 우편번호, 주소1)을 모두 입력해주세요.");
      return;
    }

    setLoading(true);

    try {
      await requestShelter(form); // form 그대로 JSON 전송
      alert("보호소 생성 요청이 접수되었습니다.\n관리자의 승인을 기다려주세요.");
      navigate("/");
    } catch (error) {
      console.error(error);
      alert("요청 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="shelter-request-container">
      <form className="shelter-request-form" onSubmit={handleSubmit}>
        <h2>보호소 생성 요청</h2>
        <div className="form-group">
          <label htmlFor="shelterName">보호소 이름</label>
          <input
            id="shelterName"
            name="shelterName"
            value={form.shelterName}
            onChange={handleChange}
            required
            placeholder="보호소 이름 입력"
          />
        </div>

        <div className="form-group">
          <label htmlFor="phone">전화번호</label>
          <input
            id="phone"
            name="phone"
            value={form.phone}
            onChange={handleChange}
            required
            placeholder="전화번호 입력"
          />
        </div>

        <div className="form-group">
          <label htmlFor="zipcode">우편번호</label>
          <div className="zip-row">
            <input
              id="zipcode"
              name="zipcode"
              value={form.zipcode}
              onChange={handleChange}
              required
              placeholder="우편번호 입력"
              readOnly
            />
            <button
              type="button"
              className="address-search-btn"
              onClick={handleAddressSearch}
            >
              주소 검색
            </button>
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="address1">주소</label>
          <input
            id="address1"
            name="address1"
            value={form.address1}
            onChange={handleChange}
            required
            placeholder="주소 입력"
            readOnly
          />
        </div>

        <div className="form-group">
          <label htmlFor="address2">상세주소</label>
          <input
            id="address2"
            name="address2"
            value={form.address2}
            onChange={handleChange}
            placeholder="상세주소 입력"
          />
        </div>

        <div className="form-group">
          <label htmlFor="description">설명</label>
          <textarea
            id="description"
            name="description"
            value={form.description}
            onChange={handleChange}
            rows={4}
            placeholder="보호소에 대한 설명"
          />
        </div>

        {/* 이미지 업로드 부분은 제거됨 */}

        <button type="submit" className="signupbtn" disabled={loading}>
          {loading ? "요청 중..." : "생성 요청 제출"}
        </button>
      </form>
    </div>
  );
}

export default ShelterRequest;
