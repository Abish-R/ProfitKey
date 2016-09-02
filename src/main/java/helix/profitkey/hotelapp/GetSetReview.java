package helix.profitkey.hotelapp;
/** ProfitKey v1.0.0
 *  Purpose	   : Get and Set Pogo class for Offers
 *  Created by : Abish
 *  Created Dt : 4/7/2016
 *  Modified on:
 *  Verified by:
 *  Verified Dt:
 * **/

public class GetSetReview {
    String rev_id,rev_date,rev_tit, rev_description, rev_img_url,name;

    /**Empty Constructor*/
    public GetSetReview(){}

    /** getting Review Id     */
    public String getReviewId(){
        return this.rev_id;
    }
    public String getReviewDate() {/** getting Review Date     */
        return this.rev_date;
    }
    public String getReviewTitle() {/** getting Review Title    */
        return this.rev_tit;
    }
    public String getReviewDescription() {/** getting Review Description    */
        return this.rev_description;
    }
    public String getReviewImageurl() {/** getting Review Image url     */
        return this.rev_img_url;
    }
    public String getReviewName(){/** getting Review Name     */
        return this.name;
    }

    /** setting ReviewId     */
    public void setReviewId(String id){
        this.rev_id=id;
    }
    public void setReviewDate(String dt) {/** setting Review Date     */
        this.rev_date = dt;
    }
    public void setReviewTitle(String tit) {/** setting Review Title     */
        this.rev_tit = tit;
    }
    public void setReviewDescription(String des) {/** setting Review Description     */
        this.rev_description = des;
    }
    public void setReviewImageurl(String url) {/** setting Review Image url     */
        this.rev_img_url = url;
    }
    public void setReviewName(String nam){/** setting Review Name     */
        this.name = nam;
    }
}
