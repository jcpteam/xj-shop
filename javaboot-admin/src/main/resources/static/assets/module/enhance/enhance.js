/** JavaBoot v4.4 date:2020-08-22 License By http://www.javaboot.cn */
layui.define(['layer', 'form', 'jquery', 'table', 'laydate'], function(exports) {
    
    /* 这些方法建议单独存放于一个JS中，此处为了整体代码的简洁性，不单独存放 */
    window.isBlank = (str) => {
        return str === undefined || str === null || /^\s*$/.test(str)
    }
    window.isNotBlank = (str) => {
        return !isBlank(str)
    }
    window.defaultString = function(str, defaultStr) {
        return isBlank(str) ? (defaultStr ? defaultStr : "") : str
    }
    window.formatTime = (millsecond) => {
        // 将毫秒格式化为 可读的 字符串
        if (!Number.isInteger(millsecond)) {
            return millsecond
        }
        let second = millsecond / 1000

        if (second < 60) {
            return (second) + "秒"
        } else if (second / 60 < 60) {
            return Math.floor((second / 60)) + "分" + Math.floor((second % 60)) + "秒"
        } else if (second / 60 / 60 < 24) {
            return Math.floor((second / 60 / 60)) + "时" + Math.floor((second / 60 % 60)) + "分" + Math.floor((second % 60)) + "秒"
        } else {
            return Math.floor((second / 86400)) + "天" + Math.floor((second % 86400 / 60 / 60)) + "时" +
                Math.floor((second % 86400 / 60 % 60)) + "分" + Math.floor((second % 60)) + "秒"
        }
        return millsecond
    }

    Date.prototype.format = function(fmt) {
        let o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        fmt = fmt.replace(new RegExp("HH", 'g'), "hh")
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            }
        }
        return fmt;
    }

    /**
     * 将日期字符串从一种类型转换为另一种类型
     * @param {string} dateStr
     * @param {string} pattern
     */
    function formatToAnotherPattern(dateStr, pattern) {
        if (dateStr === undefined || dateStr === null || dateStr.trim() === "") {
            return ""
        }
        if (pattern === undefined || pattern === null || pattern.trim() === "") {
            return ""
        }
        return new Date(dateStr).format(pattern)
    }


    /**
     * 增强layui table 功能
     * 1、给表格数据增加默认属性，以实现工具栏重新渲染功能
     * 2、当分页不启用时，将默认只显示10条 修改为显示所有
     * 3、设置表格的默认分页参数
     * 4、增加获取table实例的方法
     * 5、增加 新增行功能，可指定位置
     * 6、增加 渲染指定行、列方法
     * 7、增加 获取表格所有行数据
     * 8、增加 删除指定行
     * 9、扩展表格加载耗时显示
     * 10、内容超出设置
     * 11、支持单元格单击事件
     * 12、增强 当工具栏宽度不够时，弹出详情里面的按钮不能点击
     * 13、增加 可编辑列的配置（增加字段：editConfig: { form: "input", verify: "required", placeholder: "必填",dict: ""} ）
     * 14、增加 扩展操作列表（增加字段
     *      extToolbar: ["excel","csv"],
     *      excel: {templateUrl: "", exportUrl: "" , importUrl: "",params: {},beforeHandler(obj){} }
     *      csv: {templateUrl: "", exportUrl: "" , importUrl: "",params: {},beforeHandler(obj){} }
     * 15、获取 指定表格的指定列配置信息
     * 16、增加 合并单元格方法
     * 17、增强 当列表中某列为字典值时，自动设置 templet 函数 （为col 增加 dict 字段）
     * 18、增强 当列表中某列开启日期格式化时，自动设置templet 函数（为col 增加字段：dateFormat: boolean || string，取值：true/false、yyyy-MM-dd hh:mm:ss）
     * 19、增强 合计行数据自定义显示（为col 增加：totalRowFormatter(rows){return value}）
     * 20、设置列对齐默认为居中
     */
    let $ = layui.$,
        table = layui.table,
        form = layui.form
    if (layui && layui.table && !layui.table.$enhanced) {

        let wait = ms => new Promise((r, j) => setTimeout(r, ms))

        layui.table.config.cellToolbarName = "LAY_CELL_TOOLBAR"
        layui.table.config.parseData = (res) => {
            // 覆盖数据表格默认的数据转换，增加自定义字段，以实现更新指定行时可对列工具栏进行重新渲染，只对ajax访问时有效
            res.data && res.data.constructor == Array && res.data.forEach(o => o[layui.table.config.cellToolbarName] = "just for update cell toolbar")
            return res
        }

        let instanceMap = new Map()
        let $render = layui.table.render
        layui.table.render = (config) => {

            let waitForInstance = (timeoutms) => new Promise((resolve, reject) => {
                let check = () => {
                    if (layui.table.getInstance(config.id))
                        resolve()
                    else if ((timeoutms -= 100) < 0)
                        reject('timed out!')
                    else
                        setTimeout(check, 100)
                }
                setTimeout(check, 100)
            })

            config.request = config.request || {}
            config.startTime = new Date().getTime()

            if (!config.page) {
                config.limit = Number.MAX_VALUE
                config.request.pageName = 'currPage'
                delete config.request.limitName
            } else {
                config.request.pageName = 'currPage'
                config.request.limitName = 'pageSize'
            }

            if (config.page && !(config.limit || config.page.limit)) {
                if (typeof config.page === "boolean") {
                    config.page = { limit: 30 }
                } else {
                    config.page.limit = 30
                }
            }

            config.data && config.data.constructor == Array && config.data.forEach(o => o[layui.table.config.cellToolbarName] = "just for update cell toolbar")

            config.overflow = config.overflow || { type: 'tips' }
            let $done = config.done
            config.done = (res, curr, count) => {
                if (!config.id) {
                    return
                }

                // 去除可编辑表格，文本超出无省略号的问题
                $(`div[lay-id='${config.id}'] .layui-table-body .layui-table-cell:not(:has(input))`).css("cssText", "overflow:hidden!important;")
                $(`div[lay-id='${config.id}'] .layui-table-body .layui-table-cell:not(:has(div))`).css("cssText", "overflow:hidden!important;")

                if (config.overflow) {
                    let overflowOptions = {
                        type: "tips",
                        minWidth: 300,
                        maxWidth: 300,
                        color: "white",
                        bgColor: "black"
                    }
                    $.extend(overflowOptions, config.overflow)

                    let layBody = $(`div[lay-id='${config.id}'] .layui-table-body`)
                    // let color = 'white', bgColor = 'black', minWidth = 300, maxWidth = 300
                    let { type, color, bgColor, minWidth, maxWidth } = overflowOptions

                    let tooltipIndex = null
                    layBody.off(['mouseenter', 'mouseleave'], 'td').on('mouseenter', 'td', function() {
                        let othis = $(this),
                            elemCell = othis.children('.layui-table-cell')
                        if (othis.data('off') || othis.data("field") === 'LAY_CELL_TOOLBAR') return;
                        if (othis.has('select').length != 0) return;

                        let outerWidth = othis.children('.layui-table-cell').outerWidth()
                        let layerWidth = (outerWidth < minWidth ? minWidth : (outerWidth > maxWidth ? maxWidth : outerWidth))

                        if (elemCell.prop('scrollWidth') > elemCell.outerWidth()) {
                            tooltipIndex = layer.tips('<span style="color: ' + color + '">' + othis.text() + '</span>', this, {
                                tips: [1, bgColor],
                                maxWidth: layerWidth,
                                time: 0
                            });
                        }
                    }).on('mouseleave', 'td', function() {
                        layer.close(tooltipIndex)
                    })
                }

                (async () => {


                    // 等待获取到实例
                    await waitForInstance(10 * 1000)

                    let ins = layui.table.getInstance(config.id)
                    if (!ins) {
                        $done && $done(res, curr, count)
                        return
                    }
                    let layTable = $(`div[lay-id='${ins.config.id}']`),
                        layBody = layTable.find(`.layui-table-body`),
                        layTotal = layTable.find(`.layui-table-total`)

                    $done && $done(res, curr, count)
                })()
            }
            let instance = $render(config)
            instance != null && isNotBlank(instance.config.id) && instanceMap.set(instance.config.id, instance)

            return instance
        }


        /**
         * 获取Table实例
         * @param tableId
         * @return {any}
         */
        layui.table.getInstance = (tableId) => {
            return instanceMap.get(tableId)
        }

        /**
         * 获取指定表格的指定列配置信息
         * @param tableId
         * @param field
         * @return {null|*}
         */
        layui.table.getColConfigByField = (tableId, field) => {
            let ins = layui.table.getInstance(tableId)
            if (!ins) {
                return null
            }
            for (let i = 0; i < ins.config.cols.length; i++) {
                for (let j = 0; j < ins.config.cols[i].length; j++) {
                    if (ins.config.cols[i][j].field == field) {
                        return ins.config.cols[i][j]
                    }
                }
            }
        }
        /**
         * 添加新行
         * @param tableId
         * @param index 位置，从0开始
         * @param data
         */
        layui.table.addRow = (tableId, index, data) => {
            if (!(layui.table.cache[tableId] && data)) {
                return
            }

            // 如果有可编辑列，且为单选下拉框，并为必填，自动设置默认值为 字典的第一个值
            let row = {}
            let ins = layui.table.getInstance(tableId)
            ins.config.cols.forEach(cols => {
                cols.forEach(col => {
                    // 已经设置过值就不再重复设置了
                    if (data[col.field]) {
                        return;
                    }
                    let editConfig = col.editConfig
                    if (editConfig && editConfig.form == "select" && isNotBlank(editConfig.dict) && defaultString(editConfig.verify).indexOf("required") != -1) {
                        let list = typeof editConfig.dict == "string" ? $global.getDictDataList(editConfig.dict) : editConfig.dict
                        if (list.length == 0) {
                            return
                        }
                        row[col.field] = list[0].value
                        return;
                    }
                    if (editConfig && editConfig.form == "checkbox") {
                        row[col.field] = 0
                        return;
                    }
                })
            })
            row = { ...data, ...row }
            let bak = layui.table.cache[tableId].slice()
            // 当有默认值/下拉框时，手动指定默认值
            bak.splice(index, 0, row)
            layui.table.reload(tableId, {
                data: bak
            })
        }

        /**
         * 重新渲染指定行
         * @param tableId 表格ID
         * @param rowIndex 行索引
         * @param data 数据
         */
        layui.table.renderSpecRow = (tableId, rowIndex, data) => {
            let instance = layui.table.getInstance(tableId)
            if (instance == null) {
                return
            }
            if (!data) {
                return
            }
            data = $.extend({
                LAY_CELL_TOOLBAR: "just for update cell toolbar",
                LAY_TABLE_INDEX: rowIndex
            }, data)

            let tr = $(`[lay-id='${tableId}'] .layui-table-body .layui-table tr:eq(${rowIndex})`)
            layui.each(data, (field, index) => {
                let td = tr.children('td[data-field="' + field + '"]')

                let templet = null
                let cols = instance.config.cols
                for (let i = 0; i < cols.length; i++) {
                    let bFind = false
                    for (let j = 0; j < cols[i].length; j++) {
                        let col = cols[i][j]
                        if (col.field == field) {
                            col.templet && (templet = col.templet)
                            bFind = true
                            break
                        }
                    }
                    if (bFind) {
                        break
                    }
                }

                td.children(".layui-table-cell").html(function() {
                    return templet ? function() {
                        return "function" == typeof templet ? templet(data) :
                            layui.laytpl(layui.$(templet).html() || data[field]).render(data)
                    }() : data[field]
                }()), td.data("content", data[field])
            })

            layui.table.cache[tableId][rowIndex] = $.extend(layui.table.cache[tableId][rowIndex], data)
        }

        /**
         * 获取指定表格所有行数据
         * @param tableId
         * @return {Array}
         */
        layui.table.getRows = (tableId) => {
            if (isBlank(tableId)) {
                return []
            }
            return layui.table.cache[tableId] ? layui.table.cache[tableId] : []
        }

        /**
         * 删除指定行
         * @param {string} tableId 表格ID
         * @param {number} rowIndex 行索引。>= 0
         * @return {boolean}
         */
        layui.table.deleteRow = (tableId, rowIndex) => {
            let ins = layui.table.getInstance(tableId)
            if (ins == null) {
                return false
            }
            if (rowIndex < 0) {
                return false
            }
            $(`[lay-id='${tableId}'] .layui-table-body .layui-table tr:eq(${rowIndex})`).remove()
            layui.table.cache[tableId].splice(rowIndex, 1)
            layui.table.reload(tableId, {
                data: layui.table.cache[tableId]
            })
        }

    }

    exports('enhance', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});