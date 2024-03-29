package pt.isec.gps.rookiesguidance.gui.views.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pt.isec.gps.rookiesguidance.bd.ConnDB;
import pt.isec.gps.rookiesguidance.gui.views.View;
import pt.isec.gps.rookiesguidance.gui.views.ViewSwitcher;
import pt.isec.gps.rookiesguidance.utils.ToastMessage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistoController implements Initializable {
    ConnDB connDB;
    @FXML
    private TextField nomeText;

    @FXML
    private ChoiceBox dropdownCurso;

    @FXML
    private TextField emailText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private PasswordField confirmpasswordText;

    @FXML
    private Button registarButton;
    @FXML
    void buttonPressed()  throws SQLException {
        if(dropdownCurso.getSelectionModel().isEmpty() || nomeText.getText().isEmpty() || emailText.getText().isEmpty() || passwordText.getText().isEmpty() || confirmpasswordText.getText().isEmpty()) {
            ToastMessage.show(ViewSwitcher.getScene().getWindow(), "Preencha os campos corretamente");
            return;
        }

        int count = 0;
        for (int i = 0, len = emailText.getText().length(); i < len; i++) {
            if (Character.isDigit(emailText.getText().charAt(i))) {
                count++;
            }
        }

        if(!emailText.getText().startsWith("a") || emailText.getText().length() != 19 || !emailText.getText().endsWith("@isec.pt") || count != 10) {
            ToastMessage.show(ViewSwitcher.getScene().getWindow(), "Preencha os campos corretamente");
            return;
        }



        String curso = (String) dropdownCurso.getSelectionModel().getSelectedItem();
        String nome = nomeText.getText();
        String email = emailText.getText();
        String password = passwordText.getText();
        String confirmaPassword = confirmpasswordText.getText();
        String numero = email.substring(1, 11);
        if(password.equals(confirmaPassword)){
            if(connDB.registaNovoUtilizador(Integer.parseInt(numero),nome,curso,email,password)){
                ToastMessage.show(ViewSwitcher.getScene().getWindow(), "Utilizador registado com sucesso!");
                ViewSwitcher.switchTo(View.LOGIN);
            } else {
                ToastMessage.show(ViewSwitcher.getScene().getWindow(), "Insira uma palavra-passe válida!");
            }
        } else {
            ToastMessage.show(ViewSwitcher.getScene().getWindow(), "Insira a palavra-passe corretamente!");
        }
    }

    @FXML
    void onButtonLogin(){
        ViewSwitcher.switchTo(View.LOGIN);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            connDB = new ConnDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dropdownCurso.getItems().addAll("Engenharia Informática","Engenharia Mecânica","Engenharia Eletrotécnica");
        nomeText.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 30 ? change : null));
        emailText.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 20 ? change : null));
        passwordText.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 15 ? change : null));
        confirmpasswordText.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 15 ? change : null));
    }
}
