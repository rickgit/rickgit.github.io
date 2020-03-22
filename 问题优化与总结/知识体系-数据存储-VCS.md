
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




### svn源码
1. 下载编译及checkout命令
```shell
# svn co https://svn.apache.org/repos/asf/subversion/trunk subversion
```
2. 查看网站的user guild 运行入口及release note的主要版本
[quick-start](https://subversion.apache.org/quick-start)
[release-notes](https://subversion.apache.org/docs/release-notes/)
3. 查看Initial版本，README 目录结构，找入口




## 分布式的VCS (DVCS) -Git

Git与SVN区别:
1. Git是分布式的，操作几乎不需要联网，SVN是CS模式；
2. Git是整个仓库拷贝，SVN是相当于工作区拷贝
3. Git分支工作流 branch(master,dev,feature,release,hotfix),tag；SVN branch(分支) tag(标记)都是trunk(主线) 的拷贝
4. Git文件持久化为工作区，暂存区，贮藏区，仓库，远程仓库；SVN只有本地工作区拷贝和远程仓库；
5. Git每次修订是sha1;SVN是用全局版本号；
6. Git文件是元数据方式存储（snapshot），而SVN是按文件

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

 git log --all --reverse --grep='搜索关键字'

git branch initial_revision <commit id>
 //显示分支
git branch -v  --abbrev=40 //打印详情，hash值长度40

git checkout initial_revision  
git ls-files --stage //显示stage文件
git add -i //显示stage文件
git show-ref

//显示tag
 git tag -n --sort=taggerdate
 git for-each-ref --sort=taggerdate --format '%(refname) %(taggerdate)' refs/tags



 git checkout -b <new branch name> <tag_name>

```

[git stash 贮藏](https://www.cnblogs.com/zndxall/archive/2018/09/04/9586088.html)
[git stage 暂存]()
git list-file --stage //查看stage的文件
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
### 常见问题
```shell
Please move or remove them before you can switch branches.
# git clean  -d  -fx ""


```
[常见GUI](https://git-scm.com/downloads/guis)
git-cola Gitg

```shell
# git log --all --decorate --oneline --graph

```

gitk
```shell
#gitk -d --all --after="2018-05-21 00:00:00"  --before="2008-05-25 23:59:59" &
#gitk --follow --all -p filename //文件修改记录
```

```shell
# git config --global url.https://source.codeaurora.org.insteadOf git://codeaurora.org
```

### [Git内部原理/源码](https://zhuanlan.zhihu.com/p/71577255)
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
   

* 000001_revision       e83c5163316f89bfbde7d9ab23ca2e25604af290 Initial revision of "git", the information manager from hell
                    [如何评价 2017 2 月 23 日谷歌宣布实现了 SHA-1 碰撞？](https://www.zhihu.com/question/56234281)
                    +------------------------------------------------------+
                    |      cat-file.c                                      |
                    |                                                      |
                    |      write-tree.c   read-tree.c    commit-tree.c     |
                    |                                                      |
                    |      update-cache.c show-diff.c                      |
                    +------------------------------------------------------+
                    |                                                      |
                    |              init-db.c read-cache.c                  |
                    +------------------------------------------------------+
                    |             sha1collisiondetection   diff    tree    |
                    +------------------------------------------------------+
                    init-db         # 初始化 object database
                    update-cache    # 将工作区文件写入暂存区，对应现在的 git add
                    show-diff       # 比较工作区和暂存区的差异，对应现在的 git diff
                    commit-tree     # 提交 commit，对应现在的 git commit
                    write-tree      # 将暂存区的文件列表写入 object database，也就是生成 tree 对象（有点像 git merge，新建个DAG修订点）
                    read-tree       # 把 objce database 的一个 tree 对象加载到暂存区，（有点像 git checkout 到内存对象buf）
                    cat-file        # 查看 object 内容，对应现在的 git cat-file
  000002_fsck-cache     20222118ae4cbd0a7ba91a7012574cd2f91101ec Add first cut at "fsck-cache" that validates the SHA1 object store.
                    # 验证数据库中对象的连通性和有效性

  000003_checkout-cache 33db5f4d9027a10e477ccf054b2c1ab94f74c85a Add a "checkout-cache" command which does what the name suggests.
                   # 从暂存区读取文件内容到工作区，对应现在的 git checkout

  000004_diff-tree      9174026cfe69d73ef80b27890615f8b2ef5c265a Add "diff-tree" program to show which files have changed between two trees.
                    # 比较两个 tree 对象的差异

  000005_re-tree        84fe972055398ba0790ac0a8f159c79c83efcef4 Add a "rev-tree" helper, which calculates the revision tree graph.
                    rev-tree        # 按反向时间顺序列出提交对象
                    show-files      # 查看暂存区文件列表，对应现在的 git status
                    check-files     # Check that a set of files are up-to-date in the filesystem or do not exist. Used to verify a patch target before doing a patch.
                    merge-base      # 查找两个 commit 的最新共公 commit
                    ls-tree         # 查看 tree 对象内容，对应现在的 git ls-tree
                    merge-cache     # 合并
  000006_show-files     8695c8bfe181677ca112502c1eef67930d84a75d Add "show-files" command to show the list of managed (or non-managed) files.
  000007_check-files    74b46e32cb3907a4a062a0f11de5773054b7c71a Add a "check-files" command, which is useful for scripting patches.
  000008_ls-tree        7912c07037cf704394e9bcb7cb24c05ee03aa921 [PATCH] ls-tree for listing trees
  000009_merge-tree     33deb63a36f523c513cf29598d9c05fe78a23cac Add "merge-tree" helper program. Maybe it's retarded, maybe it's helpful.
  000010_merge-base     6683463ed6b2da9eed309c305806f9393d1ae728 Do a very simple "merge-base" that finds the most recent common parent of two commits.
  000011_merge-cache    75118b13bc8187c629886b108929f996c47daf01 Ass a "merge-cache" helper program to execute a merge on any unmerged files.
  000012_sample_script     839a7a06f35bf8cd563a41d6db97f453ab108129 Add the simple scripts I used to do a merge with content conflicts.
  000013_splite_read-cache 0fcfd160b0495c0881e142c546c4418b8cea7e93 Split up read-cache.c into more logical clumps.
  000014_unpack-file       3407bb4940c25ca67c846a48ef2c2c60c02178e0 Add "unpack-file" helper that unpacks a sha1 blob into a tmpfile.
  000015_git-export        c9823a427a0a7aabc4f840a90e82548e91f9bdd6 Add stupid "git export" thing, which can export a git archive as a set of patches and commentary.
  000016_diff-cache        e74f8f6aa7807d479d78bfc680a18a9a5198b172 Add "diff-cache" helper program to compare a tree (or commit) with the current cache state and/or 000working directory.
  000017_convert-cache     d98b46f8d9a3daf965a39f8c0089c1401e0081ee Do SHA1 hash _before_ compression.
  000018_sha1_lib          cef661fc799a3a13ffdea4a3f69f1acd295de53d Add support for alternate SHA1 library implementations.
  000019_sha1_ppc          a6ef3518f9ac8a1c46a36c8d27173b1f73d839c4 [PATCH] PPC assembly implementation of SHA1
  050424_var_transport     6eb7ed5403b7d57d5ed7e30d0cd0b312888ee95c [PATCH] Various transport programs
  000021_rev-list          64745109c41a5c4a66b9e3df6bca2fd4abf60d48 Add "rev-list" program that uses the new time-based commit listing.
  000022_git-mktag         ec4465adb38d21966acdc9510ff15c0fe4539468 Add "tag" objects that can be used to sign other objects.
  000023_tag               2636f6143751a064e366cb7763d0705b296726e3 [PATCH] Add tag header/parser to library
  000024_pull              4250a5e5b1755e45153248217fe1d5550c972c8d [PATCH] Split out "pull" from particular methods
  000025_splite_document   2cf565c53c88c557eedd7e5629437b3c6fe74329 [PATCH 1/4] split core-git.txt and update
  000026_splite_pull_fetch 7ef76925d9c19ef74874e1735e2436e56d0c4897 Split up git-pull-script into separate "fetch" and "merge" phases.
  000027_commit_helper     a3e870f2e2bcacc80d5b81d7b77c15a7928a9082 Add "commit" helper script
  050206_git               e764b8e8b3c50b131be825532ba26fa346d6586e Add "git" and "git-log-script" helper scripts.
  000029_refs              95fc75129acf14d980bdd56b9b2ee74190f81d91 [PATCH] Operations on refs
  000030_cvs2git           d4f8b390a4326625f0c3d65a8d120336e38928d7 Add CVS import scripts and programs
* 050614_git-rev-parse  178cb243387a24b1dec7613c4c5e97158163ac60 Add 'git-rev-parse' helper script
  000031_git-add           40d8cfe4117564e5520e8f4f953addaa94844476 Trivial git script fixups
                          +-------------------------------------------------------------------------------------+
                          |                                git-add-script                                       |
                          |                                                                                     |
                          +-------------------------------------------------------------------------------------+
                          |                           git-update-cache --add "$@"                               |
                          |                                                                                     |
                          +-------------------------------------------------------------------------------------+

  000032_git-checkout      303e5f4c325d008c68e5e70e901ab68b289ade2e Add "git checkout" that does what the name suggests
                          +-------------------------------------------------------------------------------------+
                          |                                git-checkout-script                                  |
                          |                                                                                     |
                          +-------------------------------------------------------------------------------------+
                          |    git-rev-parse                               git-read-tree                        |
                          |                                                                                     |
                          |                                                git-checkout-cache                   |
                          +-------------------------------------------------------------------------------------+

  000033_gitk              5569bf9bbedd63a00780fc5c110e0cfab3aa97b9 Do a cross-project merge of Paul Mackerras' gitk visualizer
  050223_git-clone         3f571e0b3a7893ed068acd75f27e152d29945637 Add "git-clone-script" thingy
                          +-------------------------------------------------------------------------------------+
                          |                                git-clone                                            |
                          |                                                                                     |
                          +-------------------------------------------------------------------------------------+
                          |        git-init-db        git-fetch-script                git-rev -parse            |
                          |                                                                                     |
                          |                           wget   git-http-pull                                      |
                          |                                                                                     |
                          |                                    http-pull.c            rev -parse.c              |
                          |                                                                                     |
                          +-------------------------------------------------------------------------------------+

  050626_git-rebase        59e6b23acef9d29b9bdabc38ee80361e19ef7ebe [PATCH] git-rebase-script: rebase local commits to new upstream head.
  000036_git-push          51cb06c36de67007f3464d864f63d93213fcaf86 Add "git-push-script" to make a more regular interface
  000037_git-branch        37f1a519f2ea0ce912ccd7c623aea992147c3900 Add "git branch" script
                          +-------------------------------------------------------------------------------------+
                          |                                git-branch-script                                    |
                          |                                                                                     |
                          +-------------------------------------------------------------------------------------+
                          |       git-sh-setup-script                   git-rev-parse --verify --default HEAD   |
                          |                                                                                     |
                          +-------------------------------------------------------------------------------------+

  000038_git-tools         98e031f0bb6e857c684e6db24d03d22cfc1a532a Merge git-tools repository under "tools" subdirectory
  000039_git-ls-remote     0fec0822721cc18d6a62ab78da1ebf87914d4921 [PATCH] git-ls-remote: show and optionally store remote refs.
  000040_update-server     8f3f9b09dc5ac8a946422422c3c70a4a4c284be3 [PATCH] Add update-server-info.
  000041_git-revert        045f82cbee3135a3d75256828b0cf101eedf38c8 git-revert: revert an existing commit.
  000042_ssh_pull          f71a69ab055c47056d0270b29b8f7455278c2422 Be more backward compatible with git-ssh-{push,pull}.
  000100_v1.0rc1           f7a2eb735982e921ae4379f1dcf5f7a023610393 GIT 0.99.9h
  060216_git-svn           3397f9df53092871de2c33c143f1f4413654c40d Introducing contrib/git-svn.
  060422_buildin           70827b15bfb11f7aea52c6995956be9d149233e1 Split up builtin commands into separate files from git.c
  070304_git-revert        9509af686bffdbd7c3f17faf3c07d2034d480ffc Make git-revert & git-cherry-pick a builtin
  070630_git-stash         f2c66ed196d1d1410d014e4ee3e2b585936101f5 Add git-stash script
  070911_reset             0e5a7faa3a903cf7a0a66c81e20a76b91f17faab Make "git reset" a builtin.
  080228_branch            a0a80f1e8ac367634314ee8e2ca159c527da3a24 Merge branch 'git-p4' of git://repo.or.cz/git/git-p4
  000101_git               8e49d50388211a0f3e7286f6ee600bf7736f4814 C implementation of the 'git' program, take two.


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

```
git filter-branch --env-filter '
OLD_EMAIL="anshu.wang.work@gmail.com"
CORRECT_NAME="rickgit"
CORRECT_EMAIL="anshu.wang.work@gmail.com"
if [ "$GIT_COMMITTER_EMAIL" = "$OLD_EMAIL" ]
then
    export GIT_COMMITTER_NAME="$CORRECT_NAME"
    export GIT_COMMITTER_EMAIL="$CORRECT_EMAIL"
fi
if [ "$GIT_AUTHOR_EMAIL" = "$OLD_EMAIL" ]
then
    export GIT_AUTHOR_NAME="$CORRECT_NAME"
    export GIT_AUTHOR_EMAIL="$CORRECT_EMAIL"
fi
' --tag-name-filter cat -- --branches --tags
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

