package com.epicsell.utils;

import com.epicsell.beans.Shop;
import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.*;

@SuppressWarnings("unused")
public class GmailInbox {

    private String mail;
    private String password;
    private static Store store = null;
    private static IMAPFolder folder = null;
    private static List<Message> newMessages;
    private static final Long timeout = 40000L;

    static {
        newMessages = new ArrayList<Message>();
    }

    public GmailInbox(Shop shop) {
        this.mail = shop.getEmail();
        this.password = shop.getPassword();
    }

    public GmailInbox(String email, String password) {
        this.mail = email;
        this.password = password;
    }

    private void connectToGmail() throws Exception {
        newMessages.clear();
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imap.connectiontimeout", "5000");
        props.setProperty("mail.imap.timeout", "5000");
        try {
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", this.mail, this.password);
            folder = (IMAPFolder) store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            folder.addMessageCountListener(new MessageCountListener() {
                public void messagesAdded(MessageCountEvent e) {
                    newMessages.addAll(Arrays.asList(e.getMessages()));
                }

                public void messagesRemoved(MessageCountEvent e) {
                    // nothing to do
                }
            });
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            throw new Exception("Can't connect");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new Exception("Can't connect");
        }
    }

    private static void waitForNewMessage(long timeout) throws MessagingException {
        Timer timeoutTimer = new Timer();
        timeoutTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    disconnectFromGmail();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }, timeout);
        folder.idle(true);
        timeoutTimer.cancel();
    }

    private static void disconnectFromGmail() throws MessagingException {
        if (folder != null) {
            folder.close(true);
            folder = null;
        }
        if (store != null) {
            store.close();
            store = null;
        }
    }

    public void archiveInbox() throws Exception {
        try {
            connectToGmail();
            Message[] messages = folder.getMessages();
            for (Message mes : messages) {
                mes.getContent();
                mes.setFlag(Flags.Flag.DELETED, true);
            }
        } finally {
            disconnectFromGmail();
        }
    }

    public String getAdminLinkAfterRegistration() throws Exception {
        String registerLink;
        String messageContent = getNewMessage();
        messageContent = messageContent.substring(messageContent.indexOf("Ваш магазин находится здесь:<br/>"),
                messageContent.length());
        messageContent = messageContent.substring(0, messageContent.indexOf("</p>"));
        messageContent = messageContent.substring(messageContent.indexOf("href=\"") + "href=\"".length(),
                messageContent.indexOf("\">"));
        return messageContent;
    }

    public String getResetPasswordLink() throws Exception {
        String registerLink;
        String messageContent = getNewMessage();
        System.out.print(messageContent);
        messageContent = messageContent.substring(messageContent.indexOf("Перейдите по ссылке для того, чтобы поменять пароль"),
                messageContent.length());
        messageContent = messageContent.substring(0, messageContent.indexOf("</p>"));
        messageContent = messageContent.substring(messageContent.indexOf("href=\"") + "href=\"".length(),
                messageContent.indexOf("\">"));
        return messageContent;
    }

    private String getNewMessage() throws Exception {
        try {
            connectToGmail();
            if (folder.getMessageCount() != 0) {
                newMessages.addAll(Arrays.asList(folder.getMessages()));
            } else {
                waitForNewMessage(timeout);
            }
            if (newMessages == null) {
                throw new Exception("Wrong amount of mails. Should be exactly 1");
            }
            Message message = newMessages.get(newMessages.size() - 1);
            String content = message.getContent().toString();
            message.setFlag(Flags.Flag.DELETED, true);
            return content;
        } finally {
            try {
                disconnectFromGmail();
            } catch (MessagingException e) {
                //
            }
        }
    }
}
