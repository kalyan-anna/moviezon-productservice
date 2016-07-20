CREATE TABLE product (
  id INTEGER PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  synopsis VARCHAR(500) NOT NULL,
  rating VARCHAR(5) NOT NULL,
  duration VARCHAR(50) NOT NULL,
  year INTEGER NOT NULL,
  cast VARCHAR(100) NOT NULL,
  director VARCHAR(100) NOT NULL,
  genre VARCHAR(10) NOT NULL,
  image_filename VARCHAR(20) NOT NULL,
  category VARCHAR(20) NOT NULL,
  price DECIMAL NOT NULL
);

CREATE TABLE inventory (
	id INTEGER PRIMARY KEY,
    product_id INTEGER NOT NULL,
    stock_count INTEGER NOT NULL,
    CONSTRAINT fk_product_inventory FOREIGN KEY (product_id) REFERENCES product(Id)
);

