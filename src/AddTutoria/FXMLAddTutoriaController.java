/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddTutoria;

import AddAsignatura.Part6ComplementaryWindowController;
import accesoBD.AccesoBD;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Asignatura;
import modelo.Tutoria;
import vista.Part5ComplementaryWindowController;

/**
 * FXML Controller class
 *
 * @author Pablo
 */
public class FXMLAddTutoriaController implements Initializable {

    public ObservableList<Alumno> datosAlumnos = null;
    private ObservableList<Asignatura> datosAsignatura = null;

    @FXML
    private Button modificarAlumnos;
    @FXML
    private Button borrarAlumnos;
    @FXML
    private TableView<Alumno> alumnosTabla;
    @FXML
    private TextField textoAlumnos;
    @FXML
    private TextField textoAsignatura;
    @FXML
    private TableColumn<Alumno, String> nombreColumna;
    @FXML
    private TableColumn<Alumno, String> apellidosColumna;
    @FXML
    private TableColumn<Alumno, String> emailColumna;
    @FXML
    private Button modificarAsignatura;
    @FXML
    private Button borrarAsignatura;
    @FXML
    private TableView<Asignatura> asignaturasTabla;
    @FXML
    private TableColumn<Asignatura, String> codigoColumna;
    @FXML
    private TableColumn<Asignatura, String> descripcionColumna;
    @FXML
    private Spinner<Integer> horaseleccionar;
    @FXML
    private Spinner<Integer> minutosseleccionar;
    @FXML
    private Text tiemposlider;
    @FXML
    private Slider slider;

    @FXML
    private Button añadirAsignatura;
    @FXML
    private Button aceptartutoria;
    @FXML
    private Text textSelecAlumno;
    @FXML
    private Text textSelecAsignatura;
    private ObservableList<Tutoria> datosTutorias;
    @FXML
    private Text fechaSeleccionada;
    private LocalDate fecha;
    @FXML
    private TextField textoAnotaciones;

    public void initializeModel() {
        datosAlumnos = AccesoBD.getInstance().getTutorias().getAlumnosTutorizados();
        datosAlumnos.sort(Comparator.comparing(Alumno::getApellidos));
        datosAsignatura = AccesoBD.getInstance().getTutorias().getAsignaturas();
        datosTutorias = AccesoBD.getInstance().getTutorias().getTutoriasConcertadas();
    }

    public void initialize(URL url, ResourceBundle rb) {

        initializeModel();

        alumnosTabla.setItems(datosAlumnos);
        asignaturasTabla.setItems(datosAsignatura);
        SpinnerValueFactory<Integer> vlh = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 20, 8, 1);
        horaseleccionar.setValueFactory(vlh);
        SpinnerValueFactory<Integer> vlminutes = new SpinnerValueFactory.IntegerSpinnerValueFactory(00, 50, 00, 10);
        minutosseleccionar.setValueFactory(vlminutes);

        //LISTENERS
        //Parte 1
        alumnosTabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textoAlumnos.setDisable(false);
                modificarAlumnos.setDisable(false);
                borrarAlumnos.setDisable(false);
            } else {
                textoAlumnos.setDisable(true);
                modificarAlumnos.setDisable(true);
                borrarAlumnos.setDisable(true);
            }
        });
        textoAlumnos.setDisable(true);
        modificarAlumnos.setDisable(true);
        borrarAlumnos.setDisable(true);

        asignaturasTabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (index()) {
                modificarAsignatura.setDisable(true);
                borrarAsignatura.setDisable(true);
                textoAsignatura.setDisable(false);
            } else if (newSelection != null) {
                modificarAsignatura.setDisable(false);
                borrarAsignatura.setDisable(false);
                textoAsignatura.setDisable(false);
            } else {
                modificarAsignatura.setDisable(true);
                borrarAsignatura.setDisable(true);
                textoAsignatura.setDisable(true);
            }
        });
        modificarAsignatura.setDisable(true);
        borrarAsignatura.setDisable(true);
        textoAsignatura.setDisable(true);

        aceptartutoria.disableProperty().bind(slider.disableProperty());
        tiemposlider.textProperty().bind(slider.valueProperty().asString("%.0f"));

        nombreColumna.setCellValueFactory(fila -> fila.getValue().nombreProperty());
        apellidosColumna.setCellValueFactory(fila -> fila.getValue().apellidosProperty());
        emailColumna.setCellValueFactory(fila -> fila.getValue().emailProperty());

        codigoColumna.setCellValueFactory(fila -> fila.getValue().codigoProperty());
        descripcionColumna.setCellValueFactory(fila -> fila.getValue().descripcionProperty());

    }

    public void setFechaSeleccionada(LocalDate f) {
        fecha = f;
        fechaSeleccionada.setText(fecha.toString());
    }

    @FXML
    private void alumnosAñadirAccion(ActionEvent event) throws IOException {

        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vista/Part5ComplementaryWindow.fxml"));
        Parent root = miLoader.load();
        Scene scene = new Scene(root);
        Stage ventana3 = new Stage();
        ventana3.setTitle("Añadir Alumno");
        ventana3.setScene(scene);
        ventana3.setResizable(false);
        ventana3.initModality(Modality.APPLICATION_MODAL);
        ventana3.showAndWait();

        Alumno student = ((Part5ComplementaryWindowController) miLoader.getController()).getStudent();
        if (student != null) {
            datosAlumnos.add(student);
        }
        textoAlumnos.setText("");
        textoAsignatura.setText("");
        textSelecAlumno.setText("Seleccione un alumno");
        textSelecAsignatura.setText("");
        textoAsignatura.setDisable(true);
        añadirAsignatura.setDisable(true);
        modificarAsignatura.setDisable(true);
        borrarAsignatura.setDisable(true);
        asignaturasTabla.setDisable(true);
        horaseleccionar.setDisable(true);
        minutosseleccionar.setDisable(true);
        slider.setDisable(true);
        AccesoBD.getInstance().salvar();

    }

    @FXML
    private void modificarAlumnosAccion(ActionEvent event) throws IOException {

        if (!datosAlumnos.isEmpty() && alumnosTabla.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/vista/Part5ComplementaryWindow.fxml"));
            Parent root = miLoader.load();

            Scene scene = new Scene(root);
            Stage ventana3 = new Stage();
            ventana3.setTitle("Modificar Alumno");
            ventana3.setScene(scene);
            ventana3.setResizable(false);
            ventana3.initModality(Modality.APPLICATION_MODAL);

            ((Part5ComplementaryWindowController) miLoader.getController()).modifyStudent(alumnosTabla.getSelectionModel().getSelectedItem());
            ventana3.showAndWait();
        }
        textoAlumnos.setText("");
        textoAsignatura.setText("");
        textSelecAlumno.setText("Seleccione un alumno");
        textSelecAsignatura.setText("");
        textoAsignatura.setDisable(true);
        añadirAsignatura.setDisable(true);
        modificarAsignatura.setDisable(true);
        borrarAsignatura.setDisable(true);
        asignaturasTabla.setDisable(true);
        horaseleccionar.setDisable(true);
        minutosseleccionar.setDisable(true);
        slider.setDisable(true);
        AccesoBD.getInstance().salvar();
    }

    @FXML
    private void borrarAlumnosAccion(ActionEvent event) {
        if (!datosAlumnos.isEmpty() && alumnosTabla.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("AVISO");
            alert.setHeaderText("Estás borrando un alumno...");
            alert.setContentText("¿Seguro que quieres continuar?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                datosAlumnos.remove(alumnosTabla.getSelectionModel().getSelectedItem());
            }
        }
        textoAlumnos.setText("");
        textoAsignatura.setText("");
        textSelecAlumno.setText("Seleccione un alumno");
        textSelecAsignatura.setText("");
        textoAsignatura.setDisable(true);
        añadirAsignatura.setDisable(true);
        modificarAsignatura.setDisable(true);
        borrarAsignatura.setDisable(true);
        asignaturasTabla.setDisable(true);
        horaseleccionar.setDisable(true);
        minutosseleccionar.setDisable(true);
        slider.setDisable(true);
        AccesoBD.getInstance().salvar();

    }

    @FXML
    private void alumnoSeleccionado(MouseEvent event) {
        if (!"".equals(alumnosTabla.getSelectionModel().getSelectedItem()) && alumnosTabla.getSelectionModel().getSelectedItem() != null) {
            textSelecAlumno.setText("");
            textoAlumnos.setText(alumnosTabla.getSelectionModel().getSelectedItem().getNombre() + " "
                    + alumnosTabla.getSelectionModel().getSelectedItem().getApellidos() + " " + alumnosTabla.getSelectionModel().getSelectedItem().getEmail());
            textoAsignatura.setText("");
            textSelecAsignatura.setText("Selecciona una asignatura");
            añadirAsignatura.setDisable(false);
            modificarAsignatura.setDisable(true);
            borrarAsignatura.setDisable(true);
            textoAsignatura.setDisable(true);
            asignaturasTabla.setDisable(false);
            horaseleccionar.setDisable(true);
            minutosseleccionar.setDisable(true);
            slider.setDisable(true);
        } else {
            textoAlumnos.setText("");
            textSelecAlumno.setText("Seleccione un alumno");
            textoAsignatura.setText("");
            textSelecAsignatura.setText("");
            textoAsignatura.setDisable(true);
            añadirAsignatura.setDisable(true);
            modificarAsignatura.setDisable(true);
            borrarAsignatura.setDisable(true);
            asignaturasTabla.setDisable(true);
            horaseleccionar.setDisable(true);
            minutosseleccionar.setDisable(true);
            slider.setDisable(true);
        }

    }

    @FXML
    private void añadirAsignaturaAccion(ActionEvent event) throws IOException {
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/AddAsignatura/Part6ComplementaryWindow.fxml"));
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
            datosAsignatura.add(subject);

        }
        textoAsignatura.setText("");
        textSelecAsignatura.setText("Selecciona una asignatura");
        horaseleccionar.setDisable(true);
        minutosseleccionar.setDisable(true);
        slider.setDisable(true);
        AccesoBD.getInstance().salvar();
    }

    public boolean index() {
        if (asignaturasTabla.getSelectionModel().getSelectedIndex() == 0) {
            return true;
        }
        if (asignaturasTabla.getSelectionModel().getSelectedIndex() == 1) {
            return true;
        }
        return false;
    }

    @FXML
    private void modificarAsignaturaAccion(ActionEvent event) throws IOException {
        if (asignaturasTabla.getSelectionModel().getSelectedItem() != null && index() == false) {
            FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/AddAsignatura/Part6ComplementaryWindow.fxml"));
            Parent root = miLoader.load();

            Scene scene = new Scene(root);
            Stage ventana3 = new Stage();
            ventana3.setTitle("Modificar Asignatura");
            ventana3.setScene(scene);
            ventana3.setResizable(false);
            ventana3.initModality(Modality.APPLICATION_MODAL);
            ((Part6ComplementaryWindowController) miLoader.getController()).modifySubject(asignaturasTabla.getSelectionModel().getSelectedItem());
            ventana3.showAndWait();
            textoAsignatura.setText("");
            textSelecAsignatura.setText("Selecciona una asignatura");
            horaseleccionar.setDisable(true);
            minutosseleccionar.setDisable(true);
            slider.setDisable(true);
            AccesoBD.getInstance().salvar();
        }
    }

    @FXML
    private void borrarAsignaturaAccion(ActionEvent event) {
        if (asignaturasTabla.getSelectionModel().getSelectedItem() != null && index() == false) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("AVISO");
            alert.setHeaderText("Estás borrando un alumno...");
            alert.setContentText("¿Seguro que quieres continuar?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                datosAsignatura.remove(asignaturasTabla.getSelectionModel().getSelectedItem());
            }
            textoAsignatura.setText("");
            textSelecAsignatura.setText("Selecciona una asignatura");
            horaseleccionar.setDisable(true);
            minutosseleccionar.setDisable(true);
            slider.setDisable(true);
            AccesoBD.getInstance().salvar();

        }
    }

    @FXML
    private void asignaturaSeleccionada(MouseEvent event) {
        if (!"".equals(asignaturasTabla.getSelectionModel().getSelectedItem())) {
            textSelecAsignatura.setText("");
            textoAsignatura.setText(asignaturasTabla.getSelectionModel().getSelectedItem().getCodigo() + " " + asignaturasTabla.getSelectionModel().getSelectedItem().getDescripcion());
            horaseleccionar.setDisable(false);
            minutosseleccionar.setDisable(false);
            slider.setDisable(false);
        } else {
            textoAsignatura.setText("");
            textSelecAsignatura.setText("Selecciona una asignatura");
            horaseleccionar.setDisable(true);
            minutosseleccionar.setDisable(true);
            slider.setDisable(true);
        }
    }

    @FXML
    private void ComprobarSiSePuedeLaTutoria(ActionEvent event) {
        datosTutorias = AccesoBD.getInstance().getTutorias().getTutoriasConcertadas();

        LocalTime inicioNuevo = LocalTime.of(horaseleccionar.getValue(), minutosseleccionar.getValue());
        LocalTime finNuevo = inicioNuevo.plus(Duration.of((long) slider.getValue(), MINUTES));
        if(finNuevo.isAfter(LocalTime.of(20, 0))){
            tiempoOcupado();
            return;
        }
        System.out.println(inicioNuevo + " " + finNuevo);
        for (Tutoria tutoria : datosTutorias) {
            if (tutoria.getFecha() != null && tutoria.getFecha().compareTo(fecha) == 0
                    && tutoria.getEstado() != Tutoria.EstadoTutoria.ANULADA) {
                LocalTime inicio = tutoria.getInicio();
                LocalTime fin = inicio.plus(tutoria.getDuracion());
                if (((inicio.isAfter(inicioNuevo) || fin.isAfter(inicioNuevo))
                        && (inicio.isBefore(finNuevo) || fin.isBefore(finNuevo)))) {
                    tiempoOcupado();
                    return;
                }
            }
        }
        addTutoria();
    }

    private void addTutoria() {
        Tutoria nueva = new Tutoria();
        Asignatura asignatura = new Asignatura();

        asignatura.setCodigo(asignaturasTabla.getSelectionModel().getSelectedItem().getCodigo());
        asignatura.setDescripcion(asignaturasTabla.getSelectionModel().getSelectedItem().getDescripcion());
        nueva.setAsignatura(asignatura);
        nueva.setEstado(Tutoria.EstadoTutoria.PEDIDA);
        nueva.setInicio(LocalTime.of(horaseleccionar.getValue(), minutosseleccionar.getValue()));
        nueva.setFecha(fecha);
        nueva.setDuracion(Duration.of((long) slider.getValue(), MINUTES));
        if (textoAnotaciones.getText() != null) {
            nueva.setAnotaciones(textoAnotaciones.getText());
        } else {
            nueva.setAnotaciones("");
        }
        nueva.getAlumnos().add(alumnosTabla.getSelectionModel().getSelectedItem());

        datosTutorias.add(nueva);
        AccesoBD.getInstance().salvar();
        ((Stage) textoAlumnos.getScene().getWindow()).close();
    }

    private void tiempoOcupado() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Tiempo ocupado por otras tutorias");
        alert.setHeaderText("Tiempo ocupado por otras tutorias");
        alert.setContentText("Cambia duracion o hora de inicio para continuar");
        alert.showAndWait();
    }

    @FXML
    private void salirSinGuardarTutoria(ActionEvent event) {
        ((Stage) textoAlumnos.getScene().getWindow()).close();
    }
}
