package qqserver;


import qqcommon.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ServerThread extends Thread{
    private Socket socket;
    private String userID;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private StringBuffer sb = new StringBuffer();

    public ServerThread(Socket socket, String userID, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.userID = userID;
        this.ois = ois;
        this.oos = oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public String getUserID() {
        return userID;
    }

    //通过一个线程使服务器与客户端时刻保持通信
    @Override
    public void run() {
        boolean loop = true;
        while (loop) {
            try {
                //离线留言箱
                HashMap<String, Message> offLineMessage = ManageThread.getOffLineMessage();
                Set<String> keySet = offLineMessage.keySet();
                Iterator<String> iterator = keySet.iterator();
                //遍历留言箱查看是否有给当前用户的留言
                while (iterator.hasNext()) {
                    String next =  iterator.next();
                    if (next.equals(userID)){
                        //如果有就将此message发送
                        oos.writeObject(offLineMessage.get(userID));
                    }
                }
                //不断读取客户端发送来的信息
                Message message = (Message) ois.readObject();
                //如果消息类型是"4"即用户要求拉取在线好友名单
                if (message.getMessageType().equals(MessageType.MESSAGE_COMM_GET_ONLINEFRIEND)) {
                    //得到拉取在线好友的message信息
                    message = ClientList.listOnline(userID);
                    //向客户端发送
                    oos.writeObject(message);
                }
                //如果消息类型是"6"即用户请求退出登录
                if (message.getMessageType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    ManageThread.removeThread(this);
                    //结束线程
                    loop = false;
                    Message m = new Message();
                    //通知用户端退出
                    m.setMessageType(MessageType.MESSAGE_CLIENT_EXIT);
                    oos.writeObject(m);
                    //关闭资源
                    oos.close();
                    ois.close();
                    socket.close();
                }
                //如果消息类型是请求私聊消息
                if (message.getMessageType().equals(MessageType.MESSAGE_COMM_MES) ||
                message.getMessageType().equals(MessageType.MESSAGE_FILE_MES)) {
                    Message m = new Message();
                    ServerThread anotherThread = null;
                    Set<String> set = ManageThread.getThreads().keySet();
                    //遍历HashMap的keyset，查看客户请求发送的接受者是否在线
                    for (String o : set) {
                        //如果在线
                        if (o.equals(message.getReceiver())){
                            anotherThread = ManageThread.getThread(o);
                            break;
                        }
                    }
                    //不在线向客户端返回，并留言
                    if (anotherThread == null){
                        m.setMessageType(MessageType.MESSAGE_MESS_EXCEPTION);
                        oos.writeObject(m);
                        sb.append(message.getContent());
                        message.setContent(sb.toString());
                        offLineMessage.put(message.getReceiver(),message);
                    }else {
                        //向receiver发送此信息
                        anotherThread.oos.writeObject(message);
                    }
                }
                if (message.getMessageType().equals(MessageType.MESSAGE_COMM_ALL_MES)) {
                    Set<String> set = ManageThread.getThreads().keySet();
                    //遍历HashMap的keyset,向所有在线用户发送信息
                    for (String o : set) {
                        message.setReceiver(o);
                        ManageThread.getThread(o).oos.writeObject(message);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
