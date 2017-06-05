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
        
        ListenableFuture<StompSession> f = dotaClient.connect();
        
        try {
        	System.out.println("got stomp");
			StompSession stompSession = f.get();
			System.out.println("subscribing");
			dotaClient.subscribe(stompSession);
			System.out.println("sending");
			dotaClient.send(stompSession);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
        
        /*
        List<Transport> transports = new ArrayList<Transport>(1);
        //transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());
        
       //WebSocketClient webSocketClient = new StandardWebSocketClient();
       WebSocketClient webSocketClient = new SockJsClient(transports);
        
        
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        //stompClient.setMessageConverter(new StringMessageConverter());
        //stompClient.setTaskScheduler(taskScheduler);
        
        String url = "ws://192.168.0.17:8080//ws-autojoin";
        StompSessionHandler sessionHandler = new MyStompSessionHandler();

        stompClient.connect(url, sessionHandler);
        */
        System.out.println("hello from main");

    } 
}
