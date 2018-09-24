package men.brakh.lfsr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class LfsrFrame extends JFrame {
    private String currentPath;
    private JRadioButton radioIsEncrypt = new JRadioButton("Encode");
    private JRadioButton radioIsDecrypt = new JRadioButton("Decode");

    private JButton btnSelectFile = new JButton("Select input file");
    private JLabel lblCurrentFile = new JLabel("input.txt");

    private JLabel lblRegister = new JLabel("Default register location:");
    private JTextField inpRegister = new JTextField("101001111111111111111011",40);

    private JButton btnApply = new JButton("To do everything!\n");

    public LfsrFrame() {
        super("Perfect LFSR encoder/decoder");
        this.setBounds(100,100,500,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(10,1));
        container.add(lblRegister);
        container.add(inpRegister);

        lblCurrentFile.setVerticalAlignment(JLabel.CENTER);
        container.add(btnSelectFile);
        container.add(lblCurrentFile);

        ButtonGroup bgSelectMode = new ButtonGroup();
        Panel pnlSelectMode = new Panel(new GridLayout(1,2));
        bgSelectMode.add(radioIsDecrypt);
        bgSelectMode.add(radioIsEncrypt);
        pnlSelectMode.add(radioIsDecrypt);
        pnlSelectMode.add(radioIsEncrypt);
        container.add(pnlSelectMode);

        radioIsEncrypt.setSelected(true);

        btnSelectFile.addActionListener(new SelectFile());
        btnApply.addActionListener(new ButtonEventListener());
        container.add(btnApply);
    }
    public String trimStr(String str) {
        if (str.length() > 40) {
            return str.substring(0, 40) + "...";
        }
        return  null;
    }
    void dialogMSG(String message, String title) {
        JOptionPane.showMessageDialog(null,
                message,
                title,
                JOptionPane.PLAIN_MESSAGE);
    }

    class SelectFile implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileopen = new JFileChooser();
            fileopen.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int ret = fileopen.showDialog(null, "OpenFile");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                currentPath = file.getAbsolutePath();
                lblCurrentFile.setText(file.getName());
            }
        }
    }
    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            FilesEncoder filesEncoder = new FilesEncoder();
            try {
                String key;
                String type;
                if (radioIsEncrypt.isSelected()) {
                    key = filesEncoder.encode(currentPath, inpRegister.getText());
                    type = "Encrypted";
                } else {
                    key = filesEncoder.decode(currentPath, inpRegister.getText());
                    type = "Decrypted";
                }
                dialogMSG(String.format("%s!\nInitial register: %s\nKey: %s",
                        type, inpRegister.getText(), trimStr(key)), "%s");
            }
            catch(InvalidRegisterException ex) {
                dialogMSG(ex.getMessage(), "ERROR");
            }
            catch(IOException ex) {
                dialogMSG("Invalid file", "ERROR");
            }
            catch (NumberFormatException ex) {
                dialogMSG("Invalid register", "ERROR");
            }

        }
    }

    public static void main(String[] args) {
        LfsrFrame app = new LfsrFrame();
        app.setVisible(true);
    }
}
