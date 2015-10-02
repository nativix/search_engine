import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;


public class SearchEnginePart2 {
	@Test
	public void testExists(){
		WORDlist hl = new WORDlist("foo",new URLlist("ITU.dk",null), new WORDlist("bar", new URLlist("Something.dk",null),new WORDlist("IT",new URLlist("SomethingElse.dk",null),null)));
		if(Searcher.exists(hl, "IT")==false)
				fail("IT is in the list");
	}
	
	@Test
	public void testWhichUrl(){
		WORDlist hl = new WORDlist("foo",new URLlist("ITU.dk",null), new WORDlist("bar", new URLlist("Something.dk",null),new WORDlist("IT",new URLlist("ssssssSomethingElse.dk",new URLlist("ssssssHereaswell.dk",null)),null)));
		Searcher.whichUrl(hl, "foo");
		Searcher.whichUrl(hl, "IT");
		System.out.println("\n");
	}
	
	
	@Test
	public void testAddUrl(){
		WORDlist wl = new WORDlist("bigfoot",new URLlist("*PAGE:www.itu.dk",null),null);
		
		Searcher.addUrlList(wl, new URLlist("*PAGE:www.itu.dk/bigfootexists",null));
		Searcher.addUrlList(wl, new URLlist("*PAGE:www.itu.dk",null));
		Searcher.whichUrl(wl, "bigfoot");
	}
	
	
	
	@Test
	public void testReadHtmlList2() throws IOException{
		WORDlist l = Searcher.readHtmlList2("itcwww-micro.txt");
		
		if(Searcher.exists(l, "IT")==false)
			fail("The word \"IT\" is in the file");
		if(Searcher.exists(l, "foo"))
			fail("The word \"foo\" is not in the file");
		System.out.println("The word \"Home\" should be in three URLs");
		Searcher.whichUrl(l, "Home");
		System.out.println("\nThe word \"IT\" should be in two URLs");
		Searcher.whichUrl(l, "IT");
		
		int count  = 0;
		while(l != null){
			count++;
			l = l.next;
		}
		if(count != 17)
			fail("There should be 25 lines in the file");
	}

}