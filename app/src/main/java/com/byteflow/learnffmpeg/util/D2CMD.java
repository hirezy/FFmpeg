package com.byteflow.learnffmpeg.util;


/**
 * 魔点D2常用命令
 */
public class D2CMD {
    public static int reinforceIRValue = 0;
    public static int ledValue = 0;

    public D2CMD() {

    }

    public static void init() {
        setReinforceIRValue(40);
        setIRValue(40);
       // showKeyBoard();
        D2CMD.setLed1Value(0);
        D2CMD.setLed2Value(0);
    }

    public static void unInit() {
        setReinforceIRValue(0);
        setIRValue(0);
        D2CMD.setLed1Value(0);
        D2CMD.setLed2Value(0);
    }

    //是否开启远程调试
    public static void remoteDebug(boolean isOpen){
        if (isOpen){
            CommandExecution.execCommand(new String[]{"setprop service.adb.tcp.port 5555","stop adbd","start adbd"}, true);
        }else {
            CommandExecution.execCommand(new String[]{"stop adbd"}, true);
        }
    }

    //是否开启开发者模式
    public static void openDev(boolean isOpen){
       if (isOpen){
           CommandExecution.execCommand("settings put global adb_enabled 1", true);
       }else {
           CommandExecution.execCommand("settings put global adb_enabled 0", true);
       }
    }

    //静默安装
    public static void installApk(String path){
        CommandExecution.execCommand(new String[]{"pm install -r " + path}, true);
    }

    //重启
    public static void reboot() {
        CommandExecution.execCommand("reboot", true);
    }

    //设置屏幕背光亮度
    public static void setBrightnessValue(int value) {
        CommandExecution.execCommand("echo " + value + " > /sys/class/leds/fill_light/brightness", true);
    }

    //设置红外光亮度，value：0~255（一般40就足够了）
    public static void setIRValue(int value) {
        CommandExecution.execCommand("echo  " + value + "  > /proc/leds/1", true);
    }

    //设置led灯1的亮度，value：0~255（一般40就足够了）
    public static void setLed1Value(int value) {
        CommandExecution.execCommand("echo  " + value + "  > /proc/leds/2", true);
    }

    //设置led灯2的亮度，value：0~255（一般40就足够了）
    public static void setLed2Value(int value) {
        CommandExecution.execCommand("echo  " + value + "  > /proc/leds/3", true);
    }

    //设置红外光增强，value：0~255（一般40就足够了）
    public static void setReinforceIRValue(int value) {
        CommandExecution.execCommand("echo  " + value + "  > /proc/leds/4", true);
    }

    //显示虚拟键盘
    public static void showKeyBoard() {
        CommandExecution.execCommand("am broadcast -a com.moredian.showBar", true);
    }

    //隐藏虚拟键盘
    public static void hideKeyBoard() {
        CommandExecution.execCommand("am broadcast -a com.moredian.hideBar", true);
    }

    //发送闸机开门命令
    public static void openDoor() {
        CommandExecution.execCommand("echo 1 > /proc/bonade_gpio/gpio_relay", false);
    }

    //发送闸机关门命令
    public static void closeDoor() {
        CommandExecution.execCommand("echo 0 > /proc/bonade_gpio/gpio_relay", false);
    }


    //为host模式
    public static void setHost() {
        CommandExecution.execCommand("echo 1 > /proc/gpio/otg", true);
    }

    //为device模式指令
    public static void setDevice() {
        CommandExecution.execCommand("echo 0 > /proc/gpio/otg", true);
    }



    public static void whiteLightOn() {
           CommandExecution.execCommand("echo 255 > /sys/class/leds/led2/brightness", false);
    }

    public static void whiteLightOff() {
        CommandExecution.execCommand("echo 0 > /sys/class/leds/led2/brightness", false);
    }

    public static void greenLightOn() {
        CommandExecution.execCommand("echo  255 > /sys/class/leds/led1/brightness", false);
    }

    public static void greenLightOff() {
        CommandExecution.execCommand("echo 0 > /sys/class/leds/led1/brightness", false);
    }

    public static void hideMenu() {
        CommandExecution.execCommand("settings put global policy_control immersive.status=com.bonade.face:immersive.navigation=com.bonade.face", true);
    }

    //设置白光灯亮度，value：0~255
    public static void setLightValue(int value) {
        CommandExecution.execCommand("echo  " + value + " > /sys/class/leds/led2/brightness", true);
    }

    //设置补光灯亮度，value：0~255
    public static void setFillLightValue(int value) {
        CommandExecution.execCommand("echo  " + value + " > /sys/class/leds/fill_light/brightness", true);
    }

}
