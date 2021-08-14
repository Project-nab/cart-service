-- insert some data to cart table
insert into cart(session_id, username, status, created_at, updated_at) values
('4EF9C11DD7E95AEA3505D0BF17F23DAC', 'baonc93@gmail.com', 0, '2021-08-14 00:00:00', '2021-08-14 00:00:00');
-- insert some data to cart item table
insert into cart_item(id, cart_session_id, product_code, quantity, created_at, updated_at) values
(1000, '4EF9C11DD7E95AEA3505D0BF17F23DAC', 'ADIDAS_TSHIRT_01', 1, '2021-08-14 00:00:00', '2021-08-14 00:00:00');