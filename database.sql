-- Tạo database J2EE
CREATE DATABASE J2EE;
GO

USE J2EE;
GO

-- 1. Tạo bảng role
CREATE TABLE role (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(50) NOT NULL UNIQUE
);

-- 2. Tạo bảng account
CREATE TABLE account (
    id INT PRIMARY KEY IDENTITY(1,1),
    login_name NVARCHAR(100) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL
);

-- 3. Tạo bảng category
CREATE TABLE category (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(255) NOT NULL
);

-- 4. Tạo bảng book
CREATE TABLE book (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(255) NOT NULL,
    author NVARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    image NVARCHAR(200),
    category_id INT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- 5. Tạo bảng account_role (N-N relationship)
CREATE TABLE account_role (
    account_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (account_id, role_id),
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);
GO

-- 6. Thêm dữ liệu mẫu
-- Thêm roles
INSERT INTO role (name) VALUES (N'ROLE_USER');
INSERT INTO role (name) VALUES (N'ROLE_ADMIN');

-- Thêm tài khoản (password là bcrypt hash của "123456")
INSERT INTO account (login_name, password) VALUES (N'admin', N'$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy2QRZM');
INSERT INTO account (login_name, password) VALUES (N'user', N'$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy2QRZM');

-- Gán roles cho users
INSERT INTO account_role (account_id, role_id) VALUES (1, 2); -- admin có ROLE_ADMIN
INSERT INTO account_role (account_id, role_id) VALUES (2, 1); -- user có ROLE_USER

-- Thêm danh mục
INSERT INTO category (name) VALUES (N'Khoa học');
INSERT INTO category (name) VALUES (N'Tiểu thuyết');
INSERT INTO category (name) VALUES (N'Lịch sử');
INSERT INTO category (name) VALUES (N'Tự truyện');
INSERT INTO category (name) VALUES (N'Kiến thức');

-- Thêm một số sách mẫu
INSERT INTO book (title, author, price, image, category_id) VALUES 
(N'Tôi là Malala', N'Malala Yousafzai', 150000, N'malala.jpg', 4);

INSERT INTO book (title, author, price, image, category_id) VALUES 
(N'Đắc nhân tâm', N'Dale Carnegie', 120000, N'dacnhantam.jpg', 5);

INSERT INTO book (title, author, price, image, category_id) VALUES 
(N'Lịch sử Việt Nam', N'Trần Trọng Dương', 200000, N'lichsu.jpg', 3);

GO

-- Hiển thị dữ liệu đã tạo
SELECT 'Bảng role:' AS [Thông tin];
SELECT * FROM role;

SELECT '' AS [Thông tin];
SELECT 'Bảng account:' AS [Thông tin];
SELECT * FROM account;

SELECT '' AS [Thông tin];
SELECT 'Bảng category:' AS [Thông tin];
SELECT * FROM category;

SELECT '' AS [Thông tin];
SELECT 'Bảng book:' AS [Thông tin];
SELECT * FROM book;

SELECT '' AS [Thông tin];
SELECT 'Bảng account_role:' AS [Thông tin];
SELECT * FROM account_role;
