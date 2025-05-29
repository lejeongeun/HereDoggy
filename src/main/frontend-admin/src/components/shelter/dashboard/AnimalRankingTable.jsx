import 'bootstrap/dist/css/bootstrap.min.css';

function AnimalRankingTable() {
const animals = [
  { name: "코코", count: 11, photo: "/img/coco.jpg" },
  { name: "나비", count: 8, photo: "/img/navi.jpg" },
  { name: "뽀삐", count: 7, photo: "/img/ppopi.jpg" },
  { name: "두부", count: 6, photo: "/img/dubu.jpg" },
  { name: "초코", count: 4, photo: "/img/choco.jpg" }
];
  return (
    <div className="card p-3" style={{ minWidth: 300, maxWidth: 400 }}>
      <h5 className="mb-3">동물별 인기 순위</h5>
      <table className="table table-sm align-middle">
        <thead>
          <tr>
            <th>순위</th>
            <th>이름</th>
            <th>예약수</th>
          </tr>
        </thead>
        <tbody>
          {animals.map((animal, i) => (
            <tr key={animal.name}>
              <td style={{fontWeight: 'bold'}}>{i + 1}</td>
              <td>
                <img
                  src={animal.photo}
                //  alt={animal.name}
                  style={{
                    width: 32,
                    height: 32,
                    objectFit: 'cover',
                    borderRadius: '50%',
                    marginRight: 7,
                    verticalAlign: 'middle'
                  }}
                />
                {animal.name}
              </td>
              <td>{animal.count}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AnimalRankingTable;
