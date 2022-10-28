package com.assignment.javaendassignment;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.DAYS;

public class MainScreenController implements Initializable {

    private Database db;
    private ObservableList<Member> members;
    private ObservableList<Item> items;
    @FXML
    private TableView<Member> tableViewMembers;
    @FXML
    private TableColumn<Member, Integer> colId;
    @FXML
    private TableColumn<Member, String> colFirstName, colLastName, colBirthDate;
    @FXML
    private TableView<Item> tableViewItems;
    @FXML
    private TableColumn<Item, Integer> itemColId;
    @FXML
    private TableColumn<Item, String> itemColAvailable, itemColTitle, itemColAuthor;
    @FXML
    private VBox addEditMemberPanel, allMembersPanel, addEditItemPanel, allItemsPanel;
    @FXML
    private TextField searchItemId, searchItemTitle, searchItemAuthor, searchMemberId, searchMemberFirstName, searchMemberLastName, searchMemberBirth, itemCodeLending, memberIdLending, itemCodeReceiving, firstNameField, lastNameField, authorField, titleField;
    @FXML
    private Label lendItemErrorText, receiveItemErrorText, memberPanelLabel, itemPanelLabel, addEditMemberErrorText, addEditItemErrorText, itemSelectErrorText, memberSelectErrorText;
    @FXML
    private DatePicker birthDateField;
    @FXML
    private Button saveItemButton, saveMemberButton;
    @FXML
    private Text welcomeText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            if (db != null) {
                members = FXCollections.observableArrayList(db.getMembers());
                items = FXCollections.observableArrayList(db.getItems());
            } else {
                throw new RuntimeException("Database is null");
            }
            //Members Table
            colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
            colFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
            colLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
            colBirthDate.setCellValueFactory(new PropertyValueFactory<>("BirthDate"));
            tableViewMembers.setItems(members);
            //Items Table
            itemColId.setCellValueFactory(new PropertyValueFactory<>("Id"));
            itemColAvailable.setCellValueFactory(new PropertyValueFactory<>("Available"));
            itemColTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
            itemColAuthor.setCellValueFactory(new PropertyValueFactory<>("Author"));
            tableViewItems.setItems(items);

            searchMemberId.textProperty().addListener((observable, oldValue, newValue) -> {
                searchMember(newValue, "Id");
            });
            searchMemberFirstName.textProperty().addListener((observable, oldValue, newValue) -> {
                searchMember(newValue, "FirstName");
            });
            searchMemberLastName.textProperty().addListener((observable, oldValue, newValue) -> {
                searchMember(newValue, "LastName");
            });
            searchMemberBirth.textProperty().addListener((observable, oldValue, newValue) -> {
                searchMember(newValue, "BirthDate");
            });
            searchItemId.textProperty().addListener((observable, oldValue, newValue) -> {
                searchItem(newValue, "Id");
            });
            searchItemTitle.textProperty().addListener((observable, oldValue, newValue) -> {
                searchItem(newValue, "Title");
            });
            searchItemAuthor.textProperty().addListener((observable, oldValue, newValue) -> {
                searchItem(newValue, "Author");
            });
        });
    }

    private void searchMember(String searchValue, String elementProperty) {
        if (searchValue != "") {
            var table = tableViewMembers.getItems().stream();
            switch (elementProperty) {
                case ("Id"):
                    table = table.filter(i -> i.getId() == Integer.parseInt(searchValue));
                    break;
                case ("FirstName"):
                    table = table.filter(i -> i.getFirstName().toLowerCase().contains(searchValue.toLowerCase()));
                    break;
                case ("LastName"):
                    table = table.filter(i -> i.getLastName().toLowerCase().contains(searchValue.toLowerCase()));
                    break;
                case ("BirthDate"):
                    table = table.filter(i -> i.getBirthDate().toString().toLowerCase().contains(searchValue.toLowerCase()));
                    break;
            }
            table.findAny().ifPresent(i -> {
                tableViewMembers.getSelectionModel().select(i);
                tableViewMembers.scrollTo(i);
            });
        }
    }

    private void searchItem(String searchValue, String elementProperty) {
        if (searchValue != "") {
            var table = tableViewItems.getItems().stream();
            switch (elementProperty) {
                case ("Id"):
                    table = table.filter(i -> i.getId() == Integer.parseInt(searchValue));
                    break;
                case ("Title"):
                    table = table.filter(i -> i.getTitle().toLowerCase().contains(searchValue.toLowerCase()));
                    break;
                case ("Author"):
                    table = table.filter(i -> i.getAuthor().toLowerCase().contains(searchValue.toLowerCase()));
                    break;
            }
            table.findAny().ifPresent(i -> {
                tableViewItems.getSelectionModel().select(i);
                tableViewItems.scrollTo(i);
            });
        }
    }

    @FXML
    protected void lendItem() {
        if (itemCodeLending.getText() != "" && memberIdLending.getText() != "") {
            int itemIndex = getItemIndexById(Integer.parseInt(itemCodeLending.getText()));
            int memberIndex = getMemberIndexById(Integer.parseInt(memberIdLending.getText()));
            if (itemIndex != -1) {
                if (memberIndex != -1) {
                    if (items.get(itemIndex).isAvailableBoolean()) {
                        items.get(itemIndex).setAvailable(false);
                        items.get(itemIndex).setLendingDate(LocalDate.now());
                        tableViewItems.refresh();
                        lendItemErrorText.setText("Item '" + items.get(itemIndex).getTitle() + "' has been lent successfully on " + LocalDate.now());
                    } else {
                        lendItemErrorText.setText("Item is not available");
                    }
                } else {
                    lendItemErrorText.setText("Member code is incorrect");
                }
            } else {
                lendItemErrorText.setText("Item code is incorrect");
            }
        } else {
            lendItemErrorText.setText("Please fill all the fields");
        }
    }

    @FXML
    protected void receiveItem() {
        if (itemCodeReceiving.getText() != "") {
            int itemIndex = getItemIndexById(Integer.parseInt(itemCodeReceiving.getText()));
            if (itemIndex != -1) {
                if (!items.get(itemIndex).isAvailableBoolean()) {
                    LocalDate currentDate = LocalDate.now();
                    LocalDate currentDateMinus3Weeks = currentDate.minusWeeks(3);
                    if (items.get(itemIndex).getLendingDate().isAfter(currentDateMinus3Weeks)) {
                        receiveItemErrorText.setText("Item '" + items.get(itemIndex).getTitle() + "' has been received successfully!");
                    } else {
                        receiveItemErrorText.setText("Item is too late. " + DAYS.between(items.get(itemIndex).getLendingDate(), currentDateMinus3Weeks) + " days late!");
                    }
                    items.get(itemIndex).setAvailable(true);
                    items.get(itemIndex).setLendingDate(null);
                    tableViewItems.refresh();
                } else {
                    receiveItemErrorText.setText("Item is not lent");
                }
            } else {
                receiveItemErrorText.setText("Item code is incorrect");
            }
        } else {
            receiveItemErrorText.setText("Please fill the field");
        }
    }


    @FXML
    protected void openAddMemberPanel() {
        memberPanelLabel.setText("Add Member");
        saveMemberButton.setText("Add Member");
        firstNameField.setText("");
        lastNameField.setText("");
        birthDateField.setValue(null);
        togglePanels(addEditMemberPanel, allMembersPanel);
    }

    @FXML
    protected void openEditMemberPanel() {
        if (!tableViewMembers.getSelectionModel().isEmpty()) {              //if user selected Member
            Member selectedMember = tableViewMembers.getSelectionModel().getSelectedItem();
            memberPanelLabel.setText("Edit Item");
            saveMemberButton.setText("Save Member");
            firstNameField.setText(selectedMember.getFirstName());
            lastNameField.setText(selectedMember.getLastName());
            birthDateField.setValue(selectedMember.getBirthDateLocalDate());
            togglePanels(addEditMemberPanel, allMembersPanel);
        } else {
            memberSelectErrorText.setText("Please select Member to edit");
        }
    }

    @FXML
    protected void deleteMemberBtn() {
        if (!tableViewMembers.getSelectionModel().isEmpty()) {              //if user selected an item
            members.remove(tableViewMembers.getSelectionModel().getSelectedItem());
            memberSelectErrorText.setText("");
        } else {
            memberSelectErrorText.setText("Please select Member to delete");
        }
    }

    @FXML
    protected void saveMember() {
        if (!firstNameField.getText().isEmpty() && !lastNameField.getText().isEmpty() && birthDateField.getValue() != null) {
            if (memberPanelLabel.getText().equals("Add Member")) {      //Add Member
                members.add(new Member(firstNameField.getText(), lastNameField.getText(), birthDateField.getValue(), members));
            } else { //Edit Member
                Member selectedMember = tableViewMembers.getSelectionModel().getSelectedItem();
                selectedMember.setFirstName(firstNameField.getText());
                selectedMember.setLastName(lastNameField.getText());
                selectedMember.setBirthDate(birthDateField.getValue());
            }
            addEditMemberErrorText.setText("");
            tableViewMembers.refresh();
            togglePanels(allMembersPanel, addEditMemberPanel);
        } else {
            addEditMemberErrorText.setText("Please fill all fields");
        }
    }

    @FXML
    protected void cancelAddMemberBtn() {
        addEditMemberErrorText.setText("");
        togglePanels(allMembersPanel, addEditMemberPanel);
    }

    @FXML
    protected void openAddItemPanel() {
        itemPanelLabel.setText("Add Item");
        saveItemButton.setText("Add Item");
        authorField.setText("");
        titleField.setText("");
        togglePanels(addEditItemPanel, allItemsPanel);
    }

    @FXML
    protected void openEditItemPanel() {
        if (!tableViewItems.getSelectionModel().isEmpty()) {              //if user selected item
            Item selectedItem = tableViewItems.getSelectionModel().getSelectedItem();
            itemPanelLabel.setText("Edit Item");
            saveItemButton.setText("Save Item");
            authorField.setText(selectedItem.getAuthor());
            titleField.setText(selectedItem.getTitle());
            togglePanels(addEditItemPanel, allItemsPanel);
        } else {
            itemSelectErrorText.setText("Please select Item to edit");
        }
    }

    @FXML
    protected void cancelItemBtn() {
        togglePanels(allItemsPanel, addEditItemPanel);
    }

    @FXML
    protected void saveItem() {
        if (!titleField.getText().isEmpty() && !authorField.getText().isEmpty()) { //if fields are empty
            if (itemPanelLabel.getText().equals("Add Item")) {//Add item
                items.add(new Item(titleField.getText(), authorField.getText(), items));
                addEditItemErrorText.setText("");
            } else {    //Edit item
                Item selectedItem = tableViewItems.getSelectionModel().getSelectedItem();
                selectedItem.setAuthor(authorField.getText());
                selectedItem.setTitle(titleField.getText());
                tableViewItems.refresh();
            }
            togglePanels(allItemsPanel, addEditItemPanel);
        } else {
            addEditItemErrorText.setText("Please fill all fields");
        }
    }

    @FXML
    protected void deleteItem() {
        if (!tableViewItems.getSelectionModel().isEmpty()) {              //if user selected an item
            items.remove(tableViewItems.getSelectionModel().getSelectedItem());
            itemSelectErrorText.setText("");
        } else {
            itemSelectErrorText.setText("Please select Item to delete");
        }
    }

    private void togglePanels(VBox panelOn, VBox panelOff) {
        panelOn.setVisible(true);
        panelOff.setVisible(false);
    }

    private int getItemIndexById(int id) {
        Item item = items.stream().filter(i -> id == i.getId()).findFirst().orElse(null);
        if (item != null) {
            return items.indexOf(item);
        } else {
            return -1;
        }
    }

    private int getMemberIndexById(int id) {
        Member member = members.stream().filter(u -> id == u.getId()).findFirst().orElse(null);
        if (member != null) {
            return members.indexOf(member);
        } else {
            return -1;
        }
    }

    public void setDb(Database database) {
        System.out.println(database);
        this.db = database;
    }

    public void saveOnClose() {
        db.save(members, items);
    }

    public void setWelcomeText(User user) {
        welcomeText.setText("Welcome, " + user.getUsername());
    }
}

