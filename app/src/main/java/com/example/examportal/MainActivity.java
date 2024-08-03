package com.example.examportal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.examportal.ApiService.UserApiService;
import com.example.examportal.databinding.ActivityMainBinding;
import com.example.examportal.fragment.AFragment;
import com.example.examportal.fragment.AddCategoryFragment;
import com.example.examportal.fragment.AddQuizFragment;
import com.example.examportal.fragment.CategoriesFragment;
import com.example.examportal.fragment.ProfileFragment;
import com.example.examportal.fragment.QuizFragment;
import com.example.examportal.helper.CustomAlertDialog;
import com.example.examportal.helper.FetchData;
import com.example.examportal.model.User;
import com.example.examportal.retrofit.RetrofitClient;
import com.example.examportal.retrofit.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserApiService apiService;
    private SessionManager sessionManager;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView openDrawableBtn;
    private User saveUser;
    TextView user_name,uname;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new FetchData(this).fetchUserData();

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue));

        apiService = RetrofitClient.getClient().create(UserApiService.class);
        sessionManager = new SessionManager(this);


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        openDrawableBtn = findViewById(R.id.openDrawerBtn);
        View headerView = navigationView.getHeaderView(0);
        user_name = headerView.findViewById(R.id.user_name);
//        user_name=toolbar.findViewById(R.id.uname);

        setUserName();




        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        openDrawableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(navigationView);

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.home) {
                    loadFragment(new AFragment(), 0);
                } else if (id == R.id.logout) {

                    sessionManager.clearSession();
                    Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent1);
                    finish();

                } else if (id == R.id.profile) {
                    loadFragment(new ProfileFragment(), 1);
                } else if (id == R.id.categories) {

                    loadFragment(new CategoriesFragment(), 1);
                } else if (id == R.id.add_category) {
                    loadFragment(new AddCategoryFragment(), 1);

                } else if (id == R.id.quiz) {

                    Bundle bundle = new Bundle();
                    bundle.putLong("cid", 0);
                    Fragment fragment = new QuizFragment();
                    fragment.setArguments(bundle);

                    loadFragment(fragment, 1);
                } else if (id == R.id.add_quiz) {

                    Bundle bundle = new Bundle();
                    bundle.putLong("cid", 0);
                    Fragment fragment = new AddQuizFragment();
                    fragment.setArguments(bundle);
                    loadFragment(fragment, 1);
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });


    }

    private void setUserName() {
        String user = sessionManager.getUser();
        Gson gson = new Gson();
        User user1 = gson.fromJson(user, User.class);

        if (user1 != null) {
            user_name.setText(user1.firstName + " " + user1.lastName);
//            uname.setText("Welocme "+user1.firstName);

        } else {
            user_name.setText("userName");
           // uname.setText("Welocme To Quizz App");
        }
    }

    private void loadFragment(Fragment fragment, int flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag == 0) {
            ft.add(R.id.container, fragment);
            fm.popBackStack(BACK_STACK_ROOT_TAG, fm.POP_BACK_STACK_INCLUSIVE);
            ft.addToBackStack(BACK_STACK_ROOT_TAG);
        } else {
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null);

        }

        ft.commit();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

//            CustomAlertDialog alertDialog=new CustomAlertDialog(MainActivity.this,"Are yow Want to exit","Yes","No");
//            alertDialog.show();
//

        }

    }

    public void openDrawer(View view) {
        drawerLayout.openDrawer(navigationView);
    }

    @Override
    protected void onRestart() {
         setUserName();
        super.onRestart();
    }

    @Override
    protected void onStart() {
         setUserName();
        super.onStart();
    }

    @Override
    protected void onResume() {
        setUserName();
        super.onResume();
    }
}