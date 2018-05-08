package org.leanpoker.player;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;

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

    static final String VERSION = "Bright Cat V5";

    public static int betRequestV2(GameState gameState) {

        Card card1 = gameState.players[gameState.in_action].hole_cards[0];
        Card card2 = gameState.players[gameState.in_action].hole_cards[1];

        int weight = calcHandsWeight(card1, card2);
        boolean raise = false;
        boolean fold = false;

        if (gameState.current_buy_in > gameState.players[gameState.in_action].bet) {

        }
        //current_buy_in - players[in_action][bet]


        if (gameState.in_action == gameState.dealer)
        {
            if (weight >= 7)
                raise = true;
            else
                fold = true;
        } else if (gameState.in_action-1 == gameState.dealer) {
            if (weight >= 9)
                raise = true;
            else
                fold = true;
        } else {
            if (weight >= 8)
                raise = true;
            else
                fold = true;
        }

        if (raise)
            return gameState.current_buy_in - gameState.players[gameState.in_action].bet + gameState.minimum_raise;
        else if (fold)
            return 0;
        else
            return 0;

    }

    public static int betRequestV1(GameState gameState) {


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





        boolean flush = false;

        communityCards.clear();
        card1 = getSuitAsInt(gameState.players[gameState.in_action].hole_cards[0].suit);
        card2 = getSuitAsInt(gameState.players[gameState.in_action].hole_cards[1].suit);


        for (int i=0; i < gameState.community_cards.length; i++ ) {
            Card cc = gameState.community_cards[i];
            communityCards.add(getSuitAsInt(cc.suit));
        }


        if (contains(communityCards, card1) > 3 || contains(communityCards, card2) > 3)
            flush = true;





        boolean straight = false;
        try {

            communityCards.clear();
            card1 = getRankAsInt(gameState.players[gameState.in_action].hole_cards[0].rank);
            card2 = getRankAsInt(gameState.players[gameState.in_action].hole_cards[1].rank);


            for (int i = 0; i < gameState.community_cards.length; i++) {
                Card cc = gameState.community_cards[i];
                communityCards.add(getRankAsInt(cc.rank));
            }

            communityCards.add(card1);
            communityCards.add(card2);
            Collections.sort(communityCards);

            int currentChain = 0;
            int currentValue = 0;
            for (int i = 0; i < communityCards.size(); i++) {
                currentValue = communityCards.get(i);
                currentChain = 0;

                for (int j = (i + 1); j < communityCards.size(); j++) {

                    if (communityCards.get(j) == currentValue + 1) {
                        currentValue = communityCards.get(j);
                        if (j - 4 == i) {
                            System.out.println("straight detected");
                        }
                    } else
                        break;
                }
            }
        }
        catch(Exception e) {
            System.out.println("error in straight detection");
            e.printStackTrace();
        }



        if (gameState.round < 3 && gameState.community_cards.length == 0) {
            //minimum preflop raise
            return gameState.current_buy_in - gameState.players[gameState.in_action].bet + gameState.minimum_raise;
        }
        else if (straight)
            return 10000;
        else if (flush)
            return 10000;
        else if (tripple > 0 && pairs > 0) {
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
        else if (gameState.community_cards.length == 0)
            return gameState.current_buy_in - gameState.players[gameState.in_action].bet;
        else {
            return 0;
        }





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

    private static int getSuitAsInt(Suit k) {
        if (k == Suit.clubs) return 1;
        else if (k == Suit.diamonds) return 2;
        else if (k == Suit.hearts) return 3;
        else if (k == Suit.spades) return 4;
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









    private static Integer calcHandsWeight(Card card1, Card card2) {
        Integer card1Rank = getRankAsInt(card1.rank);
        Integer card2Rank = getRankAsInt(card2.rank);

        Integer highCard = card1Rank;
        Integer lowCard = card2Rank;
        // get the highest card
        if (card2Rank > card1Rank) {
            highCard = card2Rank;
            lowCard = card1Rank;
        }

        Double value = highCard.doubleValue();
        // 1. map to chen values
        if (highCard == 14) {
            value = 10.0; // A
        } else if (highCard == 13) {
            value = 8.0; // K
        } else if (highCard == 12) {
            value = 7.0; // Q
        } else if (highCard == 11) {
            value = 6.0; // J
        } else if (highCard <= 10) {
            value = value / 2.0; // if it's 10 or lower divide value by 2
        }

        // 2. double if we have a pair (but min to 5)
        Boolean pocketPair = false;
        if (card1Rank == card2Rank) {
            value = value * 2.0;
            pocketPair = true;
            if (value < 5.0) {
                value = 5.0;
            }
        }

        // 3. add 2 points if suited
        if (card1.suit == card2.suit) {
            value += 2;
        }

        // 4. subtract points for gaps
        Integer distance = highCard - lowCard;
        if (distance == 3) {
            value -= 4.0;
        } else if (distance >= 4) {
            value -= 5.0;
        }

        // 5. add bonus point if distance 0 or 1, both cards under Q and not a pocket pair
        if (distance <= 1 && card1Rank < 12 && card2Rank < 12 && !pocketPair) {
            value += 1.0;
        }


        return (int)Math.round(value);
    }
}
