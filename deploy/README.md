# 湘佳电商系统部署说明

## 部署概述

本系统需要部署两个Java应用：
- `javaboot-admin.jar` - 后台管理系统（端口9999）
- `javaboot-app.jar` - 移动端API服务（端口8080）

## 部署步骤

### 1. 上传部署脚本到服务器

将 `deploy` 目录下的所有脚本上传到服务器：

```bash
# 创建目录
mkdir -p /opt/xj-shop

# 上传脚本文件（使用scp或ftp工具）
scp install.sh start.sh stop.sh status.sh restart.sh root@your_server:/opt/xj-shop/
```

### 2. 执行安装脚本

```bash
# 进入目录
cd /opt/xj-shop

# 添加执行权限
chmod +x *.sh

# 执行安装脚本（需要root权限）
./install.sh
```

安装脚本会：
- 检查并安装Java环境（如果未安装）
- 创建目录结构：`/opt/xj-shop/jars`、`/opt/xj-shop/logs`、`/opt/xj-shop/config`
- 创建配置文件模板

### 3. 上传JAR包

在本地项目根目录执行Maven打包：

```bash
mvn clean package
```

打包完成后，将以下JAR文件上传到服务器 `/opt/xj-shop/jars` 目录：
- `javaboot-admin/target/javaboot-admin.jar`
- `javaboot-app/target/javaboot-app.jar`

```bash
scp javaboot-admin/target/javaboot-admin.jar root@your_server:/opt/xj-shop/jars/
scp javaboot-app/target/javaboot-app.jar root@your_server:/opt/xj-shop/jars/
```

### 4. 修改配置文件

编辑配置文件，设置数据库连接等参数：

```bash
vi /opt/xj-shop/config/env.conf
```

配置项说明：
```bash
# 数据库配置
DB_HOST=localhost          # 数据库地址
DB_PORT=3306               # 数据库端口
DB_NAME=xj_shop_prod       # 数据库名称
DB_USER=root               # 数据库用户名
DB_PASS=your_password      # 数据库密码

# 服务端口
ADMIN_PORT=9999            # 后台管理系统端口
APP_PORT=8080              # 移动端API端口

# JVM参数
ADMIN_JVM_OPTS="-Xms512M -Xmx512M -XX:+UseG1GC"
APP_JVM_OPTS="-Xms256M -Xmx256M -XX:+UseG1GC"
```

### 5. 启动服务

```bash
./start.sh
```

### 6. 查看状态

```bash
./status.sh
```

查看详细日志：
```bash
./status.sh --logs admin   # 查看后台管理系统日志
./status.sh --logs app     # 查看移动端API日志
```

### 7. 停止服务

```bash
./stop.sh
```

### 8. 重启服务

```bash
./restart.sh
```

## 目录结构

```
/opt/xj-shop/
├── jars/                  # JAR包存放目录
│   ├── javaboot-admin.jar
│   └── javaboot-app.jar
├── logs/                  # 日志文件目录
│   ├── admin.log
│   └── app.log
├── config/                # 配置文件目录
│   └── env.conf
├── install.sh             # 安装脚本
├── start.sh               # 启动脚本
├── stop.sh                # 停止脚本
├── status.sh              # 状态检查脚本
└── restart.sh             # 重启脚本
```

## 访问地址

启动成功后：
- 后台管理系统：http://服务器IP:9999
- 移动端API：http://服务器IP:8080
- Swagger API文档：http://服务器IP:9999/swagger-ui.html

## 常见问题

### Q: 服务启动失败？
检查日志文件：
```bash
tail -100 /opt/xj-shop/logs/admin.log
tail -100 /opt/xj-shop/logs/app.log
```

常见原因：
1. 数据库连接失败 - 检查数据库配置和连接
2. 端口被占用 - 检查端口是否被其他程序占用
3. 内存不足 - 调整JVM参数

### Q: 如何查看端口占用？
```bash
netstat -tuln | grep 9999
netstat -tuln | grep 8080
```

### Q: 如何设置开机自启？
创建systemd服务文件：
```bash
vi /etc/systemd/system/xj-shop.service
```

内容参考：
```ini
[Unit]
Description=XiangJia E-commerce System
After=network.target

[Service]
Type=forking
ExecStart=/opt/xj-shop/start.sh
ExecStop=/opt/xj-shop/stop.sh
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

启用服务：
```bash
systemctl enable xj-shop
systemctl start xj-shop
```

## 前端部署（UniApp）

前端 `xj_shop` 为UniApp项目，需要在HBuilderX中打包：
- H5版本：可部署到Nginx
- 小程序：发布到微信开发者平台
- App：生成安装包分发

H5版本Nginx配置示例：
```nginx
server {
    listen 80;
    server_name your_domain.com;

    location /xjshop {
        alias /opt/xj-shop/frontend/xj_shop;
        index index.html;
    }

    # API反向代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```