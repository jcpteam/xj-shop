package com.javaboot.quartz.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * java调用执行python脚本的命令
 */
public class CmdUtils {
    private static final Logger logger = LoggerFactory.getLogger(CmdUtils.class);


    /**
     * 通过上传视图名字来确定同步那个数据
     * @param viewName
     */
    public static void callScript(String viewName){
        try {
            String cmd = "/opt/datax/datax/bin/datax.py  /opt/datax/datax/apps/" + viewName;
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
//                System.out.println(line);
                logger.error("==命令执行结果:===" +line);
            }
            input.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
