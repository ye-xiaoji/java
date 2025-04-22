package com.port.strategy;

import com.port.model.Berth;
import com.port.model.Ship;
import java.util.List;

public interface AllocationStrategy {
    record ScheduleResult(
        List<Berth> berths,
        List<Ship> ships,
        int delayedCount,
        double totalDelay
    ) {}

    ScheduleResult execute();
}