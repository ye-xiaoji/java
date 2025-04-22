package com.port.config;

public class PortConfig {
    // 定义岸桥的效率（单位：每小时装卸箱数）
    public static final double CRANE_EFFICIENCY = 30.0;

    // 定义泊位长度的安全边际（例如：每个泊位的实际可用长度是其原始长度的95%）
    public static final double SAFETY_MARGIN = 0.05;

    // 分阶段调度的阈值比例，表示如果延误船只占比小于该阈值，则采用阶段1方案
    public static final double DELAY_THRESHOLD_RATIO = 0.25;
}
