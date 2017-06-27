package example.com.friendlybattery;

import com.google.gson.JsonObject;

/**
 * Created by wbao on 6/27/17.
 */

class SettingEntry {
    String title;
    Boolean wifiStatu;
    Boolean bluetoothStatus;
    int screenBrightness;
    public SettingEntry(String title, Boolean wifiStatu, Boolean bluetoothStatus, int screenBrightness) {
        this.title = title;
        this.wifiStatu = wifiStatu;
        this.bluetoothStatus = bluetoothStatus;
        this.screenBrightness = screenBrightness;

    }
}