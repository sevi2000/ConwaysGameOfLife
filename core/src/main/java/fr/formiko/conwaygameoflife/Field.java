package fr.formiko.conwaygameoflife;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


public class Field extends Actor {
    Cell [][] field;
    int width;
    int height;
    boolean evolve = false;
    private static ShapeDrawer schd = null;
    private long timeForNextMove;
    private static final long TIME_FOR_NEXT_MOVE_MAX = 250;
    private int generatons;
    BitmapFont font = new BitmapFont();
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
        font.draw(batch, "Generation : " + generatons, 10, Gdx.graphics.getHeight() - 10);
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
                field[i][j] = new Cell((int)getX()+ i * Cell.CELL_SIZE,(int)getY()+ j*Cell.CELL_SIZE);
            }
        }
        this.generatons = 0;
        font.setColor(Color.RED);
        int newSize = 20; // Change this to the desired font size
        font.getData().setScale(newSize / font.getCapHeight());
    }
    public void evolve(){
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
                        field[i][j].alive = false;
                    }
                    if (alive && (aliveNeighbors(state, i, j) == 2 || aliveNeighbors(state, i, j) == 3)) {
                        field[i][j].alive = true;
                    }
                    if (alive && aliveNeighbors(state, i, j) > 3) {

                        field[i][j].alive = false;
                    }
                    if (!alive && aliveNeighbors(state, i, j) == 3) {
                        field[i][j].alive = true;
                    }
                }

            }
            if (areArraysEquals(state,field)){
                evolve = false;
            }
            generatons++;
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
    private boolean areArraysEquals(Cell[][] state, Cell [][] field) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (state[i][j].alive != field[i][j].alive)
                    return false;
            }
        }
        return true;
    }

    public static Field fromFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        Field f = new Field(Gdx.graphics.getWidth()/Cell.CELL_SIZE,Gdx.graphics.getHeight()/Cell.CELL_SIZE);
        int i = 0;
        for(String line : lines){
            System.out.println(line);
            String [] cells = line.split(" ");
            for (int j = 0; j < cells.length; j++) {
                boolean alive = (cells[j].charAt(0) == '1');
                f.field[i][j] = new Cell(i * Cell.CELL_SIZE, j*Cell.CELL_SIZE,alive);
            }
            i++;
        }
        return f;
    }
}
