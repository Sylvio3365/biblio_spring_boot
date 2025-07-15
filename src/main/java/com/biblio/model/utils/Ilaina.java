package com.biblio.model.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Ilaina {

    public static boolean isDimanche(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public static boolean isSamedi(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY;
    }
}
