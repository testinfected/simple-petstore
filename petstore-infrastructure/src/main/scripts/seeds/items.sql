TRUNCATE items;
TRUNCATE products;

INSERT INTO `products` (`id`, `name`, `description`, `photo_file_name`, `number`) VALUES
(21, 'Reptiles', 'Cold-blooded friends', 'iguana.png', '60090989'),
(22, 'Dogs', "Our best friends", 'dog.png', '70080944');

INSERT INTO `items` (`id`, `number`, `product_id`, `description`, `price`) VALUES
(8, '12345678', 21, 'Green adult lizard', 18.50),
(9, '12345679', 21, 'Madagascar iguana', 37.95),
(10, '12345680', 21, 'Spiny tailed iguana', 49.99),
(11, '98765432', 22, 'Golden Retriever', 79.99),
(12, '98765433', 22, 'Labrador Retriever black male', 69.99);