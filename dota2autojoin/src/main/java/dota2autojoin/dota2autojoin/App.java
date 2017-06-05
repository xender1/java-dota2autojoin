package dota2autojoin.dota2autojoin;

import java.util.concurrent.ExecutionException;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;

/**
 * 
 * 
**/
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Starting Dota2AutoJoin" );
        
        SimpleStompClient dotaClient = new SimpleStompClient();
        
        ListenableFuture<StompSession> f;   
        StompSession stompSession = null;
        try {
        	System.out.println("connecting to ws...");
        	f = dotaClient.connect();
        	System.out.println("connected.");
        	
        	System.out.println("getting stompSession");
			stompSession = f.get();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
        
        if (stompSession != null) {
			System.out.println("subscribing...");
			dotaClient.subscribe(stompSession);
			
			System.out.println("sending test message");
			dotaClient.send(stompSession);
        }
        
        System.out.println("AutoJoinService starting...");
        AutoJoinService autoJoin = new AutoJoinService();
        
        while(!autoJoin.findMatch()) {
        	
        }
        
    } 
}
