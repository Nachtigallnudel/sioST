import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
//import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
//import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;




public class sioST extends JPanel implements WindowListener, ActionListener{
	
	
		/**
	 *  
	 */
	private static final long serialVersionUID = 1L;
		private static Boolean state_switch=false;
		public String SHome=System.getProperty("user.home");
		private static Image image;
		private static TrayIcon trayIcon;
		private static JFrame frame ;
		
		private static SerialPort serialPort; 
		private static int Port=500;
		static int ZählerPort = 0;
		
		static boolean sioScreen=false;
		static boolean durchlauf=true;
		static boolean isSIOavailable=false;
		static boolean BStateSave=false;
		static boolean BReverseMode=false;
		
		
		private static ImageIcon i_Switch_on ;
		private static ImageIcon i_Switch_off ;
		private static JButton b_switch;
		private static ImageInputStream file;
		private static Image i_offline;
		private static Image i_off;
		private static Image i_on;
		private Image i_frameoff;
		private Image i_frameon;
		private JSlider	JSL_brightPublic;
		private JSlider	JSL_brightPrivacy;
		private JLabel	JLA_brightPublic;
		private JLabel	JLA_brightPrivacy;
		//private Image image;
		
		private int IPrivacyOn=150;
		private int IPublicOn=150;
		private int IPrivacyOff=0;
		private int IPublicOff=0;
		
		private CheckboxMenuItem CheckBoxReverse;
		
		
		
		
		public sioST() {
			super(new BorderLayout());
			
		//Toolkit toolkit = Toolkit.getDefaultToolkit();
		laden_Bilder();
		frame.setIconImage(i_on);
		//
		//Image image;
		image = i_frameoff;
		Image scaledImage = image.getScaledInstance(560, 250, Image.SCALE_DEFAULT); 
		i_Switch_off= new ImageIcon(scaledImage);
		
		
		
		
		Image image1;
		image1 = i_frameon;
		Image scaledImage1 = image1.getScaledInstance(560, 250, Image.SCALE_DEFAULT); 
		i_Switch_on= new ImageIcon(scaledImage1);
		
		
		
		
		JPanel panel = new JPanel();	
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));	
		
		JSL_brightPublic= new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		JSL_brightPublic.setMajorTickSpacing(50);
		JSL_brightPublic.setPaintTicks(true);
		JSL_brightPublic.setPaintLabels(true);
		//JSL_brightPublic.set("start");
		//JSL_brightPublic.addMouseListener(this);
		
		JSL_brightPrivacy= new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		JSL_brightPrivacy.setMajorTickSpacing(50);
		JSL_brightPrivacy.setPaintTicks(true);
		JSL_brightPrivacy.setPaintLabels(true);
		//JSL_brightPrivacy.addMouseListener(this);
		
		JLA_brightPublic = new JLabel("Public Power");
		JLA_brightPrivacy = new JLabel("Privacy Power");
		
		
		b_switch= new JButton();	
		b_switch.setActionCommand("1");
		b_switch.addActionListener((ActionListener) this);
		b_switch.setIcon(i_Switch_on);
		panel.add(b_switch);
		
		panel.add(JLA_brightPrivacy);
		panel.add(JSL_brightPrivacy);
		panel.add(JLA_brightPublic);
		panel.add(JSL_brightPublic);
		
		
		
	// mouse Event Public Slider++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
		JSL_brightPublic.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		    }
		    public void mouseReleased(MouseEvent e) {
	            //System.out.println("Tray Icon - Mouse released!");      
		    	BStateSave=true;
	        	System.out.println("##################### Mouse released  #######################" );
	        	 if(state_switch)
					{
		    		   //IPublicOff=JSL_brightPublic.getValue();	
		    		   IPublicOn=JSL_brightPublic.getValue();
		    		   setLED();
					}
		    	  else
		    	  {
		    		  IPublicOff=JSL_brightPublic.getValue();	
		    		  //IPublicOn=JSL_brightPublic.getValue();
		    		  setLED();
		    		    
		    	  }	        	
	        	
	        }
		  });
		
		// mouse Event Private Slider ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
		JSL_brightPrivacy.addMouseListener(new MouseAdapter() {
				    @Override
				    public void mousePressed(MouseEvent e) {
				    	
				    }
				    public void mouseReleased(MouseEvent e) {
			            //System.out.println("Tray Icon - Mouse released!");      
				    	BStateSave=true;
			        	System.out.println("##################### Mouse released  #######################" );
			        	  if(state_switch)
							{
				    		  //IPrivacyOff=JSL_brightPrivacy.getValue();
				    		  IPrivacyOn=JSL_brightPrivacy.getValue();
				    		  setLED();
							}
				    	  else
				    	  {
				    		  IPrivacyOff=JSL_brightPrivacy.getValue(); 
				    		  //IPrivacyOn=JSL_brightPrivacy.getValue();
				    		  setLED();
				    		  
				    	  }	
			        	
			        }
				  });
		
		
		
		JSL_brightPublic.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		    	  //System.out.println("Set Public Power to =  " + 	JSL_brightPublic.getValue());
		    	  
		    	 
		    	  
		    	  
		    	     	  
		    	  
		    	  
		    	  
		      }
		    });
		JSL_brightPrivacy.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		    	  //System.out.println("Set Privacy Power to =  " + 	JSL_brightPrivacy.getValue());
		    	 
		    	
		    	  
		      }
		    });
		
		
		
		
		
		
		
		
		
		
		add(panel,BorderLayout.CENTER);
		
		b_switch.setEnabled(false);
		
		
		
		//checking for support
	    if(!SystemTray.isSupported()){
	        System.out.println("System tray is not supported !!! ");
	        return ;
	    }
	    //get the systemTray of the system
	    SystemTray systemTray = SystemTray.getSystemTray();
	    
	    
	    
	   
	    
	    //laden_Bilder();
	    image = i_offline;

	    //popupmenu
	    PopupMenu trayPopupMenu = new PopupMenu();
	    CheckBoxReverse = new CheckboxMenuItem("Reverse Mode");

	    //1t menuitem for popupmenu
	    MenuItem action = new MenuItem("Open sioSHIELD");
	    action.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            //JOptionPane.showMessageDialog(null, "Action Clicked");    
	            frame.setVisible(true);
	        }
	    });     
	    trayPopupMenu.add(action);
	    
	    
	        
	    
	    trayPopupMenu.addSeparator();
	 // Add listener to autoSizeCheckBox.
	    CheckBoxReverse.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int autoSizeCheckBoxId = e.getStateChange();
                if (autoSizeCheckBoxId == ItemEvent.SELECTED) {
                	BReverseMode=true;
                	System.out.println("Reverse Mode = true" );
                	schreibenINI();
                } else {
                	BReverseMode=false;
                	System.out.println("Reverse Mode = false" );
                	schreibenINI();
                }
            }
        });
	    trayPopupMenu.add(CheckBoxReverse);
	   
	    
	    

	    //2nd menuitem of popupmenu
	    MenuItem close = new MenuItem("Close");
	    close.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            System.exit(0);             
	        }
	    });
	    trayPopupMenu.add(close);
	    
	    
	    MouseListener mouseListener = new MouseListener() {

	        public void mouseClicked(MouseEvent e) {
	            System.out.println("Tray Icon - Mouse clicked!");  
	            if(BStateSave)
	    		{
	    			schreibenINI();
	    			BStateSave=false;
	    			
	    		}
	            if(state_switch)
				{
					System.out.println("Push Button on" );
					
					state_switch=false;
					setLED();
				}
				else
				{
					System.out.println("Push Button off" );
					state_switch=true;
					setLED();
					
				}
	            
	            
	            
	        }

	        public void mouseEntered(MouseEvent e) {
	            //System.out.println("Tray Icon - Mouse entered!");         
	        	System.out.println("##################### Mouse Entered  #######################" );
	        }

	        public void mouseExited(MouseEvent e) {
	            //System.out.println("Tray Icon - Mouse exited!");       
	        	System.out.println("##################### Mouse Entered  #######################" );
	        }

	        public void mousePressed(MouseEvent e) {
	            //System.out.println("Tray Icon - Mouse pressed!");       
	        	System.out.println("##################### Mouse Entered  #######################" );
	        }

	        public void mouseReleased(MouseEvent e) {
	            //System.out.println("Tray Icon - Mouse released!");      
	        	
	        	System.out.println("##################### Mouse losgelassen  #######################" );
	        }
	    };
	    

	    //setting tray icon
	    trayIcon = new TrayIcon(i_offline, "sioSHIELD", trayPopupMenu);
	    //adjust to default size as per system recommendation 
	    trayIcon.setImageAutoSize(true);
	    trayIcon.addMouseListener(mouseListener);
	    //trayIcon.setImage(image);


	    try{
	        systemTray.add(trayIcon);
	    }catch(AWTException awtException){
	        awtException.printStackTrace();
	    }
	   // System.out.println("end of main");	
	    
	  
		
		
		
		
		
		
		
		
		
		
		Timer timer= new Timer();
		timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
				  openPort();
			  }
		 
		}, 20);
		
		lesenINI();
			
}
		
		
		
		

		public static void  main(String[] args) {
			// TODO Automatisch generierter Methodenstub
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				  @Override
				public void run() {
				    createAndShowGUI();
				  }
				});
			
			

		}
	
	
	private static void createAndShowGUI() {
		// Create and set up the window.
		//laden_Bilder();
		//ImageIcon img = new ImageIcon("images/sioshield/icon-on.png");
		frame = new JFrame("sioSHIELD1");
		frame.setIconImage(i_offline);
		frame.setSize(650, 450);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setLocation(100, 100);
		sioST newContentPane = new sioST();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);
		frame.setResizable(false);

		frame.setVisible(false);
		}
	public static void openPort()
	{

	SerialPort[] ports = SerialPort.getCommPorts();
	System.out.println("Ports :");
	ZählerPort= 0;
	Timer timer = new Timer(); 
	int i=1;
	for(SerialPort port : ports)
	{
		System.out.println(i +  ": " + port.getSystemPortName());
		i++;
	}
	System.out.println("Start Check Port+++++++++++++++" );
	
	for(@SuppressWarnings("unused") SerialPort port : ports)
	{
		//System.out.println(ZählerPort +  ": " + port.getSystemPortName());
		//System.out.println("Ports = " + ports.length);
		serialPort = ports[ZählerPort]; 			
		serialPort.openPort();
		ports[ZählerPort].isOpen();
		//serialPort.
		//System.out.println(ZählerPort+" - Port ist offen = " +ports[ZählerPort].isOpen());
		timer= new Timer();
		timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
		
			if(checkSioptica())
			{
				
				System.out.println("Port opened successfully.");
				
				durchlauf =false;
				Port=ZählerPort;
				isSIOavailable=true;
				serialPort.closePort();
				
			}
		  else {
				System.out.println("Unable to open the port.");
				
				durchlauf =false;
				return;
			}
			serialPort.closePort();
		  
			  }
			}, 2000);
		
		while (durchlauf)
		{
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Automatisch generierter Erfassungsblock
				e.printStackTrace();
			}
		}
		durchlauf=true;
		timer.cancel();
	         
		
	
	//System.out.println("Ports sind offen");
	
		ZählerPort++;
	}
	if(isSIOavailable)
	{
		serialPort = ports[Port]; 			
		serialPort.openPort();
		System.out.println(Port+" aktiver Port ist jetzt = " + serialPort.getSystemPortName());
		//b_switch.setEnabled(true);
		
		
		
		
	}
	}
	private static boolean checkSioptica()
	{
		System.out.println("Check Sioptica Board++++++++++++++++++++++++++++++++++++++++++++ com = "+serialPort.getSystemPortName()); 	
		
		String str ="SetCHECK<";
		byte[] bytes = str.getBytes();
		int i=0;
		//byte[] buffer = str.getBytes();
		serialPort.writeBytes(bytes, bytes.length);
		System.out.println("##################### " + str+ " #######################" );
		//serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 100, 100);
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
		//comPort.openPort();
		String str2="";
		try {
		while (true)
		//while (serialPort.bytesAvailable() == 0)
		   {
		      while (serialPort.bytesAvailable() == 0)
		      {
		    	   //System.out.println("warten = "+ i );
		    	   if(i==20)break;
		    	   i++;
		    	   Thread.sleep(10);
		      }
		    	

		      byte[] readBuffer = new byte[serialPort.bytesAvailable()];
		      serialPort.readBytes(readBuffer, readBuffer.length);
		      //System.out.println("Read " + numRead + " bytes.");
		      str2=new String(readBuffer, "UTF-8");
		      //System.out.println("Read Buffer = " + str2 );
		      //serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 10, 0);
		      //System.out.println("Start Scanner = " );
		      if(str2.equals(""))
		      	{
		    	  sioScreen=false;
		    	  System.out.println("Read Buffer 2= " + str2 );	
		    	  break;
		    	}
		      else
		      {
		    	  @SuppressWarnings("resource")
		    	  
			      Scanner s = new Scanner(str2).useDelimiter("<");
			      System.out.println("Read Buffer 3= " + str2 );	
			      while(s.hasNext()){
			    	  String s1 ="";	          
			          String s2="siOPTICA=true<";
			          s1=s.nextLine();
			          System.out.println("inininininininininin " +s1 +" inininininininininin ");
			          if(s1.equals(s2))
			        	  {
			        	  	System.out.println("" );
			        	  	//System.out.println("Sioptica Screen on Com : " +serialPort.getSystemPortName());
			        	  	image = i_on;
							trayIcon.setImage(image);
			        	  	b_switch.setEnabled(true);
			        	  	sioScreen=true;
			        	  	s.close();
			        	  	break;
			        	  }
			      }	     	      
			      s.close();	      
			      break;  
		      }
		      
		      
		   }
		} catch (Exception e) { 
			e.printStackTrace(); 
			//break;  
		}
		
		if(sioScreen)
		{
			System.out.println("Sioptica Screen = true" );
			return true;
		}
		else
		{
			System.out.println("Sioptica Screen = false" );
			return false;
		}
		
	}
	
	/*
	private static boolean serialIN()
	{
		//byte[] bytes = str.getBytes();
		//System.out.println("Read Serial Port  +++++++++++++++++++++++++++++++++++++  Start");
		int i=0;
		
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
		
		String str2="";
		String s1 ="";
		try {
		while (true)
		//while (serialPort.bytesAvailable() == 0)
		   {
		      while (serialPort.bytesAvailable() == 0)
		      {
		    	   //System.out.println("warten = "+ i );
		    	   if(i==20)break;
		    	   i++;
		    	   Thread.sleep(10);
		      }
		    	

		      byte[] readBuffer = new byte[serialPort.bytesAvailable()];
		      serialPort.readBytes(readBuffer, readBuffer.length);
		      //System.out.println("Read " + numRead + " bytes.");
		      str2=new String(readBuffer, "UTF-8");		      
		      if(str2.equals(""))
		      	{
		    	  sioScreen=false;
		    	  //System.out.println("Read Buffer 2= " + str2 );	
		    	  break;
		    	}
		      else
		      {
		    	  @SuppressWarnings("resource")
		    	  
			      Scanner s = new Scanner(str2).useDelimiter("<");
			      //System.out.println("Read Buffer 3= " + str2 );	
			      while(s.hasNext()){
			    	 // String s1 ="";	          
			          //String s2="siOPTICA=true<";
			          s1=s.nextLine();
			          //System.out.println("inininininininininin " +s1 +" inininininininininin ");			          
			      }	     	      
			      s.close();	      
			      break;  
		      }
		      
		      
		   }
		} catch (Exception e) { 
			e.printStackTrace(); 
			//break;  
		}
		System.out.println("inininininininininin " +s1 +" inininininininininin ");	
		
		return true;
	}
	
	*/


	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Automatisch generierter Methodenstub
		
	}


	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Automatisch generierter Methodenstub
		System.out.println("Window closing ");
		frame.setVisible(false);
		
		
	}


	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Automatisch generierter Methodenstub
		System.out.println("Window deactivated ");	
	}


	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Automatisch generierter Methodenstub
		
	}


	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Automatisch generierter Methodenstub
		
	}


	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Automatisch generierter Methodenstub
		
	}


	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Automatisch generierter Methodenstub
		System.out.println("Window closed");	
		
	}





	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Automatisch generierter Methodenstub
		
		if(BStateSave)
		{
			schreibenINI();
			BStateSave=false;
			
		}
		
		if (e.getSource() == b_switch) {
			//System.out.println("Push Button" );
			 if(state_switch)
				{
					System.out.println("Push Button on" );
					state_switch=false;		
					setLED();	
							
					
				}
				else
				{
					System.out.println("Push Button off" );					
					state_switch=true;
					setLED();
					
					
					
				}
		}
		
		
	}
	private  void laden_Bilder()
	{
		try {
			file = ImageIO.createImageInputStream(this.getClass().getResourceAsStream("img/sioshield/off.png"));
			i_frameoff = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
		try {
			file = ImageIO.createImageInputStream(this.getClass().getResourceAsStream("img/sioshield/on.png"));
			i_frameon = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
		
		try {
			file = ImageIO.createImageInputStream(this.getClass().getResourceAsStream("img/sioshield/icon-on.png"));
			i_on = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
 //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
		
		try {
			file = ImageIO.createImageInputStream(this.getClass().getResourceAsStream("img/sioshield/icon-off.png"));
			i_off = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
 //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
		
		try {
			file = ImageIO.createImageInputStream(this.getClass().getResourceAsStream("img/sioshield/icon-offline.png"));
			i_offline = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
		
		
	}
	
	private  void SetWerte()
	{
		if(state_switch)
		{
			JSL_brightPrivacy.setValue(IPrivacyOn);
			JSL_brightPublic.setValue(IPublicOn);
		}
		else
		{
			
			JSL_brightPrivacy.setValue(IPrivacyOff);
			JSL_brightPublic.setValue(IPublicOff);
			
		}
		
		System.out.println("IPrivacyOff =" + IPrivacyOff + "+++" +"IPublicOff =" + IPublicOff);
		System.out.println("IPrivacyOn =" + IPrivacyOn + "+++" +"IPublicOn =" + IPublicOn);
		
		
	}
	
	public void setLED()
	{
		System.out.println("##################### Set LED #######################" );
		if(state_switch)
		{
			SetWerte();
			System.out.println("LED off");
			String str;
			if(BReverseMode)
			{
				str ="SetLEDprivacy=" + (255-IPrivacyOn) +"<";
			}
			else
			{
				str ="SetLEDprivacy=" + IPrivacyOn +"<";
			}
			
			
			byte[] bytes = str.getBytes();
			serialPort.writeBytes(bytes, bytes.length);
			System.out.println("##################### " + str+ " #######################" );
			String str2;
			if(BReverseMode)
			{
				str2 ="SetLEDpublic=" + (255-IPublicOn) +"<";
			}
			else
			{
				str2 ="SetLEDpublic=" + IPublicOn +"<";
			}
			
			
			byte[] bytes2 = str2.getBytes();
			serialPort.writeBytes(bytes2, bytes2.length);
			System.out.println("##################### " + str2+ " #######################" );
			//serialIN();
			image = i_off;
			trayIcon.setImage(image);
			b_switch.setIcon(i_Switch_off);
			
			
			
			
		}
		else
		{
			
			
			
			SetWerte();
			System.out.println("LED on");
			String str;
			if(BReverseMode)
			{
				str ="SetLEDprivacy=" + (255-IPrivacyOff) +"<";
			}
			else
			{
				str ="SetLEDprivacy=" + IPrivacyOff +"<";
			}
			
			byte[] bytes = str.getBytes();
			serialPort.writeBytes(bytes, bytes.length);
			System.out.println("##################### " + str+ " #######################" );
			String str2;
			if(BReverseMode)
			{
				str2 ="SetLEDpublic=" + (255-IPublicOff) +"<";
			}
			else
			{
				str2 ="SetLEDpublic=" + IPublicOff +"<";
			}
			
			byte[] bytes2 = str2.getBytes();
			serialPort.writeBytes(bytes2, bytes2.length);
			System.out.println("##################### " + str2+ " #######################" );
			image = i_on;
			trayIcon.setImage(image);
			b_switch.setIcon(i_Switch_on);
			
			
		}
		

		
		
		
		
		
	}
	public void schreibenINI()
	{
		System.out.println("##################### Schreiben Ini #######################" );
		PrintWriter pWriter = null;
	    String s = SHome + "\\videos\\siOSINI.txt";
	    try {
	        pWriter = new PrintWriter(new FileWriter(s));
	        pWriter.println("siOPTICA GMBH siOSHIELD INI");
	        pWriter.println("IPrivacyOn=" +IPrivacyOn);
	        pWriter.println("IPublicOn=" +IPublicOn);
	        pWriter.println("IPrivacyOff=" +IPrivacyOff);
	        pWriter.println("IPublicOff=" +IPublicOff);
	        pWriter.println("BReverseMode=" +BReverseMode);
	        
	    } catch (IOException ioe) {
	        ioe.printStackTrace();
	    } finally {
	        if (pWriter != null) {
	            pWriter.flush();
	            pWriter.close();
	        }
	    }
	}
	
	public void lesenINI()
	{
		System.out.println("##################### Lesen Ini #######################" );
		ladeDatei(SHome + "\\videos\\siOSINI.txt");
	}
	
	

	private void ladeDatei(String datName) {

	    File file = new File(datName);
	    String[] parts2 = null;
	    String[] parts3 = null;
	    String[] parts4 = null;
	    String[] parts5 = null;
	    String[] parts6 = null;
	   

	    if (!file.canRead() || !file.isFile())
	    {
	    	System.out.println("keine Ini" ); 
	    	JOptionPane.showMessageDialog(null, "First time to start write ini File", "InfoBox: " , JOptionPane.INFORMATION_MESSAGE);
	    	schreibenINI();
	    	System.exit(0);
	    }
	       

	        BufferedReader in = null;
	    try {
	        in = new BufferedReader(new FileReader(datName));
	        String zeile = null;
	        
	        Integer Z=1;
	        while ((zeile = in.readLine()) != null) {
	            System.out.println("Zeile NR = "+ Z + " Gelesene Zeile: " + zeile );
	            if(Z==2) parts2 = zeile.split("=");
	            if(Z==3) parts3 = zeile.split("=");
	            if(Z==4) parts4 = zeile.split("=");
	            if(Z==5) parts5 = zeile.split("=");
	            if(Z==6) parts6 = zeile.split("=");
	           
	            
	            Z=Z+1;
	        }
	    } catch (IOException e) {
	    	        e.printStackTrace();
	    } finally {
	        if (in != null)
	            try {
	                in.close();
	            } catch (IOException e) {
	            	
	            }
	    }
	   
	   
	    
	    
        IPrivacyOn= Integer.parseInt(parts2[1]);
        IPublicOn= Integer.parseInt(parts3[1]);
        IPrivacyOff= Integer.parseInt(parts4[1]);
        IPublicOff= Integer.parseInt(parts5[1]);
        BReverseMode=Boolean.valueOf(parts6[1]);
        
        
        CheckBoxReverse.setState(BReverseMode);
        SetWerte();
        //setLED();
	    
	    
	    
	   
	    
	    //System.out.println("Zeile NR = "+ Z + " Gelesene Zeile: " + zeile );
	    
	    
	}

	






	}//end of class