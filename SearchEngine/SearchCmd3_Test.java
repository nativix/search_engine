import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class SearchCmdTest {
	
	@Test
	public void testExists(){
		WORDlist[] wl = new WORDlist[2];
		wl[0]=new WORDlist("foo",new URLlist("*PAGE:www.itu.dk",null),null);
		wl[1]=new WORDlist("bar",new URLlist("*PAGE:www.itu.dk",new URLlist("*PAGE:www.bar.dk",null)),new WORDlist("IT",new URLlist("*PAGE:www.itu.dk",null),null));
		
		if(Searcher.exists(wl, "IT")==false)
			fail("The word \"IT\" is in hash table");
		if(Searcher.exists(wl, "monkey"))
			fail("The word \"monkey\" is in NOT hash table");
	}
	
	@Test
	public void textWhichurl(){
		WORDlist[] wl = new WORDlist[2];
		wl[0]=new WORDlist("foo",new URLlist("*PAGE:http://www.itu.dk",null),null);
		wl[1]=new WORDlist("bar",new URLlist("*PAGE:http://www.itu.dk",new URLlist("*PAGE:http://www.bar.dk",null)),new WORDlist("IT",new URLlist("*PAGE:http://www.itu.dk",null),null));
		
		Searcher.whichUrl(wl, "foo");
		Searcher.whichUrl(wl, "bar");
	}
	
	@Test
	public void testReadHTMLlist() throws IOException{
		WORDlist[] wl = Searcher.readHtmlList3("itcwww-micro.txt");
		
		if(Searcher.exists(wl, "IT")==false)
			fail("The word \"IT\" is in the file");
		if(Searcher.exists(wl, "foo"))
			fail("The word \"foo\" is not in the file");
		
		Searcher.whichUrl(wl, "Home");
		Searcher.whichUrl(wl, "IT");
		
		int count  = 0;
		for(int i=0; i<wl.length; i++){
			while(wl[i]!=null){
				count++;
				wl[i] = wl[i].next;
			}
		}
		if(count != 17)
			fail("There should be 25 lines in the file");	
	}
}
