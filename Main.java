import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException{
        File file = new File("minutes.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        //many arraylists and temporary files to contain all information
        ArrayList<String> attendance = new ArrayList<>();
        ArrayList<String> tasks = new ArrayList<>();
        ArrayList<String> assignTasks = new ArrayList<>();
        //ArrayList<String> dueTasks = new ArrayList<>();
        File Notes = new File("MeetingNotes.txt");
        ArrayList<String> unresolvedDiscussion = new ArrayList<>();

        //it's beneficial to have a variable for strings as a place holder. 
        String command = "";
        
        //takes in the start time of the secretary 
        System.out.print("What time is it (start time): ");
        String startTime = in.readLine().strip();

        //showcase all the commands (commands start with "fj") 
        //When a command is called, then it will remain on that command until changed otherwise. 
        //When a one-time command is called, automatically change to fjn mode  
        //please note that any names or letters that start with "fj" will count it as a command
        /*
        fja = attendance (in case someone happens to walk in)
        fjt = new task to accomplish 
        fjn = note (* for important notes)
        fjsn = show current notes (*indicates important notes, not yet implemented on how it can ever become useful though)
        fjst = show current tasks 
        fjat = assign a person to a task
        fju = unresolved discussion 
        fjc = show available commands 
        fjend = initiate the end sequence 
        */

        command = in.readLine().strip();

        do{
            switch(command){
                case "fja":
                    command = attendance(attendance, in);
                    break;
                case "fjt":
                    command = addTask(tasks, assignTasks, in);
                    break;
                case "fjn":
                    command = note(Notes, in);
                    break;
                case "fjsn":
                    showCurrentNotes(Notes);
                    System.out.print("Enter a command: ");
                    command = in.readLine().strip();
                    break;
                case  "fjst":
                    showCurrentTasks(tasks, assignTasks);
                    System.out.print("Enter a command: ");
                    command = in.readLine().strip();
                    break;
                case "fjat":
                    command = assignTask(tasks, assignTasks, in);
                    break;
                case "fju":
                    command = unresolvedDiscussion(unresolvedDiscussion, in);
                    break;
                case "fjsu":
                    showUnresolvedDiscussion(unresolvedDiscussion);
                    System.out.print("Enter a command: ");
                    command = in.readLine().strip();
                    break;
                case "fjau":
                    markUnresolvedDiscussion(unresolvedDiscussion, in);
                    break;
                case "fjend":
                    System.out.print("Are you sure you want to end? (Y/N): ");
                    if(in.readLine().strip().toLowerCase().charAt(0) == 'y'){
                        end(in, file, startTime, attendance, Notes, tasks, assignTasks, unresolvedDiscussion);
                    }else{
                        System.out.print("Enter a command: ");
                        command = in.readLine().strip();
                    }
                    break;
                default:
                    command = note(Notes, in);
                    break;
            }
        }while(!command.equals("fjend"));

        System.out.println(attendance);
        System.out.println(tasks);
        System.out.println(assignTasks);
    }

    //attendance method 
    static String attendance(ArrayList<String> attendance, BufferedReader in) throws IOException{
        String temp = "";
        
        //instructions: 
        System.out.println("ATTENDANCE: (please enter each name on separate lines)");

        //loop until the user gets out of the attendance loop
        do{
            temp = in.readLine().strip();
            //if it is not a command that is entered: 
            if(!temp.contains("fj")){
                //and that the attendance array doesn't already have that name in it: 
                if(!attendance.contains(temp)){
                    attendance.add(temp);
                }else{
                    System.out.println("Name already exists");
                }
            }
        }while(!temp.contains("fj"));

        return temp;
    }

    static String addTask(ArrayList<String> tasks, ArrayList<String> assignTasks, BufferedReader in) throws IOException{
        String temp = "";

        //instructions: 
        System.out.println("ADD TASK: (enter needed tasks in separate lines, type letter \"=\" to assign on the same line)");

        //loop until the user gets out of the loop 
        do{
            temp = in.readLine().strip();
            if(temp.contains("=")){
                tasks.add((temp.substring(0, temp.indexOf("="))).strip());
                assignTasks.add((temp.substring(temp.indexOf("=") + 1)).strip());
            }else{
                tasks.add(temp);
                assignTasks.add("unassigned");
            }
        }while(!temp.contains("fj"));
        
        return temp;
    }

    static String note(File Notes, BufferedReader in) throws IOException{
        PrintWriter out = new PrintWriter(Notes);
        String temp = "";

        //instructions: 
        System.out.println("NOTES: ");

        //loop until the user gets out of the loop 
        while(!temp.contains("fj")){
            temp = in.readLine();
            if(!temp.contains("fj")){
                out.println(temp);
            }
            
        }
        
        out.close();
        return temp;
    }

    static void showCurrentNotes(File Notes) throws IOException{
        BufferedReader read = new BufferedReader(new FileReader(Notes));
        String temp;
        
        //description
        System.out.println("CURRENT NOTES:");

        //print out all the items in the notes 
        while((temp = read.readLine()) != null){
            System.out.println(temp);
        }
        System.out.println("/*end of file*/");

        read.close();
    }

    static void showCurrentTasks(ArrayList<String> tasks, ArrayList<String> assignTasks) {        
        //description
        System.out.println("CURRENT TASKS: ");

        //print out all the items stored in tasks as well as who is assigned to them
        for(int i = 0; i < tasks.size(); i++){
            System.out.println(tasks.get(i) + " - " + assignTasks.get(i));
        }

        System.out.println("/*end of tasks*/");
    }

    static String assignTask(ArrayList<String> tasks, ArrayList<String> assignTasks, BufferedReader in) throws IOException{
        String temp = "";

        //instructions: 
        System.out.println("ASSIGN TASK: (enter the number followed by a \"-\" and the name) [e.g.: 1 - John Smith]");

        //loop until the user gets out of the loop 
        do{
            if(!temp.contains("fj")){
                showCurrentTasks(tasks, assignTasks);
                temp = in.readLine().strip();
                int num = Integer.parseInt(temp.substring(0, 1));
                if(temp.contains("-")){
                    assignTasks.set(num, (temp.substring(temp.indexOf("-") + 1)).strip());
                }
            }
        }while(!temp.contains("fj"));

        return temp;
    }

    static String unresolvedDiscussion(ArrayList<String> unresolvedDiscussion, BufferedReader in) throws IOException{
        String temp = "";

        //instructions: 
        System.out.println("UNRESOLVED DISCUSSION: ");

        //loop until the user gets out of the  loop
        do{
            temp = in.readLine().strip();
            //if it is not a command that is entered: 
            if(!temp.contains("fj")){
                unresolvedDiscussion.add(temp);
            }
        }while(!temp.contains("fj"));

        return temp;
    }

    static void showUnresolvedDiscussion(ArrayList<String> unresolvedDiscussion) {        
        //description
        System.out.println("CURRENT UNRESOLVED DISCUSSIONS: ");

        //print out all the items stored in the unresolved discussion array 
        for(int i = 0; i < unresolvedDiscussion.size(); i++){
            System.out.println(unresolvedDiscussion.get(i));
        }

        System.out.println("/*end of unresolved discussion*/");
    }

    static String markUnresolvedDiscussion(ArrayList<String> unresolvedDiscussion, BufferedReader in) throws IOException{
        String temp = "";

        //instructions: 
        System.out.println("MARK UNRESOLVED DISCUSSION: (enter the number)");

        //loop until the user gets out of the loop 
        do{
            showUnresolvedDiscussion(unresolvedDiscussion);
            temp = in.readLine();
            if(!temp.contains("fj")){
                unresolvedDiscussion.remove(Integer.parseInt(temp));
            }
        }while(!temp.contains("fj"));

        return temp;
    }

    static void end(BufferedReader in, File file, String startTime, ArrayList<String> attendance, File notes, ArrayList<String> tasks, ArrayList<String> assignedTasks, ArrayList<String> unresolvedDiscussion) throws IOException{
        PrintWriter out = new PrintWriter(file);
        //last questions: 
        System.out.print("When is the next meeting? (Month date, year, at time): ");
        String nextMeetingDate = in.readLine();
        System.out.print("What time did the meeting end?: ");
        String endTime = in.readLine();
        System.out.print("Who presided?: ");
        String presided = in.readLine();
        System.out.print("What is your name as secretary?: ");
        String secretary = in.readLine();
        System.out.print("What is the date?(Month day, year): ");
        String date = in.readLine();
        System.out.print("Where did this meeting take place?: ");
        String place = in.readLine();
        System.out.print("What was this meeting for? (e.g. Weekly Meeting of Board of Directors): ");
        String why = in.readLine();
        System.out.print("What is the organization?: ");
        String org = in.readLine();

        //figure out how to format this in java, but print the title, hoepfully in all caps, and somehow make it centered (have not figured that out yet), and all in uppercase
        out.println(org.toUpperCase());
        out.println();
        //then print out Minutes
        out.println("Minutes");
        out.println();
        //then print the why 
        out.println(why);
        out.println();
        //then print out the date 
        out.println(date);
        out.println();
        out.println();
        out.println();
        //then print the location and all that stuff 
        out.println("\tThe " + why + " was called to order at " + place + " at " + startTime + ".");
        out.println();
        //then print out the attendance record: 
        out.print("\tAttendees: ");
        out.print(attendance.get(0));
        for(int i = 1; i < attendance.size(); i++){
            out.print(", " + attendance.get(i));
        }
        out.println();
        //then print who presided 
        out.println(presided + " presided and " + secretary + " recorded the proceedings of the meeting.");
        out.println();
        //then print out the notes 
        showCurrentNotes(notes);
        out.println();
        //any tasks assigned 
        showCurrentTasks(tasks, assignedTasks);
        //any unresolved discussions: 
        showUnresolvedDiscussion(unresolvedDiscussion);
        //when the next meeting is going to be 
        out.println("The next meeting will be held on " + nextMeetingDate);
        out.println();
        //adjournemnt: 
        out.println("There being no further business, the meeting was adjourned at " + endTime);
        out.println();
        out.println();
        out.println();

        out.close();
    }
}
