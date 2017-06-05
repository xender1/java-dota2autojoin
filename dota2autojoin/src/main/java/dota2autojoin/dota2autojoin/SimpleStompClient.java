package dota2autojoin.dota2autojoin;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;

public class SimpleStompClient {

	public ListenableFuture<StompSession> connect() {
        List<Transport> transports = new ArrayList<Transport>(1);
        //transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

		SockJsClient sockJsClient = new SockJsClient(transports);
		//sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

		String url = "ws://192.168.0.17:8080//ws-autojoin";
		return stompClient.connect(url, new MyHandler());
	}
	
	public void subscribe(StompSession stompSession) {
		stompSession.subscribe("/topic/autojoin",  new StompFrameHandler() {
			
			public Type getPayloadType(StompHeaders headers) {
				System.out.println(headers.toString());
				return byte[].class;
			} 
			
			public void handleFrame(StompHeaders headers, Object payload) {
				System.out.println("handling frame");
				String message = new String((byte[])payload);
				System.out.println(message);
			}


		});
	}
	
	public void send(StompSession stompSession) {
		String jsonHello = "{ \"message\" : \"Updated Java\" }";
		stompSession.send("/app/autojoinmessage", jsonHello.getBytes());
	}
	
	private class MyHandler extends StompSessionHandlerAdapter {
		public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
			System.out.println("connected.");
		}
	}
}
