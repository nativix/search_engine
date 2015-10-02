/* SearchCmd.java
   Written by Rune Hansen
   Updated by Alexandre Buisse <abui@itu.dk>
*/
import java.io.*;

class WORDlist{
	String word;
	URLlist urllist;
	WORDlist next;
	
	WORDlist(String w, URLlist u, WORDlist l){
		word = w;
		urllist = u;
		next = l;
	}
}
class URLlist{
	String url;
	URLlist next;
	
	URLlist(String u, URLlist l){
		url = u;
		next = l;
	}
}

class Searcher {
    public static boolean exists (WORDlist l, String word) {
        while (l != null) {
            if (l.word.equals (word)) {
                return true;
            }
            l = l.next;
        }
        return false;
    }
    
    public static void whichUrl(WORDlist l, String word){
    	Timer t = new Timer();
    	t.startTimer();
    	while (l != null) {
    		if (l.word.equals(word)) {
    			URLlist tmp = l.urllist;
    			while(tmp != null){
    				System.out.println("The word \""+word+"\" was found in "+tmp.url.substring(6));
    				tmp = tmp.next;
    			}
    		}
    		l = l.next;
    	}
    	t.endTimer("Found words in URLs");
    }
    
    public static WORDlist readHtmlList2 (String filename) throws IOException{
    	String read;
    	WORDlist startW, currentW, tmpW;
    	URLlist startU;
  
    	// Open the file given as argument
        BufferedReader infile = new BufferedReader (new FileReader (filename));
        read = infile.readLine();
        
        startU = new URLlist(read, null);
        read = infile.readLine();
        startW = new WORDlist(read, startU, null);
        currentW = startW;
        read = infile.readLine();
        
        
        while (read != null){
        	if (read.startsWith("*PAGE:")){
        		startU = new URLlist(read,null);
        	}
        	else{
        		tmpW=getWordList(read, startW);
        		if(tmpW != null){
        			tmpW.urllist=addUrlList(tmpW, startU);
        		}
        		else{
	        		tmpW = new WORDlist(read,startU,null);
	        		currentW.next = tmpW;
	        		currentW = tmpW;
        		}       		
        	}
        	read = infile.readLine(); 
        }        
        
        infile.close();
        return startW;
    }
    
    public static WORDlist getWordList(String word, WORDlist l){
    	while (l != null) {
    		if (l.word.equals(word)) {
    			return l;
    		}
    		l = l.next;
    	}
    	return null;
    }
    public static URLlist addUrlList(WORDlist tmpW, URLlist currentU){
    	URLlist ul = tmpW.urllist;
    	while(ul != null){
    		if(ul.url.equals(currentU.url)){
    			return tmpW.urllist;
    		}
    		ul = ul.next;
    	}
    	URLlist tmpU = new URLlist(currentU.url,null);
    	tmpU.next=tmpW.urllist;
    	tmpW.urllist=tmpU;
    	return tmpW.urllist;
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
        Timer t = new Timer();
        t.startTimer();
        WORDlist wl = Searcher.readHtmlList2 (args[0]); // Read the file and create the linked list
        t.endTimer("Create the linked list");
        // Ask for a word to search
        BufferedReader inuser =
            new BufferedReader (new InputStreamReader (System.in));
        System.out.println ("Hit return to exit.");
        while (true) {
            System.out.print ("Search for: ");
            name = inuser.readLine(); // Read a line from the terminal
            if (name == null || name.length() == 0) {
                return;
            } 
            else if (Searcher.exists(wl, name)) { //Checks if the word exists in the wordlist.
            	Searcher.whichUrl(wl, name); //Prints to console the URLs 
            } 
            else {
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