/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parte6;

import accesoBD.AccesoBD;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import static javafx.scene.paint.Color.BLACK;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Asignatura;

/**
 * FXML Controller class
 *
 * @author Pablo
 */
public class Part6AddOrDeleteSubjectController implements Initializable {

    private ObservableList<Asignatura> datosSubject = null;
    @FXML
    private Button addButton;
    @FXML
    private Button modButton;
    @FXML
    private Button delButton;
    @FXML
    private Label verificadorSub;
    @FXML
    private TableView<Asignatura> subjectsTable;
    @FXML
    private TableColumn<Asignatura, String> CodigoColumn;
    @FXML
    private TableColumn<Asignatura, String> descripcionColumna;

    private Asignatura tfg = new Asignatura("TFG", "Trabajo Fin de Grado");
    private Asignatura tfm = new Asignatura("TFM", "Trabajo Fin de Máster");

    // && subjectsTable.getSelectionModel().getSelectedItem().equals(tfg) && subjectsTable.getSelectionModel().getSelectedItem().equals(tfm) SUBSTITUTES THIS
    public void initializeModel() {
        datosSubject = AccesoBD.getInstance().getTutorias().getAsignaturas();

        if (datosSubject.isEmpty()) {
            datosSubject.add(tfg);
            datosSubject.add(tfm);
        }

        AccesoBD.getInstance().salvar();
    }

    //&& subjectsTable.getSelectionModel().getSelectedItem().equals(tfg) 
    public boolean index() {
        boolean res = false;

        if (subjectsTable.getSelectionModel().getSelectedIndex() == 0) {
            res = true;
        }
        if (subjectsTable.getSelectionModel().getSelectedIndex() == 1) {
            res = true;
        }

        return res;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initializeModel();
        subjectsTable.setItems(datosSubject);
        verificadorSub.textFillProperty().set(BLACK);

        modButton.disableProperty().bind(Bindings.not(subjectsTable.focusedProperty()));
        delButton.disableProperty().bind(Bindings.not(subjectsTable.focusedProperty()));
        modButton.disableProperty().bind(Bindings.size(datosSubject).isEqualTo(2));
        delButton.disableProperty().bind(Bindings.size(datosSubject).isEqualTo(2));

        CodigoColumn.setCellValueFactory(fila -> fila.getValue().codigoProperty());
        descripcionColumna.setCellValueFactory(fila -> fila.getValue().descripcionProperty());

    }

    @FXML
    private void AddSubject(ActionEvent event) throws IOException {
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/parte6/Part6ComplementaryWindow.fxml"));
        Parent root = miLoader.load();

        Scene scene = new Scene(root);
        Stage ventana3 = new Stage();
        ventana3.setTitle("Añadir Asignatura");
        ventana3.setScene(scene);
        ventana3.setResizable(false);
        ventana3.initModality(Modality.APPLICATION_MODAL);
        ventana3.showAndWait();

        Asignatura subject = ((Part6ComplementaryWindowController) miLoader.getController()).getSubject();
        if (subject != null) {
            datosSubject.add(subject);
            verificadorSub.setText("Asignatura Añadida Correctamente");
        } else {
            verificadorSub.setText("");
        }

        AccesoBD.getInstance().salvar();
    }

    @FXML
    private void modifySubject(ActionEvent event) throws IOException {

        if (subjectsTable.getSelectionModel().getSelectedItem() != null && index() == false) {
            FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/parte6/Part6ComplementaryWindow.fxml"));
            Parent root = miLoader.load();

            Scene scene = new Scene(root);
            Stage ventana3 = new Stage();
            ventana3.setTitle("Modificar Asignatura");
            ventana3.setScene(scene);
            ventana3.setResizable(false);
            ventana3.initModality(Modality.APPLICATION_MODAL);
            ((Part6ComplementaryWindowController) miLoader.getController()).modifySubject(subjectsTable.getSelectionModel().getSelectedItem());
            ventana3.showAndWait();

            if (((Part6ComplementaryWindowController) miLoader.getController()).getValue() == true) {
                verificadorSub.setText("Asignatura Modificada Correctamente");
            } else {
                verificadorSub.setText("");
            }
            AccesoBD.getInstance().salvar();
        }
    }

    @FXML
    private void deleteSubject(ActionEvent event) throws IOException {
        if (subjectsTable.getSelectionModel().getSelectedItem() != null && index() == false) {
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("AVISO");
            alert.setHeaderText("Estás borrando un alumno...");
            alert.setContentText("¿Seguro que quieres continuar?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                datosSubject.remove(subjectsTable.getSelectionModel().getSelectedItem());
                verificadorSub.textFillProperty().set(BLACK);
                verificadorSub.setText("Asignatura Borrada Correctamente");
                AccesoBD.getInstance().salvar();

            } else {
                verificadorSub.setText("");
            }
        }
    }

}