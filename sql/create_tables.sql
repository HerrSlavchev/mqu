CREATE TABLE network_configuration (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	cols INT NOT NULL,
	rows INT NOT NULL,
	radius INT NOT NULL,
	learning_rate NUMERIC(4,3) NOT NULL
);

CREATE TABLE document (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	path VARCHAR(255) NOT NULL,
	features VARCHAR(511) NOT NULL
);

CREATE TABLE node (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	fk_network_configuration INT NOT NULL,
	row INT NOT NULL,
	col INT NOT NULL,
	features VARCHAR(511) NOT NULL,
	INDEX network_ind (fk_network_configuration),
	FOREIGN KEY (fk_network_configuration) REFERENCES network_configuration(id)	
);

CREATE TABLE document_node (
	fk_doc INT NOT NULL,
	fk_node INT NOT NULL,
	INDEX doc_ind (fk_doc),
	INDEX node_ind (fk_node),
	FOREIGN KEY (fk_doc) REFERENCES document(id),
	FOREIGN KEY (fk_node) REFERENCES node(id)
);