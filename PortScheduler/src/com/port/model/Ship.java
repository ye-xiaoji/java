package com.port.model;

public class Ship {
    // 船舶ID
    public final int id;
    // 预计到港时间
    public final double eta;
    // 货物量（单位：箱数）
    public final double cargo;
    // 船舶长度
    public final double length;
    // 船舶吃水（单位：米）
    public final double draft;
    // 期望离港时间
    public final double latestLeave;
    
    // 分配的泊位ID
    public int assignedBerth = -1;
    // 分配的岸桥数量
    public int assignedCranes = 0;
    // 开始作业的时间
    public double startTime = 0;
    // 完成作业的时间
    public double finishTime = 0;
    // 延误时间
    public double delayTime = 0;

    // 构造方法：初始化船舶的基本信息
    public Ship(int id, double eta, double cargo, double length, 
               double draft, double latestLeave) {
        this.id = id;
        this.eta = eta;
        this.cargo = cargo;
        this.length = length;
        this.draft = draft;
        this.latestLeave = latestLeave;
    }

    // 复制构造方法：创建一个新的Ship对象，保持原始船舶的属性
    public Ship(Ship other) {
        this.id = other.id;
        this.eta = other.eta;
        this.cargo = other.cargo;
        this.length = other.length;
        this.draft = other.draft;
        this.latestLeave = other.latestLeave;
        this.assignedBerth = other.assignedBerth;
        this.assignedCranes = other.assignedCranes;
        this.startTime = other.startTime;
        this.finishTime = other.finishTime;
        this.delayTime = other.delayTime;
    }
}
