package debxquery.debxquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.ui.RadioButtonGroup;

class NodeTree {
	 
    private List<NodeTree> subNodes = new ArrayList<>();
    private String tag;
    private String value;
    private RadioButtonGroup selection;

    public NodeTree(String tag, String value) {
        this.tag = tag;
        this.value= value;
        List<Integer> data = Arrays.asList(0, 1, 2, 3, 4, 5);
        
        selection = new RadioButtonGroup<>("Select an option", data);
        selection.setItemCaptionGenerator(item -> "Option " + item);
        selection.setSelectedItem(data.get(2));
         
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
