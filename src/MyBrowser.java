
import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;




public class MyBrowser {
	
	private Combo combo;
	private Browser browser;
	

	public static void main(String[] args) throws IOException {
		MyBrowser mybrowser = new MyBrowser();
		mybrowser.init();
	}
	

	
	private void init() throws IOException{
		
		// SWT widget
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setImage(SWTResourceManager.getImage("globe.png"));
		shell.setLayout(new GridLayout(7, false));
		shell.setText("MyBrowser Demo");

		// Controls
		Button bBack = new Button(shell, SWT.NONE);//后退按钮
		bBack.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		bBack.setToolTipText("Back");
		bBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				browser.back();
				writeTxt(browser,"records.txt");
			}
		});
		bBack.setText("<");
        
        Button bForward = new Button(shell, SWT.NONE);//前进按钮
        bForward.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
        bForward.setToolTipText("Forward");
        bForward.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseDown(MouseEvent e) {
    			browser.forward();
    			writeTxt(browser,"records.txt");
    		}
    	});
        bForward.setText(">");
        
    	combo = new Combo(shell, SWT.NONE);//地址栏
    	combo.addKeyListener(new KeyAdapter() {
    		@Override
    		public void keyPressed(KeyEvent e) {
			// Enter was pressed.
			if(e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {//按下回车键时地址跳转
				browser.setUrl(combo.getText());
				writeTxt(browser,"records.txt");
				}        		
    		}
    	});
    	combo.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
    	combo.setText(setHomePage());    	
    	combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button bUrl = new Button(shell, SWT.NONE);//点击按钮跳转网址
		bUrl.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		bUrl.setToolTipText("Go to URL");
		bUrl.setText("go");
		bUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				browser.setUrl(combo.getText());
				writeTxt(browser,"records.txt");
			}
		});
		
		
		Button btnRefresh = new Button(shell, SWT.NONE);//点击按钮刷新网页
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				browser.refresh();
				writeTxt(browser,"records.txt");
			}
		});
		GridData gd_btnRefresh = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRefresh.heightHint = 30;
		gd_btnRefresh.widthHint = 25;
		btnRefresh.setLayoutData(gd_btnRefresh);
		btnRefresh.setImage(SWTResourceManager.getImage("icons8-refresh-26.png"));
		
		Button btnHome = new Button(shell, SWT.NONE);//点击按钮回到主页
		btnHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				readTxt(browser,"homepage.txt");
			}
		});
		GridData gd_btnHome = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnHome.heightHint = 30;
		gd_btnHome.widthHint = 25;
		btnHome.setLayoutData(gd_btnHome);
		btnHome.setImage(SWTResourceManager.getImage("icons8-home-page-24.png"));
		
		Button btnMore = new Button(shell, SWT.NONE);//“更多”	按钮
		GridData gd_btnMore1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnMore1.heightHint = 30;
		gd_btnMore1.widthHint = 25;
		btnMore.setLayoutData(gd_btnMore1);
		btnMore.setImage(SWTResourceManager.getImage("icons8-menu-vertical-24.png"));
		
		Menu menu_1 = new Menu(btnMore);//将当前网页设置为首页的选项
		btnMore.setMenu(menu_1);
		MenuItem mntmNewItem = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				writeTxt_homepage(browser,"homepage.txt");
				MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
		        dialog.setText("tip");
		        dialog.setMessage("您已将"+browser.getUrl()+"设为主页");
		        dialog.open();
			}
		});
		mntmNewItem.setText("设为主页");		
		
		MenuItem mntmNewItem_1 = new MenuItem(menu_1, SWT.NONE);//将当前网页添加到收藏中
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				writeTxt(browser,"collection.txt");
				MessageBox dialog=new MessageBox(shell,SWT.OK|SWT.ICON_INFORMATION);
		        dialog.setText("tip");
		        dialog.setMessage("您已将"+browser.getUrl()+"添加到收藏");
		        dialog.open();
			}
		});
		mntmNewItem_1.setText("添加到收藏");
		
		MenuItem mntmNewItem_5 = new MenuItem(menu_1, SWT.NONE);//打开收藏夹选项
		mntmNewItem_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openFile("collection.txt");
			}
		});
		mntmNewItem_5.setText("打开收藏夹");
		
		MenuItem mntmNewItem_2 = new MenuItem(menu_1, SWT.NONE);	
		mntmNewItem_2.addSelectionListener(new SelectionAdapter() {//ping
			@Override
			public void widgetSelected(SelectionEvent e) {
				ping_tracert(display, browser,"ping");
			}
		});
		mntmNewItem_2.setText("ping");
		
		MenuItem mntmNewItem_3 = new MenuItem(menu_1, SWT.NONE);//tracert
		mntmNewItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ping_tracert(display, browser,"tracert");
			}
		});
		mntmNewItem_3.setText("tracert");
		
		MenuItem mntmNewItem_4 = new MenuItem(menu_1,SWT.DROP_DOWN);//打开历史记录的选项
		mntmNewItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openFile("records.txt");
			}
		});
		mntmNewItem_4.setText("历史记录");

		// SWT Browser
		browser=new Browser(shell,SWT.NONE);	
		browser.addLocationListener(new LocationAdapter() {
			@Override
			public void changed(LocationEvent event) {
				if(!browser.getUrl().equals(combo.getText())){
					// Synchronize URL in combo 
					int idx = combo.indexOf(browser.getUrl());
					if (idx<0){
						combo.add(browser.getUrl(),0);
						combo.select(0);
					} else {
						combo.select(idx);
					}
				}
			}
		});		
		browser.setUrl(combo.getText());
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));	
		browser.addOpenWindowListener(new OpenWindowListener(){ 
			public void open(WindowEvent e){ // Embed the new window 
			final Shell shell = new Shell(display); 
			final Browser browser2 = new Browser(shell, SWT.NONE);
			e.browser = browser2;//将e的事件用我的浏览器打开 
			e.display.asyncExec(new Runnable() { //在SWT异步线程中进行对外观部件的更改
				public void run() { 
					String url = browser2.getUrl(); 
					browser.setUrl(url);
					shell.close(); 
						}
					});
				}
			}
		);

        shell.open();

    	while (!shell.isDisposed()) {
        		if (!display.readAndDispatch()) {
            		display.sleep();
        		}
        		else{
        			display.wake();
        		}
    	}

    	display.close();		
	}
	
	 void readTxt(Browser browser,String filePath) {//读取txt文件中的信息			
		  try {
		    File file = new File(filePath);	
		    String lineTxt = null;
		    if(file.isFile() && file.exists()) {
		      InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
		      BufferedReader br = new BufferedReader(isr);	
		      
		      while ((lineTxt = br.readLine()) != null) {
		        browser.setUrl(lineTxt);
		      }
		      br.close();
		    } else {
		      System.out.println("文件不存在!");
		    }
		  } catch (Exception e) {
		    System.out.println("文件读取错误!");
		  }	  
	}
	 
	 String setHomePage() throws IOException {//运行打开浏览器时设置主页
		 File file = new File("homepage.txt");
		 String lineTxt = null;
		    if(file.isFile() && file.exists()&&(file.length()>0)) {
		      InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
		      BufferedReader br = new BufferedReader(isr);	
		      while ((lineTxt = br.readLine()) != null) {		    	
			        break;
			      }
			  br.close();
			  return lineTxt;
		    }
		    else {
		    	return "about:blank";
		    }
		 
	 }
	 
	 void writeTxt_homepage(Browser browser,String filePath) {
		 try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw  		  
	            /* 读入TXT文件 */  
	            java.io.File file = new java.io.File(filePath); 
	            java.io.PrintWriter output = new java.io.PrintWriter(file);
			    if(file.isFile() && file.exists()) {
			    	//DataOutputStream os = new DataOutputStream(new FileOutputStream(file));		    
			    	output.println(browser.getUrl());
			    	output.close();
			    }
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	 
	 void writeTxt(Browser browser,String filePath) {//写入txt文件信息
		 try {  		   
	            java.io.File file = new java.io.File(filePath); 
	            FileWriter fwriter = new FileWriter(file,true);
			    if(file.isFile() && file.exists()) {		    
			    	fwriter.write(browser.getUrl()+"\r\n");
			    	fwriter.close();
			    }
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }
	 
	 void openFile(String filePath) {//打开相应的文件
		 try {
			 File file = new File(filePath); 
			 Desktop.getDesktop().open(file); // 启动已在本机桌面上注册的关联应用程序，打开文件文件file。
			 } 
		 catch (IOException | NullPointerException e) { 
			  System.err.println(e);
			 }
		 }
	 
	 void ping_tracert(Display display, Browser browser,String command){//执行ping和tracert指令
			String url = browser.getUrl().trim();
			String serverIP = null;
			if(url.contains("https://")) {//用两个if语句截取不同协议的网址
				String temp = url.substring(8);
				serverIP = temp.substring(0,temp.length()-1 );
			}
			if(url.contains("http://")) {
				String temp = url.substring(7);
				serverIP = temp.substring(0,temp.length()-1);
			}
			final Shell shell = new Shell(display); 
			shell.setSize(500,400);
			shell.open();
			Text text = new Text(shell, SWT.V_SCROLL|SWT.BORDER|SWT.V_SCROLL|SWT.CANCEL|SWT.MULTI);	
			//设置text的滚动条
			text.setLocation(0,0);
			text.setSize(500, 400);
			try {  
				Runtime ce = Runtime.getRuntime();
				InputStream in;
				BufferedInputStream bin;
				if(command.equals("ping")) {
					in = (InputStream) ce.exec("ping " + serverIP).getInputStream();
					bin = new BufferedInputStream(in);
					byte pingInfo[] = new byte[100];				
					int n;
					while ((n = bin.read(pingInfo, 0, 100)) != -1) {
						String s = null;
						s = new String(pingInfo, 0, n,"gbk");//设置了编码方式
						text.append(s+"\r\n");
						
					}
				}
				if(command.equals("tracert")) {
					in = (InputStream) ce.exec("tracert " + serverIP).getInputStream();
					bin = new BufferedInputStream(in);
					bin = new BufferedInputStream(in);
					byte pingInfo[] = new byte[100];				
					int n;
					while ((n = bin.read(pingInfo, 0, 100)) != -1) {
						String s = null;
						s = new String(pingInfo, 0, n,"gbk");//设置了编码方式
						text.append(s+"\r\n");
						
					}
				}
				
				text.append("Over!\n\n");
			} catch (Exception ee) {
				System.out.println(ee);
			}
	 	}
	 }


