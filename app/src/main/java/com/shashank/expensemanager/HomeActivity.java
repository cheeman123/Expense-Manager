package com.shashank.expensemanager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.expensemanager.activities.MainActivity;
import com.shashank.expensemanager.adapters.CustomAdapter;
import com.shashank.expensemanager.fragments.CustomBottomSheetDialogFragment;
import com.shashank.expensemanager.transactionDb.AppDatabase;
import com.shashank.expensemanager.transactionDb.AppExecutors;
import com.shashank.expensemanager.transactionDb.TransactionEntry;
import com.shashank.expensemanager.transactionDb.TransactionViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static FloatingActionButton fab;
    private List<TransactionEntry> transactionEntries;
    public TransactionViewModel transactionViewModel;
    private CustomAdapter customAdapter;
    DonutProgress donutProgress;
    private AppDatabase mAppDb;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar ) ;
        setSupportActionBar(toolbar) ;



        mAuth = FirebaseAuth.getInstance();

        transactionEntries = new ArrayList<>();
        donutProgress = findViewById(R.id.donut);

        mAppDb = AppDatabase.getInstance(this);



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomBottomSheetDialogFragment().show(getSupportFragmentManager(), "Dialog");

            }
        });


     /*   new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                // COMPLETED (1) Get the diskIO Executor from the instance of AppExecutors and
                // call the diskIO execute method with a new Runnable and implement its run method
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();

                        List<TransactionEntry> transactionEntries = customAdapter.getTransactionEntries();
                        mAppDb.transactionDao().removeExpense(transactionEntries.get(position));

                    }
                });

                Toast.makeText(HomeActivity.this, "T", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rv);
*/
        setupViewModel();


        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer , toolbar , R.string. navigation_drawer_open ,
                R.string. navigation_drawer_close ) ;
        drawer.addDrawerListener(toggle) ;
        toggle.syncState() ;
        NavigationView navigationView = findViewById(R.id. nav_view ) ;
        navigationView.setNavigationItemSelectedListener(this) ;


        /*View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.email);
        navUsername.setText(mAuth.getCurrentUser().getEmail().toString());*/

        View navHeaderView= navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView tvHeaderName= (TextView) navHeaderView.findViewById(R.id.email);
        tvHeaderName.setText(mAuth.getCurrentUser().getEmail().toString());


    }
    @Override
    public void onBackPressed () {
        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        if (drawer.isDrawerOpen(GravityCompat. START )) {
            drawer.closeDrawer(GravityCompat. START ) ;
        } else {
            super .onBackPressed() ;
        }
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main , menu) ;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId() ;
        if (id == R.id. action_settings ) {
            return true;
        }
        return super .onOptionsItemSelected(item) ;
    }
    @SuppressWarnings ( "StatementWithEmptyBody" )
    @Override
    public boolean onNavigationItemSelected ( @NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId() ;
        if (id == R.id. nav_camera ) {

            Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id. nav_gallery ) {
            Intent intent = new Intent(HomeActivity.this,Notifications.class);
            startActivity(intent);


        } else if (id == R.id. nav_slideshow ) {

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id. nav_manage ) {
        } else if (id == R.id. nav_share ) {
        }else
            {
                if (id==R.id.logout)
                {
                    mAuth.signOut();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }


        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        drawer.closeDrawer(GravityCompat. START ) ;
        return true;
    }



    public void setupViewModel(){
        transactionViewModel = ViewModelProviders.of(this)
                .get(TransactionViewModel.class);

        transactionViewModel.getExpenseList()
                .observe(this, new Observer<List<TransactionEntry>>() {
                    @Override
                    public void onChanged(@Nullable List<TransactionEntry> transactionEntriesFromDb) {
                        Log.i(LOG_TAG,"Actively retrieving from DB");


                        transactionEntries = transactionEntriesFromDb;
//                        Logging to check DB values
                        for (int i =0 ; i < transactionEntries.size() ; i++){
                            String description = transactionEntries.get(i).getDescription();
                            int amount = transactionEntries.get(i).getAmount();


                            donutProgress.setDonut_progress(String.valueOf(transactionEntries.size()));
                            //Log.i("DESCRIPTION AMOUNT",description + String.valueOf(amount));
                        }

//                        Setting Adapter
                    }
                });
    }

}
