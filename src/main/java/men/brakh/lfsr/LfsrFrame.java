package men.brakh.lfsr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

public class LfsrFrame extends JFrame {
    private String currentPath;

    private JRadioButton radioIsLFSR = new JRadioButton("LFSR");
    private JRadioButton radioIsGefe = new JRadioButton("GEFE");
    private JRadioButton radioIsRc4 = new JRadioButton("RC4");

    private JRadioButton radioIsEncrypt = new JRadioButton("Encode");
    private JRadioButton radioIsDecrypt = new JRadioButton("Decode");

    private JButton btnSelectFile = new JButton("Select input file");
    private JLabel lblCurrentFile = new JLabel("input.txt");

    private JLabel lblRegister = new JLabel("Default register location:");
    private JTextField inpRegister = new JTextField("101010101010101010101010",40);
    private JTextField inpRegister2 = new JTextField("10101010101010101010101010101010",40);
    private JTextField inpRegister3 = new JTextField("1010101010101010101010101010101010101010",40);

    private JButton btnApply = new JButton("To do everything!\n");

    public LfsrFrame() {
        super("Perfect LFSR encoder/decoder");
        this.setBounds(100,100,500,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(10,1));
        container.add(lblRegister);
        container.add(inpRegister);
        container.add(inpRegister2);
        container.add(inpRegister3);

        lblCurrentFile.setVerticalAlignment(JLabel.CENTER);
        container.add(btnSelectFile);
        container.add(lblCurrentFile);


        ButtonGroup bgSelectCipher = new ButtonGroup();
        Panel pnlSelectCipher = new Panel(new GridLayout(1,3));
        bgSelectCipher.add(radioIsLFSR);
        bgSelectCipher.add(radioIsGefe);
        bgSelectCipher.add(radioIsRc4);
        pnlSelectCipher.add(radioIsLFSR);
        pnlSelectCipher.add(radioIsGefe);
        pnlSelectCipher.add(radioIsRc4);

        RadioHandler radioHandler = new RadioHandler();
        radioIsLFSR.addItemListener(radioHandler);
        radioIsGefe.addItemListener(radioHandler);
        radioIsRc4.addItemListener(radioHandler);

        container.add(pnlSelectCipher);


        ButtonGroup bgSelectMode = new ButtonGroup();
        Panel pnlSelectMode = new Panel(new GridLayout(1,2));
        bgSelectMode.add(radioIsDecrypt);
        bgSelectMode.add(radioIsEncrypt);
        pnlSelectMode.add(radioIsDecrypt);
        pnlSelectMode.add(radioIsEncrypt);
        container.add(pnlSelectMode);

        radioIsEncrypt.setSelected(true);
        radioIsLFSR.setSelected(true);

        btnSelectFile.addActionListener(new SelectFile());
        btnApply.addActionListener(new ButtonEventListener());
        container.add(btnApply);
    }

    class RadioHandler implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            if(radioIsLFSR.isSelected() || radioIsRc4.isSelected()) {
                inpRegister2.show(false);
                inpRegister3.show(false);
            } else if (radioIsGefe.isSelected()) {
                inpRegister2.show(true);
                inpRegister3.show(true);
            }
        }
    }

    public String trimStr(String str) {
        if (str.length() >= 15*8) {
            return str.substring(0, 15*8) + "...";
        }
        return  str;
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
            FilesEncoder filesEncoder = null;
            if (radioIsLFSR.isSelected()) {
                filesEncoder = new FilesEncoder(new LFSR(inpRegister.getText()));
            } else if (radioIsGefe.isSelected()) {
                filesEncoder = new FilesEncoder(new Geffe(inpRegister.getText(), inpRegister2.getText(), inpRegister3.getText()));
            } else if(radioIsRc4.isSelected()) {
                filesEncoder = new FilesEncoder(new RC4(inpRegister.getText()));
            }
            try {
                String[] arr;
                String key;
                String text;
                String type;
                String source;
                if (radioIsEncrypt.isSelected()) {

                    arr = filesEncoder.encode(currentPath);
                    key = arr[0];
                    source = arr[2];
                    text = arr[1];
                    type = "Encrypted";
                } else {
                    arr = filesEncoder.decode(currentPath);
                    key = arr[0];
                    source = arr[2];
                    text = arr[1];
                    type = "Decrypted";
                }
                dialogMSG(String.format("%s!\nInitial register: %s\nKey: %s\nSrc: %s\nFile: %s",
                        type, inpRegister.getText(), trimStr(key), trimStr(source), trimStr(text)), type);
            }
            catch(InvalidRegisterException ex) {
                dialogMSG(ex.getMessage(), "ERROR");
            }
            catch(IOException ex) {
                dialogMSG("Invalid file", "ERROR");
            }
            catch (NumberFormatException ex) {
                dialogMSG("Invalid register", "ERROR");
                ex.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        LfsrFrame app = new LfsrFrame();
        app.setVisible(true);
    }
}
