package dota2autojoin.dota2autojoin;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;



/**
 * java -cp target\dota2autojoin-0.0.1-SNAPSHOT-shaded.jar dota2autojoin.dota2autojoin.App
 * 
**/
public class App 
{
	
	public static boolean testBool = false;
	
    public static void main( String[] args ) throws AWTException, InterruptedException
    {
        System.out.println( "Starting Dota2AutoJoin" );
        
        SimpleStompClient dotaClient = new SimpleStompClient();
        
        ListenableFuture<StompSession> f = null;   
        StompSession stompSession = null;
        try {
        	System.out.println("connecting to ws...");
        	f = dotaClient.connect();
			stompSession = f.get();
        } catch (Exception e) {
        	System.out.println("[Error]Connection Exception: " + e.getMessage());
        }
    	
        if (stompSession != null) {
        	System.out.println("connected.");
			System.out.println("subscribing...");
			dotaClient.subscribe(stompSession);
			
			System.out.println("sending test message");
			dotaClient.send(stompSession);
        } else {
        	System.out.println("No WS Connection...Running locally...");
        }
        
        System.out.println("AutoJoinService starting...");
        AutoJoinService autoJoin = new AutoJoinService();
        
        
        
        Robot robot = new Robot();
        int x = -1287;
        int y = 377;
        int width = 290;
        int height = 80;
        Rectangle area = new Rectangle(x, y, width, height);
        BufferedImage buffImg = robot.createScreenCapture(area);
        //;
        Color pcolor = new Color(buffImg.getRGB(buffImg.getWidth() - 1, buffImg.getHeight() - 1));
        System.out.println(pcolor.getRed() + " " + pcolor.getBlue() + " " + pcolor.getGreen());
        ITesseract instance = new Tesseract();
        try {
        	String result = instance.doOCR(buffImg);
        	System.out.println("result: " + result);
        } catch (TesseractException e) {
        	System.out.println(e.getMessage());
        }
        
        
        //instance.d
        Runnable mouseRunnable = createRunnable(dotaClient, stompSession);
        
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(mouseRunnable, 0, 3, TimeUnit.SECONDS);
        
        //ExecutorService newServ = Executors.newSingleThreadExecutor();
        //newServ.execute(createRunnable(dotaClient, stompSession));

        while (!autoJoin.foundMatch()) {
        	System.out.println("Checking of Accept Button Showing");
        	autoJoin.checkIfAcceptPopped();
        	Thread.sleep(5000);
        }
        while (!autoJoin.acceptedMatch()) {
        	System.out.println("Accepting match");
        	autoJoin.clickAcceptMatch();
        }
        //check that clicked / waiting / loading for players
        
        //check that loaded in
        /*
        while(!autoJoin.loadedMatch()) {
        	while (!autoJoin.acceptMatch()) {
        		while (!autoJoin.foundMatch()) {
        			autoJoin.findMatch();
        		}
        		
        	}
        }*/
        
    }
    
    private static Runnable createRunnable(final SimpleStompClient dotaClient, final StompSession stompSession) {
    	Runnable aRunnable = new Runnable() {
    		public void run() {
        		PointerInfo a = MouseInfo.getPointerInfo();
        		Point b = a.getLocation();
        		int x = (int)b.getX();
        		int y = (int)b.getY();
        		System.out.println(x + "," + y);
        		//dotaClient.send(stompSession, x, y);
        		testBool = true;
    		}
    	};
    	return aRunnable;
    }
}
