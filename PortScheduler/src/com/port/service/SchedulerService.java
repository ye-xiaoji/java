package com.port.service;

import com.port.config.PortConfig; 
import com.port.strategy.AllocationStrategy;
import com.port.strategy.Phase1Strategy;
import com.port.strategy.Phase2Strategy;
import com.port.model.Berth;
import com.port.model.Ship;
import java.util.*;
import java.util.stream.Collectors;

public class SchedulerService {
    // 存储泊位和船舶的原始数据
    private final List<Berth> originalBerths;
    private final List<Ship> originalShips;
    private final PrintService printService;

    // 构造方法：初始化泊位和船舶数据
    public SchedulerService(List<Berth> berths, List<Ship> ships) {
        this.originalBerths = deepCopyBerths(berths);
        this.originalShips = deepCopyShips(ships);
        this.printService = new PrintService();
    }

    // 运行调度过程
    public void run() {
        // 阶段1：先到先服务调度
        AllocationStrategy phase1 = new Phase1Strategy(
            deepCopyBerths(originalBerths),
            deepCopyShips(originalShips)
        );
        AllocationStrategy.ScheduleResult phase1Result = phase1.execute();
        printService.printPhaseHeader("阶段1：先到先服务调度");
        printService.printResult(phase1Result);

        // 判断是否进行阶段2调度
        if (phase1Result.delayedCount() <= getThreshold()) {
            System.out.printf("\n延误比例%.1f%% ≤阈值%.0f%%，采用阶段1方案\n",
                phase1Result.delayedCount() * 100.0 / originalShips.size(),
                PortConfig.DELAY_THRESHOLD_RATIO * 100);
            return;
        }

        // 阶段2：优化调度
        AllocationStrategy phase2 = new Phase2Strategy(
            deepCopyBerths(originalBerths),
            deepCopyShips(originalShips),
            phase1Result
        );
        AllocationStrategy.ScheduleResult phase2Result = phase2.execute();

        // 根据阶段2的结果决定最终方案
        decideFinalResult(phase1Result, phase2Result);
    }

    // 获取延误船只数的阈值
    private double getThreshold() {
        return originalShips.size() * PortConfig.DELAY_THRESHOLD_RATIO;
    }

    // 决定最终采用哪个方案
    private void decideFinalResult(AllocationStrategy.ScheduleResult phase1,
                                  AllocationStrategy.ScheduleResult phase2) {
        boolean usePhase2 = phase2.delayedCount() < phase1.delayedCount() ||
                          (phase2.delayedCount() == phase1.delayedCount() &&
                           phase2.totalDelay() < phase1.totalDelay());

        printService.printPhaseHeader("阶段2：优化调度");
        printService.printResult(usePhase2 ? phase2 : phase1);
        printService.printComparison(phase1, phase2, usePhase2);
    }

    // 深拷贝泊位数据
    private List<Berth> deepCopyBerths(List<Berth> original) {
        return original.stream().map(Berth::new).collect(Collectors.toList());
    }

    // 深拷贝船舶数据
    private List<Ship> deepCopyShips(List<Ship> original) {
        return original.stream().map(Ship::new).collect(Collectors.toList());
    }
}
