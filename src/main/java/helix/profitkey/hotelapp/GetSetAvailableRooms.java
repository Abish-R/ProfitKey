package helix.profitkey.hotelapp;
/** ProfitKey v1.0.0
 *  Purpose	   : Get and Set Pogo class for Available rooms
 *  Created by : Abish
 *  Created Dt : 4/12/2016
 *  Modified on: for extra features of hotels like fan, AC etc
 *  Verified by:
 *  Verified Dt:
 * **/

public class GetSetAvailableRooms {
    String room_type_id,room_type_name,available_rooms,price,tax,room_type_description,complementary_details,bed_details,guest_allowed;
    int offer_status,fan,ac,fridge,tv,bar_avail,wifi,room_service,restaurant_availability,today_room_availability;

    /** Empty Constructor*/
    public GetSetAvailableRooms(){}
    /** Constructor */
    public GetSetAvailableRooms(String type_id,String type,String avail,String pric,int state){
        this.room_type_id = type_id;
        this.room_type_name = type;
        this.available_rooms = avail;
        this.price = pric;
        this.offer_status = state;
    }

    /** Get Room Type Id*/
    public String getRoomTypeId() {
        return this.room_type_id;
    }
    public String getRoomType() {/** Get Room Type Id*/
        return this.room_type_name;
    }
    public String getAvailableRooms() {/** Get Available Rooms*/
        return this.available_rooms;
    }
    public String getPrice() {/** Get Price*/
        return this.price;
    }
    public String getTax() {/** Get Tax*/
        return this.tax;
    }
    public int getOfferStatus() {/** Get Offer Status*/
        return this.offer_status;
    }
    public String getRoomTypeDescription() {/** Get Room Type Description*/
        return this.room_type_description;
    }
    public int getFan() {/** Get Fan*/
        return this.fan;
    }
    public int getAC() {/** Get AC*/
        return this.ac;
    }
    public int getFridge() {/** Get Fridge*/
        return this.fridge;
    }
    public int getTV() {/** Get TV*/
        return this.tv;
    }
    public int getBar() {/** Get Bar*/
        return this.bar_avail;
    }
    public int getWifi() {/** Get Wifi*/
        return this.wifi;
    }
    public String getComplementary() {/** Get Complementary*/
        return this.complementary_details;
    }
    public String getBedDetail() {/** Get Bed Detail*/
        return this.bed_details;
    }
    public String getGuestAllowed() {/** Get Guest Allowed*/
        return this.guest_allowed;
    }
    public int getRoomService() {/** Get Room Service*/
        return this.room_service;
    }
    public int getRestaurantAvailability() {/** Get Restaurant Availability*/
        return this.restaurant_availability;
    }
    public int getRommAvailabilityToday() {/** Get Room Availability Today*/
        return today_room_availability;
    }

    /** Set Room Type Id*/
    public void setRoomTypeId(String type_id) {
        this.room_type_id = type_id;
    }
    public void setRoomType(String type) {/** Set Room Type*/
        this.room_type_name = type;
    }
    public void setAvailableRooms(String avail) {/** Set Available Rooms*/
        this.available_rooms = avail;
    }
    public void setPrice(String pric) {/** Set Price*/
        this.price = pric;
    }
    public void setTax(String tx) {/** Set Tax*/
        this.tax = tx;
    }
    public void setOfferStatus(int state) {/** Set Offer Status*/
        this.offer_status = state;
    }
    public void setRoomTypeDescription(String rm_typ) {/** Set Room Type Description*/
        this.room_type_description=rm_typ;
    }
    public void setFan(int fn) {/** Set Fan*/
        this.fan=fn;
    }
    public void setAC(int air) {/** Set AC*/
        this.ac=air;
    }
    public void setFridge(int frdg) {/** Set Fridge*/
        this.fridge=frdg;
    }
    public void setTV(int telv) {/** Set TV*/
        this.tv=telv;
    }
    public void setBar(int bar1) {/** Set Bar*/
        this.bar_avail=bar1;
    }
    public void setWifi(int wif) {/** Set Wifi*/
        this.wifi=wif;
    }
    public void setComplementary(String complement) {/** Set Complementary*/
        this.complementary_details=complement;
    }
    public void setBedDetail(String bed) {/** Set Bed Detail*/
        this.bed_details=bed;
    }
    public void setGuestAllowed(String gst_alwd) {/** Set Guest Allowed*/
        this.guest_allowed=gst_alwd;
    }
    public void setRoomService(int rm_srvc) {/** Set Room Service*/
        this.room_service=rm_srvc;
    }
    public void setRestaurantAvailability(int restaurant_avail) {/** Set Restaurant Availability*/
        this.restaurant_availability=restaurant_avail;
    }
    public void setRommAvailabilityToday(int _avail) {/** Set Room Availability Today*/
        this.today_room_availability=_avail;
    }

}
