CREATE DATABASE IF NOT EXISTS order_db;

-- 사용자 생성 (이미 있으면 오류 안 나게 처리)
CREATE USER IF NOT EXISTS 'order_user'@'%' IDENTIFIED BY 'order_user1234';

-- 권한 부여
GRANT ALL PRIVILEGES ON order_db.* TO 'order_user'@'%';

-- 권한 적용
FLUSH PRIVILEGES;