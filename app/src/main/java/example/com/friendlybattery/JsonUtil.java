package example.com.friendlybattery;

import android.content.Context;
import android.content.ContextWrapper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wbao on 6/27/17.
 */

// Reference: https://stackoverflow.com/questions/5766609/save-internal-file-in-my-own-internal-folder-in-android

public class JsonUtil {

    private static Type fooType = new TypeToken<List<SettingEntry>>() {}.getType();

    public static void saveSetting(SettingEntry entry, Context context) {

        HashMap<String, SettingEntry> settings = getSettings(context);
        settings.put(entry.title, entry);
        List<SettingEntry> list = new ArrayList<>(settings.values());
        writeToFile(context, list);
    }

    public static SettingEntry getConfig(String title, Context context) {
        HashMap<String, SettingEntry> configs = getSettings(context);
        if (configs.containsKey(title)) {
            return configs.get(title);
        }
        return null;
    }

    public static List<SettingEntry> get3Settings(Context context) {
        HashMap<String, SettingEntry> configs = getSettings(context);
        List<SettingEntry> ret = new ArrayList<>();
        for (SettingEntry se : new ArrayList<SettingEntry>(configs.values())) {
            if (ret.size() >= 3) {
                break;
            }
            ret.add(se);
        }
        return ret;
    }

    public static void flushDb(Context context) {
        writeToFile(context, new ArrayList<SettingEntry>());
    }

    private static void writeToFile(Context context, List<SettingEntry> data) {
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File jsonFile = cw.getDir("configs", Context.MODE_PRIVATE);
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
                jsonFile.mkdirs();
            }

            Gson gson = new Gson();

            String jsonStr = gson.toJson(data, fooType);
            Log.e("test0", "test1" + jsonStr);

            FileOutputStream fos = context.openFileOutput("config.json",Context.MODE_PRIVATE);
            Writer out = new OutputStreamWriter(fos);
            out.write(jsonStr);
            out.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private static String readFromFile(Context context) {

        String json = null;

        try {
            ContextWrapper cw = new ContextWrapper(context);
            File jsonFile = cw.getDir("configs", Context.MODE_PRIVATE);
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
                jsonFile.mkdirs();
            }
            FileInputStream fis = context.openFileInput("config.json");
            BufferedReader r = new BufferedReader(new InputStreamReader(fis));
            json = r.readLine();
            r.close();

        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            return null;
        }
        return json;
    }


    private static HashMap<String, SettingEntry> getSettings(Context context) {
        String jsonStr = readFromFile(context);

        Log.e("test0", "test0" + jsonStr);

        Gson gson = new Gson();
        HashMap<String, SettingEntry> ret = new HashMap<> ();
        if (TextUtils.isEmpty(jsonStr)) {
            return ret;
        }
        ArrayList<SettingEntry> formList = gson.fromJson(jsonStr, fooType);
        for (SettingEntry se : formList) {
            ret.put(se.title, se);
        }

        return ret;
    }


}
