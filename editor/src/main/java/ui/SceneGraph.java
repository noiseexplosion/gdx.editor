package ui;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneModel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class SceneGraph extends JTree {
    PerspectiveCamera camera;
    Scene scene;
    SceneModel sceneModel;
    public DefaultMutableTreeNode root;
    DefaultMutableTreeNode cameraNode;
    DefaultMutableTreeNode sceneNode;
    DefaultMutableTreeNode renderablesNode;
    DefaultMutableTreeNode lightsNode;
    DefaultMutableTreeNode environmentNode;
    public DefaultTreeModel model;

    public SceneGraph(){
        root = new DefaultMutableTreeNode("SceneGraph");
	    this.createNodes();

        root.add(sceneNode);
        root.add(renderablesNode);
        root.add(lightsNode);
        root.add(environmentNode);
       model = new DefaultTreeModel(root);
	    this.setModel(model);



    }
    void createNodes(){
        cameraNode = new DefaultMutableTreeNode("Camera");
        sceneNode = new DefaultMutableTreeNode("Scene");
        renderablesNode = new DefaultMutableTreeNode("Renderables");
        lightsNode = new DefaultMutableTreeNode("Lights");
        environmentNode = new DefaultMutableTreeNode("Environment");
    }


}

