package com.exmple.funnyvideo.MyUtils;

public class UtilCommand {

    public static String main(String args[]) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < args.length; i++) {
            if(i == (args.length -1)){
                sb.append("\"");
                sb.append(args[i]);
                sb.append("\"");
            }else{
                sb.append("\"");
                sb.append(args[i]);
                sb.append("\" ");
            }

        }
        String str = sb.toString();
        System.out.println(str);
        return str;

    }

    public static String milliSecondsToTimer(long j) {
        String str;
        String str2;
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = ((int) j2) / 60000;
        int i3 = (int) ((j2 % 60000) / 1000);
        if (i > 0) {
            str = i + ":";
        } else {
            str = "";
        }
        if (i3 < 10) {
            str2 = "0" + i3;
        } else {
            str2 = "" + i3;
        }
        return str + i2 + ":" + str2;
    }
}
