#!/bin/bash

# 湘佳电商启动脚本
# 用途：启动后台管理系统和移动端API服务

set -e

# ==================== 配置区域 ====================
APP_NAME="xj-shop"
INSTALL_DIR="/opt/${APP_NAME}"
JAR_DIR="${INSTALL_DIR}/jars"
LOG_DIR="${INSTALL_DIR}/logs"
CONFIG_DIR="${INSTALL_DIR}/config"

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

# ==================== 加载环境配置 ====================
load_config() {
    if [[ -f ${CONFIG_DIR}/env.conf ]]; then
        source ${CONFIG_DIR}/env.conf
        log_info "已加载配置文件: ${CONFIG_DIR}/env.conf"
    else
        # 使用默认配置
        ADMIN_PORT=9999
        APP_PORT=8080
        ADMIN_JVM_OPTS="-Xms512M -Xmx512M -XX:+UseG1GC"
        APP_JVM_OPTS="-Xms256M -Xmx256M -XX:+UseG1GC"
        log_warn "未找到配置文件，使用默认配置"
    fi
}

# ==================== 检查JAR文件 ====================
check_jars() {
    if [[ ! -f ${JAR_DIR}/${ADMIN_JAR} ]]; then
        log_error "未找到 ${ADMIN_JAR}"
        log_error "请将JAR包上传到 ${JAR_DIR} 目录"
        exit 1
    fi

    if [[ ! -f ${JAR_DIR}/${APP_JAR} ]]; then
        log_error "未找到 ${APP_JAR}"
        log_error "请将JAR包上传到 ${JAR_DIR} 目录"
        exit 1
    fi

    log_info "JAR包检查通过"
}

# ==================== 检查服务是否已运行 ====================
check_running() {
    ADMIN_PID=$(ps -ef | grep java | grep ${ADMIN_JAR} | grep -v grep | awk '{print $2}')
    APP_PID=$(ps -ef | grep java | grep ${APP_JAR} | grep -v grep | awk '{print $2}')

    if [[ -n "${ADMIN_PID}" ]]; then
        log_warn "后台管理系统已运行 (PID: ${ADMIN_PID})"
    fi

    if [[ -n "${APP_PID}" ]]; then
        log_warn "移动端API已运行 (PID: ${APP_PID})"
    fi
}

# ==================== 启动后台管理系统 ====================
start_admin() {
    log_info "启动后台管理系统..."

    # 检查是否已运行
    ADMIN_PID=$(ps -ef | grep java | grep ${ADMIN_JAR} | grep -v grep | awk '{print $2}')
    if [[ -n "${ADMIN_PID}" ]]; then
        log_warn "后台管理系统已运行 (PID: ${ADMIN_PID})，跳过启动"
        return 0
    fi

    # JVM参数
    JVM_OPTS="${ADMIN_JVM_OPTS:-"-Xms512M -Xmx512M -XX:+UseG1GC"}"

    # Spring Profile
    SPRING_PROFILE="prod"

    # 启动命令
    nohup java ${JVM_OPTS} \
        -Dname=${ADMIN_JAR} \
        -Duser.timezone=Asia/Shanghai \
        -jar ${JAR_DIR}/${ADMIN_JAR} \
        --spring.profiles.active=${SPRING_PROFILE} \
        --server.port=${ADMIN_PORT:-9999} \
        > ${LOG_DIR}/admin.log 2>&1 &

    sleep 3

    # 检查启动结果
    ADMIN_PID=$(ps -ef | grep java | grep ${ADMIN_JAR} | grep -v grep | awk '{print $2}')
    if [[ -n "${ADMIN_PID}" ]]; then
        log_info "后台管理系统启动成功 (PID: ${ADMIN_PID})"
        log_info "访问地址: http://localhost:${ADMIN_PORT:-9999}"
    else
        log_error "后台管理系统启动失败，请查看日志: ${LOG_DIR}/admin.log"
        exit 1
    fi
}

# ==================== 启动移动端API ====================
start_app() {
    log_info "启动移动端API..."

    # 检查是否已运行
    APP_PID=$(ps -ef | grep java | grep ${APP_JAR} | grep -v grep | awk '{print $2}')
    if [[ -n "${APP_PID}" ]]; then
        log_warn "移动端API已运行 (PID: ${APP_PID})，跳过启动"
        return 0
    fi

    # JVM参数
    JVM_OPTS="${APP_JVM_OPTS:-"-Xms256M -Xmx256M -XX:+UseG1GC"}"

    # Spring Profile
    SPRING_PROFILE="prod"

    # 启动命令
    nohup java ${JVM_OPTS} \
        -Dname=${APP_JAR} \
        -Duser.timezone=Asia/Shanghai \
        -jar ${JAR_DIR}/${APP_JAR} \
        --spring.profiles.active=${SPRING_PROFILE} \
        --server.port=${APP_PORT:-8080} \
        > ${LOG_DIR}/app.log 2>&1 &

    sleep 3

    # 检查启动结果
    APP_PID=$(ps -ef | grep java | grep ${APP_JAR} | grep -v grep | awk '{print $2}')
    if [[ -n "${APP_PID}" ]]; then
        log_info "移动端API启动成功 (PID: ${APP_PID})"
        log_info "访问地址: http://localhost:${APP_PORT:-8080}"
    else
        log_error "移动端API启动失败，请查看日志: ${LOG_DIR}/app.log"
        exit 1
    fi
}

# ==================== 显示状态 ====================
show_status() {
    echo ""
    echo "=========================================="
    echo "          服务启动状态"
    echo "=========================================="

    ADMIN_PID=$(ps -ef | grep java | grep ${ADMIN_JAR} | grep -v grep | awk '{print $2}')
    APP_PID=$(ps -ef | grep java | grep ${APP_JAR} | grep -v grep | awk '{print $2}')

    if [[ -n "${ADMIN_PID}" ]]; then
        echo -e "后台管理系统: ${GREEN}运行中${NC} (PID: ${ADMIN_PID}, Port: ${ADMIN_PORT:-9999})"
    else
        echo -e "后台管理系统: ${RED}未运行${NC}"
    fi

    if [[ -n "${APP_PID}" ]]; then
        echo -e "移动端API:    ${GREEN}运行中${NC} (PID: ${APP_PID}, Port: ${APP_PORT:-8080})"
    else
        echo -e "移动端API:    ${RED}未运行${NC}"
    fi

    echo ""
    echo "日志文件:"
    echo "  - 后台管理: ${LOG_DIR}/admin.log"
    echo "  - 移动端API: ${LOG_DIR}/app.log"
    echo "=========================================="
}

# ==================== 主程序 ====================
main() {
    echo ""
    echo "=========================================="
    echo "   湘佳电商系统启动脚本"
    echo "=========================================="
    echo ""

    load_config
    check_jars
    check_running

    start_admin
    start_app

    show_status
}

main