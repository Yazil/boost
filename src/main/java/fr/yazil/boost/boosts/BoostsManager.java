package fr.yazil.boost.boosts;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BoostsManager {

    private static BoostsManager instance;

    @Getter
    private Boost serverBoost;

    @Getter
    private final Map<Player, Boost> playerBoostMap = new HashMap<>();

    public static BoostsManager getInstance() {
        if (instance == null) {
            instance = new BoostsManager();
        }
        return instance;
    }

    //If the server has an active boost: add the supplied value to the boost; if not create a boost with supplied values
    public void boostServer(float multiplier, int duration) {
        Boost boost = new Boost(multiplier, duration);
        if(serverBoost == null) {
            this.serverBoost = boost;
        } else {
            serverBoost.addBoost(boost);
        }

        //If any value is negative we delete the boost
        if(serverBoost.getMultiplier() <= 0){
            resetServerBoost();
            return;
        }
        if(serverBoost.getDuration() <= 0){
            resetServerBoost();
            return;
        }
        if((serverBoost.getDuration() - serverBoost.getActiveFor()) <= 0) {
            resetServerBoost();
            return;
        }
    }

    //Same thing as above but with a specified player
    public void boostPlayer(Player player, float multiplier, int duration) {
        Boost boost = new Boost(multiplier, duration);
        if(!playerBoostMap.containsKey(player)) {
            playerBoostMap.put(player, boost);
        } else {
            playerBoostMap.get(player).addBoost(boost);
        }

        Boost playerBoost = playerBoostMap.get(player);
        if(playerBoost.getMultiplier() <= 0) {
            resetPlayerBoost(player);
            return;
        }
        if(playerBoost.getDuration() <= 0){
            resetPlayerBoost(player);
            return;
        }
        if((playerBoost.getDuration() - playerBoost.getActiveFor()) <= 0){
            resetPlayerBoost(player);
            return;
        }
    }

    public void resetServerBoost() {
        serverBoost = null;
    }

    public void resetPlayerBoost(Player player) {
        this.playerBoostMap.remove(player);
    }
}
