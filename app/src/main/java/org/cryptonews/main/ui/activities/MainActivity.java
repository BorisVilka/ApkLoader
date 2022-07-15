package org.cryptonews.main.ui.activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.REQUEST_INSTALL_PACKAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.cryptonews.main.BuildConfig;
import org.cryptonews.main.R;
import org.cryptonews.main.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableEmitter;
import io.reactivex.rxjava3.core.CompletableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private int flag;
    private SharedPreferences preferences;
    private AppObject appObject;
    private boolean clicked = false, end = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag= 0;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("app").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appObject = snapshot.getValue(AppObject.class);
                Log.d("TAG",appObject.alert.description);
                int dur = appObject.alert.duration;
                if(appObject.enabled) {
                    Observable<Long> completable = Observable.create(emitter -> {
                        if(!end) return;
                        end = false;
                        MyDialog dialog = new MyDialog(appObject.alert,
                                (v)->{
                            Log.d("TAG",appObject.url);
                                    if (checkPermission()) {
                                        UpdateApp atualizaApp = new UpdateApp();
                                        atualizaApp.setContext(MainActivity.this);
                                        atualizaApp.execute(appObject.url);
                                        ((DialogFragment)getSupportFragmentManager().findFragmentByTag("TAG")).dismiss();
                                    } else {
                                        clicked = true;
                                        requestPermission();
                                    }
                                });
                        dialog.show(getSupportFragmentManager(),"TAG");
                        emitter.onComplete();
                    });
                    Observable.interval(appObject.alert.duration,TimeUnit.MILLISECONDS).subscribe(aLong -> {
                        completable.subscribe();
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG",error.getMessage());
            }
        });
        if (checkPermission()) {
            //UpdateApp atualizaApp = new UpdateApp();
           // atualizaApp.setContext(MainActivity.this);
            //atualizaApp.execute("https://firebasestorage.googleapis.com/v0/b/loader-5897b.appspot.com/o/app-release.apk?alt=media&token=9dff5644-7e7c-482b-a531-6021a1968923");
        } else {
            requestPermission();
        }
        Log.d("TAG",getTheme().toString());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_slideshow,R.id.nav_prognose,R.id.nav_settings,R.id.nav_favorites,R.id.nav_terms)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        binding.navView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId()==R.id.nav_gallery) {
                binding.drawerLayout.close();
                switch (navController.getCurrentDestination().getId()) {
                    case R.id.nav_home:
                        flag= 0;
                        navController.navigate(R.id.action_nav_home_to_nav_gallery);
                        break;
                    case R.id.nav_prognose:
                        flag= 1;
                        navController.navigate(R.id.action_nav_prognose_to_nav_gallery);
                        break;
                    case R.id.nav_slideshow:
                        flag= 2;
                        navController.navigate(R.id.action_nav_slideshow_to_nav_gallery);
                        break;
                    case R.id.nav_settings:
                        flag = 3;
                        break;
                }
            }
            invalidateOptionsMenu();
            navController.navigate(item.getItemId());
            binding.drawerLayout.close();
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("TAG",item.getItemId()+"");

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(binding.navView.getCheckedItem().getItemId()==R.id.nav_gallery) {
            WebView view = findViewById(R.id.web);
            if(view.canGoBack()) view.goBack();
            else Navigation.findNavController(this,R.id.nav_host_fragment_content_main).navigateUp();
        } else super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(getLanguage(newBase));
    }
    private Context getLanguage(Context context) {
        Locale locale = new Locale(context.getSharedPreferences(MyApp.prefs,MODE_PRIVATE).getString(MyApp.language,"ru"));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    public void setTitle(String s) {
        getSupportActionBar().setTitle(s);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (locationAccepted && cameraAccepted && appObject.enabled && clicked) {
                    UpdateApp updateApp = new UpdateApp();
                    updateApp.setContext(MainActivity.this);
                    updateApp.execute(appObject.url);
                }
            }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        requestPermissions(new String[]{
                WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE, REQUEST_INSTALL_PACKAGES,
                Manifest.permission.INSTALL_PACKAGES
        }, PERMISSION_REQUEST_CODE);
        ActivityResultLauncher<Intent> unknownAppSourceDialog = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // User has allowed app to install APKs
                        // so we can now launch APK installation.

                    }
                });
        if (!getPackageManager().canRequestPackageInstalls()) {
            Intent unknownAppSourceIntent = new Intent()
                    .setAction(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                    .setData(Uri.parse(String.format("package:%s", getPackageName())));
            unknownAppSourceDialog.launch(unknownAppSourceIntent);
        }
    }

    public class UpdateApp extends AsyncTask<String, Integer, String> {
        private ProgressDialog mPDialog;
        private Context mContext;

        void setContext(Activity context) {
            mContext = context;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPDialog = new ProgressDialog(mContext);
                    mPDialog.setMessage("Please wait...");
                    mPDialog.setIndeterminate(true);
                    mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mPDialog.setCancelable(false);
                    mPDialog.show();
                }
            });
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                String fileName = "app-release.apk";
                destination += fileName;
                final Uri uri = Uri.parse("file://" + destination);

                //Delete update file if exists
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                file.mkdirs();
                File outputFile = new File(file, "app-release.apk");
                if(outputFile.exists()){
                    outputFile.delete();
                }

                //get url of app on server
                //String url = arg0[0];

                Log.d("TAG","START");
                /*URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setInstanceFollowRedirects(true);
                c.setDoOutput(true);
                c.setRequestMethod("GET");
                c.connect();
                Log.d("TAG",c.getRequestMethod());

                FileOutputStream fos = new FileOutputStream(outputFile);
                Log.d("TAG","BEFORE INPUT");
                InputStream is = c.getInputStream();
                //Log.d("TAG",c.getContentLengthLong()+"|||");
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    Log.d("TAG",len1+"!!");
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();*/
                //set downloadmanager
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(arg0[0]));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setVisibleInDownloadsUi(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setDestinationInExternalFilesDir(getApplicationContext(), null, "AppName.apk");
                //set destination
                request.setDestinationUri(uri);

                // get download service and enqueue file
                final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                final long downloadId = manager.enqueue(request);

                //set BroadcastReceiver to install app when .apk is downloaded
                String finalDestination = destination;
                Log.d("TAG",finalDestination);
                BroadcastReceiver onComplete = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Uri contentUri = FileProvider.getUriForFile(
                                        context,
                                        BuildConfig.APPLICATION_ID+".fileprovider",
                                        new File(finalDestination)
                                );
                                Log.d("TAG",contentUri.toString());
                                Intent install = new Intent(Intent.ACTION_VIEW);
                                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                                install.setData(contentUri);
                                context.startActivity(install);
                                context.unregisterReceiver(this);
                                // finish()
                            } else {
                                Intent install = new Intent(Intent.ACTION_VIEW);
                                install.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                install.setDataAndType(
                                        uri,
                                        "\"application/vnd.android.package-archive\""
                                );
                                context.startActivity(install);
                                context.unregisterReceiver(this);
                                // finish()
                            }
                            if (mPDialog != null)
                                mPDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("TAG",e.getMessage()+" | "+e.getLocalizedMessage());
                        }
                        end = true;
                    }
                };
                //register receiver for when .apk download is compete
                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            } catch (Exception e) {
                Log.d("UpdateAPP", "Update error! " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mPDialog != null)
                mPDialog.show();

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (mPDialog != null) {
                mPDialog.setIndeterminate(false);
                mPDialog.setMax(100);
                mPDialog.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
                Toast.makeText(mContext, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(mContext, "File Downloaded", Toast.LENGTH_SHORT).show();
        }


        private void installApk() {
                File toInstall = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/AppName.apk");
                Log.d("TAG",toInstall.getAbsolutePath());
                Intent intent;
                 {
                Uri apkUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", toInstall);
                intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setData(apkUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                     }
                ActivityResultLauncher<Intent> unknownAppSourceDialog = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                // User has allowed app to install APKs
                                // so we can now launch APK installation.
                                startActivity(intent);
                            }
                        });
                if (!getPackageManager().canRequestPackageInstalls()) {
                    Intent unknownAppSourceIntent = new Intent()
                            .setAction(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                            .setData(Uri.parse(String.format("package:%s", getPackageName())));
                    unknownAppSourceDialog.launch(unknownAppSourceIntent);
                } else {
                    // App already have the permission to install so launch the APK installation.
                    startActivity(intent);
                }
        }
    }

}