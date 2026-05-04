-- USER
INSERT INTO user (user_id, email, name, birth_date, address, phone_number, password_hash, status, role, last_login_at, created_at, updated_at)
VALUES
(1, 'ai-coaching-test@naver.com', 'AI코칭테스트', '2000-01-01', '서울시 강남구', '010-1111-2222', 'test', 'ACTIVE', 'USER', NULL, NOW(), NOW()),
(2, 'user2@test.com', '이민수', '2001-02-02', '서울시 마포구', '010-2222-2222', 'test-password', 'ACTIVE', 'USER', NULL, NOW(), NOW()),
(3, 'user3@test.com', '박지은', '2002-03-03', '서울시 서초구', '010-3333-3333', 'test-password', 'ACTIVE', 'USER', NULL, NOW(), NOW());

-- TERMS
INSERT INTO terms (title, content, version, term_type, is_required, is_active, created_at, updated_at)
VALUES
('서비스 이용약관', '서비스 이용약관 내용입니다.', 'v1.0', 'SERVICE', true, true, NOW(), NOW()),
('개인정보 수집 및 이용 동의', '개인정보 수집 및 이용 내용입니다.', 'v1.0', 'PRIVACY', true, true, NOW(), NOW()),
('마케팅 정보 수신 동의', '마케팅 수신 동의 내용입니다.', 'v1.0', 'MARKETING', false, true, NOW(), NOW());

-- SCENARIO
INSERT INTO scenario (scenario_id, prompt, scenario_description, complete_exp, category)
VALUES
(1, 'You are a barista. Take a coffee order.', '카페에서 커피를 주문하는 상황', 10, 'CAFE'),
(2, 'You are a barista. Recommend a menu and take the order.', '카페에서 추천 메뉴를 물어보고 주문하는 상황', 15, 'CAFE'),
(3, 'You are a barista. Handle a complicated order with customization.', '복잡한 커스터마이징 주문을 하는 상황', 25, 'CAFE'),
(4, 'You are an airport staff. Help with check-in.', '공항에서 체크인을 하는 상황', 15, 'AIRPORT'),
(5, 'You are an airport staff. Handle baggage issues.', '수하물 문제를 해결하는 상황', 25, 'AIRPORT'),
(6, 'You are a hotel receptionist. Check in a guest.', '호텔 체크인 상황', 10, 'HOTEL'),
(7, 'You are a hotel staff. Handle a complaint.', '호텔에서 불만을 제기하는 상황', 30, 'HOTEL'),
(8, 'You are a waiter. Take a food order.', '식당에서 음식을 주문하는 상황', 10, 'RESTAURANT'),
(9, 'You are a waiter. Recommend dishes and drinks.', '메뉴 추천을 받고 주문하는 상황', 20, 'RESTAURANT'),
(10, 'You are a shop assistant. Help find a product.', '매장에서 물건을 찾는 상황', 10, 'SHOPPING'),
(11, 'You are a cashier. Process payment and refund.', '결제 및 환불을 처리하는 상황', 20, 'SHOPPING');

-- REGION
INSERT INTO region (region_id, country, city, latitude, longitude)
VALUES
(1, 'Korea', 'Seoul', 37.56650000, 126.97800000),
(2, 'Korea', 'Busan', 35.17960000, 129.07560000),
(3, 'Japan', 'Tokyo', 35.67620000, 139.65030000);

-- MISSION
INSERT INTO mission (mission_title, mission_description, scenario_id)
VALUES
('카페 주문하기', '아메리카노를 주문해보세요.', 1),
('추천 메뉴 물어보기', '추천 메뉴를 질문해보세요.', 2),
('커스터마이징 주문', '샷 추가, 시럽 변경 요청하기', 3),
('공항 체크인', '항공권을 제시하고 체크인하기', 4),
('수하물 문제 해결', '분실된 짐에 대해 문의하기', 5),
('호텔 체크인', '프론트에서 체크인 요청하기', 6),
('호텔 컴플레인', '객실 문제에 대해 불만 제기하기', 7),
('음식 주문하기', '메뉴를 보고 음식 주문하기', 8),
('메뉴 추천 요청', '직원에게 인기 메뉴 물어보기', 9),
('상품 찾기', '원하는 물건 위치 물어보기', 10),
('환불 요청', '구매한 상품 환불 요청하기', 11);

-- PLACE
INSERT INTO place (place_id, google_place_id, place_name, place_description, place_address, latitude, longitude, region_id, scenario_id)
VALUES
(1, 'place-1', '스타벅스 강남점', '조용한 카페', '서울 강남구 테헤란로 123', 37.49790000, 127.02760000, 1, 1),
(2, 'place-2', '투썸플레이스 홍대점', '디저트가 맛있는 카페', '서울 마포구 홍대입구', 37.55630000, 126.92200000, 1, 2),
(3, 'place-3', '김포공항', '국내선 공항', '서울 강서구 공항대로', 37.55830000, 126.79060000, 1, 4),
(4, 'place-4', '인천국제공항', '국제선 공항', '인천 중구 공항로', 37.46020000, 126.44070000, 1, 5),
(5, 'place-5', '롯데호텔 서울', '5성급 호텔', '서울 중구 을지로', 37.56510000, 126.98270000, 1, 6),
(6, 'place-6', '신라호텔', '럭셔리 호텔', '서울 중구 동호로', 37.55690000, 127.00570000, 1, 7),
(7, 'place-7', '맥도날드 강남점', '패스트푸드점', '서울 강남구 역삼동', 37.49820000, 127.02760000, 1, 8),
(8, 'place-8', '이케아 광명점', '가구 쇼핑몰', '경기 광명시 일직로', 37.42300000, 126.88260000, 2, 10);

-- LEARNING SESSION
INSERT INTO learning_session (session_id, user_id, place_id, study_status, start_time, end_time)
VALUES
(1, 1, 1, 'COMPLETED', NOW(), NOW()),
(2, 1, 2, 'COMPLETED', NOW() - INTERVAL 25 MINUTE, NOW()),
(3, 2, 2, 'COMPLETED', NOW() - INTERVAL 20 MINUTE, NOW()),
(4, 3, 3, 'COMPLETED', NOW() - INTERVAL 15 MINUTE, NOW());

-- SESSION MESSAGE
INSERT INTO session_message (session_id, message, role, created_at)
VALUES
(1, 'Good morning. What would you like to order?', 'ASSISTANT', NOW()),
(1, 'I would like a latte with almond milk, please.', 'USER', NOW());

-- SESSION EVALUATION
INSERT INTO session_evaluation (session_id, evaluation)
VALUES
(1, '표현 좋음, 속도 개선 필요');

-- STUDY LOG
INSERT INTO study_log (study_log_id, user_id, session_id, study_type, earned_exp)
VALUES
(1, 1, 1, 'SCENARIO', 50),
(2, 1, 2, 'PLACE', 30),
(3, 2, 3, 'SCENARIO', 70),
(4, 3, 4, 'PLACE', 40);

-- STUDY SCORE
INSERT INTO study_score (study_score_id, study_log_id, naturalness_score, fluency_score, total_score)
VALUES
(1, 1, 80, 85, 82),
(2, 2, 75, 78, 76),
(3, 3, 90, 92, 91),
(4, 4, 70, 73, 72);

-- GOAL MASTER
INSERT INTO goal_master (goal_master_id, badge_id, goal_type, goal_title, goal_description, target_value, period_type, is_active, created_at, updated_at)
VALUES
(1, NULL, 'STUDY_COUNT', '하루 3회 학습하기', '하루에 학습을 3회 완료합니다.', 3, 'DAILY', true, NOW(), NOW()),
(2, NULL, 'STUDY_TIME', '하루 30분 학습하기', '하루에 30분 이상 학습합니다.', 30, 'DAILY', true, NOW(), NOW()),
(3, NULL, 'SPEAKING_COUNT', '말하기 5회 완료', '말하기 학습을 5회 완료합니다.', 5, 'WEEKLY', true, NOW(), NOW());

-- USER GOAL
INSERT INTO user_goal (user_goal_id, user_id, goal_master_id, current_value, status, start_date, end_date, completed_at, created_at, updated_at)
VALUES
(1, 1, 1, 1, 'ACTIVE', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 DAY), NULL, NOW(), NOW()),
(2, 1, 2, 100, 'ACTIVE', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), NULL, NOW(), NOW());

-- FAVORITE PLACE
INSERT INTO favorite_place (favorite_place_id, user_id, place_id, created_at)
VALUES
(1, 1, 1, NOW()),
(2, 1, 2, NOW());

-- FAVORITE SCENARIO
INSERT INTO favorite_scenario (favorite_scenario_id, user_id, scenario_id, created_at)
VALUES
(1, 1, 1, NOW()),
(2, 1, 2, NOW());

-- FRIENDSHIP
INSERT INTO friendship (friendship_id, requester_id, addressee_id, status, requested_at, responded_at)
VALUES
(1, 1, 2, 'PENDING', NOW(), NULL),
(2, 2, 3, 'ACCEPTED', NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY),
(3, 1, 3, 'REJECTED', NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 2 DAY),
(4, 3, 1, 'BLOCKED', NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 3 DAY);

-- USER REPORT
INSERT INTO user_report (report_id, reporter_id, reported_user_id, reason, status, created_at, processed_at, admin_memo)
VALUES
(1, 1, 2, '욕설 및 부적절한 행동', 'PENDING', NOW(), NULL, NULL),
(2, 2, 3, '스팸성 메시지', 'RESOLVED', NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY, '처리 완료'),
(3, 3, 1, '신고 사유 부족', 'REJECTED', NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 2 DAY, '반려');