package com.qin.defender.filter;

import com.qin.defender.blocker.AbstractBlocker;
import com.qin.defender.blocker.simple.SimpleBlocker;
import com.qin.defender.config.Config;
import com.qin.defender.counter.ICounter;
import com.qin.defender.counter.simple.SimpleCounter;
import com.qin.defender.logCache.CacheManagement;
import com.qin.defender.persistence.IPersistence;
import com.qin.defender.persistence.LogObject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author Sebastian
 * @date 2021/5/17
 */
public class GlobalFilter implements Filter {

    private IPersistence pc;

    private ICounter counter;

    private AbstractBlocker blocker;

    @Override
    public void init(FilterConfig arg0) throws ServletException {

        //解析configfile
        Config.getInstance();

        //初始化持久化容器、计数器、拦截器
        try {
            counter = SimpleCounter.getInstance();
            blocker = new SimpleBlocker();
            pc.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
                         FilterChain arg2) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse resonse = (HttpServletResponse) arg1;

        String ip = request.getRemoteAddr();

        String userAgent = request.getHeader("User-Agent");
        String url = request.getRequestURL().toString();

        //去掉参数
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }

        Date time = new Date();

        //拦截
        if (blocker.block(request, resonse)) {
            return;
        }

        //记录filter拦截日志，先写入缓存
        {
            LogObject log = new LogObject(url, ip, userAgent, time, 0);
            CacheManagement.getLogCachePool().add(log);
        }

        //写计数器
        {
            counter.secondCount(ip);
            counter.minuteCount(ip);
        }


        //向受访页面输出ajax脚本
        resonse.getWriter().print("<script> (function(){ var xmlhttp= window.XMLHttpRequest  ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'); if (xmlhttp) { xmlhttp.open('GET', '" + request.getContextPath() + "/crawler-defender/logger', true ); xmlhttp.send( null ); } })(); </script>\r\n");

        arg2.doFilter(arg0, arg1);

    }

    @Override
    public void destroy() {
        pc.destroy();
    }
}
