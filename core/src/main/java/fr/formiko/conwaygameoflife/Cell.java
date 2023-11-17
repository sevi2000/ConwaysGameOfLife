package fr.formiko.conwaygameoflife;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Cell extends Actor {
    boolean alive;
    Cell(int x,int y){
        super();
        setPosition(x,y);
        setSize(20,20);
        alive = false;
        addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
              System.out.println("Clicked cell");
              alive = !alive;
            };
        });
    }
    void kill(){
        alive = false;
    }

    void birth(){
        alive = true;
    }
    public static Cell getCellFromCell(Cell cell) {
        Cell c = new Cell((int)cell.getX(),(int)cell.getY());
        c.alive = cell.alive;
        return c;
    }
}
