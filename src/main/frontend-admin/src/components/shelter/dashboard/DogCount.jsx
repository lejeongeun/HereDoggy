import { useEffect, useState } from "react";
import { getDogs } from "../../../api/shelter/dog";

function DogCount() {
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

    const activeDogsCount = dogs.filter(dog => dog.status === "AVAILABLE" || dog.status === "RESERVED").length;

        return (
            <div>
            <p>{activeDogsCount}</p>
            <h2>보호중</h2>
            </div>
        );
}

export default DogCount;