package info.culebrasgis.basketteammaker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.SnackBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList<String> playersList, undoPlayerslist;
    private ListView lvPlayers;
    private PlayersListAdapter adapter;
    private Button btAddPlayer, btClearList, btCreateTeams;
    private TextView tvPlayersCounter;
    private EditText etPlayer;
    private Toast toast;
    private SnackBar sbUndo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePlayersList(playersList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playersList = recoverPlayersList();
        updatePlayersList();
    }

    private void initComponents() {
        tvPlayersCounter = (TextView) findViewById(R.id.tvPlayersCounter);
        btAddPlayer = (Button) findViewById(R.id.btAddPlayer);
        btClearList = (Button) findViewById(R.id.btClearList);
        btCreateTeams = (Button) findViewById(R.id.btCreateTeams);
        etPlayer = (EditText) findViewById(R.id.etPlayer);
        lvPlayers = (ListView) findViewById(R.id.lvPlayers);
        sbUndo = new SnackBar(this);
        sbUndo.applyStyle(R.style.SnackBarSingleLine)
                .text(getString(R.string.undo_warning))
                .actionText(getString(R.string.undo_action));

        btAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayer();
            }
        });

        btClearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearList();
            }
        });

        btCreateTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTeams();
            }
        });

    }

    private void updatePlayersList() {
        adapter = new PlayersListAdapter(playersList);
        lvPlayers.setAdapter(adapter);
        tvPlayersCounter.setText(getString(R.string.counter_players, String.valueOf(playersList.size())));
    }

    private void savePlayersList(ArrayList<String> players) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        if (players.size() > 0) {
            JSONArray a = new JSONArray();
            for (String player : players) {
                a.put(player);
            }
            editor.putString("players", a.toString());
        } else {
            editor.putString("players", null);
        }
        editor.apply();
    }

    private ArrayList<String> recoverPlayersList() {
        ArrayList<String> players = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String json = prefs.getString("players", null);
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    players.add(a.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return players;
    }

    private void addPlayer() {
        String name = etPlayer.getText().toString();
        if (name.length() > 0) {
            playersList.add(name);
            etPlayer.getText().clear();
            updatePlayersList();
        } else {
            showAlert(getString(R.string.error_player_name));
        }
    }

    private void clearList() {
        if (playersList.size() == 0) {
            showAlert(getString(R.string.empty_list));
        } else {
            undoPlayerslist = new ArrayList<>(playersList);
            playersList.clear();
            updatePlayersList();
            if (sbUndo.getState() == SnackBar.STATE_SHOWN) {
                sbUndo.dismiss();
            } else {
                sbUndo.actionClickListener(new SnackBar.OnActionClickListener() {
                    @Override
                    public void onActionClick(SnackBar snackBar, int i) {
                        playersList = new ArrayList<>(undoPlayerslist);
                        updatePlayersList();
                    }
                })
                        .duration(3000)
                        .show(this);
            }
        }
    }

    private void createTeams() {
        if (playersList.size() < 4) {
            showAlert(getString(R.string.min_players_warning));
        } else {
            Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {
                    // TODO
                    super.onPositiveActionClicked(fragment);
                }

                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    super.onNegativeActionClicked(fragment);
                }
            };
            builder.title(getString(R.string.title_number_teams))
                    .positiveAction(getString(R.string.ok_dialog))
                    .negativeAction(getString(R.string.cancel_dialog))
                    .contentView(R.layout.dialog_number_teams);

            DialogFragment f = DialogFragment.newInstance(builder);
//            f.show(getFragmentManager(), null);

        }
    }

    private void makeTeams(int numberTeams) {
        Intent intent = new Intent(this, TeamsActivity.class);
        intent.putExtra("numberTeams", numberTeams);
        intent.putStringArrayListExtra("playersList", playersList);
        startActivity(intent);
    }

    private void showAlert(String text) {
        if (toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        if (!toast.getView().isShown()) {
            toast.setText(text);
            toast.show();
        }
    }

    private class PlayersListAdapter extends ArrayAdapter<String> {

        PlayersListAdapter(ArrayList<String> players) {
            super(MainActivity.this, R.layout.player_row, players);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View item = inflater.inflate(R.layout.player_row, parent, false);

            TextView playerName = (TextView) item.findViewById(R.id.tvPlayerName);
            playerName.setText(playersList.get(position));

            TextView tvDeletePlayer = (TextView) item.findViewById(R.id.tvDeletePlayer);
            tvDeletePlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playersList.remove(position);
                    updatePlayersList();
                }
            });

            return item;
        }
    }

}
