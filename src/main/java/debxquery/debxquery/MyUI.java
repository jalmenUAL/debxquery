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
import java.util.List;
import java.util.Scanner;

import javax.servlet.annotation.WebServlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

import debxquery.debxquery.BaseXClient.Query;

@Theme("mytheme")
public class MyUI extends UI{

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
        treeGrid.addColumn(NodeTree::getTag).setCaption("Debugging Questions").setId("name-column");
        treeGrid.addColumn(NodeTree::getValue).setCaption("");
        treeGrid.addComponentColumn(NodeTree::getSelection).setCaption("Please Select");	
		try {    
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(xml));
			Document doc = dBuilder.parse(is);
	        Element root = doc.getDocumentElement();       
	        List<NodeTree> listch = addChildrenToTree(root.getChildNodes());
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
	public  List<NodeTree>  addChildrenToTree(NodeList children) {       
		List<NodeTree> l = new ArrayList<NodeTree>();
	    if (children.getLength() > 0) {  	
	        for (int i = 0; i < children.getLength(); i++) {    	     	
	            Node node = children.item(i);       
	            if (node.getNodeType() == Node.ELEMENT_NODE) { 	
	        	    Element Element = (Element) node;
	        	    String tag = Element.getTagName();        	    
	        	    String text = Element.getTextContent();
	        	    NodeTree sfp = null;
	        	    if (tag.equals("question")) { sfp = new NodeTree(tag,null,null);}
	        	    else {
	        	    	if (tag.equals("p") || tag.equals("sf")) {sfp = new NodeTree("Can be",text,null);}
	        	    	else if (tag.equals("values")) { if (leaf_node(Element)) {sfp = new NodeTree("equal to",text,null);} 
	        	    	else {sfp = new NodeTree("equal to",null,null);}}    	    	
	        	    	else if (leaf_node(Element)) {sfp = new NodeTree(tag,text,null);} else {sfp = new NodeTree(tag,null,null);}}	        	     
	        	    
	        	    List<NodeTree> listch =addChildrenToTree(node.getChildNodes());
	        	    sfp.setSubNodes(listch);	        	           	    
	        	    l.add(sfp);	        	    
	        	  }        
	            }  	        
	    }   
		return l;	   
	}
	
	public VerticalLayout DocPanel(String file,String database) {
		
		VerticalLayout firstdocument = new VerticalLayout();
		firstdocument.setSizeFull();
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
   		dbl.setExpandRatio(dbn, 1f);
   		dbl.setExpandRatio(name,7f);
   		dbl.setExpandRatio(save,2f);
		Panel xmld = new Panel("XML Document");
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
				try (BaseXClient session = new BaseXClient("localhost", 1984, "admin", "admin")) {			
					session.execute("drop db "+name.getValue());	
					final InputStream code = new ByteArrayInputStream(xmldoc.getValue().getBytes());
					session.create(name.getValue(),code);
					print("Successful Operation","Database has been saved");
				} catch (IOException e) {
					error("Error",e.getMessage());
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
		TabSheet documents = new TabSheet();
        VerticalLayout firstdocument = DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore1.xml","bstore1");
   		documents.addTab(firstdocument, "XML Document", null);	
		documents.addTab(null,"+");
		documents.addSelectedTabChangeListener(new SelectedTabChangeListener() {
	    @Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				 TabSheet tabsheet = event.getTabSheet();
			     Tab selectedTab = tabsheet.getTab(tabsheet.getSelectedTab());
			     if (selectedTab != null) {
			        if(selectedTab.getCaption().equals("+")){	        	    
			                VerticalLayout newTabLayout = DocPanel("","");
			                Tab tab = tabsheet.addTab(newTabLayout, "XML Document", null); 
			                int newPosition = tabsheet.getTabPosition(tab);
			                tabsheet.setTabPosition(tab,newPosition-1 );
			                tabsheet.setTabPosition(selectedTab,newPosition );         
			                tabsheet.setSelectedTab(tab);		       		
			               }
			       }}});		
	    queries.setItems("Example1",
				"Example2",
				"Example3", "Example4",
				"Example5","Example6");
		queries.setEmptySelectionCaption("Please select a query:");
		queries.setWidth("100%");
		
		strategies.setItems("Strategy1",
				"Strategy2",
				"Strategy3", "Strategy4",
				"Strategy5");
		strategies.setEmptySelectionCaption("Please select an strategy:");
		strategies.setWidth("100%");
		
		TextField strategycode = new TextField("Strategy Code");
		strategycode.setWidth("100%");
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
				if (event.getValue().equals("Example1")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example1.xq");
					editor.setValue(p);
				documents.removeAllComponents();
				VerticalLayout doc = 
				DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore1.xml",
						"bstore1");
		   		documents.addTab(doc, "XML Document", null);};
				if (event.getValue().equals("Example2")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example2.xq");
					editor.setValue(p);
					documents.removeAllComponents();
				VerticalLayout doc = 
						DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore1.xml",
								"bstore1");
				   		documents.addTab(doc, "XML Document", null);};
				if (event.getValue().equals("Example3")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example3.xq");
					editor.setValue(p);	
					documents.removeAllComponents();
				VerticalLayout doc = 
						DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore1.xml",
								"bstore1");
				   		documents.addTab(doc, "XML Document", null);};
				if (event.getValue().equals("Example4")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example4.xq");
					editor.setValue(p);
					documents.removeAllComponents();
				VerticalLayout doc = 
						DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore1.xml",
								"bstore1");
				   		documents.addTab(doc, "XML Document", null);};
				if (event.getValue().equals("Example5")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example5.xq");
					editor.setValue(p);
					documents.removeAllComponents();
				VerticalLayout doc = 
						DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore1.xml",
								"bstore1");
				   		documents.addTab(doc, "XML Document", null);};
				if (event.getValue().equals("Example6")) {
					String p = load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\example6.xq");
					editor.setValue(p);
					documents.removeAllComponents();
				VerticalLayout doc = 
						DocPanel("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\src\\main\\webapp\\VAADIN\\themes\\mytheme\\bstore1.xml",
								"bstore1");
				   		documents.addTab(doc, "XML Document", null);};
			}
		});
		
		strategies.addValueChangeListener(event -> {
			if (event.getSource().isEmpty()) {
				error("", "Empty Selection. Please select an strategy.");
			} else {	
				if (event.getValue().equals("Strategy1")) { strategycode.setValue("function($x){$x}");};
				if (event.getValue().equals("Strategy2")) {strategycode.setValue("function($x){for $ch in $x order by count($ch/values/node())\r\n" + 
						"ascending return $ch}");};
				if (event.getValue().equals("Strategy3")) {strategycode.setValue("function($x){($x[p],$x[not(p)])}");};
				if (event.getValue().equals("Strategy4")) {strategycode.setValue("function($x){for $ch in $x order by $ch/@nc descending return $ch  }");};
				if (event.getValue().equals("Strategy5")) {strategycode.setValue("function($x){(for $ch in $x where $ch[p] order by\r\n" + 
						"  count($ch/values/node()) ascending return $ch,for $ch in $x where $ch[not(p)] order by\r\n" + 
						"  count($ch/values/node()) ascending return $ch)}");};
				
				
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
					+"<root>{local:treecalls("+strategycode.getValue()+",\""+editor.getValue()+"\")}</root>")) {		 
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
								tree.setHeight("800px");
								deb.removeAllComponents();
								deb.addComponent(tree);
								main_tab.setSelectedTab(1);					    
								TreeData<NodeTree> nodeTree = tree.getTreeData();
								Integer size = nodeTree.getRootItems().size();
								List<NodeTree> rootItems = nodeTree.getRootItems();
							    selection(tree,rootItems,0,size,"Yes","main function");						    
						} catch (Exception e) {
							error("Error",e.getMessage());
						}					
					}
					session.execute("drop db bstore");
					session.execute("drop db prices");
					session.execute("drop db mylist");
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
		if (rootItems.get(i).getTag().equals("question")) {
	    List<String> data = Arrays.asList("Yes", "No", "Jump", "Abort");
		RadioButtonGroup selection = new RadioButtonGroup();
        selection = new RadioButtonGroup<String>("Select an option", data);
        selection.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        selection.setItemCaptionGenerator(item ->"");
        selection.setItemIconGenerator((IconGenerator) item -> {
        	if (item.equals("Yes")) {					        	
        	return VaadinIcons.CHECK;}
        	else if (item.equals("No")) {						            	
            	return VaadinIcons.BUG;}
        	else if (item.equals("Jump")) {						            	
            	return VaadinIcons.TIME_FORWARD;}
        	else if (item.equals("Abort")) {						            	
            	return VaadinIcons.EXIT;}
        	else return null;
        	});									
			NodeTree item = rootItems.get(i);
			item.setSelection(selection);
	
			tree.select(item);
		    tree.expand(item);	
		    
		    for (int j=0; j < 2;j++)
		    {
		    	tree.expand(item.getSubNodes().get(j));
		    }
		 
		 selection.addValueChangeListener(new ValueChangeListener() {         
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				item.removeSelection();
				//
				if (event.getValue()=="Yes") {tree.collapse(item); 
				if (i==size-1) { if (parent.equals("Yes")) {print("Debugging result","No More Nodes Can Be Analyzed");
	 			}
	 			else {print("Debugging result","Error Found in "+nodeparent.replace(System.getProperty("line.separator"), ""));} } else 
					selection(tree,rootItems,i+1,size,parent,nodeparent);}			
				if (event.getValue()=="No") {				 
											List<NodeTree> children = rootItems.get(i).getSubNodes();
											selection(tree,children,0,children.size(),"No",item.getSubNodes().get(0).getValue());
											}
				if (event.getValue()=="Jump") 
				{if (i==size-1) {tree.collapse(item);if (parent.equals("Yes")) {print("Debugging result","No More Nodes Can Be Analyzed");}
				else {print("Debugging result","Error Found in "+nodeparent.replace(System.getProperty("line.separator"), ""));}} else selection(tree,rootItems,i+1,size,parent,nodeparent);}
				if (event.getValue()=="Abort") {tree.collapse(item);print("Debugging result","Debugging Aborted");}
				
			}});
		} else if (i==size-1) {if (parent.equals("Yes")) {print("Debugging result","No More Nodes Can Be Analyzed");
		 			}
		 			else {print("Debugging result","Error Found in "+nodeparent.replace(System.getProperty("line.separator"), ""));}} else selection(tree,rootItems,i+1,size,parent,nodeparent); 
		 
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
