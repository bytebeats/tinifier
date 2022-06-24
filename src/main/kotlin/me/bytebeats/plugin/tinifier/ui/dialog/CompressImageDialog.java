package me.bytebeats.plugin.tinifier.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.tree.TreeUtil;
import me.bytebeats.plugin.tinifier.Preferences;
import me.bytebeats.plugin.tinifier.ui.ToolbarsKt;
import me.bytebeats.plugin.tinifier.ui.filetree.FileTreeCellRenderer;
import me.bytebeats.plugin.tinifier.ui.component.JImage;
import me.bytebeats.plugin.tinifier.ui.dialog.listener.ImageSelectionListener;
import me.bytebeats.plugin.tinifier.ui.dialog.listener.impl.CancelActionListener;
import me.bytebeats.plugin.tinifier.ui.dialog.listener.impl.CompressActionListener;
import me.bytebeats.plugin.tinifier.ui.dialog.listener.impl.SaveActionListener;
import me.bytebeats.plugin.tinifier.ui.filetree.FileTreeNode;
import me.bytebeats.plugin.tinifier.util.ConstsKt;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class CompressImageDialog extends JDialog {
    private JPanel contentPanel;
    private JButton saveBtn;
    private JButton cancelBtn;
    private JComponent imgBeforePanel;
    private JLabel imgDetailsBeforeLabel;
    private JComponent compressedImgAfterPanel;
    private JLabel compressedImgDetailsAfterLabel;
    private JTree imgFileTree;
    private JScrollPane imgFilesScrollPane;
    private JSplitPane splitPanel;
    private JButton compressBtn;
    private JLabel imgFilesDetails;
    private JLabel imgBeforeLabel;
    private JLabel compressedImgAfterLabel;
    private JPanel toolbar;
    private JPanel actionsPanel;
    private JPanel actionsGroupPanel;
    private JPanel detailsPanel;
    private JPanel imgFilesPanel;
    private List<VirtualFile> myFiles;
    private List<VirtualFile> myRoots;
    private Project myProject;
    private boolean imageCompressInProgress;

    public CompressImageDialog(Project project, List<VirtualFile> files, List<VirtualFile> roots) {
        imageCompressInProgress = false;
        myFiles = files;
        myRoots = roots;
        myProject = project;

        setContentPane(contentPanel);
        setModal(true);
        getRootPane().setDefaultButton(compressBtn);

        compressBtn.addActionListener(new CompressActionListener(this));
        saveBtn.addActionListener(new SaveActionListener(this));

        final CancelActionListener cancelActionListener = new CancelActionListener(this);
        cancelBtn.addActionListener(cancelActionListener);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        // call onCancel() on ESCAPE
        contentPanel.registerKeyboardAction(cancelActionListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        configureUI();
    }

    public void setDialogSize(JFrame frame) {
        this.setMinimumSize(new Dimension(frame.getWidth() / 2, frame.getHeight() / 2));
        this.setLocationRelativeTo(frame);
        Preferences settings = Preferences.Companion.getInstance();
        splitPanel.setDividerLocation(settings.getDividerOffset());
        if (settings.getDialogOffsetX() != 0) {
            this.setLocation(settings.getDialogOffsetX(), settings.getDialogOffsetY());
        }

        if (settings.getDialogWidth() != 0) {
            this.setPreferredSize(settings.getDialogSize());
        }

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                settings.setDialogSize(((CompressImageDialog) e.getSource()).getSize());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                settings.setDialogLocation(((CompressImageDialog) e.getSource()).getLocation());
            }
        });
        splitPanel.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt -> settings.setDividerOffset((int) evt.getNewValue()));
        this.pack();
    }

    private void configureUI() {
        splitPanel.setBackground(UIUtil.getPanelBackground());
        splitPanel.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    private final Color background = UIUtil.getPanelBackground();
                    private final Color dashes = UIUtil.getSeparatorShadow();

                    public void setBorder(Border b) {
                    }

                    @Override
                    public void paint(Graphics g) {
                        g.setColor(background);
                        g.fillRect(0, 0, getSize().width, getSize().height);

                        int dashHeight = 40;
                        final int top = (getSize().height - dashHeight) / 2;
                        g.setColor(dashes);
                        g.drawLine(4, top, 4, top + dashHeight);
                        g.drawLine(7, top, 7, top + dashHeight);
                        super.paint(g);
                    }
                };
            }
        });
        splitPanel.setBorder(null);
        clearTitle();

        imgBeforeLabel.setForeground(JBColor.green.darker());
        compressedImgAfterLabel.setForeground(JBColor.red.darker());
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(ConstsKt.TINIFIER + (title == null ? "" : " " + title));
    }

    public void clearTitle() {
        setTitle(null);
    }

    public JTree getTree() {
        return imgFileTree;
    }

    public Project getProject() {
        return myProject;
    }

    public JImage getImageBefore() {
        return (JImage) imgBeforePanel;
    }

    public JImage getImageAfter() {
        return (JImage) compressedImgAfterPanel;
    }

    public JLabel getDetailsBefore() {
        return imgDetailsBeforeLabel;
    }

    public JLabel getDetailsAfter() {
        return compressedImgDetailsAfterLabel;
    }

    public JLabel getTotalDetails() {
        return imgFilesDetails;
    }

    public JButton getButtonSave() {
        return saveBtn;
    }

    public JButton getButtonCancel() {
        return cancelBtn;
    }

    public JButton getButtonProcess() {
        return compressBtn;
    }

    public void setCompressInProgress(boolean value) {
        imageCompressInProgress = value;
    }

    public boolean getCompressInProgress() {
        return imageCompressInProgress;
    }

    private void onClose() {
        dispose();
    }

    private void createUIComponents() {
        UIUtil.removeScrollBorder(imgFilesScrollPane);
        imgBeforePanel = new JImage();
        compressedImgAfterPanel = new JImage();
        imgFileTree = new CheckboxTree(new FileTreeCellRenderer(myProject), buildTree());
        imgFileTree.setRootVisible(false);
        imgFileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        TreeUtil.expandAll(imgFileTree);

        imgFileTree.addTreeSelectionListener(new ImageSelectionListener(this));

        configureToolbar();
    }

    private void configureToolbar() {
        toolbar = ToolbarsKt.createToolbar();
    }

    private FileTreeNode buildTree() {
        FileTreeNode root = new FileTreeNode();
        for (VirtualFile file : myFiles) {
            getParent(root, file).add(new FileTreeNode(null, null, file));
        }

        return root;
    }

    private FileTreeNode getParent(FileTreeNode root, VirtualFile file) {
        if (myRoots.contains(file)) {
            return root;
        }

        LinkedList<VirtualFile> path = new LinkedList<>();
        while (!myRoots.contains(file)) {
            file = file.getParent();
            path.addFirst(file);
        }

        FileTreeNode parent = root;
        for (VirtualFile pathElement : path) {
            FileTreeNode node = findNodeByUserObject(parent, pathElement);
            if (node == null) {
                node = new FileTreeNode(null, null, pathElement);
                parent.add(node);
            }

            parent = node;
        }

        return parent;
    }

    @Nullable
    private FileTreeNode findNodeByUserObject(FileTreeNode root, Object userObject) {
        Enumeration<TreeNode> enumeration = root.children();
        while (enumeration.hasMoreElements()) {
            FileTreeNode node = (FileTreeNode) enumeration.nextElement();
            if (node.getUserObject() == userObject) {
                return node;
            }
        }

        return null;
    }
}
