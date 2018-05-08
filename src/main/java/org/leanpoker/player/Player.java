package org.leanpoker.player;

import com.google.gson.JsonElement;

public class Player {

    static final String VERSION = "Bright Cat V1";

    public static int betRequest(GameState gameState) {







        //game.



        return gameState.current_buy_in - gameState.players[gameState.in_action].bet;

        //current_buy_in - players[in_action][bet]



        //check
        //<return 0;




    }

    public static void showdown(JsonElement game) {
    }
}
