import { Link } from "react-router-dom";

function AddRouteCard({ onAddNewRoute }) {
  return (
    <div
      onClick={onAddNewRoute}
      style={{
        border: "2px dashed #aac",
        borderRadius: 12,
        padding: 24,
        background: "#f4f7fa",
        minHeight: 200,
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        textDecoration: "none",
        color: "#888",
        fontSize: 36,
        fontWeight: "bold",
        cursor: "pointer",
      }}
    >
      <span style={{ fontSize: 60, marginBottom: 12 }}>+</span>
      <div style={{ fontSize: 18 }}>산책로 추가</div>
    </div>
  );
}

export default AddRouteCard;

