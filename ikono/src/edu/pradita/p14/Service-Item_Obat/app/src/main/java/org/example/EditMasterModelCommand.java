package org.example;

public class EditMasterModelCommand implements Command {
    private final MasterModelDao dao;
    private final MasterModel itemToUpdate;

    public EditMasterModelCommand(MasterModelDao dao, MasterModel itemToUpdate) {
        this.dao = dao;
        this.itemToUpdate = itemToUpdate;
    }

    @Override
    public void execute() {
        dao.update(itemToUpdate);
        System.out.println("[COMMAND] EditMasterModelCommand dieksekusi untuk ID: " + itemToUpdate.getId());
    }
}