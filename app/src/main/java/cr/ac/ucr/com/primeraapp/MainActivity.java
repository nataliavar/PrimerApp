package cr.ac.ucr.com.primeraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import cr.ac.ucr.com.primeraapp.Utils.AppPreferences;
import cr.ac.ucr.com.primeraapp.adapters.MainViewPagerAdapter;
import cr.ac.ucr.com.primeraapp.fragments.ToDoListFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private  ViewPager pager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Buenas");
        setSupportActionBar(toolbar);
        pager = findViewById(R.id.vp_pager);

        bottomNavigationView = findViewById(R.id.bnv_btn_menu);
        setUpViewPagerListener();
        setUpBottomNavViewListener();
        setUpViewPager();
    }

    private  void setUpViewPager(){
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(ToDoListFragment.newInstance());
        fragments.add(ToDoListFragment.newInstance());

        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragments);

        pager.setAdapter(mainViewPagerAdapter);
    }
    private void setUpBottomNavViewListener() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my_task:
                        pager.setCurrentItem(0);
                        return true;
                    case R.id.profile:
                        pager.setCurrentItem(1);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void setUpViewPagerListener() {

    }

    /*se necesita este metodo para mostrar el menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        AppPreferences.getInstance(this).clear();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
    }

}