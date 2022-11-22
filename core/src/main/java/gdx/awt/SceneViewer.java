/*
 * Created by JFormDesigner on Wed Nov 02 13:26:36 EDT 2022
 */

package gdx.awt;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import gdx.awt.bullet.BulletPhysics;
import gdx.awt.bullet.screens.BasicCollisionDetection;
import gdx.awt.bullet.screens.TerrainScreen;
import info.clearthought.layout.*;


/**
 * @author unknown
 */
public class SceneViewer extends JFrame {
    LwjglAWTCanvas renderCanvas;
    Editor editor;
    public SceneViewer() {
        editor = new Editor();

        renderCanvas = new LwjglAWTCanvas(new BulletPhysics());
        //Load the canvas into a JPanel

        this.initComponents();
        RenderView.add(renderCanvas.getCanvas());
        //make the mouse contained in the canvas
        renderCanvas.getCanvas().setFocusable(true);

    }

    public void addUIOptions() {
        //add buttons which change the render context
        SelectScreen.add(
                new JButton("Basic Collision Detection") {{
                    this.addActionListener(e -> editor.setScreen(new BasicCollisionDetection(editor)));
                }},
                new JButton("Terrain Screen") {{
                    this.addActionListener(e -> editor.setScreen(new TerrainScreen(editor)));
                }}
        );
    }
    private void initComponents() {
        FlatAtomOneDarkIJTheme.setup();
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        JPanel dialogPane = new JPanel();
        contentPanel = new JPanel();
        RenderView = new JPanel();
        scrollPane1 = new JPanel();
        label1 = new JLabel();
        slider2 = new JSlider();
        label2 = new JLabel();
        slider3 = new JSlider();
        label3 = new JLabel();
        slider4 = new JSlider();
        label4 = new JLabel();
        slider5 = new JSlider();
        label5 = new JLabel();
        slider6 = new JSlider();
        checkBox1 = new JCheckBox();
        checkBox2 = new JCheckBox();
        buttonBar = new JPanel();
        toggleButton1 = new JToggleButton();
        okButton = new JButton();
        cancelButton = new JButton();
        SelectScreen = new JPopupMenu();
        this.addUIOptions();

        //======== this ========
        this.setResizable(false);
        this.setMinimumSize(new Dimension(1000, 700));
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {631, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setMinimumSize(null);
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {602, 60, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

                //======== RenderView ========
                {
                    RenderView.setLayout(new BoxLayout(RenderView, BoxLayout.X_AXIS));
                }
                contentPanel.add(RenderView, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 2, 3), 0, 0));

                //======== scrollPane1 ========
                {
                    scrollPane1.setPreferredSize(new Dimension(100, 10));
                    scrollPane1.setLayout(new TableLayout(new double[][] {
                        {TableLayout.FILL, TableLayout.FILL},
                        {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED}}));

                    //---- label1 ----
                    label1.setText("text");
                    scrollPane1.add(label1, new TableLayoutConstraints(0, 0, 0, 0, TableLayoutConstraints.CENTER, TableLayoutConstraints.CENTER));
                    scrollPane1.add(slider2, new TableLayoutConstraints(1, 0, 1, 0, TableLayoutConstraints.LEFT, TableLayoutConstraints.FULL));

                    //---- label2 ----
                    label2.setText("text");
                    scrollPane1.add(label2, new TableLayoutConstraints(0, 1, 0, 1, TableLayoutConstraints.CENTER, TableLayoutConstraints.CENTER));
                    scrollPane1.add(slider3, new TableLayoutConstraints(1, 1, 1, 1, TableLayoutConstraints.LEFT, TableLayoutConstraints.FULL));

                    //---- label3 ----
                    label3.setText("text");
                    scrollPane1.add(label3, new TableLayoutConstraints(0, 2, 0, 2, TableLayoutConstraints.CENTER, TableLayoutConstraints.CENTER));
                    scrollPane1.add(slider4, new TableLayoutConstraints(1, 2, 1, 2, TableLayoutConstraints.LEFT, TableLayoutConstraints.FULL));

                    //---- label4 ----
                    label4.setText("text");
                    scrollPane1.add(label4, new TableLayoutConstraints(0, 3, 0, 3, TableLayoutConstraints.CENTER, TableLayoutConstraints.CENTER));
                    scrollPane1.add(slider5, new TableLayoutConstraints(1, 3, 1, 3, TableLayoutConstraints.LEFT, TableLayoutConstraints.FULL));

                    //---- label5 ----
                    label5.setText("text");
                    scrollPane1.add(label5, new TableLayoutConstraints(0, 4, 0, 4, TableLayoutConstraints.CENTER, TableLayoutConstraints.CENTER));
                    scrollPane1.add(slider6, new TableLayoutConstraints(1, 4, 1, 4, TableLayoutConstraints.LEFT, TableLayoutConstraints.FULL));

                    //---- checkBox1 ----
                    checkBox1.setText("text");
                    scrollPane1.add(checkBox1, new TableLayoutConstraints(0, 5, 0, 5, TableLayoutConstraints.CENTER, TableLayoutConstraints.CENTER));

                    //---- checkBox2 ----
                    checkBox2.setText("text");
                    scrollPane1.add(checkBox2, new TableLayoutConstraints(1, 5, 1, 5, TableLayoutConstraints.CENTER, TableLayoutConstraints.CENTER));
                }
                contentPanel.add(scrollPane1, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 2, 0), 0, 0));

                //======== buttonBar ========
                {
                    buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                    buttonBar.setMaximumSize(new Dimension(2147483647, 40));
                    buttonBar.setMinimumSize(new Dimension(182, 40));
                    buttonBar.setPreferredSize(new Dimension(182, 40));
                    buttonBar.setLayout(new FlowLayout());

                    //---- toggleButton1 ----
                    toggleButton1.setText("text");
                    buttonBar.add(toggleButton1);

                    //---- okButton ----
                    okButton.setText("OK");
                    buttonBar.add(okButton);

                    //---- cancelButton ----
                    cancelButton.setText("Cancel");
                    buttonBar.add(cancelButton);
                    buttonBar.add(SelectScreen);
                }
                contentPanel.add(buttonBar, new GridBagConstraints(0, 1, 5, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        this.setSize(1000, 700);
        this.setLocationRelativeTo(this.getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    public JPanel contentPanel;
    public JPanel RenderView;
    private JPanel scrollPane1;
    private JLabel label1;
    private JSlider slider2;
    private JLabel label2;
    private JSlider slider3;
    private JLabel label3;
    private JSlider slider4;
    private JLabel label4;
    private JSlider slider5;
    private JLabel label5;
    private JSlider slider6;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    public JPanel buttonBar;
    private JToggleButton toggleButton1;
    private JButton okButton;
    private JButton cancelButton;
    private JPopupMenu SelectScreen;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
