import data.TodoItem;
import data.TodoItemData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;


public class TrashController {
    @FXML
    private ListView<TodoItem> trashTodoTitleList;
    @FXML
    private TextArea textArea;
    @FXML
    private Label Deadline;
    @FXML
    private BorderPane trashPane;
    @FXML
    private Button trashExitButton;
    @FXML
    private ContextMenu contextMenu;

    @FXML
    public void initialize(){

        contextMenu=new ContextMenu();
        MenuItem delete =new MenuItem("Delete");
        MenuItem restore =new MenuItem("Restore");

        restore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TodoItem item=trashTodoTitleList.getSelectionModel().getSelectedItem();
                restoreItem(item);
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TodoItem item=trashTodoTitleList.getSelectionModel().getSelectedItem();
                deleteFromTrash(item);
            }
        });

        contextMenu.getItems().addAll(restore,delete);

        trashTodoTitleList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
                if (newValue!=null){
                    TodoItem item=trashTodoTitleList.getSelectionModel().getSelectedItem();
                    textArea.setText(item.getDetails());
                    Deadline.setText(item.getDueDate().toString());
                }
            }
        });

        SortedList<TodoItem> sortedList=new SortedList<TodoItem>(TodoItemData.getInstance().getTrashTodoItems(), new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                return o1.getDueDate().compareTo(o2.getDueDate());
            }
        });

        trashTodoTitleList.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
                ListCell<TodoItem> cell = new ListCell<TodoItem>() {
                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {

                            setText(item.getTitle());
                            if (item.getDueDate().equals(LocalDate.now())) {
                                setTextFill(Color.RED);
                            } else if (item.getDueDate().isBefore(LocalDate.now())) {
                                setTextFill(Color.DIMGREY);
                            } else if (item.getDueDate().isAfter(LocalDate.now())) {
                                setTextFill(Color.SLATEBLUE);
                            }
                        }
                    }

                };

                cell.emptyProperty().addListener(
                        (obs,wasEmpty,isNowEmpty) -> {
                            if (isNowEmpty){
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(contextMenu);
                            }
                        }
                );

                return cell;
            }
        });

        trashTodoTitleList.setItems(sortedList);
        trashTodoTitleList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        trashTodoTitleList.getSelectionModel().selectFirst();
    }

    @FXML
    public void deleteFromTrash(TodoItem item){

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Item.");
        alert.setHeaderText("Are you sure you want to delete \""+item.getTitle()+"\" permanently?");
        alert.setContentText("Deleting from trash box will permanently delete the item.\nPress OK to continue or CANCEL to abort.");

        Optional<ButtonType> result=alert.showAndWait();
        if (result.isPresent()&&result.get().equals(ButtonType.OK)){
            textArea.setText("");
            Deadline.setText("");
            TodoItemData.getInstance().deleteTrashTodoItem(item);
        } else if (result.isPresent()&&result.equals(ButtonType.CANCEL)){
            alert.close();
        }
    }

    @FXML
    public void restoreItem(TodoItem item){

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Restore Item.");
        alert.setHeaderText("Are you sure you want to restore \""+item.getTitle()+"\".?");
        alert.setContentText("Restorig from trash box will move the item to Main.\nPress OK to continue or CANCEL to abort.");

        Optional<ButtonType> result=alert.showAndWait();

        if (result.isPresent()&&result.get().equals(ButtonType.OK)) {
            textArea.setText("");
            Deadline.setText("");
            TodoItemData.getInstance().restoreTrashTodoItem(item);
        } else if (result.isPresent()&&result.get().equals(ButtonType.CANCEL)){
            alert.close();
        }
    }

    @FXML
    public void exitWindow(){
        try{
            Parent root= FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
            Stage stage=(Stage)trashExitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e){
            System.out.println("Main window after trash didn't open.");
            e.printStackTrace();
        }
    }

}
