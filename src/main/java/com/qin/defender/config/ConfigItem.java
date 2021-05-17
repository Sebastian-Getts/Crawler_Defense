package com.qin.defender.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sebastian
 * @date 2021/5/17
 */
public class ConfigItem {
    private List<Threshold> thresholds = new ArrayList<>(4);
    private List<String> ipWhiteList = new ArrayList<>();
    private List<String> ipBlackList = new ArrayList<>();
    private List<String> userAgentWhiteList = new ArrayList<>();
    private List<String> userAgentBlackList = new ArrayList<>();
    private String validatorPath = "/crawler-defender/validator-page";
    private boolean botTrap = false;

    public List<Threshold> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<Threshold> thresholds) {
        this.thresholds = thresholds;
    }

    public List<String> getIpWhiteList() {
        return ipWhiteList;
    }

    public void setIpWhiteList(List<String> ipWhiteList) {
        this.ipWhiteList = ipWhiteList;
    }

    public List<String> getIpBlackList() {
        return ipBlackList;
    }

    public void setIpBlackList(List<String> ipBlackList) {
        this.ipBlackList = ipBlackList;
    }

    public List<String> getUserAgentWhiteList() {
        return userAgentWhiteList;
    }

    public void setUserAgentWhiteList(List<String> userAgentWhiteList) {
        this.userAgentWhiteList = userAgentWhiteList;
    }

    public List<String> getUserAgentBlackList() {
        return userAgentBlackList;
    }

    public void setUserAgentBlackList(List<String> userAgentBlackList) {
        this.userAgentBlackList = userAgentBlackList;
    }

    public String getValidatorPath() {
        return validatorPath;
    }

    public void setValidatorPath(String validatorPath) {
        this.validatorPath = validatorPath;
    }

    public boolean isBotTrap() {
        return botTrap;
    }

    public void setBotTrap(boolean botTrap) {
        this.botTrap = botTrap;
    }

    public static class Threshold {

        public static final String SECONDS = "seconds";
        public static final String MINUTES = "minutes";
        public static final String HOURS = "hours";
        public static final String DAYS = "days";


        private String level;
        private int duration;
        private int value;

        public Threshold(String level, int duration, int value) {
            this.level = level;
            this.duration = duration;
            this.value = value;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}
