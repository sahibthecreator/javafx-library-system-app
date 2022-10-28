package com.assignment.javaendassignment;

import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Database {
    private ArrayList<User> users;
    private ArrayList<Member> members;
    private ArrayList<Item> items;

    public Database() {
        members = new ArrayList<Member>();
        items = new ArrayList<Item>();
        users = new ArrayList<User>();
        load();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void clearDb() {
        try {
            FileOutputStream fileOut = new FileOutputStream(new File("db.dat"));
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save(ObservableList<Member> members, ObservableList<Item> items) {
        clearDb();
        try {
            FileOutputStream fileOut = new FileOutputStream(new File("db.dat"));
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            for (User user : users) {
                objectOut.writeObject(user);
            }
            for (Member member : members) {
                objectOut.writeObject(member);
            }
            for (Item item : items) {
                objectOut.writeObject(item);
            }
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void load() {
        try {
            FileInputStream fi = new FileInputStream(new File("db.dat"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            while (true) {
                try {
                    Object obj = oi.readObject();
                    if (obj instanceof Member) {
                        members.add(((Member) obj));
                    } else if (obj instanceof Item) {
                        items.add(((Item) obj));
                    } else if (obj instanceof User) {
                        users.add(((User) obj));
                    } else {
                        throw new Exception("Couldn't read object from file");
                    }
                } catch (EOFException eof) {
                    break;
                }
            }
            oi.close();
            fi.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
