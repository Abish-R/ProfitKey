package helix.profitkey.hotelapp;
/** ProfitKey v1.0.0
 *  Purpose	   : Get and Set Pogo class for Booking History
 *  Created by : Abish
 *  Created Dt : 3/12/2016
 *  Modified on: for extra features of hotels like fan, AC etc
 *  Verified by:
 *  Verified Dt:
 * **/

public class GetSetBookedHistory {
    String hotel_id, hotel_name, offerusedstatus, offer_id, offer_title, offer_description, offer_price,
            room_type_name, from_date,to_date,total_days,price,booked_date,total_price;

    /** Empty Constructor*/
    public GetSetBookedHistory(){}
    /** Constructor */
    public GetSetBookedHistory(String hotl_id, String ofr_usd_state, String htl_name, String ofr_id,String ofr_tit,
                           String ofr_descrip, String ofr_prc,String rm_typ, String frm, String to,
                               String tl_dy, String pric,String bk_dt, String tot_prc){

    }

    public String getHotelId() {/** getting Hotel Id     */
        return this.hotel_id;
    }
    public String getOfferUsedStatus() {/** getting Offer Used Status     */
        return this.offerusedstatus;
    }
    public String getHotelName() {/** getting Hotel Name     */
        return this.hotel_name;
    }
    public String getOfferId() {/** getting Offer Id     */
        return this.offer_id;
    }
    public String getOfferTitle() {/** getting Offer Title     */
        return this.offer_title;
    }
    public String getOfferDescription() {/** getting Offer Description     */
        return this.offer_description;
    }
    public String getOfferPrice() {/** getting Offer Price     */
        return this.offer_price;
    }
    public String getRoomType() {/** getting Room Type     */
        return this.room_type_name;
    }
    public String getFromDate() {/** getting From Date     */
        return this.from_date;
    }
    public String getToDate() {/** getting To Date     */
        return this.to_date;
    }
    public String getTotalDays() {/** getting Total Days     */
        return this.total_days;
    }
    public String getPrice() {/** getting Price     */
        return this.price;
    }
    public String getBookedDate() {/** getting Booked Date     */
        return this.booked_date;
    }
    public String getTotalPrice() {/** getting Total Price     */
        return this.total_price;
    }


    /** setting Hotel Id     */
    public void setHotelId(String hotl_id) {
        this.hotel_id = hotl_id;
    }
    public void setOfferUsedStatus(String ofr_usd_state) {/** setting Offer Used Status     */
        this.offerusedstatus = ofr_usd_state;
    }
    public void setHotelName(String htl_name) {/** setting Hotel Name     */
        this.hotel_name = htl_name;
    }
    public void setOfferId(String ofr_id) {/** setting Offer id     */
        this.offer_id = ofr_id;
    }
    public void setOfferTitle(String ofr_tit) {/** setting Offer Title     */
        this.offer_title = ofr_tit;
    }
    public void setOfferDescription(String ofr_descrip) {/** setting Offer Description     */
        this.offer_description = ofr_descrip;
    }
    public void setOfferPrice(String ofr_prc) {/** setting Offer Price     */
        this.offer_price = ofr_prc;
    }
    public void setRoomType(String rm_typ) {/** setting Room Type     */
        this.room_type_name = rm_typ;
    }
    public void setFromDate(String frm) {/** setting From Date     */
        this.from_date = frm;
    }
    public void setToDate(String to) {/** setting To Date     */
        this.to_date = to;
    }
    public void setTotalDays(String tl_dy) {/** setting Total Days     */
        this.total_days = tl_dy;
    }
    public void setPrice(String pric) {/** setting Price     */
        this.price = pric;
    }
    public void setBookedDate(String bk_dt) {/** setting Booked Date     */
        this.booked_date = bk_dt;
    }
    public void setTotalPrice(String tot_prc) {/** setting Total Price     */
        this.total_price = tot_prc;
    }
}
