package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    private final Connection conn;

    public AccountDAO(Connection conn) {
        this.conn = conn;
    }

    
    public Account insertAccount(Account account) {
        String query = "INSERT INTO account (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    return new Account(newId, account.getUsername(), account.getPassword());
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    
    public List<Account> getAllAccounts() {
        List<Account> accountList = new ArrayList<>();
        String query = "SELECT * FROM account";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Account acc = new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                accountList.add(acc);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return accountList;
    }

    
    public Account getAccountByUsername(String username) {
        String query = "SELECT * FROM account WHERE username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    
    public Account getAccountByUsernameAndPassword(String username, String password) {
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
