package com.port.model;

import com.port.config.PortConfig;

public class Berth {
    // 泊位ID
    public final int id;
    // 泊位的原始长度（例如：200米）
    public final double originLength;
    // 泊位的实际可用长度，考虑了安全边际
    public final double availableLength;
    // 泊位的水深（单位：米）
    public final double depth;
    // 该泊位最多可以分配的岸桥数量
    public final int maxCranes;
    // 泊位可以开始使用的时间
    public double availableTime;

    // 构造方法：初始化泊位信息，包括安全边际的可用长度
    public Berth(int id, double length, double depth, int maxCranes) {
        this.id = id;
        this.originLength = length;
        this.availableLength = length * (1 - PortConfig.SAFETY_MARGIN); // 考虑安全边际后的可用长度
        this.depth = depth;
        this.maxCranes = maxCranes;
        this.availableTime = 0; // 初始时，泊位可用时间为0
    }

    // 复制构造方法：创建一个新的Berth对象，保持原始泊位的属性
    public Berth(Berth other) {
        this.id = other.id;
        this.originLength = other.originLength;
        this.availableLength = other.availableLength;
        this.depth = other.depth;
        this.maxCranes = other.maxCranes;
        this.availableTime = other.availableTime;
    }
}
