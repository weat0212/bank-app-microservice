CREATE TABLE IF NOT EXISTS　`gateway_log`(
	id VARCHAR(255),
	log_time TIMESTAMP,
	message VARCHAR(1022),
	msg_body VARCHAR(255),
	PRIMARY KEY(id)
)