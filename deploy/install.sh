#!/bin/bash

# 湘佳电商部署安装脚本
# 用途：初始化部署环境，安装Java，创建目录结构

set -e

# ==================== 配置区域 ====================
APP_NAME="xj-shop"
INSTALL_DIR="/opt/${APP_NAME}"
JAR_DIR="${INSTALL_DIR}/jars"
LOG_DIR="${INSTALL_DIR}/logs"
UPLOAD_DIR="/root/uploads"
JAVA_VERSION="1.8.0"

# ==================== 颜色输出 ====================
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# ==================== 检查root权限 ====================
check_root() {
    if [[ $EUID -ne 0 ]]; then
        log_error "此脚本需要root权限执行"
        exit 1
    fi
}

# ==================== 安装Java ====================
install_java() {
    log_info "检查Java环境..."

    if java -version 2>&1 | grep -q "version"; then
        log_info "Java已安装"
        java -version
        return 0
    fi

    log_info "安装Java ${JAVA_VERSION}..."
    yum install -y java-${JAVA_VERSION}-openjdk java-${JAVA_VERSION}-openjdk-devel

    if java -version 2>&1 | grep -q "version"; then
        log_info "Java安装成功"
        java -version
    else
        log_error "Java安装失败"
        exit 1
    fi
}

# ==================== 创建目录结构 ====================
create_dirs() {
    log_info "创建目录结构..."

    mkdir -p ${JAR_DIR}
    mkdir -p ${LOG_DIR}
    mkdir -p ${UPLOAD_DIR}
    mkdir -p ${INSTALL_DIR}/config

    log_info "目录结构创建完成:"
    log_info "  - 应用目录: ${INSTALL_DIR}"
    log_info "  - JAR目录: ${JAR_DIR}"
    log_info "  - 日志目录: ${LOG_DIR}"
    log_info "  - 上传目录: ${UPLOAD_DIR}"
    log_info "  - 配置目录: ${INSTALL_DIR}/config"
}

# ==================== 创建配置文件模板 ====================
create_config_template() {
    log_info "创建配置文件模板..."

    # 创建环境变量配置文件
    cat > ${INSTALL_DIR}/config/env.conf << 'EOF'
# 数据库配置（请根据实际情况修改）
DB_HOST=localhost
DB_PORT=3306
DB_NAME=xj_shop_prod
DB_USER=root
DB_PASS=your_password

# 服务端口配置
ADMIN_PORT=9999
APP_PORT=8080

# JVM参数配置
ADMIN_JVM_OPTS="-Xms512M -Xmx512M -XX:+UseG1GC"
APP_JVM_OPTS="-Xms256M -Xmx256M -XX:+UseG1GC"
EOF

    log_info "配置模板已创建: ${INSTALL_DIR}/config/env.conf"
    log_warn "请修改配置文件后再启动服务!"
}

# ==================== 复制部署脚本 ====================
copy_scripts() {
    log_info "部署脚本已就位..."

    # 设置脚本权限
    chmod +x ${INSTALL_DIR}/start.sh 2>/dev/null || true
    chmod +x ${INSTALL_DIR}/stop.sh 2>/dev/null || true
    chmod +x ${INSTALL_DIR}/status.sh 2>/dev/null || true
    chmod +x ${INSTALL_DIR}/restart.sh 2>/dev/null || true

    log_info "脚本权限设置完成"
}

# ==================== 显示使用说明 ====================
show_usage() {
    echo ""
    echo "=========================================="
    echo "          部署安装完成"
    echo "=========================================="
    echo ""
    echo "下一步操作:"
    echo "1. 将JAR包上传到 ${JAR_DIR} 目录:"
    echo "   - javaboot-admin.jar"
    echo "   - javaboot-app.jar"
    echo ""
    echo "2. 修改数据库配置:"
    echo "   vi ${INSTALL_DIR}/config/env.conf"
    echo ""
    echo "3. 启动服务:"
    echo "   ${INSTALL_DIR}/start.sh"
    echo ""
    echo "4. 查看状态:"
    echo "   ${INSTALL_DIR}/status.sh"
    echo ""
    echo "5. 停止服务:"
    echo "   ${INSTALL_DIR}/stop.sh"
    echo ""
    echo "=========================================="
}

# ==================== 主程序 ====================
main() {
    echo ""
    echo "=========================================="
    echo "   湘佳电商系统部署安装脚本"
    echo "=========================================="
    echo ""

    check_root
    install_java
    create_dirs
    create_config_template

    echo ""
    show_usage
}

main