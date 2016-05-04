package com.github.czyzby.setup.data.templates.unofficial

import com.github.czyzby.setup.data.libs.unofficial.Kiwi
import com.github.czyzby.setup.data.project.Project
import com.github.czyzby.setup.data.templates.official.ClassicTemplate
import com.github.czyzby.setup.views.ProjectTemplate

/**
 * Classic project template using Kiwi utilities.
 * @author MJ
 */
@ProjectTemplate
class KiwiTemplate : ClassicTemplate() {
    override val id = "kiwiTemplate"
    private lateinit var mainClass: String
    override val width: String
        get() = mainClass + ".WIDTH"
    override val height: String
        get() = mainClass + ".HEIGHT"

    override fun apply(project: Project) {
        mainClass = project.basic.mainClass
        super.apply(project)
        // Adding gdx-kiwi dependency:
        Kiwi().initiate(project)
    }

    override fun getApplicationListenerContent(project: Project): String = """package ${project.basic.rootPackage};

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.czyzby.kiwi.util.gdx.AbstractApplicationListener;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.scene2d.Actors;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ${project.basic.mainClass} extends AbstractApplicationListener {
    /** Default application size. */
    public static final int WIDTH = 640, HEIGHT = 480;

    private Stage stage;
    private Texture texture;
    private Image image;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        texture = new Texture("badlogic.png");
        image = new Image(texture);
        stage.addActor(image);
        Actors.centerActor(image);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        Actors.centerActor(image);
    }

    @Override
    public void render(float deltaTime) {
        // AbstractApplicationListener automatically clears the screen with black color.
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void dispose() {
        // Null-safe disposing utility method:
        Disposables.disposeOf(stage, texture);
    }
}"""
}
