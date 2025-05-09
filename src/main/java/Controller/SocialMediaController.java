package Controller;
import DAO.AccountDAO;
import DAO.MessageDAO;
import Util.ConnectionUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;



import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;


public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService(new AccountDAO(ConnectionUtil.getConnection()));
        this.messageService = new MessageService(new MessageDAO(ConnectionUtil.getConnection()));
        this.messageService.setAccountService(accountService);
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

       
        app.post("/register", this::handleRegister);
        app.post("/login", this::handleLogin);
        app.post("/messages", this::handleCreateMessage);
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/{message_id}", this::handleGetMessageById);
        app.get("/accounts/{account_id}/messages", this::handleGetUserMessages);
        app.delete("/messages/{message_id}", this::handleDeleteMessage);
        app.patch("/messages/{message_id}", this::handleUpdateMessage);
        
       

        return app;
    }

    private void handleRegister(Context ctx) {
        Account inputAccount = ctx.bodyAsClass(Account.class);
        Account createdAccount = accountService.register(inputAccount);

        if (createdAccount != null) {
            ctx.json(createdAccount);
        } else {
            ctx.status(400);
        }
    }

    private void handleLogin(Context ctx) {
        Account credentials = ctx.bodyAsClass(Account.class);
        Account result = accountService.login(credentials);

        if (result != null) {
            ctx.json(result);
        } else {
            ctx.status(401);
        }
    }

    private void handleCreateMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message result = messageService.createMessage(message);

        if (result != null) {
            ctx.json(result);
        } else {
            ctx.status(400);
        }
    }

    private void handleGetAllMessages(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    private void handleGetMessageById(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            ctx.json(message != null ? message : "");
        } catch (NumberFormatException e) {
            ctx.json("");
        }
    }

    private void handleDeleteMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message deleted = messageService.deleteMessage(messageId);
            ctx.json(deleted != null ? deleted : "");
        } catch (NumberFormatException e) {
            ctx.json("");
        }
    }

    private void handleUpdateMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message input = ctx.bodyAsClass(Message.class);
            Message updated = messageService.updateMessage(messageId, input.getMessage_text());

            if (updated != null) {
                ctx.json(updated);
            } else {
                ctx.status(400);
            }
        } catch (Exception e) {
            ctx.status(400);
        }
    }

    private void handleGetUserMessages(Context ctx) {
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            ctx.json(messageService.getMessagesByUser(accountId));
        } catch (NumberFormatException e) {
            ctx.json("");
        }
    }
}
