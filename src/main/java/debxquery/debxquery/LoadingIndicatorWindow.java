package debxquery.debxquery;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class LoadingIndicatorWindow extends Window {

public LoadingIndicatorWindow() {
    center();
    setVisible(true);
    setResizable(false);
    setDraggable(false);
    setModal(true);
    setClosable(false);
    setCaption("Loading");

    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    layout.setWidth("100%");

    Label progressLabel = new Label("Please wait! Data loading in progress...");
    progressLabel.setSizeFull();

    ProgressBar progressBar = new ProgressBar();
    progressBar.setSizeFull();
    progressBar.setIndeterminate(true);
    progressBar.setVisible(true);

    layout.addComponent(progressLabel);
    layout.addComponent(progressBar);

    layout.setComponentAlignment(progressLabel, Alignment.MIDDLE_CENTER);
    layout.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
    setContent(layout);
}
}