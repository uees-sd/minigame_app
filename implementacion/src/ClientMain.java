import controllers.ClientController;
import models.ClientModel;
import views.ClientView;

public class ClientMain {
    public static void main(String[] args) {
        ClientView view = new ClientView();
        ClientModel model = new ClientModel();
        ClientController controller = new ClientController(view, model);

        String serverIP = "localhost"; // Replace with server IP
        int serverPort = 12345; // Replace with server port

        controller.startClient(serverIP, serverPort);
    }
}
