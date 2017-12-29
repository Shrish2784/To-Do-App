import data.TodoItem;
import data.TodoItemData;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

public class Controller {

    @FXML
    private ListView<TodoItem> todoTitleList;
    @FXML
    private TextArea textArea;
    @FXML
    private Label DeadLine;
    @FXML
    private BorderPane mainWindowBorderpane;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private Button trashOpener;

    @FXML
    public void initialize() {

        contextMenu= new ContextMenu();
        MenuItem delete=new MenuItem("Delete");
        MenuItem perDelete=new MenuItem ("Permanent Delete");

        perDelete.setOnAction(
                (event) ->{
                    TodoItem item=todoTitleList.getSelectionModel().getSelectedItem();
                    perDeleteItem(item);
                }
        );
////                new EventHandler<ActionEvent>() {
////            @Override
////            public void handle(ActionEvent event) {
////                TodoItem item=todoTitleList.getSelectionModel().getSelectedItem();
////                perDeleteItem(item);
////            }
////        });
        delete.setOnAction(
                (event) -> {
                    TodoItem item=todoTitleList.getSelectionModel().getSelectedItem();
                    deleteItem(item);
                }
        );
////                new EventHandler<ActionEvent>() {
////            @Override
////            public void handle(ActionEvent event) {
////                TodoItem item=todoTitleList.getSelectionModel().getSelectedItem();
////                deleteItem(item);
////            }
////        });
        contextMenu.getItems().setAll(delete, perDelete);

        todoTitleList.getSelectionModel().selectedItemProperty().addListener(

                (observable, oldValue, newValue) -> {
                    if (newValue != null) {

                        TodoItem item = todoTitleList.getSelectionModel().getSelectedItem();
                        textArea.setText(item.getDetails());
                        DeadLine.setText(item.getDueDate().toString());
                    }
                }

        );
//            @Override
//            public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
//                if (newValue != null) {
//
//                    TodoItem item = todoTitleList.getSelectionModel().getSelectedItem();
//
//                    textArea.setText(item.getDetails());
//                    DeadLine.setText(item.getDueDate().toString());
//                }
//            }
//        });
        SortedList sortedList=new SortedList(TodoItemData.getInstance().getTodoItems(), new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                return o1.getDueDate().compareTo(o2.getDueDate());
            }
        });

        todoTitleList.setItems(sortedList);
        todoTitleList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoTitleList.getSelectionModel().selectFirst();

        todoTitleList.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {

                ListCell<TodoItem> cell=new ListCell<TodoItem>(){
                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty){
                            setText(null);
                        } else {
                            setText(item.getTitle());
                            if (item.getDueDate().equals(LocalDate.now())){
                                setTextFill(Color.RED);
                            } else if (item.getDueDate().isBefore(LocalDate.now())){
                                setTextFill(Color.DIMGREY);
                            } else if (item.getDueDate().isAfter(LocalDate.now())){
                                setTextFill(Color.SLATEBLUE);
                            }
                        }
                    }
                };

                cell.setContextMenu(contextMenu);

//                cell.emptyProperty().addListener(
//                        (obs,wasEmpty,isNowEmpty)->{
//                            if (isNowEmpty){
//                                cell.setContextMenu(null);
//                            } else {
//                                cell.setContextMenu(contextMenu);
//                            }
//                        }
//                );
                return  cell;
            }
        });
    }


    public void addToDoItemDialog(){
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(mainWindowBorderpane.getScene().getWindow());
        dialog.setTitle("Add an item.");
        dialog.setHeaderText("Add a new ToDo Item.");
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AddToDoItemDialog.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result=dialog.showAndWait();
        if (result.isPresent()&&result.get()==ButtonType.OK){
            ToDoItemDialogController controller=fxmlLoader.getController();
            TodoItem item=controller.addtodoitem();

            if (item!=null)
                todoTitleList.getSelectionModel().select(item);
        }
    }

    public void trashWindow(){
//        Dialog<ButtonType> dialog=new Dialog<>();
//        dialog.initOwner(mainWindowBorderpane.getScene().getWindow());
//        FXMLLoader fxmlLoader=new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("Trash.fxml"));
//        try{
//            dialog.getDialogPane().setContent(fxmlLoader.load());
//        } catch (IOException e){
//            System.out.println("CANNOT");
//        }
//
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//
//        Optional<ButtonType> result=dialog.showAndWait();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("Trash.fxml"));
            Stage stage=(Stage)trashOpener.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e){
            System.out.println("Trash didn't open.");
        }
//        FXMLLoader fxmlLoader=new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("Trash.fxml"));
//        try{
//            Parent root=(Parent)(fxmlLoader.load());
//            Stage stage=new Stage();
//            Scene scene =new Scene(root);
//            stage.setScene(scene);
//            stage.setTitle("TrashBox");
//            stage.initOwner(mainWindowBorderpane.getScene().getWindow());
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.show();
//        } catch (IOException e){
//            System.out.println(e.getMessage());
//        }
    }

    public void deleteItem(TodoItem item){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Item.");
        alert.setHeaderText("Are you sure to delete this item:-\n"+item.getTitle());
        alert.setContentText("This item will be moved to trash box after deletion." +
                "\nPress OK to continue or CANCEL to abort.");

        Optional<ButtonType> result=alert.showAndWait();

        if ((result.isPresent())&&(result.get().equals(ButtonType.OK))) {
            textArea.setText("");
            DeadLine.setText("");
            TodoItemData.getInstance().deleteTodoItem(item);
        } else if (result.isPresent()&&result.equals(ButtonType.CANCEL)){
            alert.close();
        }
    }

    public void perDeleteItem(TodoItem item){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Permanently Delete Item");
        alert.setHeaderText("Are you sure to delete this item permanently:-\n"+item.getTitle());

        Optional<ButtonType> result= alert.showAndWait();
        if ((result.isPresent())&&(result.get().equals(ButtonType.OK))){
            textArea.setText("");
            DeadLine.setText("");
            TodoItemData.getInstance().perDeleteTodoItem(item);
        }  else if (result.isPresent()&&result.equals(ButtonType.CANCEL)){
            alert.close();
        }
    }

    public void handleKeyPressed (KeyEvent event){
        if (event.getCode().equals(KeyCode.DELETE)){
            TodoItem item=todoTitleList.getSelectionModel().getSelectedItem();
            deleteItem(item);
        }
    }

    public void exitTheApp(){
        System.exit(0);
        try{
        TodoItemData.getInstance().storeDataToFile();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}