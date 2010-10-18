package com.pyxis.petstore.domain.time;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class SystemClock implements Clock {
    
    public Date today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}
