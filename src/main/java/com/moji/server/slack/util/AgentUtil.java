package com.moji.server.slack.util;

import eu.bitwalker.useragentutils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By ds on 29/09/2019.
 */

public class AgentUtil {

    public static String getUserAgentString(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getUserAgentString() {
        return getUserAgentString(HttpUtil.getCurrentRequest());
    }

    public static UserAgent getUserAgent(HttpServletRequest request) {
        try {
            String userAgentString = getUserAgentString(request);
            return UserAgent.parseUserAgentString(userAgentString);
        } catch (Exception e) {
            // ignored
        }
        return null;
    }

    public static UserAgent getUserAgent() {
        return getUserAgent(HttpUtil.getCurrentRequest());
    }

    public static OperatingSystem getUserOs(HttpServletRequest request) {
        UserAgent userAgent = getUserAgent(request);
        return userAgent == null ? OperatingSystem.UNKNOWN : userAgent.getOperatingSystem();
    }

    public static OperatingSystem getUserOs() {
        return getUserOs(HttpUtil.getCurrentRequest());
    }

    public static Browser getBrowser(HttpServletRequest request) {
        UserAgent userAgent = getUserAgent(request);
        return userAgent == null ? Browser.UNKNOWN : userAgent.getBrowser();
    }

    public static Browser getBrowser() {
        return getBrowser(HttpUtil.getCurrentRequest());
    }

    public static Version getBrowserVersion(HttpServletRequest request) {
        UserAgent userAgent = getUserAgent(request);
        return userAgent == null ? new Version("0", "0", "0") : userAgent.getBrowserVersion();
    }

    public static BrowserType getBrowserType(HttpServletRequest request) {
        Browser browser = getBrowser(request);
        return browser == null ? BrowserType.UNKNOWN : browser.getBrowserType();
    }

    public static BrowserType getBrowserType() {
        return getBrowserType(HttpUtil.getCurrentRequest());
    }

    public static RenderingEngine getRenderingEngine(HttpServletRequest request) {
        Browser browser = getBrowser(request);
        return browser == null ? RenderingEngine.OTHER : browser.getRenderingEngine();
    }

    public static RenderingEngine getRenderingEngine() {
        return getRenderingEngine(HttpUtil.getCurrentRequest());
    }

    public static Version getBrowserVersion() {
        return getBrowserVersion(HttpUtil.getCurrentRequest());
    }

    public static DeviceType getDeviceType(HttpServletRequest request) {
        OperatingSystem operatingSystem = getUserOs(request);
        return operatingSystem == null ? DeviceType.UNKNOWN : operatingSystem.getDeviceType();
    }

    public static DeviceType getDeviceType() {
        return getDeviceType(HttpUtil.getCurrentRequest());
    }

    public static Manufacturer getManufacturer(HttpServletRequest request) {
        OperatingSystem operatingSystem = getUserOs(request);
        return operatingSystem == null ? Manufacturer.OTHER : operatingSystem.getManufacturer();
    }

    public static Manufacturer getManufacturer() {
        return getManufacturer(HttpUtil.getCurrentRequest());
    }

    public static Map<String, String> getAgentDetail(HttpServletRequest request) {
        Map<String, String> agentDetail = new HashMap<>();
        agentDetail.put("browser", getBrowser(request).toString());
        agentDetail.put("browserType", getBrowserType(request).toString());
        //agentDetail.put("browserVersion", getBrowserVersion(request).toString());
        agentDetail.put("renderingEngine", getRenderingEngine(request).toString());
        agentDetail.put("os", getUserOs(request).toString());
        agentDetail.put("deviceType", getDeviceType(request).toString());
        agentDetail.put("manufacturer", getManufacturer(request).toString());

        return agentDetail;
    }
}