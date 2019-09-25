package debxquery.debxquery;

import java.io.IOException;

import debxquery.debxquery.BaseXClient.Query;




public class Debugger {

	
	public static void main(final String... args) throws IOException {
	    // create session
	    try(BaseXClient session = new BaseXClient("localhost", 1984, "admin", "admin")) {
	      // create query instance
	      final String input = "xquery:parse(\"let $i := 1  return <xml>Text { 8 }</xml>\")";

	      try(Query query = session.query(input)) {
	        // loop through all results
	        while(query.more()) {
	          System.out.println(query.next());
	        }

	        // print query info
	        System.out.println(query.info());
	      }
	    }
	  }
}
