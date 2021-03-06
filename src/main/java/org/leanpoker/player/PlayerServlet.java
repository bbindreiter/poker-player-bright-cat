package org.leanpoker.player;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class PlayerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("Java player is running");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("action").equals("bet_request")) {
            String gameStateParameter = req.getParameter("game_state");


            try{

                System.out.println("gameStateParameter: " + gameStateParameter);


                GameState gameState1 = new Gson().fromJson(gameStateParameter , GameState.class);

                int response = Player.betRequestV1(gameState1);

                System.out.println("response: " + response);

                resp.getWriter().print(response);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("exception in doPost");
                resp.getWriter().print(0);
            }




        }
        if (req.getParameter("action").equals("showdown")) {
            String gameState = req.getParameter("game_state");

            Player.showdown(new JsonParser().parse(gameState));

        }
        if (req.getParameter("action").equals("version")) {
            resp.getWriter().print(Player.VERSION);
        }
    }
}
