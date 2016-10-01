package com.xyzc.gemini.controller;

import com.thoughtworks.xstream.XStream;
import com.xyzc.gemini.wechat.model.local.WechatAllMediaDescResult;
import com.xyzc.gemini.wechat.model.local.WechatMediaDesc;
import com.xyzc.gemini.wechat.model.local.WechatMediaDescManage;
import com.xyzc.gemini.wechat.utils.ErrorCode;
import com.xyzc.gemini.wechat.utils.SHA1;
import com.xyzc.gemini.wechat.utils.SerializeXmlUtil;
import com.xyzc.gemini.wechat.model.local.WechatMedia;
import com.xyzc.gemini.wechat.model.message.ImageMessage;
import com.xyzc.gemini.wechat.model.message.InputMessage;
import com.xyzc.gemini.wechat.model.message.MsgType;
import com.xyzc.gemini.wechat.model.message.OutputMessage;
import com.xyzc.gemini.wechat.service.WechatService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by xyzc on 2016/9/28.
 */
@Controller
public class WechatController {
    private static Logger logger = Logger.getLogger(WechatController.class);
    private String Token = "123456789gemini";
    private static final String GUIDE_EXCHANGE_PIC = "欢迎参加WeBoth举办的十一“图片交换”活动，活动需要通过以下方式进行：\n" +
            "  1.发送一张您认为有趣的旅行的图片\n" +
            "  2.发送图片的描述信息：时间，地点和故事，中间以“#”隔开，如：\n" +
            "2016/10/01/05:33#山东泰山#还有一点点到顶，身边络绎不绝的脚步声。那么多未知，我不能慌\n" +
            "如格式正确，则您会立即收到交换后的图片和信息，若不正确，请按提示信息进行操作。\n" +
            "  注：每发送一张图片，则必须为此图片添加正确的描述信息，获得交换图片后才能进行下一轮的图片交换！" +
            "祝大家玩的开心 o(*￣▽￣*)ブ";
    private static final String GUIDE_ERROR_FORM = "消息已收到！WeBoth正在开展“图片交换”活动" +
            "，如果您未参加此活动，您可以回复“图片交换”参加或者忽略以下信息：\n\n" +
            "您输入的描述信息格式有误，请重新发送图片的描述信息：时间，地点和故事，中间以“#”隔开，如：\n" +
            "2016/10/01/05:33#山东泰山#还有一点点到顶，身边络绎不绝的脚步声。那么多未知，我不能慌\n" +
            "  注：有任何疑问请联系后台小编Jwx506142129";

    private static final String GUIDE_PIC_NEED_DESCRIPTION = "您上次发送的图片还未添加描述信息" +
            "，您可以重新发送图片的描述信息：时间，地点和故事，中间以“#”隔开，如：\n" +
            "2016/10/01/05:33#山东泰山#还有一点点到顶，身边络绎不绝的脚步声。那么多未知，我不能慌\n\n" +
            "或者发送 “删除未完成图片”对未添加描述信息的图片进行删除，\n"+
            "也可以发送 “显示未完成图片” 查看您未添加描述的图片";

    @Resource
    WechatService wechatService;

    @RequestMapping(value = "wechat", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public void wechat(Model model, HttpServletRequest request
            , HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入chat");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        if (isGet) {
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            System.out.println(signature);
            System.out.println(timestamp);
            System.out.println(nonce);
            System.out.println(echostr);
            access(request, response);
        } else {
            // 进入POST聊天处理
            System.out.println("enter post");
            try {
                // 接收消息并返回消息
                acceptMessage(request, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 验证URL真实性
     */
    private String access(HttpServletRequest request, HttpServletResponse response) {
        // 验证URL真实性
        System.out.println("进入验证access");
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");// 随机数
        String echostr = request.getParameter("echostr");// 随机字符串
        List<String> params = new ArrayList<String>();
        params.add(Token);
        params.add(timestamp);
        params.add(nonce);
        // 1. 将token、timestamp、nonce三个参数进行字典序排序
        Collections.sort(params, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        String temp = SHA1.encode(params.get(0) + params.get(1) + params.get(2));
        if (temp.equals(signature)) {
            try {
                response.getWriter().write(echostr);
                System.out.println("成功返回 echostr：" + echostr);
                return echostr;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("失败 认证");
        return null;
    }

    private void acceptMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 处理接收消息
        ServletInputStream in = request.getInputStream();
        // 将POST流转换为XStream对象
        XStream xs = createXStream();
        // 将流转换为字符串
        StringBuilder xmlMsg = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            xmlMsg.append(new String(b, 0, n, "UTF-8"));
        }
        InputMessage inputMsg = (InputMessage) xs.fromXML(xmlMsg.toString());
        String custerName = inputMsg.getFromUserName();
        String serverName = inputMsg.getToUserName();
        String outXml = "";
        if (inputMsg.getMsgType().equals(MsgType.Text.toString())) {
            dumpInputText(inputMsg);
            String content = inputMsg.getContent();
            if (StringUtils.isEmpty(content)) {
                outXml = createTextXmlStr(custerName, serverName, "发生了未知错误-10001");
            } else if (content.equals("图片交换")) {
                outXml = createTextXmlStr(custerName, serverName, GUIDE_EXCHANGE_PIC);
            } else if (content.equals("显示未完成图片")) {
                List<WechatMediaDesc> wechatMedias = wechatService.findUnFinishedMedia(custerName);
                if (wechatMedias == null || wechatMedias.isEmpty()) {
                    outXml = createTextXmlStr(custerName, serverName, "不存在未完成图片");
                } else {
                    for (WechatMediaDesc media : wechatMedias) {
                        outXml += xs.toXML(createImageMsg(custerName, serverName, media.getMediaId()));
                    }
                }
            } else if (content.equals("删除未完成图片")) {
                int err = wechatService.delUnFinishedMedia(custerName);
                System.out.println("err: " + err);
                if (err == ErrorCode.ERROR_SUCCESS) {
                    outXml = createTextXmlStr(custerName, serverName, "成功删除未完成图片");
                    System.out.println("outXml: " + outXml);
                } else if (err == ErrorCode.ERROR_NO_UNFINISHED_PIC) {
                    outXml = createTextXmlStr(custerName, serverName, "不存在未完成图片");
                    System.out.println("outXml: " + outXml);
                } else {
                    outXml = createTextXmlStr(custerName, serverName, "发生了未知错误-10002");
                    System.out.println("outXml: " + outXml);
                }
            } else if (content.equals("看图片")) {
                WechatMediaDesc mediaDesc = wechatService.findUnSelfRadomMedia(custerName);
                if (mediaDesc == null) {
                    outXml = createTextXmlStr(custerName, serverName, "您上一次交换到的图片有可能已经被删除，请重新交换图片哦~");
                } else {
                    outXml = xs.toXML(createImageMsg(custerName, serverName, mediaDesc.getMediaId()));
                    System.out.println("xml转换：/n" +outXml);
                }
            } else if (content.equals("看故事")) {
                WechatMediaDesc mediaDesc = wechatService.findUnSelfRadomMedia(custerName);
                if (mediaDesc == null) {
                    outXml = createTextXmlStr(custerName, serverName, "您上一次交换到的图片有可能已经被删除，请重新交换图片哦~");
                } else {
                    outXml = createTextXmlStr(custerName, serverName
                            , "时间：" + mediaDesc.getTime()
                                    + "\n" + "地点：" + mediaDesc.getLocation()
                                    + "\n" + "    " + mediaDesc.getStory()
                                    + "\n\n" + "回复 “看图片” 显示图片");
                }
            } else {
                outXml = addMediaDescription(custerName, serverName, content, xs);
            }
        } else if (inputMsg.getMsgType().equals(MsgType.Image.toString())) {
            dumpInputMedia(inputMsg);
            try {
                int err = wechatService.addMedia(custerName, inputMsg.getMediaId(), inputMsg.getPicUrl(), "image");
                if (err == ErrorCode.ERROR_SUCCESS) {
                    outXml = createTextXmlStr(custerName, serverName, "现在您可以按照格式添加图片的描述信息了~");
                } else if (err == ErrorCode.ERROR_HAS_UNFINISHED_PIC) {
                    outXml = createTextXmlStr(custerName, serverName, GUIDE_PIC_NEED_DESCRIPTION);
                } else {
                    outXml = createTextXmlStr(custerName, serverName, "发生了未知错误-10003");
                }
            } catch (Exception e) {
                logger.error("[wechat] add picture happens: ", e);
                outXml = createTextXmlStr(custerName, serverName, "发生了未知错误-10004");
            }
        } else {
            outXml = createTextXmlStr(custerName, serverName, "WeBoth图片交换活动期间，只接收文字和图片信息，你可以发送“图片交换”获取详细信息");
        }
        System.out.println("final outXml: " + outXml);
        response.getWriter().write(outXml);
    }

    private String addMediaDescription(String custerName, String serverName, String content, XStream xs) {
        if (StringUtils.isEmpty(content)) {
            return createTextXmlStr(custerName, serverName, "发生了未知错误-10005");
        }
        List<String> desc = Arrays.asList(content.split("#"));
        if (desc == null || desc.size() != 3) {
            desc = Arrays.asList(content.split("＃"));
            if (desc == null || desc.size() != 3) {
                return createTextXmlStr(custerName, serverName, GUIDE_ERROR_FORM);
            }
        }
        WechatMediaDesc mediaDesc = new WechatMediaDesc();
        mediaDesc.setOpenId(custerName);
        mediaDesc.setTime(desc.get(0));
        mediaDesc.setLocation(desc.get(1));
        mediaDesc.setStory(desc.get(2));
        String result = "";
        try {
            int err = wechatService.addMediaDescription(custerName, mediaDesc);
            if (err == ErrorCode.ERROR_SUCCESS) {
                WechatMediaDesc media = wechatService.findUnSelfRadomMedia(custerName);
                if (media == null) {
                    result = createTextXmlStr(custerName, serverName, "未交换到任何图片哦~");
                } else {
                    result = createTextXmlStr(custerName, serverName
                            , "时间：" + media.getTime()
                                    + "\n" + "地点：" + media.getLocation()
                                    + "\n" + "    " + media.getStory()
                                    + "\n\n" + "回复 “看图片” 显示图片");
                }
            } else if (err == ErrorCode.ERROR_DESC_NEED_PIC) {
                result = createTextXmlStr(custerName, serverName, "骚年，您需要先添加图片哦~");
            } else {
                result = createTextXmlStr(custerName, serverName, "发生了未知错误-10006");
            }
        } catch (Exception e) {
            logger.error("[wechat] add media description happens: ", e);
            result = createTextXmlStr(custerName, serverName, "发生了未知错误-10007，如果描述中有表情，请尝试去除~");
        }
        return result;
    }

    private void dumpInputText(InputMessage inputMsg) {
        System.out.println("开发者微信号：" + inputMsg.getToUserName());
        System.out.println("发送方帐号：" + inputMsg.getFromUserName());
        System.out.println("消息创建时间：" + inputMsg.getCreateTime()
                + new Date(inputMsg.getCreateTime() * 1000l));
        System.out.println("消息内容：" + inputMsg.getContent());
        System.out.println("消息Id：" + inputMsg.getMsgId());
    }

    private void dumpInputMedia(InputMessage inputMsg) {
        System.out.println("获取多媒体信息");
        System.out.println("多媒体文件id：" + inputMsg.getMediaId());
        System.out.println("图片链接：" + inputMsg.getPicUrl());
        System.out.println("消息id，64位整型：" + inputMsg.getMsgId());
    }

    private XStream createXStream() {
        XStream xs = SerializeXmlUtil.createXstream();
        xs.processAnnotations(InputMessage.class);
        xs.processAnnotations(OutputMessage.class);
        // 将指定节点下的xml节点数据映射为对象
        xs.alias("xml", InputMessage.class);
        return xs;
    }

    private String createTextXmlStr(String custerName, String serverName, String content) {
        StringBuffer str = new StringBuffer();
        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间
        str.append("<xml>");
        str.append("<ToUserName><![CDATA[" + custerName + "]]></ToUserName>");
        str.append("<FromUserName><![CDATA[" + serverName + "]]></FromUserName>");
        str.append("<CreateTime>" + returnTime + "</CreateTime>");
        str.append("<MsgType><![CDATA[" + MsgType.Text.toString() + "]]></MsgType>");
        str.append("<Content><![CDATA[" + content + "]]></Content>");
        str.append("</xml>");
        System.out.println(str.toString());
        return str.toString();
    }

    private OutputMessage createImageMsg(String custerName, String serverName, String mediaId) {
        OutputMessage outputMsg = new OutputMessage();
        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间
        outputMsg.setFromUserName(serverName);
        outputMsg.setToUserName(custerName);
        outputMsg.setCreateTime(returnTime);
        outputMsg.setMsgType(MsgType.Image.toString());
        ImageMessage images = new ImageMessage();
        //images.setMediaId(inputMsg.getMediaId());
        images.setMediaId(mediaId);
        outputMsg.setImage(images);
        return outputMsg;
    }

    @RequestMapping(value = "allMediaDesc")
    @ResponseBody
    public JSONObject allMediaDesc(
            @RequestParam(required = false) int pageIndex,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retJson = new JSONObject();
        try{
            WechatAllMediaDescResult result = wechatService.getAllFinishedMediaDescs(pageIndex);
            System.out.print("all media desc request\n");
            retJson.put("list", result.getDescManages());
            retJson.put("pageIndex", result.getPageIndex());
            retJson.put("pageNum", result.getPageNum());
        } catch (Exception e) {
            logger.error("[wechat] manage wechat happens: ", e);
            retJson.put("list", "[]");
            retJson.put("pageIndex", 1);
            retJson.put("pageNum", 1);
        }
        return retJson;
    }

    @RequestMapping(value = "manage", method = { RequestMethod.GET, RequestMethod.POST })
    public String manageMediaDesc(
            @RequestParam(required = false) String token,
            HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(token) || !token.equals("xyzc")) {
            return "fail";
        }
        return "geminiManager";
    }

    @RequestMapping(value = "delDescById")
    @ResponseBody
    public JSONObject delDescById(
            @RequestParam(required = false) int id,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retJson = new JSONObject();
        try {
            wechatService.deleteDescById(id);
            retJson.put("status", "succ");
        } catch (Exception e) {
            logger.error("[wechat] delete desc by id happens: ", e);
            retJson.put("status", "fail");
        }
        return retJson;
    }

    @RequestMapping(value = "test")
    @ResponseBody
    public void test(HttpServletRequest request, HttpServletResponse response) {
        WechatMedia wechatMedia = new WechatMedia();
        wechatMedia.setMediaId("BM1434HHNPIO89");
        wechatMedia.setMediaUrl("http://115.29.144.199/proj/imgs/dog.ico");
        wechatService.addMedia("fdffdsafdas", wechatMedia.getMediaId(), wechatMedia.getMediaUrl(), "image");
        System.out.println(wechatService.findUnSelfRadomMedia("fdffdsafdas"));
    }

}
