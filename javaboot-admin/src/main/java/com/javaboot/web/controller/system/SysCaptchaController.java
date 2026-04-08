package com.javaboot.web.controller.system;

import com.javaboot.common.constant.ShiroConstants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.utils.ServletUtils;
import com.javaboot.framework.config.properties.CaptchaProperties;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 图片验证码（支持算术形式）
 *
 * @author javaboot
 */
@Controller
@RequestMapping("/captcha")
public class SysCaptchaController extends BaseController {

    @Autowired
    private CaptchaProperties properties;

    /**
     * 验证码生成
     */
    @GetMapping(value = "/captchaImage")
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            // 设置请求头为输出图片类型
            response.setContentType("image/gif");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Access-Token", request.getSession().getId());
            response.setDateHeader("Expires", 0);
            logger.info("=======验证码sid==========" + request.getSession().getId());
            String type = request.getParameter("type");
            Captcha captcha = null;
            //math-算术类型 char-字符类型
            if ("math".equals(type)) {
                captcha = new ArithmeticCaptcha(properties.getWidth(), properties.getHeight(), properties.getLen());
            } else if ("char".equals(type)) {
                captcha = new GifCaptcha(properties.getWidth(), properties.getHeight(), properties.getLen());
                captcha.setCharType(properties.getCharType());
            } else {
                captcha = new ArithmeticCaptcha(properties.getWidth(), properties.getHeight(), properties.getLen());
            }
            String capText = captcha.text().toLowerCase();
            request.getSession().setAttribute(ShiroConstants.CURRENT_VALIDATECODE, capText);
            String mobile = request.getParameter("mobile");
            if(StringUtils.isNotBlank(mobile)) {
                String base64 = captcha.toBase64();
                out = response.getOutputStream();
                out.write(base64.getBytes());
            } else {

                // 输出图片流
                captcha.out(response.getOutputStream());
            }



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}