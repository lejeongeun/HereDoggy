INSERT INTO dog_breed (
    id, breed_name, description, caution, image_url,
    size, hair_loss, exercise_need, independence,
    grooming_need, trainability, activity_demand, medical_cost,
    kid_friendly, hypoallergenic, good_for_first_time_owner
) VALUES
(1, 'MALTESE', '애교 많고 가족 친화적, 아파트에 적합', '눈물자국 주의', NULL,
 'SMALL', 'LOW', 'LOW', 'LOW',
 'HIGH', 'LOW', 'LOW', 'LOW',
 TRUE, TRUE, TRUE),

(2, 'POODLE_TOY', '똑똑하고 훈련 쉬움, 알레르기 적음', '치과 질환 주의', NULL,
 'SMALL', 'LOW', 'MEDIUM', 'MEDIUM',
 'MEDIUM', 'LOW', 'MEDIUM', 'MEDIUM',
 FALSE, TRUE, TRUE),

(3, 'POMERANIAN', '활발하고 자신감 넘침, 털 많음', '기관지 문제 주의', NULL,
 'SMALL', 'HIGH', 'MEDIUM', 'MEDIUM',
 'HIGH', 'MEDIUM', 'MEDIUM', 'MEDIUM',
 TRUE, TRUE, TRUE),

(4, 'CHIHUAHUA', '작고 충성심 강함, 경계심 있음', '저체온, 치아 문제 주의', NULL,
 'SMALL', 'LOW', 'LOW', 'HIGH',
 'LOW', 'MEDIUM', 'LOW', 'LOW',
 TRUE, TRUE, FALSE),

(5, 'YORKSHIRE_TERRIER', '용감하고 애교 많음, 실키한 털', '슬개골 탈구 주의', NULL,
 'SMALL', 'LOW', 'LOW', 'MEDIUM',
 'HIGH', 'MEDIUM', 'MEDIUM', 'MEDIUM',
 TRUE, TRUE, FALSE),

(6, 'BEAGLE', '사교적이고 냄새 추적 본능 강함', '비만, 귀 감염 주의', NULL,
 'MEDIUM', 'MEDIUM', 'HIGH', 'MEDIUM',
 'MEDIUM', 'MEDIUM', 'HIGH', 'HIGH',
 TRUE, FALSE, TRUE),

(7, 'WELSH_CORGI', '짧은 다리, 똑똑하고 활발함', '척추 질환 주의', NULL,
 'MEDIUM', 'HIGH', 'HIGH', 'MEDIUM',
 'MEDIUM', 'MEDIUM', 'HIGH', 'MEDIUM',
 TRUE, TRUE, TRUE),

(8, 'JINDO', '충성심 강하고 독립적인 토종견', '사회화 필요', NULL,
 'MEDIUM', 'MEDIUM', 'HIGH', 'HIGH',
 'LOW', 'HIGH', 'HIGH', 'LOW',
 FALSE, FALSE, FALSE),

(9, 'FRENCH_BULLDOG', '느긋하고 조용함, 아파트에 적합', '호흡기 문제 주의', NULL,
 'MEDIUM', 'LOW', 'LOW', 'LOW',
 'LOW', 'LOW', 'LOW', 'HIGH',
 TRUE, TRUE, TRUE),

(10, 'BORDER_COLLIE', '매우 똑똑하고 에너지 넘침', '관절 질환 주의', NULL,
 'MEDIUM', 'MEDIUM', 'VERY_HIGH', 'HIGH',
 'MEDIUM', 'HIGH', 'VERY_HIGH', 'HIGH',
 TRUE, FALSE, TRUE),

(11, 'GOLDEN_RETRIEVER', '온순하고 가족 친화적', '암, 관절 질환 주의', NULL,
 'LARGE', 'HIGH', 'HIGH', 'MEDIUM',
 'MEDIUM', 'LOW', 'HIGH', 'HIGH',
 TRUE, TRUE, TRUE),

(12, 'LABRADOR_RETRIEVER', '다정하고 활동적이며 훈련 쉬움', '비만 주의', NULL,
 'LARGE', 'MEDIUM', 'HIGH', 'MEDIUM',
 'LOW', 'LOW', 'HIGH', 'HIGH',
 TRUE, TRUE, TRUE),

(13, 'GERMAN_SHEPHERD', '충성심 강하고 똑똑함', '소화기, 관절 질환 주의', NULL,
 'LARGE', 'MEDIUM', 'VERY_HIGH', 'HIGH',
 'MEDIUM', 'HIGH', 'VERY_HIGH', 'HIGH',
 TRUE, FALSE, TRUE),

(14, 'SAMOYED', '친화적이고 미소 짓는 얼굴', '피부 질환 주의', NULL,
 'LARGE', 'HIGH', 'HIGH', 'MEDIUM',
 'HIGH', 'MEDIUM', 'HIGH', 'HIGH',
 TRUE, TRUE, TRUE),

(15, 'AKITA', '조용하고 독립적이며 충성심 강함', '갑상선 문제 주의', NULL,
 'LARGE', 'HIGH', 'HIGH', 'HIGH',
 'MEDIUM', 'HIGH', 'HIGH', 'HIGH',
 FALSE, FALSE, FALSE);

-- INSERT INTO dog_breed (
--     id, breed_name, size, hair_loss, exercise_need, kid_friendly, independence,
--     hypoallergenic, popular, good_for_first_time_owner, noise_tolerant,
--     grooming_need, trainability, activity_demand, medical_cost,
--     description, caution, image_url
-- ) VALUES
-- (1, 'MALTESE', 'SMALL', 'LOW', 'LOW', TRUE, 'LOW', TRUE, TRUE, TRUE, TRUE, 'HIGH', 'LOW', 'LOW', 'LOW', '애교 많고 가족 친화적, 아파트에 적합', '눈물자국 주의', NULL),
-- (2, 'POODLE_TOY', 'SMALL', 'LOW', 'MEDIUM', FALSE, 'MEDIUM', TRUE, TRUE, TRUE, TRUE, 'MEDIUM', 'LOW', 'MEDIUM', 'MEDIUM', '똑똑하고 훈련 쉬움, 알레르기 적음', '치과 질환 주의', NULL),
-- (3, 'POMERANIAN', 'SMALL', 'HIGH', 'MEDIUM', TRUE, 'MEDIUM', TRUE, TRUE, TRUE, TRUE, 'HIGH', 'MEDIUM', 'MEDIUM', 'MEDIUM', '활발하고 자신감 넘침, 털 많음', '기관지 문제 주의', NULL),
-- (4, 'CHIHUAHUA', 'SMALL', 'LOW', 'LOW', TRUE, 'HIGH', TRUE, TRUE, FALSE, TRUE, 'LOW', 'MEDIUM', 'LOW', 'LOW', '작고 충성심 강함, 경계심 있음', '저체온, 치아 문제 주의', NULL),
-- (5, 'YORKSHIRE_TERRIER', 'SMALL', 'LOW', 'LOW', TRUE, 'MEDIUM', TRUE, TRUE, FALSE, TRUE, 'HIGH', 'MEDIUM', 'MEDIUM', 'MEDIUM', '용감하고 애교 많음, 실키한 털', '슬개골 탈구 주의', NULL),
-- (6, 'BEAGLE', 'MEDIUM', 'MEDIUM', 'HIGH', TRUE, 'MEDIUM', FALSE, TRUE, TRUE, FALSE, 'MEDIUM', 'MEDIUM', 'HIGH', 'HIGH', '사교적이고 냄새 추적 본능 강함', '비만, 귀 감염 주의', NULL),
-- (7, 'WELSH_CORGI', 'MEDIUM', 'HIGH', 'HIGH', TRUE, 'MEDIUM', TRUE, TRUE, TRUE, TRUE, 'MEDIUM', 'MEDIUM', 'HIGH', 'MEDIUM', '짧은 다리, 똑똑하고 활발함', '척추 질환 주의', NULL),
-- (8, 'JINDO', 'MEDIUM', 'MEDIUM', 'HIGH', FALSE, 'HIGH', FALSE, TRUE, FALSE, FALSE, 'LOW', 'HIGH', 'HIGH', 'LOW', '충성심 강하고 독립적인 토종견', '사회화 필요', NULL),
-- (9, 'FRENCH_BULLDOG', 'MEDIUM', 'LOW', 'LOW', TRUE, 'LOW', TRUE, TRUE, TRUE, TRUE, 'LOW', 'LOW', 'LOW', 'HIGH', '느긋하고 조용함, 아파트에 적합', '호흡기 문제 주의', NULL),
-- (10, 'BORDER_COLLIE', 'MEDIUM', 'MEDIUM', 'VERY_HIGH', TRUE, 'HIGH', FALSE, TRUE, TRUE, FALSE, 'MEDIUM', 'HIGH', 'VERY_HIGH', 'HIGH', '매우 똑똑하고 에너지 넘침', '관절 질환 주의', NULL),
-- (11, 'GOLDEN_RETRIEVER', 'LARGE', 'HIGH', 'HIGH', TRUE, 'MEDIUM', TRUE, TRUE, TRUE, TRUE, 'MEDIUM', 'LOW', 'HIGH', 'HIGH', '온순하고 가족 친화적', '암, 관절 질환 주의', NULL),
-- (12, 'LABRADOR_RETRIEVER', 'LARGE', 'MEDIUM', 'HIGH', TRUE, 'MEDIUM', TRUE, TRUE, TRUE, TRUE, 'LOW', 'LOW', 'HIGH', 'HIGH', '다정하고 활동적이며 훈련 쉬움', '비만 주의', NULL),
-- (13, 'GERMAN_SHEPHERD', 'LARGE', 'MEDIUM', 'VERY_HIGH', TRUE, 'HIGH', FALSE, TRUE, TRUE, FALSE, 'MEDIUM', 'HIGH', 'VERY_HIGH', 'HIGH', '충성심 강하고 똑똑함', '소화기, 관절 질환 주의', NULL),
-- (14, 'SAMOYED', 'LARGE', 'HIGH', 'HIGH', TRUE, 'MEDIUM', TRUE, TRUE, TRUE, TRUE, 'HIGH', 'MEDIUM', 'HIGH', 'HIGH', '친화적이고 미소 짓는 얼굴', '피부 질환 주의', NULL),
-- (15, 'AKITA', 'LARGE', 'HIGH', 'HIGH', FALSE, 'HIGH', FALSE, FALSE, FALSE, FALSE, 'MEDIUM', 'HIGH', 'HIGH', 'HIGH', '조용하고 독립적이며 충성심 강함', '갑상선 문제 주의', NULL);