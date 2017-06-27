package example.com.friendlybattery;


import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        Preference prefBrightness = (Preference) getPreferenceScreen().findPreference("pref_brightness");

        LayoutInflater inflater = LayoutInflater.from(this);
        //View view =getLayoutInflater().inflate(R.layout.sliderlayout, this);
        // may be logo is a layout file, if not then put your layout file which contain an ImageView
        //SeekBar brightnessSetting = (SeekBar)view.findViewById(R.id.lightness_setting);
        //final TextView seekBarStatus = (TextView)view.findViewById(R.id.seekBarStatus);

        final EditTextPreference titleSetting = (EditTextPreference) getPreferenceScreen().findPreference("theme_title");
        final CheckBoxPreference bluetoothSetting = (CheckBoxPreference) getPreferenceScreen().findPreference("bluetooth_setting");
        final CheckBoxPreference wifi_setting = (CheckBoxPreference) getPreferenceScreen().findPreference("wifi_setting");


        titleSetting.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                titleSetting.setTitle("Theme title: " + (String) newValue);
                return true;
            }
        });

        Preference button = findPreference("save_button");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                JsonUtil.saveSetting(new SettingEntry(titleSetting.getText(), bluetoothSetting.isChecked(), wifi_setting.isChecked(), 5), SettingsActivity.this);
                Toast.makeText(getApplicationContext(), "Stored", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //brightnessSetting.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        //    int progress = 0;
//
        //    @Override
        //    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
        //        progress = progresValue;
        //        Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
        //    }
//
        //    @Override
        //    public void onStartTrackingTouch(SeekBar seekBar) {
        //        Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
        //    }
//
        //    @Override
        //    public void onStopTrackingTouch(SeekBar seekBar) {
        //        seekBarStatus.setText("Covered: " + progress + "/" + seekBar.getMax());
        //        Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
        //    }
        //});
    }

}

/*
<Preference
        android:key="pref_brightness"
        android:layout="@layout/sliderlayout"
        android:id="@+id/pref_brightness" />
 */
