package helix.profitkey.hotelapp;
/** ProfitKey v1.0.0
 *  Purpose	   : Get and Set Pogo class (Not used)
 *  Created by : Abish
 *  Created Dt : 3/8/2016
 *  Modified on: for extra features of hotels like fan, AC etc
 *  Verified by:
 *  Verified Dt:
 * **/

public class GetSetOffers {
    String offer_id,offer_name, title, description, price,tax, from_dt, to_dt, category,hotel_id,room_type_id,image_url,
            inclusions,food_options,event_options,created_date,room_type_description,complementary_details,bed_details,guest_allowed;
    int length_of_stay,fan,ac,fridge,tv,bar_avail,wifi,room_service,restaurant_availability;

    /** Empty Constructor*/
    public GetSetOffers(){
    }
    /** Constructor */
    public GetSetOffers(String ofr_id, String ofr_name, String ttl, String descrip, String prce, String frm_dt,
                        String t_dt, String categ, String htl_id, String rm_type_id) {
        this.offer_name = ofr_name;
        this.title = ttl;
        this.description = descrip;
        this.price = prce;
        this.from_dt = frm_dt;
        this.to_dt = t_dt;
        this.category = categ;
        this.hotel_id = htl_id;
        this.room_type_id = rm_type_id;
        this.offer_id = ofr_id;
    }

    /** getting Offer Name     */
    public String getOfferName() {
        return this.offer_name;
    }
    public String getOfferTitle() {/** getting Offer Title     */
        return this.title;
    }
    public String getOfferDescrip() {/** getting Offer Description     */
        return this.description;
    }
    public String getOfferPrice() {/** getting Offer Price     */
        return this.price;
    }
    public String getTax() {/** getting Tax     */
        return this.tax;
    }
    public String getOfferFromDt() {/** getting Offer From Date     */
        return this.from_dt;
    }
    public String getOfferToDt() {/** getting Offer To Date     */
        return this.to_dt;
    }
    public String getOfferCategory() {/** getting Offer Category     */
        return this.category;
    }
    public String getHotelId() {/** getting Hotel Id     */
        return this.hotel_id;
    }
    public String getRoomTypeId() {/** getting Room Type Id     */
        return this.room_type_id;
    }
    public String getOfferId() {/** getting Offer Id     */
        return this.offer_id;
    }
    public String getImageUrl() {/** getting Image Url     */
        return this.image_url;
    }
    public int getLengthOfStay() {/** getting Length Of Stay     */
        return this.length_of_stay;
    }
    public String getInclusions() {/** getting Inclusions     */
        return this.inclusions;
    }
    public String getFoodOptions() {/** getting Food Options     */
        return this.food_options;
    }
    public String getEventOptions() {/** getting Event Options     */
        return this.event_options;
    }
    public String getCreatedDate() {/** getting Created Date     */
        return this.created_date;
    }
    public String getRoomTypeDescription() {/** getting Room Type Description     */
        return this.room_type_description;
    }
    public int getFan() {/** getting Fan     */
        return this.fan;
    }
    public int getAC() {/** getting AC     */
        return this.ac;
    }
    public int getFridge() {/** getting Fridge     */
        return this.fridge;
    }
    public int getTV() {/** getting TV     */
        return this.tv;
    }
    public int getBar() {/** getting Bar     */
        return this.bar_avail;
    }
    public int getWifi() {/** getting Wifi     */
        return this.wifi;
    }
    public String getComplementary() {/** getting Complementary     */
        return this.complementary_details;
    }
    public String getBedDetail() {/** getting Bed Detail     */
        return this.bed_details;
    }
    public String getGuestAllowed() {/** getting Guest Allowed      */
        return this.guest_allowed;
    }
    public int getRoomService() {/** getting Room Service     */
        return this.room_service;
    }
    public int getRestaurantAvailability() {/** getting Restaurant Availability     */
        return this.restaurant_availability;
    }

    /** setting Offer Name     */
    public void setOfferName(String name) {
        this.offer_name = name;
    }
    public void setOfferTitle(String tit) {/** setting Offer Title     */
        this.title = tit;
    }
    public void setOfferDescrip(String descrip) {/** setting Offer Description     */
        this.description = descrip;
    }
    public void setOfferPrice(String prc) {/** setting Offer Price     */
        this.price = prc;
    }
    public void setTax(String tx) {/** setting Tax     */
        this.tax = tx;
    }
    public void setOfferFromDt(String frm) {/** setting Offer From Date     */
        this.from_dt = frm;
    }
    public void setOfferToDt(String to) {/** setting Offer To Date     */
        this.to_dt = to;
    }
    public void setOfferCategory(String catg) {/** setting Offer Category     */
        this.category = catg;
    }
    public void setHotelId(String h_id) {/** setting Hotel Id     */
        this.hotel_id = h_id;
    }
    public void setRoomTypeId(String rm_typ_id) {/** setting Room Type Id     */
        this.room_type_id = rm_typ_id;
    }
    public void setOfferId(String ofr_id) {/** setting Offer Id     */
        this.offer_id = ofr_id;
    }
    public void setImageUrl(String url){/** setting Image Url     */
        this.image_url = url;
    }
    public void setLengthOfStay(int len) {/** setting Length Of Stay     */
        this.length_of_stay=len;
    }
    public void setInclusions(String incl) {/** setting Inclusions     */
        this.inclusions=incl;
    }
    public void setFoodOptions(String food) {/** setting Food Options     */
        this.food_options=food;
    }
    public void setEventOptions(String evnt) {/** setting Event Options     */
        this.event_options=evnt;
    }
    public void setCreatedDate(String dt) {/** setting Created Date     */
        this.created_date=dt;
    }
    public void setRoomTypeDescription(String rm_typ) {/** setting Room Type Description     */
        this.room_type_description=rm_typ;
    }
    public void setFan(int fn) {/** setting Fan     */
        this.fan=fn;
    }
    public void setAC(int air) {/** setting AC     */
        this.ac=air;
    }
    public void setFridge(int frdg) {/** setting Fridge     */
        this.fridge=frdg;
    }
    public void setTV(int telv) {/** setting TV     */
        this.tv=telv;
    }
    public void setBar(int bar1) {/** setting Bar     */
        this.bar_avail=bar1;
    }
    public void setWifi(int wif) {/** setting Wifi     */
        this.wifi=wif;
    }
    public void setComplementary(String complement) {/** setting Complementary     */
        this.complementary_details=complement;
    }
    public void setBedDetail(String bed) {/** setting Bed Detail     */
        this.bed_details=bed;
    }
    public void setGuestAllowed(String gst_alwd) {/** setting Guest Allowed     */
        this.guest_allowed=gst_alwd;
    }
    public void setRoomService(int rm_srvc) {/** setting Room Service     */
        this.room_service=rm_srvc;
    }
    public void setRestaurantAvailability(int restaurant_avail) {/** setting Restaurant Availability     */
        this.restaurant_availability=restaurant_avail;
    }
}
