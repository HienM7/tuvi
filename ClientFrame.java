package tuvi;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class ClientFrame extends JFrame implements Runnable {

	private JPanel contentPane;
	private JTextField ngaySinh;
	private JTextField thangSinh;
	private JTextField namSinh;
	private Socket client_socket;
	private String result = "";
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JTextField ip;
	private JLabel IPlabel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientFrame frame = new ClientFrame("Chương trình xem tử vi");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public boolean check_IP(String n) {
		if (n == null || n.length() > 15 || n.length() < 11) {
            return false;
        } else {
            String strPattern = "[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}+\\.[0-9]{1,3}";

            //String strPattern = "[^0-9]{1,3}+\\.[^0-9]{1,3}+\\.[^0-9]{1,3}+\\.[^0-9]{1,3}";
            //String strPattern = "[^0-9]+\\.[^0-9]+\\.[^0-9]+\\.[^0-9]+\\.[^0-9]";  //co dang 1.1.1.1
            //String strPattern = "[^0-9][^.]";
            Pattern p;
            Matcher m;
            int flag = Pattern.CASE_INSENSITIVE;
            p = Pattern.compile(strPattern, flag);
            m = p.matcher(n);
            return m.find();
        }
		
	}
	
	public boolean check_date(String date) {
		String strPattern = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
		Pattern p;
        Matcher m;
        int flag = Pattern.CASE_INSENSITIVE;
        p = Pattern.compile(strPattern, flag);
        m = p.matcher(date);
        return m.find();
	}
	
	public void GUI(ClientFrame frame) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 607, 663);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(68, 10, 70, 67);
		Image logo = new ImageIcon(this.getClass().getResource("/tuvi_logo.png")).getImage();
		Image logoScale = logo.getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(), Image.SCALE_SMOOTH);
		
		lblNewLabel.setIcon(new ImageIcon(logoScale));
		
		contentPane.add(lblNewLabel);
		
		JLabel logotuvi2 = new JLabel("New label");
		logotuvi2.setBounds(446, 10, 70, 67);
		logotuvi2.setIcon(new ImageIcon(logoScale));
		contentPane.add(logotuvi2);
		
		JLabel lable1 = new JLabel("Ng\u00E0y Sinh:");
		lable1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lable1.setBounds(41, 154, 77, 47);
		contentPane.add(lable1);
		
		ngaySinh = new JTextField();
		ngaySinh.setEnabled(false);
		ngaySinh.setFont(new Font("Tahoma", Font.PLAIN, 14));
		ngaySinh.setBounds(118, 159, 207, 37);
		contentPane.add(ngaySinh);
		ngaySinh.setColumns(10);
		
		JLabel Title = new JLabel("Hệ thống xem tử vi");
		Title.setForeground(Color.BLACK);
		Title.setBackground(Color.WHITE);
		Title.setHorizontalAlignment(SwingConstants.CENTER);
		Title.setFont(new Font("Tahoma", Font.PLAIN, 30));
		Title.setBounds(0, 4, 593, 73);
		contentPane.add(Title);
		
		thangSinh = new JTextField();
		thangSinh.setEnabled(false);
		thangSinh.setFont(new Font("Tahoma", Font.PLAIN, 14));
		thangSinh.setColumns(10);
		thangSinh.setBounds(118, 214, 207, 37);
		contentPane.add(thangSinh);
		
		JLabel lable2 = new JLabel("Th\u00E1ng Sinh");
		lable2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lable2.setBounds(41, 209, 77, 47);
		contentPane.add(lable2);
		
		namSinh = new JTextField();
		namSinh.setEnabled(false);
		namSinh.setFont(new Font("Tahoma", Font.PLAIN, 14));
		namSinh.setColumns(10);
		namSinh.setBounds(118, 266, 207, 37);
		contentPane.add(namSinh);
		
		JLabel lable3 = new JLabel("N\u0103m Sinh");
		lable3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lable3.setBounds(41, 261, 77, 47);
		contentPane.add(lable3);
		
		JButton thoat = new JButton("Tho\u00E1t");
		thoat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==thoat) {
					try {
						client_socket.close();
						System.exit(0);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			
			}
		});
		thoat.setFont(new Font("Tahoma", Font.PLAIN, 14));
		thoat.setBounds(362, 266, 166, 37);
		contentPane.add(thoat);
		
		JButton reset = new JButton("Reset");
		reset.setEnabled(false);
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == reset) {
					ngaySinh.setText("");
					thangSinh.setText("");
					namSinh.setText("");
				}
			
			}
		});
		reset.setFont(new Font("Tahoma", Font.PLAIN, 14));
		reset.setBounds(362, 214, 166, 37);
		contentPane.add(reset);
		
		JButton btnXem = new JButton("Xem");
		btnXem.setEnabled(false);
		btnXem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==btnXem)
				{
					if(ngaySinh.getText().equals("") || thangSinh.getText().equals("") || namSinh.getText().equals(""))
					{
						JOptionPane.showMessageDialog(null, "Xin hãy nhập đầy đủ ngày tháng năm sinh");
					}
					else
					{
						String date = ngaySinh.getText() + "/" + thangSinh.getText() + "/" + namSinh.getText();
						if (!check_date(date)) {
							JOptionPane.showMessageDialog(null, "Ngày không tồn tại, hoặc định dạng sai vui lòng nhập lại");
							return;
						}
						try {
							DataOutputStream dos = new DataOutputStream(client_socket.getOutputStream());
							dos.writeInt(Integer.parseInt(ngaySinh.getText()));
							dos.writeInt(Integer.parseInt(thangSinh.getText()));
							dos.writeInt(Integer.parseInt(namSinh.getText()));
							dos.flush();
							
						} catch (IOException e1) {
							e1.printStackTrace();
						}
//						ngaySinh.setText("");
//						thangSinh.setText("");
//						namSinh.setText("");
					}
				}
				
			}
		});
		btnXem.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnXem.setBounds(362, 159, 166, 37);
		contentPane.add(btnXem);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportBorder(UIManager.getBorder("ComboBox.border"));
		scrollPane.setToolTipText("");
		scrollPane.setBounds(10, 318, 573, 293);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		textArea.setWrapStyleWord(true);
		textArea.setTabSize(4);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		ip = new JTextField();
		ip.setFont(new Font("Tahoma", Font.PLAIN, 14));
		ip.setColumns(10);
		ip.setBounds(118, 110, 207, 37);
		contentPane.add(ip);
		
		IPlabel = new JLabel("Nhập IP:");
		IPlabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		IPlabel.setBounds(41, 105, 77, 47);
		contentPane.add(IPlabel);
		
		JButton ketnoi = new JButton("Kết nối");
		ketnoi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==ketnoi) {
					if (check_IP(ip.getText())) {
						try
						{
							String ip_address = ip.getText();
							int open_port = 8889;
							client_socket = new Socket(ip_address, open_port);
							
							DataInputStream dis = new DataInputStream(client_socket.getInputStream());
							String response = dis.readUTF();
							
							if (response.equals("Connect success")) {
								JOptionPane.showMessageDialog(null, "Kết nối thành công");
								btnXem.setEnabled(true);
								reset.setEnabled(true);
								ngaySinh.setEnabled(true);
								thangSinh.setEnabled(true);
								namSinh.setEnabled(true);
							}
							
							//Khoi chay luon doc lap
							new Thread(frame).start();

						}catch(Exception e1)
						{
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "Kết nối thất bại");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Kết nối thất bại");
						System.out.println("kết nối thất bại");
					}
					
				}
				
				
			}
		});
		ketnoi.setFont(new Font("Tahoma", Font.PLAIN, 14));
		ketnoi.setBounds(362, 112, 166, 37);
		contentPane.add(ketnoi);
		
	}
	public ClientFrame(String title) {
		super(title);
		setResizable(false);
		GUI(this);
//		new Thread(this).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				DataInputStream dis = new DataInputStream(client_socket.getInputStream());

				String tuoi = dis.readUTF();
				String menh = dis.readUTF();
				String tuongsinh = dis.readUTF();
				String tuongKhac = dis.readUTF();
				String NamHopSo = dis.readUTF();
				String NuHopSo = dis.readUTF();
				String MauBanMenh = dis.readUTF();
				String MauTuongSinh = dis.readUTF();
				String MauKiengKy = dis.readUTF();
				String NamHuongHop = dis.readUTF();
				String NamHuongKhongHop = dis.readUTF();
				String NuHuongHop = dis.readUTF();
				String NuHuongKhongHop = dis.readUTF();
				
				String s = String.format(
						"\n"
						+ "-------------------- Kết Quả Tử Vi--------------------\n"
						+"\n"
						+ "Tuổi của bạn: %s\n"
						+ "Bạn mệnh nào và hợp với mệnh nào?\n"
						+ "- Mệnh: %s\n"
						+ "\t * Tương sinh: %s\n"
						+ "\t * Tương Khắc: %s\n"
						+ "Bạn hợp con số nào?\n"
						+ "\t * Nam hợp các số: %s\n"
						+ "\t * Nữ hợp các số: %s\n"
						+ "Bạn hợp màu gì?\n"
						+ "- Màu sắc hợp:\n"
						+ "\t * Màu bản mệnh: %s\n"
						+ "\t * Màu tương sinh: %s\n"
						+ "- Màu kiêng kỵ:\n"
						+ "\t * %s\n "
						+ "Bạn hợp hướng nào?\n"
						+"- Nam mạng: \n"
						+ "\t * Hướng hợp: %s\n"
						+ "\t * Hướng không hợp: %s\n"
						+ "- Nữ mạng: \n"
						+ "\t * Hướng hợp: %s\n"
						+ "\t * Hướng không hợp: %s\n", tuoi, menh, tuongsinh, tuongKhac, NamHopSo, NuHopSo, MauBanMenh, MauKiengKy, MauTuongSinh, NamHuongHop, NamHuongKhongHop, NuHuongHop, NuHuongKhongHop  );
				result+= s;
				textArea.setText(s);
				
			}catch(EOFException e1) {
				
			}catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
