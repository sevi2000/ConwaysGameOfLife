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
                field[i][j] = new Cell((int)getX()+ i * 40,(int)getY()+ j*40);
            }
        }
    }
    public void evolve(){
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            evolve = true;
            System.out.println("space pressed");
        }
        if (!evolve){return;}
        Cell [][] state = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                state[i][j] = field[i][j];
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (state[i][j].alive && aliveNeighbors(state,i,j) < 2) {
                    System.out.println("two or three neighbors");
                    field[i][j].alive =false;
                }
                if ((state[i][j].alive && aliveNeighbors(state,i,j) == 2) ||
                    (state[i][j].alive && aliveNeighbors(state,i,j) == 3)) {
                    System.out.println("two or three neighbors");
                    field[i][j].alive = true;
                }
                if ((state[i][j].alive && aliveNeighbors(state,i,j)  > 3)) {
                    field[i][j].alive = false;
                }
                if(!state[i][j].alive && aliveNeighbors(state,i,j) == 3){
                    field[i][j].alive = true;
                }
            }

        }
    }
    public int aliveNeighbors(Cell[][] state,int i, int j) {
        int cpt = 0;
        try {
            if (state[i - 1][j].alive) cpt++;
            if (state[i + 1][j].alive)cpt++;
            if (state[i][j + 1].alive)cpt++;
            if (state[i][j - 1].alive)cpt++;
            if (state[i - 1][j - 1].alive) cpt++;
            if (state[i - 1][j + 1].alive) cpt++;
            if (state[i + 1][j - 1].alive) cpt++;
            if (state[i + 1][j + 1].alive) cpt++;
        } catch (ArrayIndexOutOfBoundsException e){}


        return cpt;
    }
}
