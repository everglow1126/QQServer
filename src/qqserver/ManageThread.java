package qqserver;

import qqcommon.Message;
import qqcommon.User;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageThread {
    //存储离线留言
    private static HashMap<String, Message> offLineMessage = new HashMap<>();
    //存储用户线程
    private static HashMap<String, ServerThread> threads = new HashMap<>();

    public static HashMap<String, Message> getOffLineMessage() {
        return offLineMessage;
    }

    public static void addThread(ServerThread st){
        threads.put(st.getUserID(),st);
    }

    public static HashMap<String, ServerThread> getThreads() {
        return threads;
    }

    public static void removeThread(ServerThread st){
        threads.remove(st.getUserID());
    }

    public static ServerThread getThread(String userID){
        return threads.get(userID);
    }
}
