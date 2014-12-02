CREATE DATABASE challenges;

sqlite:
CREATE TABLE `location` (
  `addr` char(4) NOT NULL,
  `time` text NOT NULL,
  `distanceR` integer DEFAULT NULL,
  `centerX` integer DEFAULT NULL,
  `field1` text DEFAULT NULL
);

mysql:
CREATE TABLE `location` (
  `addr` char(4) NOT NULL,
  `time` varchar(32) DEFAULT NULL,
  `speed` float(4,1) DEFAULT 0,
  `center_offset` int DEFAULT 0,
  `triger_passed_no` int DEFAULT 0
);

// To generate the DAO classes(windows use ';') - for sqlite
java -classpath lib/jooq-3.4.2.jar;lib/jooq-meta-3.4.2.jar;lib/jooq-codegen-3.4.2.jar;lib/sqlite-jdbc-3.7.2.jar;. org.jooq.util.GenerationTool /challenges.xml

// for mysql
java -classpath lib/jooq-3.4.2.jar;lib/jooq-meta-3.4.2.jar;lib/jooq-codegen-3.4.2.jar;lib/mysql-connector-java-5.1.6.jar;. org.jooq.util.GenerationTool /challenges.xml