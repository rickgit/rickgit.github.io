


- [Dagger2在android java中使用，实现简洁初始化](https://www.jianshu.com/p/f4384d5b5a82)
@Inject Activity中需要初始化Presenter，Build后，生成 MainActivity_MembersInjector
@Inject Presenter构造方法上，Build后，生成 MainPresenter_Factory
@Component Component关联 Activity和Presenter，Build后，生成 DaggerMainComponent

- [@Module和@Provides解决第三方库提供的，我们无法在第三方库的构造器上加上@Inject注解](https://www.jianshu.com/p/24af4c102f62)

```
1.有的时候有可能使用第三方的jar包。但是我们不可能在第三方jar包上写”@Inject” 
2.构造函数可能需要传入参数。
```
@Inject Activity中需要初始化Presenter，Build后，生成 MainActivity_MembersInjector
@Module 定义两个@Provides方法，经过Build后，生成 BusinessModule_ProvideBuyPresenterFactory，BusinessModule_ProvideSellPresenterFactory 两个类
@Component 注解中包含 @Module后，生成的类 DaggerBusinessComponent，包含方法 public Builder businessModule(BusinessModule businessModule)，

Activity必须增加 DaggerMain002Component.create().inject(this);，需要进一步看源码，能不能去掉

- @Scope和@SingleTon
  @SingleTon 属于@Scope一种，用在@Provides，则**整个@Component**也需要这个注解
@Module 定义两个@Provides方法，经过Build后，生成 BusinessModule_ProvideBuyPresenterFactory，BusinessModule_ProvideSellPresenterFactory 两个类
@Component 注解中包含 @Module后，生成的类 DaggerBusinessComponent，包含方法 public Builder businessModule(BusinessModule businessModule)，


[@Scope 使用场景](https://www.jianshu.com/p/3028f491006b)
@Scope 却保证，每个ActivityComponent只有一个FragmentComponent，本质还是个单例，靠用户控制Component，module组合和各自的组合继承，决定内存对象的数量