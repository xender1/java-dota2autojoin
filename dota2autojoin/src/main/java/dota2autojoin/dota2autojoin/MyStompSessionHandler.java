package dota2autojoin.dota2autojoin;

import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;


import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import org.springframework.web.socket.sockjs.client.SockJsClient; 
import org.springframework.web.socket.sockjs.client.Transport; 
import org.springframework.web.socket.sockjs.client.WebSocketTransport; 
import java.io.IOException; 
import java.lang.reflect.Type; 
import java.time.Duration; 
import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.List; 
import java.util.concurrent.ArrayBlockingQueue; 
import java.util.concurrent.BlockingQueue; 
import java.util.concurrent.TimeUnit; 


public class MyStompSessionHandler extends StompSessionHandlerAdapter {

	
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		System.out.println("Connected to websocket...");
		String message = "{" + '"' + "message" + '"' + ":" + '"' + "FROM JAVA!" + '"' + "}";
		byte messageByteArr[] = message.getBytes();
		//session.send("/app/autojoinmessage", messageByteArr);
		System.out.println("Sent message");
		System.out.println("subscribing...");
		
		AutoJoinService myAutoJoin;
		//while (!myAutoJoin.findMatch());
		
		
		session.subscribe("/topic/autojoin", new StompFrameHandler() {
			
			public Type getPayloadType(StompHeaders headers) {
				System.out.println(headers.toString());
				return byte[].class;
			} 
			
			public void handleFrame(StompHeaders headers, Object payload) {
				System.out.println("handling frame");
				String message = new String((byte[])payload);
				System.out.println(message);
				//SimpleStompMessage mymessage = (SimpleStompMessage)payload;
			}


		});
	}
	
	
}
