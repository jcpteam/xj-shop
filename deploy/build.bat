@echo off
chcp 65001 >nul
REM 湘佳电商打包脚本 (Windows版本)
REM 用途：在本地执行Maven打包，生成部署所需的JAR文件

setlocal EnableDelayedExpansion

REM ==================== 配置区域 ====================
set PROJECT_DIR=%~dp0..
set OUTPUT_DIR=%PROJECT_DIR%\deploy\output

REM ==================== 检查Maven ====================
echo.
echo ==========================================
echo    湘佳电商系统打包脚本 (Windows)
echo ==========================================
echo.

where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到Maven命令，请先安装Maven并配置环境变量
    pause
    exit /b 1
)

echo [信息] Maven版本:
mvn -version
echo.

REM ==================== 执行打包 ====================
echo [信息] 开始打包...
echo [信息] 项目目录: %PROJECT_DIR%

cd /d %PROJECT_DIR%

REM 清理并打包（跳过测试）
mvn clean package -DskipTests -pl javaboot-admin,javaboot-app -am

if %errorlevel% neq 0 (
    echo [错误] 打包失败
    pause
    exit /b 1
)

echo [信息] 打包完成
echo.

REM ==================== 复制JAR文件 ====================
echo [信息] 复制JAR文件到输出目录...

REM 创建输出目录
if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"

REM 复制javaboot-admin.jar
if exist "%PROJECT_DIR%\javaboot-admin\target\javaboot-admin.jar" (
    copy "%PROJECT_DIR%\javaboot-admin\target\javaboot-admin.jar" "%OUTPUT_DIR%\" >nul
    echo [信息] 已复制: javaboot-admin.jar
) else (
    echo [错误] 未找到 javaboot-admin.jar
)

REM 复制javaboot-app.jar
if exist "%PROJECT_DIR%\javaboot-app\target\javaboot-app.jar" (
    copy "%PROJECT_DIR%\javaboot-app\target\javaboot-app.jar" "%OUTPUT_DIR%\" >nul
    echo [信息] 已复制: javaboot-app.jar
) else (
    echo [错误] 未找到 javaboot-app.jar
)

echo.

REM ==================== 显示结果 ====================
echo.
echo ==========================================
echo           打包结果
echo ==========================================
echo.

dir "%OUTPUT_DIR%\*.jar" 2>nul
if %errorlevel% neq 0 (
    echo [警告] 输出目录中没有JAR文件
)

echo.
echo 下一步操作:
echo 1. 将 %OUTPUT_DIR% 目录下的JAR文件上传到服务器
echo    可使用scp、ftp或其他文件传输工具
echo.
echo 2. 在服务器上启动服务
echo    cd /opt/xj-shop && ./start.sh
echo.
echo ==========================================
echo.

pause