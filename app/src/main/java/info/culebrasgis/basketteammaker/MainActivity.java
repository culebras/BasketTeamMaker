package info.culebrasgis.basketteammaker;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    private static String[] playersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playersList = new String[]{"Jugador1", "Jugador2", "Jugador3"};







    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO guardar lista jugadores de manera persistente.
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO recuperar la lista de jugadores guardada de manera persistente.
    }
}
