package org.example;

public class DeleteMasterModelCommand implements Command {
    private final MasterModelDao dao;
    private final MasterModel itemToDelete;

    public DeleteMasterModelCommand(MasterModelDao dao, MasterModel itemToDelete) {
        this.dao = dao;
        this.itemToDelete = itemToDelete;
    }

    @Override
    public void execute() {
        dao.delete(itemToDelete);
        System.out.println("[COMMAND] DeleteMasterModelCommand dieksekusi untuk ID: " + itemToDelete.getId());
    }
}