package com.javaboot.web.controller.shop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.*;
import com.javaboot.shop.service.*;
import com.javaboot.shop.vo.SpecVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 商品主Controller
 *
 * @author javaboot
 * @date 2019-08-25
 */
@Controller
@RequestMapping("/shop/goods")
public class StoreGoodsController extends BaseController {
    private String prefix = "shop/goods";

    @Autowired
    private IStoreGoodsService storeGoodsService;

    @Autowired
    private IStoreGoodsTypeService storeGoodsTypeService;

    @Autowired
    private IStoreSpecService storeSpecService;

    @Autowired
    private IStoreSpecItemService storeSpecItemService;

    @Autowired
    private IStoreSpecGoodsPriceService storeSpecGoodsPriceService;

    @RequiresPermissions("shop:goods:view")
    @GetMapping()
    public String goods() {
        return prefix + "/goods";
    }

    /**
     * 查询商品主列表
     */
    @RequiresPermissions("shop:goods:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoods storeGoods) {
        startPage();
        List<StoreGoods> list = storeGoodsService.selectStoreGoodsList(storeGoods);
        return getDataTable(list);
    }

    /**
     * 导出商品主列表
     */
    @RequiresPermissions("shop:goods:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoods storeGoods) {
        List<StoreGoods> list = storeGoodsService.selectStoreGoodsList(storeGoods);
        ExcelUtil<StoreGoods> util = new ExcelUtil<StoreGoods>(StoreGoods.class);
        return util.exportExcel(list, "goods");
    }

    /**
     * 新增商品
     */
    @GetMapping("/add")
    public String add(Model model) {
        return prefix + "/add";
    }

    /**
     * 新增保存商品主
     */
    @RequiresPermissions("shop:goods:add")
    @Log(title = "商品主", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreGoods storeGoods) {
        return toAjax(storeGoodsService.insertStoreGoods(storeGoods));
    }

    /**
     * @param goodsId
     * @param typeId
     * @param mmap
     * @return
     */
    @GetMapping("/editSpec/{goodsId}/{typeId}")
    public String editSpecAttr(@PathVariable("goodsId") Integer goodsId,
                               @PathVariable("typeId") Integer typeId,
                               ModelMap mmap) {

        StoreGoods storeGoods = storeGoodsService.selectStoreGoodsById(goodsId);

        List<StoreGoodsType> typeList = storeGoodsTypeService.selectStoreGoodsTypeList(null);
        mmap.put("storeGoods", storeGoods);
        mmap.put("goodsTypes", typeList);
        return prefix + "/spec";

    }

    /**
     * @param goodsId
     * @param typeId
     * @return
     */
    @PostMapping("/ajaxSpec")
    @ResponseBody
    public AjaxResult ajaxSpec(@RequestParam(value = "goods_id", defaultValue = "-1") Integer goodsId,
                               @RequestParam(value = "type_id", defaultValue = "0") Integer typeId) {

        if (typeId == 0) {
            return AjaxResult.success("success", "");
        }
        StoreSpec storeSpec = new StoreSpec();
        storeSpec.setTypeId(typeId);
        List<StoreSpec> specs = storeSpecService.selectStoreSpecList(storeSpec);
        List<StoreSpecItem> specItems = null;
        StoreSpecItem specItem = new StoreSpecItem();
        for (StoreSpec spec : specs) {
            specItem.setSpecId(spec.getId());
            specItems = storeSpecItemService.selectStoreSpecItemList(specItem);
            if (goodsId > 0) {
                String itemStr = storeGoodsService.getGoodsItems(goodsId);
                if (StringUtils.isNotEmpty(itemStr)) {
                    String[] strs = itemStr.split("_");
                    for (StoreSpecItem item : specItems) {
                        if (Arrays.asList(strs).contains(item.getId().toString())) {
                            item.setSelected(true);
                        }
                    }
                }

            }

            spec.setStoreSpecItems(specItems);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("data", specs);
        return AjaxResult.success("success", map);

    }


    @PostMapping("/ajaxGetSpecInput")
    @ResponseBody
    public AjaxResult ajaxGetSpecInput(@RequestBody String jsonStr) {

        SpecVO data = JSON.parseObject(jsonStr, SpecVO.class);

        Map<String, List<Integer>> map2 = new HashMap<>();
        //重新组合map有相同键值的组合成list
        for (String str : data.getList()) {
            Map<String, Object> map = (Map<String, Object>) JSONObject.parseObject(str);
            String newKey = map.get("specId").toString();
            String newValue = map.get("itemId").toString();
            List<Integer> tmpList = new ArrayList<>();
            if (map2.containsKey(newKey)) {
                tmpList = map2.get(newKey);
                tmpList.add(Integer.parseInt(newValue));
                map2.put(newKey, tmpList);

            } else {
                tmpList.add(Integer.parseInt(newValue));
                map2.put(newKey, tmpList);
            }

        }
        Iterator<Map.Entry<String, List<Integer>>> it = map2.entrySet().iterator();

        Map<String, Integer> unsortMap =  new HashMap<String, Integer>();

        //迭代map把原来值换成列表大小
        while (it.hasNext()) {
            Map.Entry<String, List<Integer>> entry = it.next();
            unsortMap.put(entry.getKey(), entry.getValue().size());
        }

        //定义一个有序的列表map
        Map<String, Integer> sortMap = new LinkedHashMap<>();

        //升序排序
        Stream<Map.Entry<String, Integer>> st = unsortMap.entrySet().stream();
        st.sorted(Comparator.comparing(e -> e.getKey()))
                .forEachOrdered(e -> sortMap.put(e.getKey(), e.getValue()));


        //根据排序后组合最终的MAP
        Map<String, List<Integer>> finalMap = new LinkedHashMap<>();
        for (String key : sortMap.keySet()) {
            finalMap.put(key, map2.get(key));
        }


        //列表笛卡儿积
        ArrayList ls = new ArrayList();
        for (List<Integer> value : finalMap.values()) {
            ls.add(value);
        }
        if (ls.isEmpty()){
            return AjaxResult.success("success", "");
        }
        List Dlist = StringUtils.Dikaerji0(ls);


        //todo 组合数据
        String str = "<table class='layui-table' id='spec_input_tab'><tr>";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);

        for (String key : finalMap.keySet()) {
            StoreSpec storeSpec = storeSpecService.selectStoreSpecById(Integer.valueOf(key));
            stringBuilder.append(MessageFormat.format("<td><b>{0}</b></td>", storeSpec.getName()));
        }
        stringBuilder.append("<td><b>本店价</b></td><td><b>市场价</b></td><td><b>授权天数</b></td><td><b>操作</b></td></tr>");


        //获取商品规格列表
        StoreSpecGoodsPrice storeSpecGoodsPrice = new StoreSpecGoodsPrice();
        storeSpecGoodsPrice.setGoodsId(Integer.valueOf(data.getGoodsId()));

        //开始迭代笛卡儿积
        Iterator<ArrayList> iter = Dlist.iterator();
        while (iter.hasNext()) {
            List<String> itemName = new ArrayList<>();
            stringBuilder.append("<tr>");
            List<Integer> iterList = iter.next();
            Collections.sort(iterList);
            iterList.forEach(item -> {
                StoreSpecItem storeSpecItem = storeSpecItemService.selectStoreSpecItemById(Integer.valueOf(item.toString()));
                stringBuilder.append(MessageFormat.format("<td>{0}</td>", storeSpecItem.getItem()));
                StoreSpec storeSpec2 = storeSpecService.selectStoreSpecById(Integer.valueOf(storeSpecItem.getSpecId()));

                itemName.add(storeSpec2.getName() + ":" + storeSpecItem.getItem());
            });

            String itemKeyStr = org.apache.commons.lang.StringUtils.join(iterList.toArray(), "_");
            String itemNameStr = org.apache.commons.lang.StringUtils.join(itemName.toArray(), ",");

            double keyPrice = 0;
            double keyMarketPrice = 0;
            int keyStoreCount = 0;

            //获取商品规格价格列表
            storeSpecGoodsPrice.setKey(itemKeyStr);
            List<StoreSpecGoodsPrice> storeSpecGoodsPrices = storeSpecGoodsPriceService.selectStoreSpecGoodsPriceList(storeSpecGoodsPrice);


            if (!storeSpecGoodsPrices.isEmpty()) {
                StoreSpecGoodsPrice goodsPrice = storeSpecGoodsPrices.get(0);
                if (goodsPrice != null) {
                    keyPrice = goodsPrice.getPrice();
                    keyMarketPrice = goodsPrice.getMarketPrice();
                    keyStoreCount = goodsPrice.getStoreCount().intValue();
                }
            }

            stringBuilder.append(MessageFormat.format("<td><input name=item[{0}][price] value={1} type=\"text\" class=\"layui-input\"/></td>",
                    itemKeyStr, String.valueOf(keyPrice)));
            stringBuilder.append(MessageFormat.format("<td><input name=item[{0}][market_price] value={1} type=\"text\" class=\"layui-input\"/></td>",
                    itemKeyStr, String.valueOf(keyMarketPrice)));
            stringBuilder.append(MessageFormat.format("<td><input name=item[{0}][store_count] value={1} type=\"text\" class=\"layui-input\"/></td>",
                    itemKeyStr, String.valueOf(keyStoreCount)));
            stringBuilder.append(MessageFormat.format("<td>无<input type='hidden' name=item[{0}][key_name] value={1} type=\"text\" class=\"layui-input\"/></td>",
                    itemKeyStr, itemNameStr));
            stringBuilder.append("</tr>");

        }
        stringBuilder.append("</table>");

        return AjaxResult.success("success", stringBuilder.toString());
    }

    @PostMapping("/saveSpecAttr")
    @ResponseBody
    public AjaxResult saveSpecAttr(HttpServletRequest request) {
        //获取所有表单参数
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Map<String, String>> specDataMap = new HashMap<>();
        Map<String, String> attrDataMap = new HashMap<>();
        Map<String, String> tmpMap = null;
        String goodsId = request.getParameter("goodsId");
        String goodsType = request.getParameter("type_id");
        if (StringUtils.isEmpty(goodsId) || Integer.valueOf(goodsId) == 0) {
            return AjaxResult.error("参数有误");
        }
        if (StringUtils.isEmpty(goodsType) || Integer.valueOf(goodsType) == 0) {
            return AjaxResult.error("请选择模型");
        }
        //过滤参数
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String[] values = request.getParameterValues(name);
            if (values != null && values.length > 0) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < values.length; i++) {
                    builder.append(values[i] + " ");
                }
                //过滤规格组合新的规格数据集合
                if (name.contains("item")) {
                    String pattern = "item\\[(\\d+[_\\d+]*)]\\[([a-z]+_?[a-z]+)]";
                    //String pattern = "item\\[(\\d+_\\d+)]\\[([a-z]+_?[a-z]+)]";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(name);
                    if (m.find()) {
                        String keyOne = m.group(1).trim();
                        System.out.println("keyOne:" + keyOne);
                        String keyTwo = m.group(2).trim();
                        System.out.println("keyTwo:" + keyTwo);
                        String keyValue = builder.toString().trim();

                        if (!specDataMap.containsKey(keyOne)) {
                            tmpMap = new HashMap<>();
                        }
                        tmpMap.put(keyTwo, keyValue);
                        specDataMap.put(keyOne, tmpMap);

                    }
                }

                //过滤属性组合新的属性集合
                if (name.contains("attr_")) {
                    String pattern2 = "attr_(\\d+)\\[0]";
                    Pattern r2 = Pattern.compile(pattern2);
                    Matcher m2 = r2.matcher(name);
                    //System.out.println(name+" : "+builder.toString());
                    if (m2.find()) {
                        attrDataMap.put(m2.group(1), builder.toString().trim());
                    } else {
                        System.out.println("no");
                    }

                }
            }
        }


        if (specDataMap.isEmpty()) {
            return AjaxResult.error("请选择规格项目");
        }

        storeGoodsService.saveSpecAttr(specDataMap, attrDataMap, goodsId, goodsType);

        return AjaxResult.success("操作成功");
    }


    /**
     * 修改商品主
     */
    @GetMapping("/edit/{goodsId}")
    public String edit(@PathVariable("goodsId") Integer goodsId, ModelMap mmap) {
        StoreGoods storeGoods = storeGoodsService.selectStoreGoodsById(goodsId);
        mmap.put("storeGoods", storeGoods);

        return prefix + "/edit";
    }

    /**
     * 修改保存商品主
     */
    @RequiresPermissions("shop:goods:edit")
    @Log(title = "商品主", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreGoods storeGoods) {
        return toAjax(storeGoodsService.updateStoreGoods(storeGoods));
    }

    /**
     * 删除商品主
     */
    @RequiresPermissions("shop:goods:remove")
    @Log(title = "商品主", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeGoodsService.deleteStoreGoodsByIds(ids));
    }

    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(StoreGoods storeGoods) {
        return toAjax(storeGoodsService.updateStoreGoods(storeGoods));
    }
}
