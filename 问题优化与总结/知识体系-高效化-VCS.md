
# 版本控制系统(VCS)

## 中央版本库来进行集中控制(CCVS) svn (Apache Subversion) 
[](https://subversion.apache.org/docs/)
[存储原理](https://www.cnblogs.com/jiangzhaowei/p/5554489.html)

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
 
[](https://git-scm.com/download/gui/windows)


Git与SVN区别:
1. Git文件持久化为工作区，暂存区，贮藏区，仓库，远程仓库；SVN只有本地工作区拷贝和远程仓库；
2. Git是分布式的，操作几乎不需要联网，SVN是CS模式；
4. Git是整个仓库拷贝，SVN是相当于工作区拷贝
5. Git分支工作流 branch(master,dev,feature,release,hotfix),tag提交记录；SVN branch(分支) tag(标记)都是trunk(主线) 的拷贝
3. Git每次修订是sha1;SVN是用全局版本号；
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
 
### 代理设置
```js


Winodws系统的做法：打开CMD，输入ipconfig /flushdns 
Linux的做法：在终端输入sudo /etc/init.d/networking restart 


如需取消代理配置,输入 
git config --global --unset http.proxy

git branch initial_revision <commit id>
 //显示分支

git checkout initial_revision  
git ls-files --stage //显示stage文件
git add -i //显示stage文件
git show-ref


 git checkout -b <new branch name> <tag_name>

```

[git stash 贮藏](https://www.cnblogs.com/zndxall/archive/2018/09/04/9586088.html)

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

### ⭐ Git 仓库仓用命令
```shell
1.工作区
git status //（红色文件）管理本地修改状态
git add //添加到index/暂存区
git clean -f//删除的是未跟踪的文件，-d 文件夹 ；-x .gitignore文件也可以删除；
git restore <file>... //丢弃修改
git mv //重命名

//工作区差异
git diff //对比工作区和stage文件的差异
git ls-files --u //冲突文件

2.暂存区
git status // （绿色文件）查看暂存区中差异信息
git checkout -- <file> //恢复索引区修改到工作区
git commit //添加到仓库

//工作区差异
git diff --cached //对比stage和branch之间的差异
git ls-files --stage //object文件
git cat-file //object对象
3 仓库记录管理
git log
git show

3.1 仓库记录的文件恢复
git reset -- <file>                     //恢复仓库修改到索引区，索引区unstage到工作区
git restore --staged <file>             //恢复仓库修改到索引区
git restore <file>                      //恢复索引区修改改到工作区
git restore --staged --worktree <file> //恢复仓库修改到工作区
git checkout -- HEAD <file> //恢复仓库修改到工作区

3.2 仓库记录的恢复
git reverse      // 未提交需要提交，错误记录提交撤销修改，新创建记录提交
git reset --soft // 未提交不删除不修改，提交记录重置的放到index 区，提交记录删除 
git reset --mix  // 未提交不删除并移到工作区，提交记录重置的放到工作区，提交记录删除 
git reset --hard // 未提交删除，要撤销的记录文件删除，提交记录删除 
git checkout     // 未提交需要提交，不删除已经commit 记录
git checkout  -f // 未提交删除，不删除已经commit 记录
git switch (2.23)// 未提交删除，不删除已经commit 记录，不能切换commit id

3.3 仓库记录搜索
git log --grep='' --author="anshu.wang"
git log -- <filepath>    //某个文件的修改
git bisect

git log --reverse
git log --reverse --tags --simplify-by-decoration --pretty="format:%ai %d" 
git log --all --reverse --grep='搜索关键字'

3.4 仓库记录操作
git rebase -i hash



4.贮藏区
git stash list //管理贮藏
git stash show //显示stash区的文件

git stash save <message>//添加到stash贮藏区
git stash pop //取出最近贮藏区修改
git stash apply stash@{message} //取出最近贮藏区修改，并删除记录
git stash drop stash@{message} //管理删除

5. 协同/工作流
git init //初始化仓库
git clone //初始化仓库&checkout
git remote -v
git push //添加记录到远程仓库
git fetch //拉取记录到本地分支
git pull //fetch &merge 拉取记录到本地仓库并工作区

5.1 记录分支管理
git branch  //git branch -v  --abbrev=40 //打印详情，hash值长度40
git merge
git rebase
//显示tag
 git tag -n --sort=taggerdate
 git for-each-ref --sort=taggerdate --format '%(refname) %(taggerdate)' refs/tags
git push origin --delete develop_AA //删除远程分支


6 配置

git config --global http.proxy 'socks://127.0.0.1:8580'
git config --global --unset http.proxy


配置git上代理地址 
git config --global http.proxy "127.0.0.1:8580"
git config --global http.proxy "127.0.0.1:9666"
```js
git config --system
D:\Program\Git/etc/gitconfig

git config --global 
C:\Users\Administrator\.gitconfig

git config
G:\workspace\ws-github\.git\config
```




[反向ip查询，临时提交](https://site.ip138.com/github.com/)
### 常见问题
```shell
Please move or remove them before you can switch branches.
# git clean  -d  -fx ""
git reset --hard HASH
git reset --soft HASH


# git checkout -f -b <branch>    
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
#### 删除submodule
```cmd

# 逆初始化模块，其中{MOD_NAME}为模块目录，执行后可发现模块目录被清空
git submodule deinit {MOD_NAME} 
# 删除.gitmodules中记录的模块信息（--cached选项清除.git/modules中的缓存）
git rm --cached {MOD_NAME} 
# 提交更改到代码库，可观察到'.gitmodules'内容发生变更
git commit -am "Remove a submodule." 

.gitmodules 删除子模块

```
#### 仓库太大
 

```
RPC failed; curl 18 transfer closed with outstanding read data remaining
为项目太久，tag资源文件太大

git config --global http.postBuffer 524288000 　　　　# 2GB
git config --global http.postBuffer 2097152000　　      # 2GB
git config --global http.postBuffer 3194304000 　　    # 3GB

git config --global http.lowSpeedLimit 0
git config --global http.lowSpeedTime 999999


git clone /github_com/large-repository --depth 1
cd large-repository
git fetch --unshallow
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
                          +-------------------------------------------------------------------------------------+
                          |                                 git.sh                                              |
                          |                                                                                     |
                          +-------------------------------------------------------------------------------------+
                          |  add                        update-index.c                                          |
                          |  apply                                                                              |
                          |  archimport                                                                         |
                          |  bisect                     git-bisect.sh                                           |
                          |  branch                     /refs/heads/$branchname                                 |
                          |  checkout                   read-tree.c   checkout-index.c    update-index.c        |
                          |  cherry                                                                             |
                          |  clone                      curl                                                    |
                          |  commit                     diff-files.c  update-index.c   update-ref.c             |
                          |  count-objects                                                                      |
                          |  cvsimport                                                                          |
                          |  diff                                                                               |
                          |  fetch                                                                              |
                          |  format-patch                                                                       |
                          |  fsck-objects                                                                       |
                          |  get-tar-commit-id                                                                  |
                          |  init-db                                                                            |
                          |  log                         rev_list.c                                             |
                          |  ls-remote                                                                          |
                          |  octopus                                                                            |
                          |  pack-objects                                                                       |
                          |  parse-remote                                                                       |
                          |  patch-id                                                                           |
                          |  prune                                                                              |
                          |  pull                                                                               |
                          |  push                        http-push.c                                            |
                          |  rebase                                                                             |
                          |  relink                                                                             |
                          |  rename                                                                             |
                          |  repack                                                                             |
                          |  request-pull                                                                       |
                          |  reset                                                                              |
                          |  resolve                                                                            |
                          |  revert                                                                             |
                          |  send-email                                                                         |
                          |  shortlog                                                                           |
                          |  show-branch                                                                        |
                          |  status                                                                             |
                          |  tag                                                                                |
                          |  verify-tag                  cat-file.c                                             |
                          |  whatchanged                                                                        |
                          +-------------------------------------------------------------------------------------+

  060216_git-svn           3397f9df53092871de2c33c143f1f4413654c40d Introducing contrib/git-svn.
  060422_buildin           70827b15bfb11f7aea52c6995956be9d149233e1 Split up builtin commands into separate files from git.c
  070304_git-revert        9509af686bffdbd7c3f17faf3c07d2034d480ffc Make git-revert & git-cherry-pick a builtin
  070630_git-stash         f2c66ed196d1d1410d014e4ee3e2b585936101f5 Add git-stash script
  070911_reset             0e5a7faa3a903cf7a0a66c81e20a76b91f17faab Make "git reset" a builtin.
  080228_branch            a0a80f1e8ac367634314ee8e2ca159c527da3a24 Merge branch 'git-p4' of git://repo.or.cz/git/git-p4
  101_git                  8e49d50388211a0f3e7286f6ee600bf7736f4814 C implementation of the 'git' program, take two.               
* 200_v2.0.0-rc0           cc291953df19aa4a97bee3590e708dc1fc557500 Git 2.0-rc0                                                    
                          +-------------------------------------------------------------------------------------+-----------+
                          |                                 git.c                                               |           |
                          |                                   handle_builtin()                                  |           |
                          |                                                                                     |           |
                          +-------------------------------------------------------------------------------------+-----------+
                          | builtin                                                                             |           |
                          +-------------------------------+-----------------------------------------------------+-----------+
                          |  	{ "add", cmd_add, RUN_SETUP | NEED_WORK_TREE },                                   |           |
                          |  	{ "annotate", cmd_annotate, RUN_SETUP },                                          |           |
                          |  	{ "apply", cmd_apply, RUN_SETUP_GENTLY },                                         |           |
                          |  	{ "archive", cmd_archive },                                                       |           |
                          |  	{ "bisect--helper", cmd_bisect__helper, RUN_SETUP },                              |           |
                          |  	{ "blame", cmd_blame, RUN_SETUP },                                                |           |
                          |  	{ "branch", cmd_branch, RUN_SETUP },                                              |           |
                          |  	{ "bundle", cmd_bundle, RUN_SETUP_GENTLY },                                       |           |
                          |  	{ "cat-file", cmd_cat_file, RUN_SETUP },                                          |           |
                          |  	{ "check-attr", cmd_check_attr, RUN_SETUP },                                      |           |
                          |  	{ "check|ignore", cmd_check_ignore, RUN_SETUP | NEED_WORK_TREE },                 |           |
                          |  	{ "check|mailmap", cmd_check_mailmap, RUN_SETUP },                                |           |
                          |  	{ "check-ref-format", cmd_check_ref_format },                                     |           |
                          |  	{ "checkout", cmd_checkout, RUN_SETUP | NEED_WORK_TREE },                         |           |
                          |  	{ "checkout-index", cmd_checkout_index,	RUN_SETUP | NEED_WORK_TREE},              |           |
                          |  	                                                                                  |           |
                          |  	{ "cherry", cmd_cherry, RUN_SETUP },                                              |           |
                          |  	{ "cherry-pick", cmd_cherry_pick, RUN_SETUP | NEED_WORK_TREE },                   |           |
                          |  	{ "clean", cmd_clean, RUN_SETUP | NEED_WORK_TREE },                               |           |
                          |  	{ "clone", cmd_clone },                                                           |           |
                          |  	{ "column", cmd_column, RUN_SETUP_GENTLY },                                       |           |
                          |  	{ "commit", cmd_commit, RUN_SETUP | NEED_WORK_TREE },                             |           |
                          |  	{ "commit-tree", cmd_commit_tree, RUN_SETUP },                                    |           |
                          |  	{ "config", cmd_config, RUN_SETUP_GENTLY },                                       |           |
                          |  	{ "count-objects", cmd_count_objects, RUN_SETUP },                                |           |
                          |  	{ "credential", cmd_credential, RUN_SETUP_GENTLY },                               |           |
                          |  	{ "describe", cmd_describe, RUN_SETUP },                                          |           |
                          |  	{ "diff", cmd_diff },                                                             |           |
                          |  	{ "diff-files", cmd_diff_files, RUN_SETUP | NEED_WORK_TREE },                     |           |
                          |  	{ "diff|index", cmd_diff_index, RUN_SETUP },                                      |           |
                          |  	{ "diff|tree", cmd_diff_tree, RUN_SETUP },                                        |           |
                          |  	{ "fast-export", cmd_fast_export, RUN_SETUP },                                    |           |
                          |  	{ "fetch", cmd_fetch, RUN_SETUP },                                                |           |
                          |  	{ "fetch-pack", cmd_fetch_pack, RUN_SETUP },                                      |           |
                          |  	{ "fmt-merge-msg", cmd_fmt_merge_msg, RUN_SETUP },                                |           |
                          |  	{ "for-each-ref", cmd_for_each_ref, RUN_SETUP },                                  |           |
                          |  	{ "format-patch", cmd_format_patch, RUN_SETUP },                                  |           |
                          |  	{ "fsck", cmd_fsck, RUN_SETUP },                                                  |           |
                          |  	{ "fsck-objects", cmd_fsck, RUN_SETUP },                                          |           |
                          |  	{ "gc", cmd_gc, RUN_SETUP },                                                      |           |
                          |  	{ "get-tar-commit-id", cmd_get_tar_commit_id },                                   |           |
                          |  	{ "grep", cmd_grep, RUN_SETUP_GENTLY },                                           |           |
                          |  	{ "hash-object", cmd_hash_object },                                               |           |
                          |  	{ "help", cmd_help },                                                             | help.c    |
                          |  	{ "index-pack", cmd_index_pack, RUN_SETUP_GENTLY },                               |           |
                          |  	{ "init", cmd_init_db },                                                          |           |
                          |  	{ "init-db", cmd_init_db },                                                       |           |
                          |  	{ "log", cmd_log, RUN_SETUP },                                                    |           |
                          |  	{ "ls-files", cmd_ls_files, RUN_SETUP },                                          |           |
                          |  	{ "ls|remote", cmd_ls_remote, RUN_SETUP_GENTLY },                                 |           |
                          |  	{ "ls-tree", cmd_ls_tree, RUN_SETUP },                                            |           |
                          |  	{ "mailinfo", cmd_mailinfo },                                                     |           |
                          |  	{ "mailsplit", cmd_mailsplit },                                                   |           |
                          |  	{ "merge", cmd_merge, RUN_SETUP | NEED_WORK_TREE },                               |           |
                          |  	{ "merge-base", cmd_merge_base, RUN_SETUP },                                      |           |
                          |  	{ "merge|file", cmd_merge_file, RUN_SETUP_GENTLY },                               |           |
                          |  	{ "merge|index", cmd_merge_index, RUN_SETUP },                                    |           |
                          |  	{ "merge|ours", cmd_merge_ours, RUN_SETUP },                                      |           |
                          |  	{ "merge|recursi^e", cmd_merge_recursive, RUN_SETUP | NEED_WORK_TREE },           |           |
                          |  	{ "merge|recursive-ours", cmd_merge_recursive, RUN_SETUP | NEED_WORK_TREE },      |           |
                          |  	{ "merge|recursive-theirs", cmd_merge_recursive, RUN_SETUP | NEED_WORK_TREE },    |           |
                          |  	{ "merge|subtree", cmd_merge_recursive, RUN_SETUP | NEED_WORK_TREE },             |           |
                          |  	{ "merge-tree", cmd_merge_tree, RUN_SETUP },                                      |           |
                          |  	{ "mktag", cmd_mktag, RUN_SETUP },                                                |           |
                          |  	{ "mktree", cmd_mktree, RUN_SETUP },                                              |           |
                          |  	{ "mv", cmd_mv, RUN_SETUP | NEED_WORK_TREE },                                     |           |
                          |  	{ "name-rev", cmd_name_rev, RUN_SETUP },                                          |           |
                          |  	{ "notes", cmd_notes, RUN_SETUP },                                                |           |
                          |  	{ "pack-objects", cmd_pack_objects, RUN_SETUP },                                  |           |
                          |  	{ "pack|redundant", cmd_pack_redundant, RUN_SETUP },                              |           |
                          |  	{ "pack-refs", cmd_pack_refs, RUN_SETUP },                                        |           |
                          |  	{ "patch-id", cmd_patch_id },                                                     |           |
                          |  	{ "pickaxe", cmd_blame, RUN_SETUP },                                              |           |
                          |  	{ "prune", cmd_prune, RUN_SETUP },                                                |           |
                          |  	{ "prune-packed", cmd_prune_packed, RUN_SETUP },                                  |           |
                          |  	{ "push", cmd_push, RUN_SETUP },                                                  |           |
                          |  	{ "read-tree", cmd_read_tree, RUN_SETUP },                                        |           |
                          |  	{ "receive-pack", cmd_receive_pack },                                             |           |
                          |  	{ "reflog", cmd_reflog, RUN_SETUP },                                              |           |
                          |  	{ "remote", cmd_remote, RUN_SETUP },                                              |           |
                          |  	{ "remote-ext", cmd_remote_ext },                                                 |           |
                          |  	{ "remote-fd", cmd_remote_fd },                                                   |           |
                          |  	{ "repack", cmd_repack, RUN_SETUP },                                              |           |
                          |  	{ "replace", cmd_replace, RUN_SETUP },                                            |           |
                          |  	{ "rerere", cmd_rerere, RUN_SETUP },                                              |           |
                          |  	{ "reset", cmd_reset, RUN_SETUP },                                                |           |
                          |  	{ "rev-list", cmd_rev_list, RUN_SETUP },                                          |           |
                          |  	{ "re<-parse", cmd_rev_parse },                                                   |           |
                          |  	{ "revert", cmd_revert, RUN_SETUP | NEED_WORK_TREE },                             |           |
                          |  	{ "rm", cmd_rm, RUN_SETUP },                                                      |           |
                          |  	{ "send-pack", cmd_send_pack, RUN_SETUP },                                        |           |
                          |  	{ "shortlog", cmd_shortlog, RUN_SETUP_GENTLY | USE_PAGER },                       |           |
                          |  	{ "show", cmd_show, RUN_SETUP },                                                  |           |
                          |  	{ "show-branch", cmd_show_branch, RUN_SETUP },                                    |           |
                          |  	{ "show-ref", cmd_show_ref, RUN_SETUP },                                          |           |
                          |  	{ "stage", cmd_add, RUN_SETUP | NEED_WORK_TREE },                                 |           |
                          |  	{ "status", cmd_status, RUN_SETUP | NEED_WORK_TREE },                             |           |
                          |  	{ "stripspace", cmd_stripspace },                                                 |           |
                          |  	{ "symbolic-ref", cmd_symbolic_ref, RUN_SETUP },                                  |           |
                          |  	{ "tag", cmd_tag, RUN_SETUP },                                                    |           |
                          |  	{ "unpack-file", cmd_unpack_file, RUN_SETUP },                                    |           |
                          |  	{ "unpack|objects", cmd_unpack_objects, RUN_SETUP },                              |           |
                          |  	{ "update|index", cmd_update_index, RUN_SETUP },                                  |           |
                          |  	{ "update|ref", cmd_update_ref, RUN_SETUP },                                      |           |
                          |  	{ "update|server-info", cmd_update_server_info, RUN_SETUP },                      |           |
                          |  	{ "upload|archi^e", cmd_upload_archive },                                         |           |
                          |  	{ "upload-archive--writer", cmd_upload_archive_writer },                          |           |
                          |  	{ "^ar", cmd_var, RUN_SETUP_GENTLY },                                             |           |
                          |  	{ "verify-pack", cmd_verify_pack },                                               |           |
                          |  	{ "verify-tag", cmd_verify_tag, RUN_SETUP },                                      |           |
                          |  	{ "version", cmd_version },                                                       |           |
                          |  	{ "whatchanged", cmd_whatchanged, RUN_SETUP },                                    |           |
                          |  	{ "write-tree", cmd_write_tree, RUN_SETUP },                                      |           |
                          |                                                                                     |           |
                          |                                                                                     |           |
                          +-------------------------------------------------------------------------------------+-----------+


```

### cUrl
curl --range 0-19999999 -o fran.pdf.part1 http://dl227.zlibcdn.com/dtoken/e
curl --range 20000000-39999999 -o fran.pdf.part2 http://dl227.zlibcdn.com/dtoken/e
curl --range 40000000-59999999 -o fran.pdf.part3 http://dl227.zlibcdn.com/dtoken/e
curl --range 60000000-79999999 -o fran.pdf.part4 http://dl227.zlibcdn.com/dtoken/e
curl --range 80000000-99999999 -o fran.pdf.part5 http://dl227.zlibcdn.com/dtoken/e
curl --range 100000000- -o fran.pdf.part6 http://dl227.zlibcdn.com/dtoken/e


curl --range 0-19999999 -O d.pdf.part1 http://booksdl.org/get.php?md5=ec0c1093fcb13280afd9bc37201e7133&key=D4S4ELJSFZUQ285C&mirr=1
curl --range 20000000-39999999 -o f.pdf.part2 http://dl49.zlibcdn.com/dtoken/b21c1157dc3d146845aa2779778ed1b6
curl --range 40000000-59999999 -o f.pdf.part3 http://dl49.zlibcdn.com/dtoken/b21c1157dc3d146845aa2779778ed1b6
curl --range 60000000-79999999 -o f.pdf.part4 http://dl49.zlibcdn.com/dtoken/b21c1157dc3d146845aa2779778ed1b6
curl --range 80000000-99999999 -o f.pdf.part5 http://dl49.zlibcdn.com/dtoken/b21c1157dc3d146845aa2779778ed1b6
curl --range 100000000- -o f.pdf.part6 http://dl49.zlibcdn.com/dtoken/b21c1157dc3d146845aa2779778ed1b6

cat f.pdf.part? > f.pdf

```
            +-----------------------------------------------------------------------------------+
            |                              main.c                                               |
            |                                                                                   |
            +-----------------------------------------------------------------------------------+
            |                                                                                   |
            |                         url.c                                                     |
            |                            curl_urlget()                                          |
            |                                                                                   |
            |                         http.c                                                    |
            |                             http()                                                |
            |                                                                                   |
            |                                                                                   |
            |                         sendf.c                                                   |
            |                             sendf()                                               |
            |                                                                                   |
            +-----------------------------------------------------------------------------------+

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


孤立分支
#git checkout --orphan app_flutter
#git rm --cached -r .
#git clone -b app_flutter http://git/test.git

```


##  Repo
Repo是谷歌用Python脚本写的调用git的一个脚本，可以实现管理多个git库。
+--------------------------------------------------+
|                                                  |
|     repo         Manifest     AOSP project       |
|                                                  |
|                     repo                         |
+--------------------------------------------------+

Gerrit,一种免费、开放源代码的代码审查软件,使用网页界面。
### 合并两个不同仓库
```
MainRepo(https://git1),DecorationRepo(https://git2)

cd MainRepo
git remote add DecorationRepo  https://git2
git fetch DecorationRepo
git checkout -b branch_decorate DecorationRepo/master
git checkout master
git merge branch_decorate --allow-unrelated-histories

 
git -c diff.mnemonicprefix=false -c core.quotepath=false --no-optional-locks checkout 4d1e6f058e0de5c5895668722d3c378888c8a3d9 --progress
git -c diff.mnemonicprefix=false -c core.quotepath=false --no-optional-locks merge 0ff1b31fc1ed6bf477e0e7a4d5301be0e7b1ed23 --allow-unrelated-histories
```
### [git删除本地所有的更改 ](https://www.cnblogs.com/ryanzheng/p/8573155.html?_t=1561774191)
```
git checkout -f
 
git reset --hard 

git clean -xdf 

```

## Git 删除仓库中的提交纪录
0. 最近一条
   git commit –amend
1. 最近几条
  git reset --soft
```  
1. 切换到主干
git checkout master
git reset --soft <hashcode>
git commit -m ""

3.git push --force 

```
2. 删除中间记录 rebase（衍合）
- Interactive Rebase；删除历史记录里是最近提交，而后面还有很多需要保留的提交
    git rebase -i  [startpoint]  [endpoint]//不指定endpoint，默认是当前分支的Head，前开后闭区间（最近的记录取得到，最后的记录取不到），可以采用上一个版本是HEAD^，上上一个版本就是HEAD^^，往上100个版本写成HEAD~100。
  git rebase -i HEAD~3 打开编辑器，将最近两个记录合并到最近第三个记录，把需要删除的记录由pick改为squash
```
# p, pick = use commit
# r, reword = use commit, but edit the commit message
# e, edit = use commit, but stop for amending
# s, squash = use commit, but meld into previous commit
# f, fixup = like "squash", but discard this commit's log message
# x, exec = run command (the rest of the line) using shell
# d, drop = remove commit
```
- Onto Rebase：删除的历史记录是连续的（中间一个或连续多个的提交用 rebase（衍合），不连续的使用 cherry-pick **）。前开后闭区间（最近的记录取得到，最后的记录取不到）
    git rebase    --onto   [newbase]   [from] [to] //只是单纯复制其他分支记录到branchName分支，原来的分支记录还在。


 
- Orphan Branch；删除的历史记录很多，要保留的则很少
- git replace ；历史记录分成两份（或更多份）

### Filename too long error
git config --system core.longpaths true


### code review checklist 
[code review checklist](https://mp.weixin.qq.com/s?__biz=MzAwMDU1MTE1OQ==&mid=2653552455&idx=1&sn=8bbc7df1040d5d46b5d793e471e38265&chksm=813a6edfb64de7c9e4f19503b474ba8a7517af2e8cc4ceb061912ee2f36918d384dfc13c28df&scene=21#wechat_redirect)

这些代码更改是否符合业务需求？

这些代码更改是否符合团队的编码质量要求？

这些代码更改是否有相关的测试？

这些代码更改是否有截图或用户界面更改？

是否为这些代码更改更新了变更日志？

是否为这些代码更改更新了应用文档？

## Android版本管理

 

 +-----------------------------------------------------------------------------------------+
|  [python]                          repo                                                 |
|                                      init                                               |
|                                      sync                                               |
|                                                                                         |
+-----------------------------------------------------------------------------------------+


##  chromium 版本管理depot_tools(gclient)



[获取depot_tools](https://source.codeaurora.cn/quic/lc/chromium/tools/depot_tools)

```depot_tools         
        +----------------------------------------------------------------------------------------------------------------------+
        |  DEPS                                                                                                                |
        |.gclient                                                                                                              |
        |                                                                                                                      |
        |                                                                                                                      |
        |cleanup                                                                                                               |
        |update                                                                                                                |
        |revert                                                                                                                |
        |status                                                                                                                |
        |diff                                                                                                                  |
        |runhooks                                                                                                              |
        |                                                                                                                      |
        |GClient                                                                                                               | 
        +----------------------------------------------------------------------------------------------------------------------+
        |                                                                                                                      |
        |gclient              gcl/                                                                                             |
        |(meta-checkout tool)/ git-cl  svn  drover  cpplint.py  pylint presubmit_support.py repo wtf weekly  git-gs zsh-goodies|
        |fetch                                                                                                                 |
        | (gclient wrapper)                                                                                                    |
        +----------------------------------------------------------------------------------------------------------------------+
        |                                              depot_tools                                                             |
        +----------------------------------------------------------------------------------------------------------------------+




        +--------------------------------------------------------------------------------------------------------------------+
        |                  fetch.py                                                                                          |
        |                    run_config_fetch(): json,json                                                                   |
        |                    run()                                                                                           |
        |                                                                                                                    |
        |        CHECKOUT_TYPE_MAP = {                            GclientGitCheckout                  GitCheckout(Checkout)  |
        |            'gclient':         GclientCheckout,                :GclientCheckout, GitCheckout      run_git()         |
        |            'gclient_git':     GclientGitCheckout,          init()                                                  |
        |            'git':             GitCheckout,                                                                         |
        |        }                                                                                                           |
        |                                                         GclientCheckout(Checkout)                                  |
        |                                                              run_gclient()                                         |
        |                                                                                                                    |
        |                                                         Checkout                                                   |
        |                                                             run()                                                  |
        |                                                                                                                    |
        +-------------------------------------------------------+------------------------------------------------------------+
        |       fetch_configs.android.py :config_util.Config    |   GClient(GitDependency)       git_common.py               |
        |                fetch_spec():json                      |      //config                                              |
        |                                                       |      //sync                         run()                  |
        |                                                       |                                     run_with_stderr()      |
        |                                                       |                                                            |
        |       fetch_configs.Chromium.py :config_util.Config   |  GitDependency(Dependency)          //submodule  config    |
        |               fetch_spec():json                       |     run()                                                  |
        |                                                       |                                                            |
        |                                                       |                                                            |
        +-------------------------------------------------------+------------------------------------------------------------+
          

```

### fetch 是获取源码

### gclient 提供 git 多个仓库（.DEPS）的批量操作。

gclient config git://source.codeaurora.cn/quic/lc 生成 **.gclient** 文件
gclient sync  执行 **.DEPS** 依赖