package com.example.w7_p1;

public class Name {
    private int id;
    private String email;
    private String first;
    private String last;

    public Name (int newId, String newFirst, String newLast, String newEmail){
        setId(newId);
        setFirst (newFirst);
        setLast (newLast);
        setEmail (newEmail);
    }

    public void setId( int newId){
        id = newId;
    }
    public void setFirst(String newFirst){
        first = newFirst;
    }
    public void setLast(String newLast){
        last = newLast;
    }
    public void setEmail(String newEmail){
        email = newEmail;
    }

    public int getId(){
        return id;
    }
    public String getFirst(){
        return first;
    }
    public String getLast(){
        return last;
    }

    public String getEmail(){
        return email;
    }
    public String toString(){
        return id + " " + first + " " + last + " " + email;
    }
}
