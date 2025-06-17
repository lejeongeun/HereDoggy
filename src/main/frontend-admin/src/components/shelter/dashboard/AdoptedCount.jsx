import { useEffect, useState } from "react";
import { getDogs } from "../../../api/shelter/dog";
import '../../../styles/shelter/main/adoptedCount.css';

function AdoptedCount() {
    const shelterId = localStorage.getItem("shelters_id");
    const [dogs, setDogs] = useState([]);

    useEffect(() => {
        if (!shelterId) return;

        const fetchDogs = async () => {
            try {
                const res = await getDogs(shelterId);
                setDogs(res.data);
            } catch (err) {
                console.error("유기견 리스트 조회 실패", err);
            }
        };
        fetchDogs();
    }, [shelterId]);

    const adoptionDogsCount = dogs.filter(dog => dog.status === "ADOPTED").length;

        return (
            <div className="dogcount-card">
            <div className="dogcount-value">{adoptionDogsCount}</div>
            <div className="dogcount-label">이달입양</div>
            </div>
        );
}

export default AdoptedCount;