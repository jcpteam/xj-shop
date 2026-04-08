package com.javaboot.wechat.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaboot.common.config.Global;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.utils.file.FileUploadUtils;
import com.javaboot.wechat.entity.WxImg;
import com.javaboot.wechat.entity.WxMaterial;
import com.javaboot.wechat.entity.WxMsg;
import com.javaboot.wechat.mapper.WxImgMapper;
import com.javaboot.wechat.mapper.WxMaterialMapper;
import com.javaboot.wechat.service.WxAccountService;
import com.javaboot.wechat.service.WxMsgService;
import com.javaboot.wechat.service.WxUserService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMediaImgUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialVideoInfoResult;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * 素材管理
 * @author Cracker
 */
@AllArgsConstructor
@Controller
@RequestMapping("/wechat/material")
public class WxMaterialController extends BaseController {

    private WxMpService wxMpService;
    private WxAccountService wxAccountService;
    private WxUserService wxUserService;
    private WxMsgService wxMsgService;
    private WxImgMapper wxImgMapper;
    private WxMaterialMapper wxMaterialMapper;
    private static final String prefix = "wechat/material" ;

    /**
     * 用户消息列表
     */
    @GetMapping("/list")
    public String user() {
        return prefix + "/list" ;
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(WxMaterial wxMaterial) {
        startPage();
        List<WxMaterial> list = wxMaterialMapper.selectList(new QueryWrapper<>(wxMaterial));
        return getDataTable(list);
    }

    @GetMapping("/listImg")
    @ResponseBody
    public TableDataInfo listPicture(String appid){
        startPage();
        wxMpService.switchover(appid);
        List<WxImg> list = wxImgMapper.selectList(new QueryWrapper<WxImg>().lambda().eq(WxImg::getAppid, appid));
        return getDataTable(list);
    }

    @PostMapping("/uploadImg")
    @ResponseBody
    public AjaxResult uploadImg(@RequestParam("file") MultipartFile multipartFile, String appid) throws WxErrorException, IOException {
        String upload = FileUploadUtils.upload(Global.getMaterialPath(), multipartFile);
        String realPath = Global.getProfile() + upload.replace("/profile", "");
        WxMediaImgUploadResult wxMediaImgUploadResult = wxMpService.getMaterialService().mediaImgUpload(new File(realPath));
        WxImg wxImg = new WxImg();
        wxImg.setAppid(appid);
        wxImg.setRealName(multipartFile.getOriginalFilename());
        wxImg.setSize(multipartFile.getSize());
        wxImg.setUrl(wxMediaImgUploadResult.getUrl());
        wxImg.setPath(upload);
        wxImg.setMediaId("");
        wxImgMapper.insert(wxImg);

        System.out.println("upload = " + upload);
        System.out.println("realPath = " + realPath);
        System.out.println("wxMediaImgUploadResult = " + wxMediaImgUploadResult.toString());
        return AjaxResult.success("图片上传成功!");
    }

    @PostMapping("/addMaterial")
    @ResponseBody
    public AjaxResult addMaterial(@RequestParam("file") MultipartFile multipartFile, WxMaterial wxMaterial) throws WxErrorException, IOException {
        String upload = FileUploadUtils.upload(Global.getMaterialPath(), multipartFile);
        String realPath = Global.getProfile() + upload.replace("/profile", "");
        WxMpMaterial wxMpMaterial = new WxMpMaterial();
        wxMpMaterial.setFile(new File(realPath));
        wxMpMaterial.setName(multipartFile.getOriginalFilename());
        wxMpMaterial.setVideoIntroduction(multipartFile.getOriginalFilename());
        wxMpMaterial.setVideoTitle(multipartFile.getOriginalFilename());
        WxMpMaterialUploadResult wxMpMaterialUploadResult = wxMpService.getMaterialService().materialFileUpload(wxMaterial.getType(), wxMpMaterial);

        WxMaterial newMaterial = new WxMaterial();
        newMaterial.setAppid(wxMaterial.getAppid());
        newMaterial.setFileName(multipartFile.getOriginalFilename());
        newMaterial.setFileLength(multipartFile.getSize());
        newMaterial.setFilePath(upload);
        newMaterial.setType(wxMaterial.getType());
        newMaterial.setUrl(wxMpMaterialUploadResult.getUrl());
        newMaterial.setMediaId(wxMpMaterialUploadResult.getMediaId());
        newMaterial.setCreateTime(DateUtil.toLocalDateTime(new Date()));
        newMaterial.setUpdateTime(DateUtil.toLocalDateTime(new Date()));
        wxMaterialMapper.insert(newMaterial);

        System.out.println("upload = " + upload);
        System.out.println("realPath = " + realPath);
        System.out.println("wxMediaImgUploadResult = " + wxMpMaterialUploadResult.toString());
        return AjaxResult.success("上传成功!");
    }

    @PostMapping("/downMaterial")
    @ResponseBody
    public WxMpMaterialVideoInfoResult downMaterial(WxMaterial wxMaterial) throws WxErrorException {
        WxMpMaterialVideoInfoResult wxMpMaterialVideoInfoResult = wxMpService.getMaterialService().materialVideoInfo(wxMaterial.getMediaId());
        return wxMpMaterialVideoInfoResult;
    }

    @PostMapping("/delMaterial")
    @ResponseBody
    public AjaxResult delMaterial(WxMaterial wxMaterial) throws WxErrorException {
        wxMaterialMapper.delete(new QueryWrapper<>(wxMaterial));
        boolean b = wxMpService.getMaterialService().materialDelete(wxMaterial.getMediaId());
        return AjaxResult.success("删除成功");
    }

}
