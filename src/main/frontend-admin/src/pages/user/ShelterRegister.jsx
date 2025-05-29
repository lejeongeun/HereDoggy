import { useState } from "react";
import { requestShelter } from "../../api/shelter/shelter";
import { useNavigate } from "react-router-dom";
import '../../styles/user/shelterRegister.css';

function ShelterRegister() {
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

  const handleSubmit = async (e) => {
    e.preventDefault();

    // 간단 유효성 검사
    if (
      !form.shelterName ||
      !form.phone ||
      !form.zipcode ||
      !form.address1
    ) {
      alert("필수 항목(이름, 전화번호, 우편번호, 주소1)을 모두 입력해주세요.");
      return;
    }

     setLoading(true);
        try {
    await requestShelter(form);  // 분리된 API 함수 사용
    alert("보호소 생성 요청이 접수되었습니다.\n관리자의 승인을 기다려주세요.");
    navigate("/"); // 요청 후 이동 경로 설정 (홈 등)
  } catch (error) {
    console.error(error);
    alert("요청 중 오류가 발생했습니다.");
  } finally {
    setLoading(false);
  }
};

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: 500, margin: "auto", padding: 20 }}>
      <h2>보호소 생성 요청</h2>

      <label>
        보호소 이름<br />
        <input
          name="shelterName"
          value={form.shelterName}
          onChange={handleChange}
          required
          placeholder="보호소 이름 입력"
        />
      </label>
      <br />

      <label>
        전화번호<br />
        <input
          name="phone"
          value={form.phone}
          onChange={handleChange}
          required
          placeholder="전화번호 입력"
        />
      </label>
      <br />

      <label>
        우편번호<br />
        <input
          name="zipcode"
          value={form.zipcode}
          onChange={handleChange}
          required
          placeholder="우편번호 입력"
        />
      </label>
      <br />

      <label>
        주소<br />
        <input
          name="address1"
          value={form.address1}
          onChange={handleChange}
          required
          placeholder="주소 입력"
        />
      </label>
      <br />

      <label>
        상세주소<br />
        <input
          name="address2"
          value={form.address2}
          onChange={handleChange}
          placeholder="상세주소 입력"
        />
      </label>
      <br />

      <label>
        설명<br />
        <textarea
          name="description"
          value={form.description}
          onChange={handleChange}
          rows={4}
          placeholder="보호소에 대한 설명"
        />
      </label>
      <br />

      <button type="submit" disabled={loading}>
        {loading ? "요청 중..." : "생성 요청 제출"}
      </button>
    </form>
  );
}

export default ShelterRegister;
