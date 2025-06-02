import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class ImagePanel extends JPanel {
    private Image backgroundImage;

    public ImagePanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(ClassLoader.getSystemResource(imagePath)).getImage();
        } catch (Exception e) {
            System.out.println("Could not load bg image: " + imagePath);
            e.printStackTrace();
        }
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

public class Server implements ActionListener {
    JTextField text1;
    JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;
    static boolean isConnected = false;

    Server() {
        f.setLayout(null);

        //header
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(21, 62, 89));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        //back
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/back.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        //profile_img
        ImageIcon pp1 = new ImageIcon(ClassLoader.getSystemResource("icons/profile1.png"));
        Image pp2 = pp1.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon pp3 = new ImageIcon(pp2);
        JLabel profile1 = new JLabel(pp3);
        profile1.setBounds(40, 10, 50, 50);
        p1.add(profile1);

        //video_icon
        ImageIcon v1 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image v2 = v1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon v3 = new ImageIcon(v2);
        JLabel video = new JLabel(v3);
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

        //phone_icon
        ImageIcon ph1 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image ph2 = ph1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon ph3 = new ImageIcon(ph2);
        JLabel phone = new JLabel(ph3);
        phone.setBounds(350, 20, 30, 30);
        p1.add(phone);

        //profile_name and status
        JLabel name = new JLabel("Anna");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.white);
        name.setFont(new Font("SANS_SERIF", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel("Active Now");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.white);
        status.setFont(new Font("SANS_SERIF", Font.BOLD, 14));
        p1.add(status);

        //chat_area
        a1 = new ImagePanel("icons/image1.png");
        a1.setBounds(5, 75, 440, 570);
        a1.setLayout(new BorderLayout());
        vertical.setOpaque(false);
        a1.add(vertical, BorderLayout.PAGE_START);
        f.add(a1);

        //text_input_field
        text1 = new JTextField();
        text1.setBounds(5, 655, 310, 40);
        f.add(text1);

        //send
        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(21, 62, 89));
        send.setForeground(Color.white);
        send.setFont(new Font("SANS_SERIF", Font.PLAIN, 16));
        send.addActionListener(this);
        f.add(send);

        //more_icon
        ImageIcon m1 = new ImageIcon(ClassLoader.getSystemResource("icons/more.png"));
        Image m2 = m1.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        ImageIcon m3 = new ImageIcon(m2);
        JLabel more = new JLabel(m3);
        more.setBounds(400, 20, 30, 30);
        p1.add(more);

        f.setSize(450, 700);
        f.setLocation(200, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.white);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String out = text1.getText();
            if (out.trim().isEmpty()) return;
        
            JPanel messagePanel = formatSentMessage(out);
            JPanel right = new JPanel(new BorderLayout());
            right.setOpaque(false);
            right.add(messagePanel, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            if (isConnected && dout != null) {
                dout.writeUTF(out);
            } else {
                System.out.println("Not connected to any client yet");
            }
        
            vertical.revalidate();
            vertical.repaint();
        
            text1.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //sent messages
    public static JPanel formatSentMessage(String out) { 
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(6, 44, 69));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);

        JLabel output = new JLabel("<html><p style=\"width: 150px\">"+out+"</p></html>");
        output.setForeground(Color.WHITE);
        output.setFont(new Font("Tahoma", Font.PLAIN,16));
        output.setOpaque(false);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(cal.getTime()));
        time.setForeground(new Color(200, 200, 200));
        time.setFont(time.getFont().deriveFont(10f));
        time.setOpaque(false);

        panel.add(output);
        panel.add(time);
        panel.setBorder(new EmptyBorder(10,10,10,10));

        return panel;
    }

    //received messages
    public static JPanel formatReceivedMessage(String out) { 
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(50, 83, 105));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);

        JLabel output = new JLabel("<html><p style=\"width: 150px\">"+out+"</p></html>");
        output.setForeground(Color.WHITE);
        output.setFont(new Font("Tahoma", Font.PLAIN,16));
        output.setOpaque(false);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(cal.getTime()));
        time.setForeground(new Color(200, 200, 200));
        time.setFont(time.getFont().deriveFont(10f));
        time.setOpaque(false);

        panel.add(output);
        panel.add(time);
        panel.setBorder(new EmptyBorder(10,10,10,10));

        return panel;
    }

    public static void main(String[] args) {
        new Server();

        try {
            ServerSocket skt = new ServerSocket(6001);
            System.out.println("Server started, waiting for client...");
            
            while (true) {
                Socket s = skt.accept();
                System.out.println("Client connected");
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                isConnected = true;

                while (true) {
                    try {
                        String msg = din.readUTF();
                        JPanel panel = formatReceivedMessage(msg);

                        JPanel left = new JPanel(new BorderLayout());
                        left.setOpaque(false);
                        left.add(panel, BorderLayout.LINE_START);
                        vertical.add(left);
                        vertical.add(Box.createVerticalStrut(15));
                        
                        vertical.revalidate();
                        vertical.repaint();
                        f.validate();
                    } catch (Exception e) {
                        System.out.println("Client disconnected");
                        isConnected = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}