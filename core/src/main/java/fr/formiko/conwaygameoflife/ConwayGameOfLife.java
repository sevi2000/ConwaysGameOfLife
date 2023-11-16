package fr.formiko.conwaygameoflife;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ConwayGameOfLife extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private Stage stage;
    private Field f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        stage = new Stage();
        f = new Field(15,12);
        f.field[1][0].birth();
        f.field[1][1].birth();
        f.field[1][2].birth();
        stage.addActor(f);
        for (int i = 0; i < f.field.length; i++) {
            for (int j = 0; j < f.field[i].length; j++) {
             stage.addActor(f.field[i][j]);
            }
        }
        System.out.println();
        //System.out.println(Arrays.toString(f.field));

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        f.evolve();

    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
