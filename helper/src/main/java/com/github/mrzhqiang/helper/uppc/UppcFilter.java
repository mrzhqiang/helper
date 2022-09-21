package com.github.mrzhqiang.helper.uppc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UppcFilter {
    private static final double FACTOR_R = 10;
    private static final int CACHE_NUM = 6;
    private static final double DISTANCE_T = 3.0;
    private static final Map<Long, List<LocationTrace>> CACHE_LOCATIONS = new HashMap<>();

    public static final double RADIUS = 1;

    /**
     * 过滤孤立点和滑动平均
     *
     * @param locationTrace
     * @return
     */
    public LocationTrace handle(LocationTrace locationTrace) {
        //获取该userId的locations如果不存在，初始化并存入
        List<LocationTrace> locationTraces = CACHE_LOCATIONS.computeIfAbsent(locationTrace.userId, k -> new ArrayList<>());
        locationTraces.add(locationTrace);
        CACHE_LOCATIONS.put(locationTrace.userId, locationTraces);
        return filter(locationTraces);
    }

    private LocationTrace filter(List<LocationTrace> locationTraces) {

        // 缓存容量未达到要求，直接返回
        if (locationTraces.size() < CACHE_NUM) {
            return null;
        }
        /* 去除孤立的点 */
        // 计算各个点到其他点的最短距离
        List<LocationTrace> locationTracesResult = new ArrayList<>();
        for (int i = 0; i < locationTraces.size(); i++) {
            LocationTrace locationTrace = locationTraces.get(i);
            double minimum_distance = 100000;
            for (int j = 0; j < locationTraces.size(); j++) {
                if (i != j) {
                    double distance = locationTrace.countDistance(locationTraces.get(j));
                    if (minimum_distance > distance) {
                        minimum_distance = distance;
                    }
                }
            }

            if (minimum_distance <= DISTANCE_T) {
                locationTracesResult.add(locationTrace);
            }
        }
        locationTraces = locationTracesResult;

        // 按奇偶性取值
        int size = locationTraces.size();

        if (size == 0) {
            return null;
        }

        int index;

        if (size % 2 == 0) {
            index = size / 2 + 1;
        } else {
            index = (size + 1) / 2;
        }
        if (index > size - 1) {
            index = size - 1;
        }

        // 排序
        Collections.sort(locationTraces, Comparator.comparing(location -> location.xyz.x));
        double x = locationTraces.get(index).xyz.x;

        Collections.sort(locationTraces, Comparator.comparing(location -> location.xyz.y));
        double y = locationTraces.get(index).xyz.y;


        LocationTrace locationTrace = locationTraces.get(index);
        locationTrace.xyz.x = x;
        locationTrace.xyz.y = y;

        locationTraces.clear();

        return locationTrace;
    }


    public LocationTrace filterRadius(LocationTrace locationTrace) {
        if (Math.hypot(locationTrace.xyz.x, locationTrace.xyz.y) > FACTOR_R * RADIUS) {
            return null;
        }
        return locationTrace;
    }

    public static class LocationTrace implements Comparable<LocationTrace> {
        public Long userId;
        public String userName;
        public String constId;
        public Long created;
        public UppcHelper.Coordinate xyz;

        public double countDistance(LocationTrace other) {
            return Math.hypot(xyz.x - other.xyz.x, xyz.y - other.xyz.y);
        }

        @Override
        public int compareTo(LocationTrace o) {
            return Double.compare(Math.hypot(xyz.x, xyz.y), Math.hypot(o.xyz.x, o.xyz.y));
        }
    }

}
