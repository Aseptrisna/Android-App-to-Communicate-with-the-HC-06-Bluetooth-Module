package com.droiduino.bluetoothconn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.UUID;

import static android.content.ContentValues.TAG;

import com.droiduino.bluetoothconn.localstorage.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private String deviceName = null;
    private String deviceAddress;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
    public static ConnectedThread connectedThread;
    public static CreateConnectThread createConnectThread;
    TextView Message;
    EditText Nomor_Jadwal,ValueJam,Jumlah_Pakan;
    TextView Jadwal1,Jadwal2,Jadwal3;
    Button Pilih_Jam,Pakan_M,Pakan_S,Pakan_L,Set_Jadwal,Kirim_Jadwal,Hapus_Jadwal;
    private TimePickerDialog timePickerDialog;
    SharedPrefManager sharedPrefManager;

    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefManager=new SharedPrefManager(this);
//        connectedThread=new ConnectedThread(MainActivity.this);
        Nomor_Jadwal=(EditText) findViewById(R.id.jadwal_number);
        ValueJam=(EditText) findViewById(R.id.valuejam);
        Jumlah_Pakan=(EditText) findViewById(R.id.valuepakan);

        Jadwal1=(TextView) findViewById(R.id.jadwal1);
        Jadwal2=(TextView) findViewById(R.id.jadwal2);
        Jadwal3=(TextView) findViewById(R.id.jadwal3);

        Pilih_Jam=(Button) findViewById(R.id.jam);
        Pakan_M=(Button) findViewById(R.id.medium);
        Pakan_S=(Button) findViewById(R.id.small);
        Pakan_L=(Button) findViewById(R.id.large);
        Set_Jadwal=(Button) findViewById(R.id.btnsetjadwal);
        Kirim_Jadwal=(Button) findViewById(R.id.btnkirimjadwal);
        Hapus_Jadwal=(Button) findViewById(R.id.btnhapusjadwal);
        // UI Initialization
        final Button buttonConnect = findViewById(R.id.buttonConnect);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        final TextView textViewInfo = findViewById(R.id.textViewInfo);
        final Button buttonToggle = findViewById(R.id.buttonToggle);
        buttonToggle.setEnabled(false);
        final ImageView imageView = findViewById(R.id.imageView);
        imageView.setBackgroundColor(getResources().getColor(R.color.colorOff));
        Message=(TextView)findViewById(R.id.textmsg);

        Pilih_Jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showtime();
            }
        });
        Pakan_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Jumlah_Pakan.setText("03");
            }
        });
        Pakan_M.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Jumlah_Pakan.setText("02");
            }
        });
        Pakan_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Jumlah_Pakan.setText("01");
            }
        });
        setJadwal();

        Set_Jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jam=ValueJam.getText().toString();
                String Jumlah_pakan=Jumlah_Pakan.getText().toString();
                String Pakan_No=Nomor_Jadwal.getText().toString();
                String Jadwal=jam+","+Jumlah_pakan;
                if(jam.equals("")){
                    Toast.makeText(MainActivity.this, "Kolom Jam Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if(Jumlah_pakan.equals("")){
                    Toast.makeText(MainActivity.this, "Pilih Jumlah Pakan !!!", Toast.LENGTH_SHORT).show();
                }else if(Pakan_No.equals("")){
                    Toast.makeText(MainActivity.this, "Masukan Nomor Jadwal", Toast.LENGTH_SHORT).show();
                }else {
                    if(Pakan_No.equals("1")){
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_Jadwal_1,Jadwal);
                    }else if(Pakan_No.equals("2")){
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_Jadwal_2,Jadwal);
                    }else if(Pakan_No.equals("3")){
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_Jadwal_3,Jadwal);
                    }else {
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_Jadwal_1,"Jadwal belum di atur");
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_Jadwal_2,"Jadwal belum di atur");
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_Jadwal_3,"Jadwal belum di atur");
                    }
                    setJadwal();
                }

            }
        });
        Hapus_Jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPString(SharedPrefManager.SP_Jadwal_1,"Jadwal belum di atur");
                sharedPrefManager.saveSPString(SharedPrefManager.SP_Jadwal_2,"Jadwal belum di atur");
                sharedPrefManager.saveSPString(SharedPrefManager.SP_Jadwal_3,"Jadwal belum di atur");
                setJadwal();
            }
        });
        Kirim_Jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Jadwal=sharedPrefManager.getSP_Jadwal_1()+","+sharedPrefManager.getSP_Jadwal_2()+","+
                        sharedPrefManager.getSP_Jadwal_3().toString();
//                connectedThread.write("01,02,03");
            }
        });

        // If a bluetooth device has been selected from SelectDeviceActivity
        deviceName = getIntent().getStringExtra("deviceName");
        if (deviceName != null){
            // Get the device address to make BT Connection
            deviceAddress = getIntent().getStringExtra("deviceAddress");
            // Show progree and connection status
            toolbar.setSubtitle("Terhubung dengan:" + deviceName + "...");
            progressBar.setVisibility(View.VISIBLE);
            buttonConnect.setEnabled(false);

            /*
            This is the most important piece of code. When "deviceName" is found
            the code will call a new thread to create a bluetooth connection to the
            selected device (see the thread code below)
             */
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter,deviceAddress);
            createConnectThread.start();
        }

        /*
        Second most important piece of Code. GUI Handler
         */
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case CONNECTING_STATUS:
                        switch(msg.arg1){
                            case 1:
                                toolbar.setSubtitle("Terhubung dengan:" + deviceName);
                                progressBar.setVisibility(View.GONE);
                                buttonConnect.setEnabled(true);
                                buttonToggle.setEnabled(true);
                                break;
                            case -1:
                                toolbar.setSubtitle("Perangkat gagal terhubung");
                                progressBar.setVisibility(View.GONE);
                                buttonConnect.setEnabled(true);
                                break;
                        }
                        break;

                    case MESSAGE_READ:
                        String arduinoMsg = msg.obj.toString(); // Read message from Arduino
                        switch (arduinoMsg.toLowerCase()){
                            case "led is turned on":
                                imageView.setBackgroundColor(getResources().getColor(R.color.colorOn));
                                textViewInfo.setText("Arduino Message : " + arduinoMsg);
                                break;
                            case "led is turned off":
                                imageView.setBackgroundColor(getResources().getColor(R.color.colorOff));
                                textViewInfo.setText("Arduino Message : " + arduinoMsg);
                                break;
                        }
                        break;
                }
            }
        };

        // Select Bluetooth Device
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to adapter list
                Intent intent = new Intent(MainActivity.this, SelectDeviceActivity.class);
                startActivity(intent);
            }
        });

        // Button to ON/OFF LED on Arduino Board
        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmdText = Message.getText().toString();
                String Jadwal=sharedPrefManager.getSP_Jadwal_1()+","+sharedPrefManager.getSP_Jadwal_2()+","+
                        sharedPrefManager.getSP_Jadwal_3().toString();
                String btnState = buttonToggle.getText().toString().toLowerCase();
                switch (btnState){
                    case "turn on":
                        buttonToggle.setText("Turn Off");
                        // Command to turn on LED on Arduino. Must match with the command in Arduino code
//                        cmdText = "<turn on>";
                        break;
                    case "turn off":
                        buttonToggle.setText("Turn On");
                        // Command to turn off LED on Arduino. Must match with the command in Arduino code
//                        cmdText = "<turn off>";
                        break;
                }
                // Send command to Arduino board
                connectedThread.write(Jadwal);
                System.out.println(Jadwal);
            }
        });
    }

    private void setJadwal() {
        ValueJam.setText("");
        Jumlah_Pakan.setText("");
        Nomor_Jadwal.setText("");
        Jadwal1.setText("Jadwal 1:"+sharedPrefManager.getSP_Jadwal_1());
        Jadwal2.setText("Jadwal 2:"+sharedPrefManager.getSP_Jadwal_2());
        Jadwal3.setText("Jadwal 3:"+sharedPrefManager.getSP_Jadwal_3());
    }

    private void showtime() {
        Calendar calendar = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    ValueJam.setText(hourOfDay+","+minute);
                }
            },
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(MainActivity.this));
        }
        timePickerDialog.show();
    }


    public static class CreateConnectThread extends Thread {

        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();

            try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e("Status", "Device connected");
                handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.e("Status", "Cannot connect to device");
                    handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.run();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    /* =============================== Thread for Data Transfer =========================================== */
    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes = 0; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                    Then send the whole String message to GUI Handler.
                     */
                    buffer[bytes] = (byte) mmInStream.read();
                    String readMessage;
                    if (buffer[bytes] == '\n'){
                        readMessage = new String(buffer,0,bytes);
                        Log.e("Arduino Message",readMessage);
                        handler.obtainMessage(MESSAGE_READ,readMessage).sendToTarget();
                        bytes = 0;
                    } else {
                        bytes++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            Log.e("message",input);
            byte[] bytes = input.getBytes(); //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
//                Berhasil();
//                Toast.makeText(MainActivity.this, "Jadwal berhasil di kirim", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("Send Error","Unable to send message",e);
            }
        }

//        private void Berhasil() {
//            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
//        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    /* ============================ Terminate Connection at BackPress ====================== */
    @Override
    public void onBackPressed() {
        // Terminate Bluetooth Connection and close app
        if (createConnectThread != null){
            createConnectThread.cancel();
        }
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
