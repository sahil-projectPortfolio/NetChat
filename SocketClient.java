import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressWarnings("serial")
public class SocketClient extends JFrame implements ActionListener, Runnable {
    JTextArea textArea = new JTextArea();
    JScrollPane jp = new JScrollPane(textArea);
    JTextField input_Text = new JTextField();
    JTextField tBox = new JTextField();
    JMenuBar menuBar = new JMenuBar();

    Socket sk;
    BufferedReader br;
    PrintWriter pw;

    public SocketClient() {
        super("NetChat");
        setFont(new Font("Heveltica", Font.PLAIN, 16));
        setForeground(new Color(204, 255, 204));     // 0, 0, 51
        setBackground(new Color(204, 255, 204));
        textArea.setToolTipText("Chat History");
        textArea.setForeground(new Color(102, 51, 0));;
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.BOLD, 14));

        textArea.setBackground(new Color(204, 255, 204));

/*
         JMenu helpMenu = new JMenu("Help");
        JMenuItem update = new JMenuItem("Update Information");
        JMenuItem connect_List = new JMenuItem("Visitor List");

        helpMenu.add(update);
        helpMenu.add(connect_List);

        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
*/
        
        // getContentPane().add(jp, "Center");
        tBox.setText("WELCOME TO NETCHAT");
        tBox.setForeground(new Color(255, 255, 255));
        tBox.setFont(new Font("Heveltica", Font.BOLD, 14));
        tBox.setBackground(new Color(0, 0, 0));
        tBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setSize(325, 411);
        getContentPane().add(tBox, "North");
        setVisible(true);

        getContentPane().add(jp, "Center");
        input_Text.setText("Enter your Message:");
        input_Text.setToolTipText("Enter your Message");
        input_Text.setForeground(new Color(0, 0, 0));
        input_Text.setFont(new Font("Tahoma", Font.BOLD, 15));
        input_Text.setBackground(new Color(230, 230, 250));
        
        getContentPane().add(input_Text, "South");
        setSize(325, 411);
        setVisible(true);

        input_Text.requestFocus(); //Place cursor at run time, work after screen is shown

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        input_Text.addActionListener(this); //Event registration
    }

    public void serverConnection() {
        try {
            String IP = JOptionPane.showInputDialog(this, "Please enter a server IP.", JOptionPane.INFORMATION_MESSAGE);
            sk = new Socket(IP, 1234);

            String name = JOptionPane.showInputDialog(this, "Please enter a Username", JOptionPane.INFORMATION_MESSAGE);
/*            while (name.length() > 7) {
                name = JOptionPane.showInputDialog(this, "Please enter a nickname.(7 characters or less)", JOptionPane.INFORMATION_MESSAGE);
            }
*/
            //read
            br = new BufferedReader(new InputStreamReader(sk.getInputStream()));

            //writing
            pw = new PrintWriter(sk.getOutputStream(), true);
            pw.println(name); // Send to server side

            new Thread(this).start();

        } catch (Exception e) {
            System.out.println(e + " Socket Connection error");
        }
    }

    public static void main(String[] args) {
        new SocketClient().serverConnection(); //Method call at the same time object creation
    }

    @Override
    public void run() {
        String data = null;
        try {
            while ((data = br.readLine()) != null) {
                textArea.append(data + "\n"); //textArea Decrease the position of the box's scroll bar by the length of the text entered
                textArea.setCaretPosition(textArea.getText().length());
            }
        } catch (Exception e) {
            System.out.println(e + "--> Client run fail");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String data = input_Text.getText();
        pw.println(data); // Send to server side
        input_Text.setText("");
    }
}