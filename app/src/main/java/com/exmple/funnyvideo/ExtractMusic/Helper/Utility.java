package com.exmple.funnyvideo.ExtractMusic.Helper;

public class Utility {

    public static boolean isFinished;

    public static long getCurrentTime() {
        return System.nanoTime() / 1000000;
    }

    public static String convertMillieToHMmSs(long j) {
        long j2 = j / 1000;
        long j3 = j2 % 60;
        long j4 = (j2 / 60) % 60;
        long j5 = (j2 / 3600) % 24;
        return j5 > 0 ? String.format("%02d:%02d:%02d", Long.valueOf(j5), Long.valueOf(j4), Long.valueOf(j3)) : String.format("%02d:%02d", Long.valueOf(j4), Long.valueOf(j3));
    }
}
