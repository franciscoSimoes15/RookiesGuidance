package pt.isec.gps.rookiesguidance.gui.views.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pt.isec.gps.rookiesguidance.bd.ConnDB;
import pt.isec.gps.rookiesguidance.gui.views.View;
import pt.isec.gps.rookiesguidance.gui.views.ViewSwitcher;
import pt.isec.gps.rookiesguidance.utils.ToastMessage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InscreveEventoController implements Initializable {
    ConnDB connDB;
    @FXML
    private ImageView homePageIcon;

    @FXML
    private CheckBox yesButton;
    @FXML
    private CheckBox noButton;
    @FXML
    private Button submeterButton;
    @FXML
    private VBox presencasVBox;
    int idEvento;
    @FXML
    void onIconPressed() {
        if(!LoginController.isGestor())
            ViewSwitcher.switchTo(View.HOMEPAGE);
        else
            ViewSwitcher.switchTo(View.HOMEPAGE_GESTORES);
    }
    @FXML
    void onEventosPressed() {
        if(!LoginController.isGestor)
            ViewSwitcher.switchTo(View.EVENTOS_ESTUDANTE);
        else
            ViewSwitcher.switchTo(View.EVENTOS);
    }

    @FXML
    void onInformacoesPressed() {
        if(!LoginController.isGestor)
            ViewSwitcher.switchTo(View.INFORMACOES_ESTUDANTE);
        else
            ViewSwitcher.switchTo(View.INFORMACOES);
    }

    @FXML
    void onPerfilPressed() {
        ViewSwitcher.switchTo(View.PERFIL);
    }

    @FXML
    void onPerguntasPressed() {
        if(!LoginController.isGestor)
            ViewSwitcher.switchTo(View.PERGUNTAS_ESTUDANTE);
        else
            ViewSwitcher.switchTo(View.PERGUNTAS);
    }

    @FXML
    void onYesSelected() {
        if(yesButton.isSelected()) {
            noButton.setSelected(false);
            submeterButton.setDisable(false);
        }
        else if(!noButton.isSelected())
            submeterButton.setDisable(true);
    }

    @FXML
    void onNoSelected() {
        if(noButton.isSelected()) {
            yesButton.setSelected(false);
            submeterButton.setDisable(false);
        }
        else if(!yesButton.isSelected())
            submeterButton.setDisable(true);
    }

    @FXML
    void onSubmeterPressed() throws SQLException {
        if(yesButton.isSelected()) {
            if (!connDB.inscreveEmEvento(LoginController.getNumero(), idEvento))
                ToastMessage.show(ViewSwitcher.getScene().getWindow(), "Impossivel inscrever no evento");
        } else {
            if (!connDB.desinscreveEmEvento(LoginController.getNumero(), idEvento))
                ToastMessage.show(ViewSwitcher.getScene().getWindow(), "Impossivel desinscrever no evento");

        }

        presencasVBox.getChildren().clear();
        try {
            ArrayList<String> eventosUtilizadores;
            ArrayList<Text> eventosUtilizadoresText = new ArrayList<>();
            eventosUtilizadores = connDB.getUtilizadoresEvento(idEvento);
            for (int i = 0; i < eventosUtilizadores.size(); i++) {
                Text t = new Text();
                t.setText(eventosUtilizadores.get(i));
                eventosUtilizadoresText.add(t);
            }
            presencasVBox.getChildren().addAll(eventosUtilizadoresText);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onTerminarSessaoPressed() throws SQLException {

        if(connDB.logout(LoginController.getNumero())){
            ToastMessage.show(ViewSwitcher.getScene().getWindow(), "Sessão terminada com sucesso");
            ViewSwitcher.switchTo(View.LOGIN);
        }else
            ToastMessage.show(ViewSwitcher.getScene().getWindow(), "Erro ao terminar sessão");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            connDB = new ConnDB();
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
        if(LoginController.isGestor)
            idEvento = EventosController.getIdEvento();
        else
            idEvento = EventosEstudantesController.getIdEvento();

        submeterButton.setDisable(true);

        try {
            ArrayList<String> eventosUtilizadores;
            ArrayList<Text> eventosUtilizadoresText = new ArrayList<>();
            eventosUtilizadores = connDB.getUtilizadoresEvento(idEvento);
            for (int i = 0; i < eventosUtilizadores.size(); i++) {
                Text t = new Text();
                t.setText(eventosUtilizadores.get(i));
                eventosUtilizadoresText.add(t);
            }
            presencasVBox.getChildren().addAll(eventosUtilizadoresText);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
