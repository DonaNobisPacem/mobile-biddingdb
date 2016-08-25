package com.example.donanobispacem.biddingdb;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by donanobispacem on 8/22/16.
 */

public class Bid implements Serializable {

    private int _id;
    private String title;
    private String contractor;
    private String number;
    private String mode;
    private String budget;
    private String amount;
    private String preprocurement;
    private String prebidding;
    private String bidding;
    private String postqualification;
    private String noa;
    private String purchase;
    private String ntp;
    private String members;
    private String addtl_info;
    private String remarks;
    private String archived;

    public  Bid(){
        super();
    }

    public Bid(int _id, String title, String contractor, String mode ) {
        super();
        this._id = _id;
        this.title = title;
        this.contractor = contractor;
        this.mode = mode;
    }

    public Bid(int _id, String title, String contractor, String mode, String number, String budget, String amount, String preprocurement, String prebidding, String bidding, String postqualification, String noa, String purchase, String ntp, String members, String addtl_info, String remarks, String archived ) {
        super();
        this._id = _id;
        this.title = title;
        this.contractor = contractor;
        this.mode = mode;
        this.number = number;
        this.budget = budget;
        this.amount = amount;
        this.preprocurement = preprocurement;
        this.prebidding = prebidding;
        this.bidding = bidding;
        this.postqualification = postqualification;
        this.noa = noa;
        this.purchase = purchase;
        this.ntp = ntp;
        this.members = members;
        this.addtl_info = addtl_info;
        this.remarks = remarks;
        this.archived = archived;
    }

    public int getID(){
        return _id;
    }

    public String getTitle(){
        return title;
    }

    public String getContractor(){
        return contractor;
    }

    public String getMode(){
        if( mode != null && !mode.equals("null") && !mode.isEmpty() ) {
            switch (Integer.parseInt(mode)) {
                case 1:
                    return "Shopping";
                case 2:
                    return "Public Bidding";
                case 3:
                    return "Small Value Procurement";
                default:
                    return "Unspecified";
            }
        }
        return "Unspecified";
    }

    public String getNumber(){
        return number;
    }

    public String getBudget(){
        String formatted = new String("N/A");

        if( budget != null && !budget.isEmpty() && !budget.equals("null") ) {
            try {
                double d = Double.parseDouble(budget);
                DecimalFormat formatter = new DecimalFormat("#,###.##");
                formatted = "Php " + formatter.format(d);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return formatted;
    }

    public String getAmount(){
        String formatted = new String("N/A");

        if( amount != null && !amount.isEmpty() && !amount.equals("null") ) {
            try {
                double d = Double.parseDouble(amount);
                DecimalFormat formatter = new DecimalFormat("#,###.##");
                formatted = "Php " + formatter.format(d);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return formatted;
    }

    public String getPreprocurement(){
        String formatted = new String("N/A");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        if( preprocurement != null && !preprocurement.isEmpty() && !preprocurement.equals("null") ) {
            try {
                Date date = inputDateFormat.parse(preprocurement);
                formatted = outputDateFormat.format(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return formatted;
    }

    public String getPrebidding(){
        String formatted = new String("N/A");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        if( prebidding != null && !prebidding.isEmpty() && !prebidding.equals("null") ) {
            try {
                Date date = inputDateFormat.parse(prebidding);
                formatted = outputDateFormat.format(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return formatted;
    }

    public String getBidding(){
        String formatted = new String("N/A");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        if( bidding != null && !bidding.isEmpty() && !bidding.equals("null") ) {
            try {
                Date date = inputDateFormat.parse(bidding);
                formatted = outputDateFormat.format(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return formatted;
    }

    public String getPostqualification(){
        String formatted = new String("N/A");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        if( postqualification != null && !postqualification.isEmpty() && !postqualification.equals("null") ) {
            try {
                Date date = inputDateFormat.parse(postqualification);
                formatted = outputDateFormat.format(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return formatted;
    }

    public String getNoa(){
        String formatted = new String("N/A");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        if( noa != null && !noa.isEmpty() && !noa.equals("null") ) {
            try {
                Date date = inputDateFormat.parse(ntp);
                formatted = outputDateFormat.format(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return formatted;
    }

    public String getPurchase(){
        String formatted = new String("N/A");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        if( purchase != null && !purchase.isEmpty() && !purchase.equals("null") ) {
            try {
                Date date = inputDateFormat.parse(purchase);
                formatted = outputDateFormat.format(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return formatted;
    }

    public String getNtp(){
        String formatted = new String("N/A");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        if( ntp != null && !ntp.isEmpty() && !ntp.equals("null") ) {
            try {
                Date date = inputDateFormat.parse(ntp);
                formatted = outputDateFormat.format(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return formatted;
    }

    public String getMembers(){
        return members;
    }

    public String getAddtl_info(){
        return addtl_info;
    }

    public String getRemarks(){
        return remarks;
    }

    public String getArchived(){
        return archived;
    }

    public void setID(int _newID) {
        _id = _newID;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public void setContractor(String newContractor) {
        contractor = newContractor;
    }

    public void setMode(String newMode) {
        mode = newMode;
    }

    public void setNumber(String newNumber) {
        number = newNumber;
    }

    public void setBudget(String newBudget) {
        budget = newBudget;
    }

    public void setAmount(String newAmount) {
        amount = newAmount;
    }

    public void setPreprocurement(String newPreprocurement) {
        preprocurement = newPreprocurement;
    }

    public void setPrebidding(String newPrebidding) {
        prebidding = newPrebidding;
    }

    public void setBidding(String newBidding) {
        bidding = newBidding;
    }

    public void setPostqualification(String newPostqualification) { postqualification = newPostqualification; }

    public void setNoa(String newNoa) {
        noa = newNoa;
    }

    public void setPurchase(String newPurchase) {
        purchase = newPurchase;
    }

    public void setNtp(String newNtp) {
        ntp = newNtp;
    }

    public void setMembers(String newMembers) {
        members = newMembers;
    }

    public void setAddtl_info(String newAddtl_info) {
        addtl_info = newAddtl_info;
    }

    public void setRemarks(String newRemarks) {
        remarks = newRemarks;
    }

    public void setArchived(String newArchived) {
        archived = newArchived;
    }

}