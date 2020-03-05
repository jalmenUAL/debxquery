package trash;

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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
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

import debxquery.debxquery.Debugger;
import trash.BaseXClient.Query;



//SELECT NO VARIABLE IN NESTED EXPRESSIONS IS NOT HANDLED
//SUBPROPERTY IS NOT HANDLED
//CYCLES

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
public class MyUI2 extends UI{
	
	 
	
	ComboBox<String> queries = new ComboBox<String>("Examples of Queries");
	
	public static String readStringFromURL(String requestURL) throws IOException {
		try (Scanner scanner = new Scanner(new URL(requestURL).openStream(), StandardCharsets.UTF_8.toString())) {
			scanner.useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : "";
		}
	}
	
	public static Tree<Label> Filetotree(String file)
	{
		Tree<Label> tree = new Tree<Label>();
		TreeData<Label> data = new TreeData<>();
		
		try {
	        File fXmlFile = new File(file);
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(fXmlFile);

	        Element root = doc.getDocumentElement();
	        String rootItem = root.getNodeName();
	        Label rootE = new Label(rootItem);
	        data.addItem(null,rootE);
	        
	        addChildrenToTree(data, root.getChildNodes(), rootE);
	    } catch (Exception e) { }
	    
	    
	    tree.setDataProvider(new TreeDataProvider<>(data));
		
		tree.setItemCaptionGenerator(new ItemCaptionGenerator<Label>() {
			private static final long serialVersionUID = -1913286695570843896L;

			 
			@Override
			public String apply(Label item) {
				// TODO Auto-generated method stub
				
				return item.getValue();
			}
		});
		
		tree.setStyleGenerator(item -> {
			 
	        if (!tree.getDataProvider().hasChildren(item))
	            return "leaf";
	        return "row";
	        
	    }
	   );
	return tree;
	};
	
	public static Tree<Label> XMLtotree(String xml)
	{
		Tree<Label> tree = new Tree<Label>();
		TreeData<Label> data = new TreeData<>();
		
		try {
	         
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(xml));
			Document doc = dBuilder.parse(is);
	        

	        Element root = doc.getDocumentElement();
	        String rootItem = root.getNodeName();
	        Label rootE = new Label(rootItem);
	        data.addItem(null,rootE);
	        
	        addChildrenToTree(data, root.getChildNodes(), rootE);
	    } catch (Exception e) { }
	    
	    
	    tree.setDataProvider(new TreeDataProvider<>(data));
		
		tree.setItemCaptionGenerator(new ItemCaptionGenerator<Label>() {
			private static final long serialVersionUID = -1913286695570843896L;

			 
			@Override
			public String apply(Label item) {
				// TODO Auto-generated method stub
				
				return item.getValue();
			}
		});
		
		tree.setStyleGenerator(item -> {
			 
	        if (!tree.getDataProvider().hasChildren(item))
	            return "leaf";
	        return "row";
	        
	    }
	   );
	return tree;
	};
	
	public static void addChildrenToTree(TreeData<Label> data, NodeList children, Label parent) {
	    if (children.getLength() > 0) {
	        for (int i = 0; i < children.getLength(); i++) {    	
	            Node node = children.item(i);   
	            if (node.getNodeType() == Node.TEXT_NODE)
	            {
	            	Text childElement = (Text) node;
	            	String child = childElement.getTextContent();
	            	Label childE = new Label(child);        	    
	        	    data.addItem(parent,childE);
	            }
	            else
	            if (node.getNodeType() == Node.ELEMENT_NODE) {
	            	
	        	    Element childElement = (Element) node;
	        	    String child = childElement.getTagName();        	    
	        	    Label childE = new Label(child); 
	        	    data.addItem(parent,childE);
	        	    if (node.hasChildNodes()) {
		            addChildrenToTree(data, node.getChildNodes(), childE);}
	        	  }             
	            }	        
	    }
	}
	
	

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		 
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
	    
		/*
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
	    */
		
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
		
		String input = Debugger.load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\q1.xq");
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
				
				session.execute("drop db bstore1");			
				File initialFile = new File("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\bstore1.xml");
				InputStream bstore1 = new FileInputStream(initialFile);
				session.create("bstore1", bstore1);
				
				String result = "";
				
				try (Query query = session.query(editor.getValue())) {		 
					 
					 
					while (query.more()) {
						
						
						String next = query.next();		
						result = result + next + "\n";
						
						 				 
					}		 
					resulte.setValue(result); 
					System.out.println(result);
					
				}
				session.execute("drop db bstore1");
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
					
					session.execute("drop db bstore1");			
					File initialFile = new File("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\bstore1.xml");
					InputStream bstore1 = new FileInputStream(initialFile);
					session.create("bstore1", bstore1);
					
					String ctree = Debugger.load("C:\\Users\\Administrator\\eclipse-workspace\\debxquery\\ctree.xq");
					
					final Window window = new Window("Debug Tree");
			        window.setWidth("100%");
			        window.setHeight("100%");
			        final VerticalLayout content = new VerticalLayout();
			        content.setWidth("100%");
			        content.setHeight("-1");
			        content.setMargin(true);
			        
					
					String result = "";
					
					try (Query query = session.query(ctree+"\n"
					+"<root>{local:naive_strategy(\""+editor.getValue()+"\")}</root>")) {		 
						while (query.more()) {							
							String next = query.next();		
							result = result + next + "\n";
						}	
						System.out.println(result);
						Tree<Label> tree = XMLtotree(result);
						TreeData<Label> data = tree.getTreeData();
						content.addComponent(tree);
						window.setContent(content);
						addWindow(window);
						
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				        DocumentBuilder dBuilder;
						try {
							 dBuilder = dbFactory.newDocumentBuilder();
							 Document doc;
							 InputSource is = new InputSource(new StringReader(result));
							 doc = dBuilder.parse(is);
							 Element root = doc.getDocumentElement();
							 
							    String option ="N";
							    
							    Integer i=0;
								while (i< root.getChildNodes().getLength() && option.equals("N")) {
									
									if (root.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE)
									{Element e = (Element) root.getChildNodes().item(i);
									option = explore(e); 
									}		
									i++;
								}		
							 
						
							 
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}					
					}
					session.execute("drop db bstore1");
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

	
	public static String explore(Element e)
	{  
		 
		System.out.print("Can be ");
		String option = "N";
		NodeList q = e.getElementsByTagName("p");
		if (q.getLength()>0) {System.out.print(((Element) q.item(0)).getTextContent());}
		else {NodeList sf = e.getElementsByTagName("sf"); if (sf.getLength()>0) {System.out.print(((Element) sf.item(0)).getTextContent());}
		}
	    System.out.print(" equal to ");
	    NodeList values = e.getElementsByTagName("values");
	    for (int i=0; i < values.getLength();i++) {
	    System.out.print(((Element)values.item(i)).getTextContent());
	    }
	    System.out.println("?");
		System.out.println("Question (Y/N/A):");	
		if (option.equals("N")) {
		NodeList questions = e.getElementsByTagName("question");
		Integer i=0;
		String optionch ="N";
		while (i< questions.getLength() && optionch.equals("N")) {
			if (e.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE)
			{
			Element ch = (Element) e.getChildNodes().item(i);
			optionch = explore(ch); }
			i++;
		}	
			
		if (optionch.equals("Y")) {System.out.println("Error in "+q.item(0)); return option;}
		}
		
		return option;
		
	}
	

	public void error(String type, String message) {
		Notification notif = new Notification(type, message, Notification.Type.ERROR_MESSAGE);
		notif.setDelayMsec(20000);
		notif.setPosition(Position.BOTTOM_RIGHT);
		notif.show(Page.getCurrent());
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI2.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
