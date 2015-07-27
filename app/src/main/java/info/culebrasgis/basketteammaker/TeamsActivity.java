package info.culebrasgis.basketteammaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

        Log.i("test0", playersList.toString());

        playersList = new ArrayList<>(shuffleArrayList(playersList));

        Log.i("test1", playersList.toString());

        TextView tv = (TextView) findViewById(R.id.test);
        tv.setText(printTeams(playersList, numberTeams)); // TODO
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("numberTeams", numberTeams);
        outState.putSerializable("playersList", playersList);
    }

    private ArrayList<String> shuffleArrayList(ArrayList<String> al) {
        int length = al.size();
        int randomPosition;
        String lastItem;

        while (length > 0) {
            randomPosition = (int) (Math.random() * length--);
            lastItem = al.get(length);
            al.set(length, al.get(randomPosition));
            al.set(randomPosition, lastItem);
        }

        return al;
    }

    private String printTeams(ArrayList<String> al, int teams) {
        int playersPerTeam = al.size() / teams;
        int rest = al.size() % teams;
        String result = "";

        for (int i = 0; i < teams; i++) {
            result += "Team " + (i + 1) + ": ";
            List<String> l = al.subList(0, playersPerTeam);
            result += l.toString() + "\n\n";
            al.subList(0, playersPerTeam).clear();
        }

        if (rest != 0) {
            result += "Last team: " + al.toString();
        }


        return result;
    }

}
