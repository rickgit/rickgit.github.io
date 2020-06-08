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


```CPP

+-------------------------------------------------------------------------------------------------+
|                           shell.c                                                               |
|                              main()                                                             |
|       main.c                                               struct Parse {                       |
|         sqlite_open():sqlite                                 sqlite *db;                        |
|         sqliteDbbeOpen():Dbbe                                sqlite_callback xCallback;         |
|         sqliteOpenCb():int                                   void *pArg;                        |
|                                                              char *zErrMsg;                     |
|                                                              Token sErrToken;                   |
|                                                              Token sFirstToken;                 |
|  struct sqlite {                  struct Dbbe {              Token sLastToken;                  |
|    Dbbe *pBe;                       char *zDir;              Table *pNewTable;                  |
|    int flags;                       int write;               Vdbe *pVdbe;                       |
|    Table *apTblHash[N_HASH];        BeFile *pOpen;           int explain;                       |
|    Index *apIdxHash[N_HASH];        int nTemp;               int initFlag;                      |
|  };                                 FILE **apTemp;           int nErr;                          |
|                                   };                       };                                   |
+-------------------------------------------------------------------------------------------------+

```


### 源码
[](http://huili.github.io/index.html)


```
+-------------------------------------------------------------------------------------------+
|                                                                                           |
|                                                                                           |
|                                                                                           |
|                                                                                           |
|                shell.c                                                                    |
|                    sqlite3_prepare(*zSql:const char,**ppStmt:sqlite3_stmt )//sql2bytecode |
|                                                                                           |
|                                                                                           |
|                                                                                           |
|                sqlite3_stmt//contain bytecode                                             |
|                                                                                           |
|                                                                                           |
|                sqlite3_step(pSelect);//vdb exec bytecode                                  |
|                                                                                           |
|                                                                                           |
|                                                                                           |
|                                                                                           |
|                                                                                           |
|                                                                                           |
|                                                                                           |
|                                                                                           |
|                                                                                           |
|                                                                                           |
+-------------------------------------------------------------------------------------------+
  000001_initial   ce0da46e617d7164641849fbec8970af4bce859c initial empty check-in
  000002_checkin   75897234bea58127dd00d961ebfe823dd8a253fb initial check-in of the new version (CVS 1)
  000003_parse.y   348784efc0f9486e991bc4461fa8d4e389a90358 :-) (CVS 6)
  000004_aggregate cce7d1761422e206689062685c79664e469a57ed added aggregate functions like count(*) (CVS 21)
  000005_distinct  efb7251d6ec9879ab5d7eec271e42fce26012b11 added DISTINCT on select (CVS 27)
  000006_operators 4794b980350f0ebd97a81c0dca24ff509376f56e added IN and BETWEEN operators (CVS 57)
  000007_drivers   bb0b679c02a1d92c331984ce924ecddc1694c928 Break out DBBE drivers into separate files. (CVS 157)
  000008_db        41a2b48bd0190da9e00dacc02982cd831e673d38 :-) (CVS 178)
  000008_pager     ed7c855cddfae61cd88398d19a683ae5e4bf4228 :-) (CVS 208)
  000009_btree     a059ad070bb2e953aabe933ae687de896077c569 Begin adding BTree code (CVS 213)
  000010_os        e3c413727bff5d17ed5c9fbfe35f29459c551f13 Add a new column in the SQLITE_MASTER table to record the root page number of primary key indices. (CVS 252)
  000200_2.0_alpha 254cba2429bdced144873fc834a3f911ddcc2f85 2.0-Alpha-2 release (CVS 258)
  000201_libtool   71eb93eaa321cca01abde40b46525051d0291ee0 Put in the new LIBTOOL build system. (CVS 271)
* 000303_3.0.3     ad19857f5a8e842d9508e98a89bfacd92bb8ad43 Version 3.0.3 (CVS 1860)



+-------------------------------------------------------+-------------------------------+
|    tclsqlite.c                    shell.c             |                               |
|         DbMain()                     main()           |                               |
|                                      main_init()      |                               |
|                                      open_db()        |                               |
|                                      do_meta_command()|                               |
|                                      sqlite3_exec()   |                               |
|                                                       |                               |
|                                                       |                               |
|                                  callback_data        |                               |
|                                      db:sqlite3       |        tokenize.c             |
|                                                       |           sqliteRunParser()   |
|                                                       |           sqliteParser()      |
|                   main.c                              |           sqliteGetToken()    |
|                      openDatabase(**ppDb：sqlite3)     |                               |
|                                                       |        parse.y                |
|                                                       |                               |
|                   vdbc.c                              |                               |
|                       sqlite3VdbeExec()               |  insert.c   select.c          |
|                                                       |                               |
|                                                       |                               |
|                                                       |                               |
+---------------------------------------------------------------------------------------+
|                   btree.c                             |                               |
|                     sqlite3BtreeOpen()                |         hash.c                |
|                     sqlite3BtreeInsert()              |            strHash()          |
|                     sqlite3BtreeCommit()              |            binHash()          |
|                                                       |            intHash()          |
|                                                       |            ptrHash()          |
|                   pager.c                             |                               |
|                      sqlite3pager_write()             |         utf.c                 |
|                      sqlite3pager_open()              |         random.c              |
|                                                       |                               |
|                   os.h                                |                               |
|                      sqlite3OsOpenReadWrite()         |                               |
|                                                       |                               |
+-------------------------------------------------------+-------------------------------+

```

[lemon](https://github.com/compiler-dept/lemon)