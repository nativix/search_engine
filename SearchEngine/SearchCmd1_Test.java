import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;


public class SearchCmdTest {
	@Test
	public void testExists(){
		HTMLlist hl = new HTMLlist("foo",new HTMLlist("bar",new HTMLlist("IT",null)));
		if(Searcher.exists(hl, "IT")==false)
				fail("IT is in the list");
	}
	
	@Test
	public void testWhere(){
		HTMLlist hl = new HTMLlist("*PAGE:www.itu.dk",new HTMLlist("foo",new HTMLlist("*PAGE:www.itu.dk/IT",new HTMLlist("IT",null))));
		Searcher.where(hl, "foo");
		Searcher.where(hl, "IT");
		System.out.println("\n");
	}
	
	@Test
	public void testReadHTMLlist() throws IOException{
		HTMLlist l = Searcher.readHtmlList("itcwww-micro.txt");
		
		if(Searcher.exists(l, "IT")==false)
			fail("The word \"IT\" is in the file");
		if(Searcher.exists(l, "foo"))
			fail("The word \"foo\" is not in the file");
		
		System.out.println("The word \"Home\" should be in three URLs");
		Searcher.where(l, "Home");
		System.out.println("\nThe word \"IT\" should be in two URLs");
		Searcher.where(l, "IT");
		
		int count  = 0;
		while(l != null){
			count++;
			l = l.next;
		}
		if(count != 25)
			fail("There should be 25 lines in the file");
	}
}
