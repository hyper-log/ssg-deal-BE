-- 더미 데이터 삽입 시작

START TRANSACTION;

-- 기존 데이터 삭제
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE product_option;
TRUNCATE TABLE product;
TRUNCATE TABLE company;
TRUNCATE TABLE promotion;

SET FOREIGN_KEY_CHECKS = 1;

-- 프로모션 데이터
INSERT INTO promotion (
        id, title, preview_url, content, content_image_url, status,
        start_promotion_date, end_promotion_date,
        created_at, created_by, updated_at, updated_by,
        is_deleted
) VALUES
          (1, '봄 패션 위크', '봄 옷 최대 50% 세일!', '트렌디한 봄 패션 아이템을 만나보세요.', 'https://example.com/spring-fashion.jpg', 'FINISHED',
           CURRENT_TIMESTAMP - INTERVAL 10 DAY, CURRENT_TIMESTAMP - INTERVAL 2 DAY,
           CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (2, '브런치 카페 페스티벌', '브런치 세트 할인 중!', '감성 가득한 카페 메뉴를 즐겨보세요.', 'https://example.com/cafe-event.jpg', 'IN_PROGRESS',
           CURRENT_TIMESTAMP - INTERVAL 1 DAY, CURRENT_TIMESTAMP + INTERVAL 7 DAY,
           CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (3, '뷰티 빅 세일', '화장품 최대 70% 할인!', '인기 화장품 브랜드 총출동!', 'https://example.com/beauty-sale.jpg', 'PENDING',
           CURRENT_TIMESTAMP + INTERVAL 1 DAY, CURRENT_TIMESTAMP + INTERVAL 15 DAY,
           CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (4, '프랑스식 갈릭 바게트 할인', '겉바속촉! 바게트 할인 중', '전통 프랑스식 바게트를 최대 20% 할인된 가격으로 만나보세요.', 'https://example.com/baguette-event.jpg', 'IN_PROGRESS',
           CURRENT_TIMESTAMP + INTERVAL 1 DAY, CURRENT_TIMESTAMP + INTERVAL 5 DAY,
           CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (5, '주방용품 기획전', '주방 조리도구 세트 특가!', '원목으로 만든 조리도구 세트를 특별한 가격에!', 'https://example.com/kitchen-event.jpg', 'IN_PROGRESS',
           CURRENT_TIMESTAMP + INTERVAL 1 DAY, CURRENT_TIMESTAMP + INTERVAL 8 DAY,
           CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (6, '캠핑 준비는 여기서!', '피크닉&캠핑템 할인전', '캠핑 테이블, 피크닉 매트를 저렴하게 만나보세요.', 'https://example.com/camping-event.jpg', 'IN_PROGRESS',
           CURRENT_TIMESTAMP + INTERVAL 1 DAY, CURRENT_TIMESTAMP + INTERVAL 4 DAY,
           CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (7, '빈티지 감성 유리컵 할인전', '빈티지 유리컵 4개 세트', '레트로 감성 가득한 유리컵이 20% 할인 중!', 'https://example.com/glass-event.jpg', 'IN_PROGRESS',
           CURRENT_TIMESTAMP - INTERVAL 5 DAY, CURRENT_TIMESTAMP + INTERVAL 2 DAY,
           CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (8, '리빙 소품 할인', '마카롱 수저받침 모음전', '컬러풀한 리빙 감성 소품 할인!', 'https://example.com/living-event.jpg', 'IN_PROGRESS',
           CURRENT_TIMESTAMP - INTERVAL 1 DAY, CURRENT_TIMESTAMP + INTERVAL 9 DAY,
           CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (9, '욕실 디퓨저 특가', '은은한 향기로 집안을 채우세요.', '아로마 디퓨저 2종 최대 20% 할인', 'https://example.com/diffuser-event.jpg', 'IN_PROGRESS',
           CURRENT_TIMESTAMP - INTERVAL 2 DAY, CURRENT_TIMESTAMP + INTERVAL 10 DAY,
           CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (10, '홈카페 소품전', '라탄 트레이 할인!', '홈카페 감성 인테리어 소품을 만나보세요.', 'https://example.com/homecafe-event.jpg', 'IN_PROGRESS',
           CURRENT_TIMESTAMP - INTERVAL 1 DAY, CURRENT_TIMESTAMP + INTERVAL 12 DAY,
           CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false);

-- 업체 데이터
INSERT INTO company (
        id, name, logo_url, manager_id, promotion_id,
        created_at, created_by, updated_at, updated_by,
        is_deleted
) VALUES
          (1, '트렌드클로젯', 'https://example.com/fashion-logo.png', 101, 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (2, '브런치타임', 'https://example.com/cafe-logo.png', 102, 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (3, '뷰티하우스', 'https://example.com/beauty-logo.png', 103, 3, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (4, '바게뜨앤코', 'https://example.com/baguette-logo.png', 104, 4, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (5, '키친라이프', 'https://example.com/kitchen-logo.png', 105, 5, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (6, '캠프존', 'https://example.com/camp-logo.png', 106, 6, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (7, '빈티지글라스', 'https://example.com/glass-logo.png', 107, 7, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (8, '리빙조이', 'https://example.com/living-logo.png', 108, 8, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (9, '디퓨저코리아', 'https://example.com/diffuser-logo.png', 109, 9, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (10, '홈카페리빙', 'https://example.com/homecafe-logo.png', 110, 10, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false);

-- 상품 데이터
-- 상품 데이터 (version 컬럼 추가: 모두 0으로 설정)
INSERT INTO product (
        id, name, original_price, promotion_price,
        preview_url, content_image_url, content, company_id,
        created_at, created_by, updated_at, updated_by,
        is_deleted, version
) VALUES
          (1, '플로럴 봄 원피스', 55000, 39000, 'https://example.com/dress-preview.jpg', 'https://example.com/dress-detail.jpg', '화사한 봄 원피스입니다.', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (2, '라이트 데님 자켓', 72000, 52000, 'https://example.com/jacket-preview.jpg', 'https://example.com/jacket-detail.jpg', '간절기용 가벼운 자켓.', 1, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (3, '디저트 세트', 12000, 22000, 'https://example.com/dessert-detail2.jpg', 'https://example.com/dessert-detail2.jpg', '맛있는 디저트.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (4, '디저트 플레이트', 12000, 9800, 'https://example.com/dessert-preview.jpg', 'https://example.com/dessert-detail.jpg', '다양한 디저트 구성.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (5, '촉촉한 수분크림', 30000, 21000, 'https://example.com/cream-preview.jpg', 'https://example.com/cream-detail.jpg', '건조한 피부를 위한 수분크림.', 3, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (6, '립 틴트 3종 세트', 27000, 18900, 'https://example.com/tint-preview.jpg', 'https://example.com/tint-detail.jpg', '데일리용 틴트 세트.', 3, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (7, '프랑스식 갈릭 바게트', 8000, 6400, 'https://example.com/baguette-preview.jpg', 'https://example.com/baguette-detail.jpg', '바삭하고 고소한 프랑스식 바게트.', 4, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (8, '레몬 타르트', 9500, 8500, 'https://example.com/tart-preview.jpg', 'https://example.com/tart-detail.jpg', '상큼한 레몬향이 가득한 타르트.', 4, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (9, '원목 주방 조리도구 세트', 35000, 27000, 'https://example.com/wood-utensils-preview.jpg', 'https://example.com/wood-utensils-detail.jpg', '친환경 원목 조리도구 5종 세트.', 5, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (10, '실리콘 냄비 받침', 5500, 3900, 'https://example.com/silicone-mat-preview.jpg', 'https://example.com/silicone-mat-detail.jpg', '고온에 강한 실리콘 받침.', 5, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (11, '미니 캠핑 테이블', 42000, 29900, 'https://example.com/camp-table-preview.jpg', 'https://example.com/camp-table-detail.jpg', '가볍고 튼튼한 캠핑용 테이블.', 6, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (12, '피크닉 매트', 26000, 19800, 'https://example.com/picnic-mat-preview.jpg', 'https://example.com/picnic-mat-detail.jpg', '방수 소재의 피크닉 매트.', 6, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (13, '빈티지 유리컵 세트', 24000, 19000, 'https://example.com/glass-preview.jpg', 'https://example.com/glass-detail.jpg', '빈티지 감성 유리컵 4개 세트.', 7, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (14, '마카롱 색감 수저받침', 4200, 3500, 'https://example.com/spoon-rest-preview.jpg', 'https://example.com/spoon-rest-detail.jpg', '알록달록 감성 수저받침.', 8, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (15, '아로마 디퓨저', 19000, 15500, 'https://example.com/diffuser-preview.jpg', 'https://example.com/diffuser-detail.jpg', '은은한 향기의 디퓨저.', 9, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (16, '라탄 트레이', 17000, 13000, 'https://example.com/rattan-tray-preview.jpg', 'https://example.com/rattan-tray-detail.jpg', '자연 감성의 라탄 트레이.', 10, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (17, '프렌치 토스트 세트', 15000, 25000, 'https://example.com/french-toast.jpg', 'https://example.com/french-toast-detail.jpg', '달콤한 프렌치 토스트와 음료 세트.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (18, '에그 베네딕트', 14000, 23000, 'https://example.com/egg-benedict.jpg', 'https://example.com/egg-benedict-detail.jpg', '신선한 재료로 만든 에그 베네딕트.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (19, '브런치 플래터', 18000, 28000, 'https://example.com/brunch-platter.jpg', 'https://example.com/brunch-platter-detail.jpg', '소시지, 베이컨, 샐러드가 어우러진 브런치 세트.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (20, '아보카도 오픈 샌드위치', 13000, 21000, 'https://example.com/avocado-toast.jpg', 'https://example.com/avocado-toast-detail.jpg', '고소한 아보카도와 토마토의 조화.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (21, '리코타 치즈 샐러드', 11000, 19000, 'https://example.com/ricotta-salad.jpg', 'https://example.com/ricotta-salad-detail.jpg', '신선한 채소와 리코타 치즈의 환상 궁합.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (22, '수제 요거트 볼', 9000, 15000, 'https://example.com/yogurt-bowl.jpg', 'https://example.com/yogurt-bowl-detail.jpg', '수제 요거트와 그래놀라, 과일 토핑.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (23, '허니버터 팬케이크', 12000, 20000, 'https://example.com/honey-pancake.jpg', 'https://example.com/honey-pancake-detail.jpg', '달콤한 허니버터와 부드러운 팬케이크.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (24, '크로크 마담', 16000, 25000, 'https://example.com/croque-madame.jpg', 'https://example.com/croque-madame-detail.jpg', '고소한 치즈와 햄이 듬뿍.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (25, '아메리칸 브렉퍼스트', 17000, 27000, 'https://example.com/american-breakfast.jpg', 'https://example.com/american-breakfast-detail.jpg', '풍성한 아침 한상 차림.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (26, '트러플 머쉬룸 리조또', 18000, 28000, 'https://example.com/truffle-risotto.jpg', 'https://example.com/truffle-risotto-detail.jpg', '트러플 향 가득 머쉬룸 리조또.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (27, '디저트 세트', 12000, 22000, 'https://example.com/dessert-detail2.jpg', 'https://example.com/dessert-detail2.jpg', '다양한 디저트와 음료 세트.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (28, '수제 스콘과 잼', 8000, 14000, 'https://example.com/scone-jam.jpg', 'https://example.com/scone-jam-detail.jpg', '갓 구운 스콘과 달콤한 잼.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (29, '바질 페스토 파스타', 15000, 25000, 'https://example.com/pesto-pasta.jpg', 'https://example.com/pesto-pasta-detail.jpg', '상큼한 바질 페스토의 풍미.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0),
          (30, '아이스 아메리카노', 5000, 9000, 'https://example.com/iced-americano.jpg', 'https://example.com/iced-americano-detail.jpg', '깔끔하고 시원한 커피.', 2, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false, 0);

INSERT INTO product_option (
        id, product_id, option_name, extra_price, product_stock,
        created_at, created_by, updated_at, updated_by,
        is_deleted
) VALUES
          (1, 1, '핑크/M', 0, 100, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (2, 1, '핑크/L', 1000, 80, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (13, 1, '브라운/S', 2000, 80, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (14, 1, '브라운/M', 3000, 80, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (15, 1, '브라운/L', 5000, 80, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (3, 2, 'S 사이즈', 0, 90, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (4, 2, 'M 사이즈', 1500, 50, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (5, 3, '커피 선택 - 아메리카노', 0, 200, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (6, 3, '커피 선택 - 라떼', 500, 150, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (16, 3, '커피 선택 - 바닐라 라떼', 500, 150, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (17, 3, '커피 선택 - 딸기 스무디', 2000, 150, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (7, 4, '초콜릿 조각 케이크', 0, 100, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (8, 4, '치즈 케이크', 700, 70, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (9, 5, '50ml', 0, 300, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (10, 5, '100ml', 5000, 150, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (11, 6, '코랄 핑크', 0, 120, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (12, 6, '레드 오렌지', 0, 130, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (18, 7, '기본형', 0, 50, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (19, 7, '허브향 추가', 1000, 30, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (20, 8, '기본형', 0, 20, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (21, 9, '기본 세트 (5종)', 0, 40, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (22, 9, '고급형 세트 (8종)', 5000, 10, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (23, 11, '블랙', 0, 25, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (24, 11, '우드패턴', 3000, 5, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (25, 13, '4개입 세트', 0, 0, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (26, 14, '핑크', 0, 100, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (27, 14, '민트', 0, 90, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (28, 14, '옐로우', 0, 80, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (29, 15, '라벤더 향', 0, 150, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (30, 15, '시트러스 향', 0, 100, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (31, 16, '원형 (25cm)', 0, 40, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false),
          (32, 16, '사각형 (30x20cm)', 2000, 30, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, false);

COMMIT;

-- 더미 데이터 삽입 끝