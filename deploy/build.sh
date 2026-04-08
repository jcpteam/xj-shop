#!/bin/bash

# 湘佳电商打包脚本
# 用途：在本地执行Maven打包，生成部署所需的JAR文件

set -e

# ==================== 配置区域 ====================
PROJECT_DIR=$(cd "$(dirname "$0")/.." && pwd)
OUTPUT_DIR="${PROJECT_DIR}/deploy/output"

# ==================== 颜色输出 ====================
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# ==================== 检查Maven ====================
check_maven() {
    if ! command -v mvn &> /dev/null; then
        log_error "未找到Maven命令，请先安装Maven"
        exit 1
    fi

    log_info "Maven版本:"
    mvn -version
}

# ==================== 执行打包 ====================
do_package() {
    log_info "开始打包..."
    log_info "项目目录: ${PROJECT_DIR}"

    cd ${PROJECT_DIR}

    # 清理并打包
    mvn clean package -DskipTests -pl javaboot-admin,javaboot-app -am

    log_info "打包完成"
}

# ==================== 复制JAR文件 ====================
copy_jars() {
    log_info "复制JAR文件到输出目录..."

    # 清理输出目录
    rm -rf ${OUTPUT_DIR}
    mkdir -p ${OUTPUT_DIR}

    # 复制javaboot-admin.jar
    if [[ -f ${PROJECT_DIR}/javaboot-admin/target/javaboot-admin.jar ]]; then
        cp ${PROJECT_DIR}/javaboot-admin/target/javaboot-admin.jar ${OUTPUT_DIR}/
        SIZE=$(ls -lh ${OUTPUT_DIR}/javaboot-admin.jar | awk '{print $5}')
        log_info "已复制: javaboot-admin.jar (${SIZE})"
    else
        log_error "未找到 javaboot-admin.jar"
        exit 1
    fi

    # 复制javaboot-app.jar
    if [[ -f ${PROJECT_DIR}/javaboot-app/target/javaboot-app.jar ]]; then
        cp ${PROJECT_DIR}/javaboot-app/target/javaboot-app.jar ${OUTPUT_DIR}/
        SIZE=$(ls -lh ${OUTPUT_DIR}/javaboot-app.jar | awk '{print $5}')
        log_info "已复制: javaboot-app.jar (${SIZE})"
    else
        log_error "未找到 javaboot-app.jar"
        exit 1
    fi

    log_info "输出目录: ${OUTPUT_DIR}"
}

# ==================== 显示结果 ====================
show_result() {
    echo ""
    echo "=========================================="
    echo "          打包结果"
    echo "=========================================="
    echo ""

    ls -lh ${OUTPUT_DIR}/*.jar

    echo ""
    echo "=========================================="
    echo "下一步操作:"
    echo "=========================================="
    echo ""
    echo "1. 上传JAR文件到服务器:"
    echo "   scp ${OUTPUT_DIR}/*.jar root@your_server:/opt/xj-shop/jars/"
    echo ""
    echo "2. 启动服务:"
    echo "   cd /opt/xj-shop && ./start.sh"
    echo ""
    echo "=========================================="
}

# ==================== 主程序 ====================
main() {
    echo ""
    echo "=========================================="
    echo "   湘佳电商系统打包脚本"
    echo "=========================================="
    echo ""

    check_maven
    do_package
    copy_jars
    show_result
}

main