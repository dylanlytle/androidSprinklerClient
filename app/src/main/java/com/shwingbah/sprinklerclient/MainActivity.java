package com.shwingbah.sprinklerclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView serverURLTextView = new TextView(this);
        serverURLTextView.setText("Server URL");

        final EditText serverURLEditText = new EditText(this);
        serverURLEditText.setSingleLine();
        serverURLEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        TextView commandTextView = new TextView(this);
        commandTextView.setText("Sprinkler Command");

        final EditText commandEditText = new EditText(this);
        commandEditText.setSingleLine();
        commandEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        Button sendCommandButton = new Button(this);
        sendCommandButton.setText("Send Command");
        sendCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendSprinklerCommand(serverURLEditText.getText().toString(), commandEditText.getText().toString());
            }
        });

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.addView(serverURLTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        rootLayout.addView(serverURLEditText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        rootLayout.addView(commandTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        rootLayout.addView(commandEditText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        rootLayout.addView(sendCommandButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        setContentView(rootLayout);
    }

    public void SendSprinklerCommand(String serverURL, String command)
    {
        SprinklerServerManager.GetInstance().SendSprinklerCommand(this, serverURL, command, new SprinklerServerManager.SendSprinklerCommandListener() {
            @Override
            public void SendSprinklerCommandReceivedResponse(boolean succeeded) {
                // Do something with response from server here if desired
                Log.i("test", "received response");
            }
        });
    }
}
