package com.javaboot.web.controller.shop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.constant.CodeConstants;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.enums.OrderPayStatus;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.domain.StoreReceipt;
import com.javaboot.shop.domain.StoreSaleSetting;
import com.javaboot.shop.dto.*;
import com.javaboot.shop.mapper.StoreSaleSettingMapper;
import com.javaboot.shop.service.IStoreSaleSettingService;
import com.javaboot.shop.vo.StoreGoodsQuotationGoodsVO;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysDeptService;
import com.javaboot.system.vo.SysDeptSelectVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品上数设置Controller
 * 
 * @author lqh
 * @date 2021-05-19
 */
@Controller
@RequestMapping("/shop/sale")
public class StoreSaleSettingController extends BaseController {
    private String prefix = "shop/sale";

    @Autowired
    private IStoreSaleSettingService storeSaleSettingService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private StoreSaleSettingMapper storeSaleSettingMapper;


    @RequiresPermissions("shop:sale:view")
    @GetMapping()
    public String sale() {
        return prefix + "/sale";
    }

    /**
     * 查询商品上数设置列表
     */
    @RequiresPermissions("shop:sale:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreSaleSetting storeSaleSetting) {
        initQryCnt(storeSaleSetting);
        startPage();
        List<StoreSaleSetting> list = storeSaleSettingService.selectStoreSaleSettingList(storeSaleSetting);
        return getDataTable(list);
    }

    /**
     * 新增商品上数设置
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存商品上数设置
     */
    @RequiresPermissions("shop:sale:add")
    @Log(title = "商品上数设置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreSaleSetting storeSaleSetting) {
        List<SysDeptSelectVO> deptList = getDeptList();
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date());
        if(hour < 9) {
            today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(new Date(), -1));
        }
        for (SysDeptSelectVO o : deptList)
        {
            storeSaleSettingService.initSaleData(o.getValue(), today);
        }
        return toAjax(deptList.size());
    }

    /**
     * 修改商品上数设置
     */
    @GetMapping("/edit/{settingId}")
    public String edit(@PathVariable("settingId") Long settingId, ModelMap mmap) {
        StoreSaleSetting storeSaleSetting = storeSaleSettingService.selectStoreSaleSettingById(settingId);
        mmap.put("storeSaleSetting", storeSaleSetting);
        mmap.put("createTime",DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, storeSaleSetting.getCreateTime()));
        return prefix + "/edit";
    }

    /**
     * 修改保存商品上数设置
     */
    @RequiresPermissions("shop:sale:edit")
    @Log(title = "商品上数设置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody StoreSaleSettingDTO dto) {
        return toAjax(storeSaleSettingService.updateStoreSaleSetting(dto));
    }

    /**
     * 删除商品上数设置
     */
    @RequiresPermissions("shop:sale:remove")
    @Log(title = "商品上数设置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeSaleSettingService.deleteStoreSaleSettingByIds(ids));
    }


    /**
     * 查询商品上数设置列表
     */
    @RequiresPermissions("shop:sale:list")
    @PostMapping("/quotationGoods")
    @ResponseBody
    public TableDataInfo quotationGoods(StoreSaleSetting storeSaleSetting) {
        //startPage();
        StoreGoodsQuotationGoodsQueryDTO dto = new StoreGoodsQuotationGoodsQueryDTO();
        dto.setDeptId(storeSaleSetting.getDeptId());
        dto.setSpuNo(storeSaleSetting.getSpuNo());
        dto.setCreateTime(storeSaleSetting.getCreateTime());
        List<StoreGoodsQuotationGoodsVO> list = storeSaleSettingService.quotationGoodsList(dto);
        // 获取订单的数量
        return getDataTable(list);
    }

    /**
     * 获取当前用户的部门列表
     * @return
     */
    private List<SysDeptSelectVO> getDeptList()
    {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        List<SysDeptSelectVO> deptList = new ArrayList<>();
        if (user.isAdmin(user.getUserId())) {
            deptList = deptService.selectDeptListById(null);
        } else {
            deptList = deptService.selectDeptListById(user.getDeptId());
        }
        return deptList;
    }


    /**
     * 修改自定义上数数量
     */
    @RequiresPermissions("shop:sale:edit")
    @PostMapping("/updateSelfQuanintiy")
    @ResponseBody
    public AjaxResult updateSelfQuanintiy(StoreSaleSetting storeSaleSetting) {
        return toAjax(storeSaleSettingService.updateSelfQuanintiy(storeSaleSetting));
    }

    /**
     * 导出商品上数设置列表
     */
    @RequiresPermissions("shop:sale:list")
    @Log(title = "导出商品上数设置列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreSaleSetting storeSaleSetting) {
        initQryCnt(storeSaleSetting);
        String today = DateUtils.getDate();
        storeSaleSetting.setStartTime(today.concat(" 00:00:00"));
        storeSaleSetting.setEndTime(today.concat(" 23:59:59"));
        List<StoreSaleSettingExportDTO> list = storeSaleSettingMapper.selectExportSaleSettingList(storeSaleSetting);
        ExcelUtil<StoreSaleSettingExportDTO> util = new ExcelUtil<>(StoreSaleSettingExportDTO.class);
        return util.exportExcel(list, "上数设置模板");
    }

    @Log(title = "导入上数设置" , businessType = BusinessType.OTHER)
    @RequestMapping("/import")
    @ResponseBody
    public AjaxResult importExcel(MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {

        ExcelUtil<StoreSaleSettingExportDTO> util = new ExcelUtil<>(StoreSaleSettingExportDTO.class);
        List<StoreSaleSettingExportDTO> saleSettingExportDTOList = util.importExcel(file.getInputStream());
        if(CollectionUtils.isNotEmpty(saleSettingExportDTOList)) {
            for (StoreSaleSettingExportDTO dto: saleSettingExportDTOList)
            {
                String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, dto.getCreateTime());
                QueryWrapper<StoreSaleSetting> tempQuery = new QueryWrapper<>();
                tempQuery.eq("dept_id", dto.getDeptId());
                tempQuery.eq("spu_no", dto.getSpuNo());
                tempQuery.eq("status", Constants.NORMAL);
                tempQuery.between("create_time", today.concat(" 00:00:00"), today.concat(" 23:59:59"));
                List<StoreSaleSetting> settingDbList = storeSaleSettingMapper.selectList(tempQuery);
                if(CollectionUtils.isNotEmpty(settingDbList)) {
                    StoreSaleSetting storeSaleSetting = settingDbList.get(0);
                    storeSaleSetting.setSelfQuanintiy(Double.parseDouble(dto.getQuanintiy()));
                    storeSaleSettingService.updateSelfQuanintiy(storeSaleSetting);
                }
            }
        }
        return AjaxResult.success("导入成功!");
    }

    private void initQryCnt(StoreSaleSetting storeSaleSetting) {
        List<String> deptIds = getDeptList().stream().map(SysDeptSelectVO::getValue).collect(Collectors.toList());
        storeSaleSetting.setDeptIdList(deptIds);

        // 默认排序
        Map<String, Object> params = new HashMap<>();
        params.put("orderByColumn","create_time");
        params.put("isAsc","desc");
        storeSaleSetting.setParams(params);
    }
}
