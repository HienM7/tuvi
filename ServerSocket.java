package tuvi;

import java.awt.*;
import java.net.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.sql.*;
import java.sql.Date;

public class ServerSocket extends JFrame 
{
	private final static int open_port = 8889;
	private JLabel label;
	public JTextArea ketqua;
	
	public void GUI()
	{
		setResizable(false);
		setLayout(new FlowLayout());
		label = new JLabel("Server xử lý");
		label.setFont(new Font("Arial",Font.PLAIN, 16));
		label.setHorizontalAlignment(JLabel.LEFT);
		ketqua = new JTextArea(21,58);
		ketqua.setEditable(false);
		
		add(label);
		add(ketqua);
	}
	public ServerSocket(String st)
	{
		super(st);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GUI();
		setSize(620,420);
		setVisible(true);
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		//Khoi tao mot doi tuong (giao dien khung frame) de hien thi lich su cua server
		ServerSocket server = new ServerSocket("Chương trình Server");
		java.net.ServerSocket server_socket = null;
		try {
			//Ket noi den CSDL
			server.ketqua.setText("Kết nối CSDL\n");
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tuvi?characterEncoding=UTF-8","root","helios25121999");
			server.ketqua.append("Kết nối CSDL thành công\n");
			Statement stmt = conn.createStatement();
			//Khoi tao serversocket
			 server_socket = new java.net.ServerSocket(open_port);
			while(true)
			{	
				try {
					//cho va lang nghe su kien ket noi tu socket khac, tra ve socket
					server.ketqua.append("Chờ và lắng nghe sự kiện kết nối\n");
					Socket serversoc = server_socket.accept();
					server.ketqua.append("Đã kết nối với socket có địa chỉ: "+serversoc.getInetAddress()+"\n");
					//xu ly luon doc lap
					DataOutputStream dos = new DataOutputStream(serversoc.getOutputStream());
					dos.writeUTF("Connect success");
					
					xuly xl = new xuly(serversoc, server, stmt);
					xl.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			server_socket.close();
		}
	}
}
class xuly extends Thread{
	Socket soc;
	ServerSocket server;
	Statement stmt;
	//Ham dung cua class xuly
	public xuly(Socket soc, ServerSocket server, Statement stmt)
	{
		this.soc = soc;
		this.server = server;
		this.stmt = stmt;
	}
	
	public String ThienCan(int nam) {
		int socuoi = nam % 10;
		switch (socuoi) {
		case 0: return "Canh";
		case 1: return "Tân";
		case 2: return "Nhâm";
		case 3: return "Quý";
		case 4: return "Giáp";
		case 5: return "Ất";
		case 6: return "Bính";
		case 7: return "Đinh";
		case 8: return "Mậu";
		case 9: return "Kỷ";
		default:
			throw new IllegalArgumentException("Unexpected value: " + socuoi);
		}
	}
	
	public String DiaChi(int nam) {
		int cacsocuoi = nam % 100;
		
		if(nam > 2000) {
			cacsocuoi = 100 + cacsocuoi;
		}
		
		int kiemtra = cacsocuoi % 12;
		
		switch (kiemtra) {
		case 0: return "Tý";
		case 1: return "Sửu";
		case 2: return "Dần";
		case 3: return "Mão";
		case 4: return "Thìn";
		case 5: return "Tỵ";
		case 6: return "Ngọ";
		case 7: return "Mùi";
		case 8: return "Thân";
		case 9: return "Dậu";
		case 10: return "Tất";
		case 11: return "Hợi";
		default:
			throw new IllegalArgumentException("Unexpected value: " + kiemtra);
		}
	}
	
	public int Menh(int nam) {
		int thiencan, diachi;
		
		int socuoi = nam % 10;
		switch (socuoi) {
		case 4:
		case 5:  thiencan = 1; 
			break;
		case 6:
		case 7:  thiencan = 2; 
			break;
		case 8:
		case 9:  thiencan = 3; 
			break;
		case 0:
		case 1:  thiencan = 4; 
			break;
		case 2:
		case 3:  thiencan = 5; 
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + socuoi);
		}
		
		int cacsocuoi = nam % 100;
		if (nam > 2000) {
			cacsocuoi = 100 + cacsocuoi;
		}
		int kiemtra = cacsocuoi % 12;
		switch (kiemtra) {
		case 0:
		case 1:
		case 6:
		case 7:  diachi = 0; 
			break;
		case 2:
		case 3:
		case 8:
		case 9:  diachi = 1; 
			break;
		case 4:
		case 5:
		case 10:
		case 11:  diachi = 2; 
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + kiemtra);
		}
		int menh = thiencan + diachi;
		while(menh > 5) {
			menh -= 5;
		}
		
		return menh;
		
	}
	
	
	public void run() {
		while(true)
		{	
			try {
				DataInputStream dis = new DataInputStream(soc.getInputStream());
				server.ketqua.append("Đã nhận dữ liệu từ client\n");
				int ngaysinh = dis.readInt();
				int thangsinh = dis.readInt();
				int namsinh = dis.readInt();
//				server.ketqua.append(Integer.toString(ngaysinh));
//				server.ketqua.append(Integer.toString(thangsinh));
//				server.ketqua.append(Integer.toString(namsinh));
//				
				String tuoi = ThienCan(namsinh) + " " + DiaChi(namsinh);
				int menh = Menh(namsinh);
//				System.out.println(menh);
//				server.ketqua.append(Integer.toString(menh));
				String sql = "select * from mang where id=" + menh;
				System.out.println(sql);
				
				ResultSet rs = stmt.executeQuery(sql);
				DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
				while(rs.next())
				{
					
					int id = rs.getInt("Id");
					String name = rs.getString("Name");
					String tuongsinh = rs.getString("tuongsinh");					
					String tuongKhac = rs.getString("tuongKhac");
					String NamHopSo = rs.getString("NamHopSo");
					String NuHopSo = rs.getString("NuHopSo");
					String MauBanMenh = rs.getString("MauBanMenh");
					String MauTuongSinh = rs.getString("MauTuongSinh");
					String MauKiengKy = rs.getString("MauKiengKy");
					String NamHuongHop = rs.getString("NamHuongHop");
					String NamHuongKhongHop = rs.getString("NamHuongKhongHop");
					String NuHuongHop = rs.getString("NuHuongHop");
					String NuHuongKhongHop = rs.getString("NuHuongKhongHop");
//					dos.writeInt(STT);
					
//					String s = String.format("%s\n %s\n %s\n %s\n %s\n %s\n %s\n %s\n %s\n %s\n %s\n %s\n %s\n", menh, tuongsinh, tuongKhac, NamHopSo, NuHopSo, MauBanMenh, MauKiengKy, MauTuongSinh, NamHuongHop, NamHuongKhongHop, NuHuongHop, NuHuongKhongHop  );
//					System.out.println(s);
//					server.ketqua.append(s);
					
					dos.writeUTF(tuoi);
					dos.writeUTF(name);
					dos.writeUTF(tuongsinh);
					dos.writeUTF(tuongKhac);
					dos.writeUTF(NamHopSo);
					dos.writeUTF(NuHopSo);
					dos.writeUTF(MauBanMenh);
					dos.writeUTF(MauTuongSinh);
					dos.writeUTF(MauKiengKy);
					dos.writeUTF(NamHuongHop);
					dos.writeUTF(NamHuongKhongHop);
					dos.writeUTF(NuHuongHop);
					dos.writeUTF(NuHuongKhongHop);
					dos.flush();
					server.ketqua.append("Server đã gửi kết quả cho Client\n");
				}
			}catch(Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
