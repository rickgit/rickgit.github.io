
# 版本控制系统(VCS)

## 中央版本库来进行集中控制(CCVS) svn (Apache Subversion) 
[](https://subversion.apache.org/docs/)

```
Repository

trunk 
tags
branch

Working copy: Working copy is a snapshot of the repository.
Commit changes: Commit is a process of storing changes from private workplace to central server. 
```

```
                              create()
+-------+-------+trunk()+---------------+       checkout()            +--------—--------+
|    trunk      +-<---+ |    Repository |   +---------------------->  |   clone repo     |
+-----+---------+       +---------------+   +------------+            +--------+--------+
      |                                                  |                     |
      |                     +    ^                       | update()            v  edit file
      |                     |    |                       |            +--------+---------+
      |                     |    |                       |            |   modify file    |
      |                     |    |                       +--------->  |
      |                     |    |                                    +--------+---------+
      |                     |    |                                             |
      |                     |    |                                             v status()/diff()
      |                     |    |                                     +-------+---------+
      |                     |    |                                     |  review change  |
      |                     |    |                                     |                 |
      |                     |    |                                     +--------+--------+
      |                     |    |                                              |  commit  and push
      |                     |    |                                              v
      |                     |    |          commit()                   +--------+--------+
      |                     |    +-------------------------------------+    commit change| <----------+
      |                     |                                          +--------+--------+            |
      |                     |                                                   |                     |
      |                     |                                                   |  amend and push     |
      |                     |                revert()                   +-------v---------+           |
      |                     |                                           |                 |           |
      |                     +-----------------------------------------> |  Fix mistakes   |           |
      |                                                                 |                 |           |
      |                                                                 +--------+--------+           |
      |                                      merge()                             v merge              |
      |                                                                 +--------+--------+           |
      +---------------------------------------------------------------> |Resolve Conflicts|           |
                                                                        +--------+--------+           |
                                                                                 | resolve()          |
                                                                                 +---------------------

```
标准目录
```
project
  |
  +-- trunk
  +     |
  +     +----- main.cpp  (3.0版本的最新文件）
  +     +----- common.h
  +
  +-- branches
  +     |
  +     +-- r1.0
  +     +     |
  +     +     +---- main.cpp （1.x版本的最新文件）
  +     +     +---- common.h 
  +     
  +-- tags   (此目录只读)
        |
        +-- r1.0
        +     |
        +     +---- main.cpp （1.0版本的发布文件）
        +     +---- common.h
```
## 分布式的VCS (DVCS) -git

https://git-scm.com/book/en/v2/Git-Internals-Plumbing-and-Porcelain

```
+---------+---------+-------------------------+---------------------+------+------+
|         |         |         original        |                     |      |      |
|         |         |                         |                     |      |      |
|         |         | heads   remotes    tags |  pack  info sha-1...| refs |      |
+---------------------------------------------------------------------------------+
| configs |     HEAD|           refs          |      objects        | logs | index|
+---------+-----------------------------------+---------------------+------+------+
|                                 .git Directory                                  |
+---------------------------------------------------------------------------------+


```
03. Git三大特色之Branch(分支)
04. Git三大特色之Stage(暂存区)
05. Git三大特色之WorkFlow(工作流)
[git工作流](https://www.jianshu.com/p/8200d4d90d77)

分支管理模型
- Git-Flow
- Github Pull Request
- Gitblit Ticket

git merge squash/rebase 


git config --global http.postbuffer 524288000 
 

```js

freegete
git config --global http.proxy 'socks://127.0.0.1:8580'
git config --global --unset http.proxy


配置git上代理地址 
git config --global http.proxy "127.0.0.1:8580"
git config --global http.proxy "127.0.0.1:9666"

Winodws系统的做法：打开CMD，输入ipconfig /flushdns 
Linux的做法：在终端输入sudo /etc/init.d/networking restart 


如需取消代理配置,输入 
git config --global --unset http.proxy

git log --reverse
git log --reverse --tags --simplify-by-decoration --pretty="format:%ai %d" 

git branch initial_revision <commit id>
 //显示分支
git branch -v  --abbrev=40 //打印详情，hash值长度40

git checkout initial_revision  

git show-ref

//显示tag
 git tag -n --sort=taggerdate
 git for-each-ref --sort=taggerdate --format '%(refname) %(taggerdate)' refs/tags



 git checkout -b <new branch name> <tag_name>

```

[git stash 贮藏](https://www.cnblogs.com/zndxall/archive/2018/09/04/9586088.html)
[git stage 暂存]()

[git reset 回滚]()
[git revert 撤销]()
某次操作，此次操作之前和之后的commit和history都会保留，并且把这次撤销
[git rebase ]()

***Model***
```
                 HEAD
                  |
                  v
                branch
remote            +              tag
   +              v               +
   |         +----+-----+ <-------+
   +-------> |  commit  | <--+
             +----+-----+    |
                  |   +------+
                  v
             +----+-----+  <------+
             |   tree   |         |
             +--+-+--+--+ +-------+
                | |  |
                v v  v
              +-+-+--++
              |  blob |
              +-------+



```

[Git内部原理/源码](https://zhuanlan.zhihu.com/p/71577255)
```
 +--------------------------------------------+--------------+---------------------+---------------------+
 |                                            |              |                     |                     |
 |                                            |              |      diff           |                     |
 +--------------------------------------------+              +---------------------+                     |
 |                                            |              |  name  sha1         |                     |
 |                                            |              +---------------------+                     |
 |                                            |   cache_head |cache_entry          |                     |
 |                                            +------------------------------------+                     |
 |                                            |          cache.h                   |                     |
+---------+----------------+-------------------------------------------------------+                     |
 |        |  read   commit |                  |          sha1 algorith             |                     |
 +---------------------------------------------------------------------------------+                     |
 |   BLOB |     TREE       |CHANGESET   TRUST |       cache (.dircache/index)      |                     |
 |        |                | (commit)         |       current directory cache      |                     |
 +--------+----------------+-----------------------------------------------------------------------------+
 |       object database (.dircache/objects)  |       current directory cache      | working directory   |
 +--------------------------------------------+----------------------------------------------------------+
 |                               Git                                               |                     |
 +---------------------------------------------------------------------------------+---------------------+


object : <ascii tag without space> + <space> + <ascii decimal size> + <byte\0> + <binary data>

tree: <mode> + <space> + <file name> + <byte\0> + <sha1>

----------e83c5163316f89bfbde7d9ab23ca2e25604af290
init-db         # 初始化 object database
update-cache    # 将工作区文件写入暂存区，对应现在的 git add
show-diff       # 比较工作区和暂存区的差异，对应现在的 git diff
write-tree      # 将暂存区的文件列表写入 object database，也就是生成 tree 对象
read-tree       # 把 objce database 的一个 tree 对象加载到暂存区，对应现在的 git checkout
commit-tree     # 提交 commit，对应现在的 git commit
cat-file        # 查看 object 内容，对应现在的 git cat-file
-----------20222118ae4cbd0a7ba91a7012574cd2f91101ec
fsck-cache      # 验证数据库中对象的连通性和有效性
-----------8d3af1d53255ac36494492720ebb83e932b0c0bc
checkout-cache  # 从暂存区读取文件内容到工作区，对应现在的 git checkout

-------------9174026cfe69d73ef80b27890615f8b2ef5c265a
diff-tree       # 比较两个 tree 对象的差异

------------84fe972055398ba0790ac0a8f159c79c83efcef4
rev-tree        # 按反向时间顺序列出提交对象

show-files      # 查看暂存区文件列表，对应现在的 git status

check-files     # Check that a set of files are up-to-date in the filesystem or do not exist. Used to verify a patch target before doing a patch.

merge-base      # 查找两个 commit 的最新共公 commit

ls-tree         # 查看 tree 对象内容，对应现在的 git ls-tree
merge-cache     # 合并


  revision_001_blob           e497ea2 Make read-tree actually unpack the whole tree.
  revision_002_fsck-cache     7660a18 Add new fsck-cache to Makefile.
  revision_003_checkout-cache 33db5f4 Add a "checkout-cache" command which does what the name suggests.
  revision_004_diff-tree      9174026 Add "diff-tree" program to show which files have changed between two trees.
  revision_005_re-tree        84fe972 Add a "rev-tree" helper, which calculates the revision tree graph.
  revision_006_show-files     8695c8b Add "show-files" command to show the list of managed (or non-managed) files.
  revision_007_check-files    74b46e3 Add a "check-files" command, which is useful for scripting patches.
  revision_008_merge-tree     33deb63 Add "merge-tree" helper program. Maybe it's retarded, maybe it's helpful.
  revision_009_merge-base     6683463 Do a very simple "merge-base" that finds the most recent common parent of two commits.
* revision_010_merge-cache    75118b1 Ass a "merge-cache" helper program to execute a merge on any unmerged files.

```

```shell

git show-ref命令查看 
.git/refs/heads不存在HEAD指向的文件，这个时候可以用

```
[修改用户名](https://www.cnblogs.com/codeking100/p/10324579.html)

```
git filter-branch -f --env-filter "GIT_AUTHOR_NAME='Newname'; GIT_AUTHOR_EMAIL='newemail'; GIT_COMMITTER_NAME='Newname'; GIT_COMMITTER_EMAIL='newemail';" HEAD

git push --force

```

### Git 分支
```shell
# git branch -v  --abbrev=40 //打印详情，hash值长度40

```
##  Repo
Repo是谷歌用Python脚本写的调用git的一个脚本，可以实现管理多个git库。

Gerrit,一种免费、开放源代码的代码审查软件,使用网页界面。

[git删除本地所有的更改 ](https://www.cnblogs.com/ryanzheng/p/8573155.html?_t=1561774191)
```
git checkout -f
 
git reset --hard 

git clean -xdf 

```

