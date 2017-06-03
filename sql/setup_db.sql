CREATE DATABASE kohonen;

CREATE USER 'webapp'@'localhost' IDENTIFIED BY 'webpass';

GRANT ALL PRIVILEGES ON kohonen.* TO 'webapp'@'localhost';