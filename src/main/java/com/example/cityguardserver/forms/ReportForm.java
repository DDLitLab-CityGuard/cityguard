package com.example.cityguardserver.forms;

import com.example.cityguardserver.databasemodels.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;

import static java.lang.Float.parseFloat;


@Getter
@Setter
@Slf4j
public class ReportForm {

    private List<Float> coordinates;
    private Calendar calendar;
    private String description;
    private String category;

    public ReportForm(String location,String date,String time,String desc,String currentDateTime, String currentLocation,String category){
        System.out.println(currentDateTime);
        System.out.println(currentLocation);
        this.coordinates=splitcoordinates(location);
        setDateandTime(date,time);
        this.description=desc;
        this.category=category;
    }




    private List<Float> splitcoordinates(String coordinates){
        List<Float> latLongList= new LinkedList<>();
        String[] latlongArray =coordinates.split(",");
        Float latFloat=parseFloat(latlongArray[0]);
        Float longFloat=parseFloat(latlongArray[1]);
        latLongList.add(latFloat);
        latLongList.add(longFloat);
        return latLongList;

    }


    private void setDateandTime(String dateString, String time){
        this.calendar=Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(dateString);
            calendar.setTime(date);
        } catch (ParseException e) {
            log.error(String.valueOf(e));
        }
        calendar.set(Calendar.HOUR_OF_DAY,12);
        calendar.set(Calendar.MINUTE,30);
    }


}