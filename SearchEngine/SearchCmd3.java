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
    public static boolean exists (WORDlist[] l, String word) {
    	int index = Math.abs(word.hashCode()%l.length);
    	WORDlist tmp = l[index];
    	while(tmp!=null){
    		if(tmp.word.equals(word)){
    			return true;
    		}
    		tmp = tmp.next;
    	}
    	return false;
    }
    
    public static void whichUrl(WORDlist[] l, String word){
    	int index = Math.abs(word.hashCode()%l.length);
    	WORDlist tmpW = l[index];
    	while (tmpW != null) {
    		if (tmpW.word.equals(word)) {
    			URLlist tmp = tmpW.urllist;
    			while(tmp != null){
    				System.out.println("The word \""+word+"\" was found in "+tmp.url.substring(6));
    				tmp = tmp.next;
    			}
    		}
    		tmpW = tmpW.next;
    	}
    }
    
    public static WORDlist[] readHtmlList3 (String filename) throws IOException{
    	String read;
    	WORDlist tmpW;
    	URLlist startU = null;
    	
    	int arraySize = 10;
    	WORDlist[] hashtable = new WORDlist[arraySize];
  
    	// Open the file given as argument
        BufferedReader infile = new BufferedReader (new FileReader (filename));
        read = infile.readLine();
        
        while (read != null){
        	if (read.startsWith("*PAGE:")){
        		startU = new URLlist(read,null);
        	}
        	else{
        		int index = Math.abs(read.hashCode()%arraySize);
        				
        		if(hashtable[index]==null){
        			hashtable[index]=new WORDlist(read, startU, null);
        		}
        		else{

        			tmpW=getWordList(read, hashtable[index]);
        			if(tmpW != null){
        				tmpW.urllist=addUrlList(tmpW, startU);
        			}
        			else{
        				tmpW  = new WORDlist(read, startU, null);
            			tmpW.next = hashtable[index];
            			hashtable[index] = tmpW;
        			}
        		}
        	}
        	read = infile.readLine(); 
        }        
        
        infile.close();
        return hashtable;
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
        WORDlist[] wl = Searcher.readHtmlList3 (args[0]);
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