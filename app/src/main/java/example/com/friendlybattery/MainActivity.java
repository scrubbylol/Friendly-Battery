package example.com.friendlybattery;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Friendly Battery");

        // Go to settings
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                myIntent.putExtra("key", 0); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });

        // Set predefined battery saving options
        final ListView list = (ListView) findViewById(R.id.my_list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Log", String.valueOf(position));
                showConfirmDialog(list, position);
            }
        });

        list.post(new Runnable() {
            @Override
            public void run() {
                Boolean[] custom = checkCustomProfiles(list);
                for (int i = 0; i < 3 ; i++) {
                    if (!custom[i]) {
                        list.getChildAt(i+3).setEnabled(false);
                        list.getChildAt(i+3).setOnClickListener(null);
                    }
                }
            }
        });

        // Revert button
        Button revert = (Button) findViewById(R.id.revert_btn);
        revert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revertDialog(v);
            }
        });
    }

    @Override
    public void onResume() {
        Log.e("testRandy", "onResume");
        super.onResume();

        final ListView list = (ListView) findViewById(R.id.my_list);

        list.post(new Runnable() {
            @Override
            public void run() {
                Boolean[] custom = checkCustomProfiles(list);
                for (int i = 0; i < 3 ; i++) {
                    if (custom[i]) {
                        if (list.getChildAt(i + 3) != null) {
                            list.getChildAt(i + 3).setEnabled(true);
                            final int finalI = i;
                            list.getChildAt(i + 3).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("Log", String.valueOf(finalI + 3));
                                    showConfirmDialog(list, finalI + 3);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public Boolean[] checkCustomProfiles(ListView l) {
        Boolean[] check = new Boolean[3];
        Arrays.fill(check, false);
        int i =0;

        List<SettingEntry> settings = JsonUtil.get3Settings(this);
        for(SettingEntry se : settings) {
            check[i] = true;
            TextView t = (TextView) l.getChildAt(i+3);
            t.setText(se.title);

            i ++;
        }
        return check;
    }

    // Revert settings dialog
    public void revertDialog(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        JsonUtil.flushDb(MainActivity.this);
                        final ListView list = (ListView) findViewById(R.id.my_list);

                        Boolean[] custom = checkCustomProfiles(list);
                        for (int i = 0; i < 3 ; i++) {
                            if (!custom[i]) {
                                if (list.getChildAt(i + 3) != null) {
                                    list.getChildAt(i + 3).setEnabled(false);
                                    list.getChildAt(i + 3).setOnClickListener(null);
                                    TextView t = (TextView) list.getChildAt(i+3);
                                    int pOne = i + 1;
                                    t.setText("Custom Profile " + pOne);
                                }
                            }
                        }

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Revert settings back to your old settings?").setPositiveButton("Yes", dialogClickListener)
                .setTitle("Are you sure?")
                .setNegativeButton("No", dialogClickListener).show();
    }

    // Confirm action dialog
    public void showConfirmDialog(ListView l, final int pos) {
        // Pos 0 - Texting
        // Pos 1 - Browsing Web
        // Pos 2 - Capture Photos
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        getDeviceSettings(pos);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        String type = "";
        type = l.getItemAtPosition(pos).toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Maximize battery saving for " + type).setPositiveButton("Yes", dialogClickListener)
                .setTitle("Are you sure?")
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void toggleWifi(boolean tog) {
        WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (tog) {
            wifi.setWifiEnabled(true);
        } else {
            wifi.setWifiEnabled(false);
        }
    }

    public void toggleBluetooth(boolean tog) {
        BluetoothAdapter blue = BluetoothAdapter.getDefaultAdapter();
        if (tog) {
            blue.enable();
        } else {
            blue.disable();
        }
    }

    public void setScreenBrightness(float bright) {
        if (Settings.System.canWrite(getApplicationContext())) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.screenBrightness = bright;
            getWindow().setAttributes(lp);
        }
    }

    public void getDeviceSettings(int pos) {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(MainActivity.CONNECTIVITY_SERVICE);

        // Access device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);
            }
        }

        // Pos 0 - Texting
        // Pos 1 - Browsing Web
        // Pos 2 - Capture Photos
        if (pos == 0) {
            toggleWifi(false);
            toggleBluetooth(false);
            setScreenBrightness(0);
        }
        else if (pos == 1) {
            toggleBluetooth(false);
            setScreenBrightness(0);
        }
        else if (pos == 2) {
            toggleWifi(false);
            toggleBluetooth(false);
            setScreenBrightness(25);
        }
        else {
            List<SettingEntry> settings = JsonUtil.get3Settings(this);
            for(SettingEntry se : settings) {
                if (se.bluetoothStatus) {
                    toggleBluetooth(true);
                } else {
                    toggleBluetooth(false);
                }

                if (se.wifiStatu) {
                    toggleWifi(true);
                } else {
                    toggleWifi(false);
                }

                setScreenBrightness(se.screenBrightness);
            }
        }
    }
}