CREATE TABLE items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  reference_number VARCHAR (8) NOT NULL,  
  product_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  INDEX (product_id),
  FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB;