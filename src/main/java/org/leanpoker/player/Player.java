package org.leanpoker.player;

import com.google.gson.JsonElement;

import java.util.ArrayList;

import static org.leanpoker.player.Rank.A;
import static org.leanpoker.player.Rank.J;
import static org.leanpoker.player.Rank.K;
import static org.leanpoker.player.Rank.Q;
import static org.leanpoker.player.Rank.V10;
import static org.leanpoker.player.Rank.V2;
import static org.leanpoker.player.Rank.V3;
import static org.leanpoker.player.Rank.V4;
import static org.leanpoker.player.Rank.V5;
import static org.leanpoker.player.Rank.V6;
import static org.leanpoker.player.Rank.V7;
import static org.leanpoker.player.Rank.V8;
import static org.leanpoker.player.Rank.V9;

public class Player {

    static final String VERSION = "Bright Cat V4";

    public static int betRequest(GameState gameState) {




        /*

        int rankCardStat[] = new int[] {};


        for (int i=0; i < rankCardStat.length; i++ ) {
            rankCardStat[i] = 0;
        }



        for (int i=0; i < gameState.community_cards.length; i++ ) {
            Card cc = gameState.community_cards[i];
            rankCardStat[getRank(cc.rank)] = rankCardStat[getRank(cc.rank)] + 1;

        }



*/


        ArrayList<Integer> communityCards = new ArrayList<>();
        Integer card1 = 0;
        Integer card2 = 0;
        int pairs = 0;
        int tripple = 0;


        for (int i=0; i < gameState.community_cards.length; i++ ) {
            Card cc = gameState.community_cards[i];
            communityCards.add(getRankAsInt(cc.rank));
        }

        card1 = getRankAsInt(gameState.players[gameState.in_action].hole_cards[0].rank);
        card2 = getRankAsInt(gameState.players[gameState.in_action].hole_cards[1].rank);

        if (card1 == card2)
            pairs++;
        else {
            if (communityCards.contains(card1))
                pairs++;
            if (communityCards.contains(card2))
                pairs++;
        }

        if (card1 == card2) {
            if (contains(communityCards, card1) > 0)
                tripple++;
        }
        else if (contains(communityCards, card1) > 1) {
            tripple++;
        }
        else if (contains(communityCards, card2) > 1) {
            tripple++;
        }





        if (tripple > 0 && pairs > 0) {
            return 10000;
        }
        else if (tripple > 0) {
            return 10000;
        }
        else if (pairs == 2 || tripple > 0) {
            return gameState.current_buy_in - gameState.players[gameState.in_action].bet + (gameState.minimum_raise * 2);
        }
        else if (pairs == 1) {
            return gameState.current_buy_in - gameState.players[gameState.in_action].bet + gameState.minimum_raise;
        }
        else
            return 0;

        //call anything
        //return gameState.current_buy_in - gameState.players[gameState.in_action].bet;







    }

    public static void showdown(JsonElement game) {
    }









    private static int getRankAsInt(Rank k) {
        if (k == V2) return 2;
        else if (k == V3) return 3;
        else if (k == V4) return 4;
        else if (k == V5) return 5;
        else if (k == V6) return 6;
        else if (k == V7) return 7;
        else if (k == V8) return 8;
        else if (k == V9) return 9;
        else if (k == V10) return 10;
        else if (k == J) return 11;
        else if (k == Q) return 12;
        else if (k == K) return 13;
        else if (k == A) return 14;
        else return 0;
    }


    private static int contains(ArrayList<Integer> communityCards, int rank) {
        int count = 0;

        for (int i=0; i < communityCards.size(); i++) {
            if((communityCards.get(i)) == rank)
                count++;
        }


        return count;
    }
}
