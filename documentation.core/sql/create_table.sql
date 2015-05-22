CREATE TABLE documentation (
	page_id varchar(255) NOT NULL,
	content_type varchar(100) NOT NULL default 'text/html',
	title varchar(512) default '',
	PRIMARY KEY (page_id)
)

