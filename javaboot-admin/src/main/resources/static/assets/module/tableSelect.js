/** JavaBoot v4.4 date:2020-08-22 License By http://www.javaboot.cn */
layui.define(['table', 'jquery', 'form',  'xmSelect'], function (exports) {
    "use strict";

    var MOD_NAME = 'tableSelect',
        $ = layui.jquery,
        table = layui.table,
        form = layui.form,
        xmSelect = layui.xmSelect;
    var TableSelect = function () {
        this.v = '1.1.0';
    };

    /**
     * 默认参数信息
     */
    TableSelect.defaults = {
        //指向容器选择器
        elem: '',
        //选择提示
        placeholder: '请选择',
        //是否开启搜索框
        search: true,
        //搜索字段名
        searchKey: 'keyword',
        //搜索框提示
        searchPlaceholder: '关键词搜索',
        //选中字段
        checkedKey: '',
        //显示字段
        checkedLabel: '',
        //连接操作符
        separator: ","
    }

    /**
     * 初始化表格选择器
     */
    TableSelect.prototype.render = function (opt) {
        this.options = $.extend(true, {}, TableSelect.defaults, opt);
        var $this = this, options = this.options;
        
        var elem = $(options.elem);
        var tableDone = options.table.done || function () { };

        var dateTime = new Date().getTime();
        var tableName = "tableSelect_table_" + dateTime;
        var searchForm = "tableSelect_form_search_" + dateTime;
        var searchBtn = "tableSelect_btn_search_" + dateTime;
        var resetBtn = "tableSelect_btn_reset_" + dateTime;
        var selectedBtn = "tableSelect_btn_selected_" + dateTime;

        var tableBox = '';
        tableBox += '<div style="margin: 5px 5px;" class="layui-anim layui-anim-upbit">';
        tableBox += '   <form class="layui-form" style="display:inline-block;" id="'+ searchForm + '" onsubmit="return false">';
        tableBox += '       <input style="display:inline-block;width:190px;height:30px;vertical-align:middle;margin-right:-1px;border: 1px solid #C9C9C9;" type="text" name="' + options.searchKey + '" placeholder="' + options.searchPlaceholder + '" autocomplete="off" class="layui-input"/>';
        tableBox += '       <button id="' + searchBtn + '" class="layui-btn layui-btn-sm layui-btn-primary tableSelect_btn_search" lay-submit lay-filter="' + searchBtn + '"><i class="layui-icon layui-icon-search"></i></button>';
        // tableBox += '       <button id="' + resetBtn + '" class="layui-btn layui-btn-sm layui-btn-primary"><i class="layui-icon layui-icon-refresh"></i></button>';
        tableBox += '   </form>';
        tableBox += '   <button id="' + selectedBtn + '" style="float:right;" class="layui-btn layui-btn-sm tableSelect_btn_select">选择<span></span></button>';
        tableBox += '   <table id="' + tableName + '" lay-filter="' + tableName + '"></table>';
        tableBox += '</div>';

        var demo1 = xmSelect.render({
            el: options.elem,
            name: options.name,
            prop: {
                name: options.checkedLabel,
                value: options.checkedKey
            },
            content: tableBox,
            height: 'auto',
        });

        //数据缓存
        var checkedData = [];

        //渲染TABLE
        options.table.elem = "#" + tableName;
        options.table.id = tableName;
        options.table.done = function (res, curr, count) {
            defaultChecked(res, curr, count);
            setChecked(res, curr, count);
            //设置xmselect选中
            setSelected(res, curr, count);
            tableDone(res, curr, count);
        };
        var tableSelect_table = table.render(options.table);

        //监听搜索按钮(form监听失效所以采用jquery监听)
        $('#'+searchBtn).click(function () {
            reloadTable();
        })

        //重载表格数据
        function reloadTable(){
            var arr = $('#'+searchForm).serializeArray();
            var obj = {};
            for (var i = 0; i < arr.length; i++) {//数据类型为"自定义类的字段名=数据"后台会自动对数据进行匹配
                obj[arr[i].name] = arr[i].value;
            }

            tableSelect_table.reload({
                where: obj,
                page: {
                    curr: 1
                }
            });
        }

        //单选监听
        table.on('radio(' + tableName + ')', function (obj) {
            if (options.checkedKey) {
                checkedData = table.checkStatus(tableName).data
            }
            console.log('radio:' + JSON.stringify(checkedData));
            updateButton(table.checkStatus(tableName).data.length)
        })

        //双击行选中(等同于单选)
        // table.on('rowDouble(' + tableName + ')', function (obj) {
        //     console.log(obj.data);
        //
        //     var values = demo1.getValue();
        //     var item = obj.data;
        //     var has = values.find(function (i) {
        //         return i[options.checkedKey] === item[options.checkedKey];
        //     })
        //     if (has) {
        //         demo1.delete([item]);
        //     } else {
        //         console.log('rowDouble:' + JSON.stringify(item));
        //         demo1.setValue([item]);
        //         demo1.closed();
        //         selectDone(obj);
        //     }
        // })

        //多选监听
        table.on('checkbox(' + tableName + ')', function (obj) {
            if (options.checkedKey) {
                if (obj.checked) {
                    for (var i = 0; i < table.checkStatus(tableName).data.length; i++) {
                        checkedData.push(table.checkStatus(tableName).data[i])
                    }
                } else {
                    if (obj.type == 'all') {
                        for (var j = 0; j < table.cache[tableName].length; j++) {
                            for (var i = 0; i < checkedData.length; i++) {
                                if (checkedData[i][options.checkedKey] == table.cache[tableName][j][options.checkedKey]) {
                                    checkedData.splice(i, 1)
                                }
                            }
                        }
                    } else {
                        //因为LAYUI问题，操作到变化全选状态时获取到的obj为空，这里用函数获取未选中的项。
                        function nu() {
                            var noCheckedKey = '';
                            for (var i = 0; i < table.cache[tableName].length; i++) {
                                if (!table.cache[tableName][i].LAY_CHECKED) {
                                    noCheckedKey = table.cache[tableName][i][options.checkedKey];
                                }
                            }
                            return noCheckedKey
                        }

                        var noCheckedKey = obj.data[options.checkedKey] || nu();
                        for (var i = 0; i < checkedData.length; i++) {
                            if (checkedData[i][options.checkedKey] == noCheckedKey) {
                                checkedData.splice(i, 1);
                            }
                        }
                    }
                }
                checkedData = uniqueObjArray(checkedData, options.checkedKey);
                updateButton(checkedData.length)
            } else {
                updateButton(table.checkStatus(tableName).data.length)
            }
        });

        //渲染表格后选中
        function setChecked(res, curr, count) {
            for (var i = 0; i < res.data.length; i++) {
                for (var j = 0; j < checkedData.length; j++) {
                    if (res.data[i][options.checkedKey] == checkedData[j][options.checkedKey]) {
                        res.data[i].LAY_CHECKED = true;
                        var index = res.data[i]['LAY_TABLE_INDEX'];
                        var checkbox = $('#' + tableName + '').next().find('tr[data-index=' + index + '] input[type="checkbox"]');
                        checkbox.prop('checked', true).next().addClass('layui-form-checked');
                        var radio = $('#' + tableName + '').next().find('tr[data-index=' + index + '] input[type="radio"]');
                        radio.prop('checked', true).next().addClass('layui-form-radioed').find("i").html('&#xe643;');
                    }
                }
            }
            var checkStatus = table.checkStatus(tableName);
            //全选
            if (checkStatus.isAll) {
                $('#' + tableName + '').next().find('.layui-table-header th[data-field="0"] input[type="checkbox"]').prop('checked', true);
                $('#' + tableName + '').next().find('.layui-table-header th[data-field="0"] input[type="checkbox"]').next().addClass('layui-form-checked');
            }
            updateButton(checkedData.length)
        }

        //设置xmselect选中
        function setSelected(res, curr, count) {
            if(checkedData){
                demo1.setValue(checkedData);
            }
        }

        //写入默认选中值(puash checkedData)
        function defaultChecked(res, curr, count) {
            if (options.checkedKey && elem.attr('ts-selected')) {
                var selected = elem.attr('ts-selected').split(",");
                for (var i = 0; i < res.data.length; i++) {
                    for (var j = 0; j < selected.length; j++) {
                        if (res.data[i][options.checkedKey] == selected[j]) {
                            checkedData.push(res.data[i])
                        }
                    }
                }
                checkedData = uniqueObjArray(checkedData, options.checkedKey);
            }
        }

        //更新选中数量
        function updateButton(n) {
            $('#' + selectedBtn + ' span').html(n == 0 ? '' : '(' + n + ')')
        }

        //数组去重
        function uniqueObjArray(arr, type) {
            var newArr = [];
            var tArr = [];
            if (arr.length == 0) {
                return arr;
            } else {
                if (type) {
                    for (var i = 0; i < arr.length; i++) {
                        if (!tArr[arr[i][type]]) {
                            newArr.push(arr[i]);
                            tArr[arr[i][type]] = true;
                        }
                    }
                    return newArr;
                } else {
                    for (var i = 0; i < arr.length; i++) {
                        if (!tArr[arr[i]]) {
                            newArr.push(arr[i]);
                            tArr[arr[i]] = true;
                        }
                    }
                    return newArr;
                }
            }
        }

        //监听选择按钮
        $("#" + selectedBtn).click(function () {
            var checkStatus = table.checkStatus(tableName);
            if (checkedData.length > 1) {
                checkStatus.data = checkedData;
            }
            console.log('selectedBtn:' + JSON.stringify(checkStatus.data));
            demo1.setValue(checkStatus.data);
            demo1.closed();
            selectDone(checkStatus);
        })

        //写值回调和关闭
        function selectDone(checkStatus) {
            reloadTable();
            if (options.checkedKey) {
                var selected = [];
                for (var i = 0; i < checkStatus.data.length; i++) {
                    selected.push(checkStatus.data[i][options.checkedKey])
                }
                elem.attr("ts-selected", selected.join(","));
            }
            options.done(elem, checkStatus);
            delete table.cache[tableName];
            checkedData = [];
        }
    }


    //自动完成渲染
    var tableSelect = new TableSelect();

    exports(MOD_NAME, tableSelect);
})