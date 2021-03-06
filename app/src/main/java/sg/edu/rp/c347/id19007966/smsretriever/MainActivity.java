package sg.edu.rp.c347.id19007966.smsretriever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment f1 = new NumberFragment();
        ft.replace(R.id.frame1, f1);

        Fragment NF2 = new WordFragment();
        ft.replace(R.id.frame2, NF2);

        ft.commit();
    }
}