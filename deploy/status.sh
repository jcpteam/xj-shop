#!/bin/bash

# 湘佳电商状态检查脚本
# 用途：查看后台管理系统和移动端API服务的运行状态

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
BLUE='\033[0;34m'
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

# ==================== 获取进程信息 ====================
get_process_info() {
    local JAR_NAME=$1
    local PID=$(ps -ef | grep java | grep ${JAR_NAME} | grep -v grep | awk '{print $2}')

    if [[ -n "${PID}" ]]; then
        local CPU_USAGE=$(ps -p ${PID} -o %cpu --no-headers | awk '{printf "%.1f", $1}')
        local MEM_USAGE=$(ps -p ${PID} -o %mem --no-headers | awk '{printf "%.1f", $1}')
        local VSZ=$(ps -p ${PID} -o vsz --no-headers | awk '{printf "%.0f", $1/1024}')
        local RSS=$(ps -p ${PID} -o rss --no-headers | awk '{printf "%.0f", $1/1024}')
        local ELAPSED=$(ps -p ${PID} -o etime --no-headers | tr -d ' ')

        echo "${PID}|${CPU_USAGE}|${MEM_USAGE}|${VSZ}|${RSS}|${ELAPSED}"
    else
        echo ""
    fi
}

# ==================== 检查端口监听 ====================
check_port() {
    local PORT=$1

    if netstat -tuln 2>/dev/null | grep -q ":${PORT} "; then
        return 0
    elif ss -tuln 2>/dev/null | grep -q ":${PORT} "; then
        return 0
    else
        return 1
    fi
}

# ==================== 检查日志文件 ====================
check_logs() {
    echo ""
    echo -e "${BLUE}日志文件状态:${NC}"

    for LOG_FILE in "admin.log" "app.log"; do
        local LOG_PATH="${LOG_DIR}/${LOG_FILE}"

        if [[ -f ${LOG_PATH} ]]; then
            local SIZE=$(ls -lh ${LOG_PATH} | awk '{print $5}')
            local LINES=$(wc -l < ${LOG_PATH})
            local LAST_MOD=$(stat -c %y ${LOG_PATH} 2>/dev/null | cut -d'.' -f1)

            echo "  ${LOG_FILE}:"
            echo "    - 大小: ${SIZE}"
            echo "    - 行数: ${LINES}"
            echo "    - 最后修改: ${LAST_MOD}"
        else
            echo "  ${LOG_FILE}: ${YELLOW}不存在${NC}"
        fi
    done
}

# ==================== 显示最近日志 ====================
show_recent_logs() {
    local LOG_FILE=$1
    local LOG_PATH="${LOG_DIR}/${LOG_FILE}"

    if [[ -f ${LOG_PATH} ]]; then
        echo -e "${BLUE}最近10行日志:${NC}"
        tail -10 ${LOG_PATH}
    fi
}

# ==================== 检查JAR文件 ====================
check_jar_files() {
    echo ""
    echo -e "${BLUE}JAR文件状态:${NC}"

    for JAR_FILE in ${ADMIN_JAR} ${APP_JAR}; do
        local JAR_PATH="${JAR_DIR}/${JAR_FILE}"

        if [[ -f ${JAR_PATH} ]]; then
            local SIZE=$(ls -lh ${JAR_PATH} | awk '{print $5}')
            local LAST_MOD=$(stat -c %y ${JAR_PATH} 2>/dev/null | cut -d'.' -f1)

            echo "  ${JAR_FILE}:"
            echo "    - 大小: ${SIZE}"
            echo "    - 最后修改: ${LAST_MOD}"
        else
            echo "  ${JAR_FILE}: ${RED}未找到${NC}"
        fi
    done
}

# ==================== 主显示函数 ====================
show_status() {
    echo ""
    echo "=========================================="
    echo "     湘佳电商系统运行状态"
    echo "=========================================="
    echo ""
    echo "检查时间: $(date '+%Y-%m-%d %H:%M:%S')"
    echo ""

    # 后台管理系统状态
    echo -e "${BLUE}【后台管理系统】${NC}"
    ADMIN_INFO=$(get_process_info ${ADMIN_JAR})

    if [[ -n "${ADMIN_INFO}" ]]; then
        IFS='|' read -r PID CPU MEM VSZ RSS ELAPSED <<< "${ADMIN_INFO}"
        echo -e "  状态: ${GREEN}运行中${NC}"
        echo "  PID: ${PID}"
        echo "  CPU使用率: ${CPU}%"
        echo "  内存使用率: ${MEM}%"
        echo "  虚拟内存: ${VSZ} MB"
        echo "  物理内存: ${RSS} MB"
        echo "  运行时间: ${ELAPSED}"

        if check_port 9999; then
            echo -e "  端口9999: ${GREEN}监听中${NC}"
        else
            echo -e "  端口9999: ${YELLOW}未监听${NC}"
        fi
    else
        echo -e "  状态: ${RED}未运行${NC}"
    fi

    echo ""

    # 移动端API状态
    echo -e "${BLUE}【移动端API】${NC}"
    APP_INFO=$(get_process_info ${APP_JAR})

    if [[ -n "${APP_INFO}" ]]; then
        IFS='|' read -r PID CPU MEM VSZ RSS ELAPSED <<< "${APP_INFO}"
        echo -e "  状态: ${GREEN}运行中${NC}"
        echo "  PID: ${PID}"
        echo "  CPU使用率: ${CPU}%"
        echo "  内存使用率: ${MEM}%"
        echo "  虚拟内存: ${VSZ} MB"
        echo "  物理内存: ${RSS} MB"
        echo "  运行时间: ${ELAPSED}"

        if check_port 8080; then
            echo -e "  端口8080: ${GREEN}监听中${NC}"
        else
            echo -e "  端口8080: ${YELLOW}未监听${NC}"
        fi
    else
        echo -e "  状态: ${RED}未运行${NC}"
    fi

    echo ""

    check_jar_files
    check_logs

    echo ""
    echo "=========================================="
}

# ==================== 快速状态检查 ====================
quick_status() {
    ADMIN_PID=$(ps -ef | grep java | grep ${ADMIN_JAR} | grep -v grep | awk '{print $2}')
    APP_PID=$(ps -ef | grep java | grep ${APP_JAR} | grep -v grep | awk '{print $2}')

    ADMIN_RUNNING=0
    APP_RUNNING=0

    if [[ -n "${ADMIN_PID}" ]]; then
        ADMIN_RUNNING=1
    fi

    if [[ -n "${APP_PID}" ]]; then
        APP_RUNNING=1
    fi

    echo "ADMIN_RUNNING=${ADMIN_RUNNING}"
    echo "ADMIN_PID=${ADMIN_PID}"
    echo "APP_RUNNING=${APP_RUNNING}"
    echo "APP_PID=${APP_PID}"
}

# ==================== 主程序 ====================
main() {
    if [[ "$1" == "--quick" ]]; then
        quick_status
        exit 0
    fi

    if [[ "$1" == "--logs" ]]; then
        local SERVICE=$2

        if [[ "${SERVICE}" == "admin" ]]; then
            show_recent_logs "admin.log"
        elif [[ "${SERVICE}" == "app" ]]; then
            show_recent_logs "app.log"
        else
            echo "请指定服务名称: admin 或 app"
            echo "用法: $0 --logs admin"
            echo "用法: $0 --logs app"
        fi
        exit 0
    fi

    show_status
}

main "$@"