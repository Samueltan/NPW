// Create the db and tables in sqlite (using sqlite command line)
CREATE DATABASE challenges;

CREATE TABLE `location` (
  `addr` char(4) NOT NULL,
  `time` text NOT NULL,
  `r1` real DEFAULT NULL,
  `r2` real DEFAULT NULL,
  `r3` real DEFAULT NULL
);

// To generate the DAO classes (Mac):
java -classpath lib/jooq-3.4.2.jar:lib/jooq-meta-3.4.2.jar:lib/jooq-codegen-3.4.2.jar:lib/sqlite-jdbc-3.7.2.jar:. org.jooq.util.GenerationTool /challenges.xml

// To generate the DAO classes (Windows):
java -classpath lib/jooq-3.4.2.jar;lib/jooq-meta-3.4.2.jar;lib/jooq-codegen-3.4.2.jar;lib/sqlite-jdbc-3.7.2.jar;. org.jooq.util.GenerationTool /challenges.xml