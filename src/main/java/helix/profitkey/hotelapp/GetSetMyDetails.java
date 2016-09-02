package helix.profitkey.hotelapp;
/** ProfitKey v1.0.0
 *  Purpose	   : Get and Set Pogo class (Not used)
 *  Created by : Abish
 *  Created Dt : 3/11/2016
 *  Modified on: for extra features of hotels like fan, AC etc
 *  Verified by:
 *  Verified Dt:
 * **/

public class GetSetMyDetails {
    String first_name, last_name, username, password, phone, mobile, email,dob,gender,city,
            country,zipcode,hotel_id,uuid;

    /** Empty Constructor*/
    public GetSetMyDetails(){}
    /** Constructor */
    public GetSetMyDetails(String f_name, String l_name, String uname, String pword,String phon,
            String mobil, String mail,String dob1, String gendr, String cit, String countr, String zipcod, String hid,String uu){
        first_name=f_name;
        last_name=l_name;
        username=uname;
        password=pword;
        phone=phon;
        mobile=mobil;
        email=mail;
        dob=dob1;
        gender=gendr;
        city=cit;
        country=countr;
        zipcode=zipcod;
        hotel_id=hid;
        uuid=uu;
    }
    /** getting First Name     */
    public String getFirstName() {
        return this.first_name;
    }
    public String getLastName() {/** getting Last Name     */
        return this.last_name;
    }
    public String getUsername() {/** getting Username     */
        return this.username;
    }
    public String getPassword() {/** getting Password     */
        return this.password;
    }
    public String getPhone() {/** getting Phone     */
        return this.phone;
    }
    public String getMobile() {/** getting Mobile     */
        return this.mobile;
    }
    public String getEmail() {/** getting Email     */
        return this.email;
    }
    public String getDOB() {/** getting DOB     */
        return this.dob;
    }
    public String getGender() {/** getting Gender     */
        return this.gender;
    }
    public String getCity() {/** getting City     */
        return this.city;
    }
    public String getCountry() {/** getting Country     */
        return this.country;
    }
    public String getZipcode() {/** getting Zipcode     */
        return this.zipcode;
    }
    public String getHotel_id() {/** getting Hotel_id     */
        return this.hotel_id;
    }
    public String getUUID() {/** getting UUID     */
        return this.uuid;
    }


    /** setting First Name     */
    public void setFirstName(String fname) {
        this.first_name = fname;
    }
    public void setLastName(String lname) {/** setting Last Name     */
        this.last_name = lname;
    }
    public void setUsername(String uname) {/** setting User name     */
        this.username = uname;
    }
    public void setPassword(String pass) {/** setting Password     */
        this.password = pass;
    }
    public void setPhone(String ph) {/** setting Phone     */
        this.phone = ph;
    }
    public void setMobile(String mob) {/** setting Mobile     */
        this.mobile = mob;
    }
    public void setEmail(String eml) {/** setting Email     */
        this.email = eml;
    }
    public void setDOB(String bday) {/** setting DOB     */
        this.dob = bday;
    }
    public void setGender(String gen) {/** setting Gender     */
        this.gender = gen;
    }
    public void setCity(String cit) {/** setting City     */
        this.city = cit;
    }
    public void setCountry(String contry) {/** setting Country     */
        this.country = contry;
    }
    public void setZipcode(String fname) {/** setting Zipcode     */
        this.zipcode = fname;
    }
    public void setHotel_id(String h_id) {/** setting Hotel_id     */
        this.hotel_id = h_id;
    }
    public void setUUID(String uuid) {/** setting UUID     */
        this.uuid = uuid;
    }

}
