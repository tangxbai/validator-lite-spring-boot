# validator-lite-spring-boot-autoconfigure

validator-lite关于springboot的自动装配组件，在springboot中是比较核心的一个部件，无法单独使用，需要通过validator-lite-spring-boot-starter来导入。



## 核心逻辑

- ValidationAutoConfiguration - 核心自动装配类，用于自动载入validator-lite插件功能；
- ValidatorStartedListener - 用于容器启动完成之后对验证规则进行一个预编译操作；
- ValidatorProperties- 关于springboot的插件配置属性；
- META-INF/spring.factories - 自动装配类的列表清单；



## 关于作者

- 邮箱：tangxbai@hotmail.com
- 掘金： https://juejin.im/user/5da5621ce51d4524f007f35f
- 简书： https://www.jianshu.com/u/e62f4302c51f
- Issuse：https://github.com/tangxbai/validator-lite-spring-boot/issues
