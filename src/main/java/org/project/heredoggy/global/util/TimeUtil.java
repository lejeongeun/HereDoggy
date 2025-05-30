package org.project.heredoggy.global.util;

import java.time.LocalTime;

public class TimeUtil {
    public static boolean isMorning(LocalTime time){
        return !time.isBefore(LocalTime.of(10, 0)) && time.isBefore(LocalTime.of(12, 0));
    }
    public static boolean isAfternoon(LocalTime time){
        return !time.isBefore(LocalTime.of(14, 0)) && time.isBefore(LocalTime.of(17, 0));
    }
}
