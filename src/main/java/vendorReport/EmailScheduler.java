package vendorReport;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class EmailScheduler {
	
	public void callScheduler() throws Exception
    {

        System.out.println("Scheduler Starterd...");
        Timer timer = new Timer();
        long millisInADay = 24*60*60*1000;
        Calendar cal = Calendar.getInstance();
        Date date =new Date();
        cal.set(cal.HOUR_OF_DAY, 11);
        cal.set(cal.MINUTE, 0);
        cal.set(cal.SECOND, 0);
        cal.set(cal.MILLISECOND,0);
        Date am11 = new Date(cal.getTimeInMillis());
        cal.set(cal.HOUR_OF_DAY, 16);
        cal.set(cal.MINUTE, 0);
        cal.set(cal.SECOND, 0);
        cal.set(cal.MILLISECOND,0);
        Date pm4 = new Date(cal.getTimeInMillis());
        System.out.println("Will send morning roster i: "+  (millisInADay+ am11.getTime()-date.getTime()) % millisInADay/60000+"mins");
        System.out.println("Will send evening roster i: "+  (millisInADay+ pm4.getTime()-date.getTime()) % millisInADay/60000+"mins");
        timer.scheduleAtFixedRate(new EmailDispatcher(),(millisInADay+ am11.getTime()-date.getTime()) % millisInADay, millisInADay);
        timer.scheduleAtFixedRate(new EmailDispatcher(), (millisInADay+ pm4.getTime()-date.getTime()) % millisInADay, millisInADay);
        //timer.scheduleAtFixedRate(new EmailDispatcher(), 5000, 30000);
       
    }
	 public static void main(String a[]) throws Exception
     {
		 EmailScheduler ems = new EmailScheduler();
         ems.callScheduler();
     }

}
