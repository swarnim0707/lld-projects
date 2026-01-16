
package org.parking_lot.core;

import java.util.EnumSet;

public enum Practice { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY };

//public final class Day extends Enum<Day> {
//    public static final Day MONDAY = new Day("MONDAY", 0);
//    public static final Day TUESDAY = new Day("TUESDAY", 1);
//
//    private static final Day[] VALUES = {MONDAY, TUESDAY};
//
//    private Day(String name, int ordinal) {
//        super(name, ordinal);
//    }
//
//    public static Day[] values() {return VALUES};
//    public static Day valueOf(String day) {
//        for(int i: )
//    };
//}

class EnumSetDemo {
    public static void main(String[] args) {
        EnumSet<Practice> a = EnumSet.range(Practice.MONDAY, Practice.FRIDAY);

    }
}