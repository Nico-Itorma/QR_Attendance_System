package com.nicoitorma.qrattendancesystem;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public static MainActivity instance;
    final int REQUEST_CODE = 1;
    BottomNavigationView bottom_view;
    BottomAppBar appBar;
    Fragment fragment;
    FloatingActionButton btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("QR ATTENDANCE SYSTEM");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.mainColor));
        appBar = findViewById(R.id.bottomAppBar);
        btn_scan = findViewById(R.id.btn_scan);
        bottom_view = findViewById(R.id.bottom_view);
        bottom_view.setOnNavigationItemSelectedListener(this);
        btn_scan.setOnClickListener(this);

        initUI();

        permissions();

        if (savedInstanceState == null) {
            initUI();
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
        if (requestCode == REQUEST_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                    }
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
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initUI();
            } else {
                permissions();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void initUI() {
        Fragment fragment = new Home_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
        instance = this;
        showBottomVisibility();
    }

    @Override
    public void onBackPressed() {
        initUI();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                fragment = new Home_Fragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment
                                , fragment.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.menu_settings:
                fragment = new Settings();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment
                                , fragment.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View view) {
        fragment = new QRScanner();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment
                        , fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    public void showBottomVisibility() {
        appBar.setVisibility(View.VISIBLE);
        btn_scan.setVisibility(View.VISIBLE);
    }

    public void hideBottomVisibility() {
        appBar.setVisibility(View.GONE);
        btn_scan.setVisibility(View.GONE);
    }
}
