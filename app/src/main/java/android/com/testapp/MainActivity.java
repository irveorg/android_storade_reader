package android.com.testapp;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button emailButton;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.email);
        emailButton = (Button) findViewById(R.id.emailButton);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFileStrucure();
            }
        });
    }

    protected void getFileStrucure(){
        Filewalker fw = new Filewalker();
        fw.walk(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
    }

    private class Filewalker {
        private int count = 0;
        private String sked = "";
        public void walk(File root) {
            count++;
            File[] list = root.listFiles();
            for (File f : list) {
                if (f.isDirectory()) {
                    sked += "time: " + f.lastModified() + ", dir: " + f.getAbsoluteFile() + "\n";
                    walk(f);
                }
                else {
                    sked += "time: " + new Date(f.lastModified()) + ", size: " + f.length() + ", " + f.getAbsoluteFile() + "\n";
                }
            }
            count--;
            if (count == 0) {
                sendMail(sked);
            }
        }
    }

    protected void sendMail(String sked) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
        i.putExtra(Intent.EXTRA_SUBJECT, "android storage structure");
        i.putExtra(Intent.EXTRA_TEXT, sked);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
