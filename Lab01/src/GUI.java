

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JDesktopPane;
import java.awt.List;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import java.awt.Window.Type;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JPasswordField passwordField;
	private JTextField txtIp;
	private JTextField txtPort;
	private JButton btnSend;
	private JButton btnRecv;
	private JLabel lblProgress;
	private JProgressBar progressBar;

	private DefaultMutableTreeNode node = null;

	public File dirC, dirS;

	private long remoteSize;
	private long localSize;

	int realPort = 0;

	JTree treeS = null;
	JTree treeC = null;

	Socket cSocket = null;

	Vector bufDir = new Vector();
	Vector bufFile = new Vector();

	DataOutputStream dout = null;
	DataInputStream din = null;
	ObjectInputStream oin = null;
	FileInputStream fin = null;
	FileOutputStream fout = null;

	String cNodeSelected = null;
	String sNodeSelected = null;

	boolean connected = false;

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	public GUI() {
		dirC = new File("/Users/arthursdays/");
		dirS = null;
		setTitle("WFTP Personal Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 490);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(354, 16, 63, 16);
		contentPane.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(421, 11, 172, 26);
		passwordField.setText("1124");
		contentPane.add(passwordField);

		JLabel lblStatus = new JLabel("Not Connected...");
		lblStatus.setBounds(23, 433, 318, 16);
		contentPane.add(lblStatus);

		progressBar = new JProgressBar();
		progressBar.setBounds(558, 433, 199, 20);
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		progressBar.setVisible(false);
		progressBar.setIndeterminate(true);
		contentPane.add(progressBar);

		JLabel lblLocalFileSize = new JLabel("Local File Size:");
		lblLocalFileSize.setBounds(39, 399, 101, 16);
		contentPane.add(lblLocalFileSize);

		JLabel lblServerFileSize = new JLabel("Server File Size: ");
		lblServerFileSize.setBounds(507, 399, 103, 16);
		contentPane.add(lblServerFileSize);

		JLabel lblcFileSize = new JLabel("Not Selected");
		lblcFileSize.setBounds(136, 399, 146, 16);
		contentPane.add(lblcFileSize);

		JLabel lblsFileSize = new JLabel("Not Selected");
		lblsFileSize.setBounds(609, 399, 148, 16);
		contentPane.add(lblsFileSize);

		lblProgress = new JLabel("0%");
		lblProgress.setBounds(763, 433, 38, 16);
		lblProgress.setVisible(false);
		contentPane.add(lblProgress);

		JLabel lblTaskStat = new JLabel("Task Status:");
		lblTaskStat.setBounds(473, 433, 83, 16);
		lblTaskStat.setVisible(false);
		contentPane.add(lblTaskStat);

		JButton btnRefresh = new JButton("Refresh");
		if (!connected)
			btnRefresh.setEnabled(false);
		btnRefresh.setBounds(354, 210, 117, 29);
		contentPane.add(btnRefresh);
		btnRefresh.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				try {
					updateTrees();
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		JButton btnConnect = new JButton("Connect!");
		JButton btnDisconnect = new JButton("Disconnect!");
		if (!connected)
			btnDisconnect.setEnabled(false);
		btnDisconnect.setBounds(711, 11, 103, 29);
		contentPane.add(btnDisconnect);
		btnDisconnect.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				try {
					cSocket.close();
					connected = false;
					btnConnect.setEnabled(true);
					btnDisconnect.setEnabled(false);
					btnRecv.setEnabled(false);
					btnSend.setEnabled(false);
					btnRefresh.setEnabled(false);
					lblStatus.setText("Not Connected...");
					lblsFileSize.setText("Not Selected");
					sNodeSelected = "";
					sayGoodBye();
					updateTreeS(null);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btnConnect.setBounds(605, 11, 103, 29);
		contentPane.add(btnConnect);
		btnConnect.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				try {
					System.out.println("Password is " + new String(passwordField.getPassword()));

					connected = connect(txtIp.getText(), Integer.parseInt(txtPort.getText()),
							new String(passwordField.getPassword()));
					if (connected) {
						btnConnect.setEnabled(false);
						btnDisconnect.setEnabled(true);
						btnRecv.setEnabled(true);
						btnSend.setEnabled(true);
						btnRefresh.setEnabled(true);
						lblStatus.setText("Connected to server: " + txtIp.getText());
					} else
						lblStatus.setText("Wrong Server, Port or Password!");
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

		JLabel lblYourComputer = new JLabel("Your Computer");
		lblYourComputer.setBounds(39, 58, 117, 16);
		contentPane.add(lblYourComputer);

		JLabel lblServer = new JLabel("Server");
		lblServer.setBounds(507, 58, 61, 16);
		contentPane.add(lblServer);

		JLabel lblIpAddress = new JLabel("Server Address:");
		lblIpAddress.setBounds(43, 16, 97, 16);
		contentPane.add(lblIpAddress);

		txtIp = new JTextField();
		txtIp.setText("127.0.0.1");
		txtIp.setBounds(143, 11, 139, 26);
		contentPane.add(txtIp);
		txtIp.setColumns(10);

		JLabel label = new JLabel(":");
		label.setBounds(286, 16, 4, 16);
		contentPane.add(label);

		txtPort = new JTextField();
		txtPort.setText("7777");
		txtPort.setBounds(294, 11, 47, 26);
		contentPane.add(txtPort);
		txtPort.setColumns(10);

		treeC = new JTree(addNodes(null, dirC, 0));
		treeC.addTreeWillExpandListener(new TreeWillExpandListener() {
			public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				System.out.println("Tree node will expand: " + node);
				System.out.println("The node path to be added in is " + ((String) node.toString()));
				if (bufDir.contains(node)) {
					File tmpFile = new File(node.toString());
					// String tmpList[] = tmpFile.list();
					// for(int i = 0; i < tmpList.length; i++)
					addNodes(node, new File(node.toString()), 0);

					bufDir.remove(node);
				}
			}

			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
				// TODO Auto-generated method stub

			};

		});

		treeC.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				// addNodes(node, new File((String) node.toString()), 0);
				System.out.println("You selected " + node.toString() + " in your PC.");
				cNodeSelected = node.toString();
				localSize = (new File(cNodeSelected).length());
				lblcFileSize.setText(String.format("%.2fMB", ((double) localSize) / 1024.0 / 1024.0));
			}
		});

		treeS = new JTree(node);
		treeS.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				// addNodes(node, new File((String)((DefaultMutableTreeNode)
				// e.getPath().getLastPathComponent()).getUserObject() +
				// File.pathSeparator), 0);
				System.out.println("You selected " + node + " in server.");
				sNodeSelected = node.toString();
				// lblsFileSize.setText("");
				try {
					remoteSize = getSize(sNodeSelected);
					lblsFileSize.setText(String.format("%.2fMB", (double) (remoteSize) / 1024.0 / 1024.0));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		btnSend = new JButton(">>>");
		if (!connected)
			btnSend.setEnabled(false);
		btnSend.setBounds(354, 160, 117, 29);
		contentPane.add(btnSend);
		btnSend.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (!(cNodeSelected.equals("") || sNodeSelected.equals("")))
					try {
						upload(cNodeSelected, sNodeSelected);
						updateTrees();
					} catch (IOException | ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btnRecv = new JButton("<<<");
		if (!connected)
			btnRecv.setEnabled(false);
		btnRecv.setBounds(354, 260, 117, 29);
		contentPane.add(btnRecv);
		btnRecv.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (!(cNodeSelected.equals("") || sNodeSelected.equals("")))
					try {
						download(sNodeSelected, cNodeSelected);
						updateTrees();
					} catch (IOException | ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		JScrollPane scrollPaneC = new JScrollPane();
		scrollPaneC.setViewportView(treeC);
		scrollPaneC.setBounds(39, 86, 286, 312);
		contentPane.add(scrollPaneC);

		JScrollPane scrollPaneS = new JScrollPane();
		scrollPaneS.setViewportView(treeS);
		scrollPaneS.setBounds(507, 86, 286, 312);
		contentPane.add(scrollPaneS);

	}

	protected DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir, int cnt) {

		String curPath = dir.getPath();
		DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);

		if (curTop != null)
			curTop.add(curDir);

		Vector buf = new Vector();
		String tmp[] = dir.list();
		if (tmp == null)
			return null;
		for (int i = 0; i < tmp.length; i++)
			buf.addElement(tmp[i]);
		Collections.sort(buf, String.CASE_INSENSITIVE_ORDER);

		File f;
		Vector files = new Vector();
		// make paths for dirs + files
		for (int i = 0; i < buf.size(); i++) {
			String thisObj = (String) buf.elementAt(i);
			String newPath;
			if (curPath.equals("."))
				newPath = thisObj;
			else
				newPath = curPath + File.separator + thisObj;
			if (thisObj.charAt(0) != '.') {
				// System.out.println(newPath+"\n"+thisObj);
				f = new File(newPath);
				if (f.isDirectory() && cnt == 4) {
					if (!bufDir.contains(curDir))
						bufDir.add(curDir);
					// bufFile.add(f);
				} else if (f.isDirectory() && cnt < 4)
					curDir.add(addNodes(curDir, f, cnt + 1));
				// else if(f.isDirectory() && cnt == 3)
				// curDir.add(addNodes(curDir, f, 4));
				else if (!f.isDirectory())
					files.addElement(newPath);
			}
		}
		// files only
		for (int i = 0; i < files.size(); i++)
			curDir.add(new DefaultMutableTreeNode(files.elementAt(i)));
		return curDir;
	}

	private boolean connect(String ip, int port, String password) {
		// Socket tmpcSocket = null;

		try {
			cSocket = new Socket(InetAddress.getByName(ip), port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("cSocket Created!");

		// din = new DataInputStream(tmpcSocket.getInputStream());
		// realPort = din.readInt();
		// cSocket = new Socket(InetAddress.getByName(ip), realPort);

		try {
			din = new DataInputStream(cSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("Two Input Streams Created!");
		try {
			dout = new DataOutputStream(cSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("All Streams Created!");

		try {
			dout.writeInt(password.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("Length Sent!");
		byte b[] = password.getBytes();
		try {
			dout.write(b);
			dout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		try {
			getNode();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void getNode() throws IOException, ClassNotFoundException {
		oin = new ObjectInputStream(din);
		System.out.println("Oin Created!");
		node = (DefaultMutableTreeNode) (oin.readObject());
		System.out.println("Object Received!");
		updateTreeS(node);
	}

	private void updateTreeS(DefaultMutableTreeNode node) {
		DefaultTreeModel model = (DefaultTreeModel) treeS.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) node;
		model.setRoot(root);

		model.reload();
		treeS.setModel(model);
		System.out.println("TreeS refeshed!");

	}

	private void updateTreeC(DefaultMutableTreeNode node) {
		DefaultTreeModel model = (DefaultTreeModel) treeC.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) node;
		model.setRoot(root);

		model.reload();
		treeC.setModel(model);
		System.out.println("TreeC refeshed!");

	}

	private void updateTrees() throws IOException, ClassNotFoundException {
		String cmd = "sendnode";
		int size = cmd.length();
		byte[] b = new byte[size];
		b = cmd.getBytes();
		dout.writeInt(size);
		dout.flush();
		dout.write(b);
		dout.flush();

		updateTreeS((DefaultMutableTreeNode) (oin.readObject()));
		updateTreeC(addNodes(null, dirC, 0));
	}

	private long getSize(String path) throws IOException {
		String cmd = "getsize";
		int size = cmd.length();
		byte[] b = new byte[size];
		b = cmd.getBytes();
		dout.writeInt(size);
		dout.flush();
		dout.write(b);
		dout.flush();

		byte[] pth = new byte[path.length()];
		pth = path.getBytes();
		dout.writeInt(path.length());
		dout.flush();
		dout.write(pth);
		dout.flush();
		long length = din.readLong();
		return length;
	}

	private void download(String src, String dst) throws IOException {
		String cmd = "download";
		int size = cmd.length();
		byte[] b = new byte[size];
		b = cmd.getBytes();
		dout.writeInt(size);
		dout.flush();
		dout.write(b);
		dout.flush();

		// send remote file dir to see if it is directory
		String path = src;
		size = src.length();
		b = new byte[size];
		b = path.getBytes();
		dout.writeInt(size);
		dout.flush();
		dout.write(b);
		dout.flush();
		boolean SrcIsDirectory;
		SrcIsDirectory = din.readBoolean();

		// see if local file is directory
		// if local file is directory, create new file inside it
		// otherwise, create new file under same directory
		String localPath;
		File tmpFile = new File(dst);
		if (tmpFile.isDirectory())
			localPath = dst;
		else
			localPath = tmpFile.getParent();
		// prepare local file name
		String tmp[] = src.split(File.separator);
		String name = tmp[tmp.length - 1];
		System.out.println("Local path name will be: " + src);
		File f = new File(localPath + File.separator + name);
		//byte[] buffer = new byte[2048];
		//int n;

		// if remote is directory, create directory with same name
		// otherwise, write file out
		if (SrcIsDirectory) {
			System.out.println("+++" + f.getAbsolutePath());
			f.mkdir();
			while (din.readInt() == 1) {
				size = din.readInt();
				b = new byte[size];
				din.read(b);
				String localFile = new String(b);
				String[] localFileName = localFile.split(File.separator);
				name = localFileName[localFileName.length - 1];
				System.out.println(src);
				download(f.getAbsolutePath() + File.separator + name);
			}
		} else {
			FileOutputStream fout = new FileOutputStream(f.getAbsolutePath());
			System.out.println("Ready to created local file: " + f.getAbsolutePath());
			dout.writeInt(1);
			dout.flush();
			long fileSize = din.readLong();
			System.out.println("Ready to Receive!");
			long sizePtr = 0;
			int tmpSize = 0;
			double progress = 100 * ((double) sizePtr / (double) fileSize);
			while (sizePtr < fileSize) {
				byte[] buffer = new byte[2048];
				tmpSize = din.read(buffer);
				fout.write(buffer, 0, tmpSize);
				fout.flush();
				sizePtr += tmpSize;
				System.out.println(100 * ((double) sizePtr / (double) fileSize));

//				if (100*(double) sizePtr / (double) fileSize - progress > 1) {
//					updateProgress();
//					progress = 100 * ((double) sizePtr / (double) fileSize);
//					System.out.println("Progress Updated!");
//				} 
//				if ((double) sizePtr / (double) fileSize >= 1){
//					SwingUtilities.invokeLater(new Runnable() {
//						public void run() {
//							lblProgress.setText("0%");
//							progressBar.setValue(0);
//							progressBar.repaint();
//							lblProgress.repaint();
//						}
//					});
//				}
			}
			System.out.println("Finished!");
			fout.close();
			return;
		}
	}

	private void download(String path) throws IOException {
		System.out.println("Download path: " + path);
		boolean SrcIsDirectory;
		SrcIsDirectory = din.readBoolean();
		File f = new File(path);

		if (SrcIsDirectory) {
			f.mkdir();
			while (din.readInt() == 1) {
				int size = din.readInt();
				byte[] b = new byte[size];
				din.read(b);
				String localFile = new String(b);
				String[] localFileName = localFile.split(File.separator);
				String name = localFileName[localFileName.length - 1];
				System.out.println("Received new directory: " + f.getAbsolutePath() + File.separator + name);
				download(f.getAbsolutePath() + File.separator + name);
			}
		} else {
			FileOutputStream fout = new FileOutputStream(f.getAbsolutePath());
			System.out.println("Ready to created local file: " + f.getAbsolutePath());
			dout.writeInt(1);
			dout.flush();
			long fileSize = din.readLong();
			System.out.println("Ready to Receive!");
			long sizePtr = 0;
			int tmpSize = 0;
			double progress = 100 * ((double) sizePtr / (double) fileSize);
			while (sizePtr < fileSize) {
				byte[] buffer = new byte[2048];
				tmpSize = din.read(buffer);
				fout.write(buffer, 0, tmpSize);
				fout.flush();
				sizePtr += tmpSize;
				System.out.println(100 * ((double) sizePtr / (double) fileSize));
				
//				if (100*(double) sizePtr / (double) fileSize - progress > 1) {
//					updateProgress();
//					progress = 100 * ((double) sizePtr / (double) fileSize);
//					System.out.println("Progress Updated!");
//				} 
//				if ((double) sizePtr / (double) fileSize >= 1){
//					SwingUtilities.invokeLater(new Runnable() {
//						public void run() {
//							lblProgress.setText("0%");
//							progressBar.setValue(0);
//							progressBar.repaint();
//							lblProgress.repaint();
//						}
//					});
//				}
			}
			System.out.println("Finished!");
			fout.close();
			return;
		}
	}

	private void upload(String src, String dst) throws IOException {
		String cmd = "upload";
		int size = cmd.length();
		byte[] b = new byte[size];
		b = cmd.getBytes();
		dout.writeInt(size);
		dout.flush();
		dout.write(b);
		dout.flush();

		// send remote file dir
		String path = dst;
		size = dst.length();
		b = new byte[size];
		b = path.getBytes();
		dout.writeInt(size);
		dout.flush();
		dout.write(b);
		dout.flush();
		File f = new File(src);
		System.out.println("File to be uploaded is " + src);
		// send if local is directory
		boolean localIsDirectory = f.isDirectory();
		dout.writeBoolean(localIsDirectory);
		dout.flush();
		dout.writeInt(src.length());
		dout.flush();
		b = new byte[src.length()];
		b = src.getBytes();
		dout.write(b);
		dout.flush();

		// see if local file is directory
		// if local file is directory, create new file inside it
		// otherwise, create new file under same directory
		if (localIsDirectory) {
			String[] strArr = f.list();
			for (int i = 0; i < strArr.length; i++)
				if (strArr[i].charAt(0) != '.') {
					System.out.println("Found new: " + src + File.separator + strArr[i]);
					upload(src + File.separator + strArr[i]);
				}
			dout.writeInt(0);
			dout.flush();
		} else {
			fin = new FileInputStream(f.getAbsolutePath());
			byte[] buffer = new byte[2048];
			int n;
			//long sizePtr = 0;
			din.readInt();
			dout.writeLong(f.length());
			dout.flush();
			long fileSize = 0;
			int tmpSize;
			double progress = 100 * ((double) fileSize / (double) f.length());
			while (fileSize < f.length()){
				tmpSize = fin.read(buffer, 0, 2048);
				dout.write(buffer);
				dout.flush();
				fileSize += tmpSize;
				System.out.println(100 * ((double) fileSize / (double) f.length()) + "\t" + tmpSize);
//				
//				if (100*(double) sizePtr / (double) f.length() - progress > 1) {
//					updateProgress();
//					progress = 100 * ((double) sizePtr / (double) f.length());
//					System.out.println("Progress Updated!");
//				} 
//				if ((double) sizePtr / (double) f.length() >= 1){
//					SwingUtilities.invokeLater(new Runnable() {
//						public void run() {
//							lblProgress.setText("0%");
//							progressBar.setValue(0);
//							progressBar.repaint();
//							lblProgress.repaint();
//						}
//					});
//				}
			}
			System.out.println("Finished!");
		}

		// if remote is directory, create directory with same name
		// otherwise, write file out

	}

	private void upload(String path) throws IOException {
		dout.writeInt(1);
		dout.flush();
		System.out.println("1 Written!");

		int length = path.length();
		byte[] b = new byte[length];
		b = path.getBytes();

		dout.writeInt(length);
		dout.write(b);
		File f = new File(path);
		System.out.println("Path Written!");
		boolean localIsDirectory = f.isDirectory();
		dout.writeBoolean(localIsDirectory);
		System.out.println("Boolean Written!");
		if (localIsDirectory) {
			String[] strArr = f.list();
			for (int i = 0; i < strArr.length; i++)
				if (strArr[i].charAt(0) != '.') {
					upload(path + File.separator + strArr[i]);
				}
			dout.writeInt(0);
			dout.flush();
		} else {
			System.out.println("Will upload file: " + path);
			fin = new FileInputStream(path);
			byte[] buffer = new byte[2048];
			int n;
			long sizePtr = 0;
			din.readInt();
			dout.writeLong(f.length());
			dout.flush();
			System.out.println("Size written!");
			long fileSize = 0;
			int tmpSize;
			double progress = 100 * ((double) fileSize / (double) f.length());
			while (fileSize < f.length()){
				tmpSize = fin.read(buffer, 0, 2048);
				dout.write(buffer);
				dout.flush();
				fileSize += tmpSize;
				System.out.println(100 * ((double) fileSize / (double) f.length()) + "\t" + tmpSize);
//				
//				if(100*(double) sizePtr / (double) f.length() - progress > 1) {
//					updateProgress();
//					progress = 100 * ((double) sizePtr / (double) f.length());
//					System.out.println("Progress Updated!");
//				} 
//				if ((double) sizePtr / (double) f.length() >= 1){
//					SwingUtilities.invokeLater(new Runnable() {
//						public void run() {
//							lblProgress.setText("0%");
//							progressBar.setValue(0);
//							progressBar.repaint();
//							lblProgress.repaint();
//						}
//					});
//				}
			}
			System.out.println("Finished!");
		}
	}

	private void updateProgress(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressBar.setValue(progressBar.getValue() + 1);
				lblProgress.setText(String.format("%d%%", progressBar.getValue() + 1));
				progressBar.repaint();
				lblProgress.repaint();
				}
		});
	}
	
	private void sayGoodBye() throws IOException {
		String cmd = "BYE BYE";
		dout.writeInt(cmd.length());
		dout.flush();
		dout.write(cmd.getBytes());
		dout.flush();
	}
}
