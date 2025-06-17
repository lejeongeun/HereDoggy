function AnimalRankingTable() {
  const animals = [
    { name: "코코", count: 11, photo: "/img/coco.jpg" },
    { name: "나비", count: 8, photo: "/img/navi.jpg" },
    { name: "뽀삐", count: 7, photo: "/img/ppopi.jpg" },
    { name: "두부", count: 6, photo: "/img/dubu.jpg" },
    { name: "초코", count: 4, photo: "/img/choco.jpg" }
  ];

  return (
    <div className="dashboard-widget">
      <div className="table-title">동물별 인기 순위</div>
      <table className="animal-ranking-table">
        <thead>
          <tr>
            <th>순위</th>
            <th>이름</th>
            <th>예약수</th>
          </tr>
        </thead>
        <tbody>
          {animals.map((animal, i) => {
            return (
              <tr key={animal.name}>
                <td style={{ fontWeight: 700, color: 'black' }}>{i + 1}</td>
                <td style={{ display: 'flex', alignItems: 'center',  justifyContent: 'center', gap: 7 }}>
                  <img
                    src={animal.photo}
                    alt={animal.name}
                    style={{
                      width: 32,
                      height: 32,
                      objectFit: 'cover',
                      borderRadius: '50%',
                      border: '2px solid #effaf3',
                    }}
                  />
                  <span>{animal.name}</span>
                </td>
                <td>{animal.count}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

export default AnimalRankingTable;
