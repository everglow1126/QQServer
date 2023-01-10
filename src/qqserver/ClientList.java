package qqserver;

import qqcommon.Message;
import qqcommon.MessageType;

import javax.swing.text.html.HTMLDocument;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ClientList {
    //创建一个合法用户的名单，存储用户名和密码
    private static HashMap<String,String> list = new HashMap<>();
    //初始化
    public ClientList() {
        list.put("100","123456");
        list.put("syf","syfshibaba");
        list.put("jbc","jbcshierzi");
    }
    //检查用户是否合法，即是否在list里面
    public static boolean checkUser(String id,String pd){
        boolean ans = false;
        Set<Map.Entry<String, String>> entries = list.entrySet();
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry next =  (Map.Entry) iterator.next();
            if (id.equals(next.getKey()) && pd.equals(next.getValue())){
                ans = true;
                break;
            }
        }
        return ans;
    }

    public static Message listOnline(String id){
        //返回的消息
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_COMM_RET_ONLINEFRIEND);
        //利用StringBuilder增添用户字符串
        StringBuffer sb = new StringBuffer("=====当前用户在线列表=====");
        sb.append("\n");
        //得到当前已有线程的keySet
        Set<String> keySet = ManageThread.getThreads().keySet();
        System.out.println(keySet.size());
        //如果当前在线用户只有一个，则代表没有好友在线
        if (keySet.size() == 1){
            message.setContent("=====没有好友在线=====");
        }else {
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String next = (String) iterator.next();
                if (!next.equals(id)) {
                    sb.append(next + " ");
                }
            }
            message.setContent(sb.toString());
        }
        return message;
    }
}
