package com.ws.data;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 02/12/13
 * Time: 00:40
 * To change this template use File | Settings | File Templates.
 */
public class Contact {

    String phone;
    String formattedPhone;

    public Contact(String phone, String formattedPhone) {
        this.phone = phone;
        this.formattedPhone = formattedPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFormattedPhone() {
        return formattedPhone;
    }

    public void setFormattedPhone(String formattedPhone) {
        this.formattedPhone = formattedPhone;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "phone='" + phone + '\'' +
                ", formattedPhone='" + formattedPhone + '\'' +
                '}';
    }
}
