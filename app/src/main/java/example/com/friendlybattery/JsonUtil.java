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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbao on 6/27/17.
 */

public class JsonUtil {

    private static Type fooType = new TypeToken<List<SettingEntry>>() {}.getType();

    public static void saveSetting(SettingEntry entry, Context context) {

        ArrayList<SettingEntry> settings = getSettings(context);
        settings.add(entry);
        writeToFile(context, settings);
    }

    public static SettingEntry getConfig(String title, Context context) {
        ArrayList<SettingEntry> configs = getSettings(context);
        for (SettingEntry se : configs) {
            if (se.title != null && se.title.equals(title)) {
                return se;
            }
        }
        return null;
    }
//https://stackoverflow.com/questions/5766609/save-internal-file-in-my-own-internal-folder-in-android
    private static void writeToFile(Context context, List<SettingEntry> data) {
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File jsonFile = cw.getDir("configs", Context.MODE_PRIVATE);
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
                jsonFile.mkdirs();
            }
            File configFile = new File(jsonFile, "config.json");

            Gson gson = new Gson();

            String jsonStr = gson.toJson(data, fooType);
            Log.e("test0", "test1" + jsonStr);



            FileOutputStream fos = context.openFileOutput("config.json",Context.MODE_PRIVATE);
            Writer out = new OutputStreamWriter(fos);
            out.write(jsonStr);
            out.close();

            //PrintWriter writer = new PrintWriter(configFile);
            //writer.print("");
            //writer.close();

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
            //File configFile = new File(jsonFile, "config.json");
            FileInputStream fis = context.openFileInput("config.json");
            BufferedReader r = new BufferedReader(new InputStreamReader(fis));
            json = r.readLine();
            r.close();
            //int size = is.available();
            //byte[] buffer = new byte[size];
            //is.read(buffer);
            //is.close();
            //json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            return null;
        }
        return json;
    }


    public static ArrayList<SettingEntry> getSettings(Context context) {


        String jsonStr = readFromFile(context);

        Log.e("test0", "test0" + jsonStr);

        Gson gson = new Gson();
        if (TextUtils.isEmpty(jsonStr)) {
            return new ArrayList<> ();
        }
        ArrayList<SettingEntry> formList = gson.fromJson(jsonStr, fooType);

        return formList;
    }


}
