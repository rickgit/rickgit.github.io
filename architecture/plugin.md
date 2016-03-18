
##更新机制
- 插件线程->pipe（handler->broadcast）->DesktopUI ->flag
广播通知桌面ui，设置flag，每次onResume判断下