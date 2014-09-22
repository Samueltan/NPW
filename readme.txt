CREATE DATABASE challenges;

CREATE TABLE `temperature` (
  `addr` char(4) NOT NULL,
  `time` integer NOT NULL,
  `temperature` real DEFAULT NULL,
  PRIMARY KEY (`addr`)
);

// To generate the DAO classes:
java -classpath lib/jooq-3.4.2.jar:lib/jooq-meta-3.4.2.jar:lib/jooq-codegen-3.4.2.jar:lib/sqlite-jdbc-3.7.2.jar:. org.jooq.util.GenerationTool /challenges.xml
