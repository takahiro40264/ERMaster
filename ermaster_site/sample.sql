
/* Drop Indexes */

DROP INDEX IF EXISTS product_code_index;
DROP INDEX IF EXISTS product_jan_index;



/* Drop Views */

DROP VIEW IF EXISTS customer_order_summary;



/* Drop Tables */

DROP TABLE IF EXISTS order_detail;
DROP TABLE IF EXISTS order_data;
DROP TABLE IF EXISTS customer_master;
DROP TABLE IF EXISTS product_master;




/* Create Tables */

-- 顧客のマスタテーブル
CREATE TABLE customer_master
(
	-- ID
	id serial NOT NULL UNIQUE,
	-- 漢字フルネーム
	name varchar(100) NOT NULL,
	-- 固定電話、携帯電話のいずれか
	tel varchar(100) NOT NULL,
	-- 都道府県から番地まで
	address varchar(300) NOT NULL,
	mail_address varchar(100) NOT NULL,
	-- 画面には表示させないが、DBには値を残しておくためのフラグ
	delete_flag boolean NOT NULL,
	-- 作成日
	create_date timestamp NOT NULL,
	-- 更新日
	update_date timestamp NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (id)
) WITHOUT OIDS;


CREATE TABLE order_data
(
	-- ID
	id serial NOT NULL UNIQUE,
	price int NOT NULL,
	-- 注文情報作成と注文出荷済みの２種類
	status int NOT NULL,
	shipping_date timestamp,
	-- 顧客のID
	customer_id int NOT NULL,
	-- 画面には表示させないが、DBには値を残しておくためのフラグ
	delete_flag boolean NOT NULL,
	-- 作成日
	create_date timestamp NOT NULL,
	-- 更新日
	update_date timestamp NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (id, customer_id)
) WITHOUT OIDS;


CREATE TABLE order_detail
(
	-- ID
	id serial NOT NULL UNIQUE,
	-- 顧客が注文した商品の個数
	order_number int NOT NULL,
	-- 商品ID
	product_id int NOT NULL,
	-- 注文ID
	order_id int NOT NULL,
	-- 画面には表示させないが、DBには値を残しておくためのフラグ
	delete_flag boolean NOT NULL,
	-- 作成日
	create_date timestamp NOT NULL,
	-- 更新日
	update_date timestamp NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (id, product_id, order_id)
) WITHOUT OIDS;


CREATE TABLE product_master
(
	-- ID
	id serial NOT NULL UNIQUE,
	-- 商品のコード
	product_code bytea NOT NULL,
	-- 商品のJANコード
	jancode varchar(50) NOT NULL,
	-- 画面には表示させないが、DBには値を残しておくためのフラグ
	delete_flag boolean NOT NULL,
	-- 作成日
	create_date timestamp NOT NULL,
	-- 更新日
	update_date timestamp NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (id, product_code, jancode)
) WITHOUT OIDS;



/* Create Foreign Keys */

ALTER TABLE order_data
	ADD FOREIGN KEY (customer_id)
	REFERENCES customer_master (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE order_detail
	ADD FOREIGN KEY (order_id)
	REFERENCES order_data (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE order_detail
	ADD FOREIGN KEY (product_id)
	REFERENCES product_master (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



/* Create Views */

CREATE VIEW customer_order_summary AS SELECT
    c.id,  
    c.name,
    c.mail_address,
    SUM(od.order_number) AS total_orders_amount
FROM
    customer_master c
JOIN
    order_data o ON c.id = o.customer_id
JOIN
    order_detail od ON o.id = od.order_id
GROUP BY
    c.id, c.name, c.mail_address;



/* Create Indexes */

CREATE INDEX product_code_index ON product_master USING BTREE (product_code);
CREATE INDEX product_jan_index ON product_master USING BTREE (jancode);



/* Comments */

COMMENT ON TABLE customer_master IS '顧客のマスタテーブル';
COMMENT ON COLUMN customer_master.id IS 'ID';
COMMENT ON COLUMN customer_master.name IS '漢字フルネーム';
COMMENT ON COLUMN customer_master.tel IS '固定電話、携帯電話のいずれか';
COMMENT ON COLUMN customer_master.address IS '都道府県から番地まで';
COMMENT ON COLUMN customer_master.delete_flag IS '画面には表示させないが、DBには値を残しておくためのフラグ';
COMMENT ON COLUMN customer_master.create_date IS '作成日';
COMMENT ON COLUMN customer_master.update_date IS '更新日';
COMMENT ON COLUMN order_data.id IS 'ID';
COMMENT ON COLUMN order_data.status IS '注文情報作成と注文出荷済みの２種類';
COMMENT ON COLUMN order_data.customer_id IS '顧客のID';
COMMENT ON COLUMN order_data.delete_flag IS '画面には表示させないが、DBには値を残しておくためのフラグ';
COMMENT ON COLUMN order_data.create_date IS '作成日';
COMMENT ON COLUMN order_data.update_date IS '更新日';
COMMENT ON COLUMN order_detail.id IS 'ID';
COMMENT ON COLUMN order_detail.order_number IS '顧客が注文した商品の個数';
COMMENT ON COLUMN order_detail.product_id IS '商品ID';
COMMENT ON COLUMN order_detail.order_id IS '注文ID';
COMMENT ON COLUMN order_detail.delete_flag IS '画面には表示させないが、DBには値を残しておくためのフラグ';
COMMENT ON COLUMN order_detail.create_date IS '作成日';
COMMENT ON COLUMN order_detail.update_date IS '更新日';
COMMENT ON COLUMN product_master.id IS 'ID';
COMMENT ON COLUMN product_master.product_code IS '商品のコード';
COMMENT ON COLUMN product_master.jancode IS '商品のJANコード';
COMMENT ON COLUMN product_master.delete_flag IS '画面には表示させないが、DBには値を残しておくためのフラグ';
COMMENT ON COLUMN product_master.create_date IS '作成日';
COMMENT ON COLUMN product_master.update_date IS '更新日';



