/** JavaBoot v4.4 date:2020-08-22 License By http://www.javaboot.cn */
layui.define(['jquery', 'zTree'], function (exports) {
    var $ = layui.jquery;
    var jQuery = layui.$;

    (function ($) {
        $.extend({
            table: {
                // 回显数据字典
                selectDictLabel: function(datas, value) {
                    var actions = [];
                    $.each(datas, function(index, dict) {
                        if (dict.dictValue == ('' + value)) {
                            var listClass = $.common.equals("default", dict.listClass) || $.common.isEmpty(dict.listClass) ? "" : "badge badge-" + dict.listClass;
                            actions.push($.common.sprintf("<span class='%s'>%s</span>", listClass, dict.dictLabel));
                            return false;
                        }
                    });
                    return actions.join('');
                },
                // 列超出指定长度浮动提示 target（copy单击复制文本 open弹窗打开文本）
                tooltip: function (value, length, target) {
                    var _length = $.common.isEmpty(length) ? 20 : length;
                    var _text = "";
                    var _value = $.common.nullToStr(value);
                    var _target = $.common.isEmpty(target) ? 'copy' : target;
                    if (_value.length > _length) {
                        _text = _value.substr(0, _length) + "...";
                        _value = _value.replace(/\'/g,"’");
                        var actions = [];
                        actions.push($.common.sprintf('<input id="tooltip-show" style="opacity: 0;position: absolute;z-index:-1" type="text" value="%s"/>', _value));
                        actions.push($.common.sprintf("<a href='###' class='tooltip-show' data-toggle='tooltip' data-target='%s' title='%s'>%s</a>", _target, _value, _text));
                        return actions.join('');
                    } else {
                        _text = _value;
                        return _text;
                    }
                }
            },
            _tree: {},
            // 树插件封装处理
            tree: {
                _option: {},
                _lastValue: {},
                // 初始化树结构
                init: function(options) {
                    var defaults = {
                        id: "tree",                    // 属性ID
                        expandLevel: 0,                // 展开等级节点
                        view: {
                            selectedMulti: false,      // 设置是否允许同时选中多个节点
                            nameIsHTML: true           // 设置 name 属性是否支持 HTML 脚本
                        },
                        check: {
                            enable: false,             // 置 zTree 的节点上是否显示 checkbox / radio
                            nocheckInherit: true,      // 设置子节点是否自动继承
                        },
                        data: {
                            key: {
                                title: "title"         // 节点数据保存节点提示信息的属性名称
                            },
                            simpleData: {
                                enable: true           // true / false 分别表示 使用 / 不使用 简单数据模式
                            }
                        },
                    };
                    var options = $.extend(defaults, options);
                    $.tree._option = options;
                    // 树结构初始化加载
                    var setting = {
                        callback: {
                            onClick: options.onClick,                      // 用于捕获节点被点击的事件回调函数
                            onCheck: options.onCheck,                      // 用于捕获 checkbox / radio 被勾选 或 取消勾选的事件回调函数
                            onDblClick: options.onDblClick,                // 用于捕获鼠标双击之后的事件回调函数
                            onAsyncSuccess: options.onAsyncSuccess,         // 异步加载完成
                            onLoadSuccess: options.onLoadSuccess //自定义异步加载完成回调函数
                        },
                        check: options.check,
                        view: options.view,
                        data: options.data
                    };
                    $.get(options.url, function(data) {
                        var treeId = $("#treeId").val();
                        tree = $.fn.zTree.init($("#" + options.id), setting, data);
                        $._tree = tree;
                        var nodes = tree.getNodesByParam("level", options.expandLevel - 1);
                        for (var i = 0; i < nodes.length; i++) {
                            tree.expandNode(nodes[i], true, false, false);
                        }
                        var node = tree.getNodesByParam("id", treeId, null)[0];
                        $.tree.selectByIdName(treeId, node);

                        if(typeof options.onLoadSuccess == 'function'){
                            options.onLoadSuccess(data);
                        }

                    });
                },
                // 搜索节点
                searchNode: function() {
                    // 取得输入的关键字的值
                    var value = $.common.trim($("#keyword").val());
                    if ($.tree._lastValue == value) {
                        return;
                    }
                    // 保存最后一次搜索名称
                    $.tree._lastValue = value;
                    var nodes = $._tree.getNodes();
                    // 如果要查空字串，就退出不查了。
                    if (value == "") {
                        $.tree.showAllNode(nodes);
                        return;
                    }
                    $.tree.hideAllNode(nodes);
                    // 根据搜索值模糊匹配
                    $.tree.updateNodes($._tree.getNodesByParamFuzzy("name", value));
                },
                // 根据Id和Name选中指定节点
                selectByIdName: function(treeId, node) {
                    if ($.common.isNotEmpty(treeId) && treeId == node.id) {
                        $._tree.selectNode(node, true);
                    }
                },
                // 显示所有节点
                showAllNode: function(nodes) {
                    nodes = $._tree.transformToArray(nodes);
                    for (var i = nodes.length - 1; i >= 0; i--) {
                        if (nodes[i].getParentNode() != null) {
                            $._tree.expandNode(nodes[i], true, false, false, false);
                        } else {
                            $._tree.expandNode(nodes[i], true, true, false, false);
                        }
                        $._tree.showNode(nodes[i]);
                        $.tree.showAllNode(nodes[i].children);
                    }
                },
                // 隐藏所有节点
                hideAllNode: function(nodes) {
                    var tree = $.fn.zTree.getZTreeObj("tree");
                    var nodes = $._tree.transformToArray(nodes);
                    for (var i = nodes.length - 1; i >= 0; i--) {
                        $._tree.hideNode(nodes[i]);
                    }
                },
                // 显示所有父节点
                showParent: function(treeNode) {
                    var parentNode;
                    while ((parentNode = treeNode.getParentNode()) != null) {
                        $._tree.showNode(parentNode);
                        $._tree.expandNode(parentNode, true, false, false);
                        treeNode = parentNode;
                    }
                },
                // 显示所有孩子节点
                showChildren: function(treeNode) {
                    if (treeNode.isParent) {
                        for (var idx in treeNode.children) {
                            var node = treeNode.children[idx];
                            $._tree.showNode(node);
                            $.tree.showChildren(node);
                        }
                    }
                },
                // 更新节点状态
                updateNodes: function(nodeList) {
                    $._tree.showNodes(nodeList);
                    for (var i = 0, l = nodeList.length; i < l; i++) {
                        var treeNode = nodeList[i];
                        $.tree.showChildren(treeNode);
                        $.tree.showParent(treeNode)
                    }
                },
                // 获取当前被勾选集合
                getCheckedNodes: function(column) {
                    var _column = $.common.isEmpty(column) ? "id" : column;
                    var nodes = $._tree.getCheckedNodes(true);
                    return $.map(nodes, function (row) {
                        return row[_column];
                    }).join();
                },
                // 不允许根父节点选择
                notAllowParents: function(_tree) {
                    var nodes = _tree.getSelectedNodes();
                    if(nodes.length == 0){
                        $.modal.msgError("请选择节点后提交");
                        return false;
                    }
                    for (var i = 0; i < nodes.length; i++) {
                        if (nodes[i].level == 0) {
                            $.modal.msgError("不能选择根节点（" + nodes[i].name + "）");
                            return false;
                        }
                        if (nodes[i].isParent) {
                            $.modal.msgError("不能选择父节点（" + nodes[i].name + "）");
                            return false;
                        }
                    }
                    return true;
                },
                // 不允许最后层级节点选择
                notAllowLastLevel: function(_tree) {
                    var nodes = _tree.getSelectedNodes();
                    for (var i = 0; i < nodes.length; i++) {
                        if (!nodes[i].isParent) {
                            $.modal.msgError("不能选择最后层级节点（" + nodes[i].name + "）");
                            return false;
                        }
                    }
                    return true;
                },
                // 隐藏/显示搜索栏
                toggleSearch: function() {
                    $('#search').slideToggle(200);
                    $('#btnShow').toggle();
                    $('#btnHide').toggle();
                    $('#keyword').focus();
                },
                // 折叠
                collapse: function() {
                    $._tree.expandAll(false);
                },
                // 展开
                expand: function() {
                    $._tree.expandAll(true);
                }
            },
            // 弹出层封装处理
            modal: {
                // 显示图标
                icon: function(type) {
                    var icon = "";
                    if (type == modal_status.WARNING) {
                        icon = 0;
                    } else if (type == modal_status.SUCCESS) {
                        icon = 1;
                    } else if (type == modal_status.FAIL) {
                        icon = 2;
                    } else {
                        icon = 3;
                    }
                    return icon;
                },
                // 消息提示
                msg: function(content, type) {
                    if (type != undefined) {
                        layer.msg(content, { icon: $.modal.icon(type), time: 1000, shift: 5 });
                    } else {
                        layer.msg(content);
                    }
                },
                // 错误消息
                msgError: function(content) {
                    $.modal.msg(content, modal_status.FAIL);
                },
                // 成功消息
                msgSuccess: function(content) {
                    $.modal.msg(content, modal_status.SUCCESS);
                },
                // 警告消息
                msgWarning: function(content) {
                    $.modal.msg(content, modal_status.WARNING);
                },
                // 弹出提示
                alert: function(content, type) {
                    layer.alert(content, {
                        icon: $.modal.icon(type),
                        title: "系统提示",
                        btn: ['确认'],
                        btnclass: ['btn btn-primary'],
                    });
                },
                // 消息提示并刷新父窗体
                msgReload: function(msg, type) {
                    layer.msg(msg, {
                            icon: $.modal.icon(type),
                            time: 500,
                            shade: [0.1, '#8F8F8F']
                        },
                        function() {
                            $.modal.reload();
                        });
                },
                // 错误提示
                alertError: function(content) {
                    $.modal.alert(content, modal_status.FAIL);
                },
                // 成功提示
                alertSuccess: function(content) {
                    $.modal.alert(content, modal_status.SUCCESS);
                },
                // 警告提示
                alertWarning: function(content) {
                    $.modal.alert(content, modal_status.WARNING);
                },
                // 关闭窗体
                close: function () {
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                },
                // 关闭全部窗体
                closeAll: function () {
                    layer.closeAll();
                },
                // 确认窗体
                confirm: function (content, callBack) {
                    layer.confirm(content, {
                        icon: 3,
                        title: "系统提示",
                        btn: ['确认', '取消']
                    }, function (index) {
                        layer.close(index);
                        callBack(true);
                    });
                },
                // 弹出层指定宽度
                open: function (title, url, width, height, callback) {
                    //如果是移动端，就使用自适应大小弹窗
                    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                        width = 'auto';
                        height = 'auto';
                    }
                    if ($.common.isEmpty(title)) {
                        title = false;
                    }
                    if ($.common.isEmpty(url)) {
                        url = "/404.html";
                    }
                    if ($.common.isEmpty(width)) {
                        width = 800;
                    }
                    if ($.common.isEmpty(height)) {
                        height = ($(window).height() - 50);
                    }
                    if ($.common.isEmpty(callback)) {
                        callback = function(index, layero) {
                            var iframeWin = layero.find('iframe')[0];
                            iframeWin.contentWindow.submitHandler(index, layero);
                        }
                    }
                    layer.open({
                        type: 2,
                        area: [width + 'px', height + 'px'],
                        fix: false,
                        //不固定
                        maxmin: true,
                        shade: 0.3,
                        title: title,
                        content: url,
                        btn: ['确定', '关闭'],
                        // 弹层外区域关闭
                        shadeClose: true,
                        yes: callback,
                        cancel: function(index) {
                            return true;
                        }
                    });
                },
                // 弹出层指定参数选项
                openOptions: function (options) {
                    var _url = $.common.isEmpty(options.url) ? "/404.html" : options.url;
                    var _title = $.common.isEmpty(options.title) ? "系统窗口" : options.title;
                    var _width = $.common.isEmpty(options.width) ? "800" : options.width;
                    var _height = $.common.isEmpty(options.height) ? ($(window).height() - 50) : options.height;
                    var _btn = ['<i class="fa fa-check"></i> 确认', '<i class="fa fa-close"></i> 关闭'];
                    if ($.common.isEmpty(options.yes)) {
                        options.yes = function(index, layero) {
                            options.callBack(index, layero);
                        }
                    }
                    layer.open({
                        type: 2,
                        maxmin: true,
                        shade: 0.3,
                        title: _title,
                        fix: false,
                        area: [_width + 'px', _height + 'px'],
                        content: _url,
                        shadeClose: $.common.isEmpty(options.shadeClose) ? true : options.shadeClose,
                        skin: options.skin,
                        btn: $.common.isEmpty(options.btn) ? _btn : options.btn,
                        yes: options.yes,
                        cancel: function () {
                            return true;
                        }
                    });
                },
                // 弹出层全屏
                openFull: function (title, url, width, height) {
                    //如果是移动端，就使用自适应大小弹窗
                    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                        width = 'auto';
                        height = 'auto';
                    }
                    if ($.common.isEmpty(title)) {
                        title = false;
                    }
                    if ($.common.isEmpty(url)) {
                        url = "/404.html";
                    }
                    if ($.common.isEmpty(width)) {
                        width = 800;
                    }
                    if ($.common.isEmpty(height)) {
                        height = ($(window).height() - 50);
                    }
                    var index = layer.open({
                        type: 2,
                        area: [width + 'px', height + 'px'],
                        fix: false,
                        //不固定
                        maxmin: true,
                        shade: 0.3,
                        title: title,
                        content: url,
                        btn: ['确定', '关闭'],
                        // 弹层外区域关闭
                        shadeClose: true,
                        yes: function(index, layero) {
                            var iframeWin = layero.find('iframe')[0];
                            iframeWin.contentWindow.submitHandler(index, layero);
                        },
                        cancel: function(index) {
                            return true;
                        }
                    });
                    layer.full(index);
                },
                // 选卡页方式打开
                openTab: function (title, url) {
                    //createMenuItem(url, title);
                },
                // 关闭选项卡
                closeTab: function (dataId) {
                    //closeItem(dataId);
                },
                // 禁用按钮
                disable: function() {
                    var doc = window.top == window.parent ? window.document : window.parent.document;
                    $("a[class*=layui-layer-btn]", doc).addClass("layer-disabled");
                },
                // 启用按钮
                enable: function() {
                    var doc = window.top == window.parent ? window.document : window.parent.document;
                    $("a[class*=layui-layer-btn]", doc).removeClass("layer-disabled");
                },
                // 打开遮罩层
                loading: function (message) {
                    //$.blockUI({ message: '<div class="loaderbox"><div class="loading-activity"></div> ' + message + '</div>' });
                    //loading层
                    var index = layer.load(1, {
                        shade: [0.1,'#fff'] //0.1透明度的白色背景
                    });
                },
                // 关闭遮罩层
                closeLoading: function () {
                    setTimeout(function(){
                        layer.closeAll('loading');
                    }, 2000);
                },
                // 重新加载
                reload: function () {
                    parent.location.reload();
                }
            },
            // 通用方法封装处理
            common: {
                // 判断字符串是否为空
                isEmpty: function (value) {
                    if (value == null || this.trim(value) == "") {
                        return true;
                    }
                    return false;
                },
                // 判断一个字符串是否为非空串
                isNotEmpty: function (value) {
                    return !$.common.isEmpty(value);
                },
                // 空对象转字符串
                nullToStr: function(value) {
                    if ($.common.isEmpty(value)) {
                        return "-";
                    }
                    return value;
                },
                // 是否显示数据 为空默认为显示
                visible: function (value) {
                    if ($.common.isEmpty(value) || value == true) {
                        return true;
                    }
                    return false;
                },
                // 空格截取
                trim: function (value) {
                    if (value == null) {
                        return "";
                    }
                    return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
                },
                // 比较两个字符串（大小写敏感）
                equals: function (str, that) {
                    return str == that;
                },
                // 比较两个字符串（大小写不敏感）
                equalsIgnoreCase: function (str, that) {
                    return String(str).toUpperCase() === String(that).toUpperCase();
                },
                // 将字符串按指定字符分割
                split: function (str, sep, maxLen) {
                    if ($.common.isEmpty(str)) {
                        return null;
                    }
                    var value = String(str).split(sep);
                    return maxLen ? value.slice(0, maxLen - 1) : value;
                },
                // 字符串格式化(%s )
                sprintf: function (str) {
                    var args = arguments, flag = true, i = 1;
                    str = str.replace(/%s/g, function () {
                        var arg = args[i++];
                        if (typeof arg === 'undefined') {
                            flag = false;
                            return '';
                        }
                        return arg;
                    });
                    return flag ? str : '';
                },
                // 指定随机数返回
                random: function (min, max) {
                    return Math.floor((Math.random() * max) + min);
                },
                // 判断字符串是否是以start开头
                startWith: function(value, start) {
                    var reg = new RegExp("^" + start);
                    return reg.test(value)
                },
                // 判断字符串是否是以end结尾
                endWith: function(value, end) {
                    var reg = new RegExp(end + "$");
                    return reg.test(value)
                },
                // 数组去重
                uniqueFn: function(array) {
                    var result = [];
                    var hashObj = {};
                    for (var i = 0; i < array.length; i++) {
                        if (!hashObj[array[i]]) {
                            hashObj[array[i]] = true;
                            result.push(array[i]);
                        }
                    }
                    return result;
                },
                // 数组中的所有元素放入一个字符串
                join: function(array, separator) {
                    if ($.common.isEmpty(array)) {
                        return null;
                    }
                    return array.join(separator);
                },
                // 获取form下所有的字段并转换为json对象
                formToJSON: function(formId) {
                    var json = {};
                    $.each($("#" + formId).serializeArray(), function(i, field) {
                        if(json[field.name]) {
                            json[field.name] += ("," + field.value);
                        } else {
                            json[field.name] = field.value;
                        }
                    });
                    return json;
                },
                // 获取obj对象长度
                getLength: function(obj) {
                    var count = 0;
                    for (var i in obj) {
                        if (obj.hasOwnProperty(i)) {
                            count++;
                        }
                    }
                    return count;
                }

                /**
                 * 计算时间差，单位秒
                 * @param startDateStr
                 * @param endDateStr
                 */
                ,calcTotalSecond: function(startDateStr, endDateStr) {
                    var date1 = new Date(startDateStr);             // 开始时间
                    var date2 = new Date(endDateStr);               // 结束时间
                    var timeSub = date2.getTime() - date1.getTime();  // 时间差毫秒
                    return timeSub / 1000;
                },
                /**
                 * 计算出相差天数
                 * @param secondSub
                 */
                formatTotalDateSub: function(secondSub) {
                    var days = Math.floor(secondSub / (24 * 3600));     // 计算出小时数
                    var leave1 = secondSub % (24*3600) ;                // 计算天数后剩余的毫秒数
                    var hours = Math.floor(leave1 / 3600);              // 计算相差分钟数
                    var leave2 = leave1 % (3600);                       // 计算小时数后剩余的毫秒数
                    var minutes = Math.floor(leave2 / 60);              // 计算相差秒数
                    var leave3 = leave2 % 60;                           // 计算分钟数后剩余的毫秒数
                    var seconds = Math.round(leave3);
                    return days + " 天 " + hours + " 时 " + minutes + " 分 " + seconds + ' 秒';
                }
            }
        });
    })(jQuery);

    (function ($) {
        $.extend({
            websocket: {
                _this: null,
                _initialized: false,
                init: function (options) {
                    if (!this.isSupported()) {
                        // console.error('Not support websocket');
                        return;
                    }
                    var op = $.extend({
                        callback: function () {
                        },
                        host: null,
                        reconnect: false
                    }, options);
                    if (!op.host) {
                        // console.error("初始化WebSocket失败，无效的请求地址");
                        return;
                    }
                    try {
                        this._this = new WebSocket(op.host);
                    } catch (error) {
                        return;
                    }
                    this._initialized = true;
                    //连接发生错误的回调方法
                    this._this.onerror = function () {
                        // console.log("与服务器连接失败...");
                    };

                    //连接成功建立的回调方法
                    this._this.onopen = function (event) {
                        // console.log("与服务器连接成功...");
                    };

                    //接收到消息的回调方法
                    this._this.onmessage = function (event) {
                        // dwz.notification.show({notification: event.data});
                        op.callback(event.data);
                        // console.log("接收到服务器端推送的消息：" + event.data);
                    };

                    //连接关闭的回调方法
                    this._this.onclose = function () {
                        $.websocket._initialized = false;
                        // console.log("已关闭当前链接");
                        if (op.reconnect) {
                            // 自动重连
                            setTimeout(function () {
                                $.websocket.open(op);
                            }, 5000);
                        }
                    }
                },
                open: function (options) {
                    var op = $.extend({
                        callback: function () {
                        },
                        host: null,
                        reconnect: false
                    }, options);

                    if (this._initialized) {
                        this.close();
                    }
                    this.init(options);
                    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
                    window.onbeforeunload = function () {
                        // console.log("窗口关闭了");
                        $.websocket.close();
                    }
                },
                isSupported: function () {
                    return 'WebSocket' in window;
                },
                send: function (message) {
                    if (!this._this) {
                        return;
                    }
                    this._this.send(message);
                },
                close: function () {
                    if (!this._this) {
                        return;
                    }
                    this._this.close();
                }
            }
        });
    })(jQuery);

    /** 消息状态码 */
    web_status = {
        SUCCESS: 0,
        FAIL: 500,
        WARNING: 301
    };

    /** 弹窗状态码 */
    modal_status = {
        SUCCESS: "success",
        FAIL: "error",
        WARNING: "warning"
    };

    exports('ext', {});
});
