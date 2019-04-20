## 数据库

 ```
+-------------------------------------------------+----------+-------------------------------+----------+-------------+-------------------------------+
|                                                 |          |                               |          |             |                               |
|                                                 |          | GROUP                         |          |             |                               |
|                                                 |          | GLOB     PRAGMA               |          |   TEXT      |  round                        |
|                                                 |          | EXPLAIN  ANALYZE              |          |             |  rtrim              length    |
|                                                 |          | VACUUM   RELEASE  REINDEX     |          |   NUMERIC   |  sqlite_version     lower     |
|                                                 |          |                               |          |             |  strftime           ltrim     |
|                                                 |          | COMMIT  SAVEPOINT  ROLLBACK   |          |   INTEGER   |  substr             max       |
|                                                 |          | BEGIN   COMMIT                |          |             |  sum                min       |
|                                                 |          |                               | Affinity |   REAL      |  time               now       |
|                                                 |          | INSERT  SELECT   UPDATE ALTER | Type     |             |  trim               nullif    |
|                                                 |          | HAVING  IN  Like NOT  BETWEEN |          |   NONE      |  upper              random    |
|                                                 |          | DELETE  WHERE    DISTINCT     |          |             |                     replace   |
|  .timeout .width   .timer     .stats .tables    |          | AND/OR  COUNT    EXISTS       |          |             |  last_insert_rowid  count     |
|  .schema  .separator          .show             |          | ORDER                         |          |             |                     date      |
|  .output  .print   .prompt    .quit  .read      | /* */    |                               |          |             |  abs                datetime  |
|  .indices .load    .log       .mode  .nullvalue |          | CREATE  TABLE/Index/TRIGGER   +----------+-------------+  avg                ifnull    |
|  .backup  .bail    .databases .dump  .echo      |          | /DROP   VIEW                  |                        |  coalesce           instr     |
|  .exit    .explain .header(s) .help  .import    |          |                               |  NULL INTEGER REAL     |  concatenate with   julianday |
|                                                 |          | ATTACH  DETACH                |  TEXT BLOB             |                               |
+-----------------------------------------------------------------------------------------------------------------------------------------------------+
|   commands                                      | Comments |       Statement               |   Data Type            |      Functions                |
+-------------------------------------------------+----------+-------------------------------+------------------------+-------------------------------+
|                            Case Sensitivity                                                                                                         |
+-----------------------------------------------------------------------------------------------------------------------------------------------------+



 ```
 
 SELECT  rule_key  FROM BlockFilter WHERE rule_key='asdfa';
/*
CREATE INDEX index_block_key ON BlockFilter(rule_key);
CREATE INDEX index_white_key ON WhiteFilter(rule_key);

DROP INDEX index_rule_key;


SELECT Substr(rule_key,3,2) FROM BlockFilter GROUP BY Substr(rule_key,4,2);

SELECT length(rule_key) FROM BlockFilter GROUP BY length(rule_key);
*/