-- Customers
INSERT INTO customer (id, name, role, loyalty_start_date, blacklisted)
VALUES
(1, 'John Employee', 'EMPLOYEE', '2020-05-01', false),
(2, 'Alice Affiliate', 'AFFILIATE', '2021-01-10', false),
(3, 'Bob Loyal', 'CUSTOMER', '2020-03-15', false),
(4, 'Charlie New', 'CUSTOMER', '2025-01-01', false);

-- Items
INSERT INTO item (id, name, category, price)
VALUES
(1, 'Apples', 'GROCERY', 50),
(2, 'Rice Bag', 'GROCERY', 300),
(3, 'Jeans', 'NON_GROCERY', 1200),
(4, 'Mobile Phone', 'NON_GROCERY', 25000),
(5, 'Notebook', 'NON_GROCERY', 120);
