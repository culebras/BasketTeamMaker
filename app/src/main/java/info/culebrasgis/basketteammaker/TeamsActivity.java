package info.culebrasgis.basketteammaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.rey.material.widget.Button;

import java.util.ArrayList;

public class TeamsActivity extends Activity {

    private int numberTeams;
    private ArrayList<String> playersList;
    private TextView tvResults, tvTeamsTitle;
    private Button btBack, btRandomizeAgain;

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

        initComponents();

        makeTeams();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("numberTeams", numberTeams);
        outState.putSerializable("playersList", playersList);
    }

    private void initComponents() {
        tvResults = (TextView) findViewById(R.id.tvResults);
        tvTeamsTitle = (TextView) findViewById(R.id.tvTeamsTitle);
        btBack = (Button) findViewById(R.id.btBack);
        btRandomizeAgain = (Button) findViewById(R.id.btRandomizeAgain);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btRandomizeAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeTeams();
            }
        });

        tvTeamsTitle.setText(String.valueOf(numberTeams) + " " + getString(R.string.title_teams));
    }

    private void makeTeams() {
        playersList = new ArrayList<>(shuffleArrayList(playersList));
        tvResults.setText(printTeams(playersList, numberTeams));
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

    private String printTeams(ArrayList<String> al, int numTeams) {
        String result = "";
        String players;
        int numPlayers = playersList.size();
        int team = 0;
        int perTeam;

        for (int i = 0; i < numPlayers; i++) {
            perTeam = (int) Math.ceil((numPlayers - i) / (numTeams - team));
            players = "";

            for (int y = 0; y < perTeam; y++) {
                if (y < perTeam - 2) {
                    players += al.get(i) + ", ";
                }
                else if (y < perTeam - 1) {
                    players += al.get(i) + getString(R.string.and);
                }
                else {
                    players += al.get(i) + ".";
                }
                i++;
            }
            i--;
            team++;
            if (i < numPlayers - 1) {
                result += "Equipo " + team + ": " + players + "\n\n\n";
            }
            else {
                result += "Equipo " + team + ": " + players;
            }
        }

        return result;
    }

}
