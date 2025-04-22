package com.port;

import com.port.model.Berth;
import com.port.model.Ship;
import com.port.service.SchedulerService;
import java.util.Arrays;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        // 初始化泊位数据
        List<Berth> berths = Arrays.asList(
            new Berth(1, 200, 10, 2),
            new Berth(2, 250, 12, 3),
            new Berth(3, 300, 15, 4),
            new Berth(4, 350, 18, 5)
        );

        // 初始化船舶数据
        List<Ship> ships = Arrays.asList(
            new Ship(1, 0, 1000, 180, 9, 2),
            new Ship(2, 1, 600, 240, 11, 40),
            new Ship(3, 2, 1200, 260, 13, 80),
            new Ship(4, 3, 900, 280, 14, 50),
            new Ship(5, 4, 1200, 300, 17, 60)
        );

        // 启动调度服务
        new SchedulerService(berths, ships).run();
    }
}
