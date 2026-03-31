USE J2EE;
GO

-- Fix garbled UTF-8 bytes stored as Latin-1 in NVARCHAR
-- Using NCHAR() to guarantee correct Unicode codepoints

-- ID 6: "Khoa học"
UPDATE category SET name =
    NCHAR(75)+NCHAR(104)+NCHAR(111)+NCHAR(97)+NCHAR(32)+NCHAR(104)+NCHAR(7885)+NCHAR(99)
WHERE id = 6;

-- ID 7: "Tiểu thuyết"
UPDATE category SET name =
    NCHAR(84)+NCHAR(105)+NCHAR(7875)+NCHAR(117)+NCHAR(32)+NCHAR(116)+NCHAR(104)+NCHAR(117)+NCHAR(121)+NCHAR(7871)+NCHAR(116)
WHERE id = 7;

-- ID 8: "Lịch sử"
UPDATE category SET name =
    NCHAR(76)+NCHAR(7883)+NCHAR(99)+NCHAR(104)+NCHAR(32)+NCHAR(115)+NCHAR(7917)
WHERE id = 8;

-- ID 9: "Tự truyện"
UPDATE category SET name =
    NCHAR(84)+NCHAR(7921)+NCHAR(32)+NCHAR(116)+NCHAR(114)+NCHAR(117)+NCHAR(121)+NCHAR(7879)+NCHAR(110)
WHERE id = 9;

-- ID 10: "Kiến thức"
UPDATE category SET name =
    NCHAR(75)+NCHAR(105)+NCHAR(7871)+NCHAR(110)+NCHAR(32)+NCHAR(116)+NCHAR(104)+NCHAR(7913)+NCHAR(99)
WHERE id = 10;

GO

-- Verify: correct data should show LEN = 8,11,7,9,9
SELECT id, name, LEN(name) AS chars FROM category ORDER BY id;
GO
