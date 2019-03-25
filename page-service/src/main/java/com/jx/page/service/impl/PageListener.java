package com.jx.page.service.impl;

import com.jx.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/3/25
 * Time:19:26
 */
public class PageListener implements MessageListener {
    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            long id = Long.parseLong(textMessage.getText());
//            System.out.println("收到的Id："+id);
            itemPageService.genItemHtml(id);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
