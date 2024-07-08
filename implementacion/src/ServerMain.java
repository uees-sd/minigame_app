import controllers.ServerController;
import models.ServerModel;
import views.ServerView;

public class ServerMain {
    public static void main(String[] args) {
        ServerView view = new ServerView();
        ServerModel model = new ServerModel();
        ServerController controller = new ServerController(view, model);

        int port = 12345; // Port number to run the server
        controller.startServer(port);
    }
}
