import bwapi.*;

import java.util.HashSet;

public class VultureAI  extends DefaultBWListener implements Runnable {

    private final Mirror bwapi;
    
    private Game game;
    
    private Player self;

    private Vulture vulture;

    private HashSet<Unit> enemyUnits;

    private int frame;
    
    //new
    public static XCS VultXCS = new XCS(); 
    public float RoundCount = 0;
    public float wins = 0;
    public int time = 0;
        
    public VultureAI() 
    {
        System.out.println("This is the VultureAI! :)");
        this.bwapi = new Mirror();
    }

    public static void main(String[] args) {
        new VultureAI().run();
    }

    @Override
    public void onStart() {
        enemyUnits = new HashSet<Unit>();
        this.game = this.bwapi.getGame();
        this.self = game.self();
        this.frame = 0;

        // complete map information
        this.game.enableFlag(0);
        
        // user input
        this.game.enableFlag(1);
        this.game.setLocalSpeed(10);
        
    }

    @Override
    public void onFrame() {
    	if (RoundCount > 0)
    	{
    		game.drawTextScreen(10, 10, "Round: " + RoundCount + " Wins: " + wins + " - " + (wins)/(RoundCount)*100 + " percent");
    		game.drawTextScreen(10, 20, "avg time: " + (time+1)/RoundCount + " s");
    	}
    	
        vulture.step();
        
        if (frame % 1000 == 0) {
            System.out.println("Frame: " + frame);
        }
        frame++;
    }

    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("New unit discovered " + unit.getType());
        UnitType type = unit.getType();

        if (type == UnitType.Terran_Vulture) {
            if (unit.getPlayer() == this.self) {
                this.vulture = new Vulture(unit, bwapi, enemyUnits);
            }
        } else if (type == UnitType.Protoss_Zealot) {
            if (unit.getPlayer() != this.self) {
               enemyUnits.add(unit);
            }
        }
    }
    
    @Override
    public void onUnitDestroy(Unit unit) {
    	if(this.enemyUnits.contains(unit)){
            this.enemyUnits.remove(unit);
    	}
    }
    

    @Override
    public void onEnd(boolean winner) 
    {
    	RoundCount++;
    	time += game.elapsedTime();
    	if (winner) wins++;
    	System.out.println("Round:" + RoundCount + " Wins:" + wins + " - " + (wins)/(RoundCount)*100.0d + "%");
    	

    }

    @Override
    public void onSendText(String text) {
    }

    @Override
    public void onReceiveText(Player player, String text) {
    }

    @Override
    public void onPlayerLeft(Player player) {
    }

    @Override
    public void onNukeDetect(Position position) {
    }

    @Override
    public void onUnitEvade(Unit unit) {
    }

    @Override
    public void onUnitShow(Unit unit) {

    }

    @Override
    public void onUnitHide(Unit unit) {
    }

    @Override
    public void onUnitMorph(Unit unit) {

    }

    @Override
    public void onUnitRenegade(Unit unit) {

    }

    @Override
    public void onSaveGame(String gameName) {
    }

    @Override
    public void onUnitComplete(Unit unit) {
    }

    @Override
    public void onPlayerDropped(Player player) {
    }

    @Override
    public void run() {
        this.bwapi.getModule().setEventListener(this);
        this.bwapi.startGame();
    }
}
