package com.zhjh.getui;

/**
 * Created by Administrator on 2018/7/9.
 */
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.zhjh.bean.GeTuiMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AppPush {

    //定义常量, appId、appKey、masterSecret 采用本文档 "第二步 获取访问凭证 "中获得的应用配置
    private  String appId = "C1ov0eKrqL7kMY9ulJuG09";
    private  String appKey = "He9zvSgMgv7T05TMQCbANA";
    private  String masterSecret = "hisLywTLTU8RgHQxK0Vja5";
    private  String url = "http://sdk.open.api.igexin.com/apiex.htm";
    private  String cid = "2369a00978be1a3847c4d9d7676e27b6";
    private IGtPush push;

    public static  void main(String[] args) throws IOException {
//        setMessageToMobile("王庆","1");
        AppPush appPush=new AppPush();

        //Alias推送测试
//        List<String> stringList=new ArrayList<>();
//        stringList.add("check");
//        stringList.add("check1");
//        if(stringList!=null&&stringList.size()>0){
//            GeTuiMessage geTuiMessage=new GeTuiMessage("儿童节","六一儿童节快乐","1");
//            appPush.sendMessageByAlias(stringList,geTuiMessage);
//        }

        //cid推送测试
        List<String> stringList=new ArrayList<>();
        stringList.add(appPush.cid);
         List<String> onLineList=appPush.getOnLineCID(stringList);
         if(onLineList!=null&&onLineList.size()>0){
             String content="待检#25#1417#E:\\IDEAUi\\IE-MES\\target\\IE-MES\\WEB-INF\\upload\\1532048540464_数据库.xlsx";
             GeTuiMessage geTuiMessage=new GeTuiMessage("儿童节1","六一儿童节快乐",content);
             appPush.sendMessageByCid(onLineList,geTuiMessage);
         }
        //所有用户推送
//        GeTuiMessage geTuiMessage=new GeTuiMessage("儿童节","所有小朋友六一儿童节快乐","公告:8");
//        appPush.setMessageToMobile(geTuiMessage);
    }

    public AppPush() {
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        this.push=push;
    }

    /**
     * 获取在线cid
     * @param stringList
     * @return
     */
    public List<String> getOnLineCID(List<String> stringList){
        List<String> onLineList=new ArrayList<>();
        for(int i=0;i<stringList.size();i++){
            String cid=stringList.get(i);
            Map<String, Object> stringObjectMap=push.getClientIdStatus(appId, cid).getResponse();
            if(stringObjectMap.get("result").equals("Online")){
                onLineList.add(cid);
            }
        }
        return onLineList;
    }

    public  void setMessageToMobile(GeTuiMessage geTuiMessage){
        NotificationTemplate notificationTemplate= notificationTemplateDemo(geTuiMessage);

        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);

        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
        AppMessage message = new AppMessage();
        message.setData(notificationTemplate);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);

        IPushResult ret = push.pushMessageToApp(message);
        System.out.println(ret.getResponse().toString());
    }

    public  NotificationTemplate notificationTemplateDemo(GeTuiMessage geTuiMessage) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);

        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(geTuiMessage.getTitle());
        style.setText(geTuiMessage.getContent());
        // 配置通知栏图标
        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);

        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent(geTuiMessage.getMessage());
        return template;
    }
    public  void sendMessageByAlias(List<String> stringList,GeTuiMessage geTuiMessage){
        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin_pushList_needDetails", "true");
        // 配置返回每个别名及其对应cid的用户状态，可选
        // System.setProperty("gexin_pushList_needAliasDetails", "true");
        // 通知透传模板
        NotificationTemplate template = notificationTemplateDemo(geTuiMessage);
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List targets = new ArrayList();
        for(int i=0;i<stringList.size();i++){
            Target target=new Target();
            target.setAppId(appId);
            String alias=stringList.get(i);
            target.setAlias(alias);
            targets.add(target);
        }
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targets);
        System.out.println(ret.getResponse().toString());
    }
    public  void sendMessageByCid(List<String> stringList,GeTuiMessage geTuiMessage){
        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin_pushList_needDetails", "true");
        // 配置返回每个别名及其对应cid的用户状态，可选
        // System.setProperty("gexin_pushList_needAliasDetails", "true");
        // 通知透传模板
        NotificationTemplate template = notificationTemplateDemo(geTuiMessage);
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List targets = new ArrayList();
        for(int i=0;i<stringList.size();i++){
            Target target=new Target();
            target.setAppId(appId);
            String cid=stringList.get(i);
            target.setClientId(cid);
            targets.add(target);
        }
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targets);
        System.out.println(ret.getResponse().toString());
    }
}

