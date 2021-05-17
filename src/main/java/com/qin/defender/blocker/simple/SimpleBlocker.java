package com.qin.defender.blocker.simple;

import com.qin.defender.blocker.AbstractBlocker;
import com.qin.defender.config.ConfigItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Sebastian
 * @date 2021/5/17
 */
public class SimpleBlocker extends AbstractBlocker {
    @Override
    protected boolean buildWhiteBlackListFromPersistence() {
        return false;
    }

    @Override
    protected boolean block(String key, int type) {
        return false;
    }

    @Override
    protected boolean blockIpBlackList(List<String> blackList) {
        return false;
    }

    @Override
    protected boolean blockUserAgentBlackList(List<String> blackList) {
        return false;
    }

    @Override
    protected boolean blockAndValidate(HttpServletRequest request, HttpServletResponse response) {
        boolean ret = false;

        int secondConut = super.couter.getSecondCount(request.getRemoteAddr());
        int minuteConut = super.couter.getMinuteCount(request.getRemoteAddr());

        for (ConfigItem.Threshold ts : config.getConfigItem().getThresholds()) {
            int countsLimit = ts.getValue();
            if (ConfigItem.Threshold.SECONDS.equals(ts.getLevel().toLowerCase())) {
                ret = secondConut >= countsLimit;
                if (ret) {
                    break;
                }
            }
            if (ConfigItem.Threshold.MINUTES.equals(ts.getLevel().toLowerCase())) {
                ret = minuteConut >= countsLimit;
                if (ret) {
                    break;
                }
            }
        }

        //返回校验页面
        if (ret) {

            try {
                //response.setContentType("text/html;charset=utf-8");
                //response.sendRedirect(request.getContextPath()+"/crawler-defender/validator?action=page");
                request.getRequestDispatcher("/crawler-defender/validator?action=page").forward(request, response);
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }
}
