# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

这是一个多模块的企业级电商平台——"湘佳电商直营批发部"，包含：

- **后端**：Java/Spring Boot 多模块应用（javaboot框架）
- **前端**：UniApp 移动端商城应用（xj_shop）
- **版本控制**：SVN（非git）

## 构建命令

### 后端（Java/Maven）

```bash
# 构建所有模块
mvn clean package

# 构建指定模块
mvn clean package -pl javaboot-admin -am

# 运行应用（开发模式）
java -jar javaboot-admin/target/javaboot-admin.jar

# 或使用部署脚本
./javaboot.sh start    # 启动服务
./javaboot.sh stop     # 停止服务
./javaboot.sh restart  # 重启服务
./javaboot.sh status   # 查看状态
```

### 前端（UniApp/xj_shop）

在 HBuilderX IDE 中打开 `xj_shop` 目录：
- 浏览器运行（H5）：端口 8083
- 微信开发者工具运行，预览小程序效果
- 通过"发行"菜单打包 Android/iOS

## 后端架构

### 模块结构

| 模块 | 用途 |
|------|------|
| `javaboot-admin` | Web服务入口（后台管理界面，Thymeleaf模板） |
| `javaboot-app` | 移动端API入口（JWT认证） |
| `javaboot-framework` | 核心框架配置（Shiro、MyBatis、Web） |
| `javaboot-system` | 系统实体（用户、角色、菜单、部门、字典） |
| `javaboot-shop` | 商城业务域（商品、订单、购物车、仓库） |
| `javaboot-activiti` | 工作流引擎（Activiti 6.0） |
| `javaboot-quartz` | 定时任务管理 |
| `javaboot-generator` | 代码生成器（CRUD） |
| `javaboot-oss` | 文件存储（七牛、阿里OSS、腾讯COS） |
| `javaboot-websocket` | WebSocket消息服务 |
| `javaboot-wechat` | 微信集成（公众号、JSAPI） |
| `javaboot-spider` | 爬虫工具 |
| `javaboot-common` | 通用工具类 |

### 分层规范

每个模块遵循标准三层架构：
- `domain` - 实体类
- `mapper` - MyBatis映射器（接口 + XML在resources/mapper目录）
- `service` - 服务接口（`IXxxService`）和实现类（`impl/XxxServiceImpl`）
- Controller 在 `javaboot-admin/web/controller` 或 `javaboot-app/controller`

### 启动入口

- 后台管理：`com.javaboot.JavaBootApplication`（端口9999）
- 移动端API：`com.javaboot.JavaBootAppApplication`（在javaboot-app模块）

### 配置文件

- 主配置：`javaboot-admin/src/main/resources/application.yml`
- 环境配置：`application-dev.yml`、`application-test.yml`、`application-prod.yml`
- 当前激活环境：`dev`（通过 `spring.profiles.active` 切换）
- 数据库：MySQL（dev环境为 `xj_shop_prod`），Druid连接池

### 核心技术栈

- Java 8、Spring Boot 2.1.8、Apache Shiro 1.5.3
- MyBatis Plus 3.4.0、Druid 1.1.24
- Thymeleaf + Layui（后台UI）
- Swagger 2.9.2（API文档地址 `/swagger-ui.html`）
- Activiti 6.0（工作流）

## 前端架构（xj_shop）

### 目录结构

```
xj_shop/
├── pages/           # 页面文件（.vue）
│   ├── tabs/        # TabBar页面（首页、分类、购物车、订单、我的）
│   └── order/       # 订单相关页面
├── components/      # 自定义Vue组件
├── common/          # 工具函数（util.js、mock.js、md5.js）
├── static/          # 静态资源（图片）
├── store/           # Vuex状态管理
├── pages.json       # 页面路由配置
├── manifest.json    # 应用配置（appid、平台等）
└── uni.scss         # 全局SCSS变量
```

### 页面路由

定义在 `pages.json`：
- `/pages/tabs/index/init` - 入口/启动页
- `/pages/tabs/index/index` - 首页
- `/pages/tabs/type/type` - 分类页
- `/pages/tabs/cart/cart` - 购物车
- `/pages/tabs/order/order` - 订单列表
- `/pages/tabs/mine/mine` - 个人中心
- `/pages/login/login` - 登录页
- 订单流程：`orderdetail`、`orderpaymid`、`orderpayresult` 等

### API对接

前端调用后端API（通过 `javaboot-app` 模块），具体API工具函数见 `common/util.js`。

## 数据库

- SQL脚本位于 `sql/` 目录
- 主要数据库脚本：`sql/db_xjshop_new.sql`
- MyBatis映射文件：各模块 `src/main/resources/mapper/**/*.xml`

## 测试

测试覆盖较少，测试文件位于：
- `javaboot-wechat/src/test/java/` - 微信控制器测试

## 部署

JAR打包时分离lib和resources：
- `target/javaboot-admin.jar` - 主可执行文件
- `target/lib/` - 依赖库
- `target/resources/` - 配置文件

使用外部配置运行：
```bash
java -jar javaboot-admin.jar --spring.config.location=./resources/
```