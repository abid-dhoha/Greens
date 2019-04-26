/*
 * Created by Melanie Trammell on 2019.04.16  * 
 * Copyright © 2019 Melanie Trammell. All rights reserved. * 
 */
package edu.vt.managers;

import edu.vt.EntityBeans.Orders;
import edu.vt.FacadeBeans.OrdersFacade;
import edu.vt.controllers.OrdersController;
import edu.vt.controllers.CartController;
import edu.vt.globals.Methods;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Melanie
 */

@Named(value = "orderManager")
@SessionScoped
public class OrderManager implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    
    //some static variables that will be editable from the place an Order page
    String orderType;
    String specialInstructions;
    
    //used to update Dababase
    OrdersFacade ejbFacade;
    
    //Inject CartController to help the order manager save the menu items 
    //that are being bought (which are in the cart), to the order
     @Inject CartController cartController;
     
     
    //Inject CartController to help the order manager save the menu items 
    //that are being bought (which are in the cart), to the order
     @Inject OrdersController ordersController;
    
    //TODO: need POJO Class
    // private List<Menu> orderItems = null;
    
    /*
    ==================
    Constructor Method
    ==================
     */
    public OrderManager(){
    }

    /*
    ================
    Instance Methods
    ================
*
     */
    
    private OrdersFacade getFacade() {
        return ejbFacade;
    }
    
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
    
    
    //TODO: what about phone number? I guess that will be in User... but how 
    //does the system remember to send the text?
    
    //TODO: how does the system remember how it was paid for?
    
    //TODO: wouldn't it be better to call OrderController to create..? won't
    //OrderController have the parameters?
    public String placeOrder() {
        
        Integer primaryKey = (Integer) Methods.sessionMap().get("user_id");
        //Integer id, String orderItems, String orderType, Date orderTimestamp, String orderStatus, float orderTotal, String specialInstructions
        Orders o = new Orders();
        
        //TODO
        //I need the User object
        //o.setUserId();

        String orderItems = cartController.getSelected().getCartItems();
        o.setOrderItems(orderItems);
        o.setOrderType(orderType);
        
        Date d = new Date(System.currentTimeMillis());
        o.setOrderTimestamp(d);
        
        o.setOrderStatus("PLACED");
        
        //TODO
        //pull pieces of orderItems and get total and place here
        o.setOrderTotal(0);
        o.setSpecialInstructions(specialInstructions);
        
        //TODO: notification flag
        
        //Create an Order Object and then call the OrderFacade create and pass all
        //necessary parameters - it felt weird to recreate what already exists in 
        //ordercontroller... but then selected is already set...
        
        ordersController.setSelected(o);
        ordersController.prepareCreate();
        ordersController.create();
        
        
        //empty the cart by calling removeAllItemsFromCart() - inject the cart controller
        cartController.removeAllItemsFromCart();
        
        return "/orders/OrderHistory?faces-redirect=true";
    }
    
    //manage changing order status
    //TODO: Should pass userid or Order id?
    //probably order id
    public void changeOrderStatus(String newStatus, Integer id){
        Orders o = getFacade().findOrdersbyId(id);
        
        if(newStatus == "READY"){
            //check flag
            //sent text or ask controller to do it
            //TODO
        
        }
        o.setOrderStatus(newStatus);
    }
    
}