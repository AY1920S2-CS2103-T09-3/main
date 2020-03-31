package seedu.expensela.model;

/**
 * ToggleListOrChart class to toggle between viewing transaction list or chart analytics
 */
public class ToggleView {

    private boolean isViewList = true;

    public boolean getIsViewList() {
        return this.isViewList;
    }

    public void switchIsViewList() {
        this.isViewList = !this.isViewList;
    }
}
