USE J2EE;
GO

-- Xem tất cả danh mục hiện tại
SELECT id, name FROM category ORDER BY id;
GO

-- Xóa các danh mục bị lỗi encoding (chứa ký tự Latin-1 thay vì Unicode đúng)
-- Các ký tự như á», áº, ï¿, Æ° là dấu hiệu của lỗi encoding
DELETE FROM category
WHERE name LIKE N'%á»%'
   OR name LIKE N'%áº%'
   OR name LIKE N'%ï¿%'
   OR name LIKE N'%Ã%';
GO

-- Kiểm tra lại sau khi xóa
SELECT id, name FROM category ORDER BY id;
GO
