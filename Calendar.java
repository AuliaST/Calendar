
// Calender 
// File: Calendar.java
// Purpose: Create a calendar with file reading, file printing,
//			and event additions to the calendar.

import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner; //program imports the Scanner from the library
import java.lang.Math;
import java.util.*;
import java.io.*;
public class Calendar
{
	// main method to start running calendar program
	public static void main(String[] args) throws FileNotFoundException
   {  
     
      File inp;
      File outp;
      Calendar cal = Calendar.getInstance();
	  Scanner input = new Scanner(System.in);
	  initializeEventArray(cal); // runs method to create and initialize global array for events
      inp = new File("calendarEvents.txt");
      // checks if there is an event txt file to load into the event array
      if(!inp.exists()) {
    	  System.out.println("no calendarEvents.txt found");
      }
      else {
    	  System.out.println("calendarEvents.txt found, loading events into calendar");
    	  loadFileToCalendar(inp);
      }
	  
	  int MM=-1, DD=-1;
	  int quit=-1;
	  String date="";
	  String choiceString = "";
	  char choice;
	  String trash = "";
	  // while statement to run until the user chooses to quit with q
	  while(quit!=1) 
	  {
		  menu();
		  choiceString = input.next();
		  if(choiceString.length()>2) {
			  choiceString = choiceString.substring(0,2);
		  }
		  // checks for given user input string's first two characters
		  // these two if statements are corresponding to the character switch
		  // that is used so as to not switch everything to a String variable
		  if(choiceString.equals("ev")) 
		  {
			  choice = 'v';
		  }
		  else if(choiceString.equals("fp")) {
			  choice = 'f';
		  }
		  else 
		  {
			  choice = choiceString.charAt(0); 
		  }
		  
		  trash = input.nextLine();
		  // switch statement to choose a menu option
		  switch (choice)
		  {
		  	  // uses the calendar object to set the month and day given user input
		  	  // adds 1 to month because the calendar object indexes months starting at 0
			  case 'e':
				  System.out.println("What month and date would you like to search? (mm/dd)");
				  date = input.nextLine();
				  MM = monthFromDate(date);
			      DD = dayFromDate(date);
			      cal.set(cal.MONTH,MM-1);
			      cal.set(cal.DATE, DD);
			      runCalendar(MM,DD, cal);
				  break;
			  // option to display the current date
			  case 't':
				  cal = Calendar.getInstance();
				  MM = cal.get(Calendar.MONTH)+1;
				  DD = cal.get(Calendar.DAY_OF_MONTH);
				  runCalendar(MM,DD, cal);
				  break;
			  // option to display the next month by adding one month to the calendar object
			  case 'n':
				  if(MM!= -1) {
					  cal.add(cal.MONTH, 1);
					  MM = cal.get(Calendar.MONTH)+1;
					  runCalendar(MM,DD, cal);
				  }
				  else {
					  System.out.println("You must have a calendar date chosen first");
				  }
				  break;
		      // option to display the previous month by subtracting one month to the calendar object
			  case 'p':
				  if(MM!=-1) {
					  cal.add(cal.MONTH, -1);
					  MM = cal.get(Calendar.MONTH)+1;
					  runCalendar(MM,DD, cal);
				  }
				  else {
					  System.out.println("You must have a calendar date chosen first");
				  }
				  
				  break;
				  // option to enter an event to the calendar 
			  case 'v':
				  System.out.println("Please enter an event (MM/DD event_title): ");
				  String event;
				  String eventTitle;
				  event = input.nextLine();
				  date = event.substring(0,5);
				  eventTitle = event.substring(6);
				  int MMEvent = monthFromDate(date);
			      int DDEvent = dayFromDate(date);
			      eventArray[MMEvent-1][DDEvent-1] = eventTitle;
			      System.out.println("this is the event just set: "+ eventArray[MMEvent-1][DDEvent-1]);
			      runCalendar(MM, DD, cal);
				  break;
				  // option to print out a specified month to a file of choice
			  case 'f':
				  System.out.println("Please enter the month you wish to print(MM): ");
				  int MMFilePrint = input.nextInt();
				  System.out.println("Please enter the name of the file you wish to print it to: ");
				  String outputFile=input.next();
				  outp = new File(outputFile);
				  printToFile(outp, MMFilePrint, cal);
				  break;
			  // sets flag to allow the program to quit
				  
			  case 'q':
				  quit=1;
				  break;  
			  // case if no valid option is chosen
			  default:
				  System.out.println("invalid option, please try again");
		  }
		  
	  }

      
       
      
   } // End of main 
	
	// global array variable to keep track of when events are
	public static String eventArray[][] = new String [12][];
   
   
   	// Method to initialize the global array keeping track of events
   	public static void initializeEventArray(Calendar cal) {
   	   // for loop to initialize each month with correct amount of days
	   for(int i = 0; i<12; i++) {
		   cal.set(cal.MONTH,i);
		   eventArray[i] = new String[cal.getActualMaximum(cal.DAY_OF_MONTH)];
	   }
	   // just initializes each sub array value to ""
	   for(int i = 0; i< 12; i++) {
		   for(int j = 0; j < eventArray[i].length;j++) {
			   eventArray[i][j]="";
		   }
	   }
	   
   }
   
   	// Methods for drawing month display at top of calendar.
   	public static void drawMonth(int month, Calendar cal)
   {
      for (int i=1; i<=85; i++)
      {
         System.out.print(" ");
      }
      System.out.println(month);
      System.out.println("       Sunday                   Monday                   Tuesday               Wednesday               Thursday                  Friday                 Saturday");
       
      drawRow(cal.getActualMaximum(cal.WEEK_OF_MONTH), cal, month); 
   } // End of drawMonth method
   
   	// method to draw the top of the month display to the specified file
   	public static void drawMonthToFile(int month, Calendar cal, File outp) throws FileNotFoundException{
	   PrintStream out = new PrintStream(new FileOutputStream(outp,true));
	   for (int i=1; i<=85; i++)
	      {
	         out.print(" ");
	      }
	      out.println(month);
	      out.println("    Sunday             Monday           Tuesday         Wednesday         Thursday         Friday          Saturday");
	      cal.set(cal.MONTH, month-1);
	      drawRowToFile(cal.getActualMaximum(cal.WEEK_OF_MONTH), cal, month, outp);
	      out.close();
   }
   
   	// method to draw a specified amount of rows for the month
   	public static void drawRow(int row, Calendar cal, int month)
   {
	   boolean eventExists = false;
	   int eventDay = 0;
	   int eventDayMover = 0;
	   int firstDayOfWeek = 0;
	   int[] eventCounter = new int[32];// array that keeps track of events in order they need to be displayed
	   int weekday;
	   int eventTotal = 0;// total amount of events
	   int eventMover = 0;// incrementer variable to move over to next event to be displayed
	   int[] eventDayArray = new int[32];// keeps track of which day the event happens on 
	   									 // because of the way my print out for the calendar was handled
	   									 // needed to know for subsequent rows
	   int temp = cal.get(cal.DAY_OF_MONTH); // saves current day of the month
	   cal.set(cal.DAY_OF_MONTH, 1);// sets current day of the month to first of the month
	   weekday = cal.get(cal.DAY_OF_WEEK);// grabs the first day of the month eg. Sunday, monday etc.
	   cal.set(cal.DAY_OF_MONTH, temp);// sets the day of the month back to original day
	   int date1 =1;
	   int flag1 = 1;
	   int daysMax = cal.getActualMaximum(cal.DAY_OF_MONTH);// grabs how many total days can be in the current month
	   flag1 = Math.abs(weekday - flag1);// to determine how many blank boxes to print at the beginning of the month
	   int blankBoxes = flag1;
	   // for loop that checks if there are any events this month
	   for(int i = 0; i<cal.getActualMaximum(cal.DAY_OF_MONTH); i++) {
		   // if yes then the event flag is raised
		   if(!eventArray[cal.get(cal.MONTH)][i].equals("")) {
			   
			   eventExists = true;
		   }
	   }
	   
      // Nested for loop to print middle of calendar
      for (int row2=1; row2<=row; row2++)
      {
         for (int i=1; i<=170; i++)
         {
            System.out.print("=");
         }
         
         
         System.out.println();
         
         
         // For loop to print row of single digit dates
         for (int i=1; i<=7; i++)
         {
        	 // if i is 1 then the week just started printing out, so firstDayOfWeek
        	 // keeps track of the first date in the current week being printed
        	 if(i==1) {
        		 firstDayOfWeek = date1; 
        	 }
        	 
            if (date1<=9 && flag1 != 0)
            {
               System.out.print("|                       ");
               flag1--;
            }
            // Else if statement that prints until 31 for double digits.
            else if (date1>=10 && date1<=daysMax)
            {
            	// if statement that prints out indication of the current day
            	if(date1==cal.get(cal.DAY_OF_MONTH)) {
            		System.out.print("|"+ date1 + "       today         ");
            	}
            	else {
            		System.out.print("|"+ date1 + "                     ");
            	}
            	// if there is an event for the current date
            	if(eventExists && !eventArray[cal.get(cal.MONTH)][date1-1].equals("")) {
            	   // keeps track of the day it falls on for the week
             	   eventDayArray[eventDay] = i;
             	   // keeps track of the actual date of when that event happened so it can be
             	   // accessed by the eventArray later
             	   eventCounter[eventTotal]=date1-1;
             	   // increments both counters
             	   eventTotal++;
             	   eventDay++;
                }
               date1++;
            }
            // else if for the indication of the current day for single digit dates
            // everything is the same but for single digits
            else if(date1<=9 && flag1==0)
            {
            	if(date1==cal.get(cal.DAY_OF_MONTH)) {
            		System.out.print("|"+date1+"        today         ");
            	}
            	else {
            		System.out.print("|"+date1+"                      ");
            	}
            	if(eventExists && !eventArray[cal.get(cal.MONTH)][date1-1].equals("")) {
            		eventDayArray[eventDay] = i;
            		eventCounter[eventTotal]=date1-1;
            		eventTotal++;
            		eventDay++;
                 }
               
               date1++;
            }
            // prints out correct blank squares after the last date of month printed
            else if(date1>daysMax){
            	System.out.print("|                       ");
            	
            }
            else {
            	System.out.print("|                      ");
            }
         } // End of single digit dates for loop
         
         System.out.println("|");
         int temp1 = firstDayOfWeek;
         // needed otherwise events would not print on the first of the month if
         // there were blank boxes that needed to be printed
         // so needed a separate counter to be done for events to be printed
         int temp2 = blankBoxes;
         // drawing inner blank boxes
         for (int i=1; i<=4; i++)
         {
        	blankBoxes=temp2;
        	firstDayOfWeek=temp1;
            for (int j=1; j<=7; j++)
            {
            	if(firstDayOfWeek>=cal.getActualMaximum(cal.DAY_OF_MONTH)) {
            		System.out.print("|                       ");
            	}
            	// if an event exists in the month, and the current counter for firstDayOfWeek is not an empty string
            	// and the firstDayOfWeek is not greater than how many days are in the current month
            	// and there are no more blank boxes to be printed, and it is the very first row of the empty part
            	// of the box
            	else if(eventExists && !eventArray[cal.get(cal.MONTH)][firstDayOfWeek-1].equals("")&&firstDayOfWeek<cal.getActualMaximum(cal.DAY_OF_MONTH)&&blankBoxes<=0&&i<2) {
            		// it will print the event of the current month of the current day that is being incremented over
            		System.out.printf("|%23s",eventArray[cal.get(cal.MONTH)][eventCounter[eventMover]]);
            		eventMover++;
            		
                 }
            	
            	
	          	 else {
	          		 System.out.print("|                       ");
	          	 }
            	// once blankBoxes hits 0, then the counter for the days
            	// can start to move
            	if(blankBoxes<=0) {
            		firstDayOfWeek++;
            	}
            	blankBoxes--;
            }
            
            System.out.println("|");
            
         } // End of drawing blank boxes
         // once the first week is over there are no more 
         // blank boxes to be drawn that will have a date
         // or an event so just sets them  to 0
         // to never be used again
         blankBoxes = 0;
         temp2 = 0;
         eventDay = 0;
         
      } // End of outer nested for loop for middle of calendar
           
      // Copied and pasted from top of code to complete calendar's bottom row.
      for (int i=1; i<=170; i++)
         {
            System.out.print("=");
         }
         
         System.out.print("=");
         System.out.println();
         
   } // End of drawRow method
   
   	// method for drawing the row to the specified output file, same as drawRow minus the new comments
   	public static void drawRowToFile(int row, Calendar cal, int month, File outp) throws FileNotFoundException {
	  PrintStream out = new PrintStream(new FileOutputStream(outp,true));
	  boolean eventExists = false;
	   int eventDay = 0;
	   int eventDayMover = 0;
	   int firstDayOfWeek = 0;
	   int[] eventCounter = new int[32];
	   int weekday;
	   int eventTotal = 0;
	   int eventMover = 0;
	   int[] eventDayArray = new int[32];
	   int temp = cal.get(cal.DAY_OF_MONTH); // saves current day of the month
	   cal.set(cal.DAY_OF_MONTH, 1);// sets current day of the month to first of the month
	   weekday = cal.get(cal.DAY_OF_WEEK);// grabs the first day of the month eg. Sunday, monday etc.
	   cal.set(cal.DAY_OF_MONTH, temp);// sets the day of the month back to original day
	   int date1 =1;
	   int flag1 = 1;
	   int daysMax = cal.getActualMaximum(cal.DAY_OF_MONTH);// grabs how many total days can be in the current month
	   flag1 = Math.abs(weekday - flag1);// to determine how many blank boxes to print at the beginning of the month
	   int blankBoxes = flag1;
	   // for loop that checks if there are any events this month
	   for(int i = 0; i<cal.getActualMaximum(cal.DAY_OF_MONTH); i++) {
		   
		   if(!eventArray[cal.get(cal.MONTH)][i].equals("")) {
			   
			   eventExists = true;
		   }
	   }
	   
     // Nested for loop to print middle of calendar
     for (int row2=1; row2<=row; row2++)
     {
        for (int i=1; i<=170; i++)
        {
           out.print("=");
        }
        
        
        out.println();
        
        
        // For loop to print row of single digit dates
        for (int i=1; i<=7; i++)
        {
       	 if(i==1) {
       		 firstDayOfWeek = date1; 
       	 }
       	 
           if (date1<=9 && flag1 != 0)
           {
              out.print("|                       ");
              flag1--;
           }
           // Else if statement that prints until 31 for double digits.
           else if (date1>=10 && date1<=daysMax)
           {
           	// if statement that prints out indication of the current day
           	if(date1==cal.get(cal.DAY_OF_MONTH)) {
           		out.print("|"+ date1 + "       today         ");
           	}
           	else {
           		out.print("|"+ date1 + "                     ");
           	}
           	
           	if(eventExists && !eventArray[cal.get(cal.MONTH)][date1-1].equals("")) {
            	   eventDayArray[eventDay] = i;
            	   eventCounter[eventTotal]=date1-1;
            	   eventTotal++;
            	   eventDay++;
               }
              date1++;
           }
           // else if for the indication of the current day for single digit dates
           else if(date1<=9 && flag1==0)
           {
           	if(date1==cal.get(cal.DAY_OF_MONTH)) {
           		out.print("|"+date1+"        today         ");
           	}
           	else {
           		out.print("|"+date1+"                      ");
           	}
           	if(eventExists && !eventArray[cal.get(cal.MONTH)][date1-1].equals("")) {
           		eventDayArray[eventDay] = i;
           		eventCounter[eventTotal]=date1-1;
           		eventTotal++;
           		eventDay++;
                }
              
              date1++;
           }
           // prints out correct blank squares after the last date of month printed
           else if(date1>daysMax){
           	out.print("|                       ");
           	
           }
           else {
           	out.print("|                      ");
           }
        } // End of single digit dates for loop
        
        out.println("|");
        int temp1 = firstDayOfWeek;
        int temp2 = blankBoxes;
        // drawing inner blank boxes
        for (int i=1; i<=4; i++)
        {
       	blankBoxes=temp2;
       	firstDayOfWeek=temp1;
           for (int j=1; j<=7; j++)
           {
           	if(firstDayOfWeek>=cal.getActualMaximum(cal.DAY_OF_MONTH)) {
           		out.print("|                       ");
           	}
           	else if(eventExists && !eventArray[cal.get(cal.MONTH)][firstDayOfWeek-1].equals("")&&firstDayOfWeek<cal.getActualMaximum(cal.DAY_OF_MONTH)&&blankBoxes<=0&&i<2) {
           		out.printf("|%23s",eventArray[cal.get(cal.MONTH)][eventCounter[eventMover]]);
           		eventMover++;
           		
                }
           	
           	
	          	 else {
	          		 out.print("|                       ");
	          	 }
           	
           	if(blankBoxes<=0) {
           		firstDayOfWeek++;
           	}
           	blankBoxes--;
           }
           
           out.println("|");
           
        } // End of drawing blank boxes
        blankBoxes = 0;
        temp2 = 0;
        eventDay = 0;
        
     } // End of outer nested for loop for middle of calendar
          
     // Copied and pasted from top of code to complete calendar's bottom row.
     for (int i=1; i<=170; i++)
        {
           out.print("=");
        }
        
        out.print("=");
        out.println();
        out.close();
  }
   
   	//Method to print the top portion of the calendar art
   	public static void art() {
        for(int i = 1;i<=2;i++) {
            System.out.println("    ****   ****    ");
        }
        System.out.println("         _");
        System.out.printf("        / \\      ***\n     --     --    \n     \\       /*  **\n      \\     /\n      /     \\\n     /__/ \\__\\");
        System.out.println();
        
        for(int j = 5; j >=0;j--) {
            System.out.print("       ");
        for(int i = j; i >=0;i--) {
            System.out.print("*");
        }
        System.out.println();
        }
    }
   
   	// method to print the top portion of the calendar art to a file
   	public static void artToFile(File outp) throws FileNotFoundException {
    	PrintStream out = new PrintStream(outp);
    	for(int i = 1;i<=2;i++) {
            out.println("    ****   ****    ");
        }
        out.println("         _");
        out.printf("        / \\      ***\n     --     --    \n     \\       /*  **\n      \\     /\n      /     \\\n     /__/ \\__\\");
        out.println();
        
        for(int j = 5; j >=0;j--) {
            out.print("       ");
        for(int i = j; i >=0;i--) {
            out.print("*");
        }
        out.println();
        }
        out.close();
    }
    
   	// Method to display given month and date
   	public static void displayDate(int month, int day)
   {
      System.out.println("Month: "+ month);
      System.out.print("Day: "+ day);
      System.out.println();
   }
   
   	// Method to convert String to integer for month
   	public static int monthFromDate(String date)
   {
      String munth;
      munth = date.substring(0,2); // Creates a substring from date, that only grabs first 2 chars of date.     
      return Integer.parseInt(munth);
   }
   
   	// Method to convert String to integer for date
   	public static int dayFromDate(String date)
   {
      String day;
      day = date.substring(3); // Creates a substring from date, that only grabs first 2 chars of date.
      return Integer.parseInt(day);
   }
   
   	// method to print the menu for the user to choose from
   	public static void menu() {
	   System.out.printf("Please type a command\n\t\"e\" to enter a date and display the corresponding calendar\n");
	   System.out.printf("\t\"t\" to get todays date and display today's calendar\n");
	   System.out.printf("\t\"n\" to display the next month\n");
	   System.out.printf("\t\"p\" to display the previous month\n");
	   System.out.printf("       \"ev\" to enter an event\n");
	   System.out.printf("       \"fp\" to print a month to a file\n");
	   System.out.printf("\t\"q\" to quit the program\n");
   }
   
   	// Method to draw out the calendar commands when necessary
   	public static void runCalendar(int MM, int DD, Calendar cal) {
	   art();
	   drawMonth(MM,cal);
	   displayDate(MM,DD);
   }
   
   	// method to load the specified file into the calendar to populate it with events
   	public static void loadFileToCalendar(File inp) throws FileNotFoundException {
	   String date;
	   int MM;
	   int DD;
	   String event;
	   String eventTitle;
	   Scanner inputRead = new Scanner(inp);
	   // reads from the file as long as there is a line left in the file to scan
	   while(inputRead.hasNextLine()) {
		   event = inputRead.nextLine();
		   date = event.substring(0,5);
		   MM = monthFromDate(date);
		   DD = dayFromDate(date);
		   
		   // replaces the _ with spaces for the event names
		   eventTitle = event.substring(6).replace("_", " ");
		   eventArray[MM-1][DD-1] = eventTitle;
	   }
	  
   }
   
   	// method to print the specified month to the output file
   	public static void printToFile(File outp, int MMFilePrint, Calendar cal) throws FileNotFoundException{
	   artToFile(outp);
	   drawMonthToFile(MMFilePrint,  cal, outp);
	   
   }
   
   
   
} // End of Assign. 1 Calendar 1
