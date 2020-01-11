package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.FuWaiHelper;


/**
 * 领取邮件
 */
public class YamenTaskElement extends AbsTaskElement {
    public YamenTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() throws Exception {
        pageData = Util.getBitmapAndPageData();

        if (checkExp(netPoint, "当前网络异常")) return false;//检查网络环境

        if (checkPage("府内")) {
            PointManagerV2.execShellCmdChuFuV2();
            Thread.sleep(1800);
            return false;
        } else if (checkPage("府外")) {
            swipeToRight();
            while (true){
                Util.getCapBitmapNew();
                if (Util.checkColorAndClick(FuWaiHelper.yaMen)) {
                    Thread.sleep(1800);
                    PointManagerV2.execShellCmdChuFuV2();
                    Thread.sleep(1800);
                    PointManagerV2.execShellCmdChuFuV2();
                    return true;
                } else if (check(4)) {
                    return true;
                }
                Thread.sleep(800);
            }
        }
        return true;
    }
}