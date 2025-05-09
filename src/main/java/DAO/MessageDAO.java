package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    private final Connection conn;

    public MessageDAO(Connection conn) {
        this.conn = conn;
    }

    
    public Message insertMessage(Message message) {
        String query = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());

            stmt.executeUpdate();

            ResultSet keySet = stmt.getGeneratedKeys();
            if (keySet.next()) {
                int id = keySet.getInt(1);
                return new Message(id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

   
    public List<Message> getAllMessages() {
        List<Message> messageList = new ArrayList<>();
        String query = "SELECT * FROM message";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Message msg = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                messageList.add(msg);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return messageList;
    }

    
    public Message getMessageById(int messageId) {
        String query = "SELECT * FROM message WHERE message_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    
    public Message deleteMessage(int messageId) {
        Message existing = getMessageById(messageId);
        if (existing == null) return null;

        String query = "DELETE FROM message WHERE message_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, messageId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) return existing;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    
    public Message updateMessage(int messageId, String newText) {
        String query = "UPDATE message SET message_text = ? WHERE message_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newText);
            stmt.setInt(2, messageId);

            int updated = stmt.executeUpdate();

            if (updated > 0) {
                return getMessageById(messageId);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    
    public List<Message> getMessagesByUser(int accountId) {
        List<Message> userMessages = new ArrayList<>();
        String query = "SELECT * FROM message WHERE posted_by = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Message msg = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                userMessages.add(msg);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return userMessages;
    }
}
