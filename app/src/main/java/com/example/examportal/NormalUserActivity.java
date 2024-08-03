package com.example.examportal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
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

import com.example.examportal.databinding.ActivityNormalUserBinding;
import com.example.examportal.fragment.AddQuizFragment;
import com.example.examportal.fragment.normalUserFragment.NormalAllQuizzesFragment;
import com.example.examportal.fragment.normalUserFragment.NormalUserCategoriesFragment;
import com.example.examportal.fragment.normalUserFragment.NormalUserProfileFragment;
import com.example.examportal.helper.CustomAlertDialog;
import com.example.examportal.model.User;
import com.example.examportal.retrofit.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

public class NormalUserActivity extends AppCompatActivity {

    private ActivityNormalUserBinding binding;

    private SessionManager sessionManager;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView openDrawableBtn;
    private User saveUser;
    TextView user_name;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNormalUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue));


        drawerLayout = findViewById(R.id.normal_drawerLayout);
        navigationView = findViewById(R.id.normal_nav_view);
        toolbar = findViewById(R.id.toolbar);
        openDrawableBtn = findViewById(R.id.openDrawerBtn);
       View headerView = navigationView.getHeaderView(0);
       user_name = headerView.findViewById(R.id.NormalName);

          sessionManager=new SessionManager(NormalUserActivity.this);
          String user = sessionManager.getUser();
         Gson gson=new Gson();
         User user1 = gson.fromJson(user, User.class);

         if (user1!=null)
         {
             user_name.setText(user1.getFirstName()+" "+user1.getLastName());
         }
         else {
             user_name.setText("User Name");
         }


        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(NormalUserActivity.this,
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

                int id=item.getItemId();

                if (id == R.id.normalProfile)
                {
                    loadFragment(new NormalUserProfileFragment(),1);

                } else if (id==R.id.normalAllQuiz) {

                    Bundle bundle = new Bundle();
                    bundle.putLong("cid1", 0);
                    Fragment fragment = new NormalAllQuizzesFragment();
                    fragment.setArguments(bundle);
                    loadFragment(fragment,1);

                } else if (id==R.id.normalCategories) {
                    loadFragment(new NormalUserCategoriesFragment(),1);
                } else if (id==R.id.normalLogout) {
                     sessionManager=new SessionManager(NormalUserActivity.this);
                    sessionManager.clearSession();
                    Intent intent=new Intent(NormalUserActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

   private void loadFragment(Fragment fragment,int flag)
   {
       FragmentManager fm=getSupportFragmentManager();
       FragmentTransaction ft=fm.beginTransaction();
       if (flag == 1) {
           ft.replace(R.id.container, fragment);
           fm.popBackStack(BACK_STACK_ROOT_TAG, fm.POP_BACK_STACK_INCLUSIVE);
           ft.addToBackStack(BACK_STACK_ROOT_TAG);
       } else {

           ft.addToBackStack(null);
       }

       ft.commit();

   }

    public void openDrawer(View view) {
        drawerLayout.openDrawer(navigationView);
    }



}