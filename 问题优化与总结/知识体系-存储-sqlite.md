## 数据库
### 历史
2000年8月，SQLite 1.0版，用gdbm作为存储管理器（backend dbbc.c）。
不久就用自己实现的能支持事务和记录按主键存储的B-tree替换了gdbm
2004年，SQLite从版本2升级到版本3，这是一次重大升级。这次升级的主要目标是增强国际化，支持UTF-8、UTF-16及用户定义字符集。
### 基础
B-Tree 5条特性：

1)每个节点最多有 m 个子节点
2)除根节点和叶子节点，其它每个节点至少有 [m/2] （注释：[]代表向上取整 ）个子节点
3)若根节点不是叶子节点，则其至少有 2 个子节点
4)所有NULL节点到根节点的高度都一样（向上分裂保证）
5)除根节点外，其它节点都包含 n 个key和 n+1 个指针，其中 [m/2] -1 <= n <= m-1 （[]代表向上取整 ）；（key超过上限，中间节点分裂到父节点，两边节点分裂）

2. B+ 区别
   1) 所有的非叶子节点都可以看作是key的索引部分
   2) m叉B+最多含m个key,BTree最多含n-1个key
   3) B+的叶子节点保存所有key的信息，依key大小顺序排列
   
## Sqlite Database Structure

```

+---------------------+  +-------------------+
|[core]               |  |                   |
|                 +--------->  Tokenizer     |
|                 |   |  |                   |
|   Interface     |   |  |                   |
|                 |   |  |                   |
|                 |   |  |     Parser        |
|   SQL Command+--+   |  |                   |
|   Processor         |  |                   |
|              <-----------+                 |
|                     |  | |   Code          |
|                     |  | +-+ Generator     |
|   Virtual Machine   |  |                   |
|                     |  |                   |
+----------+----------+  +-------------------+
           |
           |
+---------------------+  +-------------------+
|[backend] v          |  | [Accessories]     |
|                     |  |                   |
|     B-tree          |  |    Utilities      |
|                     |  |                   |
|          +          |  |                   |
|          v          |  |    Test Code      |
|     Pager           |  |                   |
|                     |  |                   |
|          +          |  |                   |
|          v          |  |                   |
|     OS Interface    |  |                   |
+---------------------+  +-------------------+

```

## 数据类型
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
+-------------------------------------------------------------------------------------------+
  000001_initial   ce0da46e617d7164641849fbec8970af4bce859c initial empty check-in
  000002_checkin   75897234bea58127dd00d961ebfe823dd8a253fb initial check-in of the new version (CVS 1)

    +-----------------------------------------------------------------+----------------------------+
    | sqlite                     [vdbe.c]//virtual mechine            | Parse                      |
    |   pBe;Dbbe*                Vdbe                                 |  db;sqlite *               |
    |   flags;int                   pBe;  Dbbe *      iStack;  int*   |  xCallback;sqlite_callback |//回调数据
    |   apTblHash[N_HASH];Table *   trace;FILE *      zStack;char **  |  pArg;   void *            |
    |   apIdxHash[N_HASH];Index *   nOp;     int      azColName;char**|  zErrMsg;char *            |
    |                               nOpAlloc;int      nTable;int      |  sErrToken;  Token         |
    |                               aOp;Op *          aTab;VdbeTable* |  sFirstToken;Token         |
    |                               nLabel;int        nList;int       |  sLastToken; Token         |
    |                               nLabelAlloc;int   apList;FILE **  |  pNewTable; Table*         |
    |                               aLabel; int*      nSort; int      |  pVdbe;Vdbe *              |
    |                               tos;     int      apSort;Sorter** |  explain; int              |
    |                               nStackAlloc;int                   |  initFlag;int              |
    |                                                                 |  nErr;    int              |
    +-----------------------------------------------------------------+                            |
    | [dbbe.c]//gdbm                                                  |                            |
    |    Dbbe                 Table               Index               |                            |
    |     zDir;char *           zName;char *       zName;char *       |                            |
    |     write;int             pHash;Table *      pHash;Index *      |                            |
    |     pOpen;BeFile *        nCol;int           nField;int         | parse.y   Token            |
    |     nTemp;int             readOnly;int       aiField;int *      |            z; char *       |
    |     apTemp;FILE **        azCol;char **      pTable;Table *     |            n; int          |
    |                           pIndex;Index *     pNext;Index *      |                            |
    |                                                                 | sqliteExec()               |
    |   BeFile                                                        |                            |
    |    zName; char *                                                |                            |
    |    dbf;GDBM_FILE                                                |                            |
    |    nRef;int                                                     |                            |
    |    pNext,pPrev;BeFile *                                         |                            |
    |                                                                 |                            |
    +-----------------------------------------------------------------+----------------------------+


  000003_parse.y   348784efc0f9486e991bc4461fa8d4e389a90358 :-) (CVS 6)
  000004_aggregate cce7d1761422e206689062685c79664e469a57ed added aggregate functions like count(*) (CVS 21)
  000005_distinct  efb7251d6ec9879ab5d7eec271e42fce26012b11 added DISTINCT on select (CVS 27)
  000006_operators 4794b980350f0ebd97a81c0dca24ff509376f56e added IN and BETWEEN operators (CVS 57)
  000007_drivers   bb0b679c02a1d92c331984ce924ecddc1694c928 Break out DBBE drivers into separate files. (CVS 157)
  000008_db        41a2b48bd0190da9e00dacc02982cd831e673d38 :-) (CVS 178)
  000008_pager     ed7c855cddfae61cd88398d19a683ae5e4bf4228 :-) (CVS 208)
  000009_btree     a059ad070bb2e953aabe933ae687de896077c569 Begin adding BTree code (CVS 213)
* 000009_btree_replace 5e00f6c7d57c7c651a098fbe3aed1b11813ef85f The code is in place to replace GDBM with BTree.  But I have not yet attempted to compile it.  I am sure the code contains bugs. (CVS 238)
      +-----------------------------------------------------------------+----------------------------+
      | sqlite                     [vdbe.c]//virtual mechine            | Parse                      |
      |   pBe;Btree *              Vdbe                                 |  db;sqlite *               |
      |   flags;int                   pBe;  Dbbe *      iStack;  int*   |  xCallback;sqlite_callback |
      |   apTblHash[N_HASH];Table *   trace;FILE *      zStack;char **  |  pArg;   void *            |
      |   apIdxHash[N_HASH];Index *   nOp;     int      azColName;char**|  zErrMsg;char *            |
      |                               nOpAlloc;int      nTable;int      |  sErrToken;  Token         |
      |                               aOp;Op *          aTab;VdbeTable* |  sFirstToken;Token         |
      |                               nLabel;int        nList;int       |  sLastToken; Token         |
      |                               nLabelAlloc;int   apList;FILE **  |  pNewTable; Table*         |
      |                               aLabel; int*      nSort; int      |  pVdbe;Vdbe *              |
      |                               tos;     int      apSort;Sorter** |  explain; int              |
      |                               nStackAlloc;int                   |  initFlag;int              |
      |                                                                 |  nErr;    int              |
      +-----------------------------------------------------------------+                            |
      | [btree.c]//btree driber                                         |                            |
      |   Btree                 Table               Index               |                            |
      |     pPager; Pager *       zName;char *       zName;char *       |                            |
      |     pCursor;BtCursor *    pHash;Table *      pHash;Index *      |                            |
      |     page1;  PageOne *     nCol;int           nField;int         | parse.y   Token            |
      |     inTrans;int           readOnly;int       aiField;int *      |            z; char *       |
      |                           azCol;char **      pTable;Table *     |            n; int          |
      |                           pIndex;Index *     pNext;Index *      |                            |
      |                                                                 | sqliteExec()               | 
      |  Pager                         BtCursor                         |                            |
      |   zFilename; char*                 pBt;Btree *                  |                            |
      |   zJournal;  char*                 pNext,pPrev;BtCursor *       |                            |
      |   fd, jfd;  int                    pgnoRoot;Pgno                |                            |
      |   dbSize;   int                    pPage; MemPage *             |                            |
      |   origDbSize; int                  idx;int                      |                            |
      |   nExtra;     int                  bSkipNext;u8                 |                            |
      |  (*xDestructor)(void*) void        iMatch;u8                    |                            |
      |   nPage;  int                                                   |                            |
      |   nRef;   int                                                   |                            |
      |   mxPage; int                                                   |                            |
      |   nHit, nMiss, nOvfl;int                                        |                            |
      |   state;   unsigned char                                        |                            |
      |   errMask; unsigned char                                        |                            |
      |   tempFile;unsigned char                                        |                            |
      |   readOnly;unsigned char                                        |                            |
      |   *aInJournal;unsigned char                                     |                            |
      |   pFirst,pLast;PgHdr *                                          |                            |
      |   pAll;PgHdr *                                                  |                            |
      |   aHash[N_PG_HASH];PgHdr *                                      |                            |
      |                                                                 |                            |
      |                                                                 |                            |
      | PgHdr                       MemPage                             |                            |
      |   pPager;Pager *               union {                          |                            |
      |   pgno;Pgno                      char aDisk[SQLITE_PAGE_SIZE];  |                            |
      |   pNextHash,pPrevHash;PgHdr*     PageHdr hdr;                   |                            |
      |   nRef;int                     } u;                             |                            |
      |   pNextFree,pPrevFree;PgHdr*   isInit;int                       |                            |
      |   pNextAll,  pPrevAll;PgHdr*   pParent;MemPage *                |                            |
      |   inJournal;char               nFree;     int                   |                            |
      |   dirty;    char               nCell;     int                   |                            |
      |                                isOverfull;int                   |                            |
      |                                apCell[MX_CELL+2];Cell*          |                            |
      +-----------------------------------------------------------------+----------------------------+
      |  read write lseek fcntl    ftruncate   fsync                    |                            |
      |                                                                 |                            |
      +-----------------------------------------------------------------+----------------------------+


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