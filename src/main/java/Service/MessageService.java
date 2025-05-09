package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountService accountService;

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    // Allow setting AccountService after construction to prevent circular dependencies
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    
    public Message createMessage(Message message) {
        String content = message.getMessage_text();

        if (content == null || content.trim().isEmpty() || content.length() > 255) {
            return null;
        }

        if (!accountService.accountExists(message.getPosted_by())) {
            return null;
        }

        // Automatically assign current timestamp if none is provided
        if (message.getTime_posted_epoch() == 0) {
            message.setTime_posted_epoch(System.currentTimeMillis());
        }

        return messageDAO.insertMessage(message);
    }

    
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Fetches a message by its unique ID.
     
     */
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    /**
     * Removes a message given its ID.
     */
    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    /**
     * Edits the content of a message
     */
    public Message updateMessage(int messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            return null;
        }

        return messageDAO.updateMessage(messageId, newMessageText);
    }

    /**
     * Retrieves all messages posted by a specific account.
     */
    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.getMessagesByUser(accountId);
    }
}
