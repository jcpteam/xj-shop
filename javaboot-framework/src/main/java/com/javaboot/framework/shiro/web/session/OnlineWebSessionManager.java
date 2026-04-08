package com.javaboot.framework.shiro.web.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.javaboot.common.constant.ShiroConstants;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.common.utils.bean.BeanUtils;
import com.javaboot.common.utils.spring.SpringUtils;
import com.javaboot.framework.shiro.session.OnlineSession;
import com.javaboot.system.domain.SysUserOnline;
import com.javaboot.system.service.ISysUserOnlineService;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 主要是在此如果会话的属性修改了 就标识下其修改了 然后方便 OnlineSessionDao同步
 *
 * @author javaboot
 */
public class OnlineWebSessionManager extends DefaultWebSessionManager {
    private static final Logger log = LoggerFactory.getLogger(OnlineWebSessionManager.class);

    @Override
    public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) throws InvalidSessionException {
        super.setAttribute(sessionKey, attributeKey, value);
        if (value != null && needMarkAttributeChanged(attributeKey)) {
            OnlineSession session = getOnlineSession(sessionKey);
            session.markAttributeChanged();
        }
    }

    private boolean needMarkAttributeChanged(Object attributeKey) {
        if (attributeKey == null) {
            return false;
        }
        String attributeKeyStr = attributeKey.toString();
        // 优化 flash属性没必要持久化
        if (attributeKeyStr.startsWith("org.springframework")) {
            return false;
        }
        if (attributeKeyStr.startsWith("javax.servlet")) {
            return false;
        }
        if (attributeKeyStr.equals(ShiroConstants.CURRENT_USERNAME)) {
            return false;
        }
        return true;
    }

    @Override
    public Object removeAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException {
        Object removed = super.removeAttribute(sessionKey, attributeKey);
        if (removed != null) {
            OnlineSession s = getOnlineSession(sessionKey);
            s.markAttributeChanged();
        }

        return removed;
    }

    public OnlineSession getOnlineSession(SessionKey sessionKey) {
        OnlineSession session = null;
        Object obj = doGetSession(sessionKey);
        if (StringUtils.isNotNull(obj)) {
            session = new OnlineSession();
            BeanUtils.copyBeanProp(session, obj);
        }
        return session;
    }

    /**
     * 验证session是否有效 用于删除过期session
     */
    @Override
    public void validateSessions() {
        if (log.isInfoEnabled()) {
            log.info("invalidation sessions...");
        }

        int invalidCount = 0;

        int timeout = (int) this.getGlobalSessionTimeout();
        Date expiredDate = DateUtils.addMilliseconds(new Date(), 0 - timeout);
        ISysUserOnlineService userOnlineService = SpringUtils.getBean(ISysUserOnlineService.class);
        List<SysUserOnline> userOnlineList = userOnlineService.selectOnlineByExpired(expiredDate);
        // 批量过期删除
        List<String> needOfflineIdList = new ArrayList<String>();
        for (SysUserOnline userOnline : userOnlineList) {
            try {
                SessionKey key = new DefaultSessionKey(userOnline.getSessionId());
                Session session = retrieveSession(key);
                if (session != null) {
                    throw new InvalidSessionException();
                }
            } catch (InvalidSessionException e) {
                if (log.isDebugEnabled()) {
                    boolean expired = (e instanceof ExpiredSessionException);
                    String msg = "Invalidated session with id [" + userOnline.getSessionId() + "]"
                        + (expired ? " (expired)" : " (stopped)");
                    log.debug(msg);
                }
                invalidCount++;
                needOfflineIdList.add(userOnline.getSessionId());
            }

        }
        if (needOfflineIdList.size() > 0) {
            try {
                userOnlineService.batchDeleteOnline(needOfflineIdList);
            } catch (Exception e) {
                log.error("batch delete db session error." , e);
            }
        }

        if (log.isInfoEnabled()) {
            String msg = "Finished invalidation session." ;
            if (invalidCount > 0) {
                msg += " [" + invalidCount + "] sessions were stopped." ;
            } else {
                msg += " No sessions were stopped." ;
            }
            log.info(msg);
        }

    }

    @Override
    protected Collection<Session> getActiveSessions() {
        throw new UnsupportedOperationException("getActiveSessions method not supported");
    }


    //    private final String X_AUTH_TOKEN = "Access-Token";
    //
    //    @Override
    //    protected void onStart(Session session, SessionContext context) {
    //        super.onStart(session, context);
    //        if (!WebUtils.isHttp(context)) {
    //            log.debug("SessionContext argument is not HTTP compatible or does not have an HTTP request/response " +
    //                "pair. No session ID cookie will be set.");
    //            return;
    //
    //        }
    //
    //        log.error("====onStart======" + session.getId());
    //
    //        HttpServletRequest request = WebUtils.getHttpRequest(context);
    //        HttpServletResponse response = WebUtils.getHttpResponse(context);
    //        Serializable sessionId = session.getId();
    //        this.storeSessionId(sessionId, request, response);
    //        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
    //        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
    //    }
    //
    //    /**
    //     * 获取sessionid
    //     * @param key
    //     * @return
    //     */
    //    @Override
    //    public Serializable getSessionId(SessionKey key) {
    //        Serializable id = super.getSessionId(key);
    //        Serializable tempId = "";
    //
    //        ServletRequest request = WebUtils.getRequest(key);
    //        ServletResponse response = WebUtils.getResponse(key);
    //
    //        if (id == null && WebUtils.isWeb(key)) {
    //            id = getSessionId(request, response);
    //
    //            tempId = getReferencedSessionId(request, response);
    //
    //        }
    //        log.error("==========" + id);
    //        log.error("=====4444444444444=====" + tempId);
    //
    //        return id != null ? id : tempId;
    //    }
    //
    //    //    @Override
    //    //    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
    //    //        return this.getReferencedSessionId(request, response);
    //    //    }
    //
    //    /**
    //     * 请求头中的sessionid
    //     * @param request
    //     * @param response
    //     * @return
    //     */
    //    private Serializable getReferencedSessionId(ServletRequest request, ServletResponse response) {
    //        String id = this.getSessionIdHeaderValue(request, response);
    //
    //        //DefaultWebSessionManager 中代码 直接copy过来
    //        if (id != null) {
    //            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
    //            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
    //            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
    //        }
    //        //不会把sessionid放在URL后
    //        request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, Boolean.FALSE);
    //        return id;
    //    }
    //
    //    /**
    //     * 请求头中获取 sessionId 并把sessionId 放入 response 中
    //     * @param request
    //     * @param response
    //     * @return
    //     */
    //    private String getSessionIdHeaderValue(ServletRequest request, ServletResponse response) {
    //        if (!(request instanceof HttpServletRequest)) {
    //            log.debug("Current request is not an HttpServletRequest - cannot get session ID cookie.  Returning null.");
    //            return null;
    //        }
    //        else {
    //            HttpServletRequest httpRequest = (HttpServletRequest) request;
    //
    //            // 在request 中 读取 x-auth-token 信息  作为 sessionId
    //            String sessionId = httpRequest.getHeader(this.X_AUTH_TOKEN);
    //
    //            // 每次读取之后 都把当前的 sessionId 放入 response 中
    //            HttpServletResponse httpResponse = (HttpServletResponse) response;
    //
    //            if (StringUtils.isNotEmpty(sessionId)) {
    //                httpResponse.setHeader(this.X_AUTH_TOKEN, sessionId);
    //                log.info("Current session ID is {}", sessionId);
    //            }
    //
    //            return sessionId;
    //        }
    //    }
    //
    //    /**
    //     * 把sessionId 放入 response header 中
    //     * onStart时调用
    //     * 没有sessionid时 会产生sessionid 并放入 response header中
    //     */
    //    private void storeSessionId(Serializable currentId, HttpServletRequest ignored, HttpServletResponse response) {
    //        if (currentId == null) {
    //            String msg = "sessionId cannot be null when persisting for subsequent requests.";
    //            throw new IllegalArgumentException(msg);
    //        } else {
    //            String idString = currentId.toString();
    //
    //            response.setHeader(this.X_AUTH_TOKEN, idString);
    //
    //            log.info("Set session ID header for session with id {}", idString);
    //        }
    //    }
}
