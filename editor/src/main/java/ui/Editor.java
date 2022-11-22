package ui;

import java.awt.event.*;
import java.beans.*;
import javax.swing.border.*;
import javax.swing.event.*;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.formdev.flatlaf.intellijthemes.FlatMonocaiIJTheme;

import gdx.editor.EditorApplicationAdapter;

import java.awt.*;
import java.util.Random;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import info.clearthought.layout.*;
import net.mgsx.gltf.scene3d.scene.SceneModel;
import org.jdesktop.swingx.*;
import squidpony.squidgrid.mapping.HeightMapFactory;

/*
 * Created by JFormDesigner on Sun Nov 13 13:29:01 EST 2022
 */



/**
 * @author unknown
 */
public class Editor extends JFrame {
    TreeModel model;
    SceneModel sceneModel;
    LwjglAWTCanvas awtCanvas;
    SceneGraph sceneGraphModel;
    EditorApplicationAdapter renderer;
    Logger log = Logger.getLogger(Editor.class.getName());

    public Editor() {
        renderer = new EditorApplicationAdapter();
        awtCanvas = new LwjglAWTCanvas(renderer);
        this.initComponents();
        RenderContext.add(awtCanvas.getCanvas());
        LightSelector.setModel(new DefaultComboBoxModel<>(new String[]
                {
                "DirectionalLight (Sun)",
                "Spotlight",
                "Pointlight"
                }
        ));
    }

    private void SceneSelector(ActionEvent e) {
        System.out.println("event fired");
    }

    private void FileChooser(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            System.out.println("Selected file: " + fileChooser.getSelectedFile().getAbsolutePath());
        }
        String path = fileChooser.getSelectedFile().getAbsolutePath();
        //create a new window context for the file chooser
        SwingUtilities.invokeLater(() -> {
            //make context current
            awtCanvas.makeCurrent();
            renderer.createModel(path);
        });

        log.info("Model loaded from path " + path);



    }

    private void AxesFlag(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            awtCanvas.makeCurrent();

            renderer.toggleAxes();
            log.info("Axes flag toggled");
        });
    }

    private void SkyboxFlag(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            renderer.toggleSkybox();
        });

        log.info("Skybox flag toggled");
    }

    private void SceneGraphValueChanged(TreeSelectionEvent e) {
        // TODO add your code here
    }

    private void ColorSelect(ActionEvent e) {
        JColorChooser colorChooser = new JColorChooser();


        Color color = JColorChooser.showDialog(this, "Choose a color", Color.BLACK);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        int hex = color.getRGB();
        ColorValueR.setText(Integer.toString(r));
        ColorValueG.setText(Integer.toString(g));
        ColorValueB.setText(Integer.toString(b));



    }

    private void SetColor(ActionEvent e) {
        String r = ColorValueR.getText();
        String g = ColorValueG.getText();
        String b = ColorValueB.getText();
        int red = Integer.parseInt(r);
        int green = Integer.parseInt(g);
        int blue = Integer.parseInt(b);


        SwingUtilities.invokeLater(() -> {
            awtCanvas.makeCurrent();
            renderer.setLightColor(red, green, blue);


        });
    }

    private void ColorValueRPropertyChange(PropertyChangeEvent e) {
        String r = ColorValueR.getText();
        String g = ColorValueG.getText();
        String b = ColorValueB.getText();
        int red = Integer.parseInt(r);
        int green = Integer.parseInt(g);
        int blue = Integer.parseInt(b);


        SwingUtilities.invokeLater(() -> {
            awtCanvas.makeCurrent();
            renderer.setLightColor(red, green, blue);


        });    }

        private void FOVSliderStateChanged(ChangeEvent e) {
            renderer.camera.fieldOfView = FOVSlider.getValue();
            FOVValue.setText(Integer.toString(FOVSlider.getValue()));
        }

        private void FarSliderStateChanged(ChangeEvent e) {
            renderer.camera.far = FarSlider.getValue();
            FarValue.setText(Integer.toString(FarSlider.getValue()));
        }

        private void NearSliderStateChanged(ChangeEvent e) {
            renderer.camera.near = (float)(NearSlider.getValue());
            NearValue.setText(Integer.toString(NearSlider.getValue()));

        }

        private void TogglePane(ActionEvent e) {
            // TODO add your code here
        }

        private void PostProcessingFlag(ActionEvent e) {
            renderer.psxFlag = PostProcessingFlag.isSelected();
        }

        private void SetTerrain(ActionEvent e) {
            int seed = Integer.parseInt(SeedValueField.getText());
            int width = 128;
            int height = 128;
            float[][] data2d = HeightMapFactory.heightMapSeeded(width,height,seed);
            //convert to 1d array
            float[] data = new float[width*height];
            for(int i = 0; i < width; i++){
                System.arraycopy(data2d[ i ], 0, data, i * height, height);
            }
            System.out.println("data length: " + data.length);
            SwingUtilities.invokeLater(() -> {
                awtCanvas.makeCurrent();
                renderer.createTerrain(data, TerrainMagnitudeSlider.getValue());
            });


        }

        private void SetRandomSeed(ActionEvent e) {
            Random rand = new Random();
            int seed = rand.nextInt(1000000);
            SeedValueField.setText(Integer.toString(seed));
        }

        private void TerrainMagnitudeSliderStateChanged(ChangeEvent e) {
            int magnitude = TerrainMagnitudeSlider.getValue();
            renderer.setTerrainMagnitude(magnitude);

        }

    private void initComponents() {
        FlatMonocaiIJTheme.setup();
        sceneModel = new SceneModel();
        sceneGraphModel = new SceneGraph();
        MutableComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement("Scene 1");
        comboBoxModel.addElement("Scene 2");



        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        SceneSelector = new JComboBox();
        FileChooser = new JButton();
        panel3 = new JPanel();
        AxesFlag = new JCheckBox();
        SkyboxFlag = new JCheckBox();
        PostProcessingFlag = new JCheckBox();
        panel2 = new JPanel();
        scrollPane1 = new JScrollPane();
        SceneGraph = new JXTree();
        Parameters = new JTabbedPane();
        LightParameters = new JPanel();
        LightSelector = new JComboBox();
        ColorLabel = new JLabel();
        separator2 = new JSeparator();
        panel4 = new JPanel();
        ColorValueR = new JTextField();
        ColorValueG = new JTextField();
        ColorValueB = new JTextField();
        splitPane1 = new JSplitPane();
        ColorSelectButton = new JButton();
        ColorSetButton = new JButton();
        separator1 = new JSeparator();
        CameraParameters = new JPanel();
        Near = new JLabel();
        NearSlider = new JSlider();
        NearValue = new JTextField();
        Far = new JLabel();
        FarSlider = new JSlider();
        FarValue = new JTextField();
        FOV = new JLabel();
        FOVSlider = new JSlider();
        FOVValue = new JTextField();
        separator3 = new JSeparator();
        TerrainParameters = new JPanel();
        MagnitudeLabel = new JLabel();
        SeedLabel = new JLabel();
        TerrainMagnitudeSlider = new JSlider();
        SeedValueField = new JTextField();
        SetTerrainButton = new JButton();
        SetRandomSeedButton = new JButton();
        separator4 = new JSeparator();
        HeightmapLabel = new JLabel();
        LoadHeightmapButton = new JButton();
        SeedLabel2 = new JLabel();
        LoadedHeightmapField = new JTextField();
        separator6 = new JSeparator();
        ResetTerrain = new JButton();
        ModelParameters = new JPanel();
        ModelSelectedLabel = new JLabel();
        SeedValueField2 = new JTextField();
        separator5 = new JSeparator();
        Animations = new JLabel();
        NoiseTypeBox2 = new JComboBox<>();
        ToggleAnimationButton = new JButton();
        SetAnimationButton2 = new JButton();
        RenderContext = new JPanel();

        //======== this ========
        this.setMinimumSize(new Dimension(1400, 720));
        this.setBackground(SystemColor.controlDkShadow);
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {262, 854, 0, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};

        //======== panel1 ========
        {
            panel1.setLayout(new GridLayout());

            //---- SceneSelector ----
            SceneSelector.addActionListener(e -> this.SceneSelector(e));
            panel1.add(SceneSelector);

            //---- FileChooser ----
            FileChooser.setText("LOAD MODEL");
            FileChooser.addActionListener(e -> this.FileChooser(e));
            panel1.add(FileChooser);
        }
        contentPane.add(panel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 2), 0, 0));

        //======== panel3 ========
        {
            panel3.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

            //---- AxesFlag ----
            AxesFlag.setText("Enable Axes");
            AxesFlag.setSelected(true);
            AxesFlag.addActionListener(e -> this.AxesFlag(e));
            panel3.add(AxesFlag);

            //---- SkyboxFlag ----
            SkyboxFlag.setText("Enable Skybox");
            SkyboxFlag.setSelected(true);
            SkyboxFlag.addActionListener(e -> this.SkyboxFlag(e));
            panel3.add(SkyboxFlag);

            //---- PostProcessingFlag ----
            PostProcessingFlag.setText("Enable Post-Processing");
            PostProcessingFlag.addActionListener(e -> this.PostProcessingFlag(e));
            panel3.add(PostProcessingFlag);
        }
        contentPane.add(panel3, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 2), 0, 0));

        //======== panel2 ========
        {
            panel2.setPreferredSize(new Dimension(175, 615));
            panel2.setLayout(new TableLayout(new double[][] {
                {TableLayout.FILL},
                {145, TableLayout.FILL, 160}}));
            ((TableLayout)panel2.getLayout()).setHGap(8);
            ((TableLayout)panel2.getLayout()).setVGap(5);

            //======== scrollPane1 ========
            {

                //---- SceneGraph ----
                SceneGraph.setModel(new DefaultTreeModel(
                    new DefaultMutableTreeNode("Scene") {
                        {
                            DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Renderables");
                                node1.add(new DefaultMutableTreeNode("Terrain"));
                            this.add(node1);
                            node1 = new DefaultMutableTreeNode("Lights");
                                DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("DirectionalLight");
                                    node2.add(new DefaultMutableTreeNode("Sun"));
                                node1.add(node2);
                                node2 = new DefaultMutableTreeNode("PointLight");
                                    node2.add(new DefaultMutableTreeNode("Empty"));
                                node1.add(node2);
                                node2 = new DefaultMutableTreeNode("SpotLight");
                                    node2.add(new DefaultMutableTreeNode("Empty"));
                                node1.add(node2);
                            this.add(node1);
                            node1 = new DefaultMutableTreeNode("Camera");
                                node1.add(new DefaultMutableTreeNode("PerspectiveCamera"));
                                node1.add(new DefaultMutableTreeNode("CameraController"));
                            this.add(node1);
                        }
                    }));
                SceneGraph.addTreeSelectionListener(e -> this.SceneGraphValueChanged(e));
                scrollPane1.setViewportView(SceneGraph);
            }
            panel2.add(scrollPane1, new TableLayoutConstraints(0, 0, 0, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

            //======== Parameters ========
            {

                //======== LightParameters ========
                {
                    LightParameters.setLayout(new GridBagLayout());
                    ((GridBagLayout)LightParameters.getLayout()).columnWidths = new int[] {102, 0, 0};
                    ((GridBagLayout)LightParameters.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    ((GridBagLayout)LightParameters.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
                    ((GridBagLayout)LightParameters.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
                    LightParameters.add(LightSelector, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- ColorLabel ----
                    ColorLabel.setText("Color");
                    ColorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    LightParameters.add(ColorLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(0, 5, 5, 5), 0, 0));

                    //---- separator2 ----
                    separator2.setBorder(new EtchedBorder());
                    LightParameters.add(separator2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //======== panel4 ========
                    {
                        panel4.setMaximumSize(null);
                        panel4.setMinimumSize(new Dimension(10, 36));
                        panel4.setPreferredSize(new Dimension(30, 36));
                        panel4.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                        panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 7));

                        //---- ColorValueR ----
                        ColorValueR.setMinimumSize(new Dimension(16, 26));
                        ColorValueR.setPreferredSize(new Dimension(30, 26));
                        ColorValueR.setText("255");
                        ColorValueR.setMargin(null);
                        ColorValueR.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                        ColorValueR.setHorizontalAlignment(SwingConstants.CENTER);
                        ColorValueR.addPropertyChangeListener("text", e -> this.ColorValueRPropertyChange(e));
                        panel4.add(ColorValueR);

                        //---- ColorValueG ----
                        ColorValueG.setEditable(false);
                        ColorValueG.setMinimumSize(new Dimension(16, 26));
                        ColorValueG.setPreferredSize(new Dimension(30, 26));
                        ColorValueG.setText("255");
                        ColorValueG.setMargin(null);
                        ColorValueG.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                        ColorValueG.setHorizontalAlignment(SwingConstants.CENTER);
                        panel4.add(ColorValueG);

                        //---- ColorValueB ----
                        ColorValueB.setEditable(false);
                        ColorValueB.setMinimumSize(new Dimension(16, 26));
                        ColorValueB.setPreferredSize(new Dimension(30, 26));
                        ColorValueB.setText("255");
                        ColorValueB.setMargin(null);
                        ColorValueB.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                        ColorValueB.setHorizontalAlignment(SwingConstants.CENTER);
                        panel4.add(ColorValueB);
                    }
                    LightParameters.add(panel4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 2, 5, 5), 0, 0));

                    //======== splitPane1 ========
                    {
                        splitPane1.setMinimumSize(new Dimension(50, 32));
                        splitPane1.setPreferredSize(new Dimension(100, 32));
                        splitPane1.setDividerSize(0);
                        splitPane1.setDividerLocation(50);
                        splitPane1.setBorder(null);
                        splitPane1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

                        //---- ColorSelectButton ----
                        ColorSelectButton.setText("Select");
                        ColorSelectButton.setMaximumSize(new Dimension(50, 0));
                        ColorSelectButton.setMinimumSize(null);
                        ColorSelectButton.setIconTextGap(7);
                        ColorSelectButton.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        ColorSelectButton.setPreferredSize(new Dimension(35, 28));
                        ColorSelectButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
                        ColorSelectButton.addActionListener(e -> this.ColorSelect(e));
                        splitPane1.setLeftComponent(ColorSelectButton);

                        //---- ColorSetButton ----
                        ColorSetButton.setText("Set");
                        ColorSetButton.setMaximumSize(new Dimension(20, 0));
                        ColorSetButton.setMinimumSize(new Dimension(15, 30));
                        ColorSetButton.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        ColorSetButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
                        ColorSetButton.setIconTextGap(10);
                        ColorSetButton.addActionListener(e -> this.SetColor(e));
                        splitPane1.setRightComponent(ColorSetButton);
                    }
                    LightParameters.add(splitPane1, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 2, 5, 5), 0, 0));

                    //---- separator1 ----
                    separator1.setBorder(new EtchedBorder());
                    LightParameters.add(separator1, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 5, 0), 0, 0));
                }
                Parameters.addTab("Lights", LightParameters);

                //======== CameraParameters ========
                {
                    CameraParameters.setLayout(new GridBagLayout());
                    ((GridBagLayout)CameraParameters.getLayout()).columnWidths = new int[] {41, 180, 0, 0};
                    ((GridBagLayout)CameraParameters.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    ((GridBagLayout)CameraParameters.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
                    ((GridBagLayout)CameraParameters.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                    //---- Near ----
                    Near.setText("NEAR:");
                    CameraParameters.add(Near, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- NearSlider ----
                    NearSlider.setMinimum(1);
                    NearSlider.setValue(1);
                    NearSlider.setMaximum(10);
                    NearSlider.setSnapToTicks(true);
                    NearSlider.addChangeListener(e -> this.NearSliderStateChanged(e));
                    CameraParameters.add(NearSlider, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- NearValue ----
                    NearValue.setMinimumSize(new Dimension(40, 26));
                    NearValue.setPreferredSize(new Dimension(30, 26));
                    NearValue.setText("1");
                    NearValue.setMargin(null);
                    NearValue.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    NearValue.setHorizontalAlignment(SwingConstants.CENTER);
                    NearValue.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    NearValue.addPropertyChangeListener("text", e -> this.ColorValueRPropertyChange(e));
                    CameraParameters.add(NearValue, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- Far ----
                    Far.setText("FAR:");
                    CameraParameters.add(Far, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- FarSlider ----
                    FarSlider.setMaximum(10000);
                    FarSlider.setMinimum(100);
                    FarSlider.setValue(5000);
                    FarSlider.addChangeListener(e -> this.FarSliderStateChanged(e));
                    CameraParameters.add(FarSlider, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- FarValue ----
                    FarValue.setMinimumSize(new Dimension(40, 26));
                    FarValue.setPreferredSize(new Dimension(30, 26));
                    FarValue.setText("5000");
                    FarValue.setMargin(null);
                    FarValue.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    FarValue.setHorizontalAlignment(SwingConstants.CENTER);
                    FarValue.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    FarValue.addPropertyChangeListener("text", e -> this.ColorValueRPropertyChange(e));
                    CameraParameters.add(FarValue, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- FOV ----
                    FOV.setText("FOV: ");
                    FOV.setHorizontalAlignment(SwingConstants.LEFT);
                    CameraParameters.add(FOV, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- FOVSlider ----
                    FOVSlider.setMaximum(120);
                    FOVSlider.setValue(70);
                    FOVSlider.setMinimum(1);
                    FOVSlider.addChangeListener(e -> this.FOVSliderStateChanged(e));
                    CameraParameters.add(FOVSlider, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- FOVValue ----
                    FOVValue.setMinimumSize(new Dimension(40, 26));
                    FOVValue.setPreferredSize(new Dimension(30, 26));
                    FOVValue.setText("70");
                    FOVValue.setMargin(null);
                    FOVValue.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    FOVValue.setHorizontalAlignment(SwingConstants.CENTER);
                    FOVValue.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    FOVValue.addPropertyChangeListener("text", e -> this.ColorValueRPropertyChange(e));
                    CameraParameters.add(FOVValue, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- separator3 ----
                    separator3.setBorder(new EtchedBorder());
                    CameraParameters.add(separator3, new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 5, 0), 0, 0));
                }
                Parameters.addTab("Camera", CameraParameters);

                //======== TerrainParameters ========
                {
                    TerrainParameters.setLayout(new GridBagLayout());
                    ((GridBagLayout)TerrainParameters.getLayout()).columnWidths = new int[] {0, 0, 0};
                    ((GridBagLayout)TerrainParameters.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    ((GridBagLayout)TerrainParameters.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0E-4};
                    ((GridBagLayout)TerrainParameters.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                    //---- MagnitudeLabel ----
                    MagnitudeLabel.setText("Magnitude");
                    TerrainParameters.add(MagnitudeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(5, 0, 8, 5), 0, 0));

                    //---- SeedLabel ----
                    SeedLabel.setText("Seed");
                    TerrainParameters.add(SeedLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(5, 5, 8, 5), 0, 0));

                    //---- TerrainMagnitudeSlider ----
                    TerrainMagnitudeSlider.setMinimum(1);
                    TerrainMagnitudeSlider.setValue(20);
                    TerrainMagnitudeSlider.addChangeListener(e -> this.TerrainMagnitudeSliderStateChanged(e));
                    TerrainParameters.add(TerrainMagnitudeSlider, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 8, 5), 0, 0));

                    //---- SeedValueField ----
                    SeedValueField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    TerrainParameters.add(SeedValueField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 8, 0), 0, 0));

                    //---- SetTerrainButton ----
                    SetTerrainButton.setText("Generate");
                    SetTerrainButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
                    SetTerrainButton.addActionListener(e -> this.SetTerrain(e));
                    TerrainParameters.add(SetTerrainButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 5, 8, 10), 0, 0));

                    //---- SetRandomSeedButton ----
                    SetRandomSeedButton.setText("Randomize");
                    SetRandomSeedButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
                    SetRandomSeedButton.addActionListener(e -> this.SetRandomSeed(e));
                    TerrainParameters.add(SetRandomSeedButton, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 8, 5), 0, 0));

                    //---- separator4 ----
                    separator4.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    TerrainParameters.add(separator4, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 8, 0), 0, 0));

                    //---- HeightmapLabel ----
                    HeightmapLabel.setText("Use Heightmap");
                    TerrainParameters.add(HeightmapLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 8, 5), 0, 0));

                    //---- LoadHeightmapButton ----
                    LoadHeightmapButton.setText("Load");
                    LoadHeightmapButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
                    TerrainParameters.add(LoadHeightmapButton, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 8, 5), 0, 0));
                    TerrainParameters.add(SeedLabel2, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(5, 5, 8, 5), 0, 0));

                    //---- LoadedHeightmapField ----
                    LoadedHeightmapField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    TerrainParameters.add(LoadedHeightmapField, new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 5, 8, 5), 0, 0));

                    //---- separator6 ----
                    separator6.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    TerrainParameters.add(separator6, new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 8, 0), 0, 0));

                    //---- ResetTerrain ----
                    ResetTerrain.setText("Generate");
                    ResetTerrain.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
                    ResetTerrain.setEnabled(false);
                    TerrainParameters.add(ResetTerrain, new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 5, 0, 5), 0, 0));
                }
                Parameters.addTab("Terrain", TerrainParameters);

                //======== ModelParameters ========
                {
                    ModelParameters.setLayout(new GridBagLayout());
                    ((GridBagLayout)ModelParameters.getLayout()).columnWidths = new int[] {0, 0, 0};
                    ((GridBagLayout)ModelParameters.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    ((GridBagLayout)ModelParameters.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0E-4};
                    ((GridBagLayout)ModelParameters.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                    //---- ModelSelectedLabel ----
                    ModelSelectedLabel.setText("Selection");
                    ModelParameters.add(ModelSelectedLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 5, 10, 12), 0, 0));

                    //---- SeedValueField2 ----
                    SeedValueField2.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                    ModelParameters.add(SeedValueField2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                        new Insets(3, 0, 10, 8), 0, 0));

                    //---- separator5 ----
                    separator5.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    ModelParameters.add(separator5, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 10, 0), 0, 0));

                    //---- Animations ----
                    Animations.setText("Animation");
                    ModelParameters.add(Animations, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(5, 0, 10, 7), 0, 0));

                    //---- NoiseTypeBox2 ----
                    NoiseTypeBox2.setModel(new DefaultComboBoxModel<>(new String[] {
                        "Empty"
                    }));
                    ModelParameters.add(NoiseTypeBox2, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 10, 0), 0, 0));

                    //---- ToggleAnimationButton ----
                    ToggleAnimationButton.setText("Toggle");
                    ToggleAnimationButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
                    ModelParameters.add(ToggleAnimationButton, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 5, 10, 12), 0, 0));

                    //---- SetAnimationButton2 ----
                    SetAnimationButton2.setText("Set");
                    SetAnimationButton2.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
                    ModelParameters.add(SetAnimationButton2, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 5, 10, 5), 0, 0));
                }
                Parameters.addTab("Models", ModelParameters);
            }
            panel2.add(Parameters, new TableLayoutConstraints(0, 1, 0, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        }
        contentPane.add(panel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 2), 10, 0));

        //======== RenderContext ========
        {
            RenderContext.setMinimumSize(new Dimension(1280, 720));
            RenderContext.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            RenderContext.setLayout(new BorderLayout());
        }
        contentPane.add(RenderContext, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 2), 0, 0));
        this.pack();
        this.setLocationRelativeTo(this.getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panel1;
    private JComboBox SceneSelector;
    private JButton FileChooser;
    private JPanel panel3;
    private JCheckBox AxesFlag;
    private JCheckBox SkyboxFlag;
    private JCheckBox PostProcessingFlag;
    private JPanel panel2;
    private JScrollPane scrollPane1;
    private JXTree SceneGraph;
    private JTabbedPane Parameters;
    private JPanel LightParameters;
    private JComboBox LightSelector;
    private JLabel ColorLabel;
    private JSeparator separator2;
    private JPanel panel4;
    private JTextField ColorValueR;
    private JTextField ColorValueG;
    private JTextField ColorValueB;
    private JSplitPane splitPane1;
    private JButton ColorSelectButton;
    private JButton ColorSetButton;
    private JSeparator separator1;
    private JPanel CameraParameters;
    private JLabel Near;
    private JSlider NearSlider;
    private JTextField NearValue;
    private JLabel Far;
    private JSlider FarSlider;
    private JTextField FarValue;
    private JLabel FOV;
    private JSlider FOVSlider;
    private JTextField FOVValue;
    private JSeparator separator3;
    private JPanel TerrainParameters;
    private JLabel MagnitudeLabel;
    private JLabel SeedLabel;
    private JSlider TerrainMagnitudeSlider;
    private JTextField SeedValueField;
    private JButton SetTerrainButton;
    private JButton SetRandomSeedButton;
    private JSeparator separator4;
    private JLabel HeightmapLabel;
    private JButton LoadHeightmapButton;
    private JLabel SeedLabel2;
    private JTextField LoadedHeightmapField;
    private JSeparator separator6;
    private JButton ResetTerrain;
    private JPanel ModelParameters;
    private JLabel ModelSelectedLabel;
    private JTextField SeedValueField2;
    private JSeparator separator5;
    private JLabel Animations;
    private JComboBox<String> NoiseTypeBox2;
    private JButton ToggleAnimationButton;
    private JButton SetAnimationButton2;
    public JPanel RenderContext;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

}
