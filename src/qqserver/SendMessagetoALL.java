package qqserver;

import qqcommon.Message;
import qqcommon.MessageType;
import util.Util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class SendMessagetoALL implements Runnable{

    @Override
    public void run() {
        while (true){
            System.out.print("请输入你想推送的消息: ");
            String news = Util.readString(100);
            Message message = new Message();
            message.setMessageType(MessageType.MESSAGE_COMM_MES);
            message.setSender("服务器");
            message.setContent(news);
            HashMap<String, ServerThread> threads = ManageThread.getThreads();
            Set<String> keySet = threads.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String next =  iterator.next();
                message.setReceiver(next);
                try {
                    threads.get(next).getOos().writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
