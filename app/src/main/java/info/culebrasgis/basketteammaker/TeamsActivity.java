package info.culebrasgis.basketteammaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.ArrayList;

public class TeamsActivity extends Activity {

    private int numberTeams;
    private ArrayList<String> playersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        if (savedInstanceState != null) {
            numberTeams = savedInstanceState.getInt("numberTeams");
            playersList = savedInstanceState.getStringArrayList("playersList");
        } else {
            Intent intent = getIntent();
            numberTeams = intent.getExtras().getInt("numberTeams");
            playersList = intent.getExtras().getStringArrayList("playersList");
        }

        TextView tv = (TextView) findViewById(R.id.test);
        tv.setText(String.valueOf(numberTeams)); // TODO
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("numberTeams", numberTeams);
        outState.putSerializable("playersList", playersList);
    }

}
