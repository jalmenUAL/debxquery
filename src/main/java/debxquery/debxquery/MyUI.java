package debxquery.debxquery;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.IconGenerator;
import com.vaadin.ui.Image;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import trash.BaseXClient;
import trash.BaseXClient.Query;

@Theme("mytheme")
public class MyUI extends UI{
	
	
	Set<String> set = new HashSet<String>(); 
	TabSheet documents = new TabSheet();

	public  String load (String filename)
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
	} catch (IOException e) {
		error("Error",e.getMessage());
	}
	} catch (FileNotFoundException e) {
		error("Error",e.getMessage());
	}	
	return fileAsString;
	}
	
	public  TreeGrid<NodeTree> XMLtotree(String xml)
	{
		 
		TreeGrid<NodeTree> treeGrid = new TreeGrid<>();
		treeGrid.setSizeFull();
		
        treeGrid.addColumn(NodeTree::getTag).setCaption("Debugging Questions").setId("question");
        treeGrid.addColumn(NodeTree::getValue).setCaption("").setId("value");
         
      
        
        treeGrid.setStyleGenerator(rowReference ->
        {
          final String value = (String) rowReference.getTag();
          if (value.equals("Can be")) {return "canbe";}
          if (value.equals("equal to")) {return "equalto";}
          if (value.equals("on the path")) {return "onthepath";}
          if (value.equals("the function call")) {return "thefunctioncall";}
          if (value.equals("with arguments")) {return "witharguments";}
          return null;
        });
         
        treeGrid.addComponentColumn(NodeTree::getSelection).setCaption("Please Select");	
		try {    
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(xml));
			Document doc = dBuilder.parse(is);
	        Element root = doc.getDocumentElement();       
	        List<NodeTree> listch = addChildrenToTree(root.getChildNodes());
	        List<NodeTree> listatt = addAttributesToTree(root.getAttributes());
	        listch.addAll(listatt);
	        treeGrid.setItems(listch,NodeTree::getSubNodes);
	        
	    } catch (Exception e) { error("Error",e.getMessage()); }	   
	return treeGrid;
	};
	
	public Boolean leaf_node(Element element)
	{
		Boolean leaf = true;
		Integer i=0;
		while (leaf && i < element.getChildNodes().getLength())
		{
			if (element.getChildNodes().item(i).hasChildNodes()) {leaf = false;}
		    i++;
		}
    return leaf;		
	}	
	
	public String text_content(Element element)
	{
	  String content = "";
	  Integer i=0;
	  while (i < element.getChildNodes().getLength())
		{
			if (element.getChildNodes().item(i).getNodeName().equals("#text"))
			{content = content + element.getChildNodes().item(i).getNodeValue();}
		    i++;
		}
    return content;
	}
	
	public List<NodeTree> addAttributesToTree(NamedNodeMap atts) {
		
	    
		List<NodeTree> l = new ArrayList<NodeTree>();
		
		for (int i=0; i<atts.getLength();i++)
		{
			NodeTree sfp = null;
			String name = atts.item(i).getNodeName();
			String text = atts.item(i).getTextContent();
			if (name.equals("nc")) {}
			else {sfp = new NodeTree(name,text,null);
			l.add(sfp);}
			
		}
		return l;
	}
	
	public  List<NodeTree>  addChildrenToTree(NodeList children) {       
		List<NodeTree> l = new ArrayList<NodeTree>();
	    if (children.getLength() > 0) {  	
	        for (int i = 0; i < children.getLength(); i++) {    	     	
	            Node node = children.item(i);       
	            if (node.getNodeType() == Node.ELEMENT_NODE) { 	
	        	    Element Element = (Element) node;
	        	    String tag = Element.getTagName();        	    
	        	    String text = Element.getTextContent();  
	        	    if (text.equals(""))  text="()";
	        	    String content = text_content(Element);
	        	    if (set.contains(text)) {}
	        	    else
	        	    {
	        	    NodeTree sfp = null;
	        	    NodeTree sfp2 = null;  	     
	        	    if (tag.equals("question")) { sfp = new NodeTree(tag,null,null); set.add(text);}
	        	    else {
	        	    	if (tag.equals("p")) {if (leaf_node(Element)) {sfp = new NodeTree("Can be",text,null);} 
	        	    	else {
	        	         
	        	    	sfp = new NodeTree("Can be",null,null);
	        	    	sfp2 = new NodeTree("on the path",content,null);      	    	
	        	    	}}
	        	    	else
	        	    	if (tag.equals("sf")) {sfp = new NodeTree("Can be",null,null);}
	        	    	else
	        	    	if (tag.equals("fun")) {sfp = new NodeTree("the function call",text,null);}
	        	    	else
	        	    	if (tag.equals("args")) 
	        	    	{if (leaf_node(Element)) 
	        	    	{sfp = new NodeTree("with arguments",text,null);} 
	        	    	else 
	        	    	{sfp = new NodeTree("with arguments",null,null);}
	        	    	}
	        	    	else if (tag.equals("values")) { if (leaf_node(Element)) {
	        	    		{sfp = new NodeTree("equal to",text,null);}
	        	    		} 
	        	    	else {sfp = new NodeTree("equal to",null,null);}}    	    	
	        	    	else if (leaf_node(Element)) {sfp = new NodeTree(tag,text,null);} else {sfp = new NodeTree(tag,null,null);}}	        	             	     
	        	    	 List<NodeTree> listch =addChildrenToTree(node.getChildNodes());
	        	    	 List<NodeTree> listatt = addAttributesToTree(node.getAttributes());
	        		     listch.addAll(listatt);
	 	        	     sfp.setSubNodes(listch);
	 	        	     l.add(sfp);
	 	        	     if (!(sfp2==null)) { l.add(sfp2);}        	     
	            }   
	            }       
	            }  	        
	    }
		return l;	   
	}
	
	public VerticalLayout DocPanel(String file,String database) {
		
		VerticalLayout firstdocument = new VerticalLayout();
		//firstdocument.setSizeFull();
		HorizontalLayout dbl = new HorizontalLayout();
		dbl.setWidth("100%");
		Label dbn = new Label("Database Name");
		dbn.setWidth("100%");
		TextField name = new TextField();
		name.setWidth("100%");
		Button save = new Button("Save");
		save.setWidth("100%");
		
		dbl.addComponent(dbn);
   		dbl.addComponent(name);
   		dbl.addComponent(save);
   		dbl.setExpandRatio(dbn, 0.8f);
   		dbl.setExpandRatio(name,8.2f);
   		dbl.setExpandRatio(save,1f);
   		
		Panel xmld = new Panel();
		AceEditor xmldoc = new AceEditor();
	   	xmldoc.setHeight("300px");
	   	xmldoc.setWidth("100%");
	   	xmldoc.setFontSize("12pt");
	   	xmldoc.setMode(AceMode.xml);
	   	xmldoc.setTheme(AceTheme.eclipse);
	   	xmldoc.setUseWorker(true);
	   	xmldoc.setReadOnly(false);
	   	xmldoc.setShowInvisibles(false);
	   	xmldoc.setShowGutter(false);
	   	xmldoc.setUseSoftTabs(false);
	   	xmldoc.setShowPrintMargin(false);
	   	xmldoc.setWordWrap(true);
   		xmld.setContent(xmldoc);
   		
   		save.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) { 			
				
				if (name.getValue().equals("bstore1") || 
						name.getValue().equals("bstore") ||
						name.getValue().equals("pet") ||
						name.getValue().equals("owner") ||
						name.getValue().equals("petOwner") ||
						name.getValue().equals("mylist") ||
						name.getValue().equals("prices"))
				{error("Forbidden Operation","This database name is not allowed. Please rename.");}
				else
				{
				try (BaseXClient session = new BaseXClient("localhost", 1984, "admin", "admin")) {			
					session.execute("drop db "+name.getValue());	
					final InputStream code = new ByteArrayInputStream(xmldoc.getValue().getBytes());
					session.create(name.getValue(),code);
					Tab selectedTab = documents.getTab(documents.getSelectedTab());
					selectedTab.setCaption(name.getValue());
					print("Successful Operation","Database has been saved");
				} catch (IOException e) {
					error("Error",e.getMessage());
				}	
				}
			}			
		});		
   		
   		
   		name.setValue(database);
		if (!file.equals("")) {String db = load(file);xmldoc.setValue(db);firstdocument.setCaption(database);}	
		firstdocument.addComponent(dbl);
   		firstdocument.addComponent(xmld);
   		return firstdocument;
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {		
		
		VerticalLayout main = new VerticalLayout();
		VerticalLayout query = new VerticalLayout();
		VerticalLayout deb = new VerticalLayout();
		main.setMargin(false);				
		
		TabSheet main_tab = new TabSheet();
		main_tab.addTab(query,"Querying",null);
		main_tab.addTab(deb, "Debugging",null);
		main_tab.setSizeFull();
		main.addComponent(main_tab);	
		
		Image lab = new Image(null, new ThemeResource("banner-deb.png"));
	    lab.setWidth("100%");
		lab.setHeight("200px");		
		main.addComponent(lab);
		main.addComponent(main_tab);
		
		ComboBox<String> queries = new ComboBox<String>("Examples of Queries");
		ComboBox<String> strategies = new ComboBox<String>("Strategies");
		
		
        VerticalLayout firstdocument = 
        DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore1.xml","bstore1");
        VerticalLayout seconddocument = 
                DocPanel("","");
        documents.addTab(firstdocument, "bstore1", null);		
		documents.addTab(seconddocument,"+");
		
		documents.addSelectedTabChangeListener(new SelectedTabChangeListener() {
	    @Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				 TabSheet tabsheet = event.getTabSheet();
			     Tab selectedTab = tabsheet.getTab(tabsheet.getSelectedTab());
			     if (selectedTab != null) {
			        if(selectedTab.getCaption().equals("+")){	        	    
			                VerticalLayout newTabLayout = DocPanel("","");
			                Tab tab = tabsheet.addTab(newTabLayout, "New", null); 
			                int newPosition = tabsheet.getTabPosition(tab);
			                tabsheet.setTabPosition(tab,newPosition-1 );
			                tabsheet.setTabPosition(selectedTab,newPosition );         
			                tabsheet.setSelectedTab(tab);		       		
			               }
			       }}});		
	    queries.setItems("Example 1",
				"Example 2",
				"Example 3", "Example 4",
				"Example 5","Example 6");
		queries.setEmptySelectionCaption("Please select a query:");
		queries.setWidth("100%");
		
		strategies.setItems("Naive",
				"Paths First",
				"Functions First", "Only Functions",
				"Heaviest First",
				"Lightest Results First",
				"Divide and Query",
				"Heaviest Paths First",
				"Heaviest Functions First"
				);
		strategies.setEmptySelectionCaption("Please select an strategy:");
		strategies.setWidth("100%");
		
		AceEditor strategycode = new AceEditor();
		strategycode.setHeight("300px");
		strategycode.setWidth("100%");
		strategycode.setFontSize("12pt");
		strategycode.setMode(AceMode.xquery);
		strategycode.setTheme(AceTheme.eclipse);
		strategycode.setUseWorker(true);
		strategycode.setReadOnly(false);
		strategycode.setShowInvisibles(false);
		strategycode.setShowGutter(false);
		strategycode.setUseSoftTabs(false);
		strategycode.setShowPrintMargin(false);
		strategycode.setWordWrap(true);
		strategycode.setValue("function($x){$x}");
		
		
		 
		/*setErrorHandler(new ErrorHandler() {		  
		@Override public void error(com.vaadin.server.ErrorEvent event) {
		}});*/
		 	 
		Panel edS = new Panel("XQUERY Query");
		Panel resP = new Panel("Execution Result");		 
		edS.setSizeFull();
		resP.setSizeFull();		
		AceEditor editor = new AceEditor();
		editor.setHeight("300px");
		editor.setWidth("100%");
		editor.setFontSize("12pt");
		editor.setMode(AceMode.xquery);
		editor.setTheme(AceTheme.eclipse);
		editor.setUseWorker(true);
		editor.setReadOnly(false);
		editor.setShowInvisibles(false);
		editor.setShowGutter(false);
		editor.setUseSoftTabs(false);
		editor.setShowPrintMargin(false);
		editor.setWordWrap(true);		
		
		String input = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\q1.xq");
		editor.setValue(input);
		
		AceEditor resulte = new AceEditor();
		resulte.setHeight("300px");
		resulte.setWidth("100%");
		resulte.setFontSize("12pt");
		resulte.setMode(AceMode.xml);
		resulte.setTheme(AceTheme.eclipse);
		resulte.setUseWorker(true);
		resulte.setReadOnly(false);
		resulte.setShowInvisibles(false);
		resulte.setShowGutter(false);
		resulte.setUseSoftTabs(false);
		resulte.setShowPrintMargin(false);
		resulte.setWordWrap(true);		
		
		edS.setContent(editor);
		resP.setContent(resulte);
		
		Button run_button = new Button("Execute Query");
		run_button.setWidth("100%");
		run_button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		run_button.setIcon(VaadinIcons.PLAY);	
		
		Button debug_button = new Button("Debug Query");
		debug_button.setWidth("100%");
		debug_button.setStyleName(ValoTheme.BUTTON_PRIMARY);
		debug_button.setIcon(VaadinIcons.TOOLS);		
		 
		
		strategycode.addValueChangeListener(new com.vaadin.data.HasValue.ValueChangeListener<String>() {
			@Override
			public void valueChange(com.vaadin.data.HasValue.ValueChangeEvent<String> event) {			
	 		 if (event.getValue().equals("")) {debug_button.setEnabled(false);}				
			}
		});
		
		
		editor.addValueChangeListener(new com.vaadin.data.HasValue.ValueChangeListener<String>() {
			@Override
			public void valueChange(com.vaadin.data.HasValue.ValueChangeEvent<String> event) {			
	 		if (editor.getValue().isEmpty()){
				debug_button.setVisible(false);
				run_button.setVisible(false);}
				else {debug_button.setVisible(true);
				run_button.setVisible(true);}				
			}
		});
				 	
		queries.addValueChangeListener(event -> {
			if (event.getSource().isEmpty()) {
				error("", "Empty Selection. Please select a query.");
			} else {			 
				if (event.getValue().equals("Example 1")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example1-bug1.xq");
					editor.setValue(p);
				documents.removeAllComponents();
				documents.removeAllComponents();
				VerticalLayout doc1 = 
				DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\mylist.xml",
						"mylist");
				VerticalLayout doc2 = 
						DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore.xml",
								"bstore");
				VerticalLayout doc3 = 
						DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\prices.xml",
								"prices");
		   		documents.addTab(doc1, "mylist", null);
		   		documents.addTab(doc2, "bstore", null);
		   		documents.addTab(doc3, "prices", null);
				
				};
				if (event.getValue().equals("Example 2")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example2b.xq");
					editor.setValue(p);
					documents.removeAllComponents();
					VerticalLayout doc1 = 
							DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\owner.xml",
									"owner");
							VerticalLayout doc2 = 
									DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\petOwner.xml",
											"petOwner");
							VerticalLayout doc3 = 
									DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\pet.xml",
											"pet");
					   		documents.addTab(doc1, "owner", null);
					   		documents.addTab(doc2, "petOwner", null);
					   		documents.addTab(doc3, "pet", null);};
				if (event.getValue().equals("Example 3")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example3.xq");
					editor.setValue(p);	
					documents.removeAllComponents();
				VerticalLayout doc = 
						DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\prices.xml",
								"prices");
				   		documents.addTab(doc, "prices", null);};
				if (event.getValue().equals("Example 4")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example4.xq");
					editor.setValue(p);
					documents.removeAllComponents();
				VerticalLayout doc = 
						DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore1.xml",
								"bstore1");
				   		documents.addTab(doc, "bstore1", null);};
				if (event.getValue().equals("Example 5")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example5.xq");
					editor.setValue(p);
					documents.removeAllComponents();
				VerticalLayout doc = 
						DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore1.xml",
								"bstore1");
				   		documents.addTab(doc, "bstore1", null);};
				if (event.getValue().equals("Example 6")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example6.xq");
					editor.setValue(p);
					documents.removeAllComponents();
				VerticalLayout doc = 
						DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore1.xml",
								"bstore1");
				   		documents.addTab(doc, "bstore1", null);};
			}
		});
		
		strategies.addValueChangeListener(event -> {
			if (event.getSource().isEmpty()) {
				error("", "Empty Selection. Please select an strategy.");
			} else {	
				if (event.getValue().equals("Naive")) { strategycode.setValue("function($x){$x}");};
				if (event.getValue().equals("Paths First")) {strategycode.setValue(
						"function($x){($x[p],$x[sf])}");};
				if (event.getValue().equals("Functions First")) {strategycode.setValue("function($x){($x[sf],$x[p])}");};
				if (event.getValue().equals("Only Functions")) {strategycode.setValue("function($x){($x[sf])}");};
				if (event.getValue().equals("Heaviest First")) {strategycode.setValue("function($x){for $ch in $x "
						+ "order by count($ch//question) descending "
						+ "return $ch}");};
				if (event.getValue().equals("Lightest Results First")) {strategycode.setValue("function($x){for $ch in $x "
						+ "order by count($ch//question) descending "
						+ "return $ch}");};
				if (event.getValue().equals("Divide and Query")) {strategycode.setValue("function($x){\r\n" + 
						"let $w := count($x//question)\r\n" + 
						"let $m := $w div 2\r\n" + 
						"let $bigger := \r\n" + 
						"(for $n in $x where count($n//question) > $m\r\n" + 
						"order by count($n//question) descending return $n)\r\n" + 
						"let $smaller := \r\n" + 
						"(for $n in $x where count($n//question) <= $m\r\n" + 
						"order by count($n//question) descending\r\n" + 
						"return $n)\r\n" + 
						"let $pivot := head($smaller)\r\n" + 
						"let $rest := tail($smaller)\r\n" + 
						"return ($pivot,$rest,$bigger) \r\n" + 
						"}");};
				if (event.getValue().equals("Heaviest Paths First")) {strategycode.setValue("function($x){for $ch in $x "
						+ "order by count($ch//sf) descending "
						+ "return $ch}");};
				if (event.getValue().equals("Heaviest Functions First")) {strategycode.setValue("function($x){for $ch in $x "
						+ "order by count($ch//p) descending "
						+ "return $ch}");};
			}
		});
		
		
		run_button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) { 				
			try (BaseXClient session = new BaseXClient("localhost", 1984, "admin", "admin")) {					
				String result = "";				
				try (Query query = session.query(editor.getValue())) {		 					 
					while (query.more()) {						
						String next = query.next();		
						result = result + next + "\n";					 				 
					}		 
					resulte.setValue(result); 
					System.out.println(result);				
				}
			} catch (IOException e) {
				error("Error",e.getMessage());
			}		
			}			
		});

		debug_button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {				 
				try (BaseXClient session = new BaseXClient("localhost", 1984, "admin", "admin")) {
					String ctree = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\ctree.xq");				
					String result = "";				
					try (Query query = session.query(ctree+"\n"
					+"<root>{local:treecalls("+strategycode.getValue()+",\""+editor.getValue().replace("\"","'")+"\")}</root>")) {		 
						while (query.more()) {							
							String next = query.next();		
							result = result + next + "\n";
						}	
						System.out.println(result);
						TreeGrid<NodeTree> tree = XMLtotree(result);		
						tree.setSizeFull();
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				        DocumentBuilder dBuilder;
						try {						
							    set.clear();
								tree.setHeight("800px");
								deb.removeAllComponents();
								deb.addComponent(tree);
								main_tab.setSelectedTab(1);					    
								TreeData<NodeTree> nodeTree = tree.getTreeData();
								Integer size = nodeTree.getRootItems().size();
								if (size>0) {
								List<NodeTree> rootItems = nodeTree.getRootItems();
							    selection(tree,rootItems,0,size,"Yes","main function");	}
								else {print("Debugging error","The set of nodes is empty. Please select another strategy.");}
						} catch (Exception e) {
							error("Error",e.getMessage());
						}					
					}
					 
				} catch (IOException e) {
					error("Error",e.getMessage());
				}		
				
			}
		});
		
		query.setWidth("100%");
		query.setHeight("100%");
		deb.setWidth("100%");
		deb.setHeight("100%");
		query.addComponent(queries);
		query.addComponent(documents); 
		query.addComponent(edS);
		query.addComponent(debug_button);
		query.addComponent(strategies);
		query.addComponent(strategycode);
		query.addComponent(run_button);
		query.addComponent(resP);				 		 
		setContent(main);
		this.setSizeFull();
	}
	
	

	@SuppressWarnings("unchecked")
	public void selection(TreeGrid tree, List<NodeTree> rootItems,Integer i,Integer size,String parent,String nodeparent)
	{		 	
			
		if (rootItems.get(i).getTag().equals("question")) 
		{
	    List<String> data = Arrays.asList("Yes", "No", "Abort");
		RadioButtonGroup selection = new RadioButtonGroup();
        selection = new RadioButtonGroup<String>("Select an option", data);
        selection.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        selection.setItemCaptionGenerator(item ->"");
        selection.setItemIconGenerator((IconGenerator) item -> {
        	
        	if (item.equals("Yes")) {					        	
        	return VaadinIcons.CHECK;}
        	else if (item.equals("No")) {						            	
            	return VaadinIcons.BUG;}      	 
        	else if (item.equals("Abort")) {						            	
            	return VaadinIcons.EXIT;}
        	else return null;
        	});			
           
			NodeTree item = rootItems.get(i);
			item.setSelection(selection);
	
			tree.select(item);
		    tree.expand(item);	
		     
		    
		    for (int j=0; j < item.getSubNodes().size();j++)
		    {
		    	if (item.getSubNodes().get(j).hasTag("Can be") ||
		    			 
		    			item.getSubNodes().get(j).hasTag("equal to"))
		    			{
		    		     tree.expand(item.getSubNodes().get(j));
		    	         if (item.getSubNodes().get(j).hasSubNodes()) {tree.expand(item.getSubNodes().get(j).getSubNodes());}
		    	        }
		    }
		 
		    
		 selection.addValueChangeListener(new ValueChangeListener() {         
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				item.removeSelection();
				//
				if (event.getValue()=="Yes") {
				tree.collapse(item); 
				if (i==size-1) { 
					if (parent.equals("Yes")) {print("Debugging result","No More Nodes Can Be Analyzed");}
				    if (parent.equals("No")) {print("Debugging result", 
				    		"Error Found in "+nodeparent.replace(System.getProperty("line.separator"), ""));
					} 
				}
				
				else 
				
	 				selection(tree,rootItems,i+1,size,parent,nodeparent);
				
				} 
				else			
				if (event.getValue()=="No") {				 
						List<NodeTree> children = rootItems.get(i).getSubNodes();
						String nodeparent = "";
						if (!(rootItems.get(i).getSubNodes().get(0).getValue()==null)) 
									{nodeparent= rootItems.get(i).getSubNodes().get(0).getValue();}
						else 
						if (rootItems.get(i).getSubNodes().get(1).getTag().equals("on the path")) 
							 
							{nodeparent = rootItems.get(i).getSubNodes().get(1).getValue();}
						
						else nodeparent = rootItems.get(i).getSubNodes().get(0).getSubNodes().get(0).getValue();
						selection(tree,children,0,children.size(),"No",nodeparent);
											}
				
				else
				if (event.getValue()=="Abort") {
					tree.collapse(item);
					print("Debugging result","Debugging Aborted");
					}
				
			    }
		 });
		 } 
		        else 
		        	if (i==size-1) {
		        		if (parent.equals("Yes")) 
		        		{print("Debugging result","No More Nodes Can Be Analyzed");
		 			    }
		 			    else {
		 			    	print("Debugging result","Error Found in "+nodeparent.replace(System.getProperty("line.separator"), ""));
		 			    	}
		        		} else 
		        			selection(tree,rootItems,i+1,size,parent,nodeparent); 
		 
	}
	
	public void print(String type, String message) {
		Notification notif = new Notification(type, message, Notification.Type.ERROR_MESSAGE);
		notif.setDelayMsec(20000);
		notif.setPosition(Position.MIDDLE_CENTER);
		notif.show(Page.getCurrent());
	}
	
	public void error(String type, String message) {
		Notification notif = new Notification(type, message, Notification.Type.ERROR_MESSAGE);
		notif.setDelayMsec(20000);
		notif.setPosition(Position.MIDDLE_CENTER);
		notif.show(Page.getCurrent());
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
