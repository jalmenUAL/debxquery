package debxquery.debxquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.themes.ValoTheme;

class NodeTree {
	 
    private List<NodeTree> subNodes = new ArrayList<>();
    private String tag;
    private String value;
    private RadioButtonGroup selection;

    public NodeTree(String tag, String value,RadioButtonGroup selection) {
        this.tag = tag;
        this.value= value;
        this.selection = selection;
        
         
         
    }
    
    public void setSelection(RadioButtonGroup selection)
    { 
    	this.selection = selection;
    }
    
    public void removeSelection()
    { 
    	this.selection = null;
    }

    public String getTag() {
        return tag;
    }
    
    public String getValue() {
        return value;
    }
    
    public RadioButtonGroup getSelection()
    {
    	return selection;
    }

    public List<NodeTree> getSubNodes() {
        return subNodes;
    }

    public void setSubNodes(List<NodeTree> subNodes) {
        this.subNodes = subNodes;
    }

    public void addSubNode(NodeTree subNode) {
        subNodes.add(subNode);
    }

    

     
}
