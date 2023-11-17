package fr.formiko.conwaygameoflife;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class Field extends Actor {
    Cell [][] field;
    int width;
    int height;
    boolean evolve = false;
    private static ShapeDrawer schd = null;
    private long timeForNextMove;
    private static final long TIME_FOR_NEXT_MOVE_MAX = 1000;
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (schd ==  null)
            schd = createShapeDrawer(batch);
        schd.setColor(Color.BLACK);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (field[i][j].alive){
                    schd.filledRectangle(getX()+ i * field[i][j].getWidth(),getY()+ j*field[i][j].getHeight(),field[i][j].getWidth(),field[i][j].getHeight(),Color.BLACK);
                } else {
                    //System.out.println("else");
                    schd.rectangle(getX()+ i * field[i][j].getWidth(),getY()+ j*field[i][j].getHeight(),field[i][j].getWidth(),field[i][j].getHeight());
                }
            }
        }
    }
    public static ShapeDrawer createShapeDrawer(Batch batch) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        Texture texture = new Texture(pixmap); // remember to dispose of later
        pixmap.dispose();
        TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
        return new ShapeDrawer(batch, region);
    }

    Field(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        this.field = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = new Cell((int)getX()+ i * 20,(int)getY()+ j*20);
            }
        }
    }
    public void evolve(){
        System.out.println("alive : " + field[0][0].alive);
        System.out.println("BEGIN EVOLVE");
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            evolve = !evolve;
            System.out.println("space pressed");
        }
        if (!evolve){return;}
        Cell [][] state = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                state[i][j] = Cell.getCellFromCell(field[i][j]);
            }
        }
        timeForNextMove += Gdx.graphics.getDeltaTime() * 1000;
        if (timeForNextMove > TIME_FOR_NEXT_MOVE_MAX) {
            timeForNextMove -= TIME_FOR_NEXT_MOVE_MAX;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    boolean alive = state[i][j].alive;
                    if (alive && aliveNeighbors(state, i, j) < 2) {
                        System.out.println("less than two neighbors");
                        field[i][j].alive = false;
                    }
                    if (alive && (aliveNeighbors(state, i, j) == 2 || aliveNeighbors(state, i, j) == 3)) {
                        System.out.println("two or three neighbors");
                        field[i][j].alive = true;
                    }
                    if (alive && aliveNeighbors(state, i, j) > 3) {
                        System.out.println("more than three neighbors");

                        field[i][j].alive = false;
                    }
                    if (!alive && aliveNeighbors(state, i, j) == 3) {
                        field[i][j].alive = true;
                    }
                }

            }
            System.out.println("END EVOLVE");
        }
    }
    public int aliveNeighbors(Cell[][] state,int i, int j) {
        int cpt = 0;
        try {
            if (state[i - 1][j].alive) cpt++;
        } catch (ArrayIndexOutOfBoundsException e){}
        try {
            if (state[i + 1][j].alive)cpt++;
        } catch (ArrayIndexOutOfBoundsException e){}
        try{
        if (state[i][j + 1].alive)cpt++;
        } catch (ArrayIndexOutOfBoundsException e){}
        try{
            if (state[i][j - 1].alive)cpt++;
        } catch (ArrayIndexOutOfBoundsException e){}
        try {
            if (state[i - 1][j - 1].alive) cpt++;
        } catch (ArrayIndexOutOfBoundsException e){}
        try {
            if (state[i - 1][j + 1].alive) cpt++;
        }catch (ArrayIndexOutOfBoundsException e){}
        try {
            if (state[i + 1][j - 1].alive) cpt++;
        }catch (ArrayIndexOutOfBoundsException e){}
        try {
            if (state[i + 1][j + 1].alive) cpt++;
        }catch (ArrayIndexOutOfBoundsException e){}
        return cpt;
    }
}
