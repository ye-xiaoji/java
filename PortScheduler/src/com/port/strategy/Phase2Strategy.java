package com.port.strategy;

import com.port.config.PortConfig;
import com.port.model.Berth;
import com.port.model.Ship;
import java.util.*;
import java.util.stream.Collectors;

public class Phase2Strategy implements AllocationStrategy {
    private final List<Berth> berths;
    private final List<Ship> ships;
    private final ScheduleResult phase1Result;

    // 构造方法：初始化阶段2调度的泊位和船舶数据
    public Phase2Strategy(List<Berth> berths, List<Ship> ships, ScheduleResult phase1Result) {
        this.berths = deepCopyBerths(berths);
        this.ships = deepCopyShips(ships);
        this.phase1Result = phase1Result;
        prepare();
    }

    // 准备阶段2调度（按期望离港时间排序）
    private void prepare() {
        resetBerths();
        resetShips();
        ships.sort(Comparator.comparingDouble(s -> s.latestLeave)); // 按期望离港时间排序
    }

    @Override
    public ScheduleResult execute() {
        int delayedCount = 0;
        // 为每艘船分配泊位并计算延误时间
        for (Ship ship : ships) {
            allocateShip(ship);
            if (ship.delayTime > 1e-6) delayedCount++;
        }
        return new ScheduleResult(berths, ships, delayedCount, calculateTotalDelay());
    }

    // 为船舶分配泊位和岸桥，计算作业时间和延误时间
    private void allocateShip(Ship ship) {
        double minDelay = Double.MAX_VALUE;
        Berth selectedBerth = null;
        int selectedCranes = 0;
        double selectedFinish = 0;

        for (Berth berth : berths) {
            if (!isCompatible(ship, berth)) continue;

            // 尝试不同的岸桥数量，选取最合适的方案
            for (int cranes = berth.maxCranes; cranes >= 1; cranes--) {
                double start = Math.max(berth.availableTime, ship.eta); // 开始时间
                double duration = ship.cargo / (cranes * PortConfig.CRANE_EFFICIENCY); // 作业时长
                double finish = start + duration; // 完成时间
                double delay = Math.max(0, finish - ship.latestLeave); // 延误时间

                boolean currentDelay = delay > 1e-6;
                boolean minDelayFlag = minDelay > 1e-6;

                boolean better = false;
                if (currentDelay != minDelayFlag) {
                    better = !currentDelay;
                } else {
                    better = (delay < minDelay) || 
                           (Math.abs(delay - minDelay) < 1e-6 && finish < selectedFinish);
                }

                // 更新最优方案
                if (better) {
                    minDelay = delay;
                    selectedBerth = berth;
                    selectedCranes = cranes;
                    selectedFinish = finish;
                }
            }
        }

        // 更新船舶和泊位的状态
        if (selectedBerth != null) {
            updateAllocation(ship, selectedBerth, selectedCranes, selectedFinish, minDelay);
        }
    }

    // 检查船舶和泊位是否兼容（长度和吃水要求）
    private boolean isCompatible(Ship ship, Berth berth) {
        return ship.length <= berth.availableLength && ship.draft <= berth.depth;
    }

    // 更新船舶的分配信息
    private void updateAllocation(Ship ship, Berth berth, int cranes, 
                                 double finish, double delay) {
        berth.availableTime = finish;
        ship.assignedBerth = berth.id;
        ship.assignedCranes = cranes;
        ship.startTime = finish - (ship.cargo / (cranes * PortConfig.CRANE_EFFICIENCY));
        ship.finishTime = finish;
        ship.delayTime = delay;
    }

    // 计算总延误时间
    private double calculateTotalDelay() {
        return ships.stream().mapToDouble(s -> s.delayTime).sum();
    }

    // 重置泊位状态
    private void resetBerths() {
        berths.forEach(b -> b.availableTime = 0);
    }

    // 重置船舶状态
    private void resetShips() {
        ships.forEach(s -> {
            s.assignedBerth = -1;
            s.assignedCranes = 0;
            s.startTime = 0;
            s.finishTime = 0;
            s.delayTime = 0;
        });
    }

    // 深拷贝泊位列表
    private List<Berth> deepCopyBerths(List<Berth> original) {
        return original.stream().map(Berth::new).collect(Collectors.toList());
    }

    // 深拷贝船舶列表
    private List<Ship> deepCopyShips(List<Ship> original) {
        return original.stream().map(Ship::new).collect(Collectors.toList());
    }
}
