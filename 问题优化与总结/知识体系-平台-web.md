```

+-----------------------------------------------------------------+
|                                                                 |
|                 MySql                                           |
|                                                                 |
+-----------------------------------------------------------------+
|                                                                 |
|                 DAO(Hibernate)                                  |
|                                                                 |
+-----------------------------------------------------------------+
|                                                                 |
|                 Spring                                          |
|                                                                 |
+-----------------------------------------------------------------+
|                                                                 |
|                 SpringMVC                                       |
|                                                                ++
+-----------------------------------------------------------------+
|                                                                 |
|                 H5 (HTML/CSS/JS)                                |
|                                                                 |
+-----------------------------------------------------------------+

```

## hexo 博客搭建
### [hexo 博客搭建](https://hexo.io/zh-cn/)
```
npm install hexo-cli -g
hexo init blog
cd blog
npm install
hexo server
```

### Hexo-Next 主题
```
cd blog
git clone https://github.com/next-theme/hexo-theme-next

blog/_config.yml 配置文件的主题改为hexo-theme-next
theme: hexo-theme-next

刷新配置
hexo clean && hexo g && hexo d && hexo s

```
### [Hexo 部署到github](https://hexo.io/zh-cn/docs/github-pages)
```
npm install hexo-deployer-git --save

```
### 构建工具
rsbuild  （小型使用vite）
### 包管理器
pnpm  （不推荐 npm/yarn）
```
设置环境变量
PNPM_HOME C:\Users\account\AppData\Local\pnpm

初始化项目
pnpm install
```

