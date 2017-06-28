package example.com.friendlybattery;

import android.app.Activity;
import android.os.Bundle;



public class RefreshScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        finish();
    }

}

/*
<Preference
        android:key="pref_brightness"
        android:layout="@layout/sliderlayout"
        android:id="@+id/pref_brightness" />
 */
