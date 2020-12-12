package com.example.noti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button button;
    int UPI_PAYMENT = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Appointment","My Appointment",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(InternetConnection(MainActivity.this)){
                    String status = "";
                    String paymentCancel = "";
                    String approvalRefNo = "";

                    if (status.equals("success"))
                    {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,"Appointment");
                builder.setContentTitle("Appointment");
                builder.setContentText("Your appointment is successful book");
                builder.setSmallIcon(R.drawable.ic_baseline_message_24);
                builder.setAutoCancel(true);

                Intent intent = new Intent(MainActivity.this,
                        MainActivity2.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this
                        , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                builder.setContentIntent(pendingIntent);
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                managerCompat.notify(1,builder.build());

                    }

                }


                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", "pavan.n.sap@okaxis")
                                .appendQueryParameter("pn", "your-merchant-name")
                                .appendQueryParameter("mc", "your-merchant-code")// merchant id is required for transaction
                                .appendQueryParameter("tr", "your-transaction-ref-id")
                                .appendQueryParameter("tn", "your-transaction-note")
                                .appendQueryParameter("am", "500")
                                .appendQueryParameter("cu", "INR")
                                .appendQueryParameter("url", "your-transaction-url")
                                .build();

                Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
                upiPayIntent.setData(uri);

                //here will open dialog to choose an app for the payment
                Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

                //
                if(null != chooser.resolveActivity(getPackageManager())){
                    startActivityForResult(chooser,UPI_PAYMENT );
                }
                else
                    {
                    Toast.makeText(MainActivity.this, "No UPI app found",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //For mobile internet connection check internet is available or Not.
    public  boolean InternetConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()
            && networkInfo.isConnectedOrConnecting()
            && networkInfo.isAvailable()){
                return  true;
            }
        }
     return false;
    }
}