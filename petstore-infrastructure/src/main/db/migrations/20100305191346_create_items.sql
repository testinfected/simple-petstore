CREATE TABLE items (
  id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  reference_number VARCHAR (8) NOT NULL UNIQUE,
  product_id BIGINT(20) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  INDEX (product_id),
  FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB;