package example.com.friendlybattery;


import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;


public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        Preference prefBrightness = (Preference) getPreferenceScreen().findPreference("pref_brightness");

        LayoutInflater inflater = LayoutInflater.from(this);
        View view =inflater.inflate(R.layout.sliderlayout, null);
        // may be logo is a layout file, if not then put your layout file which contain an ImageView
        SeekBar brightnessSetting = (SeekBar)view.findViewById(R.id.lightness_setting);


        CheckBoxPreference bluetoothSetting = (CheckBoxPreference) getPreferenceScreen().findPreference("bluetooth_setting");
        CheckBoxPreference wifi_setting = (CheckBoxPreference) getPreferenceScreen().findPreference("wifi_setting");

        Preference button = findPreference("save_button");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                return true;
            }
        });

        brightnessSetting.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
