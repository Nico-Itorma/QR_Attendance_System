package com.nicoitorma.qrattendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/*
    MAIN ACTIVITY WHERE EVERYTHING IS HANDLED
 */
public class MainActivity extends AppCompatActivity {


    DrawerLayout drawer;
    NavigationView navView;
    Toolbar toolbar;
    public static MainActivity instance;
    final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        permissions();

        if (savedInstanceState == null) {
            navView.setCheckedItem(R.id.menu_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home_Fragment()).commit();
        }
    }

    private void permissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            initUI();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE)
        {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                    } else if (WRITE_EXTERNAL_STORAGE.equals(permission)) {
                        new AlertDialog.Builder(getApplicationContext()).setTitle("PERMISSION DENIED")
                                .setMessage("This allow user to save generated QR to device local storage")
                                .setPositiveButton("Ok", (dialogInterface, i1) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE))
                                .setNegativeButton("Cancel", null)
                                .create()
                                .show();
                    }
                }
            }
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initUI();
            }
            else {
                permissions();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void initUI() {

        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);

        String home_title = "Home";
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(home_title);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //click listener for home, qr generator, database, attendance, settings
        navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home_Fragment()).commit();
                    navView.setCheckedItem(R.id.menu_home);
                    toolbar.setTitle(home_title);
                    break;
                case R.id.menu_db:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DB_Fragment()).commit();
                    navView.setCheckedItem(R.id.menu_db);
                    toolbar.setTitle("QR Database");
                    break;
                case R.id.menu_att:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Attendance_Fragment()).commit();
                    navView.setCheckedItem(R.id.menu_att);
                    toolbar.setTitle("Attendance Report");
                    break;
                case R.id.menu_generate:
                    MainActivity.instance.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GenQr()).commit();
                    navView.setCheckedItem(R.id.menu_generate);
                    toolbar.setTitle("QR Generator");
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
        instance = this;
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home_Fragment()).commit();
        navView.setCheckedItem(R.id.menu_home);
    }
}