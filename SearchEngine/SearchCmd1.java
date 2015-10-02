/* SearchCmd.java
   Written by Rune Hansen
   Updated by Alexandre Buisse <abui@itu.dk>
*/

import java.io.*;

class HTMLlist {
    String str;
    HTMLlist next;

    HTMLlist (String s, HTMLlist n) {
        str = s;
        next = n;
    }
}

class Searcher {

    public static boolean exists (HTMLlist l, String word) {
        while (l != null) {
            if (l.str.equals (word)) {
                return true;
            }
            l = l.next;
        }
        return false;
    }
    
    public static void where(HTMLlist l, String word){
    	Timer t = new Timer();
    	t.startTimer();
    	String url = null;
    	boolean seenBefore = false;
    	while(l != null){
    		if(l.str.startsWith("*PAGE:")){
    			seenBefore = false;
    			url=l.str.substring(6);
    		}
    		else if(l.str.equals(word) && seenBefore==false){
    			System.out.println("The word \""+word+"\" has been found. In: "+url);
    			seenBefore = true;
    		}
    		l=l.next;
    	}
    	t.endTimer("Found word and URL's");
    }

    public static HTMLlist readHtmlList (String filename) throws IOException {
        String name;
        HTMLlist start, current, tmp;

        // Open the file given as argument
        BufferedReader infile = new BufferedReader (new FileReader (filename));

        name = infile.readLine(); //Read the first line
        start = new HTMLlist (name, null);
        current = start;
        name = infile.readLine(); // Read the next line
        while (name != null) {    // Exit if there is none
            tmp = new HTMLlist (name, null);
            current.next = tmp;
            current = tmp;            // Update the linked list
            name = infile.readLine(); // Read the next line
        }
        infile.close(); // Close the file

        return start;
    }
}

public class SearchCmd {

    public static void main (String[] args) throws IOException {
        String name;

        // Check that a filename has been given as argument
        if (args.length != 1) {
            System.out.println ("Usage: java SearchCmd <datafile>");
            System.exit (1);
        }
        Timer t=new Timer();
        t.startTimer();
        // Read the file and create the linked list
        HTMLlist l = Searcher.readHtmlList (args[0]);
        t.endTimer("Read the file and create the linked list");
        // Ask for a word to search
        BufferedReader inuser =
            new BufferedReader (new InputStreamReader (System.in));

        System.out.println ("Hit return to exit.");
        while (true) {
            System.out.print ("Search for: ");
            name = inuser.readLine(); // Read a line from the terminal
            if (name == null || name.length() == 0) {
                return;
            } else if (Searcher.exists (l, name)) {
                Searcher.where(l, name);
            } else {
                System.out.println ("The word \""+name+"\" has NOT been found.");
            }
        }
    }
}

class Timer{
	private long startTime, endTime;
	
	void startTimer(){
		startTime=System.currentTimeMillis();
	}
	void endTimer(String s){
		endTime=System.currentTimeMillis();
		System.out.println(s+" took "+(endTime-startTime)+"ms");
	}

}