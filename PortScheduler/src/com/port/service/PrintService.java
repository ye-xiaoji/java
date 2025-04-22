package com.port.service;

import com.port.model.Ship;
import com.port.strategy.AllocationStrategy.ScheduleResult;

public class PrintService {
    // 打印阶段头部信息
    public void printPhaseHeader(String phase) {
        System.out.println("\n" + "=".repeat(20) + " " + phase + " " + "=".repeat(20));
    }

    // 打印调度结果
    public void printResult(ScheduleResult result) {
        System.out.println("\n分配结果（按处理顺序）：");
        System.out.println("ID | 泊位 | 岸桥 |  作业时间  | 延误 | 作业详情");
        result.ships().forEach(this::printShipDetail);
        System.out.printf("总延误时间: %.1f 小时%n", result.totalDelay());
    }

    // 打印每艘船的详细信息
    private void printShipDetail(Ship s) {
        System.out.printf("%2d | %2d | %2d | %5.1f-%5.1f | %4.1f | 船舶%d（长%.0fm）%n",
            s.id, s.assignedBerth, s.assignedCranes,
            s.startTime, s.finishTime, s.delayTime,
            s.id, s.length);
    }

    // 打印阶段比较结果
    public void printComparison(ScheduleResult phase1, ScheduleResult phase2, boolean usePhase2) {
        System.out.println("\n调度结果对比：");
        System.out.println("  原延误船只数：" + phase1.delayedCount());
        System.out.println("  现延误船只数：" + (usePhase2 ? phase2.delayedCount() : phase1.delayedCount()));
        System.out.printf("  总延误时间缩短量：%.1f 小时%n",
            phase1.totalDelay() - (usePhase2 ? phase2.totalDelay() : phase1.totalDelay()));
        
        System.out.println(usePhase2 ? 
            "  经结果分析，采用阶段2的方案。" : 
            "  经结果分析，仍采用阶段1的方案。");
    }
}
