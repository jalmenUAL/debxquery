package debxquery.debxquery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Position;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI{
	
	String choice="Yes";
	final Window window = new Window("Debug Tree");
	ComboBox<String> queries = new ComboBox<String>("Examples of Queries");
	
	public static String readStringFromURL(String requestURL) throws IOException {
		try (Scanner scanner = new Scanner(new URL(requestURL).openStream(), StandardCharsets.UTF_8.toString())) {
			scanner.useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : "";
		}
	}
	
	public  TreeGrid<NodeTree> XMLtotree(String xml)
	{
		TreeGrid<NodeTree> treeGrid = new TreeGrid<>();
        treeGrid.addColumn(NodeTree::getTag).setCaption("Debugging Questions").setId("name-column");
        treeGrid.addColumn(NodeTree::getValue).setCaption("Value");
        treeGrid.addComponentColumn(NodeTree::getSelection).setCaption("Please Select");	
		try {
	         
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(xml));
			Document doc = dBuilder.parse(is);
	        Element root = doc.getDocumentElement();       
	        List<NodeTree> listch = addChildrenToTree(root.getChildNodes());
	        treeGrid.setItems(listch,NodeTree::getSubNodes);
	    } catch (Exception e) { }
	   
	return treeGrid;
	};
	
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
	        	    	else if (tag.equals("values")) {sfp = new NodeTree("equal to",null,null);}
	        	    	else sfp = new NodeTree(tag,text,null);}	        	     
	        	    //if (node.hasChildNodes()) {
	        	    List<NodeTree> listch =addChildrenToTree(node.getChildNodes());
	        	    sfp.setSubNodes(listch);	        	    
	        	    //}
	        	    l.add(sfp);	        	    
	        	  }        
	            }  	        
	    }   
		return l;	   
	}
	
	

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		
		window.setWidth("100%");
	    window.setHeight("100%");		 
		final VerticalLayout main = new VerticalLayout();
		main.setMargin(false);		
		Image lab = new Image(null, new ThemeResource("banner-deb.png"));
	    lab.setWidth("100%");
		lab.setHeight("200px");		
		main.addComponent(lab);
		TabSheet tabsheet = new TabSheet();	
		VerticalLayout newTabLayout = new VerticalLayout();
		TextField name = new TextField("Database Name");
		name.setWidth("100%");
        AceEditor resultt = new AceEditor();
   		resultt.setHeight("300px");
   		resultt.setWidth("100%");
   		resultt.setFontSize("12pt");
   		resultt.setMode(AceMode.xml);
   		resultt.setTheme(AceTheme.eclipse);
   		resultt.setUseWorker(true);
   		resultt.setReadOnly(false);
   		resultt.setShowInvisibles(false);
   		resultt.setShowGutter(false);
   		resultt.setUseSoftTabs(false);
   		resultt.setShowPrintMargin(false);
   		resultt.setWordWrap(true);
   		newTabLayout.addComponent(name);
   		newTabLayout.addComponent(resultt);
		Tab tab = tabsheet.addTab(newTabLayout, "XML Document", null);		
		name.setValue("bstore1");
		String db = Debugger.load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\bstore1.xml");
		resultt.setValue(db);		
		tabsheet.addTab(new Label("XML Documents"),"+");
		tabsheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
	    @Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub
				 TabSheet tabsheet = event.getTabSheet();
			     Tab selectedTab = tabsheet.getTab(tabsheet.getSelectedTab());
			     if (selectedTab != null) {
			        if(selectedTab.getCaption().equals("+")){
			                VerticalLayout newTabLayout = new VerticalLayout();
			                TextField name = new TextField("Database Name");
			        		name.setWidth("100%");
			                AceEditor resultt = new AceEditor();
				       		resultt.setHeight("300px");
				       		resultt.setWidth("100%");
				       		resultt.setFontSize("12pt");
				       		resultt.setMode(AceMode.xml);
				       		resultt.setTheme(AceTheme.eclipse);
				       		resultt.setUseWorker(true);
				       		resultt.setReadOnly(false);
				       		resultt.setShowInvisibles(false);
				       		resultt.setShowGutter(false);
				       		resultt.setUseSoftTabs(false);
				       		resultt.setShowPrintMargin(false);
				       		resultt.setWordWrap(true);
				       		newTabLayout.addComponent(name);
				       		newTabLayout.addComponent(resultt);
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
				"Example5");
		queries.setEmptySelectionCaption("Please select a query:");
		queries.setWidth("100%");
		 
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
		String input = Debugger.load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\q1b.xq");
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
		AceEditor resultd = new AceEditor();
		resultd.setHeight("300px");
		resultd.setWidth("100%");
		resultd.setFontSize("12pt");
		resultd.setMode(AceMode.xml);
		resultd.setTheme(AceTheme.eclipse);
		resultd.setUseWorker(true);
		resultd.setReadOnly(false);
		resultd.setShowInvisibles(false);
		resultd.setShowGutter(false);
		resultd.setUseSoftTabs(false);
		resultd.setShowPrintMargin(false);
		resultd.setWordWrap(true);		
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
				// error("", "Empty Selection. Please select an ontology.");
			} else {			 
			}
		});
		run_button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) { 				
			try (BaseXClient session = new BaseXClient("localhost", 1984, "admin", "admin")) {			
				session.execute("drop db bstore");	
				session.execute("drop db prices");
				session.execute("drop db mylist");
				File initialFile = new File("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\bstore.xml");
				InputStream bstore = new FileInputStream(initialFile);
				session.create("bstore", bstore);	
				File initialFile2 = new File("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\prices.xml");
				InputStream prices = new FileInputStream(initialFile2);
				session.create("prices", prices);
				File initialFile3 = new File("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\mylist.xml");
				InputStream mylist = new FileInputStream(initialFile3);
				session.create("mylist", mylist);			
				String result = "";				
				try (Query query = session.query(editor.getValue())) {		 					 
					while (query.more()) {						
						String next = query.next();		
						result = result + next + "\n";					 				 
					}		 
					resulte.setValue(result); 
					System.out.println(result);				
				}
				session.execute("drop db bstore");
				session.execute("drop db prices");
				session.execute("drop db mylist");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			}			
		});

		debug_button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {				 
				try (BaseXClient session = new BaseXClient("localhost", 1984, "admin", "admin")) {
					session.execute("drop db bstore");	
					session.execute("drop db prices");
					session.execute("drop db mylist");
					File initialFile = new File("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\bstore.xml");
					InputStream bstore = new FileInputStream(initialFile);
					session.create("bstore", bstore);	
					File initialFile2 = new File("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\prices.xml");
					InputStream prices = new FileInputStream(initialFile2);
					session.create("prices", prices);
					File initialFile3 = new File("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\mylist.xml");
					InputStream mylist = new FileInputStream(initialFile3);
					session.create("mylist", mylist);
					String ctree = Debugger.load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\ctree.xq");				
					String result = "";				
					try (Query query = session.query(ctree+"\n"
					+"<root>{local:naive_strategy(\""+editor.getValue()+"\")}</root>")) {		 
						while (query.more()) {							
							String next = query.next();		
							result = result + next + "\n";
						}	
						System.out.println(result);
						TreeGrid<NodeTree> tree = XMLtotree(result);					
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				        DocumentBuilder dBuilder;
						try {						       
							    final VerticalLayout content = new VerticalLayout();
							    content.setWidth("100%");
						        content.setHeight("100%");
						        content.setMargin(true);
								content.addComponent(tree);
								tree.setWidth("100%");
								tree.setHeight("100%");
								window.setSizeFull();
								window.setContent(content);
								addWindow(window);							 
							    
								TreeData<NodeTree> nodeTree = tree.getTreeData();
								
								Integer size = nodeTree.getRootItems().size();
								List<NodeTree> rootItems = nodeTree.getRootItems();
								choice="Yes";
								 
							    selection(tree,rootItems,0,size,"Yes","main function");
							     
								    
								    
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}					
					}
					session.execute("drop db bstore");
					session.execute("drop db prices");
					session.execute("drop db mylist");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				
			}
		});
		
		main.setWidth("100%");
		main.addComponent(lab);
		main.addComponent(queries);
		main.addComponent(tabsheet); 
		main.addComponent(edS);
		main.addComponent(run_button);
		main.addComponent(resP);
		main.addComponent(debug_button);		 		 
		setContent(main);
		this.setSizeFull();
	}
	
	

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
				if (i==size-1) { if (parent.equals("Yes")) {Notification.show("No More Nodes Can Be Analyzed");
	 			}
	 			else {Notification.show("Error Found in "+nodeparent.replace(System.getProperty("line.separator"), ""));} } else 
					selection(tree,rootItems,i+1,size,parent,nodeparent);}
				
				if (event.getValue()=="No") {
					 
											List<NodeTree> children = rootItems.get(i).getSubNodes();
											selection(tree,children,0,children.size(),"No",item.getSubNodes().get(0).getValue());
											}
				if (event.getValue()=="Jump") 
				{if (i==size-1) {tree.collapse(item);if (parent.equals("Yes")) {Notification.show("No More Nodes Can Be Analyzed");}
				else {Notification.show("Error Found in "+nodeparent.replace(System.getProperty("line.separator"), ""));}} else selection(tree,rootItems,i+1,size,parent,nodeparent);}
				if (event.getValue()=="Abort") {tree.collapse(item);Notification.show("Debugging Aborted");}
				
			}});
		} else if (i==size-1) {if (parent.equals("Yes")) {Notification.show("No More Nodes Can Be Analyzed");
		 			}
		 			else {Notification.show("Error Found in "+nodeparent.replace(System.getProperty("line.separator"), ""));}} else selection(tree,rootItems,i+1,size,parent,nodeparent); 
		 
	}
	
	public void error(String type, String message) {
		Notification notif = new Notification(type, message, Notification.Type.ERROR_MESSAGE);
		notif.setDelayMsec(20000);
		notif.setPosition(Position.BOTTOM_RIGHT);
		notif.show(Page.getCurrent());
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
