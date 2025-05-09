package Service;

import DAO.AccountDAO;
import Model.Account;
import java.util.List;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    
    public Account register(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        if (password == null || password.length() < 4) {
            return null;
        }

        // Prevent duplicate usernames
        if (accountDAO.getAccountByUsername(username) != null) {
            return null;
        }

        // Add to database
        return accountDAO.insertAccount(account);
    }

    
    public Account login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        if (username == null || password == null) {
            return null;
        }

        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }

    
    public Account getAccountById(int accountId) {
        List<Account> accounts = accountDAO.getAllAccounts();
        for (Account acc : accounts) {
            if (acc.getAccount_id() == accountId) {
                return acc;
            }
        }
        return null;
    }

    public boolean accountExists(int accountId) {
        return getAccountById(accountId) != null;
    }
}
