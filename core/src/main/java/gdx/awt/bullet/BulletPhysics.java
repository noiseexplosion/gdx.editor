package gdx.awt.bullet;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.bullet.Bullet;
import gdx.awt.bullet.screens.SelectScreen;
import gdx.awt.bullet.screens.SelectScreen;

public class BulletPhysics extends Game {
    @Override
    public void create() {
        Bullet.init();
	    this.setScreen(new SelectScreen(this));
    }
}


