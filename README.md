# Validator-lite-spring-boot
![version](https://img.shields.io/badge/release-1.3.0-blue) [![maven central](https://img.shields.io/badge/maven%20central-1.3.0-brightgreen)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis) [![license](https://img.shields.io/badge/license-Apache%202.0-blue)](http://www.apache.org/licenses/LICENSE-2.0.html)

Validator-lite关于springboot的整合组件，在springboot开发环境中，你只需要在maven中配置插件库的引用，在springboot插件自动装配机制的帮助下，可以帮我们省去一部分默认配置，即使不做任何配置也可以使用插件的相关功能。

关于validator-lite如何使用，这里不再做过多的说明，请参考以下关联文档。



## 关联文档

关于纯java环境，请移步到：https://github.com/tangxbai/validator-lite

关于整合spring，请移步到：https://github.com/tangxbai/validator-lite-spring



## 快速开始

导入springboot-starter启动组件，配合配置参数即可正常使用插件Api。

```xml
<dependency>
    <groupId>com.viiyue.plugins</groupId>
    <artifactId>validator-lite-spring-boot-starter</artifactId>
    <version>[VERSION]</version>
</dependency>
```

如何获取最新版本？[点击这里获取最新版本](https://search.maven.org/search?q=g:com.viiyue.plugins%20AND%20a:validator-lite-spring-boot-starter&core=gav)



## 基础配置

```properties
# 可以通过以下配置更改默认语言环境
# 如果这里没有显式设置，插件将会通过Spring获取一个合适的语言环境作为当前语言。
validator.setting.default-language = zh-TW

# 是否开启严谨模式，默认即严谨模式，更改为false转换为宽松模式。
validator.setting.enable-strict-mode = true

# 是否开启单校验模式，开启之后当有一个验证对象被驳回，其他的验证对象将不再继续校验，默认全部校验。
validator.setting.enable-single-mode = false

# 是否打印警告日志，插件会提示一些不影响运行的日志，但是可以纠正的信息，默认开启。
validator.setting.enable-warning-log = true
```

以上各配置项的样例值均为默认值，如果想要更改默认值的话，请帖上您的条例配置，如果您想继续使用这些默认值的话，可以省略这些配置直接使用。

这里贴的是properties的配置方式，各位可以自行转换成 `yml` 的配置形式。



## 使用方式

关于如何使用，这里就不再赘述了，你可以 [点击这里查看更详细的文档](https://github.com/tangxbai/validator-lite-spring#如何使用)。



## 关于作者

- 邮箱：tangxbai@hotmail.com
- 掘金： https://juejin.im/user/5da5621ce51d4524f007f35f
- 简书： https://www.jianshu.com/u/e62f4302c51f
- Issuse：https://github.com/tangxbai/validator-lite-spring-boot/issues
