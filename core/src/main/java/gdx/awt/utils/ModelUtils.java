package gdx.awt.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class ModelUtils {
    public static ModelInstance createModelInstance(Color color) {
        ModelBuilder modelBuilder = new ModelBuilder();

        Material boxMaterial = new Material();
        boxMaterial.set( ColorAttribute.createDiffuse(color) );

        int usageCode = VertexAttributes.Usage.Position + VertexAttributes.Usage.ColorPacked + VertexAttributes.Usage.Normal;

        Model boxModel = modelBuilder.createBox( 5f, 5f, 5f, boxMaterial, usageCode );

        return new ModelInstance(boxModel);
    }
}
