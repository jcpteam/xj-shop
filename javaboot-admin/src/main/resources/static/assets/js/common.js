/** JavaBoot v4.4 date:2020-08-22 License By http://www.javaboot.cn */

// 以下代码是配置layui扩展模块的目录，每个页面都需要引入
layui.config({
    base: getProjectUrl() + 'assets/module/'
}).extend({
    tableSelect: 'tableSelect/tableSelect',
    numinput: 'numinput/numinput',
    dropdown: 'dropdown/dropdown',
    notice: 'notice/notice',
    cascader: 'cascader/cascader',
    fileChoose: 'fileChoose/fileChoose',
    treeSelect: 'treeSelect/treeSelect',
    step: 'steps/steps',
    //treeTable: 'treeTable/treeTable',
    tagsInput: 'tagsInput/tagsInput',
    Split: 'Split/Split',
    Cropper: 'Cropper/Cropper',
    citypicker: 'city-picker/city-picker',
    zTree: 'zTree/zTree',
    introJs: 'introJs/introJs',
    jsonview: 'jsonview/jsonview',
    //
    soulTable: 'soulTable/soulTable',
    tableChild: 'soulTable/tableChild',
    tableMerge: 'soulTable/tableMerge',
    tableFilter: 'soulTable/tableFilter',
    excel: 'soulTable/excel',
    autocomplete: 'autocomplete/autocomplete',
    //
    enhance: 'enhance/enhance'

}).use(['layer', 'admin'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;
    var admin = layui.admin;

});

/** 获取当前项目的根路径，通过获取layui.js全路径截取assets之前的地址 */
function getProjectUrl() {
    var layuiDir = layui.cache.dir;
    if (!layuiDir) {
        var js = document.scripts, last = js.length - 1, src;
        for (var i = last; i > 0; i--) {
            if (js[i].readyState === 'interactive') {
                src = js[i].src;
                break;
            }
        }
        var jsPath = src || js[last].src;
        layuiDir = jsPath.substring(0, jsPath.lastIndexOf('/') + 1);
    }
    return layuiDir.substring(0, layuiDir.indexOf('assets'));
}