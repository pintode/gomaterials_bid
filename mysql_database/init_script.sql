START TRANSACTION;


USE gomaterials;


DROP TABLE bid_response;
DROP TABLE plant_item;
DROP TABLE bid_request;
DROP TABLE supplier;
DROP TABLE landscaper;


CREATE TABLE landscaper (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_landscaper__name (name)
);

CREATE TABLE supplier (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_supplier__name (name)
);

CREATE TABLE bid_request (
  id bigint NOT NULL AUTO_INCREMENT,
  created_at datetime(6) NOT NULL,
  project_name varchar(255) NOT NULL,
  required_by date NOT NULL,
  status enum('OPEN', 'IN_PROGRESS', 'AWARDED', 'COMPLETED', 'CANCELLED') NOT NULL,
  landscaper_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  KEY fk_bid_request__landscaper_id (landscaper_id),
  CONSTRAINT fk_bid_request__landscaper_id FOREIGN KEY (landscaper_id) REFERENCES landscaper (id)
);

CREATE TABLE plant_item (
  id bigint NOT NULL AUTO_INCREMENT,
  grade varchar(100) NOT NULL,
  name varchar(255) NOT NULL,
  quantity bigint NOT NULL,
  unit varchar(50) NOT NULL,
  bid_request_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  KEY idx_plant_item__bid_request_id (bid_request_id),
  CONSTRAINT fk_plant_item__bid_request_id FOREIGN KEY (bid_request_id) REFERENCES bid_request (id)
);

CREATE TABLE bid_response (
  id bigint NOT NULL AUTO_INCREMENT,
  created_at datetime(6) NOT NULL,
  estimated_delivery_date date DEFAULT NULL,
  notes varchar(2048) DEFAULT NULL,
  status enum('SUBMITTED', 'AWARDED', 'REJECTED', 'WITHDRAWN', 'CANCELLED') NOT NULL,
  total_price decimal(12, 2) DEFAULT NULL,
  bid_request_id bigint DEFAULT NULL,
  supplier_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  KEY fk_bid_response__bid_request_id (bid_request_id),
  KEY fk_bid_response__supplier_id (supplier_id),
  CONSTRAINT fk_bid_response__bid_request_id FOREIGN KEY (bid_request_id) REFERENCES bid_request (id),
  CONSTRAINT fk_bid_response__supplier_id FOREIGN KEY (supplier_id) REFERENCES supplier (id)
);


INSERT INTO landscaper(id, name) VALUES
  (1, 'GreenScape Inc.'),
  (2, 'Maple Leaf Gardens'),
  (3, 'Urban Roots'),
  (4, 'Northern Bloom'),
  (5, 'Evergreen Designs'),
  (6, 'True North Landscapes');
ALTER TABLE landscaper AUTO_INCREMENT = 6;

INSERT INTO supplier(id, name) VALUES
  (1, 'PlantPro Supply'),
  (2, 'Northern Nurseries'),
  (3, 'GreenThumb Supply'),
  (4, 'Maple Grove Supply'),
  (5, 'Evergreen Wholesale'),
  (6, 'True Leaf Supply');
ALTER TABLE supplier AUTO_INCREMENT = 6;

INSERT INTO bid_request(id, created_at, project_name, required_by, status, landscaper_id) VALUES
  (1, '2025-08-08 16:09:17.209899', 'Garden Renovation', '2025-08-14', 'IN_PROGRESS', 4),
  (2, '2025-08-08 16:11:12.854742', 'Park Landscaping', '2025-08-28', 'IN_PROGRESS', 1),
  (3, '2025-08-08 16:13:48.186695', 'Residential Yard Design', '2025-08-21', 'IN_PROGRESS', 1);
ALTER TABLE bid_request AUTO_INCREMENT = 3;

INSERT INTO plant_item(id, grade, name, quantity, unit, bid_request_id) VALUES
  (1, '#1', 'Red Oak', 30, 'units', 1),
  (2, '#2', 'Hydrangea', 40, 'units', 1),
  (3, '#1', 'Black-eyed Susan', 80, 'units', 1),
  (4, '#1', 'Sugar Maple', 10, 'units', 2),
  (5, '#1', 'Eastern Redbud', 25, 'units', 2),
  (6, '#2', 'River Birch', 20, 'units', 2),
  (7, '#1', 'Serviceberry', 70, 'units', 3),
  (8, '#1', 'Red Twig Dogwood', 60, 'units', 3),
  (9, '#1', 'Dogwood', 60, 'units', 3);
ALTER TABLE plant_item AUTO_INCREMENT = 9;

INSERT INTO bid_response(id, created_at, estimated_delivery_date, notes, status, total_price, bid_request_id, supplier_id) VALUES
  (1, '2025-08-07 16:20:00.000000', '2025-08-14', '', 'SUBMITTED', 12000.00, 1, 6),
  (2, '2025-08-07 16:21:00.000000', '2025-08-07', '', 'SUBMITTED', 11500.00, 1, 3),
  (3, '2025-08-07 16:22:00.000000', '2025-08-27', '', 'SUBMITTED', 28500.00, 2, 5),
  (4, '2025-08-07 16:23:00.000000', '2025-08-27', '', 'SUBMITTED', 31800.00, 2, 1),
  (5, '2025-08-07 16:24:00.000000', '2025-08-28', '', 'SUBMITTED', 26300.00, 2, 4),
  (6, '2025-08-07 16:25:00.000000', '2025-08-18', '', 'SUBMITTED', 16000.00, 3, 2),
  (7, '2025-08-07 16:26:00.000000', '2025-08-12', '', 'SUBMITTED', 18000.00, 3, 5);
ALTER TABLE bid_response AUTO_INCREMENT = 7;


COMMIT;