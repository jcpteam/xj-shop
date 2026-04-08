#!/bin/bash

# 湘佳电商停止脚本
# 用途：停止后台管理系统和移动端API服务

set -e

# ==================== 配置区域 ====================
APP_NAME="xj-shop"
INSTALL_DIR="/opt/${APP_NAME}"
JAR_DIR="${INSTALL_DIR}/jars"
LOG_DIR="${INSTALL_DIR}/logs"

# JAR包名称
ADMIN_JAR="javaboot-admin.jar"
APP_JAR="javaboot-app.jar"

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

# ==================== 停止服务 ====================
stop_service() {
    local JAR_NAME=$1
    local SERVICE_NAME=$2

    log_info "停止 ${SERVICE_NAME}..."

    PID=$(ps -ef | grep java | grep ${JAR_NAME} | grep -v grep | awk '{print $2}')

    if [[ -z "${PID}" ]]; then
        log_warn "${SERVICE_NAME} 未运行"
        return 0
    fi

    log_info "发现进程 PID: ${PID}"

    # 发送TERM信号，优雅停止
    kill -TERM ${PID}

    # 等待进程退出
    local count=0
    local max_wait=30

    while [[ $count -lt $max_wait ]]; do
        PID=$(ps -ef | grep java | grep ${JAR_NAME} | grep -v grep | awk '{print $2}')

        if [[ -z "${PID}" ]]; then
            log_info "${SERVICE_NAME} 已停止"
            return 0
        fi

        sleep 1
        count=$((count + 1))
        echo -n "."
    done

    echo ""

    # 如果进程还在运行，强制杀死
    PID=$(ps -ef | grep java | grep ${JAR_NAME} | grep -v grep | awk '{print $2}')
    if [[ -n "${PID}" ]]; then
        log_warn "进程未响应，强制停止..."
        kill -9 ${PID}
        sleep 2

        PID=$(ps -ef | grep java | grep ${JAR_NAME} | grep -v grep | awk '{print $2}')
        if [[ -z "${PID}" ]]; then
            log_info "${SERVICE_NAME} 已强制停止"
        else
            log_error "${SERVICE_NAME} 停止失败"
            return 1
        fi
    fi
}

# ==================== 显示状态 ====================
show_status() {
    echo ""
    echo "=========================================="
    echo "          服务停止状态"
    echo "=========================================="

    ADMIN_PID=$(ps -ef | grep java | grep ${ADMIN_JAR} | grep -v grep | awk '{print $2}')
    APP_PID=$(ps -ef | grep java | grep ${APP_JAR} | grep -v grep | awk '{print $2}')

    if [[ -n "${ADMIN_PID}" ]]; then
        echo -e "后台管理系统: ${RED}运行中${NC} (PID: ${ADMIN_PID})"
    else
        echo -e "后台管理系统: ${GREEN}已停止${NC}"
    fi

    if [[ -n "${APP_PID}" ]]; then
        echo -e "移动端API:    ${RED}运行中${NC} (PID: ${APP_PID})"
    else
        echo -e "移动端API:    ${GREEN}已停止${NC}"
    fi

    echo "=========================================="
}

# ==================== 主程序 ====================
main() {
    echo ""
    echo "=========================================="
    echo "   湘佳电商系统停止脚本"
    echo "=========================================="
    echo ""

    stop_service ${ADMIN_JAR} "后台管理系统"
    stop_service ${APP_JAR} "移动端API"

    show_status
}

main