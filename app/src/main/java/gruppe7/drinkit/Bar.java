package gruppe7.drinkit;

import java.util.Calendar;

/**
 * Created by namanhnguyen on 17/06/16.
 */
public class Bar implements Comparable<Bar> {

    private String name;
    private String openingTime;
    private Double price;
    private String closingTime;
    private String location;
    private double latitude;
    private double longitude;
    private String open;
    private String type;
    private int amount;
    private Integer distance;
    private String sortBy;
    public String buttonName;

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }


    public int getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setButtonName(String name) {
        this.buttonName = name;
    }

    public String getButtonName() {
        return buttonName;
    }

    public String getName() {
        return name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }


    @Override
    public int compareTo(Bar o) {
        if (o.sortBy.equals("price")) {
            return this.price.compareTo(o.getPrice());
        }
        return this.distance.compareTo(o.getDistance());
    }

    public boolean isOpen (){
        Calendar curDate = Calendar.getInstance();
        int dayOfWeek = curDate.get(Calendar.DAY_OF_WEEK);

        if (getOpen().equals("Alle dage")){
            return true;
        }
        else if (getOpen().equals("fredagsåbent")){
            if(dayOfWeek !=Calendar.FRIDAY){
                return false;
            }
        }else if(getOpen().equals("Hverdage")){
            if(dayOfWeek >=(Calendar.MONDAY)&&dayOfWeek<=Calendar.FRIDAY){
                return false;
            }
        }
        String[] openArray = getOpeningTime().split(":");
        String[] closedArray = getClosingTime().split(":");
        int openHours = Integer.parseInt(openArray[0]);
        int openMin = Integer.parseInt(openArray[1]);
        int openTimeSec = openHours * 360 + openMin * 60;

        int closedHours = Integer.parseInt(closedArray[0]);
        int closedMin = Integer.parseInt(closedArray[1]);
        int closedTimeSec = closedHours * 360 + closedMin * 60;

        int currentTimeSec = curDate.get(Calendar.HOUR_OF_DAY) * 360 + curDate.get(Calendar.MINUTE) * 60;
        curDate.get(Calendar.MINUTE);

        if (currentTimeSec > openTimeSec && currentTimeSec < closedTimeSec) {
            return true;
        }
        return false;
    }

}