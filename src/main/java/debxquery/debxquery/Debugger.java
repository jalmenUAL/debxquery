package debxquery.debxquery;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import debxquery.debxquery.BaseXClient.Query;

public class Debugger {
	
	public static String load (String filename)
	{
	String fileAsString = "";
	InputStream is;
	try {
		is = new FileInputStream(filename);
	
	BufferedReader buf = new BufferedReader(new InputStreamReader(is));
	        
	String line;
	try {
		line = buf.readLine();
		StringBuilder sb = new StringBuilder();
		
		while(line != null){
			   sb.append(line).append("\n");
			   line = buf.readLine();
			}
		buf.close();	        
		    fileAsString = sb.toString();
			//System.out.println("Contents : " + fileAsString);
			
			
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return fileAsString;
	}

	 
	public static void main(final String... args) throws IOException {

		String b1store = "<bib>\r\n" + "    <book year=\"1994\">\r\n" + "        <title>TCP/IP Illustrated</title>\r\n"
				+ "        <author><last>Stevens</last><first>W.</first></author>\r\n"
				+ "        <publisher>Addison-Wesley</publisher>\r\n" + "        <price>65.95</price>\r\n"
				+ "    </book>\r\n" + " \r\n" + "    <book year=\"1992\">\r\n"
				+ "        <title>Advanced Programming in the Unix environment</title>\r\n"
				+ "        <author><last>Stevens</last><first>W.</first></author>\r\n"
				+ "        <publisher>Addison-Wesley</publisher>\r\n" + "        <price>65.95</price>\r\n"
				+ "    </book>\r\n" + " \r\n" + "    <book year=\"2000\">\r\n"
				+ "        <title>Data on the Web</title>\r\n"
				+ "        <author><last>Abiteboul</last><first>Serge</first></author>\r\n"
				+ "        <author><last>Buneman</last><first>Peter</first></author>\r\n"
				+ "        <author><last>Suciu</last><first>Dan</first></author>\r\n"
				+ "        <publisher>Morgan Kaufmann Publishers</publisher>\r\n" + "        <price>39.95</price>\r\n"
				+ "    </book>\r\n" + " \r\n" + "    <book year=\"1999\">\r\n"
				+ "        <title>The Economics of Technology and Content for Digital TV</title>\r\n"
				+ "        <editor>\r\n" + "               <last>Gerbarg</last><first>Darcy</first>\r\n"
				+ "                <affiliation>CITI</affiliation>\r\n" + "        </editor>\r\n"
				+ "            <publisher>Kluwer Academic Publishers</publisher>\r\n"
				+ "        <price>129.95</price>\r\n" + "    </book>\r\n" + " \r\n" + "</bib>";

		String b2store = "<reviews>\r\n" + "    <entry>\r\n" + "        <title>Data on the Web</title>\r\n"
				+ "        <price>34.95</price>\r\n" + "        <review>\r\n"
				+ "               A very good discussion of semi-structured database\r\n"
				+ "               systems and XML.\r\n" + "        </review>\r\n" + "    </entry>\r\n"
				+ "    <entry>\r\n" + "        <title>Advanced Programming in the Unix environment</title>\r\n"
				+ "        <price>65.95</price>\r\n" + "        <review>\r\n"
				+ "               A clear and detailed discussion of UNIX programming.\r\n" + "        </review>\r\n"
				+ "    </entry>\r\n" + "    <entry>\r\n" + "        <title>TCP/IP Illustrated</title>\r\n"
				+ "        <price>65.95</price>\r\n" + "        <review>\r\n"
				+ "               One of the best books on TCP/IP.\r\n" + "        </review>\r\n" + "    </entry>\r\n"
				+ "</reviews>";

		String books = "<chapter>\r\n" + "    <title>Data Model</title>\r\n" + "    <section>\r\n"
				+ "        <title>Syntax For Data Model</title>\r\n" + "    </section>\r\n" + "    <section>\r\n"
				+ "        <title>XML</title>\r\n" + "        <section>\r\n"
				+ "            <title>Basic Syntax</title>\r\n" + "        </section>\r\n" + "        <section>\r\n"
				+ "            <title>XML and Semistructured Data</title>\r\n" + "        </section>\r\n"
				+ "    </section>\r\n" + "</chapter>";

		String prices = "<prices>\r\n" + "    <book>\r\n"
				+ "        <title>Advanced Programming in the Unix environment</title>\r\n"
				+ "        <source>bstore2.example.com</source>\r\n" + "        <price>65.95</price>\r\n"
				+ "    </book>\r\n" + "    <book>\r\n"
				+ "        <title>Advanced Programming in the Unix environment</title>\r\n"
				+ "        <source>bstore1.example.com</source>\r\n" + "        <price>65.95</price>\r\n"
				+ "    </book>\r\n" + "    <book>\r\n" + "        <title>TCP/IP Illustrated</title>\r\n"
				+ "        <source>bstore2.example.com</source>\r\n" + "        <price>65.95</price>\r\n"
				+ "    </book>\r\n" + "    <book>\r\n" + "        <title>TCP/IP Illustrated</title>\r\n"
				+ "        <source>bstore1.example.com</source>\r\n" + "        <price>65.95</price>\r\n"
				+ "    </book>\r\n" + "    <book>\r\n" + "        <title>Data on the Web</title>\r\n"
				+ "        <source>bstore2.example.com</source>\r\n" + "        <price>34.95</price>\r\n"
				+ "    </book>\r\n" + "    <book>\r\n" + "        <title>Data on the Web</title>\r\n"
				+ "        <source>bstore1.example.com</source>\r\n" + "        <price>39.95</price>\r\n"
				+ "    </book>\r\n" + "</prices>";

		String booksection = "<book>\r\n" + "  <title>Data on the Web</title>\r\n"
				+ "  <author>Serge Abiteboul</author>\r\n" + "  <author>Peter Buneman</author>\r\n"
				+ "  <author>Dan Suciu</author>\r\n" + "  <section id=\"intro\" difficulty=\"easy\" >\r\n"
				+ "    <title>Introduction</title>\r\n" + "    <p>Text ... </p>\r\n" + "    <section>\r\n"
				+ "      <title>Audience</title>\r\n" + "      <p>Text ... </p>\r\n" + "    </section>\r\n"
				+ "    <section>\r\n" + "      <title>Web Data and the Two Cultures</title>\r\n"
				+ "      <p>Text ... </p>\r\n" + "      <figure height=\"400\" width=\"400\">\r\n"
				+ "        <title>Traditional client/server architecture</title>\r\n"
				+ "        <image source=\"csarch.gif\"/>\r\n" + "      </figure>\r\n" + "      <p>Text ... </p>\r\n"
				+ "    </section>\r\n" + "  </section>\r\n" + "  <section id=\"syntax\" difficulty=\"medium\" >\r\n"
				+ "    <title>A Syntax For Data</title>\r\n" + "    <p>Text ... </p>\r\n"
				+ "    <figure height=\"200\" width=\"500\">\r\n"
				+ "      <title>Graph representations of structures</title>\r\n"
				+ "      <image source=\"graphs.gif\"/>\r\n" + "    </figure>\r\n" + "    <p>Text ... </p>\r\n"
				+ "    <section>\r\n" + "      <title>Base Types</title>\r\n" + "      <p>Text ... </p>\r\n"
				+ "    </section>\r\n" + "    <section>\r\n"
				+ "      <title>Representing Relational Databases</title>\r\n" + "      <p>Text ... </p>\r\n"
				+ "      <figure height=\"250\" width=\"400\">\r\n" + "        <title>Examples of Relations</title>\r\n"
				+ "        <image source=\"relations.gif\"/>\r\n" + "      </figure>\r\n" + "    </section>\r\n"
				+ "    <section>\r\n" + "      <title>Representing Object Databases</title>\r\n"
				+ "      <p>Text ... </p>\r\n" + "    </section>       \r\n" + "  </section>\r\n" + "</book> ";

		String report = "<report>\r\n" + "  <section>\r\n" + "    <section.title>Procedure</section.title>\r\n"
				+ "     <section.content>\r\n"
				+ "      The patient was taken to the operating room where she was placed\r\n"
				+ "      in supine position and\r\n"
				+ "      <anesthesia>induced under general anesthesia.</anesthesia>\r\n" + "      <prep> \r\n"
				+ "        <action>A Foley catheter was placed to decompress the bladder</action>\r\n"
				+ "        and the abdomen was then prepped and draped in sterile fashion.\r\n" + "      </prep>  \r\n"
				+ "      <incision>\r\n" + "        A curvilinear incision was made\r\n"
				+ "        <geography>in the midline immediately infraumbilical</geography>\r\n"
				+ "        and the subcutaneous tissue was divided\r\n"
				+ "        <instrument>using electrocautery.</instrument>\r\n" + "      </incision>\r\n"
				+ "      The fascia was identified and\r\n"
				+ "      <action>#2 0 Maxon stay sutures were placed on each side of the midline.\r\n"
				+ "      </action>\r\n" + "      <incision>\r\n" + "        The fascia was divided using\r\n"
				+ "        <instrument>electrocautery</instrument>\r\n" + "        and the peritoneum was entered.\r\n"
				+ "      </incision>\r\n" + "      <observation>The small bowel was identified.</observation>\r\n"
				+ "      and\r\n" + "      <action>\r\n" + "        the\r\n"
				+ "        <instrument>Hasson trocar</instrument>\r\n"
				+ "        was placed under direct visualization.\r\n" + "      </action>\r\n" + "      <action>\r\n"
				+ "        The\r\n" + "        <instrument>trocar</instrument>\r\n"
				+ "        was secured to the fascia using the stay sutures.\r\n" + "      </action>\r\n"
				+ "     </section.content>\r\n" + "  </section>\r\n" + "</report>";

		String q1 = "<bib>\r\n" + " {\r\n" + "  for $b in db:open(\"bstore1\")/bib/book\r\n"
				+ "  where $b/publisher = \"Addison-Wesley\" and $b/@year > 1991\r\n" + "  return\r\n"
				+ "    <book year=\"{ $b/@year }\">\r\n" + "     { $b/title }\r\n" + "    </book>\r\n" + " }\r\n"
				+ "</bib> ";

		String q2 = "<results>\r\n" + "  {\r\n" + "    for $b in db:open(\"bstore1\")/bib/book,\r\n"
				+ "        $t in $b/title,\r\n" + "        $a in $b/author\r\n" + "    return\r\n"
				+ "        <result>\r\n" + "            { $t }    \r\n" + "            { $a }\r\n"
				+ "        </result>\r\n" + "  }\r\n" + "</results>";

		String q3 = "<results>\r\n" + "{\r\n" + "    for $b in db:open(\"bstore1\")/bib/book\r\n" + "    return\r\n"
				+ "        <result>\r\n" + "            { $b/title }\r\n" + "            { $b/author  }\r\n"
				+ "        </result>\r\n" + "}\r\n" + "</results> ";

		String q4 = "<results>\r\n" + "  {\r\n" + "    let $a := db:open(\"bstore1\")//author\r\n"
				+ "    for $last in distinct-values($a/last),\r\n"
				+ "        $first in distinct-values($a[last=$last]/first)\r\n" + "    order by $last, $first\r\n"
				+ "    return\r\n" + "        <result>\r\n" + "            <author>\r\n"
				+ "               <last>{ $last }</last>\r\n" + "               <first>{ $first }</first>\r\n"
				+ "            </author>\r\n" + "            {\r\n"
				+ "                for $b in db:open(\"bstore1\")/bib/book\r\n"
				+ "                where some $ba in $b/author \r\n"
				+ "                      satisfies ($ba/last = $last and $ba/first=$first)\r\n"
				+ "                return $b/title\r\n" + "            }\r\n" + "        </result>\r\n" + "  }\r\n"
				+ "</results> ";

		String q5 = "<books-with-prices>\r\n" + "  {\r\n" + "    for $b in db:open(\"bstore1\")//book,\r\n"
				+ "        $a in db:open(\"bstore2\")//entry\r\n" + "    where $b/title = $a/title\r\n"
				+ "    return\r\n" + "        <book-with-prices>\r\n" + "            { $b/title }\r\n"
				+ "            <price-bstore2>{ $a/price/text() }</price-bstore2>\r\n"
				+ "            <price-bstore1>{ $b/price/text() }</price-bstore1>\r\n"
				+ "        </book-with-prices>\r\n" + "  }\r\n" + "</books-with-prices>";

		String q6 = "<bib>\r\n" + "  {\r\n" + "    for $b in db:open(\"bstore1\")//book\r\n"
				+ "    where count($b/author) > 0\r\n" + "    return\r\n" + "        <book>\r\n"
				+ "            { $b/title }\r\n" + "            {\r\n"
				+ "                for $a in $b/author[position()<=2]  \r\n" + "                return $a\r\n"
				+ "            }\r\n" + "            {\r\n" + "                if (count($b/author) > 2)\r\n"
				+ "                 then <et-al/>\r\n" + "                 else ()\r\n" + "            }\r\n"
				+ "        </book>\r\n" + "  }\r\n" + "</bib>";

		String q7 = "<bib>\r\n" + "  {\r\n" + "    for $b in db:open(\"bstore1\")//book\r\n"
				+ "    where $b/publisher = \"Addison-Wesley\" and $b/@year > 1991\r\n" + "    order by $b/title\r\n"
				+ "    return\r\n" + "        <book>\r\n" + "            { $b/@year }\r\n"
				+ "            { $b/title }\r\n" + "        </book>\r\n" + "  }\r\n" + "</bib> ";

		String q8 = "for $b in db:open(\"bstore1\")//book\r\n" + "let $e := $b/*[contains(string(.), \"Suciu\") \r\n"
				+ "               and ends-with(local-name(.), \"or\")]\r\n" + "where exists($e)\r\n" + "return\r\n"
				+ "    <book>\r\n" + "        { $b/title }\r\n" + "        { $e }\r\n" + "    </book> ";

		String q9 = "<results>\r\n" + "  {\r\n" + "    for $t in db:open(\"books\")//(chapter | section)/title\r\n"
				+ "    where contains($t/text(), \"XML\")\r\n" + "    return $t\r\n" + "  }\r\n" + "</results> ";

		String q10 = "<results>\r\n" + "  {\r\n" + "    let $doc := db:open(\"prices\")\r\n"
				+ "    for $t in distinct-values($doc//book/title)\r\n"
				+ "    let $p := $doc//book[title = $t]/price\r\n" + "    return\r\n"
				+ "      <minprice title=\"{ $t }\">\r\n" + "        <price>{ min($p) }</price>\r\n"
				+ "      </minprice>\r\n" + "  }\r\n" + "</results> ";

		String q11 = "<bib>\r\n" + "{\r\n" + "        for $b in db:open(\"bstore1\")//book[author]\r\n"
				+ "        return\r\n" + "            <book>\r\n" + "                { $b/title }\r\n"
				+ "                { $b/author }\r\n" + "            </book>\r\n" + "}\r\n" + "{\r\n"
				+ "        for $b in db:open(\"bstore1\")//book[editor]\r\n" + "        return\r\n"
				+ "          <reference>\r\n" + "            { $b/title }\r\n"
				+ "            {$b/editor/affiliation}\r\n" + "          </reference>\r\n" + "}\r\n" + "</bib>  ";

		String q12 = "<bib>\r\n" + "{\r\n" + "    for $book1 in db:open(\"bstore1\")//book,\r\n"
				+ "        $book2 in db:open(\"bstore1\")//book\r\n" + "    let $aut1 := for $a in $book1/author \r\n"
				+ "                 order by $a/last, $a/first\r\n" + "                 return $a\r\n"
				+ "    let $aut2 := for $a in $book2/author \r\n" + "                 order by $a/last, $a/first\r\n"
				+ "                 return $a\r\n" + "    where $book1 << $book2\r\n"
				+ "    and not($book1/title = $book2/title)\r\n" + "    and deep-equal($aut1, $aut2) \r\n"
				+ "    return\r\n" + "        <book-pair>\r\n" + "            { $book1/title }\r\n"
				+ "            { $book2/title }\r\n" + "        </book-pair>\r\n" + "}\r\n" + "</bib> ";

		String q13 = "declare function local:toc($book-or-section as element()) as element()*\r\n" + "{\r\n"
				+ "    for $section in $book-or-section/section\r\n" + "    return\r\n" + "      <section>\r\n"
				+ "         { $section/@* , $section/title , local:toc($section) }                 \r\n"
				+ "      </section>\r\n" + "};\r\n" + "\r\n" + "<toc>\r\n" + "   {\r\n"
				+ "     for $s in db:open(\"book\")/book return local:toc($s)\r\n" + "   }\r\n" + "</toc> ";

		String q14 = "<figlist>\r\n" + "  {\r\n" + "    for $f in db:open(\"book\")//figure\r\n" + "    return\r\n"
				+ "        <figure>\r\n" + "            { $f/@* }\r\n" + "            { $f/title }\r\n"
				+ "        </figure>\r\n" + "  }\r\n" + "</figlist> ";

		String q15 = "<section_count>{ count(db:open(\"book\")//section) }</section_count>, \r\n"
				+ "<figure_count>{ count(db:open(\"book\")//figure) }</figure_count>";

		String q16 = "<top_section_count>\r\n" + " { \r\n" + "   count(db:open(\"book\")/book/section) \r\n" + " }\r\n"
				+ "</top_section_count>";

		String q17 = "<section_list>\r\n" + "  {\r\n" + "    for $s in db:open(\"book\")//section\r\n"
				+ "    let $f := $s/figure\r\n" + "    return\r\n"
				+ "        <section title=\"{ $s/title/text() }\" figcount=\"{ count($f) }\"/>\r\n" + "  }\r\n"
				+ "</section_list>";

		String q18 = "declare function local:section-summary($book-or-section as element()*)\r\n"
				+ "  as element()*\r\n" + "{\r\n" + "  for $section in $book-or-section\r\n" + "  return\r\n"
				+ "    <section>\r\n" + "       { $section/@* }\r\n" + "       { $section/title }       \r\n"
				+ "       <figcount>         \r\n" + "         { count($section/figure) }\r\n"
				+ "       </figcount>                \r\n"
				+ "       { local:section-summary($section/section) }                      \r\n" + "    </section>\r\n"
				+ "};\r\n" + "\r\n" + "<toc>\r\n" + "  {\r\n" + "    for $s in db:open(\"book\")/book/section\r\n"
				+ "    return local:section-summary($s)\r\n" + "  }\r\n" + "</toc> ";

		String q19 = "for $s in db:open(\"report1\")//section[section.title = \"Procedure\"]\r\n"
				+ "return ($s//incision)[2]/instrument";

		String q20 = "for $s in db:open(\"report1\")//section[section.title = \"Procedure\"]\r\n"
				+ "return ($s//instrument)[position()<=2]";

		String q21 = "let $i2 := (db:open(\"report1\")//incision)[2]\r\n"
				+ "for $a in (db:open(\"report1\")//action)[. >> $i2][position()<=2]\r\n" + "return $a//instrument ";

		String q22 = "for $p in db:open(\"report1\")//section[section.title = \"Procedure\"]\r\n"
				+ "where not(some $a in $p//anesthesia satisfies\r\n" + "        $a << ($p//incision)[1] )\r\n"
				+ "return $p ";

		String q23 = "declare function local:precedes($a as node(), $b as node()) as xs:boolean \r\n" + "{\r\n"
				+ "    $a << $b\r\n" + "      and\r\n" + "    empty($a//node() intersect $b)\r\n" + "};\r\n" + "\r\n"
				+ "\r\n" + "declare function local:follows($a as node(), $b as node()) as xs:boolean \r\n" + "{\r\n"
				+ "    $a >> $b\r\n" + "      and\r\n" + "    empty($b//node() intersect $a)\r\n" + "};\r\n" + "\r\n"
				+ "<critical_sequence>\r\n" + " {\r\n"
				+ "  let $proc := db:open(\"report1\")//section[section.title=\"Procedure\"][1]\r\n"
				+ "  for $n in $proc//node()\r\n" + "  where local:follows($n, ($proc//incision)[1])\r\n"
				+ "    and local:precedes($n, ($proc//incision)[2])\r\n" + "  return $n\r\n" + " }\r\n"
				+ "</critical_sequence> ";

		String q24 = "declare function local:between($seq as node()*, $start as node(), $end as node())\r\n"
				+ " as item()*\r\n" + "{\r\n" + "  let $nodes :=\r\n" + "    for $n in $seq except $start//node()\r\n"
				+ "    where $n >> $start and $n << $end\r\n" + "    return $n\r\n"
				+ "  return $nodes except $nodes//node()\r\n" + "};\r\n" + "\r\n" + "<critical_sequence>\r\n" + " {\r\n"
				+ "  let $proc := db:open(\"report1\")//section[section.title=\"Procedure\"][1],\r\n"
				+ "      $first :=  ($proc//incision)[1],\r\n" + "      $second:=  ($proc//incision)[2]\r\n"
				+ "  return local:between($proc//node(), $first, $second)\r\n" + " }\r\n" + "</critical_sequence>";

		// create session
		try (BaseXClient session = new BaseXClient("localhost", 1984, "admin", "admin")) {

		    File initialFile2 = new File("mylist.xml");
		    InputStream mylist = new FileInputStream(initialFile2);
		    
		    File initialFile3 = new File("bstore.xml");
		    InputStream bstore = new FileInputStream(initialFile3);
		    
		    File initialFile4 = new File("prices.xml");
		    InputStream pricesf = new FileInputStream(initialFile4);
		    
		    final InputStream b1st = new ByteArrayInputStream(b1store.getBytes());
			final InputStream b2st = new ByteArrayInputStream(b2store.getBytes());
			final InputStream bookt = new ByteArrayInputStream(books.getBytes());
			final InputStream reportt = new ByteArrayInputStream(report.getBytes());
			
			session.execute("drop db mylist");
			session.execute("drop db prices");
			session.execute("drop db bstore");
			session.execute("drop db bstore1");
			session.execute("drop db bstore2");
			session.execute("drop db book");
			session.execute("drop db report");

			session.create("mylist", mylist);
			session.create("bstore", bstore);
			session.create("prices", pricesf);
			session.create("bstore1", b1st);
			session.create("bstore2", b2st);
			session.create("book", bookt);
			session.create("report", reportt);

			String input = load("ctree.xq");

			 
			try (Query query = session.query(input)) {		 
				 
				String option ="Y";
				while (query.more() && option.equals("Y")) {
					
					
					String next = query.next();		
					option = explore(session,next); 
					 				 
				}		 
				System.out.println(query.info());
			}
			
			 session.execute("drop db bstore1");
			 session.execute("drop db bstore2");
			 session.execute("drop db book");
			 session.execute("drop db report");
			 session.execute("drop db mylist");
			 session.execute("drop db bstore");
			 session.execute("drop db prices");
		}
		}
	

	
	public static String explore(BaseXClient session,String next)
	{  
		Scanner scanner = new Scanner(System. in);
		String question = "let $x:=" + next + "return $x/sc/node()";
		System.out.print("Can be ");
		Query qsc;
		String option = "Y";
		try {
			qsc = session.query(question);
			while(qsc.more()) {
				System.out.println(qsc.next());}
				String question2 = "let $x:=" + next + "return $x/values/node()";
				System.out.print(" equal to ");
				Query qvalues = session.query(question2);
				while(qvalues.more()) {
				System.out.print(qvalues.next());}			
				System.out.println("?");
				System.out.println("Question (Y/N):");	
				option  = scanner.nextLine();  
		        if (option.equals("N")) {
		        	
		        	String questionch = "let $x:=" + next + "return $x/question";
		        	Query qch;
		    		try {
		    			qch = session.query(questionch);
		    			String optionch ="Y";
		    			while(qch.more() && optionch.equals("Y")) {
		    				 String ch = qch.next();
		    				 optionch = explore(session,ch);
		    				 
		    			}
		    			if (optionch.equals("Y")) {System.out.println("Error in "+qsc.next());}
		    			
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    		}
		    		
		    		return option;
		        } else  return option;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return option;
		
        
		 
	}
	
}
